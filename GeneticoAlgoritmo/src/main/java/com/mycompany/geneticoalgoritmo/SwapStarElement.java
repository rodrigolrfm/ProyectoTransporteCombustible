package com.mycompany.geneticoalgoritmo;


// Structured used to keep track of the best SWAP* move
public class SwapStarElement
{
	public double moveCost = 1.e30;
	public Node U = null;
	public Node bestPositionU = null;
	public Node V = null;
	public Node bestPositionV = null;
}