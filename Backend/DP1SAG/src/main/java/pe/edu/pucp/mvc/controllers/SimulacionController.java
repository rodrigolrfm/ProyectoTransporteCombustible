package pe.edu.pucp.mvc.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import pe.edu.pucp.mvc.models.PedidoModel;
import pe.edu.pucp.mvc.services.SimulacionService;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class SimulacionController {
    @Autowired
    SimulacionService simulacionService;

    @PostMapping(value = "/cargarPedidosSimulacion3dias", consumes = {"multipart/form-data"})
    public String cargarPedidosSimulacion3dias(@RequestParam("simulacion3dias") MultipartFile file) throws IOException {
        return null;
    }
}
