package pe.edu.pucp.mvc.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.io.Serializable;


@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor

public class PlantaModel extends NodoModel implements Serializable {


    @Builder.Default
    private boolean esPrincipal=false;

    @Override
    public String toString(){
        return String.format("[%d,%d]", this.getCoordenadaX(), this.getCoordenadaY());
    }

    @Override
    public boolean equals(Object obj) {
        NodoModel v = (NodoModel) obj;
        return this.getCoordenadaX() == v.getCoordenadaX() && this.getCoordenadaY() == v.getCoordenadaY();
    }

}
