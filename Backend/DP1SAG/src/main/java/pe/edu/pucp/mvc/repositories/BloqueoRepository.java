package pe.edu.pucp.mvc.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pe.edu.pucp.mvc.models.BloqueoModel;
import pe.edu.pucp.mvc.models.NodoModel;
import pe.edu.pucp.mvc.models.VehiculoModel;

import java.util.List;
import java.util.Optional;

@Repository
public interface BloqueoRepository extends CrudRepository<BloqueoModel, Integer> {
    public abstract Optional<BloqueoModel> findById(Integer id);

    @Query(value ="CALL OBTENER_BLOQUEOS_DIA_DIA();",nativeQuery = true)
    List<BloqueoModel> findBloqueosNodos();

    @Query(value ="select * from bloqueo;",nativeQuery = true)
    List<BloqueoModel> getBloqueos();

    @Query(value ="call get_bloqueos_rango_fechas(?1, ?2);",nativeQuery = true)
    List<BloqueoModel> getBloqueosFechas(String fechaIni, String fechaFin);

    @Query(value ="SELECT * FROM bloqueo WHERE ( ?1 <= fin_bloqueo ) AND ( ?2 >= inicio_bloqueo ) AND (day(inicio_bloqueo) = ?3 );",nativeQuery = true)
    List<BloqueoModel> getBloqueosFechasIntervarlo3dias(String fechaIni, String fechaFin, int dia);

    @Query(value = "call get_bloqueos_3_dias()", nativeQuery = true)
    List<BloqueoModel> getBloqueos3dias();
}