package pe.edu.pucp.utils;


import pe.edu.pucp.mvc.models.NodoModel;

import java.util.List;

public class UtilidadesCuenta {
    public static int countOcurrences(NodoModel vertex, List<NodoModel> list){
        int count = 0;
        count = list.stream().filter(v -> (vertex.equals(v))).map(_item -> 1).reduce(count, Integer::sum);
        return count;
    }
}
