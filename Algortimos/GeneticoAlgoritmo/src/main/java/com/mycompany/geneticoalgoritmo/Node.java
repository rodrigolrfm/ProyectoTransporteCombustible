package com.mycompany.geneticoalgoritmo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

public class Node implements Comparable<Node> {
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

	int coordX;
	int coordY;

	public Node(int coordX, int coordY) {
		this.coordX = coordX;
		this.coordY = coordY;
	}

	@Override
	public String toString() {
		return "Node{" +
				"coordX=" + coordX +
				", coordY=" + coordY +
				'}';
	}

	public static List<Node> cargarBloqueados(String direccion) {

		List<Node> listaNodos = new ArrayList<Node>();

		try {
			try (BufferedReader bf = new BufferedReader(new FileReader(direccion))) {
				String linea = bf.readLine();

				while (linea != null) {
					String[] lineaDividida = linea.split(",");

					for (int i = 1; i < lineaDividida.length; i += 2) {

						Node c = new Node(Integer.parseInt(lineaDividida[i]), Integer.parseInt(lineaDividida[i + 1]));
						listaNodos.add(c);
					}

					linea = bf.readLine();
				}
			}

		} catch (Exception e) {
			System.out.println("Error en leer");

		}
		return listaNodos;

	}

	public int numeroNodo(int mapSizeY){
		return (mapSizeY)*this.coordX + this.coordY;
	}

	public static Node calcularVertice(int start, int sizeY){
		int cordX = Math.floorDiv(start,sizeY);
		int cordY = start % sizeY;
		return new Node(cordX,cordY);
	}

	public static String getRoute(ArrayList<Integer> ruta){
		String cadena = new String("");
		ruta.remove(0);
		int last = ruta.size()-1;
		ruta.remove(last);
		for (Integer nodo:
				ruta) {
			cadena = cadena + nodo + " -> ";
		}
		return cadena;
	}

	public static Integer calculardistancia(Node a,Node b){

		return Math.abs(a.coordX - b.coordX) + Math.abs(b.coordY - a.coordY);

	}

}