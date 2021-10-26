package pe.edu.pucp.mvc.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;

@Setter
@Getter
@Entity
@Table(name = "conductor")
public class ConductorModel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true,nullable = false)
    private int idConductor;

    @OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name = "idUsuario",nullable = false)
    private UsuarioModel conductor;

    public ConductorModel(){

    }

    @OneToOne(mappedBy = "conductor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private VehiculoModel vehiculo;

}
