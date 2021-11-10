
package pe.edu.pucp.algorithm;


import java.util.List;
import java.util.PriorityQueue;
import javafx.util.Pair;
import pe.edu.pucp.mvc.models.Pedido;
import pe.edu.pucp.mvc.models.Vehicle;
import pe.edu.pucp.utils.VehicleState;


public class Knapsack {
    
    public static int allocate(PriorityQueue<Pair<Float, Pedido>> requestList, List<Vehicle> vehicles, List<Pedido> listaDesdoblada) throws Exception {
        boolean assign = false;
        int assigned = 0;
        for (Pair<Float, Pedido> par: requestList){ 
            Pedido r = par.getValue();
            if(!r.isFlat()){
                assign = assignToAvailableVehicle(r, vehicles, listaDesdoblada);
                if(assign){
                    assigned += 1;
                    r.setFlat(true); 
                }
            }
        }
    
        return assigned;
    }

    public static boolean assignToAvailableVehicle(Pedido r, List<Vehicle> vehicles, List<Pedido> listaDesdoblada) throws Exception {
        boolean flat = false;
        if (vehicles.isEmpty()) {
            throw new Exception("Vehiculos no aptos");
        }
        for (Vehicle v : vehicles) {
            if (v.getState().equals(VehicleState.DISPONIBLE) &&
                r.getQuantityGLP() + v.getQuantityRequest() <= v.getLoadGLP()) {
                v.setQuantityRequest(r.getQuantityGLP()+ v.getQuantityRequest());
                if(!v.getRequestList().isEmpty()){
                    for(Pedido req : v.getRequestList()){
                        if(req.equals(r)){
                            req.setQuantityGLP(r.getQuantityGLP()+ req.getQuantityGLP());
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
                    v.setState(VehicleState.NO_DISPONIBLE);
                }
                return true;
            }
        }
        return false;
    } 
}
