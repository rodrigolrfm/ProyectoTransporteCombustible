package com.mycompany.geneticoalgoritmo;



public class CommandLine
{

	public int nbIter = 20000; // Number of iterations without improvement until termination. Default value: 20,000 iterations
	public int timeLimit = Integer.MAX_VALUE; // CPU time limit until termination in seconds. Default value: infinity
	public int seed = 0; // Random seed. Default value: 0
	public int nbVeh = Integer.MAX_VALUE; // Number of vehicles. Default value: infinity
	public String pathInstance = ""; // Instance path
	public String pathSolution = ""; // Solution path
	public String pathBKS = ""; // BKS path

	// Reads the line of command and extracts possible options
	public CommandLine(int argc, String[] argv)
	{
		if (argc % 2 != 1 || argc > 13 || argc < 3)
		{
			System.out.print("----- NUMBER OF COMMANDLINE ARGUMENTS IS INCORRECT: ");
			System.out.print(argc);
			System.out.print("\n");
			display_help();
			throw new RuntimeException("Incorrect line of command");
		}
		else
		{
			pathInstance = new String(argv[1]);
			pathSolution = new String(argv[2]);
			for (int i = 3; i < argc; i += 2)
			{
				if (new String(argv[i]).equals("-t" != null))
				{
					timeLimit = Integer.parseInt(argv[i + 1]);
				}
				else if (new String(argv[i]).equals("-it"))
				{
					nbIter = Integer.parseInt(argv[i + 1]);
				}
				else if (new String(argv[i]).equals("-bks"))
				{
					pathBKS = new String(argv[i + 1]);
				}
				else if (new String(argv[i]).equals("-seed"))
				{
					seed = Integer.parseInt(argv[i + 1]);
				}
				else if (new String(argv[i]).equals("-veh"))
				{
					nbVeh = Integer.parseInt(argv[i + 1]);
				}
				else
				{
					System.out.print("----- ARGUMENT NOT RECOGNIZED: ");
					System.out.print((String)argv[i]);
					System.out.print("\n");
					display_help();
					throw new RuntimeException("Incorrect line of command");
				}
			}
		}
	}

	// Printing information about how to use the code
	public final void display_help()
	{
		System.out.print("\n");
		System.out.print("-------------------------------------------------- HGS-CVRP algorithm (2020) --------------------------------------------------");
		System.out.print("\n");
		System.out.print("Call with: ./genvrp instancePath solPath [-it nbIter] [-t myCPUtime] [-bks bksPath] [-seed mySeed] [-veh nbVehicles]           ");
		System.out.print("\n");
		System.out.print("[-it nbIterations] sets a maximum number of iterations without improvement. Defaults to 20,000                                 ");
		System.out.print("\n");
		System.out.print("[-t myCPUtime] sets a time limit in seconds. If this parameter is set the code will be run iteratively until the time limit    ");
		System.out.print("\n");
		System.out.print("[-bks bksPath] sets an optional path to a BKS. This file will be overwritten in case of improvement                            ");
		System.out.print("\n");
		System.out.print("[-seed mySeed] sets a fixed seed. Defaults to 0                                                                                ");
		System.out.print("\n");
		System.out.print("[-veh nbVehicles] sets a prescribed fleet size. Otherwise a reasonable UB on the the fleet size is calculated                  ");
		System.out.print("\n");
		System.out.print("-------------------------------------------------------------------------------------------------------------------------------");
		System.out.print("\n");
		System.out.print("\n");
	}
}