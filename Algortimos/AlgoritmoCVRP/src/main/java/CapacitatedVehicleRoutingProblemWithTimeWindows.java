import com.google.ortools.Loader;
import com.google.ortools.constraintsolver.Assignment;
import com.google.ortools.constraintsolver.FirstSolutionStrategy;
import com.google.ortools.constraintsolver.IntVar;
import com.google.ortools.constraintsolver.RoutingDimension;
import com.google.ortools.constraintsolver.RoutingIndexManager;
import com.google.ortools.constraintsolver.RoutingModel;
import com.google.ortools.constraintsolver.RoutingSearchParameters;
import com.google.ortools.constraintsolver.main;

import java.util.*;
import java.util.function.LongBinaryOperator;
import java.util.function.LongUnaryOperator;
import java.util.logging.Logger;

// A pair class

class Pair<K, V> {
    final K first;
    final V second;

    public static <K, V> Pair<K, V> of(K element0, V element1) {
        return new Pair<K, V>(element0, element1);
    }

    public Pair(K element0, V element1) {
        this.first = element0;
        this.second = element1;
    }
}

/**
 * Sample showing how to model and solve a capacitated vehicle routing problem with time windows
 * using the swig-wrapped version of the vehicle routing library in src/constraint_solver.
 */
public class CapacitatedVehicleRoutingProblemWithTimeWindows {
    private static Logger logger =
            Logger.getLogger(CapacitatedVehicleRoutingProblemWithTimeWindows.class.getName());

    // Locations representing either an order location or a vehicle route
    // start/end.
    private List<Pair<Integer, Integer>> locations = new ArrayList();

    // Quantity to be picked up for each order.
    private List<Integer> orderDemands = new ArrayList();
    // Time window in which each order must be performed.
    private List<Pair<Integer, Integer>> orderTimeWindows = new ArrayList();
    // Penalty cost "paid" for dropping an order.
    private List<Integer> orderPenalties = new ArrayList();

    // Capacity of the vehicles.
    private int vehicleCapacity = 0;
    // Latest time at which each vehicle must end its tour.
    private List<Integer> vehicleEndTime = new ArrayList();
    // Cost per unit of distance of each vehicle.
    private List<Integer> vehicleCostCoefficients = new ArrayList();
    private long vehicleCapacities[];
    // Vehicle start and end indices. They have to be implemented as int[] due
    // to the available SWIG-ed interface.
    private int vehicleStarts[];
    private int vehicleEnds[];
    // Random number generator to produce data.
    private final Random randomGenerator = new Random(0xBEEF);

    private Map<Integer, Node> mapeoIndexNode =  new HashMap();

    private int nVehicles;
    private int nOrders;

    /**
     * Creates a Manhattan Distance evaluator with 'costCoefficient'.
     *
     * @param manager Node Index Manager.
     * @param costCoefficient The coefficient to apply to the evaluator.
     */
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

    /**
     * Creates order data. Location of the order is random, as well as its demand (quantity), time
     * window and penalty.
     *
     * @param numberOfOrders number of orders to build.
     * @param xMax maximum x coordinate in which orders are located.
     * @param yMax maximum y coordinate in which orders are located.
     * @param demandMax maximum quantity of a demand.
     * @param timeWindowMax maximum starting time of the order time window.
     * @param timeWindowWidth duration of the order time window.
     * @param penaltyMin minimum pernalty cost if order is dropped.
     * @param penaltyMax maximum pernalty cost if order is dropped.
     */
    private void buildOrders(int numberOfOrders, int xMax, int yMax, int demandMax, int timeWindowMax,
                             int timeWindowWidth, int penaltyMin, int penaltyMax) {
        logger.info("Building orders.");
        ArrayList<Pedido> pedidos = Pedido.leerPedidos("data\\pedidos\\testAlgoritmo.txt");
        nOrders = pedidos.size();
        for (int i = 0; i < pedidos.size(); i++) {
            Pedido p = pedidos.get(i);
            mapeoIndexNode.put(locations.size(),new Node(p.node.coordX, p.node.coordY));
            locations.add(Pair.of(p.node.coordX, p.node.coordY));
            orderDemands.add(p.demanda * -1);
            int timeWindowStart = p.tiempoPedido.getHour()*60 + p.tiempoPedido.getMinute();
            int timeWindowEnd = timeWindowStart + p.plazoHoras * 60;
            orderTimeWindows.add(Pair.of(timeWindowStart, timeWindowEnd));
            //orderPenalties.add(randomGenerator.nextInt(penaltyMax - penaltyMin + 1) + penaltyMin);
            orderPenalties.add(500);
        }

       /* for (int order = 0; order < numberOfOrders; ++order) {
            locations.add(Pair.of(randomGenerator.nextInt(xMax + 1), randomGenerator.nextInt(yMax + 1)));
            orderDemands.add(randomGenerator.nextInt(demandMax + 1) * -1);
            int timeWindowStart = randomGenerator.nextInt(timeWindowMax + 1);
            orderTimeWindows.add(Pair.of(timeWindowStart, timeWindowStart + timeWindowWidth));
            orderPenalties.add(randomGenerator.nextInt(penaltyMax - penaltyMin + 1) + penaltyMin);
        }*/
    }

