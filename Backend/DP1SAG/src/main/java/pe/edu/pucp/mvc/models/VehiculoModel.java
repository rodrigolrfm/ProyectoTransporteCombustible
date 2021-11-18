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

    private double combustible;

    @Column(nullable = false)
    private double capacidad;

    @Column(nullable = false)
    private int tieneAveria;


    public VehiculoModel(EntidadVehiculo vehiculo){
        this.cantidadPedidos=vehiculo.getCantidadPedidos();
        this.capacidad = vehiculo.getCapacidad();
        this.cargaGLP = vehiculo.getCargaGLP();
        this.estadoVehiculo = vehiculo.getEstadoVehiculo();
        this.estado = vehiculo.getEstado();
        this.fechaInicio = vehiculo.getFechaInicio();
        this.velocidad = vehiculo.getVelocidad();
        this.tieneAveria = vehiculo.getTieneAveria();
        this.tipoVehiculo = vehiculo.getTipoVehiculo();
        this.pesoTotal = vehiculo.getPesoTotal();
        this.pesoTara = vehiculo.getPesoTara();
        this.combustible =vehiculo.getCombustible();
    }

}