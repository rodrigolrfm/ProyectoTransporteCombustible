package pe.edu.pucp.mvc.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
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

    @PostMapping(value = "/cargaMasivaPedidos", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public List<PedidoModel> cargaMasivaNodos(@RequestParam("filear") MultipartFile file) throws IOException {

        int glpTotal = 0;
        List<PedidoModel> listaPedidos = new ArrayList<PedidoModel>();
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

            int num = 0;
            while ((line = br.readLine()) != null) {
                rowRequest = line.split(",");
                day = rowRequest[0].split(":");
                strDate = file.getOriginalFilename().substring(6,10) + "/" + file.getOriginalFilename().substring(10,12) + "/" + day[0] + " " + day[1] + ":" + day[2];
                date = sdf.parse(strDate);

                Calendar cal = Calendar.getInstance(), reqDate = Calendar.getInstance();
                cal.setTime(date);
                reqDate.setTime(date);
                cal.add(Calendar.HOUR, Integer.parseInt(rowRequest[4]));

                glpTotal += Integer.parseInt(rowRequest[3]);

                PedidoModel pedido = PedidoModel.builder()
                        .idNodo(num++)
                        .coordenadaX(Integer.parseInt(rowRequest[1]))
                        .coordenadaY(Integer.parseInt(rowRequest[2]))
                        .fechaPedido(reqDate)
                        .cantidadGLP(Integer.parseInt(rowRequest[3]))
                        .horasLimite(cal).build();
                listaPedidos.add(pedido);
                pedidoService.guardarPedido(pedido);

            }
        } catch (IOException | ParseException | NumberFormatException e) {
            e.printStackTrace();
        }

        listaPedidos.sort((r1, r2) -> Long.compare(r1.getHorasLimite().getTimeInMillis() , r2.getHorasLimite().getTimeInMillis()));
        return listaPedidos;
    }
}
