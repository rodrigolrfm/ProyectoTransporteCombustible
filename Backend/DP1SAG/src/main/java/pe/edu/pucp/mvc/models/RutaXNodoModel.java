package pe.edu.pucp.mvc.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;

@Setter
@Getter
@Entity
@Table(name="rutaxnodo")
public class RutaXNodoModel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true,nullable = false)
    private int idRutaxNodo;

    @ManyToOne
    @JoinColumn(name = "idRuta",nullable = false)
    private RutaModel ruta;

}
