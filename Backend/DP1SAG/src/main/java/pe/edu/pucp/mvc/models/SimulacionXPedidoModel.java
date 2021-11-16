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
    @JoinColumn(name = "idNodo",nullable = false)
    private PedidoModel pxSimulacion;

    @Column(nullable = false)
    private int setCompleto;

}
