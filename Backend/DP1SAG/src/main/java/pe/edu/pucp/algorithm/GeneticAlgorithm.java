/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.edu.pucp.algorithm;


import java.util.ArrayList;

import pe.edu.pucp.mvc.controllers.MapaModel;
import pe.edu.pucp.mvc.models.NodoModel;
import pe.edu.pucp.mvc.models.EntidadVehiculo;


public class GeneticAlgorithm {
    
    public static final int GENERATIONS = 30;
    public static final int POPULATION_SIZE = 50;
    public static final int TOURNAMENT_PARTICIPANTS = 20;
    public static final int NUMBER_OF_PARENTS = 20;
    public static final int NUMBER_OF_DIRECT_CHROMOSOMES = 25;
    
    public static void GA(EntidadVehiculo EntidadVehiculo, ArrayList<NodoModel> pedidos, MapaModel mapaModelConfiguration){ //podriamos poner el mapa como parametro
        int i=0;
        Chromosome chromosome = Chromosome.builder().currentStart(EntidadVehiculo.getNodoActual())
                .genes(pedidos).finalDepot(EntidadVehiculo.getNodoActual()) //este ultimo puede cambiar
                .build();
        
        if(pedidos.size() == 1){
            chromosome.getFitness(EntidadVehiculo, mapaModelConfiguration);
            EntidadVehiculo.setRutaVehiculo(new ArrayList<>(chromosome.getRoute()));
            return;
        }
        
        Generation generation = new Generation();
        generation.initPopulation(chromosome, mapaModelConfiguration.getPlantas(), POPULATION_SIZE);
        generation.setN_parents(NUMBER_OF_PARENTS);
        generation.setN_directs(NUMBER_OF_DIRECT_CHROMOSOMES);
        generation.setBest_chromosome(chromosome, EntidadVehiculo, mapaModelConfiguration);
        generation.calculateAllFitness(EntidadVehiculo, mapaModelConfiguration);
        
        double menor = generation.getBest_fitness();
        Chromosome route = generation.getBest_chromosome();
        //System.out.println("Ruta inicial: " + route.getRoute());
                
        // aqui se implementa el algoritmo genetico con 30 generaciones
        while(i < GENERATIONS){
            try {
                generation.generateNewGeneration(TOURNAMENT_PARTICIPANTS);
                generation.calculateAllFitness(EntidadVehiculo, mapaModelConfiguration);
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
        System.out.println("Ruta final: " + route.getRoute());
        EntidadVehiculo.setRutaVehiculo(new ArrayList<>(route.getRoute()));
    }
}
