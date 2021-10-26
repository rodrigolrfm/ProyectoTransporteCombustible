package pe.edu.pucp.mvc.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.pucp.mvc.models.MapaModel;
import pe.edu.pucp.mvc.repositories.MapaRepository;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class MapaService {
    @Autowired
    MapaRepository mapaRepository;

    public ArrayList<MapaModel> obtenerMapas(){
        return (ArrayList<MapaModel>) mapaRepository.findAll();
    }

    public MapaModel guardarMapa(MapaModel mapa){
        return mapaRepository.save(mapa);
    }

    public Optional<MapaModel> obtenerPorId(Integer id){
        return mapaRepository.findById(id);
    }

}
