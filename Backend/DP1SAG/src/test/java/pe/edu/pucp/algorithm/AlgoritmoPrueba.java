package pe.edu.pucp.algorithm;

import java.util.Calendar;
import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;

import java.util.PriorityQueue;

import javafx.util.Pair;
import org.junit.jupiter.api.Test;
import pe.edu.pucp.mvc.models.NodoModel;
//import pe.edu.pucp.mvc.models.NodoModel;
import pe.edu.pucp.mvc.models.PedidoModel;
import pe.edu.pucp.mvc.models.PlantaModel;
import pe.edu.pucp.mvc.models.Vehicle;
import pe.edu.pucp.utils.LecturaBloques;
import pe.edu.pucp.utils.Lectura;
import pe.edu.pucp.utils.LecturaVehiculo;


public class AlgoritmoPrueba {

    @Test
    public void simularpedido() throws Exception {
        List<PedidoModel> requestList;
        List<Vehicle> vehicleList;
        

        vehicleList = LecturaVehiculo.TxtReader("src\\main\\java\\pe\\edu\\pucp\\files\\vehiculos2021.txt");

        requestList = Lectura.TxtReader("src\\main\\java\\pe\\edu\\pucp\\files\\ventas\\ventas202112.txt");

        
        ArrayList<NodoModel> blockList = LecturaBloques.TxtReader("src\\main\\java\\pe\\edu\\pucp\\files\\bloqueos\\202112bloqueadas.txt");
        
        // Depositos iniciales
        ArrayList<PlantaModel> depots = new ArrayList<>();
        depots.add(PlantaModel.builder().coordenadaX(12).coordenadaY(8).esPrincipal(true).build());
        depots.add(PlantaModel.builder().coordenadaX(42).coordenadaY(42).build());
        depots.add(PlantaModel.builder().coordenadaX(63).coordenadaY(3).build());
        
        Map map = new Map(70, 50, depots);
        map.setBlockList(blockList);
        
        // TODO: agregar restricciones de plantas sin GLP
        // TODO: agregar mantenimientos y averías
        
        vehicleList.forEach(v -> { 
            // Fecha de inicio y copia de fecha de inicio
            Calendar init = Calendar.getInstance(); 
            init.set(2021, 11, 1, 0, 0, 0);
            v.setInitDate(init); 
            v.setCurrentLocation(depots.get(0)); 
            v.setFuel(25);
            v.setVelocidad(50);
        });
        
        List<PedidoModel> requestListDesdoblado = new ArrayList<>();
        int minimo = 5;

        // Split request list in minimum capacity
        int totalCapacity = 0;
        // TODO: Mapear pedidos desdoblados
        for(PedidoModel r : requestList){ 
            int i = 0;
            for(; i < (int)r.getCantidadGLP()/minimo; i++)
                requestListDesdoblado.add(PedidoModel.builder()
                    .idPedido(r.getIdPedido())
                    .idExtendido(i)
                        .clienteModel(r.getClienteModel())
                    .cantidadGLP(minimo)
                    .coordenadaX(r.getCoordenadaX())
                    .coordenadaY(r.getCoordenadaY())
                    .fechaPedido(r.getFechaPedido())
                    .horasLimite(r.getHorasLimite()).build());
            if(r.getCantidadGLP()%minimo != 0.0)
                requestListDesdoblado.add(PedidoModel.builder()
                    .idPedido(r.getIdPedido())
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
        // lista que tendrá los vehículos y sus listas de pedidos ordenados por indice
        ArrayList<Pair<Vehicle, PriorityQueue<Pair<Float, PedidoModel>>>> listaVC = new ArrayList<>();
        
        List<PedidoModel> auxRequest = new ArrayList<>();
        requestListDesdoblado.forEach(r -> {
            auxRequest.add(r);
        });
        
        
        float totalTime=0;
        do {
            
            listaVC.clear();

            vehicleList.forEach(v -> {
                PriorityQueue<Pair<Float, PedidoModel>> requestListArreange = new PriorityQueue<>(Comparator.comparing(Pair::getKey));
                listaVC.add(new Pair<>(v, requestListArreange));
            });
            
            int colapso;
            // TODO: filtrar los pedidos que no se han registrado hasta ese momento
            for(PedidoModel req : requestListDesdoblado){
                colapso = 0;
                for(Pair<Vehicle, PriorityQueue<Pair<Float, PedidoModel>>> lvc : listaVC){
                    Vehicle v = lvc.getKey();
                    PriorityQueue<Pair<Float, PedidoModel>> requestListArreange = lvc.getValue();
                    float distance = v.getCurrentLocation().getDistancia(req),
                          tiempoAproximado = (float)(distance/v.getVelocidad()),
                          tiempoLlegadaLimite = req.getHorasLimite().getTimeInMillis() - v.getInitDate().getTimeInMillis();
                    
                    if(tiempoLlegadaLimite > 0)
                        requestListArreange.add(new Pair<>((tiempoLlegadaLimite /tiempoAproximado),req));
                    else{
                        System.out.println("idPedido colapsado: " + req.getIdPedido() + "-" + req.getIdExtendido());
                        colapso++;
                    }
                }
                if(colapso == listaVC.size()){

                    int totalGLP = 0;
                    for(PedidoModel r : requestListDesdoblado)
                        totalGLP += r.getCantidadGLP();
                    
                    int pedidoCompletado = 0;
                    for(PedidoModel rq : requestList){
                        double aux = 0;
                        aux = auxRequest.stream()
                                .filter(auxrq -> auxrq.getIdPedido() == rq.getIdPedido())
                                .map(auxrq -> auxrq.isAtendido() ? auxrq.getCantidadGLP() : 0)
                                .reduce(aux, (accumulator, _item) -> accumulator + _item);
                        if(aux == rq.getCantidadGLP())
                            pedidoCompletado++;
                    }
                            
                    System.out.println("Total de glp entregado = " + (totalCapacity - totalGLP));
                    System.out.println("Total de glp que falta entregar = " + totalGLP);
                    System.out.println("Total de tiempo = " + totalTime/60);
                    System.out.println("Pedidos recibidos = " + requestList.size());
                    System.out.println("Pedidos completados = " + pedidoCompletado);
                    throw new Exception("Llegó al colapso logístico");
                }
            }          
            
            for(Pair<Vehicle, PriorityQueue<Pair<Float, PedidoModel>>> vc : listaVC){
                //Assign request to vehicles
                vehicleList.clear(); // se puede quitar esta lista intermedia
                vehicleList.add(vc.getKey());
//                System.out.println("PQ: " + vc.getValue());
                int assigned = 0;
                try {
                    assigned += Knapsack.allocate(vc.getValue(), vehicleList, auxRequest); // TODO: refactorizar para no destruir la lista a cada rato
                    //verificar si entra en el camión los pedidos.
                }
                catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
            
            vehicleList.clear(); // se puede eliminar esta lista intermedia
            listaVC.forEach(vc -> {
               vehicleList.add(vc.getKey());
            });

            //Algoritmo Genetico
            ArrayList<NodoModel> vertices = new ArrayList<>(); // se puede quitar esta lista intermedia
            for(Vehicle v : vehicleList){
                vertices.clear();
                v.getRequestList().forEach( p -> { vertices.add(p); });
                System.out.println(v.getRequestList());
                if(!v.getRequestList().isEmpty())
                    GeneticAlgorithm.GA(v, vertices, map);
                    
            }
            for(Vehicle v : vehicleList){
                if(v.getRuta() != null && !v.getRuta().isEmpty()) { // si encontró una buana ruta.
                    v.getInitDate().add(Calendar.MINUTE, Math.round((float) Math.ceil(v.calculateTimeToDispatch())));
                    v.setCurrentLocation(v.getRuta().get(v.getRuta().size() - 1));
                    totalTime += v.calculateTimeToDispatch();
                    // se guardan las rutas y los pedidos
                }
                v.clearVehicle();
            }

            // Se hace sort para las capacidadades
            vehicleList.sort((v1, v2) -> Long.compare(v1.getInitDate().getTimeInMillis() , v2.getInitDate().getTimeInMillis()));

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
        for(PedidoModel rq : requestList){
            double aux = 0;
            aux = auxRequest.stream()
                    .filter(auxrq -> auxrq.getIdPedido() == rq.getIdPedido())
                    .map(auxrq -> auxrq.isAtendido() ? auxrq.getCantidadGLP() : 0)
                    .reduce(aux, (accumulator, _item) -> accumulator + _item);
            if(aux == rq.getCantidadGLP())
                pedidoCompletado++;
        }
                
        System.out.println("Pedidos recibidos = " + requestList.size());
        System.out.println("Pedidos completados = " + pedidoCompletado);
        System.out.println("C logró :D");
        
      }

      /*
    private static void printCalendar(Calendar cal){
        System.out.println("Año: " + cal.get(Calendar.YEAR) + " - Mes: " + cal.get(Calendar.MONTH) + " - Día: " + cal.get(Calendar.DATE)
                + " - Hora: " + cal.get(Calendar.HOUR) + " - Minuto: " + cal.get(Calendar.MINUTE) + " - Segundo: " + cal.get(Calendar.SECOND));
    }
    */
}


