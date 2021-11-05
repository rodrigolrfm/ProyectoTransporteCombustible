package pe.edu.pucp.mvc.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pe.edu.pucp.mvc.models.MapaModel;
import pe.edu.pucp.mvc.services.MapaService;

import java.util.ArrayList;
import java.util.Optional;

@RestController
@RequestMapping("/mapa")
public class MapaController {
    @Autowired
    MapaService mapaService;

    @GetMapping()
    public ArrayList<MapaModel> obtenerMapas(){
        return mapaService.obtenerMapas();
    }

    @PostMapping()
    public MapaModel guardarMapa(@RequestBody MapaModel mapa){
        return this.mapaService.guardarMapa(mapa);
    }

    @GetMapping(path="/{id}")
    public Optional<MapaModel> obtenerMapaId(@PathVariable("id") Integer id){
        return this.mapaService.obtenerPorId(id);
    }

    @DeleteMapping(path = "/{id}")
    public String eliminarXID(@PathVariable("id") Integer id){
        boolean ok = this.mapaService.eliminarMapa(id);
        if (ok){
            return "Se eliminó el usuario con id: " + id;
        }else{
            return "No pudo eliminarse el usuario con id: " + id;
        }
    }

}
