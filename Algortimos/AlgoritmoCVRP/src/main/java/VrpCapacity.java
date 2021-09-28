//package com.google.ortools.constraintsolver.samples;
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

import java.io.SyncFailedException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import org.javatuples.Pair;

/** Minimal VRP. */

public final class VrpCapacity {
    public final Node plantaPrincipal = new Node(0,0);

    private static final Logger logger = Logger.getLogger(VrpCapacity.class.getName());

    static class DataModel {
        public int mapSizeX = 3;
        public int mapSizeY = 5;
        public long[][] distanceMatrix = null;
        public long[] demands = {0, 1, 1, 2, 4, 0, 1, 0, 4, 5, 7, 8, 9, 1, 1};
        public long[] vehicleCapacities = {5, 5, 5, 5};
        public int vehicleNumber = 4;
        public int depot = 0;
    }

    /// @brief Print the solution.
    static void printSolution(
            DataModel data, RoutingModel routing, RoutingIndexManager manager, Assignment solution, Map <Integer,Integer>pedidos) {
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
                //routeLoad += data.demands[(int) nodeIndex];
                routeLoad += pedidos.get(nodeIndex);
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
        logger.info("Total load of all routes: " + totalLoad);
    }

    public static void main(String[] args) throws Exception {
        DataModel data = new DataModel();
        Map<Integer, Integer> pedidos = Pedido.leerPedidos("data\\pedidos\\pedidos.txt", data.mapSizeY);
        System.out.println(pedidos.size());
        Graph mapa = new Graph(data.mapSizeX*data.mapSizeY);
        for(int i=0; i<15;i++){
            mapa.addVertax(String.valueOf(i));
        }

        List<Node> listaNodos = Node.cargarBloqueados("data\\bloqueos\\prueba1.txt");

        DistanceMatrix mapaPrueb = new DistanceMatrix(data.mapSizeX,data.mapSizeY,null,null, listaNodos);
        for (int i=0; i<mapaPrueb.matrixSize ;i++){
            for (int j=0; j<mapaPrueb.matrixSize;j++){
                if (i!=j){
                    mapa.addEdges(i,j,(int)mapaPrueb.matrix[i][j]);
                }
            }
        }
        Table<Integer, Integer, Pair <Integer, ArrayList<Integer>>> routes = HashBasedTable.create();

        mapa.dijkStra(1);
        System.out.println(mapa.getDistance(12));
        System.out.println(mapa.getPath(12));
        ArrayList<Integer> route = Graph.getRoute(mapa.getPath(12));


        /*AStar a = new AStar();
        double valor = a.aStar(mapaPrueb.matrix,0,7, mapaPrueb.mapSizeX,mapaPrueb.mapSizeY);
        System.out.println(valor);*/


        Loader.loadNativeLibraries();
        // Instantiate the data problem.
        //DataModel data = new DataModel();
        data.distanceMatrix = mapaPrueb.matrix;
        //DistanceMatrix distanceMatrixAux = new DistanceMatrix(3, 5, null, null, null);
        //long[][] distanceMatrix = ;
        //distanceMatrixAux.print();
        // Create Routing Index Manager
        ///data.distanceMatrix = distanceMatrix;

        RoutingIndexManager manager = new RoutingIndexManager(pedidos.size()+1, data.vehicleNumber, data.depot);
        // Create Routing Model.
        RoutingModel routing = new RoutingModel(manager);

        // Create and register a transit callback
        final int transitCallbackIndex =
                routing.registerTransitCallback((long fromIndex, long toIndex) -> {
                    // Convert from routing variable Index to user NodeIndex.
                    int fromNode = manager.indexToNode(fromIndex);
                    int toNode = manager.indexToNode(toIndex);
                    if(!routes.contains(fromNode,toNode)){
                        mapa.dijkStra(fromNode);
                        Integer distancia = (int)mapa.getDistance(toNode);
                        ArrayList<Integer> ruta = Graph.getRoute(mapa.getPath(toNode));
                        routes.put(fromNode,toNode,new Pair<>(distancia,ruta));
                    }
                    return routes.get(fromNode, toNode).getValue0();
                });

        // Define cost of each arc.
        routing.setArcCostEvaluatorOfAllVehicles(transitCallbackIndex);

        // Add Capacity constraint.
        final int demandCallbackIndex = routing.registerUnaryTransitCallback((long fromIndex) -> {
            // Convert from routing variable Index to user NodeIndex.
            int fromNode = manager.indexToNode(fromIndex);
            if (pedidos.containsKey(fromNode)){
                return pedidos.get(fromNode);
            }else{
                pedidos.put(fromNode, 0);
                return 0;
            }
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

        // Print solution on console.
        printSolution(data, routing, manager, solution, pedidos);
    }

    private VrpCapacity() {}
}