    /**
     * Creates fleet data. Vehicle starting and ending locations are random, as well as vehicle costs
     * per distance unit.
     *
     * @param numberOfVehicles
     * @param xMax maximum x coordinate in which orders are located.
     * @param yMax maximum y coordinate in which orders are located.
     * @param endTime latest end time of a tour of a vehicle.
     * @param capacity capacity of a vehicle.
     * @param costCoefficientMax maximum cost per distance unit of a vehicle (mimimum is 1),
     */

    private void buildFleet(
            int numberOfVehicles, int xMax, int yMax, int endTime, int capacity, int costCoefficientMax) {
        logger.info("Building fleet.");
        ArrayList<Cisterna> cisternas = Cisterna.cargarFlota("data\\flota\\flota_1.txt");
        nVehicles = cisternas.size();
        vehicleStarts = new int[nVehicles];
        vehicleEnds = new int[nVehicles];
        vehicleCapacities = new long[nVehicles];
        for (int i = 0; i < cisternas.size(); i++) {
            Cisterna c = cisternas.get(i);
            vehicleCapacities[i] = c.capacity;
            vehicleStarts[i] = locations.size();
            mapeoIndexNode.put(locations.size(),new Node(c.start.coordX, c.start.coordY));
            locations.add(Pair.of(c.start.coordX, c.start.coordY));
            vehicleEnds[i] = locations.size();
            mapeoIndexNode.put(locations.size(),new Node(c.end.coordX, c.end.coordY));
            locations.add(Pair.of(c.end.coordX, c.end.coordY));
            vehicleEndTime.add(endTime);
            //vehicleCostCoefficients.add(randomGenerator.nextInt(costCoefficientMax) + 1);
            vehicleCostCoefficients.add(1);
        }

        /*vehicleCapacity = capacity;
        vehicleStarts = new int[numberOfVehicles];
        vehicleEnds = new int[numberOfVehicles];
        for (int vehicle = 0; vehicle < numberOfVehicles; ++vehicle) {
            vehicleStarts[vehicle] = locations.size();
            locations.add(Pair.of(randomGenerator.nextInt(xMax + 1), randomGenerator.nextInt(yMax + 1)));
            vehicleEnds[vehicle] = locations.size();
            locations.add(Pair.of(randomGenerator.nextInt(xMax + 1), randomGenerator.nextInt(yMax + 1)));
            vehicleEndTime.add(endTime);
            vehicleCostCoefficients.add(randomGenerator.nextInt(costCoefficientMax) + 1);
        }*/
    }

