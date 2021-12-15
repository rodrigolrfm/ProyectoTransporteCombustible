
package pe.edu.pucp.mvc.controllers;

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
public class MapaModel {
    private int dimensionX;
    private int dimensionY;
    private List<PlantaModel> plantas;
    private NodoModel[][] mapa;
    
    public MapaModel(int nColumns, int nRows, ArrayList<PlantaModel> depots) {
        mapa = new NodoModel[nColumns][nRows];
        this.dimensionX = nColumns;
        this.dimensionY = nRows;
        this.plantas = depots;
        for (int i = 0; i < nColumns; i++) {
            for (int j = 0; j < nRows; j++) {
                Date dateNow = UtilidadesFechas.convertToDateViaInstant(LocalDateTime.now());
                NodoModel v = NodoModel.builder().coordenadaX(i).coordenadaY(j)
                        .inicioBloqueo(dateNow).finBloqueo(dateNow).build();
                mapa[i][j] = v;
            }
        }
        this.plantas.forEach(d -> mapa[d.getCoordenadaX()][d.getCoordenadaY()] = d);
    }

    
    public void setBlockList(List<NodoModel> blockList) {
        blockList.forEach(v -> {
            setBlock(v);
        });
    }
    
    public void setBlock(final NodoModel v) {
        mapa[v.getCoordenadaX()][v.getCoordenadaY()].setEstaBloqueado(true);
        mapa[v.getCoordenadaX()][v.getCoordenadaY()].setInicioBloqueo(v.getInicioBloqueo());
        mapa[v.getCoordenadaX()][v.getCoordenadaY()].setFinBloqueo(v.getFinBloqueo());
        mapa[v.getCoordenadaX()][v.getCoordenadaY()].setBlockList(v.getBlockList());
    }
    
    public void clearRoute(){
        for(int i=0;i<dimensionX;i++)
            for(int j=0;j<dimensionY;j++)
                mapa[i][j].setNodoprevio(null);
    }
    
    public void deleteBlocks(){
        for(int i=0;i<dimensionX;i++){
            for(int j=0;j<dimensionY;j++){
                NodoModel v = mapa[i][j];
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
                if(mapa[i][j].isBlocked())
                    System.out.print("B ");
                else if(mapa[i][j] instanceof PlantaModel)
                    System.out.print("D ");
                else
                    System.out.print("  ");
            }
            System.out.println("");
        }
    }

    public ArrayList<PlantaModel> obtenerPlantarIntermedias(){
        ArrayList<PlantaModel> plantas = new ArrayList<>();
        plantas.add(PlantaModel.builder().coordenadaX(12).coordenadaY(8).esPrincipal(true).build());
        plantas.add(PlantaModel.builder().coordenadaX(42).coordenadaY(42).build());
        plantas.add(PlantaModel.builder().coordenadaX(63).coordenadaY(3).build());
        return plantas;
    }

}
