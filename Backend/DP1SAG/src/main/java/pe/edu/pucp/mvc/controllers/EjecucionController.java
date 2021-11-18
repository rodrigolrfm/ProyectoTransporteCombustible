package pe.edu.pucp.mvc.controllers;


import javafx.util.Pair;
import org.apache.logging.log4j.message.MapMessage;
import org.json.*;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import pe.edu.pucp.algorithm.GeneticAlgorithm;
import pe.edu.pucp.algorithm.Knapsack;
import pe.edu.pucp.mvc.models.EntidadVehiculo;
import pe.edu.pucp.mvc.models.NodoModel;
import pe.edu.pucp.mvc.models.PedidoModel;
import pe.edu.pucp.mvc.models.PlantaModel;
import pe.edu.pucp.mvc.planificacion.ScheduledTasks;
import pe.edu.pucp.utils.Lectura;
import pe.edu.pucp.utils.LecturaBloques;
import pe.edu.pucp.utils.LecturaVehiculo;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

@RestController
@RequestMapping("/ejecutar")
public class EjecucionController {

    public List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    @CrossOrigin
    @GetMapping(value = "/obtenerRutas")
    public SseEmitter devolverRutas(){
        SseEmitter sseEmitter = new SseEmitter();
        ScheduledTasks.emi = sseEmitter;
        emitters.add(sseEmitter);
        return sseEmitter;
    }


}
