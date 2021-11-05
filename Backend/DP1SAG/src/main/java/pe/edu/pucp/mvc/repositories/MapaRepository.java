package pe.edu.pucp.mvc.repositories;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pe.edu.pucp.mvc.models.MapaModel;

import java.util.Optional;

@Repository
public interface MapaRepository extends CrudRepository<MapaModel, Integer> {
    public abstract Optional<MapaModel> findById(Integer id);
}
