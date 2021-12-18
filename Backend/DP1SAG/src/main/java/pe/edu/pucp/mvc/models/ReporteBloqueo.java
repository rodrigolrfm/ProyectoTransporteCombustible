package pe.edu.pucp.mvc.models;

import lombok.*;

import java.util.Date;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReporteBloqueo {
    private int coordenadaX;
    private int coordenadaY;
    private String inicioBloqueo;
    private String finBloqueo;
}
