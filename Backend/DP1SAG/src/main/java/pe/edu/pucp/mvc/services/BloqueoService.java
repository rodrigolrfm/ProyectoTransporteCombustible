package pe.edu.pucp.mvc.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.pucp.mvc.models.BloqueoModel;
import pe.edu.pucp.mvc.models.EntidadVehiculo;
import pe.edu.pucp.mvc.models.NodoModel;
import pe.edu.pucp.mvc.models.PedidoModel;
import pe.edu.pucp.mvc.repositories.BloqueoRepository;
import pe.edu.pucp.mvc.repositories.VehiculoRepository;

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

    public List<BloqueoModel> getBloqueosFechas3dias(Calendar fechaIni, Calendar fechaFin, int dia) throws ParseException {

        List<BloqueoModel> bloqueos;
        Timestamp ini = new Timestamp(fechaIni.getTime().getTime());
        Timestamp tofecha = new Timestamp(fechaFin.getTime().getTime());

        bloqueos = bloqueoRepository.getBloqueosFechasIntervarlo3dias(ini, tofecha, dia);
        List<BloqueoModel> newLista = new ArrayList<>();
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


        return newLista;
    }


    public List<BloqueoModel> getBloqueos3dias() {
        List<BloqueoModel> bloqueos;
        bloqueos = bloqueoRepository.getBloqueos3dias();
        return bloqueos;
    }
}
