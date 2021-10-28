package pe.edu.pucp.mvc.controllers;

import net.bytebuddy.dynamic.DynamicType;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pe.edu.pucp.mvc.dtos.NodoDTO;
import pe.edu.pucp.mvc.models.NodoModel;
import pe.edu.pucp.mvc.services.NodoService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

@RestController
@RequestMapping("/nodo")
public class NodoController {
    @Autowired
    NodoService nodoService;

    @GetMapping()
    public List<NodoModel> obtenerNodos(){
        return nodoService.obtenerNodos();
    }

    @PostMapping()
    public NodoModel guardarNodo(@RequestBody NodoDTO nodo){

        NodoModel nodoModel = new NodoModel();
        nodoModel.setEstaBloqueado(nodo.getEsta_bloqueado());
        nodoModel.setCoordenadaX(nodo.getCoordenadax());
        nodoModel.setCoordenadaY(nodo.getCoordenaday());

        return this.nodoService.guardarNodo(nodoModel,nodo.getId_mapa());

    }

    @GetMapping(path="/{id}")
    public Optional<NodoModel> obtenerNodoId(@PathVariable("id") Integer id){
        return this.nodoService.obtenerPorId(id);
    }


}
