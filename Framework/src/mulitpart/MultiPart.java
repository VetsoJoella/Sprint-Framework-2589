package mulitpart;

import jakarta.servlet.http.Part;
import java.io.InputStream;
import java.io.ByteArrayOutputStream ;
import java.io.File ;
import java.io.FileOutputStream ;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;

public class MultiPart {
    
    String name ;               // name dans la form
    String realName ;           // nom de fichier en soi
    InputStream inputStream ; 

    public String getName() {
        return this.name;
    }

    public void setName(String Name) {
        this.name = Name;
    }

    public byte[] getBytesContent() throws Exception{
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024]; 

        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            byteArrayOutputStream.write(buffer, 0, bytesRead);
        }

        inputStream.close();
        return buffer ;
    }

    public String getRealName() {
        return this.realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public InputStream getInputStream() {
        return this.inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }



    public MultiPart() {}

    public MultiPart(String name, String realName, InputStream inputStream) {
        setName(name);
        setRealName(realName);
        setInputStream(inputStream);
    }

    public MultiPart(Part part) throws Exception{

        setName(part.getSubmittedFileName());
        setName(part.getName());
        setRealName(part.getSubmittedFileName());
        setInputStream(part.getInputStream());
    }
    
    
    public MultiPart(Object object) throws Exception{

        if(object instanceof Part) {
            Part part = (Part)object ; 
            setName(part.getName());
            setRealName(part.getSubmittedFileName());
            setInputStream(part.getInputStream());
        } else {
            throw new IllegalArgumentException("L'objet ne peut pas être cast en MultiPart");
        }
    
    }

    public void saveAsFile(String path) throws IOException {
        Path targetFile = Paths.get(path);
        System.out.println("targetFile "+targetFile.toString());
        Files.createDirectories(targetFile.getParent());
        // Files.createFile(targetFile);
        Files.copy(inputStream, targetFile);
    }

}
