package pe.edu.pucp.mvc.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.pucp.mvc.models.PedidoModel;
import pe.edu.pucp.mvc.models.SimulacionModel;
import pe.edu.pucp.mvc.repositories.PedidoRepository;
import pe.edu.pucp.mvc.repositories.SimulacionRepository;

@Service
public class SimulacionService {
    @Autowired
    SimulacionRepository simulacionRepository;
    public SimulacionModel guardarSimulacion(SimulacionModel simulacion) {
        return simulacionRepository.save(simulacion);
    }
}
