package pe.edu.pucp.mvc.repositories;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pe.edu.pucp.mvc.models.NodoModel;
import pe.edu.pucp.mvc.models.EntidadVehiculo;
import pe.edu.pucp.mvc.models.VehiculoModel;

import java.util.Optional;

@Repository
public interface VehiculoRepository extends CrudRepository<VehiculoModel, Integer> {
    public abstract Optional<VehiculoModel> findById(Integer id);

}
