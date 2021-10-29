package pe.edu.pucp.mvc.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NodoDTO {

    int coordenadax;
    int coordenaday;
    int esta_bloqueado;
    int id_mapa;
}