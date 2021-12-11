package pe.edu.pucp.mvc.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.pucp.mvc.models.SimulacionXPedidoModel;
import pe.edu.pucp.mvc.repositories.SimulacionRepository;
import pe.edu.pucp.mvc.repositories.SimulacionXPedidoRepository;

@Service
public class SimulacionXPedidoService {

    @Autowired
    SimulacionXPedidoRepository simulacionXPedidoRepository;
    public SimulacionXPedidoModel guardarSimulacionXPedido(SimulacionXPedidoModel simulacionXPedido) {
        return simulacionXPedidoRepository.save(simulacionXPedido);
    }
}
