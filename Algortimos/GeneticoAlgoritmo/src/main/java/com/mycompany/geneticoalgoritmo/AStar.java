package com.mycompany.geneticoalgoritmo;

import java.util.Arrays;

public class AStar {

    public double aStar(long[][] graph, int start, int goal, int sizeX, int sizeY) {

        long[] distances = new long[graph.length];
        Arrays.fill(distances, Integer.MAX_VALUE);
        distances[start] = 0;    
        double[] priorities = new double[graph.length];  
        Arrays.fill(priorities, Integer.MAX_VALUE);
        priorities[start] = 1;
        boolean[] visited = new boolean[graph.length];

        while (true) {

            double lowestPriority = Integer.MAX_VALUE;
            int lowestPriorityIndex = -1;
            for (int i = 0; i < priorities.length; i++) {
                //... by going through all nodes that haven't been visited yet
                if (priorities[i] < lowestPriority && !visited[i]) {
                    lowestPriority = priorities[i];
                    lowestPriorityIndex = i;
                }
            }

            
            
            if (lowestPriorityIndex == -1) {
                return -1;
            } else if (lowestPriorityIndex == goal) {
                System.out.println("Goal node found!");
                return distances[lowestPriorityIndex];
            }

            for (int i = 0; i < graph[lowestPriorityIndex].length; i++) {
                if (graph[lowestPriorityIndex][i] != 0 && !visited[i]) {
                    if (distances[lowestPriorityIndex] + graph[lowestPriorityIndex][i] < distances[i]) {
                        distances[i] = distances[lowestPriorityIndex] + graph[lowestPriorityIndex][i];
                        priorities[i] = distances[i] + 1;
                        System.out.println("Updating distance of node " + i + " to " + distances[i] + " and priority to " + priorities[i]);
                    }
                }
            }

            visited[lowestPriorityIndex] = true;
        }


    }

    public int calcularHeuristica(int start ,int goal, int sizeY){
        Node inicio = Node.calcularVertice(start,sizeY);
        Node fin = Node.calcularVertice(goal,sizeY);
        return (int) Math.sqrt( (inicio.coordX - fin.coordX)^2 + (inicio.coordY - fin.coordY)^2);
    }

}