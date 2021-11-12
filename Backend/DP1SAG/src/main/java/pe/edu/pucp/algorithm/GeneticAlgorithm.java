/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.edu.pucp.algorithm;


import java.util.ArrayList;
import javafx.util.Pair;
import pe.edu.pucp.mvc.models.Vehicle;


public class GeneticAlgorithm {
    
    public static final int GENERATIONS = 30;
    public static final int POPULATION_SIZE = 50;
    public static final int TOURNAMENT_PARTICIPANTS = 20;
    public static final int NUMBER_OF_PARENTS = 20;
    public static final int NUMBER_OF_DIRECT_CHROMOSOMES = 25;
    
    public static void GA(Vehicle vehicle, ArrayList<Node> pedidos, Map mapConfiguration){ //podriamos poner el mapa como parametro
        int i=0;
        Chromosome chromosome = Chromosome.builder().currentStart(vehicle.getCurrentLocation())
                .genes(pedidos).finalDepot(vehicle.getCurrentLocation()) //este ultimo puede cambiar
                .build();
        
        if(pedidos.size() == 1){
            chromosome.getFitness(vehicle, mapConfiguration);
            vehicle.setRuta(new ArrayList<>(chromosome.getRoute()));
            return;
        }
        
        Generation generation = new Generation();
        generation.initPopulation(chromosome, mapConfiguration.getDepots(), POPULATION_SIZE);
        generation.setN_parents(NUMBER_OF_PARENTS);
        generation.setN_directs(NUMBER_OF_DIRECT_CHROMOSOMES);
        generation.setBest_chromosome(chromosome, vehicle, mapConfiguration);
        generation.calculateAllFitness(vehicle, mapConfiguration);
        
        double menor = generation.getBest_fitness();
        Chromosome route = generation.getBest_chromosome();
        System.out.println("Ruta inicial: " + route.getRoute());
                
        // aqui se implementa el algoritmo genetico con 30 generaciones
        while(i < GENERATIONS){
            try {
                generation.generateNewGeneration(TOURNAMENT_PARTICIPANTS);
                generation.calculateAllFitness(vehicle, mapConfiguration);
                if (generation.getBest_fitness() < menor){
                    route = generation.getBest_chromosome();
                    menor = generation.getBest_fitness();
                }
                i++;
            }
            catch (Exception ex) {
                System.err.println(ex.getMessage());
            }
        }
        
        vehicle.setRuta(new ArrayList<>(route.getRoute()));
    }
}
