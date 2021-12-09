package pe.edu.pucp.mvc.planificacion;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import pe.edu.pucp.mvc.controllers.MapaModel;
import pe.edu.pucp.mvc.models.*;
import pe.edu.pucp.mvc.services.BloqueoService;
import pe.edu.pucp.mvc.services.PedidoService;
import pe.edu.pucp.mvc.services.VehiculoService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InicializarDiaDia {
    @Autowired
    private PedidoService pedidoService;

    @Autowired
    private VehiculoService vehiculoService;

    @Autowired
    private BloqueoService bloqueoService;

    public List<PedidoModel> listaPedidos;
    public List<VehiculoModel> vehiculoModels;
    public List<EntidadVehiculo> listaVehiculos = new ArrayList<>();
    public List<NodoModel> blockList = new ArrayList<>();
    public List<BloqueoModel> bloqueos = new ArrayList<>();
    public MapaModel mapaModel;

    public void inicializarProceso(){
        // Carga de informacion de los vehiculos
        vehiculoModels = vehiculoService.listaVehiculosDisponibles();
        vehiculoModels.forEach(vehiculo -> listaVehiculos.add(new EntidadVehiculo(vehiculo)));

        // Carga de información de los bloqueos
        bloqueos = bloqueoService.listaBloqueosDiaDia();
        bloqueos.forEach(bloqueo -> blockList.add(new NodoModel(bloqueo)));

        // Depositos iniciales
        ArrayList<PlantaModel> plantas = new ArrayList<>();
        plantas.add(PlantaModel.builder().coordenadaX(12).coordenadaY(8).esPrincipal(true).build());
        plantas.add(PlantaModel.builder().coordenadaX(42).coordenadaY(42).build());
        plantas.add(PlantaModel.builder().coordenadaX(63).coordenadaY(3).build());
        mapaModel = new MapaModel(70, 50, plantas);
        // Se crea el mapa

        mapaModel.setBlockList(blockList);

        // Inicializar las fechas de inicio de los vehículos
        listaVehiculos.forEach(v -> {
            // Fecha de inicio y copia de fecha de inicio
            Calendar init = Calendar.getInstance();
            init.set(2021, 0, 0, 0, 0, 0);
            v.setFechaInicio(init);
            v.setNodoActual(plantas.get(0));
            v.setCombustible(25);
            v.setVelocidad(50);
        });
    }

}
