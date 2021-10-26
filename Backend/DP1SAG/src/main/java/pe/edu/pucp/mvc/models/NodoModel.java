package pe.edu.pucp.mvc.models;

import ch.qos.logback.core.net.server.Client;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name="nodo")
public class NodoModel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true,nullable = false)
    private int idNodo;


    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name = "idMapa", nullable = false)
    private MapaModel mapaModel;

    @OneToOne(mappedBy = "lugarNodo", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private ClienteModel clienteModel;

    @OneToOne(mappedBy = "nodo", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private PlantaModel planta;

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

    public int getIdNodo() {
        return idNodo;
    }

    public void setIdNodo(int idNodo) {
        this.idNodo = idNodo;
    }

    public int getCoordenadaX() {
        return coordenadaX;
    }

    public void setCoordenadaX(int coordenadaX) {
        this.coordenadaX = coordenadaX;
    }

    public int getCoordenadaY() {
        return coordenadaY;
    }

    public void setCoordenadaY(int coordenadaY) {
        this.coordenadaY = coordenadaY;
    }

    public int getEstaBloqueado() {
        return estaBloqueado;
    }

    public void setEstaBloqueado(int estaBloqueado) {
        this.estaBloqueado = estaBloqueado;
    }

    public MapaModel getMapaModel() {
        return mapaModel;
    }

    public void setMapaModel(MapaModel mapaModel) {
        this.mapaModel = mapaModel;
    }

    public ClienteModel getClienteModel() {
        return clienteModel;
    }

    public void setClienteModel(ClienteModel clienteModel) {
        this.clienteModel = clienteModel;
    }

    public PlantaModel getPlanta() {
        return planta;
    }

    public void setPlanta(PlantaModel planta) {
        this.planta = planta;
    }
}
