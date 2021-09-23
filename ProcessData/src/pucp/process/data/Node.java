package pucp.process.data;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class Node {
	static final AtomicLong MAP_ID = new AtomicLong(0);
	static final AtomicLong ID = new AtomicLong(0);
	int coordX;
	int coordY;
	private boolean isDepot;
	private boolean isClient;
	final long mapId = MAP_ID.getAndIncrement();
	long id;

	public Node(int coordX, int coordY) {
		this.coordX = coordX;
		this.coordY = coordY;
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

	public boolean isDepot() {
		return isDepot;
	}

	public void setDepot(boolean isDepot) {
		this.isDepot = isDepot;
	}

	public boolean isClient() {
		return isClient;
	}

	public void setClient(boolean isClient) {
		this.isClient = isClient;
	}

}
