package pe.edu.pucp.mvc.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@Entity
@Table(name="pedido")
public class PedidoModel implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true,nullable = false)
    private int idPedido;

    private double cantidadGLP;
    private LocalDateTime horaPedido;
    private LocalDateTime horaEntrega;
    private double costoOperativo;

    @ManyToOne
    @JoinColumn(name = "idCliente",nullable = false)
    private ClienteModel clienteModel;


}
