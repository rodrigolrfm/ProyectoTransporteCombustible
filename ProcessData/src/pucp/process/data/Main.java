package pucp.process.data;

import java.util.Iterator;
import java.util.List;

public class Main {

	public static void main(String[] args) {
		List<Node> bloqueados = Node.cargarBloqueados("data\\bloqueos\\prueba.txt");
		//DistanceMatrix distanceMatrix = new DistanceMatrix(70, 50, null, null, bloqueados);
		for (Iterator iterator = bloqueados.iterator(); iterator.hasNext();) {
			Node node = (Node) iterator.next();
			System.out.println(node.mapId);
		}
	}

}
