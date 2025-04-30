package itu.springboot.classes.download;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Map;

import jakarta.servlet.http.HttpServletResponse;

public class DownloadObject {

    private String extension;
    private Object data;
    private String contentType;
    private String name;
    private byte[] bytes;
    // private String csv;

    public DownloadObject(){}
    // Constructeur
    public DownloadObject(String extension, Object data, String contentType, String name, byte[] bytes) {
        setExtension(extension);
        setData(data);
        setContentType(contentType);
        setName(name);
        setBytes(bytes);
    }

    // Getters et Setters
    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public void convertToByte() {
        // setContentType("application/octet-stream");
        if (this.data != null) {
            String text = this.data.toString(); // transforme en String
            setBytes(text.getBytes(StandardCharsets.UTF_8)); // transforme en UTF-8 bytes
        }
    }

    public void convertToByte(String str) {
        if (str!= null) {
            setBytes(str.getBytes(StandardCharsets.UTF_8)); // transforme en UTF-8 bytes
        }
    }

    public void convert(){}

    // void convertToByte(Object object) {
    //     setHeader("application/octet-stream");
    //     if (object != null) {
    //         String text = object.toString(); // transforme en String
    //         setBytes(text.getBytes(StandardCharsets.UTF_8)); // transforme en UTF-8 bytes
    //     }
    // }

    // public void convertToCSV() {

    //     setHeader("text/csv");
    //     if (this.data == null) {
    //         return;
    //     }
    //     try {
    //         CsvMapper csvMapper = new CsvMapper();
    //         CsvSchema schema;
    
    //         if (this.data instanceof Collection) {
    //             // Cas : Collection d'objets
    //             Collection<?> collection = (Collection<?>) this.data;
    //             if (!collection.isEmpty()) {
    //                 // Prend la classe du premier élément pour construire le schema
    //                 Object first = collection.iterator().next();
    //                 schema = csvMapper.schemaFor(first.getClass()).withHeader();
    //                 // this.csv = csvMapper.writer(schema).writeValueAsString(collection);
    //                 convertToByte(csvMapper.writer(schema).writeValueAsString(collection));
    //             } else convertToByte("");
    //         } else {
    //             schema = csvMapper.schemaFor(this.data.getClass()).withHeader();
    //             convertToByte(csvMapper.writer(schema).writeValueAsString(this.data));
    //         }
    //     } catch (Exception e) {
    //         throw new RuntimeException("Erreur de conversion en CSV avec Jackson", e);
    //     }
    // }

    public void download(HttpServletResponse res) throws Exception{

        res.setContentType(getContentType());
        res.setHeader("Content-Disposition", "attachment; filename=\"" + (getName() != null ? getExtension() : "fichier") + (getExtension() != null ? getExtension() : "") + "\"");
        res.setContentLength(getBytes().length);

        try (OutputStream out = res.getOutputStream()) {
            out.write(getBytes());
            out.flush();
        }
    }
    // public Object getData(){
    //     if(getBytes()!=null) return getBytes() ;
    //     return getCsv() ;
    // }

}
