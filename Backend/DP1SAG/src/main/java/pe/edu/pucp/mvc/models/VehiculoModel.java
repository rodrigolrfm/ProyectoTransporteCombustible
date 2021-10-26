package pe.edu.pucp.mvc.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name="vehiculo")
public class VehiculoModel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true,nullable = false)
    private int idVehiculo;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "idConductor",nullable = false)
    private ConductorModel conductor;

    @OneToMany(mappedBy = "vehiculo", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<RutaModel> rutaList;

    @Column(nullable = false, length = 50)
    private String estado;

    @Column(nullable = false)
    private double capacidad;

    @Column(nullable = false)
    private int tieneAveria;

    @OneToMany(mappedBy = "vxPedido", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<VehiculoXPedidoModel> ListVxPedido;
}
