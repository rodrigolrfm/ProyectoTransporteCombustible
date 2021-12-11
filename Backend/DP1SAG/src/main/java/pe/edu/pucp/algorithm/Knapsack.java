
package pe.edu.pucp.algorithm;


import java.util.List;
import java.util.PriorityQueue;
import javafx.util.Pair;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import pe.edu.pucp.mvc.models.PedidoModel;
import pe.edu.pucp.mvc.models.EntidadVehiculo;
import pe.edu.pucp.mvc.services.VehiculoService;


public class Knapsack {

    public static int allocate(PriorityQueue<Pair<Float, PedidoModel>> requestList, List<EntidadVehiculo> vehicles, List<PedidoModel> listaDesdoblada) throws Exception {
        boolean assign = false;
        int assigned = 0;
        for (Pair<Float, PedidoModel> par: requestList){ 
            PedidoModel r = par.getValue();
            if(!r.isAtendido()){
                assign = assignToAvailableVehicle(r, vehicles, listaDesdoblada);
                if(assign){
                    assigned += 1;
                    r.setAtendido(true);
                }
            }
        }
    
        return assigned;
    }

    public static boolean assignToAvailableVehicle(PedidoModel r, List<EntidadVehiculo> vehicles, List<PedidoModel> listaDesdoblada) throws Exception {
        boolean flat = false;
        if (vehicles.isEmpty()) {
            throw new Exception("Vehiculos no aptos");
        }
        for (EntidadVehiculo v : vehicles) {
            if ((v.getEstadoVehiculo()==0) &&
                r.getCantidadGLP() + v.getCantidadPedidos() <= v.getCargaGLP()) {

                v.setCantidadPedidos(r.getCantidadGLP()+ v.getCantidadPedidos());
                if(!v.getListaPedidos().isEmpty()){
                    for(PedidoModel req : v.getListaPedidos()){
                        if(req.equals(r)){
                            req.setCantidadGLP(r.getCantidadGLP()+ req.getCantidadGLP());
                            int i = 0;
                            for(; i< listaDesdoblada.size(); i++)
                                if(listaDesdoblada.get(i).completetlyEqual(r))
                                {
                                    break;
                                }
                            listaDesdoblada.remove(i);
                            flat = true;
                        }
                    }
                    if (!flat) v.getListaPedidos().add(r);
                }
                else {
                    v.getListaPedidos().add(r);
                }
                if(v.getCantidadPedidos() == v.getCargaGLP()){
                    v.setEstadoVehiculo(1);
                }
                return true;
            }
        }
        return false;
    } 
}
