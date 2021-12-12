package pe.edu.pucp.mvc.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.pucp.mvc.dtos.PedidosRutasDTO;
import pe.edu.pucp.mvc.models.NodoModel;
import pe.edu.pucp.mvc.models.PedidoModel;
import pe.edu.pucp.mvc.repositories.PedidoRepository;

import java.util.Calendar;
import java.util.List;

@Service
public class PedidoService {

    @Autowired
    PedidoRepository pedidoRepository;
    public PedidoModel guardarPedido(PedidoModel pedido){
        return pedidoRepository.save(pedido);
    }

    public List<PedidoModel> listaPedidosSinAtender(){
        return pedidoRepository.findPedidosSinAtender();
    }

    public Integer getMaxIdNodo(){
        Integer id = pedidoRepository.findMaxIdNodo();
        if (id == null){
            return -1;
        }else{
            return id;
        }
    }

    public int actualizarPedidosAtentidosDesdoblado(Integer idNodo, Integer idExtendido){
        return pedidoRepository.actualizarPedidoAtentido(idNodo, idExtendido);
    }

    public int verificarTotalPedidosDesdobladosAtendidos(Integer idNodo){
        return pedidoRepository.getValuePedidosTotalAtentido(idNodo);
    }

    public int actualizarPedidoPadreAtentido(Integer idNodo){
        return pedidoRepository.actualizarPedidoPadre(idNodo);
    }

    public List<PedidoModel> obtenerPedidos3días(Calendar inicio, Calendar fin, int dia){
        return pedidoRepository.getPedidos3dias(inicio,fin,dia);
    }

}
