package pe.edu.pucp.mvc.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Setter
@Getter
@Entity
@Table(name = "vehiculoxpedido")
public class VehiculoXPedidoModel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true,nullable = false)
    private int idVehiculoXPedido;

    @ManyToOne
    @JoinColumn(name = "idVehiculo",nullable = false)
    private VehiculoModel vxPedido;

    @ManyToOne
    @JoinColumns({@JoinColumn(name = "idNodo",nullable = false),
            @JoinColumn(name = "idExtendido",nullable = false)})
    private PedidoModel pxVehiculo;

    //@ManyToOne
    //@JoinColumn(name = "idExtendido",nullable = false)
    //private PedidoModel pxVehiculoExtendido;

}
