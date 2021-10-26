package pe.edu.pucp.mvc.models;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "planta")
public class PlantaModel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true,nullable = false)
    private int idPlanta;


    @OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name = "idNodo",nullable = false)
    private NodoModel nodo;


    private int esPrincipal;

}
