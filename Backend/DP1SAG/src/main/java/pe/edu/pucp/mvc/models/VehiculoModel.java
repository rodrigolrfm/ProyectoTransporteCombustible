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

    @Column(name = "estado",nullable = true, length = 50)
    private String estado;

    @Column(name = "velocidad",nullable = true)
    private double velocidad;

    @Column(name = "tipoVehiculo",nullable = true)
    private int  tipoVehiculo;

    @Column(name = "pesoTara",nullable = true)
    private double pesoTara;

    @Column(name = "cargaGLP",nullable = true)
    private int cargaGLP;

    @Column(name = "pesoCargaGLP",nullable = true)
    private double pesoCargaGLP;

    @Column(name = "pesoTotal",nullable = true)
    private double pesoTotal;

    @Column(name = "estadoVehiculo",nullable = true)
    private int estadoVehiculo;

    @Column(name = "fechaInicio",nullable = true)
    private Calendar fechaInicio;

    @Builder.Default
    @Column(name = "cantidadPedidos",nullable = true)
    private double cantidadPedidos = 0;

    @OneToMany(mappedBy = "vehiculoModel")
    @Builder.Default
    private List<PedidoModel> listaPedidos = new ArrayList<>();

    @Column(name = "combustible",nullable = true)
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
        this.pesoTotal = vehiculo.getPesoTotal();
        this.pesoTara = vehiculo.getPesoTara();
        this.combustible =vehiculo.getCombustible();
    }

}