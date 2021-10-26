package pe.edu.pucp.mvc.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "cliente")
public class ClienteModel implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true,nullable = false)
    private int idCliente;

    @Column(nullable = false,length = 100)
    private String nombres;

    @Column(nullable = false,length = 100)
    private String apellidos;


    @OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name = "idNodo",nullable = false)
    private NodoModel lugarNodo;

    @OneToMany(mappedBy = "clienteModel", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PedidoModel> clienteModel;

}
