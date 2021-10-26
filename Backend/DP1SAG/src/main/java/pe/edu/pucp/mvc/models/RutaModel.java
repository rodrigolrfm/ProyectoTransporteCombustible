package pe.edu.pucp.mvc.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name="ruta")
public class RutaModel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true,nullable = false)
    private int idRuta;

    @Column(nullable = false)
    private double distanciaTotal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idVehiculo",nullable = false)
    private VehiculoModel vehiculo;

    public RutaModel(){
    }
    public RutaModel(double distanciaTotal, VehiculoModel vehiculo){
        this.distanciaTotal=distanciaTotal;
        this.vehiculo = vehiculo;
    }
}
