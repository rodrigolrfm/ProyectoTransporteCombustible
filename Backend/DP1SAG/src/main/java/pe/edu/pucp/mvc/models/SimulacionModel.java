package pe.edu.pucp.mvc.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "simulacion")
public class SimulacionModel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true,nullable = false)
    private int idSimulacion;


    private Double velocidad;

    @Column(nullable = false,length = 45)
    private String tipo;
    private LocalDateTime inicio;
    private LocalDateTime fin;
    private double duracionHoras;

    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name = "idAnalista",nullable = false)
    private AnalistaModel analistaSimulacionModel;

    @OneToMany(mappedBy = "sxPedido", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<SimulacionXPedidoModel> ListSxPedido;
}
