
package pe.edu.pucp.mvc.models;
import java.util.Calendar;
import lombok.AllArgsConstructor;
import lombok.*;
import lombok.experimental.SuperBuilder;
import pe.edu.pucp.algorithm.Node;


@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor

public class Pedido extends Node {
    private int idPedido;

    private int idDesdoblado;
    private String client;
    private Calendar orderDate;
    private double quantityGLP;
    private Calendar hoursLimit;
    private double timePedido;
    @Builder.Default
    private boolean flat = false;
    
    public boolean merge(Pedido pedido){
        return this.getCoordX() == pedido.getCoordX() && 
                this.getCoordY() == pedido.getCoordY() && 
                this.idPedido == pedido.getIdPedido() &&
                (this.client == null ? pedido.getClient() == null : this.client.equals(pedido.getClient())) &&
                this.hoursLimit.equals(pedido.getHoursLimit());
    }
    public boolean completetlyEqual(Pedido pedido){
        return this.getCoordX() == pedido.getCoordX() && 
                this.getCoordY() == pedido.getCoordY() && 
                this.idDesdoblado == pedido.getIdDesdoblado() &&
                this.idPedido == pedido.getIdPedido() &&
                (this.client == null ? pedido.getClient() == null : this.client.equals(pedido.getClient())) &&
                this.hoursLimit.equals(pedido.getHoursLimit()) &&
                this.quantityGLP == pedido.getQuantityGLP();
    }
    
    @Override
    public String toString(){
      
        return String.format("[%d,%d]", this.getCoordX(), this.getCoordY());
    }
    
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Pedido){
            Pedido r = (Pedido) obj;
            return this.getCoordX() == r.getCoordX() && this.getCoordY() == r.getCoordY() && this.idPedido == r.getIdPedido();
        }
        else if(obj instanceof Node){
            Node v = (Node) obj;
            return this.getCoordX() == v.getCoordX() && this.getCoordY() == v.getCoordY();
        }
        else return false;
    }
    
    public void print(){
        System.out.print("CoordX " + this.getCoordX());
        System.out.print("  CoordY " + this.getCoordY());
        System.out.print("  idPedido " + this.getIdPedido());
        
        System.out.print("  hoursLimit " + this.getHoursLimit().getTimeInMillis());
        System.out.println();
    }

}
