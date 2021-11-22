package pe.edu.pucp.mvc.models;

import lombok.*;
import lombok.experimental.SuperBuilder;
import pe.edu.pucp.utils.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class NodoModel implements Comparable<NodoModel>, Serializable {

    private int idNodo;

    private int coordenadaX;
    private int coordenadaY;
    @Builder.Default
    private double distanciaMinima = Double.MAX_VALUE;
    @Builder.Default
    private double f = Double.MAX_VALUE;
    @Builder.Default
    private double g = 1;
    private double h;
    @Builder.Default
    private boolean estaBloqueado = false;
    private Date inicioBloqueo;
    private Date finBloqueo;

    @Builder.Default
    private NodoModel nodoprevio = null;

    @Builder.Default
    List<BloqueModel> blockList = new ArrayList<>();

    public NodoModel(int coordenadaX, int coordenadaY, boolean estaBloqueado) {
        this.coordenadaX = coordenadaX;
        this.coordenadaY = coordenadaY;
        this.estaBloqueado = estaBloqueado;
    }
    public float getDistancia(NodoModel destino){
        int a,b,c,d;
        float r,r1;
        a = this.coordenadaX;
        b = destino.getCoordenadaX();
        c = this.coordenadaY;
        d = destino.getCoordenadaY();
        r = Math.abs((float) b - a);
        r1 = Math.abs((float) d - c);

        return r + r1;
    }

    @Override
    public int compareTo(NodoModel v) {
        return Double.compare(this.f,v.getF());
    }

    public boolean isBlocked(){
        if(this.inicioBloqueo == null || this.finBloqueo == null)
            return false;
        Date dateNow = UtilidadesFechas.convertToDateViaInstant(LocalDateTime.now());
        return dateNow.compareTo(this.inicioBloqueo) > 0 && dateNow.compareTo(this.finBloqueo) < 0;
    }

    public boolean isBlocked(Date date){
        if(this.blockList.isEmpty())
            return false;

        return blockList.stream().anyMatch(block -> (date.compareTo(block.getInicioBloqueo()) >= 0 && date.compareTo(block.getFinBloqueo()) <= 0));
    }

    @Override
    public boolean equals(Object obj) {
        NodoModel v = (NodoModel) obj;
        return this.coordenadaX == v.getCoordenadaX() && this.coordenadaY == v.getCoordenadaY();
    }

    public NodoModel(NodoModel n){
        this.setCoordenadaX(n.getCoordenadaX());
        this.setCoordenadaY(n.getCoordenadaY());
    }

    @Override
    public String toString(){
        return String.format("[%d,%d]", coordenadaX, coordenadaY);
    }

}
