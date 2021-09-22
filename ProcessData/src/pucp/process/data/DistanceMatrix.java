package pucp.process.data;

import java.util.ArrayList;
import java.util.List;

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
	public List<Node> bloqueados;
	public List<Node> nodos;

	public DistanceMatrix(int[][] map, int mapSizeX, int mapSizeY) {
		super();
		this.map = map;
		this.mapSizeX = mapSizeX;
		this.mapSizeY = mapSizeY;
	}

	public DistanceMatrix(int mapSizeX, int mapSizeY, int[] depots, int[] clients, List<Node> bloqueados) {
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
		if (this.bloqueados != null) {
			Bloqueo bloqueo = new Bloqueo(this.bloqueados);
			this.matrix = bloqueo.cargarBloqueos(this.nodos, this.map, this.matrixSize, K);
		} else {
			this.createMatrixSinBloqueos();
		}
	}

	private void createMatrixSinBloqueos() {
		long[][] matrix = new long[matrixSize][matrixSize];

		for (int i = 0; i < matrixSize; i++) {
			for (int j = 0; j < matrixSize; j++) {
				if (i == j) {
					matrix[i][j] = 0;
					continue;
				}
				// calcular distancia, si esta es igual a 1, significa que son consecutivos
				double distance = Math.sqrt(Math.pow(this.nodos.get(i).coordY - this.nodos.get(j).coordY, 2)
						+ Math.pow(this.nodos.get(i).coordX - this.nodos.get(j).coordX, 2));
				if (distance==1) {
					matrix[i][j] = K;
				} else {
					matrix[i][j] = 0;
				}
			}
		}
		this.matrix = matrix;
	}

	private void iniMap() {
		int nNode = 0;
		nodos = new ArrayList<Node>();
		for (int i = 0; i < this.mapSizeX; i++)
			for (int j = 0; j < this.mapSizeY; j++) {
				this.map[i][j] = nNode;
				Node node = new Node(i, j);
				nodos.add(node);
				nNode++;
			}
	}

	public void reset() {
		for (int i = 0; i < this.mapSizeX; i++)
			for (int j = 0; j < this.mapSizeY; j++)
				this.map[i][j] = 0;
	}

	public void print() {
		// TODO Auto-generated method stub
		for (int i = 0; i < matrixSize; i++) {
			for (int j = 0; j < matrixSize; j++) {
				System.out.print(this.matrix[i][j]+",");
			}
			System.out.println();
		}
	}

}
