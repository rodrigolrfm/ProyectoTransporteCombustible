package pe.edu.pucp.mvc.models;

import lombok.*;

import java.io.Serializable;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PositionModel implements Serializable {
    private int x;
    private int y;
    private int destino;

}
