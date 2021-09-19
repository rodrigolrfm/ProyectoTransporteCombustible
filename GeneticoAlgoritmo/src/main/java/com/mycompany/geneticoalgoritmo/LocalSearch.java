package com.mycompany.geneticoalgoritmo;

import java.util.*;


public class LocalSearch{
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

	private void setLocalVariablesRouteU(){
            
        }
}