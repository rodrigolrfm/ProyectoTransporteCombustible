package pe.edu.pucp.mvc.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Setter
@Getter
@Entity
@Table(name="mapa")
public class MapaModel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true,nullable = false)
    private int idMapa;

    private int largo;
    private int ancho;

    @OneToMany(mappedBy = "mapaModel", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<NodoModel> nodoList;

    public MapaModel(int largo, int ancho){
        this.largo=largo;
        this.ancho=ancho;
    }


    public MapaModel() {

    }

    public int getIdMapa() {
        return idMapa;
    }

    public int getLargo() {
        return largo;
    }

    public int getAncho() {
        return ancho;
    }

    public void setIdMapa(int idMapa) {
        this.idMapa = idMapa;
    }

    public void setLargo(int largo) {
        this.largo = largo;
    }

    public void setAncho(int ancho) {
        this.ancho = ancho;
    }
}
