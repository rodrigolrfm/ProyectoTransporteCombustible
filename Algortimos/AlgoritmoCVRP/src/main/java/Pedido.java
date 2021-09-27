import java.util.ArrayList;

public class Pedido {
    public Node node;
    public int demanda;
    public String nombreCliente;

    public Pedido(Node node, int demanda, String nombreCliente) {
        this.node = node;
        this.demanda = demanda;
        this.nombreCliente = nombreCliente;
    }

    public static ArrayList<Pedido> leerPedidos(String fileName){
        return null;
    }
}
