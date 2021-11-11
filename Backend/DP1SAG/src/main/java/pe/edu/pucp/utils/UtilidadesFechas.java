package pe.edu.pucp.utils;

import java.util.Date;
import java.time.ZoneId;
import java.time.LocalDateTime;

public class UtilidadesFechas {
    public static Date convertToDateViaInstant(LocalDateTime dateToConvert) {
        return Date.from(dateToConvert.atZone(ZoneId.systemDefault()).toInstant());
    }
}
