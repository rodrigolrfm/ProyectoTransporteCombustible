
// Structure containing a route
public class Route
{
	public int cour; // Route index
	public int nbCustomers; // Number of customers visited in the route
	public int whenLastModified; // "When" this route has been last modified
	public int whenLastTestedSWAPStar; // "When" the SWAP* moves for this route have been last tested
	public Node depot; // Pointer to the associated depot
	public double duration; // Total time on the route
	public double load; // Total load on the route
	public double reversalDistance; // Difference of cost if the route is reversed
	public double penalty; // Current sum of load and duration penalties
	public double polarAngleBarycenter; // Polar angle of the barycenter of the route
	public CircleSector sector = new CircleSector(); // Circle sector associated to the set of customers
}