package pe.edu.pucp.mvc.planificacion;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import pe.edu.pucp.mvc.controllers.MapaModel;
import pe.edu.pucp.mvc.models.*;
import pe.edu.pucp.mvc.services.BloqueoService;
import pe.edu.pucp.mvc.services.PedidoService;
import pe.edu.pucp.mvc.services.VehiculoService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ControlTarea {
    private String controlCron;
    private String tipoAction;
    private String datos;
    private Date fechaInicio;
    private Date fechaFin;
    private String fecha;

    @Builder.Default
    private int intervaloHoras = 3;

    private List<PedidoModel> listaPedidos;

    public List<VehiculoModel> vehiculoModels;

    @Builder.Default
    private List<EntidadVehiculo> listaVehiculos = new ArrayList<>();

    @Builder.Default
    private List<NodoModel> blockList = new ArrayList<>();

    private List<BloqueoModel> bloqueos;
    @Builder.Default
    private List<PedidoModel> listaDesdoblado = new ArrayList<>();
    private MapaModel mapaModel= null;
    private String ID;
    private int capacidadTotal = 0;
    private int timeAdicional = 2;
    private Date fechaReferencial;

    private PedidoService pedidoService;

    private VehiculoService vehiculoService;

    private BloqueoService bloqueoService;
}
