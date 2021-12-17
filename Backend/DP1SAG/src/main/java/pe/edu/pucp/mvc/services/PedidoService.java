package pe.edu.pucp.mvc.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.pucp.mvc.dtos.PedidosRutasDTO;
import pe.edu.pucp.mvc.models.NodoModel;
import pe.edu.pucp.mvc.models.PedidoModel;
import pe.edu.pucp.mvc.repositories.PedidoRepository;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Service
public class PedidoService {

    private static int iniHora = 0;
    private static int finHora = 4;
    private static int cantRepeticionesxDia = 0;
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

        Timestamp ini = new Timestamp(inicio.getTime().getTime());
        Timestamp tofecha = new Timestamp(fin.getTime().getTime());
        List<PedidoModel> listaPedidos = pedidoRepository.getPedidos3dias(ini,tofecha,dia);
        List<PedidoModel> newLista = new ArrayList<>();
        Calendar fromdata = Calendar.getInstance();
        Calendar todata = Calendar.getInstance();

        if(!(cantRepeticionesxDia==6)){
            /*
            fromdata.set(Calendar.MONTH,inicio.get(Calendar.MONTH)-1);
            fromdata.set(Calendar.YEAR,inicio.get(Calendar.YEAR));
            fromdata.set(Calendar.DAY_OF_MONTH,dia);
            fromdata.set(Calendar.HOUR,iniHora);
            fromdata.set(Calendar.SECOND,0);
            fromdata.set(Calendar.MINUTE,0);
            todata.set(Calendar.MONTH,inicio.get(Calendar.MONTH)-1);
            todata.set(Calendar.YEAR,inicio.get(Calendar.YEAR));
            todata.set(Calendar.DAY_OF_MONTH,dia);
            todata.set(Calendar.HOUR,finHora);
            todata.set(Calendar.SECOND,0);
            todata.set(Calendar.MINUTE,0);
            */
            fromdata.set(inicio.get(Calendar.YEAR),inicio.get(Calendar.MONTH),dia,iniHora,0,0);
            todata.set(inicio.get(Calendar.YEAR),inicio.get(Calendar.MONTH),dia,finHora,0,0);
            for (PedidoModel pedido:listaPedidos) {

                if ((pedido.getFechaPedido().compareTo(fromdata)>=0) && ((pedido.getFechaPedido().compareTo(todata)<=0))){
                    newLista.add(pedido);
                }
            }
            iniHora+=4;
            finHora+=4;
            cantRepeticionesxDia+=1;
        }else{
            iniHora = 0;
            finHora = 4;
            cantRepeticionesxDia = 1;
            fromdata.set(inicio.get(Calendar.YEAR),inicio.get(Calendar.MONTH),dia,iniHora,0,0);
            todata.set(inicio.get(Calendar.YEAR),inicio.get(Calendar.MONTH),dia,finHora,0,0);
            for (PedidoModel pedido:listaPedidos) {
                if ((pedido.getFechaPedido().compareTo(fromdata)>0) && ((pedido.getFechaPedido().compareTo(todata)<0))){
                    newLista.add(pedido);
                }
            }
        }
        return newLista;
    }

}
