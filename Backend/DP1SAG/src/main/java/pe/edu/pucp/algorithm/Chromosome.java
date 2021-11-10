

package pe.edu.pucp.algorithm;

import java.util.*;
import java.util.function.BiFunction;
import java.time.temporal.ChronoUnit;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pe.edu.pucp.mvc.models.Pedido;
import pe.edu.pucp.mvc.models.Vehicle;
import pe.edu.pucp.utils.CountUtils;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Chromosome {
    private List<Node> genes;
    @Builder.Default
    private double fitness = Double.MAX_VALUE;
    private Node currentStart;
    private Node finalDepot;
    private List<Node> route;

    private void computeFitness() {
        // cojer todos los genes y calcular sus distancias para obtener el fitness
        if(currentStart == null)
            throw new NullPointerException("currentStart es null");
        
        if(finalDepot == null)
            throw new NullPointerException("currentStart es null");
        
        int i = 0;
        fitness = 0;
        fitness += currentStart.getDistancia(genes.get(0));
        for(; i < genes.size() - 1; i++){
            fitness += genes.get(i).getDistancia(genes.get(i + 1));
        }
        fitness += finalDepot.getDistancia(genes.get(i));
    }
    
    private double computeFitness(Vehicle v, Map mapConfiguration) {
        if(currentStart == null){
            throw new NullPointerException("currentStart es null");
        }

        if(finalDepot == null){
            throw new NullPointerException("FinalDepot es null");
        }

        int i = 0;
        double distancia = 0, gastoCombustible = 0, distanciaTotal = 0, glp = v.getLoadWeightGLP()/2 + v.getGrossWeightTara(); 
        long tiempoTotal = (long) 0.0, tiempo = (long) 0.0, partial_time = (long) 0.0;
        Date init_delivery_time, previousDeliveredDate;
        Pedido req = null;
        this.route = new ArrayList<>();
        List<Node> partialRoute;
        
        previousDeliveredDate = v.getInitDate().getTime();
        if(!currentStart.equals(new Node(genes.get(0)))){
            partialRoute = AStar.get_shortest_path(currentStart, genes.get(0), 
                    mapConfiguration, previousDeliveredDate, (int) v.getVelocity(), null);
            partialRoute.removeIf(Objects::isNull);
            this.route.addAll(partialRoute);
            distancia = partialRoute.size();
            partial_time = new Double(((double)partialRoute.size()*3600000/v.getVelocity())).longValue();
            previousDeliveredDate = Date.from(previousDeliveredDate.toInstant().plus(partial_time, ChronoUnit.MILLIS));
            if(distancia == 0)
                return Double.MAX_VALUE;
        }
        
        if(previousDeliveredDate.compareTo(((Pedido) genes.get(0)).getHoursLimit().getTime()) > 0)
            return -1;
        
        gastoCombustible = distancia*glp/150;
        distanciaTotal = distancia;

        // verificar que el pedido llegue en el plazo establecido
        for(; i < genes.size() - 1; i++){
            req = (Pedido) genes.get(i);
            Node prevVertex = i == 0 ? currentStart : this.route.get(this.route.size() - 1);
            distancia = 0;
            
            if(!req.equals(new Node(genes.get(i+1)))){
                partialRoute = AStar.get_shortest_path(req, genes.get(i + 1), 
                        mapConfiguration, previousDeliveredDate, (int) v.getVelocity(), prevVertex);
                partialRoute.removeIf(Objects::isNull);
                this.route.addAll(partialRoute);
                distancia = partialRoute.size();
                partial_time = new Double(((double)partialRoute.size()*3600000/v.getVelocity())).longValue();
                previousDeliveredDate = Date.from(previousDeliveredDate.toInstant().plus(partial_time, ChronoUnit.MILLIS));
                if(distancia == 0)
                    return Double.MAX_VALUE;
                
                if(previousDeliveredDate.compareTo(((Pedido) genes.get(i + 1)).getHoursLimit().getTime()) > 0)
                    return -1;
            }
            glp -= req.getQuantityGLP()/2; // hacer peso del glp una constante simbolica
            gastoCombustible += distancia*glp/150;
            distanciaTotal += distancia; 
        }

        req = (Pedido) genes.get(i);
        glp -= req.getQuantityGLP()/2;
        partialRoute = AStar.get_shortest_path(genes.get(i), finalDepot, 
                mapConfiguration, previousDeliveredDate, (int) v.getVelocity(), this.route.get(this.route.size() - 1));
        partialRoute.removeIf(Objects::isNull);
        this.route.addAll(partialRoute);
        distancia = partialRoute.size();

        if(distancia == 0)
            return Double.MAX_VALUE;
        gastoCombustible += distancia*glp/150;
        distanciaTotal += distancia; 
        
        if(gastoCombustible >= v.getFuel())
            return -1.0;
        // TODO: setear petroleo que sobra para pasarlo al camion
        this.route.add(finalDepot);
        
        return distanciaTotal;
    }
    
    public void setGenes(Node current, List<Node> v, Node finalD){
        currentStart = current;
        ArrayList<Node> vertexList = new ArrayList<>();
        vertexList.addAll(v);
        genes = vertexList;
        finalDepot = finalD;
    }
    
    public Double getFitness(BiFunction<Vehicle, Chromosome, Double> fn, Vehicle vehicle){
        if(fitness == Double.MAX_VALUE)
            fitness = fn.apply(vehicle, this);
        
        return fitness == -1 ? Double.MAX_VALUE : fitness;
    }
    
    public Double getFitness(Vehicle vehicle, Map mapConfiguration){
        if(fitness == Double.MAX_VALUE)
            fitness = computeFitness(vehicle, mapConfiguration);
        
        return fitness == -1 ? Double.MAX_VALUE : fitness;
    }
    
    public Double getFitness(){
        if(fitness == Double.MAX_VALUE)
            computeFitness();
        
        return fitness;
    }

    public static List<Chromosome> crossover(Chromosome parent1, Chromosome parent2) {
        
        List<Chromosome> children = new ArrayList<>();
        if (parent1.getGenes().size() == 1) {
            children.add(parent1);
            children.add(parent2);
            return children;
        }
        Random random = new Random();
        int i = 0;
        while (i == 0) {
            i = random.nextInt(parent1.getGenes().size());
        }
        List<Node> firstC1 = new ArrayList<>(parent1.getGenes().subList(0, i));
        List<Node> firstC2 = new ArrayList<>(parent2.getGenes().subList(0, i));
        List<Node> lastC1 = new ArrayList<>(parent1.getGenes().subList(i, parent1.getGenes().size()));
        List<Node> lastC2 = new ArrayList<>(parent2.getGenes().subList(i, parent2.getGenes().size()));
        firstC1.addAll(lastC2);
        firstC2.addAll(lastC1);
        Chromosome child1 = Chromosome.builder().currentStart(parent1.getCurrentStart()).genes(firstC1).finalDepot(parent1.getFinalDepot()).build();
        Chromosome child2 = Chromosome.builder().currentStart(parent2.getCurrentStart()).genes(firstC2).finalDepot(parent2.getFinalDepot()).build();
        children = process_gen_repeted(child1, child2, parent1, parent2, i);
        return children;
    }
    
    private static List<Chromosome> process_gen_repeted(final Chromosome child1, final Chromosome child2,
                                                        Chromosome parent1, Chromosome parent2, int pos) {
        List<Chromosome> modifiedChildren = new ArrayList<>();
        Chromosome child1Aux = new Chromosome();
        child1Aux.setGenes(child1.getCurrentStart(), child1.getGenes(), child1.getFinalDepot());
        Chromosome child2Aux = new Chromosome();
        child2Aux.setGenes(child2.getCurrentStart() ,child2.getGenes(), child2.getFinalDepot());
        int count1 = 0;
        List<Node> sublist1 = new ArrayList<>(child1.getGenes().subList(0, pos));
        List<Node> sublist2 = new ArrayList<>(child2.getGenes().subList(0, pos));
        for (Node v1 : sublist1) {
            int repeat = 0;
            repeat = CountUtils.countOcurrences(v1, child1Aux.getGenes());
            if (repeat > 1) {
                int count2 = 0;
                List<Node> subparent1 = new ArrayList<>(parent1.getGenes().subList(pos, parent1.getGenes().size()));
                for (Node v2 : subparent1) {
                    if (!child1Aux.getGenes().contains(v2)) {
                        child1Aux.getGenes().remove(count1);
                        //child1Aux.getGenes().set(count1,parent1.getGenes().get(count2));
                        child1Aux.getGenes().add(count1, subparent1.get(count2));
                    }
                    count2++;
                }
            }
            count1++;
        }

        count1 = 0;
        for (Node v1 : sublist2) {
            int repeat = 0;
            repeat = CountUtils.countOcurrences(v1, child2Aux.getGenes());
            if (repeat > 1) {
                int count2 = 0;
                List<Node> subparent2 = new ArrayList<>(parent2.getGenes().subList(pos, parent2.getGenes().size()));
                for (Node v2 : subparent2) {
                    if (!child2Aux.getGenes().contains(v2)) {
                        child2Aux.getGenes().remove(count1);
                        //child2Aux.getGenes().set(count1,parent2.getGenes().get(count2));
                        child2Aux.getGenes().add(count1, subparent2.get(count2));
                    }
                    count2++;
                }
            }
            count1++;
        }
        modifiedChildren.add(child1Aux);
        modifiedChildren.add(child2Aux);
        return modifiedChildren;

    }
    
    public int compareTo(Chromosome o) {
        return Double.compare(this.getFitness(), o.getFitness());
    }

    @Override
    public String toString() {
        StringBuilder chromosome = new StringBuilder();
        chromosome.append("Chromosome: ").append(currentStart).append(genes).append(finalDepot).append("\n");
        chromosome.append("Ruta: ").append(route).append("\n");
        chromosome.append("Fitness: ").append(fitness).append("\n");
        return chromosome.toString();
    }
}
