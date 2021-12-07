package pe.edu.pucp.mvc.models;
import lombok.*;

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

    @Column(nullable = true, length = 50)
    private String estado;

    @Column(nullable = true)
    private double velocidad;

    @Column(nullable = true)
    private int  tipoVehiculo;

    @Column(nullable = true)
    private double pesoTara;

    @Column(nullable = true)
    private int cargaGLP;

    @Column(nullable = true)
    private double pesoCargaGLP;

    @Column(nullable = true)
    private double pesoTotal;

    @Column(nullable = true)
    private int estadoVehiculo;

    @Column(nullable = true)
    private Calendar fechaInicio;

    @Builder.Default
    @Column(nullable = true)
    private double cantidadPedidos = 0;


    @OneToMany(mappedBy = "vehiculoModel")
    @Builder.Default
    @Column(nullable = true)
    private List<PedidoModel> listaPedidos = new ArrayList<>();

    private double combustible;

    @Column(nullable = true)
    private double capacidad;

    @Column(nullable = true)
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
        this.pesoCargaGLP = vehiculo.getPesoCargaGLP();
        this.pesoTotal = vehiculo.getPesoTotal();
        this.pesoTara = vehiculo.getPesoTara();
        this.combustible =vehiculo.getCombustible();
    }

}