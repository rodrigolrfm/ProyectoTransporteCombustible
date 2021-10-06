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
            DataModel data, RoutingModel routing, RoutingIndexManager manager, Assignment solution, BiMap<Integer, Integer> mapeo,
            Table<Integer, Integer, Pair <Integer, ArrayList<Integer>>> routes) {
        // Solution cost.
        logger.info("Objective: " + solution.objectiveValue());
        // Inspect solution.
        long totalDistance = 0;
        long totalLoad = 0;
        for (int i = 0; i < data.vehicleNumber; ++i) {
            long index = routing.start(i);
            logger.info("Route for Vehicle " + i + ":");
            long routeDistance = 0;
            //long routeLoad = 0;
            long routeLoad = data.vehicleCapacities[i];
            String route = "";
            int nIteracion = 0;
            while (!routing.isEnd(index)) {
                long nodeIndex = manager.indexToNode(index);
                routeLoad -= data.demands[(int) nodeIndex];
                route += mapeo.inverse().get((int) nodeIndex)  + " Carga(" + routeLoad + ") -> ";
                long previousIndex = index;
                index = solution.value(routing.nextVar(index));

                long toNode = manager.indexToNode(index);
                String rutaIntermedia = Node.getRoute(routes.get((int)nodeIndex,(int)toNode).getValue1());
                route += rutaIntermedia;
                routeDistance += routing.getArcCostForVehicle(previousIndex, index, i);
            }
            route += mapeo.inverse().get(manager.indexToNode(routing.end(i)));
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
        Graph mapa = new Graph(data.mapSizeX*data.mapSizeY);
        for(int i=0; i<data.mapSizeX*data.mapSizeY;i++){
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
        BiMap<Integer, Integer> mapeo = HashBiMap.create();
        //planta
        mapeo.put(0,0);
        data.demands = new long[pedidos.size()+1];
        data.demands[0] = 0;
        //pedidos
        int i = 1;
        for (Map.Entry<Integer,Integer> entry:
             pedidos.entrySet()) {
               mapeo.put(entry.getKey(), i);
               data.demands[i] = entry.getValue();
               ++i;
        }

        data.distanceMatrix = new long[i][i];
        for(int k=0; k<i; ++k){
            for(int w=0; w<i; ++w){
                if (w==k){
                    routes.put(k, w, new Pair<>(0, null));
                    data.distanceMatrix[w][k] = 0;
                }else{
                    if(!(routes.contains(k,w)||routes.contains(w,k))){
                        mapa.reset();
                        mapa.dijkStra(mapeo.inverse().get(w));
                        for(int h=w+1; h<i; ++h){
                            int distance = (int) mapa.getDistance(mapeo.inverse().get(h));
                            ArrayList<Integer> ruta = Graph.getRoute(mapa.getPath(mapeo.inverse().get(h)));
                            routes.put(w,h,new Pair<>(distance, new ArrayList<>(ruta)));
                            data.distanceMatrix[w][h] = distance;
                            data.distanceMatrix[h][w] = distance;
                            Collections.reverse(ruta);
                            routes.put(h,w,new Pair<>(distance, new ArrayList<>(ruta)));
                        }
                    }else if(routes.get(k,w).getValue0() > 100000000){
                        mapa.reset();
                        mapa.dijkStra(mapeo.inverse().get(k));
                        int distance = (int) mapa.getDistance(mapeo.inverse().get(w));
                        ArrayList<Integer> ruta = Graph.getRoute(mapa.getPath(mapeo.inverse().get(w)));
                        routes.put(k,w,new Pair<>(distance, new ArrayList<>(ruta)));
                        data.distanceMatrix[w][k] = distance;
                        data.distanceMatrix[k][w] = distance;
                        Collections.reverse(ruta);
                        routes.put(w,k,new Pair<>(distance, new ArrayList<>(ruta)));
                    }
                }
            }
        }
        /*
        for (Map.Entry<Integer,Integer> entry:
             mapeo.entrySet() ){
            System.out.print(entry.getKey()+" "+entry.getValue());
            System.out.println();
        }
        System.out.println(routes.get(4,7).getValue0());
        */
        /*
        for(int k = 0; k<data.distanceMatrix.length; ++k) {
            for (int w = 0; w < data.distanceMatrix.length; ++w) {
                System.out.print(data.distanceMatrix[k][w] + " ");
            }
            System.out.println();
        }
        */

        /*AStar a = new AStar();
        double valor = a.aStar(mapaPrueb.matrix,0,7, mapaPrueb.mapSizeX,mapaPrueb.mapSizeY);
        System.out.println(valor);*/


        Loader.loadNativeLibraries();
        // Instantiate the data problem.
        //DataModel data = new DataModel();

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

        // Print solution on console.
        printSolution(data, routing, manager, solution, mapeo, routes);
    }

    private VrpCapacity() {}
}
