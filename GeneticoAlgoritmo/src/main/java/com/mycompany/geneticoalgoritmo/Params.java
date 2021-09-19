package com.mycompany.geneticoalgoritmo;


import java.util.*;


public class Params
{

	/* PARAMETERS OF THE GENETIC ALGORITHM */
	public int nbGranular = 20; // Granular search parameter, limits the number of moves in the RI local search
	public int mu = 25; // Minimum population size
	public int lambda = 40; // Number of solutions created before reaching the maximum population size (i.e., generation size)
	public int nbElite = 4; // Number of elite individuals (reduced in HGS-2020)
	public int nbClose = 5; // Number of closest solutions/individuals considered when calculating diversity contribution
	public double targetFeasible = 0.2; // Reference proportion for the number of feasible individuals, used for the adaptation of the penalty parameters

	/* ADAPTIVE PENALTY COEFFICIENTS */
	public double penaltyCapacity; // Penalty for one unit of capacity excess (adapted through the search)
	public double penaltyDuration; // Penalty for one unit of duration excess (adapted through the search)

	/* DATA OF THE PROBLEM INSTANCE */			
	public boolean isRoundingInteger; // Distance calculation convention
	public boolean isDurationConstraint; // Indicates if the problem includes duration constraints
	public int nbClients; // Number of clients (excluding the depot)
	public int nbVehicles; // Number of vehicles
	public double durationLimit; // Route duration limit
	public double vehicleCapacity; // Capacity limit
	public double totalDemand; // Total demand required by the clients
	public double maxDemand; // Maximum demand of a client
	public double maxDist; // Maximum distance between two clients
	public ArrayList< Client > cli = new ArrayList< Client >(); // Vector containing information on each client
	public ArrayList< ArrayList< Double >> timeCost = new ArrayList< ArrayList< Double >>(); // Distance matrix
	public ArrayList< ArrayList< Integer >> correlatedVertices = new ArrayList< ArrayList< Integer >>(); // Neighborhood restrictions: For each client, list of nearby customers

