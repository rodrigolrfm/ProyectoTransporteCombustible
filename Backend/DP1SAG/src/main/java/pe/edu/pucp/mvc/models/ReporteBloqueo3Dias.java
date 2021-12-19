package pe.edu.pucp.mvc.models;

import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReporteBloqueo3Dias {
    private CoordenadaModel bloqueo;
    private String startTime;
    private String endTime;
}
