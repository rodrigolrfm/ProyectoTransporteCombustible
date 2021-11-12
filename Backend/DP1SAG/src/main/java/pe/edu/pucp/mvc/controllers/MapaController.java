package pe.edu.pucp.mvc.controllers;


import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import pe.edu.pucp.mvc.models.MapaModel;
import pe.edu.pucp.mvc.scheduled.ScheduledTasks;
import pe.edu.pucp.mvc.services.MapaService;
import org.slf4j.Logger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

@RestController
@RequestMapping("/mapa")
public class MapaController {
    @Autowired
    MapaService mapaService;
    public List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    @CrossOrigin
    @GetMapping(value = "/obtenerMapas")
    public SseEmitter devolverMapas(){
        SseEmitter sseEmitter = new SseEmitter();
        ScheduledTasks.emi = sseEmitter;
        emitters.add(sseEmitter);
        return sseEmitter;
    }


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
