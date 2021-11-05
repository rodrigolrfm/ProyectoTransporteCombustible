package pe.edu.pucp.mvc.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "usuario")
public class UsuarioModel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true,nullable = false)
    private int idUsuario;

    @Column(nullable = false, length = 150)
    private String nombres;

    @Column(nullable = false, length = 100)
    private String apellidoPaterno;

    @Column(nullable = false, length = 100)
    private String apellidoMaterno;

    @Column(nullable = false, length = 8)
    private String DNI;

    @Column(nullable = false, length = 45)
    private String correo;

    public UsuarioModel(){
    }

    public UsuarioModel(String nombres, String apellidoPaterno, String apellidoMaterno, String DNI, String correo) {
        this.nombres = nombres;
        this.apellidoPaterno = apellidoPaterno;
        this.apellidoMaterno = apellidoMaterno;
        this.DNI = DNI;
        this.correo = correo;
    }

}
