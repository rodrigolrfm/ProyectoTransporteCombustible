package pe.edu.pucp.mvc.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pe.edu.pucp.mvc.models.MapaModel;
import pe.edu.pucp.mvc.models.NodoModel;
import pe.edu.pucp.mvc.services.MapaService;
import pe.edu.pucp.mvc.services.NodoService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    public NodoModel guardarNodo(@RequestBody NodoModel nodo){
        return this.nodoService.guardarNodo(nodo);
    }

    @GetMapping(path="/{id}")
    public Optional<NodoModel> obtenerMapaId(@PathVariable("id") Integer id){
        return this.nodoService.obtenerPorId(id);
    }

}
