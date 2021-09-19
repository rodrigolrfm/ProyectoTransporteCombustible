import genetico.Pair;
import java.util.*;
import java.io.*;
import java.time.Clock;

public class Population implements Closeable
{

   private Params params; // Problem parameters
   private Split split; // Split algorithm
   private LocalSearch localSearch; // Local search structure
   private ArrayList<Individual> feasibleSubpopulation = new ArrayList<Individual>(); // Feasible subpopulation, kept ordered by increasing penalized cost
   private ArrayList<Individual> infeasibleSubpopulation = new ArrayList<Individual>(); // Infeasible subpopulation, kept ordered by increasing penalized cost
   private LinkedList<Boolean> listFeasibilityLoad = new LinkedList<Boolean>(); // Load feasibility of the last 100 individuals generated by LS
   private LinkedList<Boolean> listFeasibilityDuration = new LinkedList<Boolean>(); // Duration feasibility of the last 100 individuals generated by LS
   private ArrayList< Pair<Long, Double>> searchProgress = new ArrayList<Pair<Long, Double>>(); // Keeps tracks of the time stamps of successive best solutions
   private Individual bestSolutionRestart = new Individual(); // Best solution found during the current restart of the algorthm
   private Individual bestSolutionOverall = new Individual(); // Best solution found during the complete execution of the algorithm

   // Evaluates the biased fitness of all individuals in the population
   private void updateBiasedFitnesses(ArrayList<Individual> pop)
   {
	   // Ranking the individuals based on their diversity contribution (decreasing order of distance)
	   ArrayList<Pair<Double, Integer>> ranking = new ArrayList<Pair<Double, Integer>>();
	   for (int i = 0 ; i < (int)pop.size(); i++)
	   {
		   ranking.add(new Pair<Double, Integer>(-pop.get(i).averageBrokenPairsDistanceClosest(params.nbClose),i));
	   }
	   Collections.sort(ranking);

	   // Updating the biased fitness values
	   if (pop.size() == 1)
	   {
		   pop.get(0).biasedFitness = 0;
	   }
	   else
	   {
		   for (int i = 0; i < (int)pop.size(); i++)
		   {
			   double divRank = (double)i / (double)(pop.size() - 1); // Ranking from 0 to 1
			   double fitRank = (double)ranking.get(i).second / (double)(pop.size() - 1);
			   if ((int)pop.size() <= params.nbElite) // Elite individuals cannot be smaller than population size
			   {
				   pop.get(ranking.get(i).second).biasedFitness = fitRank;
			   }
			   else
			   {
				   pop.get(ranking.get(i).second).biasedFitness = fitRank + (1.0 - (double)params.nbElite / (double)pop.size()) * divRank;
			   }
		   }
	   }
   }

   // Removes the worst individual in terms of biased fitness
   private void removeWorstBiasedFitness(ArrayList<Individual> pop)
   {
	   updateBiasedFitnesses(pop);
	   if (pop.size() <= 1)
	   {
		   throw new RuntimeException("Eliminating the best individual: this should not occur in HGS");
	   }

	   Individual worstIndividual = null;
	   int worstIndividualPosition = -1;
	   boolean isWorstIndividualClone = false;
	   double worstIndividualBiasedFitness = -1.e30;
	   for (int i = 1; i < (int)pop.size(); i++)
	   {
		   boolean isClone = (pop.get(i).averageBrokenPairsDistanceClosest(1) < DefineConstants.MY_EPSILON); // A distance equal to 0 indicates that a clone exists
		   if ((isClone && !isWorstIndividualClone) || (isClone == isWorstIndividualClone && pop.get(i).biasedFitness > worstIndividualBiasedFitness))
		   {
			   worstIndividualBiasedFitness = pop.get(i).biasedFitness;
			   isWorstIndividualClone = isClone;
			   worstIndividualPosition = i;
			   worstIndividual = pop.get(i);
		   }
	   }

	   pop.remove(worstIndividualPosition); // Removing the individual from the population
	   for (Individual myIndividual2 : pop)
	   {
		   myIndividual2.removeProximity(worstIndividual); // Cleaning its distances from the other individuals in the population
	   }
	   worstIndividual = null; // Freeing memory
   }


