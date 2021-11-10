package pe.edu.pucp.mvc.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name="nodo")
public class NodoModel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private int idNodo;

    
    @ManyToOne
    @JoinColumn(name = "idMapa",nullable = false)
    private MapaModel mapaModel;



    private int coordenadaX;
    private int coordenadaY;
    private int estaBloqueado;

    public NodoModel() {
    }

    public NodoModel(MapaModel mapaModel, int coordenadaX, int coordenadaY, int estaBloqueado) {
        this.mapaModel = mapaModel;
        this.coordenadaX = coordenadaX;
        this.coordenadaY = coordenadaY;
        this.estaBloqueado = estaBloqueado;
    }

}
