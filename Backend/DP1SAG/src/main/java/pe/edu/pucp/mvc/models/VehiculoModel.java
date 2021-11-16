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
@Entity
@Table(name="vehiculo")
public class VehiculoModel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true,nullable = false)
    private int idVehiculo;

    @OneToOne
    @JoinColumn(name = "idConductor",nullable = false)
    private ConductorModel conductor;

    @Column(nullable = false, length = 50)
    private String estado;

    private double velocidad;
    private int  tipoVehiculo;

    private double pesoTara;
    private int cargaGLP;
    private double pesoCargaGLP;
    private double pesoTotal;

    private int estadoVehiculo;
    private Calendar fechaInicio;

    @Builder.Default
    private double cantidadPedidos = 0;

    @OneToMany(mappedBy = "vehiculoModel")
    @Builder.Default
    private List<PedidoModel> listaPedidos = new ArrayList<>();


    @OneToOne(mappedBy = "idNodo")
    @Builder.Default
    NodoModel nodoActual = null;

    @OneToMany(mappedBy = "idNodo")
    @Builder.Default
    ArrayList<NodoModel> rutaVehiculo = null;

    private double combustible;

    @Column(nullable = false)
    private double capacidad;

    @Column(nullable = false)
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