	// Initialization from a given data set
	public Params(String pathToInstance, int nbVeh, int seedRNG)
	{
		this.nbVehicles = nbVeh;
		String content;
		String content2;
		String content3;
		double serviceTimeData = 0.0;
		nbClients = 0;
		totalDemand = 0.0;
		maxDemand = 0.0;
		durationLimit = 1.e30;
		vehicleCapacity = 1.e30;
		isRoundingInteger = true;
		isDurationConstraint = false;

		// Initialize RNG
		srand(seedRNG);

		// Read INPUT dataset
		std::ifstream inputFile = new std::ifstream(pathToInstance);
		if (inputFile.is_open())
		{
			getline(inputFile, content);
			getline(inputFile, content);
			getline(inputFile, content);
//C++ TO JAVA CONVERTER WARNING: The right shift operator was not replaced by Java's logical right shift operator since the left operand was not confirmed to be of an unsigned type, but you should review whether the logical right shift operator (>>>) is more appropriate:
			for (inputFile >> content ; !content.equals("NODE_COORD_SECTION") ; inputFile >> content)
			{
				if (content.equals("DIMENSION"))
				{
//C++ TO JAVA CONVERTER WARNING: The right shift operator was not replaced by Java's logical right shift operator since the left operand was not confirmed to be of an unsigned type, but you should review whether the logical right shift operator (>>>) is more appropriate:
					inputFile >> content2 >> nbClients;
					nbClients--;
				} // Need to substract the depot from the number of nodes
				else if (content.equals("EDGE_WEIGHT_TYPE"))
				{
//C++ TO JAVA CONVERTER WARNING: The right shift operator was not replaced by Java's logical right shift operator since the left operand was not confirmed to be of an unsigned type, but you should review whether the logical right shift operator (>>>) is more appropriate:
					inputFile >> content2 >> content3;
				}
				else if (content.equals("CAPACITY"))
				{
//C++ TO JAVA CONVERTER WARNING: The right shift operator was not replaced by Java's logical right shift operator since the left operand was not confirmed to be of an unsigned type, but you should review whether the logical right shift operator (>>>) is more appropriate:
					inputFile >> content2 >> vehicleCapacity;
				}
				else if (content.equals("DISTANCE"))
				{
//C++ TO JAVA CONVERTER WARNING: The right shift operator was not replaced by Java's logical right shift operator since the left operand was not confirmed to be of an unsigned type, but you should review whether the logical right shift operator (>>>) is more appropriate:
					inputFile >> content2 >> durationLimit;
					isDurationConstraint = true;
				}
				else if (content.equals("SERVICE_TIME"))
				{
//C++ TO JAVA CONVERTER WARNING: The right shift operator was not replaced by Java's logical right shift operator since the left operand was not confirmed to be of an unsigned type, but you should review whether the logical right shift operator (>>>) is more appropriate:
					inputFile >> content2 >> serviceTimeData;
				}
				else
				{
					throw new String("Unexpected data in input file: " + content);
				}
			}
			if (nbClients <= 0)
			{
				throw new RuntimeException("Number of nodes is undefined");
			}
			if (vehicleCapacity == 1.e30)
			{
				throw new RuntimeException("Vehicle capacity is undefined");
			}

			// Reading client coordinates
			cli = new ArrayList<Client>(nbClients + 1);
			for (int i = 0; i <= nbClients; i++)
			{
//C++ TO JAVA CONVERTER WARNING: The right shift operator was not replaced by Java's logical right shift operator since the left operand was not confirmed to be of an unsigned type, but you should review whether the logical right shift operator (>>>) is more appropriate:
				inputFile >> cli.get(i).custNum >> cli.get(i).coordX >> cli.get(i).coordY;
				cli.get(i).custNum--;
				cli.get(i).polarAngle = CircleSector.positive_mod((int)32768.0 * Math.atan2(cli.get(i).coordY - cli.get(0).coordY, cli.get(i).coordX - cli.get(0).coordX) / DefineConstants.PI);
			}

			// Reading demand information
//C++ TO JAVA CONVERTER WARNING: The right shift operator was not replaced by Java's logical right shift operator since the left operand was not confirmed to be of an unsigned type, but you should review whether the logical right shift operator (>>>) is more appropriate:
			inputFile >> content;
			if (!content.equals("DEMAND_SECTION"))
			{
				throw new String("Unexpected data in input file: " + content);
			}
			for (int i = 0; i <= nbClients; i++)
			{
//C++ TO JAVA CONVERTER WARNING: The right shift operator was not replaced by Java's logical right shift operator since the left operand was not confirmed to be of an unsigned type, but you should review whether the logical right shift operator (>>>) is more appropriate:
				inputFile >> content >> cli.get(i).demand;
				cli.get(i).serviceDuration = (i == 0) ? 0.0 : serviceTimeData;
				if (cli.get(i).demand > maxDemand)
				{
					maxDemand = cli.get(i).demand;
				}
				totalDemand += cli.get(i).demand;
			}

			// Reading depot information (in all current instances the depot is represented as node 1, the program will return an error otherwise)
//C++ TO JAVA CONVERTER WARNING: The right shift operator was not replaced by Java's logical right shift operator since the left operand was not confirmed to be of an unsigned type, but you should review whether the logical right shift operator (>>>) is more appropriate:
			inputFile >> content >> content2 >> content3 >> content3;
			if (!content.equals("DEPOT_SECTION"))
			{
				throw new String("Unexpected data in input file: " + content);
			}
			if (!content2.equals("1"))
			{
				throw new String("Expected depot index 1 instead of " + content2);
			}
			if (!content3.equals("EOF"))
			{
				throw new String("Unexpected data in input file: " + content3);
			}
		}
		else
		{
			throw new IllegalArgumentException("Impossible to open instance file: " + pathToInstance);
		}

		// Default initialization if the number of vehicles has not been provided by the user
		if (nbVehicles == Integer.MAX_VALUE)
		{
			nbVehicles = Math.ceil(1.2 * totalDemand / vehicleCapacity) + 2; // Safety margin: 20% + 2 more vehicles than the trivial bin packing LB
			System.out.print("----- FLEET SIZE WAS NOT SPECIFIED: DEFAULT INITIALIZATION TO ");
			System.out.print(nbVehicles);
			System.out.print(" VEHICLES");
			System.out.print("\n");
		}
		else
		{
			System.out.print("----- FLEET SIZE SPECIFIED IN THE COMMANDLINE: SET TO ");
			System.out.print(nbVehicles);

		}
	}

}