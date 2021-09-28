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

}
