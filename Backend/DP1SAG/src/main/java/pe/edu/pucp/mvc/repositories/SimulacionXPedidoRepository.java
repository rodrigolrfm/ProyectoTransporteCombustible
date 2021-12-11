package pe.edu.pucp.mvc.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pe.edu.pucp.mvc.models.PedidoModel;
import pe.edu.pucp.mvc.models.SimulacionXPedidoModel;

@Repository
public interface SimulacionXPedidoRepository extends CrudRepository<SimulacionXPedidoModel, Integer> {
}
