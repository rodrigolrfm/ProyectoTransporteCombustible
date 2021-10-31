package pe.edu.pucp.mvc.controllers;


import java.io.*;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class FileController<st>
{
    @RequestMapping(value = "/upload",
            method = RequestMethod.POST,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)

    public String fileUpload(@RequestParam("file") MultipartFile file) throws IOException
    {
        File convertFile = new File("/home/ubuntu/resources/" + file.getOriginalFilename());
        convertFile.createNewFile();

        try (FileOutputStream fout = new FileOutputStream(convertFile))
        {
            fout.write(file.getBytes());
            try{
                BufferedReader br = new BufferedReader(new FileReader(convertFile));
                String st;
                while ((st = br.readLine()) != null){
                    System.out.println(st);
                }
            } catch (Exception ex){
                System.out.println();
            }
            String st;
        }
        catch (Exception exe)
        {
            exe.printStackTrace();
        }
        return "File has uploaded successfully";
    }

}