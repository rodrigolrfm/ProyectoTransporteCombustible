
package pe.edu.pucp.algorithm;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import pe.edu.pucp.mvc.models.Depot;
import pe.edu.pucp.utils.UtilidadesFechas;


@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Map {
    private int dimensionX;
    private int dimensionY;
    private List<Depot> depots;
    private Node[][] map;
    
    public Map(int nColumns, int nRows, ArrayList<Depot> depots) {
        map = new Node[nColumns][nRows];
        this.dimensionX = nColumns;
        this.dimensionY = nRows;
        this.depots = depots;
        for (int i = 0; i < nColumns; i++) {
            for (int j = 0; j < nRows; j++) {
                Date dateNow = UtilidadesFechas.convertToDateViaInstant(LocalDateTime.now());
                Node v = Node.builder().coordX(i).coordY(j)
                        .initDateBlocked(dateNow).endDateBlocked(dateNow).build();
                map[i][j] = v;
            }
        }
        this.depots.forEach(d -> map[d.getCoordX()][d.getCoordY()] = d);
    }
    
    public void setBlockList(List<Node> blockList) {
        blockList.forEach(v -> {
            setBlock(v);
        });
    }
    
    public void setBlock(final Node v) {
        map[v.getCoordX()][v.getCoordY()].setBlocked(true);
        map[v.getCoordX()][v.getCoordY()].setInitDateBlocked(v.getInitDateBlocked());
        map[v.getCoordX()][v.getCoordY()].setEndDateBlocked(v.getEndDateBlocked());
        map[v.getCoordX()][v.getCoordY()].setBlockList(v.getBlockList());
    }
    
    public void clearRoute(){
        for(int i=0;i<dimensionX;i++)
            for(int j=0;j<dimensionY;j++)
                map[i][j].setPreviousVertex(null);
    }
    
    public void deleteBlocks(){
        for(int i=0;i<dimensionX;i++){
            for(int j=0;j<dimensionY;j++){
                Node v = map[i][j];
                v.setBlocked(false);
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
                else if(map[i][j] instanceof Depot)
                    System.out.print("D ");
                else
                    System.out.print("  ");
            }
            System.out.println("");
        }
    }
}
