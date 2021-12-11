package pe.edu.pucp.mvc.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pe.edu.pucp.mvc.models.SimulacionModel;

@Repository
public interface SimulacionRepository extends CrudRepository<SimulacionModel, Integer> {

}
