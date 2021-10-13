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
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.LongBinaryOperator;
import java.util.function.LongUnaryOperator;
import java.util.logging.Logger;



public class Algoritmo {
    private static Logger logger = Logger.getLogger(Algoritmo.class.getName());
    private long[] capacities;
    private final int nVehicles = 20;
    private List<Integer> vehicleEndTime = new ArrayList();
    private List<Pair<Integer, Integer>> locations = new ArrayList();
    private List<Integer> orderDemands = new ArrayList();
    private List<Pair<Integer, Integer>> orderTimeWindows = new ArrayList();
    private List<Integer> orderPenalties = new ArrayList();
    private int nPedidos;
    private int depot;
    private List<Integer> vehicleCostCoefficients = new ArrayList();

    private LongBinaryOperator buildManhattanCallback(
            RoutingIndexManager manager, int costCoefficient) {
        return new LongBinaryOperator() {
            public long applyAsLong(long firstIndex, long secondIndex) {
                try {
                    int firstNode = manager.indexToNode(firstIndex);
                    int secondNode = manager.indexToNode(secondIndex);
                    Pair<Integer, Integer> firstLocation = locations.get(firstNode);
                    Pair<Integer, Integer> secondLocation = locations.get(secondNode);
                    return (long) costCoefficient
                            * (Math.abs(firstLocation.first - secondLocation.first)
                            + Math.abs(firstLocation.second - secondLocation.second));
                } catch (Throwable throwed) {
                    logger.warning(throwed.getMessage());
                    return 0;
                }
            }
        };
    }

    private void buildFleet(int endTime){
        capacities = new long[nVehicles];

        for (int i = 0; i < 2; ++i){
            vehicleEndTime.add(endTime);
            capacities[i] = 25;
        }

        for (int i = 2; i < 6; ++i){
            vehicleEndTime.add(endTime);
            capacities[i] = 15;
        }

        for (int i = 6; i < 10; ++i){
            vehicleEndTime.add(endTime);
            capacities[i] = 10;
        }

        for (int i = 10; i < 20; ++i){
            vehicleEndTime.add(endTime);
            capacities[i] = 5;
        }
    }

    private void buildOrders(ArrayList<Pedido> pedidos){
        //depot
        locations.add(Pair.of(12,8));
        depot = 0;
        pedidos.forEach((pedido) -> {
            locations.add(Pair.of(pedido.node.coordX, pedido.node.coordY));
            orderDemands.add(pedido.demanda);
            int timeWindowStart = pedido.tiempoPedido.getHour()*60 + pedido.tiempoPedido.getMinute();
            int timeWindowEnd = timeWindowStart + pedido.plazoHoras * 60;
            orderTimeWindows.add(Pair.of(timeWindowStart, timeWindowEnd));
            orderPenalties.add(60);
        });
        nPedidos = pedidos.size();
    }

    private void solve() {
        logger.info(
                "Resolviendo para " + nPedidos + " pedidos y " + nVehicles + " vehiculos.");
        final int numberOfLocations = locations.size();
        RoutingIndexManager manager =
                new RoutingIndexManager(numberOfLocations, nVehicles, depot);
        RoutingModel model = new RoutingModel(manager);
        final int bigNumber = 1000000000;

        final LongBinaryOperator callback = buildManhattanCallback(manager, 1);
        final String timeStr = "time";
        model.addDimension(
                model.registerTransitCallback(callback), bigNumber, bigNumber, false, timeStr);

        RoutingDimension timeDimension = model.getMutableDimension(timeStr);

        LongUnaryOperator demandCallback = new LongUnaryOperator() {
            public long applyAsLong(long index) {
                try {
                    int node = manager.indexToNode(index);
                    if (node < nPedidos) {
                        return orderDemands.get(node);
                    }
                    return 0;
                } catch (Throwable throwed) {
                    logger.warning(throwed.getMessage());
                    return 0;
                }
            }
        };

        final String capacityStr = "capacity";

        model.addDimensionWithVehicleCapacity(model.registerUnaryTransitCallback(demandCallback), 0, capacities, true,
                capacityStr);

        RoutingDimension capacityDimension = model.getMutableDimension(capacityStr);

        // Setting up vehicles
        LongBinaryOperator[] callbacks = new LongBinaryOperator[nVehicles];
        for (int vehicle = 0; vehicle < nVehicles; ++vehicle) {
            final int costCoefficient = 2;
            callbacks[vehicle] = buildManhattanCallback(manager, costCoefficient);
            final int vehicleCost = model.registerTransitCallback(callbacks[vehicle]);
            model.setArcCostEvaluatorOfVehicle(vehicleCost, vehicle);
            timeDimension.cumulVar(model.end(vehicle)).setMax(vehicleEndTime.get(vehicle));
        }

        // Setting up orders
        for (int order = 0; order < nPedidos; ++order) {
            timeDimension.cumulVar(order).setRange(
                    orderTimeWindows.get(order).first, orderTimeWindows.get(order).second);
            long[] orderIndices = {manager.nodeToIndex(order)};
            model.addDisjunction(orderIndices, orderPenalties.get(order));
        }

        // Solving
        RoutingSearchParameters parameters =
                main.defaultRoutingSearchParameters()
                        .toBuilder()
                        .setFirstSolutionStrategy(FirstSolutionStrategy.Value.ALL_UNPERFORMED)
                        .build();

        logger.info("Search");
        Assignment solution = model.solveWithParameters(parameters);

        if (solution != null) {
            String output = "Total cost: " + solution.objectiveValue() + "\n";
            // Dropped orders
            String dropped = "";
            for (int order = 0; order < nPedidos; ++order) {
                if (solution.value(model.nextVar(order)) == order) {
                    dropped += " " + order;
                }
            }
            if (dropped.length() > 0) {
                output += "Dropped orders:" + dropped + "\n";
            }
            // Routes
            for (int vehicle = 0; vehicle < nVehicles; ++vehicle) {
                String route = "Vehicle " + vehicle + ": ";
                long order = model.start(vehicle);
                // Empty route has a minimum of two nodes: Start => End
                if (model.isEnd(solution.value(model.nextVar(order)))) {
                    route += "Empty";
                } else {
                    for (; !model.isEnd(order); order = solution.value(model.nextVar(order))) {
                        IntVar load = capacityDimension.cumulVar(order);
                        IntVar time = timeDimension.cumulVar(order);
                        route += order + " Load(" + solution.value(load) + ") "
                                + "Time(" + solution.min(time) + ", " + solution.max(time) + ") -> ";
                    }
                    IntVar load = capacityDimension.cumulVar(order);
                    IntVar time = timeDimension.cumulVar(order);
                    route += order + " Load(" + solution.value(load) + ") "
                            + "Time(" + solution.min(time) + ", " + solution.max(time) + ")";
                }
                output += route + "\n";
            }
            logger.info(output);
        }
    }

    public static void main(String[] args){
        Loader.loadNativeLibraries();
        Algoritmo algoritmo = new Algoritmo();
        ArrayList<Pedido> pedidos = Pedido.leerPedidos("data\\pedidos\\testAlgoritmo.txt");
        //ArrayList<Pedido> pedidosSeleccionados = Pedido.priorizarPedidos(pedidos);
        algoritmo.buildFleet(10000000);
        algoritmo.buildOrders(pedidos);
        algoritmo.solve();
    }


}
