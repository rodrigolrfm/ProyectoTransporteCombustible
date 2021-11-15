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
@Entity
@Table(name = "planta")
public class PlantaModel extends NodoModel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true,nullable = false)
    private int idPlanta;


    @OneToOne
    @JoinColumn(name = "idNodo",nullable = false)
    private NodoModel nodo;

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
