package pucp.process.data;

public class DistanceMatrix {
	public int[][] map;
	public int mapSizeX;
	public int mapSizeY;
	public Route[] routes;
	public int[] depots;
	public int[] clients;
	public long[][] matrix;
	public int matrixSize;
	public final int K = 1;
	public Node[] bloqueados;
	public Node[] nodos;
	
	public DistanceMatrix(int[][] map, int mapSizeX, int mapSizeY) {
		super();
		this.map = map;
		this.mapSizeX = mapSizeX;
		this.mapSizeY = mapSizeY;
	}
	
	public DistanceMatrix(int mapSizeX, int mapSizeY, int[] depots, int[] clients, Node[] bloqueados) {
		this.mapSizeX = mapSizeX;
		this.mapSizeY = mapSizeY;
		this.map = new int[mapSizeX][mapSizeY];
		this.iniMap();
		this.matrixSize = this.mapSizeX * this.mapSizeY;
		this.depots = depots;
		this.clients = clients;
		this.bloqueados = bloqueados;
		this.createMatrix();
	}

	private void createMatrix() {
		Bloqueo bloqueo = new Bloqueo(this.bloqueados);
		this.matrix = bloqueo.cargarBloqueos(nodos, this.map,this.matrixSize, K);
	}
	
	private void iniMap() {
		int nNode = 0;
		nodos = new Node[this.mapSizeX*this.mapSizeY];
		for(int i = 0; i < this.mapSizeX; i++)
			for(int j = 0; j < this.mapSizeY; j++) {
				this.map[i][j] = nNode;
				Node node = new Node(i, j);
				nodos[nNode] = node;
				nNode++;
			}
	}

	public void reset() {
		for(int i = 0; i < this.mapSizeX; i++)
			for(int j = 0; j < this.mapSizeY; j++)
				this.map[i][j] = 0;
	}

}
