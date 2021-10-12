//package com.google.ortools.constraintsolver.samples;
import com.google.common.collect.HashBiMap;
import com.google.ortools.Loader;
import com.google.ortools.constraintsolver.Assignment;
import com.google.ortools.constraintsolver.FirstSolutionStrategy;
import com.google.ortools.constraintsolver.LocalSearchMetaheuristic;
import com.google.ortools.constraintsolver.RoutingIndexManager;
import com.google.ortools.constraintsolver.RoutingModel;
import com.google.ortools.constraintsolver.RoutingSearchParameters;
import com.google.ortools.constraintsolver.main;
import com.google.protobuf.Duration;
import com.google.protobuf.StringValue;
import com.google.common.collect.Table;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.BiMap;

import java.io.SyncFailedException;
import java.lang.reflect.Array;
import java.util.*;
import java.util.logging.Logger;
import org.javatuples.Pair;

/** Minimal VRP. */

public final class VrpCapacity1 {
    public final Node plantaPrincipal = new Node(0,0);

    private static final Logger logger = Logger.getLogger(VrpCapacity1.class.getName());

    static class DataModel {
        public int mapSizeX = 70;
        public int mapSizeY = 50;
        public long[][] distanceMatrix = null;
        public long[] demands = null;
        public long[] vehicleCapacities = null;
        public int vehicleNumber = 0;
        //Para 50
        //public long[] vehicleCapacities = {41, 41, 41, 41, 41, 41, 41, 41, 41};
        //public int vehicleNumber = 9;
        //Para 100
        //public long[] vehicleCapacities = {52,52,52,52,52,52,52,52,52,52,52,52,52,52,52};
        //public int vehicleNumber = 15;

        //public long[] vehicleCapacities = {53,53,53,53,53,53,53,53,53,53,53,53,53,53,53,53,53,53,53,53,53,53,53,53,53,53,53,53,53,53,53,53,53,53,53,53,53,53,53,53,53,53,53,53,53,53,53,53,53,53,53,53,53,53,53,53,53,53,53,53,53,53,53,53,53,53,53,53,53,53,53,53,53,53,53,53,53,53,53};
        //public int vehicleNumber = 79;
        public int depot = 0;
    }

    /// @brief Print the solution.
    static void printSolution(
            DataModel data, RoutingModel routing, RoutingIndexManager manager, Assignment solution) {
        // Solution cost.
        logger.info("Objective: " + solution.objectiveValue());
        // Inspect solution.
        long totalDistance = 0;
        long totalLoad = 0;
        for (int i = 0; i < data.vehicleNumber; ++i) {
            long index = routing.start(i);
            logger.info("Route for Vehicle " + i + ":");
            long routeDistance = 0;
            long routeLoad = 0;
            String route = "";
            while (!routing.isEnd(index)) {
                long nodeIndex = manager.indexToNode(index);
                routeLoad += data.demands[(int) nodeIndex];
                route += nodeIndex + " Load(" + routeLoad + ") -> ";
                long previousIndex = index;
                index = solution.value(routing.nextVar(index));
                routeDistance += routing.getArcCostForVehicle(previousIndex, index, i);
            }
            route += manager.indexToNode(routing.end(i));
            logger.info(route);
            logger.info("Distance of the route: " + routeDistance + "m");
            totalDistance += routeDistance;
            totalLoad += routeLoad;
        }
        logger.info("Total distance of all routes: " + totalDistance + "m");
        //logger.info("Total load of all routes: " + totalLoad);
    }

    public static void main(String[] args) throws Exception {
        int nPed = 500;
        String archivo = new String(Integer.toString(nPed)+"pedidos.txt");

        long start = System.currentTimeMillis();
        DataModel data = new DataModel();
        ArrayList<Pair<Node, Integer>> pedidos = Pedido.leerPedidosExp("data\\pedidos\\" + archivo, data.mapSizeY);

        if (nPed == 50){
            data.vehicleCapacities = new long[]{41, 41, 41, 41, 41, 41, 41, 41, 41};
            data.vehicleNumber = 9;
        }

        if(nPed == 100){
            data.vehicleCapacities = new long[]{52,52,52,52,52,52,52,52,52,52,52,52,52,52,52};
            data.vehicleNumber = 15;
            //data.vehicleCapacities = new long[]{100,100,100,100,100,100,100,100,100};
            //data.vehicleNumber = 9;
        }

        if(nPed == 500){
            data.vehicleCapacities = new long[]{53,53,53,53,53,53,53,53,53,53,53,53,53,53,53,53,53,53,53,53,53,53,53,53,53,53,53,53,53,53,53,53,53,53,53,53,53,53,53,53,53,53,53,53,53,53,53,53,53,53,53,53,53,53,53,53,53,53,53,53,53,53,53,53,53,53,53,53,53,53,53,53,53,53,53,53,53,53,53};
            data.vehicleNumber = 79;
        }

        if(nPed == 1000){
            data.vehicleCapacities = new long[]{70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70};
            data.vehicleNumber = 156;
        }

        data.distanceMatrix = new long[pedidos.size()+1][pedidos.size()+1];
        data.demands = new long[pedidos.size()+1];
        for(int i = 0; i < pedidos.size(); i++){
            data.demands[i] = pedidos.get(i).getValue1();
            for(int j = 0; j < pedidos.size(); j++){
                Node nodo1 = pedidos.get(i).getValue0();
                Node nodo2 = pedidos.get(j).getValue0();
                data.distanceMatrix[i][j] = Math.abs(nodo2.coordY - nodo1.coordY) + Math.abs(nodo2.coordX - nodo1.coordX);
            }
        }

        Loader.loadNativeLibraries();

        RoutingIndexManager manager = new RoutingIndexManager(pedidos.size()+1, data.vehicleNumber, data.depot);
        // Create Routing Model.
        RoutingModel routing = new RoutingModel(manager);

        // Create and register a transit callback
        final int transitCallbackIndex =
                routing.registerTransitCallback((long fromIndex, long toIndex) -> {
                    // Convert from routing variable Index to user NodeIndex.
                    int fromNode = manager.indexToNode(fromIndex);
                    int toNode = manager.indexToNode(toIndex);
                    return data.distanceMatrix[fromNode][toNode];
                });

        // Define cost of each arc.
        routing.setArcCostEvaluatorOfAllVehicles(transitCallbackIndex);

        // Add Capacity constraint.
        final int demandCallbackIndex = routing.registerUnaryTransitCallback((long fromIndex) -> {
            // Convert from routing variable Index to user NodeIndex.
            int fromNode = manager.indexToNode(fromIndex);
            return data.demands[fromNode];
        });


        routing.addDimensionWithVehicleCapacity(demandCallbackIndex, 0, // null capacity slack
                data.vehicleCapacities, // vehicle maximum capacities
                true, // start cumul to zero
                "Capacity");

        // Setting first solution heuristic.
        RoutingSearchParameters searchParameters =
                main.defaultRoutingSearchParameters()
                        .toBuilder()
                        .setFirstSolutionStrategy(FirstSolutionStrategy.Value.PATH_CHEAPEST_ARC)
                        .setLocalSearchMetaheuristic(LocalSearchMetaheuristic.Value.GUIDED_LOCAL_SEARCH)
                        .setTimeLimit(Duration.newBuilder().setSeconds(1).build())
                        .build();

        // Solve the problem.
        Assignment solution = routing.solveWithParameters(searchParameters);

        long end = System.currentTimeMillis();
        // Print solution on console.
        printSolution(data, routing, manager, solution);
        System.out.println("Tiempo de ejecución: " + (end-start));
    }

    //private VrpCapacity1() {}
}
