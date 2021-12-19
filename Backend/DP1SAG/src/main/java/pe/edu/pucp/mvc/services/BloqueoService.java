package pe.edu.pucp.mvc.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.pucp.mvc.models.BloqueoModel;
import pe.edu.pucp.mvc.models.CoordenadaModel;
import pe.edu.pucp.mvc.models.NodoModel;
import pe.edu.pucp.mvc.models.ReporteBloqueo3Dias;
import pe.edu.pucp.mvc.repositories.BloqueoRepository;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class BloqueoService {
    @Autowired
    BloqueoRepository bloqueoRepository;

    private static int iniHora = 0;
    private static int finHora = 12;
    private static int cantRepeticionesxDia = 0;

    private static int iniHoraJ = 0;
    private static int finHoraJ = 12;
    private static int cantRepeticionesxDiaJ = 0;

    public BloqueoModel guardarBloqueo(BloqueoModel bloqueo) {
        return bloqueoRepository.save(bloqueo);
    }

    public List<NodoModel> listaBloqueosDiaDia(){
        List<NodoModel> listaNodos = new ArrayList<>();
        List<BloqueoModel> listaBloqueos  = bloqueoRepository.findBloqueosNodos();
        listaBloqueos.forEach(bloqueo -> listaNodos.add(new NodoModel(bloqueo)));
        return listaNodos;
    }

    public List<BloqueoModel> getBloqueos(){
        List<BloqueoModel> bloqueos;
        bloqueos = bloqueoRepository.getBloqueos();
        return bloqueos;
    }

    public List<BloqueoModel> getBloqueosFechas(String fechaIni, String fechaFin) {
        List<BloqueoModel> bloqueos;
        bloqueos = bloqueoRepository.getBloqueosFechas(fechaIni, fechaFin);
        return bloqueos;
    }

    public List<ReporteBloqueo3Dias> getBloqueosJson3dias(Calendar fechaIni, Calendar fechaFin, int dia) throws ParseException{
        List<BloqueoModel> bloqueos;
        Timestamp ini = new Timestamp(fechaIni.getTime().getTime());
        Timestamp tofecha = new Timestamp(fechaFin.getTime().getTime());

        bloqueos = bloqueoRepository.getBloqueosFechasIntervarlo3dias(ini, tofecha, dia);
        List<BloqueoModel> newLista = new ArrayList<>();
        List<ReporteBloqueo3Dias> jsonBloqueos3dias = new ArrayList<>();

        Calendar fromdata = Calendar.getInstance();
        Calendar todata = Calendar.getInstance();

        if(!(cantRepeticionesxDiaJ==2)){
            fromdata.set(fechaIni.get(Calendar.YEAR),fechaIni.get(Calendar.MONTH),dia,iniHoraJ,0,0);
            todata.set(fechaIni.get(Calendar.YEAR),fechaIni.get(Calendar.MONTH),dia,finHoraJ,0,0);
            for (BloqueoModel bloqueo:bloqueos) {
                Calendar bloqueoDateInicio = Calendar.getInstance();
                bloqueoDateInicio.setTime(bloqueo.getInicioBloqueo());

                if ((bloqueoDateInicio.compareTo(fromdata)>=0) && ((bloqueoDateInicio.compareTo(todata)<=0))){
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
                    sdf.setTimeZone(TimeZone.getTimeZone("America/Bogota"));
                    String inicio = sdf.format(bloqueo.getInicioBloqueo().getTime());
                    String fin =  sdf.format(bloqueo.getFinBloqueo().getTime());
                    jsonBloqueos3dias.add(ReporteBloqueo3Dias.builder().startTime(inicio)
                            .endTime(fin).bloqueo(CoordenadaModel.builder().x(bloqueo.getCoordenadaX())
                                    .y(bloqueo.getCoordenadaY()).build()).build());
                }
            }
            iniHoraJ+=12;
            finHoraJ+=12;
            cantRepeticionesxDiaJ+=1;
        }else{
            iniHoraJ = 0;
            finHoraJ = 12;
            cantRepeticionesxDiaJ = 1;
            fromdata.set(fechaIni.get(Calendar.YEAR),fechaIni.get(Calendar.MONTH),dia,iniHoraJ,0,0);
            todata.set(fechaIni.get(Calendar.YEAR),fechaIni.get(Calendar.MONTH),dia,finHoraJ,0,0);
            for (BloqueoModel bloqueo:bloqueos) {
                Calendar bloqueoDateInicio = Calendar.getInstance();
                bloqueoDateInicio.setTime(bloqueo.getInicioBloqueo());
                if ((bloqueoDateInicio.compareTo(fromdata)>=0) && ((bloqueoDateInicio.compareTo(todata)<=0))){
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
                    sdf.setTimeZone(TimeZone.getTimeZone("America/Bogota"));
                    String inicio = sdf.format(bloqueo.getInicioBloqueo().getTime());
                    String fin =  sdf.format(bloqueo.getFinBloqueo().getTime());
                    jsonBloqueos3dias.add(ReporteBloqueo3Dias.builder().startTime(inicio)
                            .endTime(fin).bloqueo(CoordenadaModel.builder().x(bloqueo.getCoordenadaX())
                                    .y(bloqueo.getCoordenadaY()).build()).build());
                }
            }
        }

        return jsonBloqueos3dias;

    }

    public List<NodoModel> getBloqueosFechas3dias(Calendar fechaIni, Calendar fechaFin, int dia) throws ParseException {

        List<BloqueoModel> bloqueos;
        Timestamp ini = new Timestamp(fechaIni.getTime().getTime());
        Timestamp tofecha = new Timestamp(fechaFin.getTime().getTime());

        bloqueos = bloqueoRepository.getBloqueosFechasIntervarlo3dias(ini, tofecha, dia);
        List<BloqueoModel> newLista = new ArrayList<>();
        List<NodoModel> newListaNodos = new ArrayList<>();
        //Calendar fechaIniCal = Calendar.getInstance();
        //fechaIniCal.setTime(fechaInicial);

        Calendar fromdata = Calendar.getInstance();
        Calendar todata = Calendar.getInstance();


        //Calendar inicio = Calendar.getInstance();
        //inicio.setTime(fechaInicial);

        if(!(cantRepeticionesxDia==2)){
            fromdata.set(fechaIni.get(Calendar.YEAR),fechaIni.get(Calendar.MONTH),dia,iniHora,0,0);
            todata.set(fechaIni.get(Calendar.YEAR),fechaIni.get(Calendar.MONTH),dia,finHora,0,0);
            for (BloqueoModel bloqueo:bloqueos) {
                Calendar bloqueoDateInicio = Calendar.getInstance();
                bloqueoDateInicio.setTime(bloqueo.getInicioBloqueo());
                //if ((bloqueoDateInicio.compareTo(fromdata)>=0) && ((bloqueoDateInicio.compareTo(todata)<=0))){
                //    newLista.add(bloqueo);
                //}
                if ((bloqueoDateInicio.compareTo(fromdata)>=0) && ((bloqueoDateInicio.compareTo(todata)<=0))){
                    newLista.add(bloqueo);
                }
            }
            iniHora+=12;
            finHora+=12;
            cantRepeticionesxDia+=1;
        }else{
            iniHora = 0;
            finHora = 12;
            cantRepeticionesxDia = 1;
            fromdata.set(fechaIni.get(Calendar.YEAR),fechaIni.get(Calendar.MONTH),dia,iniHora,0,0);
            todata.set(fechaIni.get(Calendar.YEAR),fechaIni.get(Calendar.MONTH),dia,finHora,0,0);
            for (BloqueoModel bloqueo:bloqueos) {
                Calendar bloqueoDateInicio = Calendar.getInstance();
                bloqueoDateInicio.setTime(bloqueo.getInicioBloqueo());
                if ((bloqueoDateInicio.compareTo(fromdata)>=0) && ((bloqueoDateInicio.compareTo(todata)<=0))){
                    newLista.add(bloqueo);
                }
            }
        }
        newLista.forEach(bloqueo ->newListaNodos.add(new NodoModel(bloqueo)));

        return newListaNodos;
    }


    public List<BloqueoModel> getBloqueos3dias() {
        List<BloqueoModel> bloqueos;
        bloqueos = bloqueoRepository.getBloqueos3dias();
        return bloqueos;
    }
}
