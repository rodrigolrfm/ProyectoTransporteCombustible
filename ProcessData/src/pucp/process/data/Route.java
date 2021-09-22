package pucp.process.data;

public class Route {
	public int inicio;
	public int fin;
	public int[] route;

	public Route(int[] route) {
		super();
		this.route = route;
		this.inicio = route[0];
		this.fin = route[route.length];
	}
	
}
