package pe.edu.pucp.utils;

import pe.edu.pucp.mvc.models.Vehicle;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class VehicleReader {
    public static List<Vehicle> TxtReader(String path) throws IOException , Exception{
        List<Vehicle> vehicleList = new ArrayList<>();

            File file = new File(path);
            final BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            String[] rowRequest;
            char type;
            while ((line = br.readLine()) != null) {
                rowRequest = line.split(",");
                type = rowRequest[0].charAt(1);
                
                Vehicle r = Vehicle.builder()
                        .velocity(50)
                        .grossWeightTara(Double.parseDouble(rowRequest[1]))
                        .loadGLP(Integer.parseInt(rowRequest[2]))
                        .loadWeightGLP(Double.parseDouble(rowRequest[3]))
                        .totalWeight(Double.parseDouble(rowRequest[4]))
                        .state(VehicleState.DISPONIBLE).build();
                switch(type){
                    case 'A': r.setType(VehicleType.TA);
                        break;
                    case 'B': r.setType(VehicleType.TB);
                        break;
                    case 'C': r.setType(VehicleType.TC);
                        break;
                    case 'D': r.setType(VehicleType.TD);
                        break;
                } 
                
                vehicleList.add(r);
                
            }
        
        return vehicleList;
    }
}
