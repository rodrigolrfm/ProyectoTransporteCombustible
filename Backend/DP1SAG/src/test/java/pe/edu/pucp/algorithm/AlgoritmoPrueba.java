package pe.edu.pucp.algorithm;

import java.util.Calendar;
import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;

import java.util.PriorityQueue;

import javafx.util.Pair;
import org.junit.jupiter.api.Test;
import pe.edu.pucp.mvc.controllers.MapaModel;
import pe.edu.pucp.mvc.models.NodoModel;
//import pe.edu.pucp.mvc.models.NodoModel;
import pe.edu.pucp.mvc.models.PedidoModel;
import pe.edu.pucp.mvc.models.PlantaModel;
import pe.edu.pucp.mvc.models.EntidadVehiculo;
import pe.edu.pucp.utils.LecturaBloques;
import pe.edu.pucp.utils.Lectura;
import pe.edu.pucp.utils.LecturaVehiculo;


public class AlgoritmoPrueba {

    @Test
    public void simularpedido() throws Exception {


        List<PedidoModel> listaPedidos;

        List<EntidadVehiculo> listaVehiculos;

        listaVehiculos = LecturaVehiculo.lectura("src\\main\\java\\pe\\edu\\pucp\\files\\vehiculos2021.txt");

        listaPedidos = Lectura.lectura("src\\main\\java\\pe\\edu\\pucp\\files\\ventas\\ventas202112.txt");

        
        ArrayList<NodoModel> blockList = LecturaBloques.lectura("src\\main\\java\\pe\\edu\\pucp\\files\\bloqueos\\202112bloqueadas.txt");
        
        // Inicializar plantas

        ArrayList<PlantaModel> plantas = new ArrayList<>();
        plantas.add(PlantaModel.builder().coordenadaX(12).coordenadaY(8).esPrincipal(true).build());
        plantas.add(PlantaModel.builder().coordenadaX(42).coordenadaY(42).build());
        plantas.add(PlantaModel.builder().coordenadaX(63).coordenadaY(3).build());
        
        MapaModel mapaModel = new MapaModel(70, 50, plantas);
        mapaModel.setBlockList(blockList);

        // Inicializar vehículos
        listaVehiculos.forEach(v -> {
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
            int i = 0;
            for(; i < (int)r.getCantidadGLP()/minimo; i++)
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
                    .idExtendido(++i)
                    .clienteModel(r.getClienteModel())
                    .cantidadGLP(r.getCantidadGLP()%minimo)
                    .coordenadaX(r.getCoordenadaX())
                    .coordenadaY(r.getCoordenadaY())
                    .fechaPedido(r.getFechaPedido())
                    .horasLimite(r.getHorasLimite()).build());
            totalCapacity += r.getCantidadGLP();
        }      
    
        System.out.println("Total Capacity: " + totalCapacity);

        ArrayList<Pair<EntidadVehiculo, PriorityQueue<Pair<Float, PedidoModel>>>> listaVC = new ArrayList<>();

        // Lista de pedidos auxiliar
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
                    System.out.println("Total de glp por entregar = " + totalGLP);
                    System.out.println("Total de tiempo = " + totalTime/60);
                    System.out.println("Pedidos recibidos = " + listaPedidos.size());
                    System.out.println("Pedidos completados = " + pedidoCompletado);
                    throw new Exception("COLAPSO LOGÍSTICO!!");
                }
            }          
            
            for(Pair<EntidadVehiculo, PriorityQueue<Pair<Float, PedidoModel>>> vc : listaVC){
                //Assign request to vehicles
                listaVehiculos.clear();
                listaVehiculos.add(vc.getKey());
                int assigned = 0;
                try {
                    assigned += Knapsack.allocate(vc.getValue(), listaVehiculos, auxRequest);
                }
                catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
            
            listaVehiculos.clear();
            listaVC.forEach(vc -> {
               listaVehiculos.add(vc.getKey());
            });

            //Algoritmo Genético
            ArrayList<NodoModel> vertices = new ArrayList<>();
            for(EntidadVehiculo v : listaVehiculos){
                vertices.clear();
                v.getListaPedidos().forEach(p -> {
                    vertices.add(p);
                });
                System.out.println(v.getListaPedidos());
                if(!v.getListaPedidos().isEmpty())
                    GeneticAlgorithm.Genetic(v, vertices, mapaModel);
                    
            }
            for(EntidadVehiculo v : listaVehiculos){
                if(v.getRutaVehiculo() != null && !v.getRutaVehiculo().isEmpty()) {
                    v.getFechaInicio().add(Calendar.MINUTE, Math.round((float) Math.ceil(v.calculateTimeToDispatch())));
                    v.setNodoActual(v.getRutaVehiculo().get(v.getRutaVehiculo().size() - 1));
                    totalTime += v.calculateTimeToDispatch();
                    // se guardan las rutas y los pedidos
                }
                v.clearVehicle();
            }

            // Ordenar por capacidad
            listaVehiculos.sort((v1, v2) -> Long.compare(v1.getFechaInicio().getTimeInMillis() , v2.getFechaInicio().getTimeInMillis()));

            List<PedidoModel> aux = new ArrayList<>();
            requestListDesdoblado.forEach(r -> {
                if(!r.isAtendido()) {
                    aux.add(r);
                }
            });
            
            requestListDesdoblado = aux;
            
        } while(!requestListDesdoblado.isEmpty());
        
        System.out.println("Total de tiempo = " + totalTime/60);
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
                
        System.out.println("Cantidad de pedidos procesados = " + listaPedidos.size());
        System.out.println("Cantidad de pedidos completados = " + pedidoCompletado);
        
      }

}