    /** Solves the current routing problem. */
    private void solve(final int numberOfOrders, final int numberOfVehicles) {
        logger.info(
                "Creating model with " + numberOfOrders + " orders and " + numberOfVehicles + " vehicles.");
        // Finalizing model
        final int numberOfLocations = locations.size();

        RoutingIndexManager manager =
                new RoutingIndexManager(numberOfLocations, numberOfVehicles, vehicleStarts, vehicleEnds);

        RoutingModel model = new RoutingModel(manager);

        // Setting up dimensions
        final int bigNumber = 100000;
        final LongBinaryOperator callback = buildManhattanCallback(manager, 1);
        final String timeStr = "time";
        model.addDimension(
                model.registerTransitCallback(callback), bigNumber, bigNumber, false, timeStr);
        RoutingDimension timeDimension = model.getMutableDimension(timeStr);

        LongUnaryOperator demandCallback = new LongUnaryOperator() {
            public long applyAsLong(long index) {
                try {
                    int node = manager.indexToNode(index);
                    if (node < numberOfOrders) {
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
//        model.addDimension(
//                model.registerUnaryTransitCallback(demandCallback), 0, vehicleCapacity, false, capacityStr);
//        RoutingDimension capacityDimension = model.getMutableDimension(capacityStr);

        model.addDimensionWithVehicleCapacity(
                model.registerUnaryTransitCallback(demandCallback), 0, vehicleCapacities, false, capacityStr);
        RoutingDimension capacityDimension = model.getMutableDimension(capacityStr);

        // Setting up vehicles
        LongBinaryOperator[] callbacks = new LongBinaryOperator[numberOfVehicles];
        for (int vehicle = 0; vehicle < numberOfVehicles; ++vehicle) {
            final int costCoefficient = vehicleCostCoefficients.get(vehicle);
            callbacks[vehicle] = buildManhattanCallback(manager, costCoefficient);
            final int vehicleCost = model.registerTransitCallback(callbacks[vehicle]);
            model.setArcCostEvaluatorOfVehicle(vehicleCost, vehicle);
            timeDimension.cumulVar(model.end(vehicle)).setMax(vehicleEndTime.get(vehicle));
        }

        // Setting up orders
        for (int order = 0; order < numberOfOrders; ++order) {
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
            for (int order = 0; order < numberOfOrders; ++order) {
                if (solution.value(model.nextVar(order)) == order) {
                    dropped += " " + order;
                }
            }
            if (dropped.length() > 0) {
                output += "Dropped orders:" + dropped + "\n";
            }
            // Routes
            for (int vehicle = 0; vehicle < numberOfVehicles; ++vehicle) {
                String route = "Vehicle " + vehicle + ": ";
                long order = model.start(vehicle);
                // Empty route has a minimum of two nodes: Start => End
                if (model.isEnd(solution.value(model.nextVar(order)))) {
                    route += "Empty";
                } else {
                    IntVar cargaInicial = capacityDimension.cumulVar(order);
                    IntVar tiempoInicial = timeDimension.cumulVar(order);
                    route += mapeoIndexNode.get((int)order).toString() + " Carga inicial(" + solution.value(cargaInicial) + ") "
                            + "Time(" + solution.min(tiempoInicial) + ", " + solution.max(tiempoInicial) + ") -> ";
                    order = solution.value(model.nextVar(order));
                    for (; !model.isEnd(order); order = solution.value(model.nextVar(order))) {
                        IntVar load = capacityDimension.cumulVar(order);
                        IntVar time = timeDimension.cumulVar(order);
                        IntVar nextLoad = capacityDimension.cumulVar(solution.value(model.nextVar(order)));
                        long delta = solution.value(load) - solution.value(nextLoad);
                        route += mapeoIndexNode.get((int)order).toString() + " Descargar(" + delta + ") "
                                + "Time(" + solution.min(time) + ", " + solution.max(time) + ") -> ";
                    }
                    IntVar load = capacityDimension.cumulVar(order);
                    IntVar time = timeDimension.cumulVar(order);
                    route += mapeoIndexNode.get((int)order).toString() + " Carga Final(" + solution.value(load) + ") "
                            + "Time(" + solution.min(time) + ", " + solution.max(time) + ")";
                }
                output += route + "\n";
            }
            logger.info(output);
        }
    }

    public static void main(String[] args) throws Exception {
        Loader.loadNativeLibraries();
        CapacitatedVehicleRoutingProblemWithTimeWindows problem =
                new CapacitatedVehicleRoutingProblemWithTimeWindows();
        final int xMax = 20;
        final int yMax = 20;
        final int demandMax = 3;
        final int timeWindowMax = 100 * 60;
        final int timeWindowWidth = 4 * 60;
        final int penaltyMin = 50;
        final int penaltyMax = 100;
        final int endTime = 24 * 60;
        final int costCoefficientMax = 3;

        final int orders = 100;
        final int vehicles = 20;
        final int capacity = 50;

        problem.buildOrders(
                orders, xMax, yMax, demandMax, timeWindowMax, timeWindowWidth, penaltyMin, penaltyMax);
        problem.buildFleet(vehicles, xMax, yMax, endTime, capacity, costCoefficientMax);
        problem.solve(problem.nOrders, problem.nVehicles);
    }
}