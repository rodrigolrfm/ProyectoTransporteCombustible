package pe.edu.pucp.mvc.models;

import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EntidadVehiculo implements Serializable {

    private int idVehiculo;
    private String estado;
    private double velocidad;
    private int tipoVehiculo;
    private double pesoTara;
    private int cargaGLP;
    private double pesoCargaGLP;
    private double pesoTotal;

    private int estadoVehiculo;
    private Calendar fechaInicio;

    @Builder.Default
    private double cantidadPedidos = 0;

    @Builder.Default
    private List<PedidoModel> listaPedidos = new ArrayList<>();


    @Builder.Default
    NodoModel nodoActual = null;

    @Builder.Default
    List<NodoModel> rutaVehiculo = null;

    private double combustible;

    private double capacidad;

    private int tieneAveria;

    public void clearVehicle(){
        cantidadPedidos = 0;
        listaPedidos.clear();
        estadoVehiculo = 0;
    }

    public float calculateTimeToDispatch(){
        return ((float) this.rutaVehiculo.size() / (float) this.velocidad)*60;
    }

    public List<PositionModel> getRutaVehiculoPositions(List<PedidoModel> pedidos) {
        List<PositionModel> ruta = new ArrayList<>();
        this.getRutaVehiculo().forEach(nodo -> {
            int esDestino = 0;
            for(PedidoModel pedido : pedidos) {
                if (nodo.getCoordenadaX() == pedido.getCoordenadaX() && nodo.getCoordenadaY() == pedido.getCoordenadaY()) {
                    esDestino = 1;
                    break;
                }
            }
            ruta.add(PositionModel.builder().x(nodo.getCoordenadaX()).y(nodo.getCoordenadaY()).destino(Integer.valueOf(esDestino)).build());
        });
        return ruta;
    }

    public EntidadVehiculo(VehiculoModel vehiculo){
        this.idVehiculo = vehiculo.getIdVehiculo();
        this.cantidadPedidos=vehiculo.getCantidadPedidos();
        this.capacidad = vehiculo.getCapacidad();
        this.cargaGLP = vehiculo.getCargaGLP();
        this.estadoVehiculo = vehiculo.getEstadoVehiculo();
        this.estado = vehiculo.getEstado();
        this.fechaInicio = vehiculo.getFechaInicio();
        this.velocidad = vehiculo.getVelocidad();
        this.tieneAveria = vehiculo.getTieneAveria();
        this.tipoVehiculo = vehiculo.getTipoVehiculo();
        this.pesoCargaGLP = vehiculo.getPesoCargaGLP();
        this.pesoTotal = vehiculo.getPesoTotal();
        this.pesoTara = vehiculo.getPesoTara();
        this.combustible =vehiculo.getCombustible();
    }
}