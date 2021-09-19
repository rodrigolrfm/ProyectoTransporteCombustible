import java.util.*;

public class Individual{
  public Params params; // Problem parameters
  public CostSol myCostSol = new CostSol(); // Solution cost parameters
  public ArrayList< Integer > chromT = new ArrayList< Integer >(); // Giant tour representing the individual
  public ArrayList< ArrayList<Integer>> chromR = new ArrayList< ArrayList<Integer>>(); // For each vehicle, the associated sequence of deliveries (complete solution)
  public ArrayList< Integer > successors = new ArrayList< Integer >(); // For each node, the successor in the solution (can be the depot 0)
  public ArrayList< Integer > predecessors = new ArrayList< Integer >(); // For each node, the predecessor in the solution (can be the depot 0)
  public std::multiset< std::pair < Double, Individual >> indivsPerProximity = new std::multiset< std::pair < Double, Individual >>(); // The other individuals in the population, ordered by increasing proximity (the set container follows a natural ordering based on the first value of the pair)
  public boolean isFeasible; // Feasibility status of the individual
  public double biasedFitness; // Biased fitness of the solution


  public final void evaluateCompleteCost()
  {
	  myCostSol = new CostSol();
	  for (int r = 0; r < params.nbVehicles; r++)
	  {
		  if (!chromR.get(r).isEmpty())
		  {
			  double distance = params.timeCost.get(0).get(chromR.get(r).get(0));
			  double load = params.cli.get(chromR.get(r).get(0)).demand;
			  double service = params.cli.get(chromR.get(r).get(0)).serviceDuration;
			  predecessors.set(chromR.get(r).get(0), 0);
			  for (int i = 1; i < (int)chromR.get(r).size(); i++)
			  {
				  distance += params.timeCost.get(chromR.get(r).get(i - 1)).get(chromR.get(r).get(i));
				  load += params.cli.get(chromR.get(r).get(i)).demand;
				  service += params.cli.get(chromR.get(r).get(i)).serviceDuration;
				  predecessors.set(chromR.get(r).get(i), chromR.get(r).get(i - 1));
				  successors.set(chromR.get(r).get(i - 1), chromR.get(r).get(i));
			  }
			  successors.set(chromR.get(r).get(chromR.get(r).size() - 1), 0);
			  distance += params.timeCost.get(chromR.get(r).get(chromR.get(r).size() - 1)).get(0);
			  myCostSol.distance += distance;
			  myCostSol.nbRoutes++;
			  if (load > params.vehicleCapacity)
			  {
				  myCostSol.capacityExcess += load - params.vehicleCapacity;
			  }
			  if (distance + service > params.durationLimit)
			  {
				  myCostSol.durationExcess += distance + service - params.durationLimit;
			  }
		  }
	  }

	  myCostSol.penalizedCost = myCostSol.distance + myCostSol.capacityExcess * params.penaltyCapacity + myCostSol.durationExcess * params.penaltyDuration;
	  isFeasible = (myCostSol.capacityExcess < DefineConstants.MY_EPSILON && myCostSol.durationExcess < DefineConstants.MY_EPSILON);
  }

  // Removing an individual in the structure of proximity
  public final void removeProximity(Individual indiv)
  {
	  var it = indivsPerProximity.begin();
	  while (it.second != indiv)
	  {
		  ++it;
	  }
	  indivsPerProximity.erase(it);
  }

  // Distance measure with another individual
  public final double brokenPairsDistance(Individual indiv2)
  {
	  int differences = 0;
	  for (int j = 1; j <= params.nbClients; j++)
	  {
		  if (successors.get(j) != indiv2.successors.get(j) && successors.get(j) != indiv2.predecessors.get(j))
		  {
			  differences++;
		  }
		  if (predecessors.get(j) == 0 && indiv2.predecessors.get(j) != 0 && indiv2.successors.get(j) != 0)
		  {
			  differences++;
		  }
	  }
	  return (double)differences / (double)params.nbClients;
  }

  // Returns the average distance of this individual with the nbClosest individuals
  public final double averageBrokenPairsDistanceClosest(int nbClosest)
  {
	  double result = 0;
	  int maxSize = Math.<Integer>min(nbClosest, indivsPerProximity.size());
	  var it = indivsPerProximity.begin();
	  for (int i = 0 ; i < maxSize; i++)
	  {
		  result += it.first;
		  ++it;
	  }
	  return result / (double)maxSize;
  }

  // Exports a solution in CVRPLib format (adds a final line with the computational time)
  public final void exportCVRPLibFormat(String fileName)
  {
	  System.out.print("----- WRITING SOLUTION WITH VALUE ");
	  System.out.print(myCostSol.penalizedCost);
	  System.out.print(" IN : ");
	  System.out.print(fileName);
	  System.out.print("\n");
	  std::ofstream myfile = new std::ofstream(fileName);
	  if (myfile.is_open())
	  {
		  for (int k = 0; k < params.nbVehicles; k++)
		  {
			  if (!chromR.get(k).isEmpty())
			  {
				  myfile << "Route #" << k + 1 << ":"; // Route IDs start at 1 in the file format
				  for (int i : chromR.get(k))
				  {
					  myfile << " " << i;
				  }
				  myfile << std::endl;
			  }
		  }
		  myfile << "Cost " << myCostSol.penalizedCost << std::endl;
		  myfile << "Time " << (double)clock() / (double)CLOCKS_PER_SEC << std::endl;
	  }
	  else
	  {
		  System.out.print("----- IMPOSSIBLE TO OPEN: ");
		  System.out.print(fileName);
		  System.out.print("\n");
	  }
  }

  // Reads a solution in CVRPLib format, returns TRUE if the process worked, or FALSE if the file does not exist or is not readable
  public static boolean readCVRPLibFormat(String fileName, ArrayList<ArrayList<Integer>> readSolution, tangible.RefObject<Double> readCost)
  {
	  readSolution.clear();
	  std::ifstream inputFile = new std::ifstream(fileName);
	  if (inputFile.is_open())
	  {
		  String inputString;
		  inputFile >> inputString;
		  // Loops as long as the first line keyword is "Route"
		  for (int r = 0; inputString.equals("Route") ; r++)
		  {
			  readSolution.add(new ArrayList<Integer>());
			  inputFile >> inputString;
			  getline(inputFile, inputString);
			  std::stringstream ss = new std::stringstream(inputString);
			  int inputCustomer;
                  }
          }
  }
}