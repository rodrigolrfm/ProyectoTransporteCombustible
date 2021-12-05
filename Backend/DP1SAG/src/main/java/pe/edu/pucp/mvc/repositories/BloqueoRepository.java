package pe.edu.pucp.mvc.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pe.edu.pucp.mvc.models.BloqueoModel;
import pe.edu.pucp.mvc.models.NodoModel;

import java.util.Optional;

@Repository
public interface BloqueoRepository extends CrudRepository<BloqueoModel, Integer> {
    public abstract Optional<BloqueoModel> findById(Integer id);

}