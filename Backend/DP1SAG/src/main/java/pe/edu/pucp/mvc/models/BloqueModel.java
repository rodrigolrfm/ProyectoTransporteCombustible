package pe.edu.pucp.mvc.models;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "bloqueo")
public class BloqueModel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private int idBloqueo;


    @ManyToOne
    @JoinColumn(name = "idNodo", nullable = false)
    private NodoModel idNodoBloqueo;

    private Date inicioBloqueo;

    private Date finBloqueo;
}
