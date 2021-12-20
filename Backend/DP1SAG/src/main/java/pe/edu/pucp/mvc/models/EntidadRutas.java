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

    @Override
    public String toString(){
        int nRuta = 1;
        StringBuilder output = new StringBuilder();
        for(EntidadRuta ruta : this.paths){
            output.append("ruta: " + (nRuta++) + "\n");
            for(PositionModel pos : ruta.getPath()){
                output.append("x: " +  pos.getX() + " y: "+pos.getY() + "\n");
            }
            output.append("Inicio " + ruta.getStartTime());
            output.append("Fin " + ruta.getEndTime());
        }
        return output.toString();
    }
}
