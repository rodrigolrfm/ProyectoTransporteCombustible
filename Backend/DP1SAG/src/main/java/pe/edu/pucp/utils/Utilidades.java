package pe.edu.pucp.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Utilidades<T> {
    
    public static <T> List<T> shuffle(List<T> arr) {
        List<T> copy = new ArrayList<>();
        copy.addAll(arr);
        List<T> shuffled = new ArrayList<>();
        Random r = new Random();
        int n= arr.size();
        for (int i = 0; i < n; i++) {
            int j = r.nextInt(copy.size());
            T temp = copy.get(j);
            shuffled.add(temp);
            copy.remove(j);
        }
        return shuffled;
    }
}