
package pe.edu.pucp.algorithm;


import lombok.*;
import pe.edu.pucp.mvc.controllers.MapaModel;
import pe.edu.pucp.mvc.models.NodoModel;
import pe.edu.pucp.mvc.models.PlantaModel;
import pe.edu.pucp.mvc.models.EntidadVehiculo;
import pe.edu.pucp.utils.Utilidades;

import java.util.*;

@Getter
@Setter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Generation {
     @Builder.Default
    private List<Chromosome> chromosomesList = new ArrayList<>();
    private int generationNumber;
    private Chromosome bestChromosome;
    @Builder.Default
    private double bestFitness = Double.MAX_VALUE;
    @Builder.Default
    private double ratioCrossover = 0.6;
    @Builder.Default
    private double probMutation = 0.7;
    private int nParents;
    private int nDirect;

    public void initPopulation(Chromosome chromosome, List<PlantaModel> depots, int population_size){
        generationNumber = 0;
        Random r = new Random();
        for(int i = 0; i < population_size; i++){
            Chromosome newChromosome = new Chromosome();
            List<NodoModel> shuffleGenes = new ArrayList<>(chromosome.getGenes());
            shuffleGenes = Utilidades.shuffle(shuffleGenes);
            newChromosome.setCurrentStart(chromosome.getCurrentStart());
            newChromosome.setGenes(shuffleGenes);
            newChromosome.setFinalDepot((NodoModel) depots.get(r.nextInt(depots.size())));
            chromosomesList.add(newChromosome);
        }
    }

    public void generateNewGeneration(int tournamentParticipants){
        List<Chromosome>  directs;
        List<Chromosome>  parents;
        nParents = (int) Math.round(chromosomesList.size() * ratioCrossover);
        nParents = nParents % 2 == 0 ? nParents : nParents -1;
        nDirect = chromosomesList.size() - nParents;
        directs = tournament_Selection("Direct", tournamentParticipants);
        parents = tournament_Selection("Crosses", tournamentParticipants);
        parents = cross_parents(parents);
        parents = mutate(parents, probMutation);
        chromosomesList.clear();
        chromosomesList.addAll(directs);
        chromosomesList.addAll(parents);
        generationNumber += 1;
    }

    private List<Chromosome> mutate(List<Chromosome> parents, double prob_mutate) {
        List<Chromosome> mutations = new ArrayList<>();
        Chromosome mutation;
        for(Chromosome c: parents){
            mutation = mutation(c,prob_mutate);
            mutations.add(mutation);
        }
        return mutations;
    }

    private Chromosome mutation(Chromosome c, double prob_mutate) {
        List<NodoModel> auxiliar;
        Random random = new Random();
        for(int i = 0; i < c.getGenes().size(); i++){
            double j = random.nextDouble();
            if(j < prob_mutate){
                auxiliar = inversion_mutation(c.getGenes());
                c.setGenes(auxiliar);
            }
        }
        return c;
    }

    private List<NodoModel> inversion_mutation(List<NodoModel> genes) {
        Random random = new Random();
        int index1 = random.nextInt(genes.size());
        int index2 = random.nextInt(genes.size()-index1)+index1;
        List<NodoModel> chromosome_mid = new ArrayList<>(genes.subList(index1,index2));
        Collections.reverse(chromosome_mid);
        List<NodoModel> chromosome_result= new ArrayList<>(genes.subList(0,index1));
        chromosome_result.addAll(chromosome_mid);
        chromosome_result.addAll(genes.subList(index2,genes.size()));
        return chromosome_result;
    }

    private List<Chromosome> cross_parents(List<Chromosome> parents) {
        List<Chromosome> children = new ArrayList<>();
        int n_parents = parents.size();
        for(int i=0;i<n_parents;i+=2){
            children.addAll(Chromosome.crossOver(parents.get(i),parents.get(i+1)));
        }
        return children;
    }


    public List<Chromosome> tournament_Selection(String type, int tournamentParticipants){
        List<Chromosome> winners = new ArrayList<>();
        int n = type.equals("Direct") ? nDirect : nParents;
        Utilidades.shuffle(chromosomesList);
        for (int j=0; j < 2; j++) {
            for (int i = 0; i < n; i += 2) {
                Chromosome c = chromosomesList.get(i)
                        .compareTo(chromosomesList.get(i + 1)) < 0 ? chromosomesList.get(i) : chromosomesList.get(i + 1);
                winners.add(c);
            }
        }
        return winners;
    }

    public void calculateAllFitness(EntidadVehiculo EntidadVehiculo, MapaModel mapaModelConfiguration){
        assert !chromosomesList.isEmpty(): "Cromosoma vacío";
        for(Chromosome c : chromosomesList){
            double fitness = c.getFitness(EntidadVehiculo, mapaModelConfiguration);
            assert c.getRoute() !=  null: "Ruta vacía";
            if(fitness < bestFitness){
                bestFitness = fitness;
                bestChromosome = Chromosome.builder()
                        .fitness(c.getFitness())
                        .currentStart(c.getCurrentStart())
                        .genes(c.getGenes())
                        .finalDepot(c.getFinalDepot())
                        .route(c.getRoute())
                        .build();
            }
        }
    }
    
    public void setBestChromosome(Chromosome c, EntidadVehiculo v, MapaModel m){
        this.bestChromosome = c;
        this.bestChromosome.getFitness(v, m);
    }
}
