package pe.edu.pucp.mvc.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pe.edu.pucp.mvc.models.BloqueModel;
import pe.edu.pucp.mvc.models.NodoModel;

import java.util.Optional;

@Repository
public interface BloqueoRepository extends CrudRepository<BloqueModel, Integer> {
    public abstract Optional<BloqueModel> findById(Integer id);

}