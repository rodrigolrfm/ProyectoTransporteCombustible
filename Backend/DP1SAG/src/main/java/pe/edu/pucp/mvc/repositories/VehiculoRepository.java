package pe.edu.pucp.mvc.repositories;


import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.pucp.mvc.models.NodoModel;
import pe.edu.pucp.mvc.models.EntidadVehiculo;
import pe.edu.pucp.mvc.models.PedidoModel;
import pe.edu.pucp.mvc.models.VehiculoModel;

import java.util.List;
import java.util.Optional;

@Repository
public interface VehiculoRepository extends CrudRepository<VehiculoModel, Integer> {
    public abstract Optional<VehiculoModel> findById(Integer id);

    @Query(value ="CALL OBTENER_VEHICULOS_DISPONIBLES();",nativeQuery = true)
    List<VehiculoModel> findVehiculosDisponibles();

    @Transactional
    @Modifying
    @Query(value ="update vehiculo set estado_vehiculo = 1 where id_vehiculo = ?1 ;",nativeQuery = true)
    int updateEstadoVehiculo(Integer id);


}
