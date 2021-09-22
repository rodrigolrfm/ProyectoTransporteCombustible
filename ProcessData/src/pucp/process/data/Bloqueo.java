package pucp.process.data;

import java.util.ArrayList;

public class Bloqueo {
	public Node[] bloqueados;
	public ArrayList<Integer> bloqueadosMap;
	
	public Bloqueo(Node[] bloqueados) {
		this.bloqueados = bloqueados;
		this.bloqueadosMap = new ArrayList<Integer>(bloqueados.length);
	}
	
	public long[][] cargarBloqueos(Node[] nodos, int[][] map, int matrixSize, int K) {
		for(int i = 0; i < this.bloqueados.length; i++) {
			Node nodo = this.bloqueados[i];
			this.bloqueadosMap.add(map[nodo.coordX][nodo.coordY]);
		}
		long [][] matrix = new long[matrixSize][matrixSize];
		
		for(int i = 0; i < matrixSize; i++) {
			for(int j = 0; j < matrixSize; j++) {
				if(i==j) {
					matrix[i][j] = 0;
					continue;
				}
				if(bloqueadosMap.contains(i) || bloqueadosMap.contains(j)) {
					matrix[i][j] = 0;
					continue;
				}
				//calcular distancia, si esta es igual a 1, significa que son consecutivos
				double distance = Math.sqrt(Math.pow(nodos[i].coordY-nodos[j].coordY,2) + Math.pow(nodos[i].coordX-nodos[j].coordX,2));
				if(distance==1.0) {
					matrix[i][j] = K;
				}else {
					matrix[i][j] = 0;
				}
			}
		}
		return matrix;
	}
}
