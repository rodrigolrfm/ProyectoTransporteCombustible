package com.mycompany.geneticoalgoritmo;

import java.util.*;

public class Node
{
	public boolean isDepot; // Tells whether this node represents a depot or not
	public int cour; // Node index
	public int position; // Position in the route
	public int whenLastTestedRI; // "When" the RI moves for this node have been last tested
	public Node next; // Next node in the route order
	public Node prev; // Previous node in the route order
	public Route route; // Pointer towards the associated route
	public double cumulatedLoad; // Cumulated load on this route until the customer (including itself)
	public double cumulatedTime; // Cumulated time on this route until the customer (including itself)
	public double cumulatedReversalDistance; // Difference of cost if the segment of route (0...cour) is reversed (useful for 2-opt moves with asymmetric problems)
	public double deltaRemoval; // Difference of cost in the current route if the node is removed (used in SWAP*)
}