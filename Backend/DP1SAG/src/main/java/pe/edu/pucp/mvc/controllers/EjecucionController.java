package pe.edu.pucp.mvc.controllers;


import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.amqp.AbstractRabbitListenerContainerFactoryConfigurer;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import pe.edu.pucp.algorithm.GeneticAlgorithm;
import pe.edu.pucp.algorithm.Knapsack;
import pe.edu.pucp.mvc.models.*;
import pe.edu.pucp.mvc.planificacion.ControlTarea;
import pe.edu.pucp.mvc.planificacion.PlanificacionTareas;
import pe.edu.pucp.mvc.planificacion.PlanificadorTareasServicios;
import pe.edu.pucp.mvc.planificacion.ScheduledTasks;
import pe.edu.pucp.mvc.services.BloqueoService;
import pe.edu.pucp.mvc.services.PedidoService;
import pe.edu.pucp.mvc.services.VehiculoService;
import pe.edu.pucp.utils.LecturaPedido;
import pe.edu.pucp.utils.LecturaBloques;
import pe.edu.pucp.utils.LecturaVehiculo;

import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

@RestController
@RequestMapping("/ejecutar")
public class EjecucionController {

    @Autowired
    private PlanificadorTareasServicios planificadorTareasServicios;

    @Autowired
    private PedidoService pedidoService;

    @Autowired
    private VehiculoService vehiculoService;

    @Autowired
    private BloqueoService bloqueoService;

    public static List<SseEmitter> sseEmitters = new ArrayList<>();

    @GetMapping(value = "/obtenerRutas")
    public SseEmitter devolverRutas(){
        SseEmitter sseEmitter = new SseEmitter(Long.MAX_VALUE);
        sseEmitters.add(sseEmitter);
        sseEmitter.onCompletion(() -> { EjecucionController.sseEmitters.remove(ScheduledTasks.emi); ScheduledTasks.emi = null; });
        sseEmitter.onTimeout(() -> { EjecucionController.sseEmitters.remove(ScheduledTasks.emi); ScheduledTasks.emi = null; });
        ScheduledTasks.emi = sseEmitter;
        return sseEmitter;
    }

    private String generateCronExpression() {
        Calendar now = Calendar.getInstance();
        now.add(Calendar.SECOND, 20);
        int min = now.get(Calendar.MINUTE);
        int sec = now.get(Calendar.SECOND);
        return sec + " " + min + "/10 * * * ?";
    }

    /*
    @GetMapping(value = "/obtenerTresDias")
    public void devolverRutasTresDias(){
        Metodo metodo = new Metodo();
        metodo.ejectuarAlgoritmo();
    }*/

