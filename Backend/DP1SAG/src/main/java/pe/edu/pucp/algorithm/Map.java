
package pe.edu.pucp.algorithm;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import pe.edu.pucp.mvc.models.NodoModel;
import pe.edu.pucp.mvc.models.PlantaModel;
import pe.edu.pucp.utils.UtilidadesFechas;


@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Map {
    private int dimensionX;
    private int dimensionY;
    private List<PlantaModel> depots;
    private NodoModel[][] map;
    
    public Map(int nColumns, int nRows, ArrayList<PlantaModel> depots) {
        map = new NodoModel[nColumns][nRows];
        this.dimensionX = nColumns;
        this.dimensionY = nRows;
        this.depots = depots;
        for (int i = 0; i < nColumns; i++) {
            for (int j = 0; j < nRows; j++) {
                Date dateNow = UtilidadesFechas.convertToDateViaInstant(LocalDateTime.now());
                NodoModel v = NodoModel.builder().coordenadaX(i).coordenadaY(j)
                        .inicioBloqueo(dateNow).finBloqueo(dateNow).build();
                map[i][j] = v;
            }
        }
        this.depots.forEach(d -> map[d.getCoordenadaX()][d.getCoordenadaY()] = d);
    }
    
    public void setBlockList(List<NodoModel> blockList) {
        blockList.forEach(v -> {
            setBlock(v);
        });
    }
    
    public void setBlock(final NodoModel v) {
        map[v.getCoordenadaX()][v.getCoordenadaY()].setEstaBloqueado(true);
        map[v.getCoordenadaX()][v.getCoordenadaY()].setInicioBloqueo(v.getInicioBloqueo());
        map[v.getCoordenadaX()][v.getCoordenadaY()].setFinBloqueo(v.getFinBloqueo());
        map[v.getCoordenadaX()][v.getCoordenadaY()].setBlockList(v.getBlockList());
    }
    
    public void clearRoute(){
        for(int i=0;i<dimensionX;i++)
            for(int j=0;j<dimensionY;j++)
                map[i][j].setNodoprevio(null);
    }
    
    public void deleteBlocks(){
        for(int i=0;i<dimensionX;i++){
            for(int j=0;j<dimensionY;j++){
                NodoModel v = map[i][j];
                v.setEstaBloqueado(false);
                if(!v.getBlockList().isEmpty()){
                    v.setBlockList(new ArrayList<>());
                }
            }
        }
    }
    
    public void printMap(){
        for (int i = 0; i < this.dimensionX; i++) {
            for (int j = 0; j < this.dimensionY; j++) {
                if(map[i][j].isBlocked())
                    System.out.print("B ");
                else if(map[i][j] instanceof PlantaModel)
                    System.out.print("D ");
                else
                    System.out.print("  ");
            }
            System.out.println("");
        }
    }
}
