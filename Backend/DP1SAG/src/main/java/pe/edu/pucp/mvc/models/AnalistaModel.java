package pe.edu.pucp.mvc.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "analista")
public class AnalistaModel implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true,nullable = false)
    private int idAnalista;

    @OneToOne
    @JoinColumn(name = "idUsuario",nullable = false)
    private UsuarioModel usuarioAnalista;

    public AnalistaModel(){
    }

    public int getIdAnalista() {
        return idAnalista;
    }

    public void setIdAnalista(int idAnalista) {
        this.idAnalista = idAnalista;
    }

    public UsuarioModel getUsuarioAnalista() {
        return usuarioAnalista;
    }

    public void setUsuarioAnalista(UsuarioModel usuarioAnalista) {
        this.usuarioAnalista = usuarioAnalista;
    }

}
