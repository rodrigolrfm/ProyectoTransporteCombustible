package pe.edu.pucp.mvc.dtos;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import pe.edu.pucp.mvc.models.NodoModel;

import java.util.ArrayList;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PedidosRutasDTO{
    ArrayList<NodoModel> listaNodos;
    DateTimeFormat.ISO tiempo;
}
