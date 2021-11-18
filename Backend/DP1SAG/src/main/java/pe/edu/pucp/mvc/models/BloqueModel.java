package pe.edu.pucp.mvc.models;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class BloqueModel implements Serializable {

    private Date inicioBloqueo;

    private Date finBloqueo;
}
