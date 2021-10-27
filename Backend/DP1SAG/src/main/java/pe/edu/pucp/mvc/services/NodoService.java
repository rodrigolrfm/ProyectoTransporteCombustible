package pe.edu.pucp.mvc.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.pucp.mvc.models.MapaModel;
import pe.edu.pucp.mvc.models.NodoModel;
import pe.edu.pucp.mvc.repositories.NodoRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class NodoService {
    @Autowired
    NodoRepository nodoRepository;

    public ArrayList<NodoModel> obtenerNodos(){
        return (ArrayList<NodoModel>) nodoRepository.findAll();
    }

    public NodoModel guardarNodo(NodoModel nodo){
        return nodoRepository.save(nodo);
    }

    public Optional<NodoModel> obtenerPorId(Integer id){
        return nodoRepository.findById(id);
    }

}
