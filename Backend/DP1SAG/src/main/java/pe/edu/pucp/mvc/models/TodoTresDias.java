package pe.edu.pucp.mvc.models;

import lombok.*;

import java.util.List;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TodoTresDias {
    List<ReporteBloqueo3Dias> bloqueoTresDias;
    EntidadRutas arregloRutas;
}