   // Creates an initial population of individuals
   public final void generatePopulation()
   {
	   for (int i = 0; i < 4 * params.mu; i++)
	   {
		   Individual randomIndiv = new Individual(params);
		   split.generalSplit(randomIndiv, params.nbVehicles);
		   localSearch.run(randomIndiv, params.penaltyCapacity, params.penaltyDuration);
		   addIndividual(randomIndiv, true);
		   if (!randomIndiv.isFeasible && tangible.RandomNumbers.nextNumber() % 2 == 0) // Repair half of the solutions in case of infeasibility
		   {
			   localSearch.run(randomIndiv, params.penaltyCapacity * 10.0, params.penaltyDuration * 10.0);
			   if (randomIndiv.isFeasible)
			   {
				   addIndividual(randomIndiv, false);
			   }
		   }
		   randomIndiv = null;
	   }
   }

   // Add an individual in the population (survivor selection is automatically triggered whenever the population reaches its maximum size)
   // Returns TRUE if a new best solution of the run has been found
   public final boolean addIndividual(Individual indiv, boolean updateFeasible)
   {
	   if (updateFeasible)
	   {
		   listFeasibilityLoad.addLast(indiv.myCostSol.capacityExcess < DefineConstants.MY_EPSILON);
		   listFeasibilityDuration.addLast(indiv.myCostSol.durationExcess < DefineConstants.MY_EPSILON);
		   listFeasibilityLoad.removeFirst();
		   listFeasibilityDuration.removeFirst();
	   }

	   // Find the adequate subpopulation in relation to the individual feasibility
	   ArrayList<Individual> subpop = (indiv.isFeasible) ? feasibleSubpopulation : infeasibleSubpopulation;

	   // Create a copy of the individual and updade the proximity structures calculating inter-individual distances
	   Individual myIndividual = new Individual(indiv);
	   for (Individual myIndividual2 : subpop)
	   {
		   double myDistance = myIndividual.brokenPairsDistance(myIndividual2);
		   myIndividual2.indivsPerProximity.insert({myDistance, myIndividual});
		   myIndividual.indivsPerProximity.insert({myDistance, myIndividual2});
	   }

	   // Identify the correct location in the population and insert the individual
	   int place = (int)subpop.size();
	   while (place > 0 && subpop.get(place - 1).myCostSol.penalizedCost > indiv.myCostSol.penalizedCost - DefineConstants.MY_EPSILON)
	   {
		   place--;
	   }
	   subpop.emplace(subpop.iterator() + place, myIndividual);

	   // Trigger a survivor selection if the maximimum population size is exceeded
	   if ((int)subpop.size() > params.mu + params.lambda)
	   {
		   while ((int)subpop.size() > params.mu)
		   {
			   removeWorstBiasedFitness(subpop);
		   }
	   }

	   // Track best solution
	   if (indiv.isFeasible && indiv.myCostSol.penalizedCost < bestSolutionRestart.myCostSol.penalizedCost - DefineConstants.MY_EPSILON)
	   {
//C++ TO JAVA CONVERTER TODO TASK: The following line was determined to be a copy assignment (rather than a reference assignment) - this should be verified and a 'copyFrom' method should be created:
//ORIGINAL LINE: bestSolutionRestart = *indiv;
		   bestSolutionRestart.copyFrom(indiv);
		   if (indiv.myCostSol.penalizedCost < bestSolutionOverall.myCostSol.penalizedCost - DefineConstants.MY_EPSILON)
		   {
//C++ TO JAVA CONVERTER TODO TASK: The following line was determined to be a copy assignment (rather than a reference assignment) - this should be verified and a 'copyFrom' method should be created:
//ORIGINAL LINE: bestSolutionOverall = *indiv;
			   bestSolutionOverall.copyFrom(indiv);
			   searchProgress.add(new genetico.Pair<clock_t, Double>(clock(),bestSolutionOverall.myCostSol.penalizedCost));
		   }
		   return true;
	   }
	   else
	   {
		   return false;
	   }
   }

