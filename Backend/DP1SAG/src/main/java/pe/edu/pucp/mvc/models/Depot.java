
package pe.edu.pucp.mvc.models;


import lombok.*;
import lombok.experimental.SuperBuilder;
import pe.edu.pucp.algorithm.Node;


@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Depot extends Node {
    
    @Builder.Default
    private boolean isPrincipalDepot = false;
    
    @Override
    public String toString(){
        return String.format("[%d,%d]", this.getCoordX(), this.getCoordY());
    }
    
    @Override
    public boolean equals(Object obj) {
        Node v = (Node) obj;
        return this.getCoordX() == v.getCoordX() && this.getCoordY() == v.getCoordY();
    }
}
