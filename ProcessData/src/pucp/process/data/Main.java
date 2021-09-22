package pucp.process.data;

import java.util.List;

public class Main {

	public static void main(String[] args) {
		List<Node> bloqueados = Node.cargarBloqueados("data\\bloqueos\\prueba1.txt");
		DistanceMatrix distanceMatrix = new DistanceMatrix(5, 7, null, null, bloqueados);
		distanceMatrix.print();
	}

}
