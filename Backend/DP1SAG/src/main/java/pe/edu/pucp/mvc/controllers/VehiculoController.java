package pe.edu.pucp.mvc.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import pe.edu.pucp.mvc.models.EntidadVehiculo;
import pe.edu.pucp.mvc.models.PedidoModel;
import pe.edu.pucp.mvc.models.VehiculoModel;
import pe.edu.pucp.mvc.services.VehiculoService;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/vehiculo")
public class VehiculoController {

    @Autowired
    VehiculoService vehiculoService;

    @PostMapping(value = "/cargaMasivaVehiculos", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void cargaMasivaVehiculos(@RequestParam("file") MultipartFile file) throws IOException{

        List<EntidadVehiculo> vehicleList = new ArrayList<>();

        String content = new String(file.getBytes());
        Reader inputString = new StringReader(content);
        final BufferedReader br = new BufferedReader(inputString);

        String line;
        String[] rowRequest;
        char type;
        while ((line = br.readLine()) != null) {
            rowRequest = line.split(",");
            type = rowRequest[0].charAt(1);

            EntidadVehiculo vehiculo = EntidadVehiculo.builder()
                    .velocidad(50)
                    .pesoTara(Double.parseDouble(rowRequest[1]))
                    .cargaGLP(Integer.parseInt(rowRequest[2]))
                    .pesoCargaGLP(Double.parseDouble(rowRequest[3]))
                    .pesoTotal(Double.parseDouble(rowRequest[4]))
                    .estadoVehiculo(0).build();
            switch(type){
                case 'A': vehiculo.setTipoVehiculo(1);
                    break;
                case 'B': vehiculo.setTipoVehiculo(2);
                    break;
                case 'C': vehiculo.setTipoVehiculo(3);
                    break;
                case 'D': vehiculo.setTipoVehiculo(4);
                    break;
            }
            vehicleList.add(vehiculo);

            this.vehiculoService.guardarVehiculo( new VehiculoModel(vehiculo));
        }
    }

}
