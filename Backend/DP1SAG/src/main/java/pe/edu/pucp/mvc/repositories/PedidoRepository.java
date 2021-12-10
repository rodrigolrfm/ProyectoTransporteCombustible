package pe.edu.pucp.mvc.repositories;

import org.springframework.data.jpa.repository.Query;
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

    @Query(value ="CALL OBTENER_PEDIDOS_SIN_ATENDER();", nativeQuery = true)
    List<PedidoModel> findPedidosSinAtender();

    @Query(value ="SELECT max(id_nodo) FROM pedido;", nativeQuery = true)
    Integer findMaxIdNodo();
}
