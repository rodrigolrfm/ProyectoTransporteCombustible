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

    public static void Genetic(EntidadVehiculo vehiculo, ArrayList<NodoModel> pedidos, MapaModel mapaModelConfiguration){
        int i = 0;
        Chromosome chromosome = Chromosome.builder().currentStart(vehiculo.getNodoActual()).genes(pedidos).finalDepot(vehiculo.getNodoActual()).build();

        if(pedidos.size() == 1){
            chromosome.getFitness(vehiculo, mapaModelConfiguration);
            vehiculo.setRutaVehiculo(new ArrayList<>(chromosome.getRoute()));
            return;
        }
        
        Generation generation = new Generation();
        generation.initPopulation(chromosome, mapaModelConfiguration.getPlantas(), Parameters.POPULATION_SIZE);
        generation.setNParents(Parameters.NUMBER_OF_PARENTS);
        generation.setNDirect(Parameters.NUMBER_OF_DIRECT_CHROMOSOMES);
        generation.setBestChromosome(chromosome, vehiculo, mapaModelConfiguration);
        generation.calculateAllFitness(vehiculo, mapaModelConfiguration);
        
        double menor = generation.getBestFitness();
        Chromosome route = generation.getBestChromosome();

        while(i < Parameters.GENERATIONS){
            try {
                generation.generateNewGeneration(Parameters.TOURNAMENT_PARTICIPANTS);
                generation.calculateAllFitness(vehiculo, mapaModelConfiguration);
                if (generation.getBestFitness() < menor){
                    route = generation.getBestChromosome();
                    menor = generation.getBestFitness();
                }
                i++;
            }
            catch (Exception ex) {
                System.err.println(ex.getMessage());
            }
        }
        System.out.println("Ruta final: " + route.getRoute());
        vehiculo.setRutaVehiculo(new ArrayList<>(route.getRoute()));
    }
}
