package pe.edu.pucp.mvc.controllers;
/*
import net.bytebuddy.dynamic.DynamicType;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pe.edu.pucp.mvc.dtos.NodoDTO;
import pe.edu.pucp.mvc.models.NodoModel;
import pe.edu.pucp.mvc.models.PedidoModel;
import pe.edu.pucp.mvc.services.NodoService;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/nodo")
public class NodoController {
    @Autowired
    NodoService nodoService;

    @GetMapping()
    public List<NodoModel> obtenerNodos(){
        return nodoService.obtenerNodos();
    }

    @PostMapping()
    public NodoModel guardarNodo(@RequestBody NodoDTO nodo){

        NodoModel nodoModel = new NodoModel();
        nodoModel.setCoordenadaX(nodo.getCoordenadax());
        nodoModel.setCoordenadaY(nodo.getCoordenaday());
        return this.nodoService.guardarNodo(nodoModel,nodo.getId_mapa());

    }

    @GetMapping(path="/{id}")
    public Optional<NodoModel> obtenerNodoId(@PathVariable("id") Integer id){
        return this.nodoService.obtenerPorId(id);
    }

}
*/