package pe.edu.pucp.mvc.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.pucp.mvc.models.EntidadVehiculo;
import pe.edu.pucp.mvc.models.PedidoModel;
import pe.edu.pucp.mvc.models.VehiculoModel;
import pe.edu.pucp.mvc.repositories.VehiculoRepository;

import java.util.List;
import java.util.Optional;

@Service
public class VehiculoService {

    @Autowired
    VehiculoRepository vehiculoRepository;

    public VehiculoModel guardarVehiculo(VehiculoModel vehiculo){
        return vehiculoRepository.save(vehiculo);
    }

    public List<VehiculoModel> listaVehiculosDisponibles(){
        return vehiculoRepository.findVehiculosDisponibles();
    }

    public void actualizarEstadoVehiculo(Integer id){
        vehiculoRepository.updateEstadoVehiculo(id);
    }

    public void actualizarEstadoVehiculoToVacio(Integer id){
        vehiculoRepository.updateEstadoVehiculotoVacio(id);
    }

}
