package pe.edu.pucp.mvc.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.pucp.mvc.models.EntidadVehiculo;
import pe.edu.pucp.mvc.models.PedidoModel;
import pe.edu.pucp.mvc.models.PlantaModel;
import pe.edu.pucp.mvc.models.VehiculoModel;
import pe.edu.pucp.mvc.repositories.VehiculoRepository;

import java.sql.Timestamp;
import java.util.*;

@Service
public class VehiculoService {

    @Autowired
    VehiculoRepository vehiculoRepository;

    public VehiculoModel guardarVehiculo(VehiculoModel vehiculo){
        return vehiculoRepository.save(vehiculo);
    }

    public List<EntidadVehiculo> listaVehiculosDisponibles() {
        List<VehiculoModel> vehiculoModels = vehiculoRepository.findVehiculosDisponibles();
        List<EntidadVehiculo> listaVehiculos = new ArrayList<>();
        vehiculoModels.forEach(vehiculo -> listaVehiculos.add(new EntidadVehiculo(vehiculo)));
        ArrayList<PlantaModel> plantas = new ArrayList<>();
        plantas.add(PlantaModel.builder().coordenadaX(12).coordenadaY(8).esPrincipal(true).build());
        plantas.add(PlantaModel.builder().coordenadaX(42).coordenadaY(42).build());
        plantas.add(PlantaModel.builder().coordenadaX(63).coordenadaY(3).build());
        listaVehiculos.forEach(v->v.setNodoActual(plantas.get(0)));

        
        return listaVehiculos;
    }

    public void actualizarEstadoVehiculo(Integer id){
        vehiculoRepository.updateEstadoVehiculo(id);
    }

    public void actualizarEstadoVehiculoToVacio(Integer id){
        vehiculoRepository.updateEstadoVehiculotoVacio(id);
    }

    /*
    public int actualizarTiempoEstado(int id, Timestamp fechaInicio){
        return vehiculoRepository.updateEstadoVehiculoTiempoEstado(id,fechaInicio);
    }*/

    public int actualizarTiempoEstado(int id){
        return vehiculoRepository.updateEstadoVehiculoTiempoEstado(id);
    }

    public int inicializarFechaInicioVehiculo(Timestamp fechaHora){
        return vehiculoRepository.ingresarFechasInicioInicializados(fechaHora);
    }

}
