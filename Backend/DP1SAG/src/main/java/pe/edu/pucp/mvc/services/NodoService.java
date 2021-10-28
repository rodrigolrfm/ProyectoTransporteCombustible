package pe.edu.pucp.mvc.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.pucp.mvc.models.MapaModel;
import pe.edu.pucp.mvc.models.NodoModel;
import pe.edu.pucp.mvc.repositories.MapaRepository;
import pe.edu.pucp.mvc.repositories.NodoRepository;

import java.util.List;
import java.util.Optional;

@Service
public class NodoService {
    @Autowired
    NodoRepository nodoRepository;

<<<<<<< HEAD
    @Autowired
    MapaRepository mapaRepository;


    public List<NodoModel> obtenerNodos(){
        return (List<NodoModel>) nodoRepository.findAll();
=======
    public ArrayList<NodoModel> obtenerNodos(){
        return (ArrayList<NodoModel>) nodoRepository.findAll();
>>>>>>> 96f9da6b49f42dfeba77420d9f99013300fc9a71
    }

    public NodoModel guardarNodo(NodoModel nodo,int id){
        Optional<MapaModel> mapa = mapaRepository.findById(id);
        nodo.setMapaModel(mapa.get());
        return nodoRepository.save(nodo);
    }

    public Optional<NodoModel> obtenerPorId(Integer id){
        return nodoRepository.findById(id);
    }

}
