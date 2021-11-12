
package pe.edu.pucp.mvc.models;


import lombok.*;
import lombok.experimental.SuperBuilder;
import java.util.Date;


@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Block {
    private Date initDateBlocked;
    private Date endDateBlocked;
}
