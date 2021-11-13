package pe.edu.pucp.utils;
import pe.edu.pucp.mvc.models.Pedido;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.io.*;

public class Lectura {
    public static List<Pedido> TxtReader(String path) throws IOException , Exception{
        List<Pedido> requestList = new ArrayList<Pedido>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        int glpTotal = 0;
        try {
            File file = new File(path);
            final BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            String strDate;
            String[] rowRequest, day;
            Date date;
            
            int num = 0;
            while ((line = br.readLine()) != null) {
                rowRequest = line.split(",");
                day = rowRequest[0].split(":");
                strDate = file.getName().substring(6,10) + "/" + file.getName().substring(10,12) + "/" + day[0] + " " + day[1] + ":" + day[2];
                date = sdf.parse(strDate);
                
                Calendar cal = Calendar.getInstance(), reqDate = Calendar.getInstance();
                cal.setTime(date);
                reqDate.setTime(date);
                cal.add(Calendar.HOUR, Integer.parseInt(rowRequest[4]));
                
                glpTotal += Integer.parseInt(rowRequest[3]);
                
                Pedido r = Pedido.builder()
                        .idPedido(num++)
                        .coordX(Integer.parseInt(rowRequest[1]))
                        .coordY(Integer.parseInt(rowRequest[2]))
                        .orderDate(reqDate)
                        .quantityGLP(Integer.parseInt(rowRequest[3]))
                        .hoursLimit(cal).build();
                requestList.add(r);
                
            }
        } catch (IOException | ParseException | NumberFormatException e) {
            e.printStackTrace();
        }
        

        
        requestList.sort((r1, r2) -> Long.compare(r1.getHoursLimit().getTimeInMillis() , r2.getHoursLimit().getTimeInMillis()));
        
        
        return requestList;
    }

}
