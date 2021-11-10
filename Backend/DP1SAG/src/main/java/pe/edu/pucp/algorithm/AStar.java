
package pe.edu.pucp.algorithm;


import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.PriorityQueue;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;



public class AStar {
    public static double PESO=1;
    public static final List<Node> get_shortest_path(Node start, Node target,Map mapConfiguration,Date previousDeliveredDate,int velocity, Node previousVertex){
        PriorityQueue<Node> closedList = new PriorityQueue<>();
        PriorityQueue<Node> openList = new PriorityQueue<>();
        List<Node> neighbour_list = new ArrayList<>();
        
        if(start == null || target == null){
            System.err.println("Valores Nulos de los Argumentos");
            return new ArrayList<>();
        }
        
        mapConfiguration.clearRoute();
        start.setPreviousVertex(null);
        target.setPreviousVertex(null);
        double timeAdvance = (1.0/velocity)*3600;
        
        start.setF(start.getG() + start.getDistancia(target));
        openList.add(start);
                
        while(!openList.isEmpty()){
            Node n = openList.peek();
            if(n.equals(target)){
                target.setPreviousVertex(n.getPreviousVertex());
                break;
            }
            if(n.isBlocked(previousDeliveredDate)){
                neighbour_list.add(previousVertex);
            }else{
                neighbour_list = graph_adjacentNeighbour(n, mapConfiguration,previousDeliveredDate, target, timeAdvance);
            }
            for(Node current: neighbour_list){
                double totalWeight = n.getG() + PESO;

                if(!openList.contains(current) && !closedList.contains(current)){
                    current.setPreviousVertex(n);
                    current.setG(totalWeight);
                    current.setF(current.getG() + current.getDistancia(target));
                    openList.add(current);
                } else {
                    //Sacar los movimientos hasta el momento
                    List<Node> movs = new ArrayList<>();
                    for (Node vertex = current.getPreviousVertex(); vertex != null; vertex = vertex.getPreviousVertex()) {
                        movs.add(vertex);
                    }
                    List<Node> movements = new ArrayList<>(movs);
                    Collections.reverse(movements);
                    long partial_time = new Double(((double)movements.size()*3600000/velocity)).longValue();
                    Date checkDate = Date.from(previousDeliveredDate.toInstant().plus(partial_time, ChronoUnit.MILLIS));
                    if(totalWeight < current.getG() && !current.isBlocked(checkDate)){
                        current.setPreviousVertex(n);
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
        List<Node> movs = new ArrayList<>();
        for (Node vertex = target.getPreviousVertex(); vertex != null; vertex = vertex.getPreviousVertex()) {
            movs.add(vertex);
        }
        List<Node> movements = new ArrayList<>(movs);
        Collections.reverse(movements);
        return movements;
    }
    private static List<Node> graph_adjacentNeighbour(Node u, Map mapConfiguration,Date now,
                                                        Node target, double timeAdvance) {
        List<Node> neighbours = new ArrayList<>();
        //Date now = Date.from(Instant.now());
        now = Date.from(now.toInstant().plus(new Double(timeAdvance).longValue(),ChronoUnit.SECONDS));
        int i= u.getCoordX();
        int j = u.getCoordY();
        int MAX_x = mapConfiguration.getDimensionX();
        int MAX_y = mapConfiguration.getDimensionY();
        Node v1;
        Node v2;
        Node v3;
        Node v4;
        if((i==0) && (j==0)){
            v1=mapConfiguration.getMap()[i+1][j];
            v2=mapConfiguration.getMap()[i][j+1];
            Node n1 = Node.builder().coordX(v1.getCoordX()).coordY(v1.getCoordY())
                    .initDateBlocked(v1.getInitDateBlocked())
                    .endDateBlocked(v1.getEndDateBlocked())
                    .previousVertex(v1.getPreviousVertex())
                    .blockList(v1.getBlockList()).build();
            Node n2 = Node.builder().coordX(v2.getCoordX()).coordY(v2.getCoordY())
                    .initDateBlocked(v2.getInitDateBlocked())
                    .endDateBlocked(v2.getEndDateBlocked())
                    .previousVertex(v2.getPreviousVertex())
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
            Node n1 = Node.builder().coordX(v1.getCoordX()).coordY(v1.getCoordY())
                    .initDateBlocked(v1.getInitDateBlocked())
                    .endDateBlocked(v1.getEndDateBlocked())
                    .previousVertex(v1.getPreviousVertex())
                    .blockList(v1.getBlockList()).build();
            Node n2 = Node.builder().coordX(v2.getCoordX()).coordY(v2.getCoordY())
                    .initDateBlocked(v2.getInitDateBlocked())
                    .endDateBlocked(v2.getEndDateBlocked())
                    .previousVertex(v2.getPreviousVertex())
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
            Node n1 = Node.builder().coordX(v1.getCoordX()).coordY(v1.getCoordY())
                    .initDateBlocked(v1.getInitDateBlocked())
                    .endDateBlocked(v1.getEndDateBlocked())
                    .previousVertex(v1.getPreviousVertex())
                    .blockList(v1.getBlockList()).build();
            Node n2 = Node.builder().coordX(v2.getCoordX()).coordY(v2.getCoordY())
                    .initDateBlocked(v2.getInitDateBlocked())
                    .endDateBlocked(v2.getEndDateBlocked())
                    .previousVertex(v2.getPreviousVertex())
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
            Node n1 = Node.builder().coordX(v1.getCoordX()).coordY(v1.getCoordY())
                    .initDateBlocked(v1.getInitDateBlocked())
                    .endDateBlocked(v1.getEndDateBlocked())
                    .previousVertex(v1.getPreviousVertex())
                    .blockList(v1.getBlockList()).build();
            Node n2 = Node.builder().coordX(v2.getCoordX()).coordY(v2.getCoordY())
                    .initDateBlocked(v2.getInitDateBlocked())
                    .endDateBlocked(v2.getEndDateBlocked())
                    .previousVertex(v2.getPreviousVertex())
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
            Node n1 = Node.builder().coordX(v1.getCoordX()).coordY(v1.getCoordY())
                    .initDateBlocked(v1.getInitDateBlocked())
                    .endDateBlocked(v1.getEndDateBlocked())
                    .previousVertex(v1.getPreviousVertex())
                    .blockList(v1.getBlockList()).build();
            Node n2 = Node.builder().coordX(v2.getCoordX()).coordY(v2.getCoordY())
                    .initDateBlocked(v2.getInitDateBlocked())
                    .endDateBlocked(v2.getEndDateBlocked())
                    .previousVertex(v2.getPreviousVertex())
                    .blockList(v2.getBlockList()).build();
            Node n3 = Node.builder().coordX(v3.getCoordX()).coordY(v3.getCoordY())
                    .initDateBlocked(v3.getInitDateBlocked())
                    .endDateBlocked(v3.getEndDateBlocked())
                    .previousVertex(v3.getPreviousVertex())
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
            Node n1 = Node.builder().coordX(v1.getCoordX()).coordY(v1.getCoordY())
                    .initDateBlocked(v1.getInitDateBlocked())
                    .endDateBlocked(v1.getEndDateBlocked())
                    .previousVertex(v1.getPreviousVertex())
                    .blockList(v1.getBlockList()).build();
            Node n2 = Node.builder().coordX(v2.getCoordX()).coordY(v2.getCoordY())
                    .initDateBlocked(v2.getInitDateBlocked())
                    .endDateBlocked(v2.getEndDateBlocked())
                    .previousVertex(v2.getPreviousVertex())
                    .blockList(v2.getBlockList()).build();
            Node n3 = Node.builder().coordX(v3.getCoordX()).coordY(v3.getCoordY())
                    .initDateBlocked(v3.getInitDateBlocked())
                    .endDateBlocked(v3.getEndDateBlocked())
                    .previousVertex(v3.getPreviousVertex())
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
            Node n1 = Node.builder().coordX(v1.getCoordX()).coordY(v1.getCoordY())
                    .initDateBlocked(v1.getInitDateBlocked())
                    .endDateBlocked(v1.getEndDateBlocked())
                    .previousVertex(v1.getPreviousVertex())
                    .blockList(v1.getBlockList()).build();
            Node n2 = Node.builder().coordX(v2.getCoordX()).coordY(v2.getCoordY())
                    .initDateBlocked(v2.getInitDateBlocked())
                    .endDateBlocked(v2.getEndDateBlocked())
                    .previousVertex(v2.getPreviousVertex())
                    .blockList(v2.getBlockList()).build();
            Node n3 = Node.builder().coordX(v3.getCoordX()).coordY(v3.getCoordY())
                    .initDateBlocked(v3.getInitDateBlocked())
                    .endDateBlocked(v3.getEndDateBlocked())
                    .previousVertex(v3.getPreviousVertex())
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
            Node n1 = Node.builder().coordX(v1.getCoordX()).coordY(v1.getCoordY())
                    .initDateBlocked(v1.getInitDateBlocked())
                    .endDateBlocked(v1.getEndDateBlocked())
                    .previousVertex(v1.getPreviousVertex())
                    .blockList(v1.getBlockList()).build();
            Node n2 = Node.builder().coordX(v2.getCoordX()).coordY(v2.getCoordY())
                    .initDateBlocked(v2.getInitDateBlocked())
                    .endDateBlocked(v2.getEndDateBlocked())
                    .previousVertex(v2.getPreviousVertex())
                    .blockList(v2.getBlockList()).build();
            Node n3 = Node.builder().coordX(v3.getCoordX()).coordY(v3.getCoordY())
                    .initDateBlocked(v3.getInitDateBlocked())
                    .endDateBlocked(v3.getEndDateBlocked())
                    .previousVertex(v3.getPreviousVertex())
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
            Node n1 = Node.builder().coordX(v1.getCoordX()).coordY(v1.getCoordY())
                    .initDateBlocked(v1.getInitDateBlocked())
                    .endDateBlocked(v1.getEndDateBlocked())
                    .previousVertex(v1.getPreviousVertex())
                    .blockList(v1.getBlockList()).build();
            Node n2 = Node.builder().coordX(v2.getCoordX()).coordY(v2.getCoordY())
                    .initDateBlocked(v2.getInitDateBlocked())
                    .endDateBlocked(v2.getEndDateBlocked())
                    .previousVertex(v2.getPreviousVertex())
                    .blockList(v2.getBlockList()).build();
            Node n3 = Node.builder().coordX(v3.getCoordX()).coordY(v3.getCoordY())
                    .initDateBlocked(v3.getInitDateBlocked())
                    .endDateBlocked(v3.getEndDateBlocked())
                    .previousVertex(v3.getPreviousVertex())
                    .blockList(v3.getBlockList()).build();
            Node n4 = Node.builder().coordX(v4.getCoordX()).coordY(v4.getCoordY())
                    .initDateBlocked(v4.getInitDateBlocked())
                    .endDateBlocked(v4.getEndDateBlocked())
                    .previousVertex(v4.getPreviousVertex())
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
