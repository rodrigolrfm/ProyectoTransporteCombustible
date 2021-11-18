package pe.edu.pucp.mvc.models;

import lombok.*;

import java.util.List;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class EntidadRuta implements Comparable<EntidadRuta>{
    private List<PositionModel> path;
    private String startTime;
    private String endTime;

    @Override
    public int compareTo(EntidadRuta ruta) {
        return this.getStartTime().compareTo(ruta.getStartTime());
    }
}
