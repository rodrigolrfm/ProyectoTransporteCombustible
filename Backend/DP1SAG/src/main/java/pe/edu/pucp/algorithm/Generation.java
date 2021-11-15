
package pe.edu.pucp.algorithm;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pe.edu.pucp.mvc.models.NodoModel;
import pe.edu.pucp.mvc.models.PlantaModel;
import pe.edu.pucp.mvc.models.Vehicle;
import pe.edu.pucp.utils.Utilidades;

import java.util.*;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Generation {
     @Builder.Default
    private List<Chromosome> chromosomesList = new ArrayList<>();
    private int generation_number;
    private Chromosome best_chromosome;
    @Builder.Default
    private double best_fitness = Double.MAX_VALUE;
    @Builder.Default
    private double ratio_cross = 0.6;
    @Builder.Default
    private double prob_mutate = 0.7;
    private int n_parents;
    private int n_directs;

    public void initPopulation(Chromosome chromosome, List<PlantaModel> depots, int population_size){
        generation_number = 0;
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
        List<Chromosome>  directs = new ArrayList<>();
        List<Chromosome>  parents = new ArrayList<>();
        n_parents= (int) Math.round(chromosomesList.size()*ratio_cross);
        n_parents = n_parents % 2 == 0 ? n_parents : n_parents-1;
        n_directs= chromosomesList.size() - n_parents;
        directs = tournament_Selection("Direct", tournamentParticipants);
        parents = tournament_Selection("Crosses", tournamentParticipants);
        parents = cross_parents(parents);
        parents = mutate(parents, prob_mutate);
        chromosomesList.clear();
        chromosomesList.addAll(directs);
        chromosomesList.addAll(parents);
        generation_number += 1;
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
        List<NodoModel> auxiliar = new ArrayList<>();
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
            children.addAll(Chromosome.crossover(parents.get(i),parents.get(i+1)));
        }
        return children;
    }


    public List<Chromosome> tournament_Selection(String type, int tournamentParticipants){
        List<Chromosome> winners = new ArrayList<>();
        List<Chromosome> tournamentResults = new ArrayList<>();
        int n = type.equals("Direct") ? n_directs : n_parents;
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
    
    public void calculateAllFitness(){
        for(Chromosome c :chromosomesList){
            double fitness = c.getFitness();
            if(fitness < best_fitness){
                best_fitness = fitness;
                best_chromosome = Chromosome.builder()
                        .fitness(c.getFitness())
                        .currentStart(c.getCurrentStart())
                        .genes(c.getGenes())
                        .finalDepot(c.getFinalDepot())
                        .build();
            }
        }
    }
    
    public void calculateAllFitness(Vehicle vehicle, Map mapConfiguration){
        assert !chromosomesList.isEmpty(): "Cromosoma vacio";
        for(Chromosome c : chromosomesList){
            double fitness = c.getFitness(vehicle, mapConfiguration);
            assert c.getRoute() !=  null: "GG";
            if(fitness < best_fitness){
                best_fitness = fitness;
                best_chromosome = Chromosome.builder()
                        .fitness(c.getFitness())
                        .currentStart(c.getCurrentStart())
                        .genes(c.getGenes())
                        .finalDepot(c.getFinalDepot())
                        .route(c.getRoute())
                        .build();
            }
        }
    }
    
    public void setBest_chromosome(Chromosome c, Vehicle v, Map m){
        this.best_chromosome = c;
        this.best_chromosome.getFitness(v, m);
    }
}
