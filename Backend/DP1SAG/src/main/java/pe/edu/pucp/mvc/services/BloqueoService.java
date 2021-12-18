package pe.edu.pucp.mvc.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.pucp.mvc.models.BloqueoModel;
import pe.edu.pucp.mvc.models.EntidadVehiculo;
import pe.edu.pucp.mvc.models.NodoModel;
import pe.edu.pucp.mvc.models.PedidoModel;
import pe.edu.pucp.mvc.repositories.BloqueoRepository;
import pe.edu.pucp.mvc.repositories.VehiculoRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class BloqueoService {
    @Autowired
    BloqueoRepository bloqueoRepository;

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
}
