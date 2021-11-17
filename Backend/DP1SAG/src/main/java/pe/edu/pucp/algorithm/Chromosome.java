

package pe.edu.pucp.algorithm;

import java.util.*;
import java.util.function.BiFunction;
import java.time.temporal.ChronoUnit;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pe.edu.pucp.mvc.controllers.MapaModel;
import pe.edu.pucp.mvc.models.NodoModel;
import pe.edu.pucp.mvc.models.PedidoModel;
import pe.edu.pucp.mvc.models.VehiculoModel;
import pe.edu.pucp.utils.UtilidadesCuenta;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Chromosome {
    private List<NodoModel> genes;
    @Builder.Default
    private double fitness = Double.MAX_VALUE;
    private NodoModel currentStart;
    private NodoModel finalDepot;
    private List<NodoModel> route;

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
    
    private double computeFitness(VehiculoModel v, MapaModel mapaModelConfiguration) {
        if(currentStart == null){
            throw new NullPointerException("currentStart es null");
        }

        if(finalDepot == null){
            throw new NullPointerException("FinalDepot es null");
        }

        int i = 0;
        double distancia = 0, gastoCombustible = 0, distanciaTotal = 0, glp = v.getPesoCargaGLP()/2 + v.getPesoTara();
        long tiempoTotal = (long) 0.0, tiempo = (long) 0.0, partial_time = (long) 0.0;
        Date init_delivery_time, previousDeliveredDate;
        PedidoModel req = null;
        this.route = new ArrayList<>();
        List<NodoModel> partialRoute;
        
        previousDeliveredDate = v.getFechaInicio().getTime();
        if(!currentStart.equals(new NodoModel(genes.get(0)))){
            partialRoute = AStar.get_shortest_path(currentStart, genes.get(0),
                    mapaModelConfiguration, previousDeliveredDate, (int) v.getVelocidad(), null);
            partialRoute.removeIf(Objects::isNull);
            this.route.addAll(partialRoute);
            distancia = partialRoute.size();
            partial_time = new Double(((double)partialRoute.size()*3600000/v.getVelocidad())).longValue();
            previousDeliveredDate = Date.from(previousDeliveredDate.toInstant().plus(partial_time, ChronoUnit.MILLIS));
            if(distancia == 0)
                return Double.MAX_VALUE;
        }
        
        if(previousDeliveredDate.compareTo(((PedidoModel) genes.get(0)).getHorasLimite().getTime()) > 0)
            return -1;
        
        gastoCombustible = distancia*glp/150;
        distanciaTotal = distancia;

        // verificar que el PedidoModel llegue en el plazo establecido
        for(; i < genes.size() - 1; i++){
            req = (PedidoModel) genes.get(i);
            NodoModel prevVertex = i == 0 ? currentStart : this.route.get(this.route.size() - 1);
            distancia = 0;
            
            if(!req.equals(new NodoModel(genes.get(i+1)))){
                partialRoute = AStar.get_shortest_path(req, genes.get(i + 1),
                        mapaModelConfiguration, previousDeliveredDate, (int) v.getVelocidad(), prevVertex);
                partialRoute.removeIf(Objects::isNull);
                this.route.addAll(partialRoute);
                distancia = partialRoute.size();
                partial_time = new Double(((double)partialRoute.size()*3600000/v.getVelocidad())).longValue();
                previousDeliveredDate = Date.from(previousDeliveredDate.toInstant().plus(partial_time, ChronoUnit.MILLIS));
                if(distancia == 0)
                    return Double.MAX_VALUE;
                
                if(previousDeliveredDate.compareTo(((PedidoModel) genes.get(i + 1)).getHorasLimite().getTime()) > 0)
                    return -1;
            }
            glp -= req.getCantidadGLP()/2; // hacer peso del glp una constante simbolica
            gastoCombustible += distancia*glp/150;
            distanciaTotal += distancia; 
        }

        req = (PedidoModel) genes.get(i);
        glp -= req.getCantidadGLP()/2;
        partialRoute = AStar.get_shortest_path(genes.get(i), finalDepot,
                mapaModelConfiguration, previousDeliveredDate, (int) v.getVelocidad(), this.route.get(this.route.size() - 1));
        partialRoute.removeIf(Objects::isNull);
        this.route.addAll(partialRoute);
        distancia = partialRoute.size();

        if(distancia == 0)
            return Double.MAX_VALUE;
        gastoCombustible += distancia*glp/150;
        distanciaTotal += distancia; 
        
        if(gastoCombustible >= v.getCombustible())
            return -1.0;
        // TODO: setear petroleo que sobra para pasarlo al camion
        this.route.add(finalDepot);
        
        return distanciaTotal;
    }
    
    public void setGenes(NodoModel current, List<NodoModel> v, NodoModel finalD){
        currentStart = current;
        ArrayList<NodoModel> vertexList = new ArrayList<>();
        vertexList.addAll(v);
        genes = vertexList;
        finalDepot = finalD;
    }
    
    public Double getFitness(BiFunction<VehiculoModel, Chromosome, Double> fn, VehiculoModel VehiculoModel){
        if(fitness == Double.MAX_VALUE)
            fitness = fn.apply(VehiculoModel, this);
        
        return fitness == -1 ? Double.MAX_VALUE : fitness;
    }
    
    public Double getFitness(VehiculoModel VehiculoModel, MapaModel mapaModelConfiguration){
        if(fitness == Double.MAX_VALUE)
            fitness = computeFitness(VehiculoModel, mapaModelConfiguration);
        
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
        List<NodoModel> firstC1 = new ArrayList<>(parent1.getGenes().subList(0, i));
        List<NodoModel> firstC2 = new ArrayList<>(parent2.getGenes().subList(0, i));
        List<NodoModel> lastC1 = new ArrayList<>(parent1.getGenes().subList(i, parent1.getGenes().size()));
        List<NodoModel> lastC2 = new ArrayList<>(parent2.getGenes().subList(i, parent2.getGenes().size()));
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
        List<NodoModel> sublist1 = new ArrayList<>(child1.getGenes().subList(0, pos));
        List<NodoModel> sublist2 = new ArrayList<>(child2.getGenes().subList(0, pos));
        for (NodoModel v1 : sublist1) {
            int repeat = 0;
            repeat = UtilidadesCuenta.countOcurrences(v1, child1Aux.getGenes());
            if (repeat > 1) {
                int count2 = 0;
                List<NodoModel> subparent1 = new ArrayList<>(parent1.getGenes().subList(pos, parent1.getGenes().size()));
                for (NodoModel v2 : subparent1) {
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
        for (NodoModel v1 : sublist2) {
            int repeat = 0;
            repeat = UtilidadesCuenta.countOcurrences(v1, child2Aux.getGenes());
            if (repeat > 1) {
                int count2 = 0;
                List<NodoModel> subparent2 = new ArrayList<>(parent2.getGenes().subList(pos, parent2.getGenes().size()));
                for (NodoModel v2 : subparent2) {
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
