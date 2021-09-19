//====================================================================================================
//The Free Edition of C++ to Java Converter limits conversion output to 100 lines per file.

//To purchase the Premium Edition, visit our website:
//https://www.tangiblesoftwaresolutions.com/order/order-cplus-to-java.html
//====================================================================================================

import java.util.*;


// Structure used in SWAP* to remember the three best insertion positions of a customer in a given route
public class ThreeBestInsert
{
	public int whenLastCalculated;
	public double[] bestCost = new double[3];
	public Node[] bestLocation = tangible.Arrays.initializeWithDefaultNodeInstances(3);

	public final void compareAndAdd(double costInsert, Node placeInsert)
	{
		if (costInsert >= bestCost[2])
		{
			return;
		}
		else if (costInsert >= bestCost[1])
		{
			bestCost[2] = costInsert;
			bestLocation[2] = placeInsert;
		}
		else if (costInsert >= bestCost[0])
		{
			bestCost[2] = bestCost[1];
			bestLocation[2] = bestLocation[1];
			bestCost[1] = costInsert;
			bestLocation[1] = placeInsert;
		}
		else
		{
			bestCost[2] = bestCost[1];
			bestLocation[2] = bestLocation[1];
			bestCost[1] = bestCost[0];
			bestLocation[1] = bestLocation[0];
			bestCost[0] = costInsert;
			bestLocation[0] = placeInsert;
		}
	}

	// Resets the structure (no insertion calculated)
	public final void reset()
	{
		bestCost[0] = 1.e30;
		bestLocation[0] = null;
		bestCost[1] = 1.e30;
		bestLocation[1] = null;
		bestCost[2] = 1.e30;
		bestLocation[2] = null;
	}

	public ThreeBestInsert()
	{
		reset();
	}
}


// Main local learch structure
public class LocalSearch
{


	private Params params; // Problem parameters
	private boolean searchCompleted; // Tells whether all moves have been evaluated without success
	private int nbMoves; // Total number of moves (RI and SWAP*) applied during the local search. Attention: this is not only a simple counter, it is also used to avoid repeating move evaluations
	private ArrayList< Integer > orderNodes = new ArrayList< Integer >(); // Randomized order for checking the nodes in the RI local search
	private ArrayList< Integer > orderRoutes = new ArrayList< Integer >(); // Randomized order for checking the routes in the SWAP* local search
	private TreeSet< Integer > emptyRoutes = new TreeSet< Integer >(); // indices of all empty routes
	private int loopID; // Current loop index

	/* THE SOLUTION IS REPRESENTED AS A LINKED LIST OF ELEMENTS */
	private ArrayList< Node > clients = new ArrayList< Node >(); // Elements representing clients (clients[0] is a sentinel and should not be accessed)
	private ArrayList< Node > depots = new ArrayList< Node >(); // Elements representing depots
	private ArrayList< Node > depotsEnd = new ArrayList< Node >(); // Duplicate of the depots to mark the end of the routes
	private ArrayList< Route > routes = new ArrayList< Route >(); // Elements representing routes
	private ArrayList< ArrayList< ThreeBestInsert >> bestInsertClient = new ArrayList< ArrayList< ThreeBestInsert >>(); // (SWAP*) For each route and node, storing the cheapest insertion cost

	/* TEMPORARY VARIABLES USED IN THE LOCAL SEARCH LOOPS */
	// nodeUPrev -> nodeU -> nodeX -> nodeXNext
	// nodeVPrev -> nodeV -> nodeY -> nodeYNext
	private Node nodeU;
	private Node nodeX;
	private Node nodeV;
	private Node nodeY;
	private Route routeU;
	private Route routeV;
	private int nodeUPrevIndex;
	private int nodeUIndex;
	private int nodeXIndex;
	private int nodeXNextIndex;
	private int nodeVPrevIndex;
	private int nodeVIndex;
	private int nodeYIndex;
	private int nodeYNextIndex;
	private double loadU;
	private double loadX;
	private double loadV;
	private double loadY;
	private double serviceU;
	private double serviceX;
	private double serviceV;
	private double serviceY;
	private double penaltyCapacityLS;
	private double penaltyDurationLS;

	private void setLocalVariablesRouteU()
	{

//====================================================================================================
//End of the allowed output for the Free Edition of C++ to Java Converter.

//To purchase the Premium Edition, visit our website:
//https://www.tangiblesoftwaresolutions.com/order/order-cplus-to-java.html
//====================================================================================================
