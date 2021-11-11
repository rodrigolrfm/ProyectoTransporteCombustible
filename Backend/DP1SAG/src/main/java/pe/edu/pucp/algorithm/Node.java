/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.edu.pucp.algorithm;

import java.time.LocalDateTime;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.*;
import lombok.experimental.SuperBuilder;
import pe.edu.pucp.mvc.models.Block;
import pe.edu.pucp.utils.UtilidadesFechas;


@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Node implements Comparable<Node>{
    private int coordX;
    private int coordY;
    private Node previousVertex = null;
    @Builder.Default
    private double minDistance = Double.MAX_VALUE;
    @Builder.Default
    private double f = Double.MAX_VALUE;
    @Builder.Default
    private double g = 1;
    private double h;
    @Builder.Default
    private boolean blocked = false;
    private Date initDateBlocked;
    private Date endDateBlocked;
    @Builder.Default
    List<Block> blockList = new ArrayList<>();
    
    public float getDistancia(Node destino){
        int a,b,c,d;
        float r,r1;
        a = this.getCoordX();
        b = destino.getCoordX();
        c = this.getCoordY();
        d = destino.getCoordY();
        r = Math.abs((float) b - a);
        r1 = Math.abs((float) d - c);

        return r + r1; 
    }
    
    @Override
    public int compareTo(Node v) {
        return Double.compare(this.f,v.getF());
    }
    
    public boolean isBlocked(){
        if(this.initDateBlocked == null || this.endDateBlocked == null)
            return false;
        Date dateNow = UtilidadesFechas.convertToDateViaInstant(LocalDateTime.now());
        return dateNow.compareTo(this.initDateBlocked) > 0 && dateNow.compareTo(this.endDateBlocked) < 0;
    }

    public boolean isBlocked(Date date){
        if(this.blockList.isEmpty())
            return false;
               
        return blockList.stream().anyMatch(block -> (date.compareTo(block.getInitDateBlocked()) >= 0 && date.compareTo(block.getEndDateBlocked()) <= 0));
    }

    @Override
    public boolean equals(Object obj) {
        Node v = (Node) obj;
        return this.coordX == v.getCoordX() && this.coordY == v.getCoordY();
    }
    
    @Override
    public String toString(){
        return String.format("[%d,%d]", coordX, coordY);
    }
    
    public Node(Node r){
        this.coordX = r.getCoordX();
        this.coordY = r.getCoordY();
    }
}
