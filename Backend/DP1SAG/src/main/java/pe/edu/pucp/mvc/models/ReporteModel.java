package pe.edu.pucp.mvc.models;

import lombok.*;

import java.io.Serializable;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReporteModel implements Serializable {
    @Builder.Default
    private Integer nPedidosEntregados = 0;
    @Builder.Default
    private Integer nPedidosSinEntregar = 0;
    @Builder.Default
    private Double glpEntregado = 0.0;
}
