import java.io.BufferedReader;
import java.io.FileReader;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Pedido {
    public LocalDateTime tiempoEntrega;
    public Node node;
    public int demanda;

    public Pedido(Node node, int demanda) {
        this.tiempoEntrega = LocalDateTime.now();
        this.node = node;
        this.demanda = demanda;
    }

    public Pedido(Node node, int demanda, int year, int month, int day, int hour, int minute, int second){
        this.tiempoEntrega = LocalDateTime.of(year, month, day, hour, minute, second);
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

                    for (int i = 0; i < lineaDividida.length; i += 3) {
                        Node nodo = new Node(Integer.parseInt(lineaDividida[i]), Integer.parseInt(lineaDividida[i+1]));
                        //Pedido pedido = new Pedido(nodo, Integer.parseInt(lineaDividida[i+2]));
                        int key = nodo.numeroNodo(mapSizeY);
                        listaPedidos.put(key, Integer.parseInt(lineaDividida[i+2]));
                    }
                    linea = bf.readLine();
                }
            }

        } catch (Exception e) {
            System.out.println("Error en leer");

        }
        return listaPedidos;
    }
}
