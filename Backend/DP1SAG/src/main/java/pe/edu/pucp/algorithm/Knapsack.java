
package pe.edu.pucp.algorithm;


import java.util.List;
import java.util.PriorityQueue;
import javafx.util.Pair;
import pe.edu.pucp.mvc.models.PedidoModel;
import pe.edu.pucp.mvc.models.Vehicle;
import pe.edu.pucp.utils.EstadoVehiculo;


public class Knapsack {
    
    public static int allocate(PriorityQueue<Pair<Float, PedidoModel>> requestList, List<Vehicle> vehicles, List<PedidoModel> listaDesdoblada) throws Exception {
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

    public static boolean assignToAvailableVehicle(PedidoModel r, List<Vehicle> vehicles, List<PedidoModel> listaDesdoblada) throws Exception {
        boolean flat = false;
        if (vehicles.isEmpty()) {
            throw new Exception("Vehiculos no aptos");
        }
        for (Vehicle v : vehicles) {
            if (v.getState().equals(EstadoVehiculo.DISPONIBLE) &&
                r.getCantidadGLP() + v.getQuantityRequest() <= v.getLoadGLP()) {
                v.setQuantityRequest(r.getCantidadGLP()+ v.getQuantityRequest());
                if(!v.getRequestList().isEmpty()){
                    for(PedidoModel req : v.getRequestList()){
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
                    if (!flat) v.getRequestList().add(r);
                }
                else v.getRequestList().add(r);
                if(v.getQuantityRequest() == v.getLoadGLP()){
                    v.setState(EstadoVehiculo.NO_DISPONIBLE);
                }
                return true;
            }
        }
        return false;
    } 
}
