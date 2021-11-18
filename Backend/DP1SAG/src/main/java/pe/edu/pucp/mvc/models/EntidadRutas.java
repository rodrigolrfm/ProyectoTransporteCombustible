package pe.edu.pucp.mvc.models;

import lombok.*;

import java.util.List;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EntidadRutas {
    private List<EntidadRuta> paths;

    public void agregarRuta(EntidadRuta rutaVehiculo) {
        this.paths.add(rutaVehiculo);
    }
}
