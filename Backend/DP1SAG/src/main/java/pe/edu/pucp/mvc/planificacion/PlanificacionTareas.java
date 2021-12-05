package pe.edu.pucp.mvc.planificacion;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;


@Service
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlanificacionTareas implements Runnable{

    private String uuid;
    private PlanificadorTareasServicios planificadorTareasServicios;
    private ControlTarea controlTarea;
    private SseEmitter emitter;
    @Builder.Default
    private int counter = 0;

    @Override
    public void run() {
        // aqui deberia ir el algoritmo y un emitter
        System.out.println("Running action: " + controlTarea.getTipoAction());
        System.out.println("With Data: " + controlTarea.getDatos());

        try{


            emitter.send(
                    SseEmitter.event()
                            .name("3dias")
                            .data(String.format("Este es el %d", counter))
            );

            counter++;
            if(counter==3){
                emitter.send(SseEmitter.event().name("STOP").data("Jorge es cabro"));
                emitter.complete();
                planificadorTareasServicios.eliminarPlanificadorTareas(uuid);
            }

        }
        catch(IOException e){
            emitter.completeWithError(e);
            planificadorTareasServicios.eliminarPlanificadorTareas(uuid);
        }
        //counter++;
    }
}
