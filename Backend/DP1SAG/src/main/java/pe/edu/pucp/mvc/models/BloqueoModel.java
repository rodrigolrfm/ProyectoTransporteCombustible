package pe.edu.pucp.mvc.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.Date;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "bloqueo")
public class BloqueoModel{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int bloqueo;

    private int coordenadaX;

    private int coordenadaY;

    @Builder.Default
    private double distanciaMinima = Double.MAX_VALUE;

    @Builder.Default
    private double f = Double.MAX_VALUE;

    @Builder.Default
    private double g = 1;

    private double h;

    @Builder.Default

    private boolean estaBloqueado = false;

    private Date inicioBloqueo;

    private Date finBloqueo;
}

