package pe.edu.pucp.mvc.controllers;


import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.tomcat.util.json.JSONParser;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pe.edu.pucp.mvc.dtos.NodoDTO;
import pe.edu.pucp.mvc.dtos.PedidosRutasDTO;
import pe.edu.pucp.mvc.models.NodoModel;

@RestController
@RequestMapping( "/archivos")
public class FileController<st>
{

    @PostMapping("/simularRutas")
    public JsonObject simular(){
        PedidosRutasDTO rutasPedidos1 = new PedidosRutasDTO();
        PedidosRutasDTO rutasPedidos2 = new PedidosRutasDTO();
        PedidosRutasDTO rutasPedidos3 = new PedidosRutasDTO();
        JsonObject convertedObject = null;
        String line = null;
        String total = null;
        try {
            File file = new File( "D:\\CICLO10\\Trabajo\\Grupo2\\Download\\json2rutas.txt");
            String path = "D:\\CICLO10\\Trabajo\\Grupo2\\Download\\json2rutas.txt";
            BufferedReader br= new BufferedReader(new FileReader(file));
            while ((line = br.readLine()) != null) {
                total = line;
            }
            total = "";
            System.out.println("ruta"+ total);
            //String texto = new String(Files.readAllBytes(Paths.get("jsonRutas.txt")))
            convertedObject = new Gson().fromJson(total, JsonObject.class);

        } catch (Exception ex){
            System.out.println(ex.getMessage());
        }
        return convertedObject;
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String fileUpload(@RequestParam("file") MultipartFile file) throws IOException
    {
        File convertFile = new File("/home/ubuntu/resources/" + file.getOriginalFilename());
        //File convertFile = new File("D:/work/" + file.getOriginalFilename());
        convertFile.createNewFile();

        try (FileOutputStream fout = new FileOutputStream(convertFile))
        {
            fout.write(file.getBytes());
            try{
                BufferedReader br = new BufferedReader(new FileReader(convertFile));
                String st;
                while ((st = br.readLine()) != null){
                    System.out.println(st);
                }
            } catch (Exception ex){
                System.out.println();
            }
            String st;
        }
        catch (Exception exe)
        {
            exe.printStackTrace();
        }
        return "File has uploaded successfully";
    }

}