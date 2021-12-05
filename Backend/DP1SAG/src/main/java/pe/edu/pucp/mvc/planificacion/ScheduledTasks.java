package pe.edu.pucp.mvc.planificacion;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import pe.edu.pucp.mvc.controllers.EjecucionController;
import pe.edu.pucp.mvc.metodos.EjecucionAlgoritmo;
import pe.edu.pucp.mvc.models.EntidadRutas;

@Component
public class ScheduledTasks {

    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    public static SseEmitter emi = null;
    EntidadRutas rutasFinal = EntidadRutas.builder().paths(new ArrayList<>()).build();
    //@Scheduled(fixedRate = 45000)
    @Scheduled(cron="40 * * * * *")
    public void reportCurrentTime() throws Exception {
        //JSONObject json = new JSONObject();
        rutasFinal = EjecucionAlgoritmo.ejecutarAlgoritmoColapso();
        if (emi!=null){
            emi.send(SseEmitter.event().name("RUTAS").data(rutasFinal));
        }
    }

}
