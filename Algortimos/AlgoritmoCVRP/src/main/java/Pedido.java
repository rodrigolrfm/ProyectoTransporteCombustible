import java.io.BufferedReader;
import java.io.FileReader;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.javatuples.Pair;

public class Pedido {
    public LocalDateTime tiempoPedido;
    public Node node;
    public int demanda;
    public int plazoHoras;

    public Pedido(Node node, int demanda) {
        this.tiempoPedido = LocalDateTime.now();
        this.node = node;
        this.demanda = demanda;
        this.plazoHoras = 0;
    }

    public Pedido(Node node, int demanda, int year, int month, int day, int hour, int minute, int second){
        this.tiempoPedido = LocalDateTime.of(year, month, day, hour, minute, second);
        this.node = node;
        this.demanda = demanda;
    }

    public static Map<Integer, Integer> leerPedidos(String fileName, int mapSizeY){
        Map<Integer, Integer> listaPedidos = new HashMap<>();

        try {
            try (BufferedReader bf = new BufferedReader(new FileReader(fileName))) {
                String linea = bf.readLine();

                while (linea != null) {
                    String[] lineaDividida = linea.split(",");

                    for (int i = 0; i < lineaDividida.length; i += 5) {
                        Node nodo = new Node(Integer.parseInt(lineaDividida[i+1]), Integer.parseInt(lineaDividida[i+2]));
                        //Pedido pedido = new Pedido(nodo, Integer.parseInt(lineaDividida[i+2]));
                        int key = nodo.numeroNodo(mapSizeY);
                        listaPedidos.put(key, Integer.parseInt(lineaDividida[i+3]));
                    }
                    linea = bf.readLine();
                }
            }

        } catch (Exception e) {
            System.out.println("Error en leer");

        }
        return listaPedidos;
    }

    public static ArrayList<Pair<Node, Integer>> leerPedidosExp(String fileName, int mapSizeY){
        ArrayList<Pair<Node, Integer>> pedidos = new ArrayList<>();

        try {
            try (BufferedReader bf = new BufferedReader(new FileReader(fileName))) {
                String linea = bf.readLine();
                Node depot = new Node(0,0);
                Pair<Node, Integer> pedido = new Pair<>(depot, 0);
                pedidos.add(pedido);
                //int nPedido = 1;
                while (linea != null) {
                    String[] lineaDividida = linea.split(",");

                    for (int i = 0; i < lineaDividida.length; i += 5) {
                        Node nodo = new Node(Integer.parseInt(lineaDividida[i+1]), Integer.parseInt(lineaDividida[i+2]));
                        //Pedido pedido = new Pedido(nodo, Integer.parseInt(lineaDividida[i+2]));
                        //int key = nodo.numeroNodo(mapSizeY);
                        pedido = new Pair<>(nodo, Integer.parseInt(lineaDividida[i+3]));
                        pedidos.add(pedido);
                    }
                    linea = bf.readLine();
                    //nPedido++;
                }
            }

        } catch (Exception e) {
            System.out.println("Error en leer");

        }
        return pedidos;
    }
}
