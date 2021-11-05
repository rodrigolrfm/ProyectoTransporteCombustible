package pe.edu.pucp.mvc.controllers;


import java.io.*;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.tomcat.util.json.JSONParser;
import org.json.JSONObject;
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
        JsonObject convertedObject=null;

        try {
            String path = "{ \"paths\": [\n" +
                    " {\"path\":[\n" +
                    " {\"destino\": 0, \"x\": 12, \"y\": 8},\n" +
                    " {\"destino\": 0, \"x\": 13, \"y\": 8},\n" +
                    " {\"destino\": 0, \"x\": 14, \"y\": 8},\n" +
                    " {\"destino\": 0, \"x\": 14, \"y\": 9},\n" +
                    " {\"destino\": 0, \"x\": 15, \"y\": 9},\n" +
                    " {\"destino\": 0, \"x\": 16, \"y\": 9},\n" +
                    " {\"destino\": 0, \"x\": 17, \"y\": 9},\n" +
                    " {\"destino\": 0, \"x\": 17, \"y\": 10},\n" +
                    " {\"destino\": 0, \"x\": 18, \"y\": 10},\n" +
                    " {\"destino\": 0, \"x\": 19, \"y\": 10},\n" +
                    " {\"destino\": 0, \"x\": 20, \"y\": 10},\n" +
                    " {\"destino\": 0, \"x\": 21, \"y\": 10},\n" +
                    " {\"destino\": 0, \"x\": 22, \"y\": 10},\n" +
                    " {\"destino\": 0, \"x\": 23, \"y\": 10},\n" +
                    " {\"destino\": 0, \"x\": 24, \"y\": 10},\n" +
                    " {\"destino\": 0, \"x\": 25, \"y\": 10},\n" +
                    " {\"destino\": 0, \"x\": 26, \"y\": 10},\n" +
                    " {\"destino\": 0, \"x\": 27, \"y\": 10},\n" +
                    " {\"destino\": 0, \"x\": 27, \"y\": 11},\n" +
                    " {\"destino\": 0, \"x\": 28, \"y\": 11},\n" +
                    " {\"destino\": 0, \"x\": 28, \"y\": 12},\n" +
                    " {\"destino\": 0, \"x\": 29, \"y\": 12},\n" +
                    " {\"destino\": 0, \"x\": 30, \"y\": 12},\n" +
                    " {\"destino\": 0, \"x\": 31, \"y\": 12},\n" +
                    " {\"destino\": 0, \"x\": 31, \"y\": 13},\n" +
                    " {\"destino\": 0, \"x\": 32, \"y\": 13},\n" +
                    " {\"destino\": 0, \"x\": 33, \"y\": 13},\n" +
                    " {\"destino\": 0, \"x\": 34, \"y\": 13},\n" +
                    " {\"destino\": 0, \"x\": 35, \"y\": 13},\n" +
                    " {\"destino\": 0, \"x\": 36, \"y\": 13},\n" +
                    " {\"destino\": 0, \"x\": 36, \"y\": 14},\n" +
                    " {\"destino\": 0, \"x\": 36, \"y\": 15},\n" +
                    " {\"destino\": 0, \"x\": 36, \"y\": 16},\n" +
                    " {\"destino\": 0, \"x\": 36, \"y\": 17},\n" +
                    " {\"destino\": 0, \"x\": 37, \"y\": 17},\n" +
                    " {\"destino\": 0, \"x\": 38, \"y\": 17},\n" +
                    " {\"destino\": 0, \"x\": 38, \"y\": 18},\n" +
                    " {\"destino\": 0, \"x\": 38, \"y\": 19},\n" +
                    " {\"destino\": 0, \"x\": 39, \"y\": 19},\n" +
                    " {\"destino\": 0, \"x\": 40, \"y\": 19},\n" +
                    " {\"destino\": 0, \"x\": 40, \"y\": 20},\n" +
                    " {\"destino\": 0, \"x\": 40, \"y\": 21},\n" +
                    " {\"destino\": 0, \"x\": 40, \"y\": 22},\n" +
                    " {\"destino\": 0, \"x\": 40, \"y\": 23},\n" +
                    " {\"destino\": 0, \"x\": 40, \"y\": 24},\n" +
                    " {\"destino\": 1, \"x\": 40, \"y\": 25},\n" +
                    " {\"destino\": 0, \"x\": 40, \"y\": 26},\n" +
                    " {\"destino\": 0, \"x\": 40, \"y\": 27},\n" +
                    " {\"destino\": 0, \"x\": 40, \"y\": 28},\n" +
                    " {\"destino\": 0, \"x\": 40, \"y\": 29},\n" +
                    " {\"destino\": 0, \"x\": 40, \"y\": 30},\n" +
                    " {\"destino\": 0, \"x\": 40, \"y\": 31},\n" +
                    " {\"destino\": 0, \"x\": 40, \"y\": 32},\n" +
                    " {\"destino\": 0, \"x\": 40, \"y\": 33},\n" +
                    " {\"destino\": 0, \"x\": 40, \"y\": 34},\n" +
                    " {\"destino\": 0, \"x\": 40, \"y\": 35},\n" +
                    " {\"destino\": 0, \"x\": 40, \"y\": 36},\n" +
                    " {\"destino\": 0, \"x\": 40, \"y\": 37},\n" +
                    " {\"destino\": 0, \"x\": 41, \"y\": 37},\n" +
                    " {\"destino\": 0, \"x\": 41, \"y\": 38},\n" +
                    " {\"destino\": 0, \"x\": 41, \"y\": 39},\n" +
                    " {\"destino\": 0, \"x\": 41, \"y\": 40},\n" +
                    " {\"destino\": 0, \"x\": 41, \"y\": 41},\n" +
                    " {\"destino\": 0, \"x\": 41, \"y\": 42},\n" +
                    " {\"destino\": 0, \"x\": 42, \"y\": 42}\n" +
                    " ],\n" +
                    " \"startTime\": \"2021-11-05T00:33:51.969+00:00\",\n" +
                    " \"endTime\": \"2021-11-05T01:05:51.969+00:00\"},\n" +
                    " {\"path\":[\n" +
                    " {\"destino\": 0, \"x\": 12, \"y\": 8},\n" +
                    " {\"destino\": 0, \"x\": 12, \"y\": 9},\n" +
                    " {\"destino\": 0, \"x\": 12, \"y\": 10},\n" +
                    " {\"destino\": 0, \"x\": 12, \"y\": 11},\n" +
                    " {\"destino\": 0, \"x\": 12, \"y\": 12},\n" +
                    " {\"destino\": 0, \"x\": 13, \"y\": 12},\n" +
                    " {\"destino\": 0, \"x\": 13, \"y\": 13},\n" +
                    " {\"destino\": 0, \"x\": 13, \"y\": 14},\n" +
                    " {\"destino\": 0, \"x\": 13, \"y\": 15},\n" +
                    " {\"destino\": 0, \"x\": 13, \"y\": 16},\n" +
                    " {\"destino\": 0, \"x\": 13, \"y\": 17},\n" +
                    " {\"destino\": 0, \"x\": 13, \"y\": 18},\n" +
                    " {\"destino\": 0, \"x\": 13, \"y\": 19},\n" +
                    " {\"destino\": 0, \"x\": 13, \"y\": 20},\n" +
                    " {\"destino\": 0, \"x\": 13, \"y\": 21},\n" +
                    " {\"destino\": 0, \"x\": 13, \"y\": 22},\n" +
                    " {\"destino\": 0, \"x\": 13, \"y\": 23},\n" +
                    " {\"destino\": 0, \"x\": 13, \"y\": 24},\n" +
                    " {\"destino\": 0, \"x\": 14, \"y\": 24},\n" +
                    " {\"destino\": 0, \"x\": 14, \"y\": 25},\n" +
                    " {\"destino\": 0, \"x\": 15, \"y\": 25},\n" +
                    " {\"destino\": 0, \"x\": 16, \"y\": 25},\n" +
                    " {\"destino\": 0, \"x\": 16, \"y\": 26},\n" +
                    " {\"destino\": 0, \"x\": 16, \"y\": 27},\n" +
                    " {\"destino\": 0, \"x\": 17, \"y\": 27},\n" +
                    " {\"destino\": 0, \"x\": 18, \"y\": 27},\n" +
                    " {\"destino\": 0, \"x\": 19, \"y\": 27},\n" +
                    " {\"destino\": 0, \"x\": 20, \"y\": 27},\n" +
                    " {\"destino\": 0, \"x\": 21, \"y\": 27},\n" +
                    " {\"destino\": 0, \"x\": 21, \"y\": 28},\n" +
                    " {\"destino\": 0, \"x\": 22, \"y\": 28},\n" +
                    " {\"destino\": 0, \"x\": 22, \"y\": 29},\n" +
                    " {\"destino\": 0, \"x\": 22, \"y\": 30},\n" +
                    " {\"destino\": 0, \"x\": 22, \"y\": 31},\n" +
                    " {\"destino\": 0, \"x\": 22, \"y\": 32},\n" +
                    " {\"destino\": 0, \"x\": 22, \"y\": 33},\n" +
                    " {\"destino\": 0, \"x\": 22, \"y\": 34},\n" +
                    " {\"destino\": 0, \"x\": 22, \"y\": 35},\n" +
                    " {\"destino\": 0, \"x\": 23, \"y\": 35},\n" +
                    " {\"destino\": 0, \"x\": 24, \"y\": 35},\n" +
                    " {\"destino\": 1, \"x\": 25, \"y\": 35},\n" +
                    " {\"destino\": 0, \"x\": 25, \"y\": 34},\n" +
                    " {\"destino\": 0, \"x\": 25, \"y\": 33},\n" +
                    " {\"destino\": 0, \"x\": 25, \"y\": 32},\n" +
                    " {\"destino\": 0, \"x\": 25, \"y\": 31},\n" +
                    " {\"destino\": 0, \"x\": 25, \"y\": 30},\n" +
                    " {\"destino\": 0, \"x\": 25, \"y\": 29},\n" +
                    " {\"destino\": 0, \"x\": 25, \"y\": 28},\n" +
                    " {\"destino\": 0, \"x\": 25, \"y\": 27},\n" +
                    " {\"destino\": 0, \"x\": 25, \"y\": 26},\n" +
                    " {\"destino\": 0, \"x\": 25, \"y\": 25},\n" +
                    " {\"destino\": 0, \"x\": 26, \"y\": 25},\n" +
                    " {\"destino\": 0, \"x\": 27, \"y\": 25},\n" +
                    " {\"destino\": 0, \"x\": 28, \"y\": 25},\n" +
                    " {\"destino\": 0, \"x\": 29, \"y\": 25},\n" +
                    " {\"destino\": 1, \"x\": 30, \"y\": 25},\n" +
                    " {\"destino\": 0, \"x\": 31, \"y\": 25},\n" +
                    " {\"destino\": 0, \"x\": 32, \"y\": 25},\n" +
                    " {\"destino\": 0, \"x\": 33, \"y\": 25},\n" +
                    " {\"destino\": 0, \"x\": 34, \"y\": 25},\n" +
                    " {\"destino\": 0, \"x\": 35, \"y\": 25},\n" +
                    " {\"destino\": 0, \"x\": 36, \"y\": 25},\n" +
                    " {\"destino\": 0, \"x\": 37, \"y\": 25},\n" +
                    " {\"destino\": 0, \"x\": 38, \"y\": 25},\n" +
                    " {\"destino\": 0, \"x\": 39, \"y\": 25},\n" +
                    " {\"destino\": 1, \"x\": 40, \"y\": 25},\n" +
                    " {\"destino\": 0, \"x\": 40, \"y\": 26},\n" +
                    " {\"destino\": 0, \"x\": 40, \"y\": 27},\n" +
                    " {\"destino\": 0, \"x\": 40, \"y\": 28},\n" +
                    " {\"destino\": 0, \"x\": 40, \"y\": 29},\n" +
                    " {\"destino\": 0, \"x\": 40, \"y\": 30},\n" +
                    " {\"destino\": 0, \"x\": 40, \"y\": 31},\n" +
                    " {\"destino\": 0, \"x\": 40, \"y\": 32},\n" +
                    " {\"destino\": 0, \"x\": 40, \"y\": 33},\n" +
                    " {\"destino\": 0, \"x\": 40, \"y\": 34},\n" +
                    " {\"destino\": 0, \"x\": 40, \"y\": 35},\n" +
                    " {\"destino\": 0, \"x\": 40, \"y\": 36},\n" +
                    " {\"destino\": 0, \"x\": 40, \"y\": 37},\n" +
                    " {\"destino\": 0, \"x\": 41, \"y\": 37},\n" +
                    " {\"destino\": 0, \"x\": 41, \"y\": 38},\n" +
                    " {\"destino\": 0, \"x\": 41, \"y\": 39},\n" +
                    " {\"destino\": 0, \"x\": 41, \"y\": 40},\n" +
                    " {\"destino\": 0, \"x\": 41, \"y\": 41},\n" +
                    " {\"destino\": 0, \"x\": 41, \"y\": 42},\n" +
                    " {\"destino\": 0, \"x\": 42, \"y\": 42}\n" +
                    " ],\n" +
                    " \"startTime\": \"2021-11-05T01:33:51.969+00:00\",\n" +
                    " \"endTime\": \"2021-11-05T01:05:51.969+00:00\"},\n" +
                    " {\"path\":[\n" +
                    " {\"destino\": 0, \"x\": 12, \"y\": 8},\n" +
                    " {\"destino\": 0, \"x\": 13, \"y\": 8},\n" +
                    " {\"destino\": 0, \"x\": 14, \"y\": 8},\n" +
                    " {\"destino\": 0, \"x\": 14, \"y\": 9},\n" +
                    " {\"destino\": 0, \"x\": 15, \"y\": 9},\n" +
                    " {\"destino\": 0, \"x\": 16, \"y\": 9},\n" +
                    " {\"destino\": 0, \"x\": 17, \"y\": 9},\n" +
                    " {\"destino\": 0, \"x\": 17, \"y\": 10},\n" +
                    " {\"destino\": 0, \"x\": 18, \"y\": 10},\n" +
                    " {\"destino\": 0, \"x\": 19, \"y\": 10},\n" +
                    " {\"destino\": 0, \"x\": 20, \"y\": 10},\n" +
                    " {\"destino\": 0, \"x\": 21, \"y\": 10},\n" +
                    " {\"destino\": 0, \"x\": 22, \"y\": 10},\n" +
                    " {\"destino\": 0, \"x\": 23, \"y\": 10},\n" +
                    " {\"destino\": 0, \"x\": 24, \"y\": 10},\n" +
                    " {\"destino\": 0, \"x\": 25, \"y\": 10},\n" +
                    " {\"destino\": 0, \"x\": 26, \"y\": 10},\n" +
                    " {\"destino\": 0, \"x\": 27, \"y\": 10},\n" +
                    " {\"destino\": 0, \"x\": 27, \"y\": 11},\n" +
                    " {\"destino\": 0, \"x\": 28, \"y\": 11},\n" +
                    " {\"destino\": 0, \"x\": 28, \"y\": 12},\n" +
                    " {\"destino\": 0, \"x\": 29, \"y\": 12},\n" +
                    " {\"destino\": 0, \"x\": 30, \"y\": 12},\n" +
                    " {\"destino\": 0, \"x\": 31, \"y\": 12},\n" +
                    " {\"destino\": 0, \"x\": 31, \"y\": 13},\n" +
                    " {\"destino\": 0, \"x\": 32, \"y\": 13},\n" +
                    " {\"destino\": 0, \"x\": 33, \"y\": 13},\n" +
                    " {\"destino\": 0, \"x\": 34, \"y\": 13},\n" +
                    " {\"destino\": 0, \"x\": 35, \"y\": 13},\n" +
                    " {\"destino\": 0, \"x\": 36, \"y\": 13},\n" +
                    " {\"destino\": 0, \"x\": 36, \"y\": 14},\n" +
                    " {\"destino\": 0, \"x\": 36, \"y\": 15},\n" +
                    " {\"destino\": 0, \"x\": 36, \"y\": 16},\n" +
                    " {\"destino\": 0, \"x\": 36, \"y\": 17},\n" +
                    " {\"destino\": 0, \"x\": 37, \"y\": 17},\n" +
                    " {\"destino\": 0, \"x\": 38, \"y\": 17},\n" +
                    " {\"destino\": 0, \"x\": 38, \"y\": 18},\n" +
                    " {\"destino\": 0, \"x\": 38, \"y\": 19},\n" +
                    " {\"destino\": 0, \"x\": 39, \"y\": 19},\n" +
                    " {\"destino\": 0, \"x\": 40, \"y\": 19},\n" +
                    " {\"destino\": 0, \"x\": 40, \"y\": 20},\n" +
                    " {\"destino\": 0, \"x\": 40, \"y\": 21},\n" +
                    " {\"destino\": 0, \"x\": 40, \"y\": 22},\n" +
                    " {\"destino\": 0, \"x\": 40, \"y\": 23},\n" +
                    " {\"destino\": 0, \"x\": 40, \"y\": 24},\n" +
                    " {\"destino\": 1, \"x\": 40, \"y\": 25},\n" +
                    " {\"destino\": 0, \"x\": 40, \"y\": 26},\n" +
                    " {\"destino\": 0, \"x\": 40, \"y\": 27},\n" +
                    " {\"destino\": 0, \"x\": 40, \"y\": 28},\n" +
                    " {\"destino\": 0, \"x\": 40, \"y\": 29},\n" +
                    " {\"destino\": 0, \"x\": 40, \"y\": 30},\n" +
                    " {\"destino\": 0, \"x\": 40, \"y\": 31},\n" +
                    " {\"destino\": 0, \"x\": 40, \"y\": 32},\n" +
                    " {\"destino\": 0, \"x\": 40, \"y\": 33},\n" +
                    " {\"destino\": 0, \"x\": 40, \"y\": 34},\n" +
                    " {\"destino\": 0, \"x\": 40, \"y\": 35},\n" +
                    " {\"destino\": 0, \"x\": 40, \"y\": 36},\n" +
                    " {\"destino\": 0, \"x\": 40, \"y\": 37},\n" +
                    " {\"destino\": 0, \"x\": 41, \"y\": 37},\n" +
                    " {\"destino\": 0, \"x\": 41, \"y\": 38},\n" +
                    " {\"destino\": 0, \"x\": 41, \"y\": 39},\n" +
                    " {\"destino\": 0, \"x\": 41, \"y\": 40},\n" +
                    " {\"destino\": 0, \"x\": 41, \"y\": 41},\n" +
                    " {\"destino\": 0, \"x\": 41, \"y\": 42},\n" +
                    " {\"destino\": 0, \"x\": 42, \"y\": 42}\n" +
                    " ],\n" +
                    " \"startTime\": \"2021-11-05T02:33:51.969+00:00\",\n" +
                    " \"endTime\": \"2021-11-05T01:05:51.969+00:00\"}] \n" +
                    "}\n";
            convertedObject = new Gson().fromJson(path, JsonObject.class);

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