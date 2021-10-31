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

    
    public PedidoModel(double cantidadGLP, double costoOperativo){
        this.cantidadGLP=cantidadGLP;
        this.costoOperativo=costoOperativo;
    }


    public PedidoModel() {

    }

    public int getIdPedido() {
        return idPedido;
    }

    public double getCantidadGLP() {
        return cantidadGLP;
    }

    public double getCostoOperativo() {
        return costoOperativo;
    }
     
    /*
    public void setIdMapa(int idMapa) {
        this.idMapa = idMapa;
    }

    public void setLargo(int largo) {
        this.largo = largo;
    }

    public void setAncho(int ancho) {
        this.ancho = ancho;
    }
    */
}
