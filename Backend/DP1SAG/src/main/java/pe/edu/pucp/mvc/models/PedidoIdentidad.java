package pe.edu.pucp.mvc.models;

import org.springframework.data.annotation.Id;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.IdClass;
import java.io.Serializable;

public class PedidoIdentidad implements Serializable {

    private int idExtendido;
    private int idNodo;

}
