package pe.edu.pucp.mvc.planificacion;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ControlTarea {
    private String controlCron;
    private String tipoAction;
    private String datos;
}
