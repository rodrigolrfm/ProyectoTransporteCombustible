
package pe.edu.pucp.algorithm;


import pe.edu.pucp.mvc.models.NodoModel;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.PriorityQueue;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;



public class AStar {
    public static double PESO=1;
    public static final List<NodoModel> get_shortest_path(NodoModel start, NodoModel target, Map mapConfiguration, Date previousDeliveredDate, int velocity, NodoModel nodoprevio){
        PriorityQueue<NodoModel> closedList = new PriorityQueue<>();
        PriorityQueue<NodoModel> openList = new PriorityQueue<>();
        List<NodoModel> neighbour_list = new ArrayList<>();
        
        if(start == null || target == null){
            System.err.println("Valores Nulos de los Argumentos");
            return new ArrayList<>();
        }
        
        mapConfiguration.clearRoute();
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
                neighbour_list.add(nodoprevio);
            }else{
                neighbour_list = graph_adjacentNeighbour(n, mapConfiguration,previousDeliveredDate, target, timeAdvance);
            }
            for(NodoModel current: neighbour_list){
                double totalWeight = n.getG() + PESO;

                if(!openList.contains(current) && !closedList.contains(current)){
                    current.setNodoprevio(n);
                    current.setG(totalWeight);
                    current.setF(current.getG() + current.getDistancia(target));
                    openList.add(current);
                } else {
                    //Sacar los movimientos hasta el momento
                    List<NodoModel> movs = new ArrayList<>();
                    for (NodoModel vertex = current.getNodoprevio(); vertex != null; vertex = vertex.getNodoprevio()) {
                        movs.add(vertex);
                    }
                    List<NodoModel> movements = new ArrayList<>(movs);
                    Collections.reverse(movements);
                    long partial_time = new Double(((double)movements.size()*3600000/velocity)).longValue();
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
    private static List<NodoModel> graph_adjacentNeighbour(NodoModel u, Map mapConfiguration,Date now,
                                                        NodoModel target, double timeAdvance) {
        List<NodoModel> neighbours = new ArrayList<>();
        //Date now = Date.from(Instant.now());
        now = Date.from(now.toInstant().plus(new Double(timeAdvance).longValue(),ChronoUnit.SECONDS));
        int i= u.getCoordenadaX();
        int j = u.getCoordenadaY();
        int MAX_x = mapConfiguration.getDimensionX();
        int MAX_y = mapConfiguration.getDimensionY();
        NodoModel v1;
        NodoModel v2;
        NodoModel v3;
        NodoModel v4;
        if((i==0) && (j==0)){
            v1=mapConfiguration.getMap()[i+1][j];
            v2=mapConfiguration.getMap()[i][j+1];
            NodoModel n1 = NodoModel.builder().coordenadaX(v1.getCoordenadaX()).coordenadaY(v1.getCoordenadaY())
                    .inicioBloqueo(v1.getInicioBloqueo())
                    .finBloqueo(v1.getFinBloqueo())
                    .nodoprevio(v1.getNodoprevio())
                    .blockList(v1.getBlockList()).build();
            NodoModel n2 = NodoModel.builder().coordenadaX(v2.getCoordenadaX()).coordenadaY(v2.getCoordenadaY())
                    .inicioBloqueo(v2.getInicioBloqueo())
                    .finBloqueo(v2.getFinBloqueo())
                    .nodoprevio(v2.getNodoprevio())
                    .blockList(v2.getBlockList()).build();
            if(n1.equals(target)){
                neighbours.add(n1);
            }else if(!n1.isBlocked(now)) {
                neighbours.add(n1);
            }
            if(n2.equals(target)){
                neighbours.add(n2);
            }else if(!n2.isBlocked(now))
                neighbours.add(n2);
        }else if((i==MAX_x-1)&& (j==MAX_y-1)){
            v1=mapConfiguration.getMap()[i-1][j];
            v2=mapConfiguration.getMap()[i][j-1];
            NodoModel n1 = NodoModel.builder().coordenadaX(v1.getCoordenadaX()).coordenadaY(v1.getCoordenadaY())
                    .inicioBloqueo(v1.getInicioBloqueo())
                    .finBloqueo(v1.getFinBloqueo())
                    .nodoprevio(v1.getNodoprevio())
                    .blockList(v1.getBlockList()).build();
            NodoModel n2 = NodoModel.builder().coordenadaX(v2.getCoordenadaX()).coordenadaY(v2.getCoordenadaY())
                    .inicioBloqueo(v2.getInicioBloqueo())
                    .finBloqueo(v2.getFinBloqueo())
                    .nodoprevio(v2.getNodoprevio())
                    .blockList(v2.getBlockList()).build();
            if(n1.equals(target)){
                neighbours.add(n1);
            }else if(!n1.isBlocked(now))
                neighbours.add(n1);
            if(n2.equals(target)){
                neighbours.add(n2);
            }else if(!n2.isBlocked(now))
                neighbours.add(n2);
        }else if((i==0)&&(j==MAX_y-1)){
            v1=mapConfiguration.getMap()[i][j-1];
            v2=mapConfiguration.getMap()[i+1][j];
            NodoModel n1 = NodoModel.builder().coordenadaX(v1.getCoordenadaX()).coordenadaY(v1.getCoordenadaY())
                    .inicioBloqueo(v1.getInicioBloqueo())
                    .finBloqueo(v1.getFinBloqueo())
                    .nodoprevio(v1.getNodoprevio())
                    .blockList(v1.getBlockList()).build();
            NodoModel n2 = NodoModel.builder().coordenadaX(v2.getCoordenadaX()).coordenadaY(v2.getCoordenadaY())
                    .inicioBloqueo(v2.getInicioBloqueo())
                    .finBloqueo(v2.getFinBloqueo())
                    .nodoprevio(v2.getNodoprevio())
                    .blockList(v2.getBlockList()).build();
            if(n1.equals(target)){
                neighbours.add(n1);
            }else if(!n1.isBlocked(now))
                neighbours.add(n1);
            if(n2.equals(target)){
                neighbours.add(n2);
            }else if(!n2.isBlocked(now))
                neighbours.add(n2);
        }else if((i==MAX_x-1)&&(j==0)) {
            v1=mapConfiguration.getMap()[i-1][j];
            v2=mapConfiguration.getMap()[i][j+1];
            NodoModel n1 = NodoModel.builder().coordenadaX(v1.getCoordenadaX()).coordenadaY(v1.getCoordenadaY())
                    .inicioBloqueo(v1.getInicioBloqueo())
                    .finBloqueo(v1.getFinBloqueo())
                    .nodoprevio(v1.getNodoprevio())
                    .blockList(v1.getBlockList()).build();
            NodoModel n2 = NodoModel.builder().coordenadaX(v2.getCoordenadaX()).coordenadaY(v2.getCoordenadaY())
                    .inicioBloqueo(v2.getInicioBloqueo())
                    .finBloqueo(v2.getFinBloqueo())
                    .nodoprevio(v2.getNodoprevio())
                    .blockList(v2.getBlockList()).build();
            if(n1.equals(target)){
                neighbours.add(n1);
            }else if(!n1.isBlocked(now))
                neighbours.add(n1);
            if(n2.equals(target)){
                neighbours.add(n2);
            }else if(!n2.isBlocked(now))
                neighbours.add(n2);
        }else if((i!=0)&&(j==0)) {
            v1=mapConfiguration.getMap()[i-1][j];
            v2=mapConfiguration.getMap()[i+1][j];
            v3=mapConfiguration.getMap()[i][j+1];
            NodoModel n1 = NodoModel.builder().coordenadaX(v1.getCoordenadaX()).coordenadaY(v1.getCoordenadaY())
                    .inicioBloqueo(v1.getInicioBloqueo())
                    .finBloqueo(v1.getFinBloqueo())
                    .nodoprevio(v1.getNodoprevio())
                    .blockList(v1.getBlockList()).build();
            NodoModel n2 = NodoModel.builder().coordenadaX(v2.getCoordenadaX()).coordenadaY(v2.getCoordenadaY())
                    .inicioBloqueo(v2.getInicioBloqueo())
                    .finBloqueo(v2.getFinBloqueo())
                    .nodoprevio(v2.getNodoprevio())
                    .blockList(v2.getBlockList()).build();
            NodoModel n3 = NodoModel.builder().coordenadaX(v3.getCoordenadaX()).coordenadaY(v3.getCoordenadaY())
                    .inicioBloqueo(v3.getInicioBloqueo())
                    .finBloqueo(v3.getFinBloqueo())
                    .nodoprevio(v3.getNodoprevio())
                    .blockList(v3.getBlockList()).build();
            if(n1.equals(target)){
                neighbours.add(n1);
            }else if(!n1.isBlocked(now))
                neighbours.add(n1);
            if(n2.equals(target)){
                neighbours.add(n2);
            }else if(!n2.isBlocked(now))
                neighbours.add(n2);
            if(n3.equals(target)){
                neighbours.add(n3);
            }else if(!n3.isBlocked(now))
                neighbours.add(n3);
        }else if((i==0)&&(j!=0)) {
            v1=mapConfiguration.getMap()[i+1][j];
            v2=mapConfiguration.getMap()[i][j-1];
            v3=mapConfiguration.getMap()[i][j+1];
            NodoModel n1 = NodoModel.builder().coordenadaX(v1.getCoordenadaX()).coordenadaY(v1.getCoordenadaY())
                    .inicioBloqueo(v1.getInicioBloqueo())
                    .finBloqueo(v1.getFinBloqueo())
                    .nodoprevio(v1.getNodoprevio())
                    .blockList(v1.getBlockList()).build();
            NodoModel n2 = NodoModel.builder().coordenadaX(v2.getCoordenadaX()).coordenadaY(v2.getCoordenadaY())
                    .inicioBloqueo(v2.getInicioBloqueo())
                    .finBloqueo(v2.getFinBloqueo())
                    .nodoprevio(v2.getNodoprevio())
                    .blockList(v2.getBlockList()).build();
            NodoModel n3 = NodoModel.builder().coordenadaX(v3.getCoordenadaX()).coordenadaY(v3.getCoordenadaY())
                    .inicioBloqueo(v3.getInicioBloqueo())
                    .finBloqueo(v3.getFinBloqueo())
                    .nodoprevio(v3.getNodoprevio())
                    .blockList(v3.getBlockList()).build();
            if(n1.equals(target)){
                neighbours.add(n1);
            }else if(!n1.isBlocked(now))
                neighbours.add(n1);
            if(n2.equals(target)){
                neighbours.add(n2);
            }else if(!n2.isBlocked(now))
                neighbours.add(n2);
            if(n3.equals(target)){
                neighbours.add(n3);
            }else if(!n3.isBlocked(now))
                neighbours.add(n3);
        }else if((i==MAX_x-1)&&(j!=0)) {
            v1=mapConfiguration.getMap()[i-1][j];
            v2=mapConfiguration.getMap()[i][j-1];
            v3=mapConfiguration.getMap()[i][j+1];
            NodoModel n1 = NodoModel.builder().coordenadaX(v1.getCoordenadaX()).coordenadaY(v1.getCoordenadaY())
                    .inicioBloqueo(v1.getInicioBloqueo())
                    .finBloqueo(v1.getFinBloqueo())
                    .nodoprevio(v1.getNodoprevio())
                    .blockList(v1.getBlockList()).build();
            NodoModel n2 = NodoModel.builder().coordenadaX(v2.getCoordenadaX()).coordenadaY(v2.getCoordenadaY())
                    .inicioBloqueo(v2.getInicioBloqueo())
                    .finBloqueo(v2.getFinBloqueo())
                    .nodoprevio(v2.getNodoprevio())
                    .blockList(v2.getBlockList()).build();
            NodoModel n3 = NodoModel.builder().coordenadaX(v3.getCoordenadaX()).coordenadaY(v3.getCoordenadaY())
                    .inicioBloqueo(v3.getInicioBloqueo())
                    .finBloqueo(v3.getFinBloqueo())
                    .nodoprevio(v3.getNodoprevio())
                    .blockList(v3.getBlockList()).build();
            if(n1.equals(target)){
                neighbours.add(n1);
            }else if(!n1.isBlocked(now))
                neighbours.add(n1);
            if(n2.equals(target)){
                neighbours.add(n2);
            }else if(!n2.isBlocked(now))
                neighbours.add(n2);
            if(n3.equals(target)){
                neighbours.add(n3);
            }else if(!n3.isBlocked(now))
                neighbours.add(n3);
        }else if((i!=0)&&(j==MAX_y-1)) {
            v1=mapConfiguration.getMap()[i-1][j];
            v2=mapConfiguration.getMap()[i+1][j];
            v3=mapConfiguration.getMap()[i][j-1];
            NodoModel n1 = NodoModel.builder().coordenadaX(v1.getCoordenadaX()).coordenadaY(v1.getCoordenadaY())
                    .inicioBloqueo(v1.getInicioBloqueo())
                    .finBloqueo(v1.getFinBloqueo())
                    .nodoprevio(v1.getNodoprevio())
                    .blockList(v1.getBlockList()).build();
            NodoModel n2 = NodoModel.builder().coordenadaX(v2.getCoordenadaX()).coordenadaY(v2.getCoordenadaY())
                    .inicioBloqueo(v2.getInicioBloqueo())
                    .finBloqueo(v2.getFinBloqueo())
                    .nodoprevio(v2.getNodoprevio())
                    .blockList(v2.getBlockList()).build();
            NodoModel n3 = NodoModel.builder().coordenadaX(v3.getCoordenadaX()).coordenadaY(v3.getCoordenadaY())
                    .inicioBloqueo(v3.getInicioBloqueo())
                    .finBloqueo(v3.getFinBloqueo())
                    .nodoprevio(v3.getNodoprevio())
                    .blockList(v3.getBlockList()).build();
            if(n1.equals(target)){
                neighbours.add(n1);
            }else if(!n1.isBlocked(now))
                neighbours.add(n1);
            if(n2.equals(target)){
                neighbours.add(n2);
            }else if(!n2.isBlocked(now))
                neighbours.add(n2);
            if(n3.equals(target)){
                neighbours.add(n3);
            }else if(!n3.isBlocked(now))
                neighbours.add(n3);
        }else if(i<MAX_x-1 && j<MAX_y-1){
            v1=mapConfiguration.getMap()[i][j+1];
            v2=mapConfiguration.getMap()[i][j-1];
            v3=mapConfiguration.getMap()[i+1][j];
            v4=mapConfiguration.getMap()[i-1][j];
            NodoModel n1 = NodoModel.builder().coordenadaX(v1.getCoordenadaX()).coordenadaY(v1.getCoordenadaY())
                    .inicioBloqueo(v1.getInicioBloqueo())
                    .finBloqueo(v1.getFinBloqueo())
                    .nodoprevio(v1.getNodoprevio())
                    .blockList(v1.getBlockList()).build();
            NodoModel n2 = NodoModel.builder().coordenadaX(v2.getCoordenadaX()).coordenadaY(v2.getCoordenadaY())
                    .inicioBloqueo(v2.getInicioBloqueo())
                    .finBloqueo(v2.getFinBloqueo())
                    .nodoprevio(v2.getNodoprevio())
                    .blockList(v2.getBlockList()).build();
            NodoModel n3 = NodoModel.builder().coordenadaX(v3.getCoordenadaX()).coordenadaY(v3.getCoordenadaY())
                    .inicioBloqueo(v3.getInicioBloqueo())
                    .finBloqueo(v3.getFinBloqueo())
                    .nodoprevio(v3.getNodoprevio())
                    .blockList(v3.getBlockList()).build();
            NodoModel n4 = NodoModel.builder().coordenadaX(v4.getCoordenadaX()).coordenadaY(v4.getCoordenadaY())
                    .inicioBloqueo(v4.getInicioBloqueo())
                    .finBloqueo(v4.getFinBloqueo())
                    .nodoprevio(v4.getNodoprevio())
                    .blockList(v4.getBlockList()).build();
            if(n1.equals(target)){
                neighbours.add(n1);
            }else if(!n1.isBlocked(now))
                neighbours.add(n1);
            if(n2.equals(target)){
                neighbours.add(n2);
            }else if(!n2.isBlocked(now))
                neighbours.add(n2);
            if(n3.equals(target)){
                neighbours.add(n3);
            }else if(!n3.isBlocked(now))
                neighbours.add(n3);
            if(n4.equals(target)){
                neighbours.add(n4);
            }else if(!n4.isBlocked(now))
                neighbours.add(n4);
        }
        return neighbours;
    }

}
