package pe.edu.pucp.mvc.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import pe.edu.pucp.mvc.models.BloqueModel;
import pe.edu.pucp.mvc.models.NodoModel;
import pe.edu.pucp.mvc.services.BloqueoService;
import pe.edu.pucp.mvc.services.VehiculoService;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

@RestController
@RequestMapping("/bloqueo")
public class BloqueoController {

    @Autowired
    BloqueoService bloqueoService;

    @PostMapping(value = "/cargaMasivaBloqueos", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void cargaMasivaBloqueo(@RequestParam("file") MultipartFile file) throws IOException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");

        ArrayList<NodoModel> blockedList = new ArrayList<>();
        try{
            File convertFile = new File("/home/ubuntu/resources/" + file.getOriginalFilename());
            final BufferedReader br = new BufferedReader(new FileReader(convertFile));

            String line;
            String strInitDate,strEndDate;
            String[] rowRequest, splitStrDate,initSplitDate,endSplitDate;
            Date initDate,endDate;
            while ((line = br.readLine()) != null) {
                rowRequest = line.split(",");
                splitStrDate = rowRequest[0].split("-");
                initSplitDate = splitStrDate[0].split(":"); //dd hh mm
                endSplitDate = splitStrDate[1].split(":");
                strInitDate = file.getName().substring(0,4) + "/" +
                        file.getName().substring(4,6) + "/" +
                        initSplitDate[0]+ " " + initSplitDate[1] + ":" + initSplitDate[2];
                strEndDate = file.getName().substring(0,4) + "/" +
                        file.getName().substring(4,6) + "/" +
                        endSplitDate[0]+ " " + endSplitDate[1] + ":" + endSplitDate[2];
                initDate = sdf.parse(strInitDate);
                endDate = sdf.parse(strEndDate);
                for(int i = 1; i<rowRequest.length; i+=2){
                    NodoModel nodo = null;
                    // Se crea la clase bloqueo
                    BloqueModel block = BloqueModel.builder().inicioBloqueo(initDate).finBloqueo(endDate).build();
                    try{
                        int x = Integer.parseInt(rowRequest[i+2]);
                        int y = Integer.parseInt(rowRequest[i+3]);
                    }catch (IndexOutOfBoundsException e){
                        nodo = NodoModel.builder().inicioBloqueo(initDate).finBloqueo(endDate).estaBloqueado(true)
                                .coordenadaX(Integer.parseInt(rowRequest[i]))
                                .coordenadaY(Integer.parseInt(rowRequest[i+1])).build();
                        //Búsqueda del Nodo
                        nodo.getBlockList().add(block);

                        //Agregar bloqueo en la base de datos
                        blockedList.add(nodo);

                        break;
                    }
                    int canty=0;
                    int cantx=0;

                    if(rowRequest[i].equals(rowRequest[i + 2])){
                        canty=Integer.parseInt(rowRequest[i+3]) - Integer.parseInt(rowRequest[i+1]);
                    }else{
                        cantx=Integer.parseInt(rowRequest[i+2]) - Integer.parseInt(rowRequest[i]);
                    }

                    if(canty>0){
                        for(int j =0;j<canty;j++){
                            nodo = NodoModel.builder().inicioBloqueo(initDate).finBloqueo(endDate).estaBloqueado(true)
                                    .coordenadaX(Integer.parseInt(rowRequest[i]))
                                    .coordenadaY(Integer.parseInt(rowRequest[i+1])+j).build();
                            nodo.getBlockList().add(block);
                            blockedList.add(nodo);
                        }

                    }else{
                        for(int j =0;j<Math.abs(canty);j++){
                            nodo = NodoModel.builder().inicioBloqueo(initDate).finBloqueo(endDate).estaBloqueado(true)
                                    .coordenadaX(Integer.parseInt(rowRequest[i]))
                                    .coordenadaY(Integer.parseInt(rowRequest[i+1])-j).build();
                            nodo.getBlockList().add(block);
                            blockedList.add(nodo);
                        }
                    }
                    if(cantx>0){
                        for(int j =0;j<cantx;j++){
                            nodo = NodoModel.builder().inicioBloqueo(initDate).finBloqueo(endDate).estaBloqueado(true)
                                    .coordenadaX(Integer.parseInt(rowRequest[i])+j)
                                    .coordenadaY(Integer.parseInt(rowRequest[i+1])).build();
                            nodo.getBlockList().add(block);
                            blockedList.add(nodo);
                        }
                    }else{
                        for(int j =0 ;j<Math.abs(cantx);j++) {
                            nodo = NodoModel.builder().inicioBloqueo(initDate).finBloqueo(endDate).estaBloqueado(true)
                                    .coordenadaX(Integer.parseInt(rowRequest[i]) - j)
                                    .coordenadaY(Integer.parseInt(rowRequest[i + 1])).build();
                            nodo.getBlockList().add(block);
                            blockedList.add(nodo);
                            bloqueoService.guardarBloqueo(block);
                        }
                    }

                }
            }
        } catch (IOException | ParseException | NumberFormatException e) {
            e.printStackTrace();
        }

    }


}