   // Cleans all solutions and generates a new initial population (only used when running HGS until a time limit, in which case the algorithm restarts until the time limit is reached)
   public final void restart()
   {
	   System.out.print("----- RESET: CREATING A NEW POPULATION -----");
	   System.out.print("\n");
	   for (Individual indiv : feasibleSubpopulation)
	   {
		   indiv = null;
	   }
	   for (Individual indiv : infeasibleSubpopulation)
	   {
		   indiv = null;
	   }
	   feasibleSubpopulation.clear();
	   infeasibleSubpopulation.clear();
	   bestSolutionRestart = new Individual();
	   generatePopulation();
   }

   // Adaptation of the penalty parameters
   public final void managePenalties()
   {
	   // Setting some bounds [0.1,1000] to the penalty values for safety
	   double fractionFeasibleLoad = (double) Collections.frequency(listFeasibilityLoad, true) / (double)listFeasibilityLoad.size();
	   if (fractionFeasibleLoad < params.targetFeasible - 0.05 && params.penaltyCapacity < 1000)
	   {
		   params.penaltyCapacity = Math.<Double>min(params.penaltyCapacity * 1.2,1000.0);
	   }
   }
   
public final void managePenalties()
   {
	   // Setting some bounds [0.1,1000] to the penalty values for safety
	   double fractionFeasibleLoad = (double) Collections.frequency(listFeasibilityLoad, true) / (double)listFeasibilityLoad.size();
	   if (fractionFeasibleLoad < params.targetFeasible - 0.05 && params.penaltyCapacity < 1000)
	   {
		   params.penaltyCapacity = Math.<Double>min(params.penaltyCapacity * 1.2,1000.0);
	   }
	   else if (fractionFeasibleLoad > params.targetFeasible + 0.05 && params.penaltyCapacity > 0.1)
	   {
		   params.penaltyCapacity = Math.<Double>max(params.penaltyCapacity * 0.85, 0.1);
	   }

	   // Setting some bounds [0.1,1000] to the penalty values for safety
	   double fractionFeasibleDuration = (double)Collections.frequency(listFeasibilityDuration, true) / (double)listFeasibilityDuration.size();
	   if (fractionFeasibleDuration < params.targetFeasible - 0.05 && params.penaltyDuration < 1000)
	   {
		   params.penaltyDuration = Math.<Double>min(params.penaltyDuration * 1.2,1000.0);
	   }
	   else if (fractionFeasibleDuration > params.targetFeasible + 0.05 && params.penaltyDuration > 0.1)
	   {
		   params.penaltyDuration = Math.<Double>max(params.penaltyDuration * 0.85, 0.1);
	   }

	   // Update the evaluations
	   for (int i = 0; i < (int)infeasibleSubpopulation.size(); i++)
	   {
		   infeasibleSubpopulation.get(i).myCostSol.penalizedCost = infeasibleSubpopulation.get(i).myCostSol.distance + params.penaltyCapacity * infeasibleSubpopulation.get(i).myCostSol.capacityExcess + params.penaltyDuration * infeasibleSubpopulation.get(i).myCostSol.durationExcess;
	   }

	   // If needed, reorder the individuals in the infeasible subpopulation since the penalty values have changed (simple bubble sort for the sake of simplicity)
	   for (int i = 0; i < (int)infeasibleSubpopulation.size(); i++)
	   {
		   for (int j = 0; j < (int)infeasibleSubpopulation.size() - i - 1; j++)
		   {
			   if (infeasibleSubpopulation.get(j).myCostSol.penalizedCost > infeasibleSubpopulation.get(j + 1).myCostSol.penalizedCost + DefineConstants.MY_EPSILON)
			   {
				   Individual indiv = infeasibleSubpopulation.get(j);
				   infeasibleSubpopulation.set(j, infeasibleSubpopulation.get(j + 1));
				   infeasibleSubpopulation.set(j + 1, indiv);
			   }
		   }
	   }
   }

