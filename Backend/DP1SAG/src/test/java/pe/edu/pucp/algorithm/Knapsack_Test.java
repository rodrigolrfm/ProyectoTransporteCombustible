package pe.edu.pucp.algorithm;

import java.util.Calendar;
import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;

import java.util.PriorityQueue;

import javafx.util.Pair;
import pe.edu.pucp.mvc.models.Depot;
import pe.edu.pucp.mvc.models.Pedido;
import pe.edu.pucp.mvc.models.Vehicle;
import pe.edu.pucp.utils.BlockingReader;
import pe.edu.pucp.utils.RequestReader;
import pe.edu.pucp.utils.VehicleReader;


public class Knapsack_Test {
    public static void main(String[] args) throws Exception {
        List<Pedido> requestList;
        List<Vehicle> vehicleList;
        

        vehicleList = VehicleReader.TxtReader("src\\main\\java\\pe\\edu\\pucp\\files\\vehiculos2021.txt");

        requestList = RequestReader.TxtReader("src\\main\\java\\pe\\edu\\pucp\\files\\ventas\\ventas202112.txt");

        
        ArrayList<Node> blockList = BlockingReader.TxtReader("src\\main\\java\\pe\\edu\\pucp\\files\\bloqueos\\202112bloqueadas.txt");
        
        // Depositos iniciales
        ArrayList<Depot> depots = new ArrayList<>();
        depots.add(Depot.builder().coordX(5).coordY(10).isPrincipalDepot(true).build());
        depots.add(Depot.builder().coordX(42).coordY(42).build());
        depots.add(Depot.builder().coordX(63).coordY(3).build());
        
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
            v.setVelocity(50);
        });
        
        List<Pedido> requestListDesdoblado = new ArrayList<>();
        int minimo = 5;

        // Split request list in minimum capacity
        int totalCapacity = 0;
        // TODO: Mapear pedidos desdoblados
        for(Pedido r : requestList){ 
            int i = 0;
            for(; i < (int)r.getQuantityGLP()/minimo; i++)
                requestListDesdoblado.add(Pedido.builder()
                    .idPedido(r.getIdPedido())
                    .idDesdoblado(i)
                    .client(r.getClient())
                    .quantityGLP(minimo)
                    .coordX(r.getCoordX())
                    .coordY(r.getCoordY())
                    .orderDate(r.getOrderDate())
                    .hoursLimit(r.getHoursLimit()).build());
            if(r.getQuantityGLP()%minimo != 0.0)
                requestListDesdoblado.add(Pedido.builder()
                    .idPedido(r.getIdPedido())
                    .idDesdoblado(++i)
                    .client(r.getClient())
                    .quantityGLP(r.getQuantityGLP()%minimo)
                    .coordX(r.getCoordX())
                    .coordY(r.getCoordY())
                    .orderDate(r.getOrderDate())
                    .hoursLimit(r.getHoursLimit()).build());
            totalCapacity += r.getQuantityGLP();
        }      
    
        System.out.println("Total Capacity: " + totalCapacity);
        // lista que tendrá los vehículos y sus listas de pedidos ordenados por indice
        ArrayList<Pair<Vehicle, PriorityQueue<Pair<Float, Pedido>>>> listaVC = new ArrayList<>();
        
        List<Pedido> auxRequest = new ArrayList<>();
        requestListDesdoblado.forEach(r -> {
            auxRequest.add(r);
        });
        
        
        float totalTime=0;
        do {
            
            listaVC.clear();

            vehicleList.forEach(v -> {
                PriorityQueue<Pair<Float, Pedido>> requestListArreange = new PriorityQueue<>(Comparator.comparing(Pair::getKey));
                listaVC.add(new Pair<>(v, requestListArreange));
            });
            
            int colapso;
            // TODO: filtrar los pedidos que no se han registrado hasta ese momento
            for(Pedido req : requestListDesdoblado){
                colapso = 0;
                for(Pair<Vehicle, PriorityQueue<Pair<Float, Pedido>>> lvc : listaVC){
                    Vehicle v = lvc.getKey();
                    PriorityQueue<Pair<Float, Pedido>> requestListArreange = lvc.getValue();
                    float distance = v.getCurrentLocation().getDistancia(req),
                          time = (float)(distance/v.getVelocity()),
                          tiempoLlegada = req.getHoursLimit().getTimeInMillis() - v.getInitDate().getTimeInMillis();
                    
                    if(tiempoLlegada > 0)
                        requestListArreange.add(new Pair<>((tiempoLlegada/time),req));
                    else{
                        System.out.println("idPedido colapsado: " + req.getIdPedido() + "-" + req.getIdDesdoblado());
                        colapso++;
                    }
                }
                if(colapso == listaVC.size()){

                    int totalGLP = 0;
                    for(Pedido r : requestListDesdoblado)
                        totalGLP += r.getQuantityGLP();
                    
                    int pedidoCompletado = 0;
                    for(Pedido rq : requestList){
                        double aux = 0;
                        aux = auxRequest.stream()
                                .filter(auxrq -> auxrq.getIdPedido() == rq.getIdPedido())
                                .map(auxrq -> auxrq.isFlat() ? auxrq.getQuantityGLP() : 0)
                                .reduce(aux, (accumulator, _item) -> accumulator + _item);
                        if(aux == rq.getQuantityGLP())
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
            
            for(Pair<Vehicle, PriorityQueue<Pair<Float, Pedido>>> vc : listaVC){
                //Assign request to vehicles
                vehicleList.clear(); // se puede quitar esta lista intermedia
                vehicleList.add(vc.getKey());
//                System.out.println("PQ: " + vc.getValue());
                int assigned = 0;
                try {
                    assigned += Knapsack.allocate(vc.getValue(), vehicleList, auxRequest); // TODO: refactorizar para no destruir la lista a cada rato
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
            ArrayList<Node> vertices = new ArrayList<>(); // se puede quitar esta lista intermedia
            for(Vehicle v : vehicleList){
                vertices.clear();
                v.getRequestList().forEach( p -> { vertices.add(p); });
                System.out.println(v.getRequestList());
                if(!v.getRequestList().isEmpty())
                    GeneticAlgorithm.GA(v, vertices, map);
                    
            }
            for(Vehicle v : vehicleList){
                if(v.getRuta() != null && !v.getRuta().isEmpty()){
                    v.getInitDate().add(Calendar.MINUTE, Math.round((float) Math.ceil(v.calculateTimeToDispatch())));
                    v.setCurrentLocation(v.getRuta().get(v.getRuta().size()-1));
                    totalTime += v.calculateTimeToDispatch();
                 }
                v.clearVehicle();
            }
            
            vehicleList.sort((v1, v2) -> Long.compare(v1.getInitDate().getTimeInMillis() , v2.getInitDate().getTimeInMillis()));

            List<Pedido> aux = new ArrayList<>();
            requestListDesdoblado.forEach(r -> {
                if(!r.isFlat()) {
                    aux.add(r);
                }
            });
            
            requestListDesdoblado = aux;
            
        } while(!requestListDesdoblado.isEmpty());
        
        System.out.println("Total de tiempo = " + totalTime/60);
        int pedidoCompletado = 0;
        for(Pedido rq : requestList){
            double aux = 0;
            aux = auxRequest.stream()
                    .filter(auxrq -> auxrq.getIdPedido() == rq.getIdPedido())
                    .map(auxrq -> auxrq.isFlat() ? auxrq.getQuantityGLP() : 0)
                    .reduce(aux, (accumulator, _item) -> accumulator + _item);
            if(aux == rq.getQuantityGLP())
                pedidoCompletado++;
        }
                
        System.out.println("Pedidos recibidos = " + requestList.size());
        System.out.println("Pedidos completados = " + pedidoCompletado);
        System.out.println("C logró :D");
        
    }
    
    private static void printCalendar(Calendar cal){
        System.out.println("Año: " + cal.get(Calendar.YEAR) + " - Mes: " + cal.get(Calendar.MONTH) + " - Día: " + cal.get(Calendar.DATE)
                + " - Hora: " + cal.get(Calendar.HOUR) + " - Minuto: " + cal.get(Calendar.MINUTE) + " - Segundo: " + cal.get(Calendar.SECOND));
    }
    
}


