package pe.edu.pucp.mvc.planificacion;


import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.ScheduledFuture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;


@Service
public class PlanificadorTareasServicios{
    @Autowired
    private TaskScheduler taskScheduler;

    Map<String, ScheduledFuture<?>> jobsMap = new HashMap<>();

    /**
     * metodo que crea una tarea programada y la agrega a la lista de tareas
     * programadas
     * @param jobId
     * @param tasklet
     * @param cronExpression
     */
    public void planificarTareas(String jobId, Runnable tasklet, String cronExpression) {
        System.out.println("Scheduling task with job id: " + jobId + " and cron expression: " + cronExpression);
        ScheduledFuture<?> scheduledTask = taskScheduler.schedule(tasklet, new CronTrigger(cronExpression, TimeZone.getTimeZone(TimeZone.getDefault().getID())));
        jobsMap.put(jobId, scheduledTask);
    }

    /**
     * metodo que cancela la ejecucion de una tarea programada de la lista de
     * tareas programadas
     * @param jobId
     */
    public void eliminarPlanificadorTareas(String jobId) {
        ScheduledFuture<?> scheduledTask = jobsMap.get(jobId);
        if(scheduledTask != null) {
            scheduledTask.cancel(true);
            jobsMap.put(jobId, null);
            System.out.println("Trabajo " + jobId + " detenido");
        }
    }
}
