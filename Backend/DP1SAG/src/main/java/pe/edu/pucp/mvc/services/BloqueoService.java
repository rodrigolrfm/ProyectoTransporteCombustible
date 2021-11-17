package pe.edu.pucp.mvc.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.pucp.mvc.models.BloqueModel;
import pe.edu.pucp.mvc.models.VehiculoModel;
import pe.edu.pucp.mvc.repositories.BloqueoRepository;
import pe.edu.pucp.mvc.repositories.VehiculoRepository;

@Service
public class BloqueoService {
    @Autowired
    BloqueoRepository bloqueoRepository;

    public BloqueModel guardarBloqueo(BloqueModel bloqueo) {
        return bloqueoRepository.save(bloqueo);
    }

}
