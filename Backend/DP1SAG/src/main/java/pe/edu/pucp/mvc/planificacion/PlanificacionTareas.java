package pe.edu.pucp.mvc.planificacion;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

import javafx.util.Pair;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import pe.edu.pucp.algorithm.GeneticAlgorithm;
import pe.edu.pucp.algorithm.Knapsack;
import pe.edu.pucp.mvc.controllers.MapaModel;
import pe.edu.pucp.mvc.models.*;
import pe.edu.pucp.mvc.services.BloqueoService;
import pe.edu.pucp.mvc.services.PedidoService;
import pe.edu.pucp.mvc.services.VehiculoService;

import javax.annotation.PostConstruct;


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


    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    /*
    private List<PedidoModel> listaPedidos = new ArrayList<>();
    public List<VehiculoModel> vehiculoModels = new ArrayList<>();
    private List<EntidadVehiculo> listaVehiculos = new ArrayList<>();
    private List<NodoModel> blockList = new ArrayList<>();
    private List<BloqueoModel> bloqueos = new ArrayList<>();
    private List<PedidoModel> requestListDesdoblado = new ArrayList<>();
    private MapaModel mapaModel= null;
    private static int dia = 0;
    private static Calendar inicio = null;
    private static Calendar fin = null;
    */
    private static int dia = 0;

    private PedidoService pedidoService;
    private VehiculoService vehiculoService;
    private BloqueoService bloqueoService;

    private List<EntidadVehiculo> listaVehiculos = new ArrayList<>();
    private List<PedidoModel> requestListDesdoblado = new ArrayList<>();
    private List<NodoModel> blockList = new ArrayList<>();

    public PlanificacionTareas(Date fecha,List<EntidadVehiculo> listaVeh){
        Calendar date = Calendar.getInstance();
        date.setTime(fecha);
        dia = date.get(Calendar.DATE);
        this.listaVehiculos = listaVeh;
    }
    /*
    @PostConstruct
    private void postConstruct() {
        Calendar date = Calendar.getInstance();
        date.setTime(controlTarea.getFechaInicio());
        dia = date.get(Calendar.DATE);
    }*/

    @Override
    public void run() {
        // aqui deberia ir el algoritmo y un emitter

        try{
            EntidadRutas rutasFinal = EntidadRutas.builder().paths(new ArrayList<>()).build();
            int minimo = 5;
            // Split request list in minimum capacity
            int totalCapacity = 0;
            Calendar inicio = Calendar.getInstance();
            Calendar fin = Calendar.getInstance();
            inicio.setTime(controlTarea.getFechaInicio());
            fin.setTime(controlTarea.getFechaFin());

            //Carga de los pedidos

            requestListDesdoblado = pedidoService.obtenerPedidos3días(inicio,fin,dia);

            listaVehiculos = vehiculoService.listaVehiculosDisponibles();
            listaVehiculos.sort((v1, v2) -> Long.compare(v1.getFechaInicio().getTimeInMillis() , v2.getFechaInicio().getTimeInMillis()));
            //listaVehiculos = vehiculoService.listaVehiculosDisponibles();

            blockList = bloqueoService.listaBloqueosDiaDia();

            var depositos = controlTarea.getMapaModel().getPlantas();
            for(PedidoModel pedido:requestListDesdoblado){
                totalCapacity += pedido.getCantidadGLP();
            }

            System.out.println("Total Capacity: " + totalCapacity);

            // lista que tendrá los vehículos y sus listas de pedidos ordenados por indice
            ArrayList<Pair<EntidadVehiculo, PriorityQueue<Pair<Float, PedidoModel>>>> listaVC = new ArrayList<>();

            // Tener un arreglo auxiliar de pedidos
            List<PedidoModel> auxRequest = new ArrayList<>();
            requestListDesdoblado.forEach(r -> {
                auxRequest.add(r);
            });


            float tiempoTotal=0;
            do {

                listaVC.clear();

                listaVehiculos.forEach(v -> {
                    PriorityQueue<Pair<Float, PedidoModel>> requestListArreange = new PriorityQueue<>(Comparator.comparing(Pair::getKey));
                    listaVC.add(new Pair<>(v, requestListArreange));
                });

                int colapso;

                for(PedidoModel req : requestListDesdoblado){
                    colapso = 0;
                    for(Pair<EntidadVehiculo, PriorityQueue<Pair<Float, PedidoModel>>> lvc : listaVC){
                        EntidadVehiculo v = lvc.getKey();
                        PriorityQueue<Pair<Float, PedidoModel>> requestListArreange = lvc.getValue();
                        float distance = v.getNodoActual().getDistancia(req);
                        float tiempoAproximado = (float)(distance/v.getVelocidad());

                        //System.out.println("Tiempo del carro : "+ v.getFechaInicio().getTime().toString());
                        //System.out.println("Tiempo del pedido : "+ req.getHorasLimite().getTime().toString());

                        float tiempoLlegadaLimite = req.getHorasLimite().getTimeInMillis() - v.getFechaInicio().getTimeInMillis();

                        if(tiempoLlegadaLimite > 0)
                            requestListArreange.add(new Pair<>((tiempoLlegadaLimite /tiempoAproximado),req));
                        else{
                            System.out.println("idPedido colapsado: " + req.getIdNodo() + "-" + req.getIdExtendido());
                            colapso++;
                        }
                    }
                    if(colapso == listaVC.size()){
                        try {
                            int totalGLP = 0;
                            for (PedidoModel r : requestListDesdoblado)
                                totalGLP += r.getCantidadGLP();

                            int pedidoCompletado = 0;
                            for (PedidoModel rq : requestListDesdoblado) {
                                double aux = 0;
                                aux = auxRequest.stream()
                                        .filter(auxrq -> auxrq.getIdNodo() == rq.getIdNodo())
                                        .map(auxrq -> auxrq.isAtendido() ? auxrq.getCantidadGLP() : 0)
                                        .reduce(aux, (accumulator, _item) -> accumulator + _item);
                                if (aux == rq.getCantidadGLP())
                                    pedidoCompletado++;
                            }
                        }catch(Exception error){
                            System.err.println("Coalpso Logístico" + error.getMessage());
                        }
                    }
                }

                int indice=0;
                for(Pair<EntidadVehiculo, PriorityQueue<Pair<Float, PedidoModel>>> vc : listaVC){
                    // Se asigna los pedidos a los vehículos
                    //listaVehiculos.clear(); // se puede quitar esta lista intermedia
                    //listaVehiculos.add(vc.getKey());

                    //System.out.println("PQ: " + vc.getValue());
                    int asignado = 0;
                    try {
                        asignado += Knapsack.allocate(vc.getValue(), listaVehiculos, auxRequest);
                        //verificar si entra en el camión los pedidos.
                        if (indice == listaVC.size() - 1) {
                            continue;
                        }
                        Pair<EntidadVehiculo, PriorityQueue<Pair<Float, PedidoModel>>> vcNext = listaVC.get(indice + 1);
                        /*EntidadVehiculo currentVehicle = vc.getKey();
                        EntidadVehiculo nextVehicle = vcNext.getKey();
                        if (currentVehicle.getFechaInicio().get(Calendar.DATE)
                                != nextVehicle.getFechaInicio().get(Calendar.DATE)) {
                            depositos.forEach((d) -> {
                                d.set(d.getTotalCapacity());
                            });
                        }*/
                        if (!vc.getKey().getListaPedidos().isEmpty()){
                            vehiculoService.actualizarEstadoVehiculo(vc.getKey().getIdVehiculo());
                        }

                    }
                    catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    indice++;
                }

                listaVehiculos.clear(); // se puede eliminar esta lista intermedia
                listaVC.forEach(vc -> {
                    listaVehiculos.add(vc.getKey());
                });

                //Algoritmo Genetico
                ArrayList<NodoModel> vertices = new ArrayList<>(); // se puede quitar esta lista intermedia
                for(EntidadVehiculo v : listaVehiculos){
                    vertices.clear();
                    v.getListaPedidos().forEach(p -> { vertices.add(p); });
                    System.out.println(v.getListaPedidos());
                    if(!v.getListaPedidos().isEmpty())
                        GeneticAlgorithm.Genetic(v, vertices, controlTarea.getMapaModel());

                }
                Timestamp fechaHoraNuevaVehiculo = null;
                for(EntidadVehiculo v : listaVehiculos){
                    if(v.getRutaVehiculo() != null && !v.getRutaVehiculo().isEmpty()) { // si encontró una buana ruta.
                        v.getFechaInicio().add(Calendar.MINUTE, Math.round((float) Math.ceil(v.calculateTimeToDispatch())));
                        v.setNodoActual(v.getRutaVehiculo().get(v.getRutaVehiculo().size() - 1));
                        tiempoTotal += v.calculateTimeToDispatch();
                        // se guardan las rutas y los pedidos
                        // aquí se podría enviar cada vehículo con su ruta
                        SimpleDateFormat sdf;
                        sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
                        sdf.setTimeZone(TimeZone.getTimeZone("CET"));
                        String text = sdf.format(v.getFechaInicio().getTime());
                        EntidadRuta rutaVehiculo = EntidadRuta.builder().startTime(text).path(v.getRutaVehiculoPositions(requestListDesdoblado)).endTime("F").build();
                        rutasFinal.agregarRuta(rutaVehiculo);
                        //vehiculoService.actualizarEstadoVehiculoToVacio(v.getIdVehiculo());
                        fechaHoraNuevaVehiculo = new Timestamp(v.getFechaInicio().getTime().getTime());
                        vehiculoService.actualizarTiempoEstado(v.getIdVehiculo(), fechaHoraNuevaVehiculo);
                        v.clearVehicle();
                    }
                }

                // Se hace sort para las capacidadades

                listaVehiculos.sort((v1, v2) -> Long.compare(v1.getFechaInicio().getTimeInMillis() , v2.getFechaInicio().getTimeInMillis()));

                // Se filtra una nueva lista de los pedidos desdoblados
                List<PedidoModel> aux = new ArrayList<>();
                requestListDesdoblado.forEach(r -> {
                    if(!r.isAtendido()) {
                        aux.add(r);
                    }else{
                        pedidoService.actualizarPedidosAtentidosDesdoblado(r.getIdNodo(),r.getIdExtendido());
                    }
                    if (pedidoService.verificarTotalPedidosDesdobladosAtendidos(r.getIdNodo())==1){
                        pedidoService.actualizarPedidoPadreAtentido(r.getIdNodo());
                    }

                });

                requestListDesdoblado = aux;

            } while(!requestListDesdoblado.isEmpty());

            Collections.sort(rutasFinal.getPaths());


            emitter.send(
                    SseEmitter.event()
                            .name("3dias")
                            .data(rutasFinal)
            );

            counter++;
            if (counter%6==0){
                dia+=1;
            }
            if(counter==18){
                emitter.send(SseEmitter.event().name("STOP").data("ACABO"));
                emitter.complete();
                planificadorTareasServicios.eliminarPlanificadorTareas(uuid);
                dia=0;
            }

        }
        catch(IOException e){
            emitter.completeWithError(e);
            planificadorTareasServicios.eliminarPlanificadorTareas(uuid);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