   // Select an individal by binary tournament
   public final Individual getBinaryTournament()
   {
	   Individual individual1;
	   Individual individual2;

	   updateBiasedFitnesses(feasibleSubpopulation);
	   updateBiasedFitnesses(infeasibleSubpopulation);

	   int place1 = tangible.RandomNumbers.nextNumber() % (feasibleSubpopulation.size() + infeasibleSubpopulation.size());
	   if (place1 >= (int)feasibleSubpopulation.size())
	   {
		   individual1 = infeasibleSubpopulation.get(place1 - feasibleSubpopulation.size());
	   }
	   else
	   {
		   individual1 = feasibleSubpopulation.get(place1);
	   }

	   int place2 = tangible.RandomNumbers.nextNumber() % (feasibleSubpopulation.size() + infeasibleSubpopulation.size());
	   if (place2 >= (int)feasibleSubpopulation.size())
	   {
		   individual2 = infeasibleSubpopulation.get(place2 - feasibleSubpopulation.size());
	   }
	   else
	   {
		   individual2 = feasibleSubpopulation.get(place2);
	   }

	   if (individual1.biasedFitness < individual2.biasedFitness)
	   {
		   return individual1;
	   }
	   else
	   {
		   return individual2;
	   }
   }

   // Accesses the best feasible individual
   public final Individual getBestFeasible()
   {
	   if (!feasibleSubpopulation.isEmpty())
	   {
//C++ TO JAVA CONVERTER TODO TASK: The following line was determined to contain a copy constructor call - this should be verified and a copy constructor should be created:
//ORIGINAL LINE: return feasibleSubpopulation[0];
		   return new Individual(feasibleSubpopulation.get(0));
	   }
	   else
	   {
		   return null;
	   }
   }

   // Accesses the best infeasible individual
   public final Individual getBestInfeasible()
   {
	   if (!infeasibleSubpopulation.isEmpty())
	   {
//C++ TO JAVA CONVERTER TODO TASK: The following line was determined to contain a copy constructor call - this should be verified and a copy constructor should be created:
//ORIGINAL LINE: return infeasibleSubpopulation[0];
		   return new Individual(infeasibleSubpopulation.get(0));
	   }
	   else
	   {
		   return null;
	   }
   }

   // Accesses the best found solution at all time
   public final Individual getBestFound()
   {
	   if (bestSolutionOverall.myCostSol.penalizedCost < 1.e29)
	   {
//C++ TO JAVA CONVERTER TODO TASK: The following line was determined to contain a copy constructor call - this should be verified and a copy constructor should be created:
//ORIGINAL LINE: return &bestSolutionOverall;
		   return new Individual(bestSolutionOverall);
	   }
	   else
	   {
		   return null;
	   }
   }

   // Prints population state
   public final void printState(int nbIter, int nbIterNoImprovement)
   {
	   System.out.printf("It %6d %6d | T(s) %.2f", nbIter, nbIterNoImprovement, (double)clock() / (double)CLOCKS_PER_SEC);

	   if (getBestFeasible() != null)
	   {
		   System.out.printf(" | Feas %zu %.2f %.2f", feasibleSubpopulation.size(), getBestFeasible().myCostSol.penalizedCost, getAverageCost(feasibleSubpopulation));
	   }
	   else
	   {
		   System.out.print(" | NO-FEASIBLE");
	   }

	   if (getBestInfeasible() != null)
	   {
		   System.out.printf(" | Inf %zu %.2f %.2f", infeasibleSubpopulation.size(), getBestInfeasible().myCostSol.penalizedCost, getAverageCost(infeasibleSubpopulation));
	   }
	   else
	   {
		   System.out.print(" | NO-INFEASIBLE");
	   }

	   System.out.printf(" | Div %.2f %.2f", getDiversity(feasibleSubpopulation), getDiversity(infeasibleSubpopulation));
	   System.out.printf(" | Feas %.2f %.2f", (double)std::count(listFeasibilityLoad.iterator(), listFeasibilityLoad.end(), true) / (double)listFeasibilityLoad.size(), (double)std::count(listFeasibilityDuration.iterator(), listFeasibilityDuration.end(), true) / (double)listFeasibilityDuration.size());
	   System.out.printf(" | Pen %.2f %.2f", params.penaltyCapacity, params.penaltyDuration);
	   System.out.print("\n");
   }

