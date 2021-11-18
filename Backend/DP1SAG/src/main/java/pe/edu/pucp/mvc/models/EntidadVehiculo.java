package pe.edu.pucp.mvc.models;

import lombok.*;
import pe.edu.pucp.utils.EstadoVehiculo;
import pe.edu.pucp.utils.TipoVehiculo;

import javax.persistence.*;
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

}