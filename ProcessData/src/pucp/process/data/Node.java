package pucp.process.data;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class Node {
	int coordX;
	int coordY;

	public Node(int coordX, int coordY) {
		this.coordX = coordX;
		this.coordY = coordY;
	}

	public static List<Node> cargarBloqueados(String direccion) {

		List<Node> listanodos = new ArrayList<Node>();

		try {
			try (BufferedReader bf = new BufferedReader(new FileReader(direccion))) {
				String linea = bf.readLine();

				while (linea != null) {
					String[] lineaDividida = linea.split(",");
					
					for (int i = 1; i < lineaDividida.length; i += 2) {
						
						Node c = new Node(Integer.parseInt(lineaDividida[i]), Integer.parseInt(lineaDividida[i + 1]));
						listanodos.add(c);
					}

					linea = bf.readLine();
				}
			}

		} catch (Exception e) {
			System.out.println("Error en leer");

		}
		return listanodos;

	}

}
