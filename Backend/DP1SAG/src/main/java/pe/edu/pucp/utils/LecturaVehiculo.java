package pe.edu.pucp.utils;

import pe.edu.pucp.mvc.models.EntidadVehiculo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class LecturaVehiculo {
    public static List<EntidadVehiculo> TxtReader(String path) throws IOException , Exception{
        List<EntidadVehiculo> vehicleList = new ArrayList<>();

            File file = new File(path);
            final BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            String[] rowRequest;
            char type;
            while ((line = br.readLine()) != null) {
                rowRequest = line.split(",");
                type = rowRequest[0].charAt(1);
                
                EntidadVehiculo r = EntidadVehiculo.builder()
                        .velocidad(50)
                        .pesoTara(Double.parseDouble(rowRequest[1]))
                        .cargaGLP(Integer.parseInt(rowRequest[2]))
                        .pesoCargaGLP(Double.parseDouble(rowRequest[3]))
                        .pesoTotal(Double.parseDouble(rowRequest[4]))
                        .estadoVehiculo(0).build();
                switch(type){
                    case 'A': r.setTipoVehiculo(1);
                        break;
                    case 'B': r.setTipoVehiculo(2);
                        break;
                    case 'C': r.setTipoVehiculo(3);
                        break;
                    case 'D': r.setTipoVehiculo(4);
                        break;
                } 
                
                vehicleList.add(r);
                
            }
        
        return vehicleList;
    }
}
