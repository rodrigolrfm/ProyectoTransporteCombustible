package pe.edu.pucp.utils;

import pe.edu.pucp.algorithm.Node;
import pe.edu.pucp.mvc.models.Block;

import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.IOException;
import java.io.BufferedReader;
import java.util.ArrayList;

import java.text.ParseException;



public class BlockingReader {
    public static ArrayList<Node> TxtReader(String path) throws Exception{
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        ArrayList<Node> blockedList = new ArrayList<>();
        try{
            File file = new File(path);
            final BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            String strInitDate,strEndDate;
            String[] rowRequest, splitStrDate,initSplitDate,endSplitDate;
            Date initDate,endDate;
            while ((line = br.readLine()) != null) {
                rowRequest = line.split(",");
                splitStrDate = rowRequest[0].split("-");
                initSplitDate = splitStrDate[0].split(":"); //dd hh mm
                endSplitDate = splitStrDate[1].split(":");
                strInitDate = file.getName().substring(0,4) + "/" +
                        file.getName().substring(4,6) + "/" +
                        initSplitDate[0]+ " " + initSplitDate[1] + ":" + initSplitDate[2];
                strEndDate = file.getName().substring(0,4) + "/" +
                        file.getName().substring(4,6) + "/" +
                        endSplitDate[0]+ " " + endSplitDate[1] + ":" + endSplitDate[2];
                initDate = sdf.parse(strInitDate);
                endDate = sdf.parse(strEndDate);
                for(int i = 1; i<rowRequest.length; i+=2){
                    Node p = null;
                    Block block = Block.builder().initDateBlocked(initDate).endDateBlocked(endDate).build();
                    try{
                        int x = Integer.parseInt(rowRequest[i+2]);
                        int y = Integer.parseInt(rowRequest[i+3]);
                    }catch (IndexOutOfBoundsException e){
                        p = Node.builder().initDateBlocked(initDate).endDateBlocked(endDate).blocked(true)
                                .coordX(Integer.parseInt(rowRequest[i]))
                                .coordY(Integer.parseInt(rowRequest[i+1])).build();
                        p.getBlockList().add(block);
                        blockedList.add(p);
                        break;
                    }
                    int canty=0;
                    int cantx=0;

                    if(rowRequest[i].equals(rowRequest[i + 2])){
                        canty=Integer.parseInt(rowRequest[i+3]) - Integer.parseInt(rowRequest[i+1]);
                    }else{
                        cantx=Integer.parseInt(rowRequest[i+2]) - Integer.parseInt(rowRequest[i]);
                    }

                    if(canty>0){
                        for(int j =0;j<canty;j++){
                            p = Node.builder().initDateBlocked(initDate).endDateBlocked(endDate).blocked(true)
                                    .coordX(Integer.parseInt(rowRequest[i]))
                                    .coordY(Integer.parseInt(rowRequest[i+1])+j).build();
                            p.getBlockList().add(block);
                            blockedList.add(p);
                        }

                    }else{
                        for(int j =0;j<Math.abs(canty);j++){
                            p = Node.builder().initDateBlocked(initDate).endDateBlocked(endDate).blocked(true)
                                    .coordX(Integer.parseInt(rowRequest[i]))
                                    .coordY(Integer.parseInt(rowRequest[i+1])-j).build();
                            p.getBlockList().add(block);
                            blockedList.add(p);
                        }
                    }
                    if(cantx>0){
                        for(int j =0;j<cantx;j++){
                            p = Node.builder().initDateBlocked(initDate).endDateBlocked(endDate).blocked(true)
                                    .coordX(Integer.parseInt(rowRequest[i])+j)
                                    .coordY(Integer.parseInt(rowRequest[i+1])).build();
                            p.getBlockList().add(block);
                            blockedList.add(p);
                        }
                    }else{
                        for(int j =0 ;j<Math.abs(cantx);j++) {
                            p = Node.builder().initDateBlocked(initDate).endDateBlocked(endDate).blocked(true)
                                    .coordX(Integer.parseInt(rowRequest[i]) - j)
                                    .coordY(Integer.parseInt(rowRequest[i + 1])).build();
                            p.getBlockList().add(block);
                            blockedList.add(p);
                        }
                    }

                }
            }
        } catch (IOException | ParseException | NumberFormatException e) {
            e.printStackTrace();
        }
        return blockedList;
    }
}
