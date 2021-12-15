package pe.edu.pucp.mvc.planificacion;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
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

    @Autowired
    private PedidoService pedidoService;

    @Autowired
    private VehiculoService vehiculoService;

    @Autowired
    private BloqueoService bloqueoService;


    @PostConstruct
    private void postConstruct() {

        // cargar fechas de inicio
        inicio = Calendar.getInstance();
        fin = Calendar.getInstance();
        inicio.set(2021,11,13,0,0,0);
        fin.set(2021,11,15,0,0,0);

        // Carga de velículos
        vehiculoModels = vehiculoService.listaVehiculosDisponibles();
        vehiculoModels.forEach(vehiculo -> listaVehiculos.add(new EntidadVehiculo(vehiculo)));

        // Carga de información de los bloqueos
        blockList = bloqueoService.listaBloqueosDiaDia();
        //bloqueos.forEach(bloqueo -> blockList.add(new NodoModel(bloqueo)));

        // Depositos iniciales
        ArrayList<PlantaModel> plantas = new ArrayList<>();
        plantas.add(PlantaModel.builder().coordenadaX(12).coordenadaY(8).esPrincipal(true).build());
        plantas.add(PlantaModel.builder().coordenadaX(42).coordenadaY(42).build());
        plantas.add(PlantaModel.builder().coordenadaX(63).coordenadaY(3).build());
        mapaModel = new MapaModel(70, 50, plantas);
        // Se crea el mapa

        mapaModel.setBlockList(blockList);

        LocalDateTime now = LocalDateTime.now();
        dia = now.getDayOfMonth();
        // Inicializar las fechas de inicio de los vehículos
        listaVehiculos.forEach(v -> {
            // Fecha de inicio y copia de fecha de inicio
            Calendar init = Calendar.getInstance();
            init.set(now.getYear(), now.getMonth().getValue()-1, now.getDayOfMonth(), now.getHour(), now.getMinute(), now.getSecond());
            v.setFechaInicio(init);
            v.setNodoActual(plantas.get(0));
            v.setCombustible(25);
            v.setVelocidad(50);
        });
    }



    @Override
    public void run() {
        // aqui deberia ir el algoritmo y un emitter
        System.out.println("Running action: " + controlTarea.getTipoAction());
        System.out.println("With Data: " + controlTarea.getDatos());

        try{
            EntidadRutas rutasFinal = EntidadRutas.builder().paths(new ArrayList<>()).build();
            int minimo = 5;
            // Split request list in minimum capacity
            int totalCapacity = 0;

            //Carga de los pedidos
            requestListDesdoblado = pedidoService.obtenerPedidos3días(inicio,fin,dia);

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


            float totalTime=0;
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
                        float distance = v.getNodoActual().getDistancia(req),
                                tiempoAproximado = (float)(distance/v.getVelocidad()),

                                tiempoLlegadaLimite = req.getHorasLimite().getTimeInMillis() - v.getFechaInicio().getTimeInMillis();

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


                for(Pair<EntidadVehiculo, PriorityQueue<Pair<Float, PedidoModel>>> vc : listaVC){
                    // Se asigna los pedidos a los vehículos

                    listaVehiculos.clear(); // se puede quitar esta lista intermedia
                    listaVehiculos.add(vc.getKey());

                    //System.out.println("PQ: " + vc.getValue());
                    int assigned = 0;
                    try {
                        assigned += Knapsack.allocate(vc.getValue(), listaVehiculos, auxRequest);
                        //verificar si entra en el camión los pedidos.
                        if (!vc.getKey().getListaPedidos().isEmpty()){
                            vehiculoService.actualizarEstadoVehiculo(vc.getKey().getIdVehiculo());
                        }
                    }
                    catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
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
                        GeneticAlgorithm.Genetic(v, vertices, mapaModel);

                }
                for(EntidadVehiculo v : listaVehiculos){
                    if(v.getRutaVehiculo() != null && !v.getRutaVehiculo().isEmpty()) { // si encontró una buana ruta.
                        v.getFechaInicio().add(Calendar.MINUTE, Math.round((float) Math.ceil(v.calculateTimeToDispatch())));
                        v.setNodoActual(v.getRutaVehiculo().get(v.getRutaVehiculo().size() - 1));
                        totalTime += v.calculateTimeToDispatch();
                        // se guardan las rutas y los pedidos
                        // aquí se podría enviar cada vehículo con su ruta
                        SimpleDateFormat sdf;
                        sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
                        sdf.setTimeZone(TimeZone.getTimeZone("CET"));
                        String text = sdf.format(v.getFechaInicio().getTime());
                        EntidadRuta rutaVehiculo = EntidadRuta.builder().startTime(text).path(v.getRutaVehiculoPositions(requestListDesdoblado)).endTime("F").build();
                        rutasFinal.agregarRuta(rutaVehiculo);
                        v.clearVehicle();
                        vehiculoService.actualizarEstadoVehiculoToVacio(v.getIdVehiculo());
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
            dia+=1;
            counter++;
            if(counter==3){
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
