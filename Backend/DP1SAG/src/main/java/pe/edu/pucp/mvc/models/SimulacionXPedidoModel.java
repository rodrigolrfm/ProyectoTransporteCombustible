package pe.edu.pucp.mvc.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Setter
@Getter
@Entity
@Table(name = "simulacionxpedido")
public class SimulacionXPedidoModel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true,nullable = false)
    private int idSimulacionXPedido;

    @ManyToOne
    @JoinColumn(name = "idSimulacion",nullable = false)
    private SimulacionModel sxPedido;

    @ManyToOne
    @JoinColumns({@JoinColumn(name = "idNodo",nullable = false),
                 @JoinColumn(name = "idExtendido",nullable = false)})
    private PedidoModel pxSimulacion;

    //@ManyToOne
    //@JoinColumn(name = "idExtendido",nullable = false)
    //private PedidoModel pxSimulacionExtendido;

    @Column(nullable = false)
    private int setCompleto;

}
