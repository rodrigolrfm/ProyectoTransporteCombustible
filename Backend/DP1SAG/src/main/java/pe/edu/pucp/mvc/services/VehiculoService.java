package pe.edu.pucp.mvc.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.pucp.mvc.models.EntidadVehiculo;
import pe.edu.pucp.mvc.models.VehiculoModel;
import pe.edu.pucp.mvc.repositories.VehiculoRepository;

@Service
public class VehiculoService {
    @Autowired
    VehiculoRepository vehiculoRepository;
    public VehiculoModel guardarVehiculo(VehiculoModel vehiculo){
        return vehiculoRepository.save(vehiculo);
    }
}
