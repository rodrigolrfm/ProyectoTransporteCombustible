
package pe.edu.pucp.algorithm;


import pe.edu.pucp.mvc.controllers.MapaModel;
import pe.edu.pucp.mvc.models.NodoModel;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.PriorityQueue;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class AStar {
    public static double peso = 1;
    public static final List<NodoModel> get_shortest_path(NodoModel start, NodoModel target, MapaModel mapaModelConfiguration, Date previousDeliveredDate, int velocity, NodoModel nodoprevio){
        PriorityQueue<NodoModel> closedList = new PriorityQueue<>();
        PriorityQueue<NodoModel> openList = new PriorityQueue<>();
        List<NodoModel> neighbourList = new ArrayList<>();
        
        if(start == null || target == null){
            System.err.println("Valores nulos de inicio o fin");
            return new ArrayList<>();
        }
        
        mapaModelConfiguration.clearRoute();
        start.setNodoprevio(null);
        target.setNodoprevio(null);
        double timeAdvance = (1.0/velocity)*3600;
                      
        start.setF(start.getG() + start.getDistancia(target));
        openList.add(start);
                
        while(!openList.isEmpty()){
            NodoModel n = openList.peek();
            if(n.equals(target)){
                target.setNodoprevio(n.getNodoprevio());
                break;
            }
            if(n.isBlocked(previousDeliveredDate)){
                neighbourList.add(nodoprevio);
            }else{
                neighbourList = graph_adjacentNeighbour(n, mapaModelConfiguration,previousDeliveredDate, target, timeAdvance);
            }
            for(NodoModel current: neighbourList){
                double totalWeight = n.getG() + peso;

                if(!openList.contains(current) && !closedList.contains(current)){
                    current.setNodoprevio(n);
                    current.setG(totalWeight);
                    current.setF(current.getG() + current.getDistancia(target));
                    openList.add(current);
                } else {
                    List<NodoModel> movs = new ArrayList<>();
                    for (NodoModel vertex = current.getNodoprevio(); vertex != null; vertex = vertex.getNodoprevio()) {
                        movs.add(vertex);
                    }
                    List<NodoModel> movements = new ArrayList<>(movs);
                    Collections.reverse(movements);
                    long partial_time = (((long)movements.size()*3600000/velocity));
                    Date checkDate = Date.from(previousDeliveredDate.toInstant().plus(partial_time, ChronoUnit.MILLIS));
                    if(totalWeight < current.getG() && !current.isBlocked(checkDate)){
                        current.setNodoprevio(n);
                        current.setG(totalWeight);
                        current.setF(current.getG() + current.getDistancia(target));

                        if(closedList.contains(current)){
                            closedList.remove(current);
                            openList.add(current);
                        }
                    }
                }
            }
            openList.remove(n);
            closedList.add(n);
        }
        openList.clear();
        List<NodoModel> movs = new ArrayList<>();
        for (NodoModel vertex = target.getNodoprevio(); vertex != null; vertex = vertex.getNodoprevio()) {
            movs.add(vertex);
        }
        List<NodoModel> movements = new ArrayList<>(movs);
        Collections.reverse(movements);
        return movements;
    }
    private static List<NodoModel> graph_adjacentNeighbour(NodoModel nodo, MapaModel mapaModelConfiguration, Date currentTime, NodoModel target, double timeAdvance) {

        List<NodoModel> neighbours = new ArrayList<>();
        currentTime = Date.from(currentTime.toInstant().plus((long)(timeAdvance),ChronoUnit.SECONDS));
        int i = nodo.getCoordenadaX();
        int j = nodo.getCoordenadaY();
        int dimensionXMax = mapaModelConfiguration.getDimensionX();
        int dimensionYMax = mapaModelConfiguration.getDimensionY();
        NodoModel nodo1;
        NodoModel nodo2;
        NodoModel nodo3;
        NodoModel nodo4;
        if((i==0) && (j==0)){
            nodo1= mapaModelConfiguration.getMapa()[i+1][j];
            nodo2= mapaModelConfiguration.getMapa()[i][j+1];
            NodoModel neighbour1 = NodoModel.builder().coordenadaX(nodo1.getCoordenadaX()).coordenadaY(nodo1.getCoordenadaY())
                    .inicioBloqueo(nodo1.getInicioBloqueo())
                    .finBloqueo(nodo1.getFinBloqueo())
                    .nodoprevio(nodo1.getNodoprevio())
                    .blockList(nodo1.getBlockList()).build();
            NodoModel neighbour2 = NodoModel.builder().coordenadaX(nodo2.getCoordenadaX()).coordenadaY(nodo2.getCoordenadaY())
                    .inicioBloqueo(nodo2.getInicioBloqueo())
                    .finBloqueo(nodo2.getFinBloqueo())
                    .nodoprevio(nodo2.getNodoprevio())
                    .blockList(nodo2.getBlockList()).build();
            if(neighbour1.equals(target)){
                neighbours.add(neighbour1);
            }else if(!neighbour1.isBlocked(currentTime)) {
                neighbours.add(neighbour1);
            }
            if(neighbour2.equals(target)){
                neighbours.add(neighbour2);
            }else if(!neighbour2.isBlocked(currentTime))
                neighbours.add(neighbour2);
        }else if((i==dimensionXMax-1)&& (j==dimensionYMax-1)){
            nodo1 = mapaModelConfiguration.getMapa()[i-1][j];
            nodo2 = mapaModelConfiguration.getMapa()[i][j-1];
            NodoModel n1 = NodoModel.builder().coordenadaX(nodo1.getCoordenadaX()).coordenadaY(nodo1.getCoordenadaY())
                    .inicioBloqueo(nodo1.getInicioBloqueo())
                    .finBloqueo(nodo1.getFinBloqueo())
                    .nodoprevio(nodo1.getNodoprevio())
                    .blockList(nodo1.getBlockList()).build();
            NodoModel n2 = NodoModel.builder().coordenadaX(nodo2.getCoordenadaX()).coordenadaY(nodo2.getCoordenadaY())
                    .inicioBloqueo(nodo2.getInicioBloqueo())
                    .finBloqueo(nodo2.getFinBloqueo())
                    .nodoprevio(nodo2.getNodoprevio())
                    .blockList(nodo2.getBlockList()).build();
            if(n1.equals(target)){
                neighbours.add(n1);
            }else if(!n1.isBlocked(currentTime))
                neighbours.add(n1);
            if(n2.equals(target)){
                neighbours.add(n2);
            }else if(!n2.isBlocked(currentTime))
                neighbours.add(n2);
        }else if((i==0)&&(j==dimensionYMax-1)){
            nodo1 = mapaModelConfiguration.getMapa()[i][j-1];
            nodo2 = mapaModelConfiguration.getMapa()[i+1][j];
            NodoModel n1 = NodoModel.builder().coordenadaX(nodo1.getCoordenadaX()).coordenadaY(nodo1.getCoordenadaY())
                    .inicioBloqueo(nodo1.getInicioBloqueo())
                    .finBloqueo(nodo1.getFinBloqueo())
                    .nodoprevio(nodo1.getNodoprevio())
                    .blockList(nodo1.getBlockList()).build();
            NodoModel n2 = NodoModel.builder().coordenadaX(nodo2.getCoordenadaX()).coordenadaY(nodo2.getCoordenadaY())
                    .inicioBloqueo(nodo2.getInicioBloqueo())
                    .finBloqueo(nodo2.getFinBloqueo())
                    .nodoprevio(nodo2.getNodoprevio())
                    .blockList(nodo2.getBlockList()).build();
            if(n1.equals(target)){
                neighbours.add(n1);
            }else if(!n1.isBlocked(currentTime))
                neighbours.add(n1);
            if(n2.equals(target)){
                neighbours.add(n2);
            }else if(!n2.isBlocked(currentTime))
                neighbours.add(n2);
        }else if((i==dimensionXMax-1)&&(j==0)) {
            nodo1= mapaModelConfiguration.getMapa()[i-1][j];
            nodo2= mapaModelConfiguration.getMapa()[i][j+1];
            NodoModel n1 = NodoModel.builder().coordenadaX(nodo1.getCoordenadaX()).coordenadaY(nodo1.getCoordenadaY())
                    .inicioBloqueo(nodo1.getInicioBloqueo())
                    .finBloqueo(nodo1.getFinBloqueo())
                    .nodoprevio(nodo1.getNodoprevio())
                    .blockList(nodo1.getBlockList()).build();
            NodoModel n2 = NodoModel.builder().coordenadaX(nodo2.getCoordenadaX()).coordenadaY(nodo2.getCoordenadaY())
                    .inicioBloqueo(nodo2.getInicioBloqueo())
                    .finBloqueo(nodo2.getFinBloqueo())
                    .nodoprevio(nodo2.getNodoprevio())
                    .blockList(nodo2.getBlockList()).build();
            if(n1.equals(target)){
                neighbours.add(n1);
            }else if(!n1.isBlocked(currentTime))
                neighbours.add(n1);
            if(n2.equals(target)){
                neighbours.add(n2);
            }else if(!n2.isBlocked(currentTime))
                neighbours.add(n2);
        }else if((i!=0)&&(j==0)) {
            nodo1= mapaModelConfiguration.getMapa()[i-1][j];
            nodo2= mapaModelConfiguration.getMapa()[i+1][j];
            nodo3= mapaModelConfiguration.getMapa()[i][j+1];
            NodoModel n1 = NodoModel.builder().coordenadaX(nodo1.getCoordenadaX()).coordenadaY(nodo1.getCoordenadaY())
                    .inicioBloqueo(nodo1.getInicioBloqueo())
                    .finBloqueo(nodo1.getFinBloqueo())
                    .nodoprevio(nodo1.getNodoprevio())
                    .blockList(nodo1.getBlockList()).build();
            NodoModel n2 = NodoModel.builder().coordenadaX(nodo2.getCoordenadaX()).coordenadaY(nodo2.getCoordenadaY())
                    .inicioBloqueo(nodo2.getInicioBloqueo())
                    .finBloqueo(nodo2.getFinBloqueo())
                    .nodoprevio(nodo2.getNodoprevio())
                    .blockList(nodo2.getBlockList()).build();
            NodoModel n3 = NodoModel.builder().coordenadaX(nodo3.getCoordenadaX()).coordenadaY(nodo3.getCoordenadaY())
                    .inicioBloqueo(nodo3.getInicioBloqueo())
                    .finBloqueo(nodo3.getFinBloqueo())
                    .nodoprevio(nodo3.getNodoprevio())
                    .blockList(nodo3.getBlockList()).build();
            if(n1.equals(target)){
                neighbours.add(n1);
            }else if(!n1.isBlocked(currentTime))
                neighbours.add(n1);
            if(n2.equals(target)){
                neighbours.add(n2);
            }else if(!n2.isBlocked(currentTime))
                neighbours.add(n2);
            if(n3.equals(target)){
                neighbours.add(n3);
            }else if(!n3.isBlocked(currentTime))
                neighbours.add(n3);
        }else if((i == 0)&&(j != 0)) {
            nodo1= mapaModelConfiguration.getMapa()[i+1][j];
            nodo2= mapaModelConfiguration.getMapa()[i][j-1];
            nodo3= mapaModelConfiguration.getMapa()[i][j+1];
            NodoModel n1 = NodoModel.builder().coordenadaX(nodo1.getCoordenadaX()).coordenadaY(nodo1.getCoordenadaY())
                    .inicioBloqueo(nodo1.getInicioBloqueo())
                    .finBloqueo(nodo1.getFinBloqueo())
                    .nodoprevio(nodo1.getNodoprevio())
                    .blockList(nodo1.getBlockList()).build();
            NodoModel n2 = NodoModel.builder().coordenadaX(nodo2.getCoordenadaX()).coordenadaY(nodo2.getCoordenadaY())
                    .inicioBloqueo(nodo2.getInicioBloqueo())
                    .finBloqueo(nodo2.getFinBloqueo())
                    .nodoprevio(nodo2.getNodoprevio())
                    .blockList(nodo2.getBlockList()).build();
            NodoModel n3 = NodoModel.builder().coordenadaX(nodo3.getCoordenadaX()).coordenadaY(nodo3.getCoordenadaY())
                    .inicioBloqueo(nodo3.getInicioBloqueo())
                    .finBloqueo(nodo3.getFinBloqueo())
                    .nodoprevio(nodo3.getNodoprevio())
                    .blockList(nodo3.getBlockList()).build();
            if(n1.equals(target)){
                neighbours.add(n1);
            }else if(!n1.isBlocked(currentTime))
                neighbours.add(n1);
            if(n2.equals(target)){
                neighbours.add(n2);
            }else if(!n2.isBlocked(currentTime))
                neighbours.add(n2);
            if(n3.equals(target)){
                neighbours.add(n3);
            }else if(!n3.isBlocked(currentTime))
                neighbours.add(n3);
        }else if((i == dimensionXMax-1)&&(j != 0)) {
            nodo1= mapaModelConfiguration.getMapa()[i-1][j];
            nodo2= mapaModelConfiguration.getMapa()[i][j-1];
            nodo3= mapaModelConfiguration.getMapa()[i][j+1];
            NodoModel n1 = NodoModel.builder().coordenadaX(nodo1.getCoordenadaX()).coordenadaY(nodo1.getCoordenadaY())
                    .inicioBloqueo(nodo1.getInicioBloqueo())
                    .finBloqueo(nodo1.getFinBloqueo())
                    .nodoprevio(nodo1.getNodoprevio())
                    .blockList(nodo1.getBlockList()).build();
            NodoModel n2 = NodoModel.builder().coordenadaX(nodo2.getCoordenadaX()).coordenadaY(nodo2.getCoordenadaY())
                    .inicioBloqueo(nodo2.getInicioBloqueo())
                    .finBloqueo(nodo2.getFinBloqueo())
                    .nodoprevio(nodo2.getNodoprevio())
                    .blockList(nodo2.getBlockList()).build();
            NodoModel n3 = NodoModel.builder().coordenadaX(nodo3.getCoordenadaX()).coordenadaY(nodo3.getCoordenadaY())
                    .inicioBloqueo(nodo3.getInicioBloqueo())
                    .finBloqueo(nodo3.getFinBloqueo())
                    .nodoprevio(nodo3.getNodoprevio())
                    .blockList(nodo3.getBlockList()).build();
            if(n1.equals(target)){
                neighbours.add(n1);
            }else if(!n1.isBlocked(currentTime))
                neighbours.add(n1);
            if(n2.equals(target)){
                neighbours.add(n2);
            }else if(!n2.isBlocked(currentTime))
                neighbours.add(n2);
            if(n3.equals(target)){
                neighbours.add(n3);
            }else if(!n3.isBlocked(currentTime))
                neighbours.add(n3);
        }else if((i!=0)&&(j==dimensionYMax-1)) {
            nodo1 = mapaModelConfiguration.getMapa()[i-1][j];
            nodo2 = mapaModelConfiguration.getMapa()[i+1][j];
            nodo3 = mapaModelConfiguration.getMapa()[i][j-1];
            NodoModel n1 = NodoModel.builder().coordenadaX(nodo1.getCoordenadaX()).coordenadaY(nodo1.getCoordenadaY())
                    .inicioBloqueo(nodo1.getInicioBloqueo())
                    .finBloqueo(nodo1.getFinBloqueo())
                    .nodoprevio(nodo1.getNodoprevio())
                    .blockList(nodo1.getBlockList()).build();
            NodoModel n2 = NodoModel.builder().coordenadaX(nodo2.getCoordenadaX()).coordenadaY(nodo2.getCoordenadaY())
                    .inicioBloqueo(nodo2.getInicioBloqueo())
                    .finBloqueo(nodo2.getFinBloqueo())
                    .nodoprevio(nodo2.getNodoprevio())
                    .blockList(nodo2.getBlockList()).build();
            NodoModel n3 = NodoModel.builder().coordenadaX(nodo3.getCoordenadaX()).coordenadaY(nodo3.getCoordenadaY())
                    .inicioBloqueo(nodo3.getInicioBloqueo())
                    .finBloqueo(nodo3.getFinBloqueo())
                    .nodoprevio(nodo3.getNodoprevio())
                    .blockList(nodo3.getBlockList()).build();
            if(n1.equals(target)){
                neighbours.add(n1);
            }else if(!n1.isBlocked(currentTime))
                neighbours.add(n1);
            if(n2.equals(target)){
                neighbours.add(n2);
            }else if(!n2.isBlocked(currentTime))
                neighbours.add(n2);
            if(n3.equals(target)){
                neighbours.add(n3);
            }else if(!n3.isBlocked(currentTime))
                neighbours.add(n3);
        }else if(i<dimensionXMax-1 && j<dimensionYMax-1){
            nodo1= mapaModelConfiguration.getMapa()[i][j+1];
            nodo2= mapaModelConfiguration.getMapa()[i][j-1];
            nodo3= mapaModelConfiguration.getMapa()[i+1][j];
            nodo4= mapaModelConfiguration.getMapa()[i-1][j];
            NodoModel n1 = NodoModel.builder().coordenadaX(nodo1.getCoordenadaX()).coordenadaY(nodo1.getCoordenadaY())
                    .inicioBloqueo(nodo1.getInicioBloqueo())
                    .finBloqueo(nodo1.getFinBloqueo())
                    .nodoprevio(nodo1.getNodoprevio())
                    .blockList(nodo1.getBlockList()).build();
            NodoModel n2 = NodoModel.builder().coordenadaX(nodo2.getCoordenadaX()).coordenadaY(nodo2.getCoordenadaY())
                    .inicioBloqueo(nodo2.getInicioBloqueo())
                    .finBloqueo(nodo2.getFinBloqueo())
                    .nodoprevio(nodo2.getNodoprevio())
                    .blockList(nodo2.getBlockList()).build();
            NodoModel n3 = NodoModel.builder().coordenadaX(nodo3.getCoordenadaX()).coordenadaY(nodo3.getCoordenadaY())
                    .inicioBloqueo(nodo3.getInicioBloqueo())
                    .finBloqueo(nodo3.getFinBloqueo())
                    .nodoprevio(nodo3.getNodoprevio())
                    .blockList(nodo3.getBlockList()).build();
            NodoModel n4 = NodoModel.builder().coordenadaX(nodo4.getCoordenadaX()).coordenadaY(nodo4.getCoordenadaY())
                    .inicioBloqueo(nodo4.getInicioBloqueo())
                    .finBloqueo(nodo4.getFinBloqueo())
                    .nodoprevio(nodo4.getNodoprevio())
                    .blockList(nodo4.getBlockList()).build();
            if(n1.equals(target)){
                neighbours.add(n1);
            }else if(!n1.isBlocked(currentTime))
                neighbours.add(n1);
            if(n2.equals(target)){
                neighbours.add(n2);
            }else if(!n2.isBlocked(currentTime))
                neighbours.add(n2);
            if(n3.equals(target)){
                neighbours.add(n3);
            }else if(!n3.isBlocked(currentTime))
                neighbours.add(n3);
            if(n4.equals(target)){
                neighbours.add(n4);
            }else if(!n4.isBlocked(currentTime))
                neighbours.add(n4);
        }
        return neighbours;
    }

}
