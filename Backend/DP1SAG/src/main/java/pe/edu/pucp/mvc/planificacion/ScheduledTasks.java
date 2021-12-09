package pe.edu.pucp.mvc.planificacion;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import javafx.util.Pair;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import pe.edu.pucp.algorithm.GeneticAlgorithm;
import pe.edu.pucp.algorithm.Knapsack;
import pe.edu.pucp.mvc.controllers.EjecucionController;
import pe.edu.pucp.mvc.controllers.MapaModel;
import pe.edu.pucp.mvc.metodos.EjecucionAlgoritmo;
import pe.edu.pucp.mvc.models.*;
import pe.edu.pucp.mvc.services.BloqueoService;
import pe.edu.pucp.mvc.services.PedidoService;
import pe.edu.pucp.mvc.services.VehiculoService;
import pe.edu.pucp.utils.LecturaBloques;
import pe.edu.pucp.utils.LecturaVehiculo;

@Component
public class ScheduledTasks {

    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    private InicializarDiaDia inicializarDiaDia = new InicializarDiaDia();

    @Autowired
    private PedidoService pedidoService;

    @Autowired
    private VehiculoService vehiculoService;

    @Autowired
    private BloqueoService bloqueoService;

    public static SseEmitter emi = null;

    @Scheduled(fixedRate = 80000)
    public void reportCurrentTime() throws Exception {

        EntidadRutas rutasFinal = EntidadRutas.builder().paths(new ArrayList<>()).build();

        inicializarDiaDia.inicializarProceso();
        List<PedidoModel> listaPedidos = inicializarDiaDia.listaPedidos;
        List<EntidadVehiculo> listaVehiculos = inicializarDiaDia.listaVehiculos;
        List<NodoModel> blockList = inicializarDiaDia.blockList;
        List<BloqueoModel> bloqueos = inicializarDiaDia.bloqueos;
        List<PedidoModel> requestListDesdoblado = inicializarDiaDia.listaPedidos;
        MapaModel mapaModel= inicializarDiaDia.mapaModel;

        int minimo = 5;

        // Split request list in minimum capacity
        int totalCapacity = 0;

        requestListDesdoblado = pedidoService.listaPedidosSinAtender();

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

                    int totalGLP = 0;
                    for(PedidoModel r : requestListDesdoblado)
                        totalGLP += r.getCantidadGLP();

                    int pedidoCompletado = 0;
                    for(PedidoModel rq : requestListDesdoblado){
                        double aux = 0;
                        aux = auxRequest.stream()
                                .filter(auxrq -> auxrq.getIdNodo() == rq.getIdNodo())
                                .map(auxrq -> auxrq.isAtendido() ? auxrq.getCantidadGLP() : 0)
                                .reduce(aux, (accumulator, _item) -> accumulator + _item);
                        if(aux == rq.getCantidadGLP())
                            pedidoCompletado++;
                    }

                    throw new Exception("Llegó al colapso logístico");
                }
            }

            for(Pair<EntidadVehiculo, PriorityQueue<Pair<Float, PedidoModel>>> vc : listaVC){
                // Se asigna los pedidos a los vehículos

                listaVehiculos.clear(); // se puede quitar esta lista intermedia
                listaVehiculos.add(vc.getKey());

                System.out.println("PQ: " + vc.getValue());
                int assigned = 0;
                try {
                    assigned += Knapsack.allocate(vc.getValue(), listaVehiculos, auxRequest);
                    //verificar si entra en el camión los pedidos.
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
                    EntidadRuta rutaVehiculo = EntidadRuta.builder().startTime(text).path(v.getRutaVehiculoPositions(requestListDesdoblado)).endTime("Alap").build();
                    rutasFinal.agregarRuta(rutaVehiculo);
                }
                v.clearVehicle();
            }

            // Se hace sort para las capacidadades
            listaVehiculos.sort((v1, v2) -> Long.compare(v1.getFechaInicio().getTimeInMillis() , v2.getFechaInicio().getTimeInMillis()));

            List<PedidoModel> aux = new ArrayList<>();
            requestListDesdoblado.forEach(r -> {
                if(!r.isAtendido()) {
                    aux.add(r);
                }
            });

            requestListDesdoblado = aux;

        } while(!requestListDesdoblado.isEmpty());

        Collections.sort(rutasFinal.getPaths());

        if (emi!=null){
            emi.send(SseEmitter.event().name("RUTAS").data(rutasFinal));
        }
    }

}
