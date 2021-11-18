package pe.edu.pucp.mvc.models;

import lombok.*;

import java.util.List;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class EntidadRuta {
    private List<PositionModel> path;
    private String startTime;
    private String endTime;
}
