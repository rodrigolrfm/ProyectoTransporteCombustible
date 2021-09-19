import java.util.*;
import java.io.*;


public class Genetic implements Closeable
{

	private Params params; // Problem parameters
	private Split split; // Split algorithm
	private Population population; // Population
	private LocalSearch localSearch; // Local Search structure
	private Individual offspring; // First individual to be used as input for the crossover

	// OX Crossover
	private void crossoverOX(Individual result, Individual parent1, Individual parent2)
	{
		// Frequency table to track the customers which have been already inserted
		ArrayList<Boolean> freqClient = new ArrayList<Boolean>(params.nbClients + 1);

		// Picking the beginning and end of the crossover zone
		int start = tangible.RandomNumbers.nextNumber() % params.nbClients;
		int end = tangible.RandomNumbers.nextNumber() % params.nbClients;
		while (end == start)
		{
			end = tangible.RandomNumbers.nextNumber() % params.nbClients;
		}

		// Copy in place the elements from start to end (possibly "wrapping around" the end of the array)
		int j = start;
		while (j % params.nbClients != (end + 1) % params.nbClients)
		{
			result.chromT.set(j % params.nbClients, parent1.chromT.get(j % params.nbClients));
			freqClient.set(result.chromT.get(j % params.nbClients), true);
			j++;
		}

		// Fill the remaining elements in the order given by the second parent
		for (int i = 1; i <= params.nbClients; i++)
		{
			int temp = parent2.chromT.get((end + i) % params.nbClients);
			if (freqClient.get(temp) == false)
			{
				result.chromT.set(j % params.nbClients, temp);
				j++;
			}
		}

		// Completing the individual with the Split algorithm
		split.generalSplit(result, parent1.myCostSol.nbRoutes);
	}


	// Running the genetic algorithm until maxIterNonProd consecutive iterations or a time limit
	public final void run(int maxIterNonProd, int timeLimit)
	{
		int nbIterNonProd = 1;
		for (int nbIter = 0 ; nbIterNonProd <= maxIterNonProd && clock() / CLOCKS_PER_SEC < timeLimit ; nbIter++)
		{
			/* SELECTION AND CROSSOVER */
			crossoverOX(offspring, population.getBinaryTournament(), population.getBinaryTournament());

			/* LOCAL SEARCH */
			localSearch.run(offspring, params.penaltyCapacity, params.penaltyDuration);
			boolean isNewBest = population.addIndividual(offspring, true);
			if (!offspring.isFeasible && tangible.RandomNumbers.nextNumber() % 2 == 0) // Repair half of the solutions in case of infeasibility
			{
				localSearch.run(offspring, params.penaltyCapacity * 10.0, params.penaltyDuration * 10.0);
				if (offspring.isFeasible)
				{
					isNewBest = (population.addIndividual(offspring, false) || isNewBest);
				}
			}

			/* TRACKING THE NUMBER OF ITERATIONS SINCE LAST SOLUTION IMPROVEMENT */
			if (isNewBest)
			{
				nbIterNonProd = 1;
			}
			else
			{
				nbIterNonProd++;
			}

			/* DIVERSIFICATION, PENALTY MANAGEMENT AND TRACES */
			if (nbIter % 100 == 0)
			{
				population.managePenalties();
			}
			if (nbIter % 500 == 0)
			{
				population.printState(nbIter, nbIterNonProd);
			}

			/* FOR TESTS INVOLVING SUCCESSIVE RUNS UNTIL A TIME LIMIT: WE RESET THE ALGORITHM/POPULATION EACH TIME maxIterNonProd IS ATTAINED*/
			if (timeLimit != Integer.MAX_VALUE && nbIterNonProd == maxIterNonProd)
			{
				population.restart();
				nbIterNonProd = 1;
			}
		}
	}

	// Constructor
	public Genetic(Params params, Split split, Population population, LocalSearch localSearch)
	{
		this.params = params;
		this.split = split;
		this.population = population;
		this.localSearch = localSearch;
		offspring = new Individual(params);
	}

}