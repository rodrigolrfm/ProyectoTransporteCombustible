package com.mycompany.geneticoalgoritmo;

import java.util.ArrayList;
import java.util.List;

public class Bloqueo {
	public List<Node> bloqueados;
	public ArrayList<Integer> bloqueadosMap;
	
	public Bloqueo(List<Node> bloqueados) {
		this.bloqueados = bloqueados;
		this.bloqueadosMap = new ArrayList<Integer>(bloqueados.size());
	}
	
	public long[][] cargarBloqueos(List<Node> nodos, int[][] map, int matrixSize, int K) {
		for(int i = 0; i < this.bloqueados.size(); i++) {
			Node nodo = this.bloqueados.get(i);
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
				double distance = Math.sqrt(Math.pow(nodos.get(i).coordY-nodos.get(j).coordY,2) + Math.pow(nodos.get(i).coordX-nodos.get(j).coordX,2));
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
