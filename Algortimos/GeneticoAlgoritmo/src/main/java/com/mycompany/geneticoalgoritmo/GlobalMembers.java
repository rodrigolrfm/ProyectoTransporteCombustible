/*
package com.mycompany.geneticoalgoritmo;
import static java.lang.System.nanoTime;
public class GlobalMembers
{
	public static void main(int argc, String[] args)
	{
		try
		{
			// Reading the arguments of the program
			CommandLine commandline = new CommandLine(argc, args);

			// Reading the data file and initializing some data structures
			System.out.print("----- READING DATA SET: ");
			System.out.print(commandline.pathInstance);
			System.out.print("\n");
			Params params = new Params(commandline.pathInstance, commandline.nbVeh, commandline.seed);

			// Creating the Split and local search structures
			Split split = new Split(params);
			LocalSearch localSearch = new LocalSearch(params);

			// Initial population
			System.out.print("----- INSTANCE LOADED WITH ");
			System.out.print(params.nbClients);
			System.out.print(" CLIENTS AND ");
			System.out.print(params.nbVehicles);
			System.out.print(" VEHICLES");
			System.out.print("\n");
			System.out.print("----- BUILDING INITIAL POPULATION");
			System.out.print("\n");
			Population population = new Population(params, split, localSearch);

			// Genetic algorithm
			System.out.print("----- STARTING GENETIC ALGORITHM");
			System.out.print("\n");
			Genetic solver = new Genetic(params, split, population, localSearch);
			solver.run(commandline.nbIter, commandline.timeLimit);
			System.out.print("----- GENETIC ALGORITHM FINISHED, TIME SPENT: ");
			System.out.print(nanoTime());
			System.out.print("\n");

			// Exporting the best solution
			if (population.getBestFound() != null)
			{
				population.getBestFound().exportCVRPLibFormat(commandline.pathSolution);
				population.exportSearchProgress(commandline.pathSolution + ".PG.csv", commandline.pathInstance, commandline.seed);
				if (!commandline.pathBKS.equals(""))
				{
					population.exportBKS(commandline.pathBKS);
				}
			}
		}
		catch (Exception e)
		{
			System.out.print("EXCEPTION | "+ e.getMessage());
			System.out.print(e);
			System.out.print("\n");
		}

	}

}
*/