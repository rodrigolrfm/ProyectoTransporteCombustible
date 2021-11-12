package pe.edu.pucp.utils;


import pe.edu.pucp.algorithm.Node;

import java.util.List;

public class UtilidadesCuenta {
    public static int countOcurrences(Node vertex, List<Node> list){
        int count = 0;
        count = list.stream().filter(v -> (vertex.equals(v))).map(_item -> 1).reduce(count, Integer::sum);
        return count;
    }
}
