package pe.edu.pucp.mvc.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pe.edu.pucp.mvc.models.NodoModel;
import pe.edu.pucp.mvc.models.PedidoModel;

import java.util.Optional;

@Repository
public interface PedidoRepository extends CrudRepository<PedidoModel, Integer> {
    public abstract Optional<PedidoModel> findById(Integer id);
}
