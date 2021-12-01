package pe.edu.pucp.mvc.models;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Calendar;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="pedido")
public class PedidoModel extends NodoModel implements Serializable {

    @Id
    @Column(unique = false)
    private int idNodo;

    @Column(name = "idExtendido",nullable = true)
    private int idExtendido;

    @Column(name = "fechaPedido",nullable = true)
    private Calendar fechaPedido;

    @Column(name = "horasLimite",nullable = true)
    private Calendar horasLimite;

    @Column(name = "cantidadGLP",nullable = true)
    private double cantidadGLP;

    @Column(name = "costoOperativo",nullable = true)
    private double costoOperativo;

    @Column(name = "horaPedido",nullable = true)
    private double horaPedido;

    @ManyToOne
    @JoinColumn(name = "idVehiculo",nullable = true)
    private VehiculoModel vehiculoModel;

    @Builder.Default
    private boolean atendido = false;

    @Column(name = "cliente",nullable = true,length = 200)
    private String clienteModel;


    public boolean merge(PedidoModel PedidoModel){
        return this.getCoordenadaX() == PedidoModel.getCoordenadaX() &&
                this.getCoordenadaY() == PedidoModel.getCoordenadaY() &&
                this.getIdNodo() == PedidoModel.getIdNodo() &&
                (this.clienteModel == null ? PedidoModel.getClienteModel() == null : this.clienteModel.equals(PedidoModel.getClienteModel())) &&
                this.horasLimite.equals(PedidoModel.getHorasLimite());
    }
    public boolean completetlyEqual(PedidoModel PedidoModel){
        return this.getCoordenadaX() == PedidoModel.getCoordenadaX() &&
                this.getCoordenadaY() == PedidoModel.getCoordenadaY() &&
                this.idExtendido == PedidoModel.getIdExtendido() &&
                this.getIdNodo() == PedidoModel.getIdNodo() &&
                (this.clienteModel == null ? PedidoModel.getClienteModel() == null : this.clienteModel.equals(PedidoModel.getClienteModel())) &&
                this.horasLimite.equals(PedidoModel.getHorasLimite()) &&
                this.cantidadGLP == PedidoModel.getCantidadGLP();
    }

    @Override
    public String toString(){

        return String.format("[%d,%d]", this.getCoordenadaX(), this.getCoordenadaY());
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof PedidoModel){
            PedidoModel r = (PedidoModel) obj;
            return this.getCoordenadaX() == r.getCoordenadaX() && this.getCoordenadaY() == r.getCoordenadaY() && this.getIdNodo() == r.getIdNodo();
        }
        else if(obj instanceof NodoModel){
            NodoModel v = (NodoModel) obj;
            return this.getCoordenadaX() == v.getCoordenadaX() && this.getCoordenadaY() == v.getCoordenadaY();
        }
        else return false;
    }

    public void print(){
        System.out.print("coordenadaX " + this.getCoordenadaX());
        System.out.print("  coordenadaY " + this.getCoordenadaY());
        System.out.print("  idPedido " + this.getIdNodo());

        System.out.print("  hoursLimit " + this.getHorasLimite().getTimeInMillis());
        System.out.println();
    }

}
