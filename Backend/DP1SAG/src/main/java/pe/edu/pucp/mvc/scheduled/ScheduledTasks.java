package pe.edu.pucp.mvc.scheduled;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import pe.edu.pucp.mvc.controllers.MapaController;
import pe.edu.pucp.mvc.models.MapaModel;
import pe.edu.pucp.mvc.services.MapaService;

@Component
public class ScheduledTasks {

    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    public static SseEmitter emi;
    @Autowired
    MapaService mapaService;

    @Scheduled(fixedRate = 10000)
    public void reportCurrentTime() throws IOException {
        ArrayList<MapaModel> mapas = null;
        if (emi!=null){
            mapas = new ArrayList<MapaModel>();
            mapas = mapaService.obtenerMapas();
            emi.send(SseEmitter.event().name("MAPAS").data(mapas));
        }
    }
}