    @GetMapping(value = "/obtenerTresDias")
    public SseEmitter devolverRutasTresDias(String fecha){
        SseEmitter emitter = null;

        try{
            emitter = new SseEmitter(Long.MAX_VALUE);
            ControlTarea controlDatosPlanificador = ControlTarea.builder()
                    .tipoAction("Simulacion3Dias")
                    .datos("")
                    .fecha(fecha)
                    .controlCron(generateCronExpression())
                    .build();
            // Cargar vehículos
            controlDatosPlanificador.setVehiculoModels(vehiculoService.listaVehiculosDisponibles());
            List<EntidadVehiculo> listaVehiculos = new ArrayList<>();
            List<VehiculoModel> vehiculoModels = controlDatosPlanificador.getVehiculoModels();
            vehiculoModels.forEach(vehiculo -> listaVehiculos.add(new EntidadVehiculo(vehiculo)));
            controlDatosPlanificador.setListaVehiculos(listaVehiculos);

            // Cargar bloqueos
            controlDatosPlanificador.setBlockList(bloqueoService.listaBloqueosDiaDia());

            //Cargar pedidos
            controlDatosPlanificador.setListaPedidos(pedidoService.listaPedidosSinAtender());

            MapaModel mapa = new MapaModel();
            controlDatosPlanificador.setMapaModel(new MapaModel(70,50,mapa.obtenerPlantarIntermedias()));
            Date fechaIni = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(fecha);
            controlDatosPlanificador.setFechaInicio(fechaIni);
            var localDate = fechaIni.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            // Para aumentar la fecha a 3 dentro de tres días
            localDate = localDate.plusDays(3);
            System.out.println(java.sql.Timestamp.valueOf(localDate));
            controlDatosPlanificador.setFechaFin(java.sql.Timestamp.valueOf(localDate));
            controlDatosPlanificador.setFechaReferencial(fechaIni);


            PlanificacionTareas planificadorTareas = new PlanificacionTareas();
            String uuid = UUID.randomUUID().toString();
            emitter.onCompletion(() -> planificadorTareasServicios.eliminarPlanificadorTareas(uuid));
            emitter.onTimeout(() -> planificadorTareasServicios.eliminarPlanificadorTareas(uuid));
            planificadorTareas.setControlTarea(controlDatosPlanificador);
            planificadorTareas.setEmitter(emitter);
            planificadorTareas.setUuid(uuid);
            planificadorTareas.setPlanificadorTareasServicios(planificadorTareasServicios);
            planificadorTareasServicios.planificarTareas(uuid, planificadorTareas, controlDatosPlanificador.getControlCron());

        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
        return emitter;

    }

    @PostMapping(value = "/simularRutasColapso")
    public EntidadRutas ejecutarAlgortimo() throws Exception {

        EntidadRutas rutasFinal = EntidadRutas.builder().paths(new ArrayList<>()).build();

        List<PedidoModel> listaPedidos;

        List<EntidadVehiculo> listaVehiculos;

        listaVehiculos = LecturaVehiculo.lectura("/home/ubuntu/Grupo2/Download/vehiculos2021.txt");

        //listaPedidos = LecturaPedido.lectura("/home/ubuntu/Grupo2/Download/ventas/ventas202202.txt");

        ArrayList<NodoModel> blockList = LecturaBloques.lectura("/home/ubuntu/Grupo2/Download/bloqueos/202112bloqueadas.txt");

        // Depositos iniciales
        ArrayList<PlantaModel> plantas = new ArrayList<>();
        plantas.add(PlantaModel.builder().coordenadaX(12).coordenadaY(8).esPrincipal(true).build());
        plantas.add(PlantaModel.builder().coordenadaX(42).coordenadaY(42).build());
        plantas.add(PlantaModel.builder().coordenadaX(63).coordenadaY(3).build());

        MapaModel mapaModel = new MapaModel(70, 50, plantas);
        mapaModel.setBlockList(blockList);


        listaVehiculos.forEach(v -> {
            // Fecha de inicio y copia de fecha de inicio
            Calendar init = Calendar.getInstance();
            init.set(2021, 11, 1, 0, 0, 0);
            v.setFechaInicio(init);
            v.setNodoActual(plantas.get(0));
            v.setCombustible(25);
            v.setVelocidad(50);
        });

        List<PedidoModel> requestListDesdoblado = new ArrayList<>();
        int minimo = 5;

        // Split request list in minimum capacity
        int totalCapacity = 0;

        requestListDesdoblado = pedidoService.listaPedidosSinAtender();

        for(PedidoModel pedido: requestListDesdoblado){
            totalCapacity += pedido.getCantidadGLP();
        }

        System.out.println("Total Capacity: " + totalCapacity);
        // lista que tendrá los vehículos y sus listas de pedidos ordenados por indice
        ArrayList<Pair<EntidadVehiculo, PriorityQueue<Pair<Float, PedidoModel>>>> listaVC = new ArrayList<>();

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
                //Assign request to vehicles
                listaVehiculos.clear(); // se puede quitar esta lista intermedia
                listaVehiculos.add(vc.getKey());
//                System.out.println("PQ: " + vc.getValue());
                int assigned = 0;
                try {
                    //assigned += Knapsack.allocate(vc.getValue(), listaVehiculos, auxRequest);
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

            /*List<PedidoModel> aux = new ArrayList<>();
            requestListDesdoblado.forEach(r -> {
                if(!r.isAtendido()) {
                    aux.add(r);
                }
            });

            requestListDesdoblado = aux;
            */
        } while(!requestListDesdoblado.isEmpty());

        Collections.sort(rutasFinal.getPaths());

        return rutasFinal;
    }


    @PostMapping(value = "/simularRutas")
    public EntidadRutas ejecutarAlgortimodias() throws Exception {


        EntidadRutas rutasFinal = EntidadRutas.builder().paths(new ArrayList<>()).build();

        List<PedidoModel> listaPedidos;

        List<EntidadVehiculo> listaVehiculos;

        //listaVehiculos = LecturaVehiculo.TxtReader("/home/ubuntu/Grupo2/Download/vehiculos2021.txt");
        listaVehiculos = LecturaVehiculo.lectura("D:\\CICLO10\\Trabajo\\Grupo2\\Download\\vehiculos2021.txt");
        //listaPedidos = Lectura.TxtReader("/home/ubuntu/Grupo2/Download/ventas/ventas202201.txt");
        listaPedidos = LecturaPedido.lectura("D:\\CICLO10\\Trabajo\\Grupo2\\Download\\ventas\\ventas202201.txt");
        //ArrayList<NodoModel> blockList = LecturaBloques.TxtReader("/home/ubuntu/Grupo2/Download/bloqueos/202112bloqueadas.txt");
        ArrayList<NodoModel> blockList = LecturaBloques.lectura("D:\\CICLO10\\Trabajo\\Grupo2\\Download\\bloqueos\\202112bloqueadas.txt");
        // Depositos iniciales
        ArrayList<PlantaModel> plantas = new ArrayList<>();
        plantas.add(PlantaModel.builder().coordenadaX(12).coordenadaY(8).esPrincipal(true).build());
        plantas.add(PlantaModel.builder().coordenadaX(42).coordenadaY(42).build());
        plantas.add(PlantaModel.builder().coordenadaX(63).coordenadaY(3).build());

        MapaModel mapaModel = new MapaModel(70, 50, plantas);
        mapaModel.setBlockList(blockList);


        listaVehiculos.forEach(v -> {
            // Fecha de inicio y copia de fecha de inicio
            Calendar init = Calendar.getInstance();
            init.set(2021, 11, 1, 0, 0, 0);
            v.setFechaInicio(init);
            v.setNodoActual(plantas.get(0));
            v.setCombustible(25);
            v.setVelocidad(50);
        });

        List<PedidoModel> requestListDesdoblado = new ArrayList<>();
        int minimo = 5;

        // Split request list in minimum capacity
        int totalCapacity = 0;

        for(PedidoModel r : listaPedidos){
            int i = 1;
            for(; i < (int)r.getCantidadGLP()/minimo + 1; i++)
                requestListDesdoblado.add(PedidoModel.builder()
                        .idNodo(r.getIdNodo())
                        .idExtendido(i)
                        .clienteModel(r.getClienteModel())
                        .cantidadGLP(minimo)
                        .coordenadaX(r.getCoordenadaX())
                        .coordenadaY(r.getCoordenadaY())
                        .fechaPedido(r.getFechaPedido())
                        .horasLimite(r.getHorasLimite()).build());
            if(r.getCantidadGLP()%minimo != 0.0)
                requestListDesdoblado.add(PedidoModel.builder()
                        .idNodo(r.getIdNodo())
                        .idExtendido(i)
                        .clienteModel(r.getClienteModel())
                        .cantidadGLP(r.getCantidadGLP()%minimo)
                        .coordenadaX(r.getCoordenadaX())
                        .coordenadaY(r.getCoordenadaY())
                        .fechaPedido(r.getFechaPedido())
                        .horasLimite(r.getHorasLimite()).build());
            totalCapacity += r.getCantidadGLP();
        }

        System.out.println("Total Capacity: " + totalCapacity);
        // lista que tendrá los vehículos y sus listas de pedidos ordenados por indice
        ArrayList<Pair<EntidadVehiculo, PriorityQueue<Pair<Float, PedidoModel>>>> listaVC = new ArrayList<>();

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
                    for(PedidoModel rq : listaPedidos){
                        double aux = 0;
                        aux = auxRequest.stream()
                                .filter(auxrq -> auxrq.getIdNodo() == rq.getIdNodo())
                                .map(auxrq -> auxrq.isAtendido() ? auxrq.getCantidadGLP() : 0)
                                .reduce(aux, (accumulator, _item) -> accumulator + _item);
                        if(aux == rq.getCantidadGLP())
                            pedidoCompletado++;
                    }

                    System.out.println("Total de glp entregado = " + (totalCapacity - totalGLP));
                    System.out.println("Total de glp que falta entregar = " + totalGLP);
                    System.out.println("Total de tiempo = " + totalTime/60);
                    System.out.println("Pedidos recibidos = " + listaPedidos.size());
                    System.out.println("Pedidos completados = " + pedidoCompletado);
                    throw new Exception("Llegó al colapso logístico");
                }
            }

            for(Pair<EntidadVehiculo, PriorityQueue<Pair<Float, PedidoModel>>> vc : listaVC){
                //Assign request to vehicles
                listaVehiculos.clear(); // se puede quitar esta lista intermedia
                listaVehiculos.add(vc.getKey());
//                System.out.println("PQ: " + vc.getValue());
                int assigned = 0;
                try {
                    //assigned += Knapsack.allocate(vc.getValue(), listaVehiculos, auxRequest);
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

        return rutasFinal;
    }



}