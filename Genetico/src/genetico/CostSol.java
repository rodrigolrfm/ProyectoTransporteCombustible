import java.util.*;

public class CostSol
{
	public double penalizedCost; // Penalized cost of the solution
	public int nbRoutes; // Number of routes
	public double distance; // Total Distance
	public double capacityExcess; // Sum of excess load in all routes
	public double durationExcess; // Sum of excess duration in all routes
	public CostSol()
	{
		penalizedCost = 0.0;
		nbRoutes = 0;
		distance = 0.0;
		capacityExcess = 0.0;
		durationExcess = 0.0;
	}
}