package pe.edu.pucp.mvc.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Calendar;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PedidoUniqueDTO implements Serializable {
    int coordenadaX;
    int coordenadaY;

    @JsonProperty("fechaPedido")
    @JsonFormat(pattern="yyyy/MM/dd HH:mm")
    Calendar fechaPedido;

    @JsonProperty("horasLimite")
    @JsonFormat(pattern="yyyy/MM/dd HH:mm")
    Calendar horasLimite;
    double cantidadGLP;
}
