package pe.edu.pucp.mvc.repositories;

import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pe.edu.pucp.mvc.models.NodoModel;
import pe.edu.pucp.mvc.models.PedidoModel;

import java.util.List;
import java.util.Optional;

@Repository
public interface PedidoRepository extends CrudRepository<PedidoModel, Integer> {
    public abstract Optional<PedidoModel> findById(Integer id);

    @Procedure(value="OBTENER_PEDIDOS_SIN_ATENDER();")
    List<PedidoModel> findPedidosSinAtender();
}