   // Returns the average diversity value among the 50% best individuals in the subpopulation
   public double getDiversity(final ArrayList<Individual> pop)
   {
	   double average = 0.0;
	   int size = Math.<Integer>min(params.mu, pop.size()); // Only monitoring the "mu" better solutions to avoid too much noise in the measurements
	   for (int i = 0; i < size; i++)
	   {
		   average += pop.get(i).averageBrokenPairsDistanceClosest(size);
	   }
	   if (size > 0)
	   {
		   return average / (double)size;
	   }
	   else
	   {
		   return -1.0;
	   }
   }

   // Returns the average solution value among the 50% best individuals in the subpopulation
   public double getAverageCost(final ArrayList<Individual> pop)
   {
	   double average = 0.0;
	   int size = Math.<Integer>min(params.mu, pop.size()); // Only monitoring the "mu" better solutions to avoid too much noise in the measurements
	   for (int i = 0; i < size; i++)
	   {
		   average += pop.get(i).myCostSol.penalizedCost;
	   }
	   if (size > 0)
	   {
		   return average / (double)size;
	   }
	   else
	   {
		   return -1.0;
	   }
   }
 
// Overwrites a solution written in a file if the current solution is better
   public final void exportBKS(String fileName)
   {
       double readCost;
       ArrayList<ArrayList<Integer>> readSolution = new ArrayList<ArrayList<Integer>>();
       System.out.print("----- CHECKING FOR POSSIBLE BKS UPDATE");
       System.out.print("\n");
       tangible.RefObject<Double> tempRef_readCost = new tangible.RefObject<Double>(readCost);
       boolean readOK = Individual.readCVRPLibFormat(fileName, readSolution, tempRef_readCost);
       readCost = tempRef_readCost.argValue;
       if (bestSolutionOverall.myCostSol.penalizedCost < 1.e29 && (!readOK || bestSolutionOverall.myCostSol.penalizedCost < readCost - DefineConstants.MY_EPSILON))
       {
           System.out.print("----- NEW BKS: ");
           System.out.print(bestSolutionOverall.myCostSol.penalizedCost);
           System.out.print(" !!!");
           System.out.print("\n");
           bestSolutionOverall.exportCVRPLibFormat(fileName);
       }
   }

   // Exports in a file the history of solution improvements
   public final void exportSearchProgress(String fileName, String instanceName, int seedRNG)
   {
       std::ofstream myfile = new std::ofstream(fileName);
       for (genetico.Pair<clock_t, Double> state : searchProgress)
       {
           myfile << instanceName << ";" << seedRNG << ";" << state.second << ";" << (double)state.first / (double)CLOCKS_PER_SEC << std::endl;
       }
   }

   // Constructor
   public Population(Params params, Split split, LocalSearch localSearch)
   {
       this.params = params;
       this.split = split;
       this.localSearch = localSearch;
       listFeasibilityLoad = new LinkedList<Boolean>(100, true);
       listFeasibilityDuration = new LinkedList<Boolean>(100, true);
       generatePopulation();
   }

   // Destructor
   public final void close()
   {
       for (int i = 0; i < (int)feasibleSubpopulation.size(); i++)
       {
           if (feasibleSubpopulation.get(i) != null)
           {
               feasibleSubpopulation.get(i).close();
           }
       }
       for (int i = 0; i < (int)infeasibleSubpopulation.size(); i++)
       {
           if (infeasibleSubpopulation.get(i) != null)
           {
               infeasibleSubpopulation.get(i).close();
           }
       }
   }
   
   
}