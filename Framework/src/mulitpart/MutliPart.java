package mulitpart;

import jakarta.servlet.http.Part;
import java.io.InputStream;
import java.io.ByteArrayOutputStream ;
import java.io.File ;
import java.io.FileOutputStream ;


public class MutliPart {
    
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


    public void setBytesContent(Part part) {
        InputStream inputStream = part.getInputStream();
        setInputStream(inputStream);
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



    public MutliPart() {}

    public MutliPart(String name, String realName, InputStream inputStream) {
        setName(name);
        setRealName(realName);
        setInputStream(inputStream);
    }

    public MutliPart(Part part){

        setName(part.getSubmittedFileName());
        setRealName(part.getName());
        setInputStream(part);
    }
    




}
