
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class Cisterna {
    public int capacity;
    public Node start;
    public Node end;

    public Cisterna(int capacity, Node start, Node end) {
        this.capacity = capacity;
        this.start = start;
        this.end = end;
    }

    public static ArrayList<Cisterna> cargarFlota(String fileName){
        ArrayList<Cisterna> cisternas = new ArrayList<>();
        try {
            try (BufferedReader bf = new BufferedReader(new FileReader(fileName))) {
                String linea = bf.readLine();

                while (linea != null) {
                    String[] lineaDividida = linea.split(",");

                    for (int i = 0; i < lineaDividida.length; i += 5) {
                        int capacidad = Integer.parseInt(lineaDividida[i]);
                        int inicioX = Integer.parseInt(lineaDividida[i+1]);
                        int inicioY = Integer.parseInt(lineaDividida[i+2]);
                        int finX = Integer.parseInt(lineaDividida[i+3]);
                        int finY = Integer.parseInt(lineaDividida[i+4]);
                        Node inicio = new Node(inicioX, inicioY);
                        Node fin = new Node(finX, finY);
                        Cisterna cisterna = new Cisterna(capacidad, inicio, fin);
                        cisternas.add(cisterna);
                    }
                    linea = bf.readLine();
                }
            }

        } catch (Exception e) {
            System.out.println("Error en leer");

        }

        return cisternas;
    }
}
