/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.edu.pucp.mvc.models;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pe.edu.pucp.algorithm.Node;
import pe.edu.pucp.utils.VehicleState;
import pe.edu.pucp.utils.VehicleType;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Vehicle {
    private int idVehicle;
    private double velocity;
    private VehicleType type;
    private double grossWeightTara;
    private int loadGLP;
    private double loadWeightGLP;
    private double totalWeight;
    private VehicleState state;
    private Calendar initDate;
    @Builder.Default
    private double quantityRequest = 0;
    @Builder.Default
    private List<Pedido> requestList = new ArrayList<>();
    private Pedido assignRequest;
    @Builder.Default
    Node currentLocation = null;
    @Builder.Default
    ArrayList<Node> ruta = null;
    private double fuel;
    
    public void clearVehicle(){
        quantityRequest = 0;
        requestList.clear();
        state = VehicleState.DISPONIBLE;
    }
    
    public float calculateTimeToDispatch(){
        return ((float) this.ruta.size() / (float) this.velocity)*60; 
    }

}
