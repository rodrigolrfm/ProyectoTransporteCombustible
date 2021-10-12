import com.google.ortools.Loader;
import com.google.ortools.constraintsolver.Assignment;
import com.google.ortools.constraintsolver.FirstSolutionStrategy;
import com.google.ortools.constraintsolver.IntVar;
import com.google.ortools.constraintsolver.RoutingDimension;
import com.google.ortools.constraintsolver.RoutingIndexManager;
import com.google.ortools.constraintsolver.RoutingModel;
import com.google.ortools.constraintsolver.RoutingSearchParameters;
import com.google.ortools.constraintsolver.main;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.LongBinaryOperator;
import java.util.function.LongUnaryOperator;
import java.util.logging.Logger;



public class Algoritmo {
    private static Logger logger = Logger.getLogger(Algoritmo.class.getName());


    public static void main(String[] args){
        ArrayList<Pedido> pedidos = Pedido.leerPedidos("data\\pedidos\\ventas202109.txt");
        ArrayList<Pedido> pedidosSeleccionados = Pedido.priorizarPedidos(pedidos);
    }
}
