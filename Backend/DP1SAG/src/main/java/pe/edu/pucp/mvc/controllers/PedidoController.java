package pe.edu.pucp.mvc.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pe.edu.pucp.mvc.dtos.PedidoUniqueDTO;
import pe.edu.pucp.mvc.models.PedidoModel;
import pe.edu.pucp.mvc.services.PedidoService;
import pe.edu.pucp.mvc.services.VehiculoService;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


@RestController
@RequestMapping("/pedido")
public class PedidoController {

    @Autowired
    PedidoService pedidoService;

    @PostMapping(value = "/insertarPedido", consumes = MediaType.APPLICATION_JSON_VALUE)
    public String insertarPedido(@RequestBody PedidoModel pedidoUnique){
        PedidoModel pedido = PedidoModel.builder().coordenadaX(pedidoUnique.getCoordenadaX())
                                .coordenadaY(pedidoUnique.getCoordenadaY())
                                .fechaPedido(pedidoUnique.getFechaPedido())
                                .horasLimite(pedidoUnique.getHorasLimite())
                                .cantidadGLP(pedidoUnique.getCantidadGLP())
                                .build();
        String response;
        pedido.setIdExtendido(0);
        Integer idNodo = pedidoService.getMaxIdNodo();
        pedido.setIdNodo(idNodo + 1);
        try{
            int minimo = 5;
            pedido = pedidoService.guardarPedido(pedido);
            int i = 1;
            PedidoModel pedidoPartido=null;
            for(; i < (int)pedido.getCantidadGLP()/minimo + 1; i++) {
                pedidoPartido = PedidoModel.builder()
                        .idNodo(pedido.getIdNodo())
                        .idExtendido(i)
                        .clienteModel(pedido.getClienteModel())
                        .cantidadGLP(minimo)
                        .coordenadaX(pedido.getCoordenadaX())
                        .coordenadaY(pedido.getCoordenadaY())
                        .fechaPedido(pedido.getFechaPedido())
                        .horasLimite(pedido.getHorasLimite()).build();
                pedidoService.guardarPedido(pedidoPartido);
            }

            if(pedido.getCantidadGLP()%minimo != 0.0) {
                pedidoPartido = PedidoModel.builder()
                        .idNodo(pedido.getIdNodo())
                        .idExtendido(i)
                        .clienteModel(pedido.getClienteModel())
                        .cantidadGLP(pedido.getCantidadGLP() % minimo)
                        .coordenadaX(pedido.getCoordenadaX())
                        .coordenadaY(pedido.getCoordenadaY())
                        .fechaPedido(pedido.getFechaPedido())
                        .horasLimite(pedido.getHorasLimite()).build();
                pedidoService.guardarPedido(pedidoPartido);
            }
            response = "SUCCESS";
        }catch (Exception e){
            response = e.getMessage();
        }
        return response;
    }

    @PostMapping(value = "/cargaMasivaPedidos", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public List<PedidoModel> cargaMasivaNodos(@RequestParam("filear") MultipartFile file) throws IOException {

        int glpTotal = 0;
        List<PedidoModel> listaPedidos = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");

        try {
            //File convertFile = new File("/home/ubuntu/resources/" + file.getOriginalFilename());
            File convertFile = new File("D:\\CICLO10\\Trabajo\\" + file.getOriginalFilename());
            convertFile.createNewFile();
            //FileOutputStream fout = new FileOutputStream(convertFile);
            BufferedReader br = new BufferedReader(new FileReader(convertFile));
            String line;
            String strDate;
            String[] rowRequest, day;
            Date date;

            while ((line = br.readLine()) != null) {
                int minimo = 5;
                int totalCapacity = 0;
                rowRequest = line.split(",");
                day = rowRequest[0].split(":");
                strDate = file.getOriginalFilename().substring(6,10) + "/" + file.getOriginalFilename().substring(10,12) + "/" + day[0] + " " + day[1] + ":" + day[2];
                date = sdf.parse(strDate);

                Calendar cal = Calendar.getInstance(), reqDate = Calendar.getInstance();
                cal.setTime(date);
                reqDate.setTime(date);
                cal.add(Calendar.HOUR, Integer.parseInt(rowRequest[4]));

                glpTotal += Integer.parseInt(rowRequest[3]);
                Integer idNodo = pedidoService.getMaxIdNodo();
                PedidoModel pedido = PedidoModel.builder()
                        .idNodo(idNodo + 1)
                        .idExtendido(0)
                        .coordenadaX(Integer.parseInt(rowRequest[1]))
                        .coordenadaY(Integer.parseInt(rowRequest[2]))
                        .fechaPedido(reqDate)
                        .cantidadGLP(Integer.parseInt(rowRequest[3]))
                        .horasLimite(cal).build();
                listaPedidos.add(pedido);
                pedido = pedidoService.guardarPedido(pedido);
                int i = 1;
                PedidoModel pedidoPartido=null;
                for(; i < (int)pedido.getCantidadGLP()/minimo + 1; i++) {
                    pedidoPartido = PedidoModel.builder()
                            .idNodo(pedido.getIdNodo())
                            .idExtendido(i)
                            .clienteModel(pedido.getClienteModel())
                            .cantidadGLP(minimo)
                            .coordenadaX(pedido.getCoordenadaX())
                            .coordenadaY(pedido.getCoordenadaY())
                            .fechaPedido(pedido.getFechaPedido())
                            .horasLimite(pedido.getHorasLimite()).build();
                    pedidoService.guardarPedido(pedidoPartido);
                }

                if(pedido.getCantidadGLP()%minimo != 0.0) {
                    pedidoPartido = PedidoModel.builder()
                            .idNodo(pedido.getIdNodo())
                            .idExtendido(i)
                            .clienteModel(pedido.getClienteModel())
                            .cantidadGLP(pedido.getCantidadGLP() % minimo)
                            .coordenadaX(pedido.getCoordenadaX())
                            .coordenadaY(pedido.getCoordenadaY())
                            .fechaPedido(pedido.getFechaPedido())
                            .horasLimite(pedido.getHorasLimite()).build();
                    pedidoService.guardarPedido(pedidoPartido);
                }
                totalCapacity += pedido.getCantidadGLP();
            }
        } catch (IOException | ParseException | NumberFormatException e) {
            e.printStackTrace();
        }

        listaPedidos.sort((r1, r2) -> Long.compare(r1.getHorasLimite().getTimeInMillis() , r2.getHorasLimite().getTimeInMillis()));
        return listaPedidos;
    }

    @PostMapping(value = "/listaPedidos")
    public List<PedidoModel> devolverListaPedidos(){
        List<PedidoModel> listaPedidos = new ArrayList<>();
        listaPedidos = pedidoService.listaPedidosSinAtender();
        return listaPedidos;
    }

}