package pe.edu.pucp.mvc.repositories;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.pucp.mvc.models.NodoModel;
import pe.edu.pucp.mvc.models.PedidoModel;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

@Repository
public interface PedidoRepository extends CrudRepository<PedidoModel, Integer> {
    public abstract Optional<PedidoModel> findById(Integer id);

    @Query(value ="CALL OBTENER_PEDIDOS_SIN_ATENDER();", nativeQuery = true)
    List<PedidoModel> findPedidosSinAtender();

    @Query(value ="SELECT max(id_nodo) FROM pedido;", nativeQuery = true)
    Integer findMaxIdNodo();

    @Transactional
    @Modifying
    @Query(value ="update pedido set atendido = 1 where id_nodo = ?1 and id_extendido = ?2 ;", nativeQuery = true)
    Integer actualizarPedidoAtentido(Integer idNodo, Integer idExtendido);

    @Procedure("OBTENER_LISTA_DESDOBLADOS_ATENDIDOS")
    int getValuePedidosTotalAtentido(Integer idNodo);

    @Transactional
    @Modifying
    @Query(value ="update pedido set atendido = 1 where id_nodo = ?1 and id_extendido = 0 ;", nativeQuery = true)
    Integer actualizarPedidoPadre(Integer idNodo);

    /*
    @Procedure("OBTENER_PEDIDOS_3_DIAS")
    */

    @Query(value ="select * from pedido where (fecha_pedido >= ?1 ) and ( fecha_pedido <= ?2 ) and ( day(fecha_pedido) = ?3 ) and ( id_extendido > 0 ) and ( atendido = 0 );", nativeQuery = true)
    List<PedidoModel> getPedidos3dias(Timestamp inicio, Timestamp fin , int dia);


}

