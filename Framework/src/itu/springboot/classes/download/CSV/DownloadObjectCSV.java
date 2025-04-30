package itu.springboot.classes.download.CSV;

import itu.springboot.classes.download.DownloadObject;

import java.util.Collection;

import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

public class DownloadObjectCSV extends DownloadObject{

    public DownloadObjectCSV(){
        super() ;
    }

    public DownloadObjectCSV(String extension, Object data, String name) {
        super(extension, data, name) ;
     }

    public void convert(){
        setContentType("text/csv");
        if (getData() == null) {
            return;
        }
        try {
            CsvMapper csvMapper = new CsvMapper();
            CsvSchema schema;
    
            if (getData() instanceof Collection) {
                // Cas : Collection d'objets
                Collection<?> collection = (Collection<?>) getData();
                if (!collection.isEmpty()) {
                    // Prend la classe du premier élément pour construire le schema
                    Object first = collection.iterator().next();
                    schema = csvMapper.schemaFor(first.getClass()).withHeader();
                    // this.csv = csvMapper.writer(schema).writeValueAsString(collection);
                    convertToByte(csvMapper.writer(schema).writeValueAsString(collection));
                } else convertToByte("");
            } else {
                schema = csvMapper.schemaFor(getData().getClass()).withHeader();
                convertToByte(csvMapper.writer(schema).writeValueAsString(getData()));
            }
        } catch (Exception e) {
            throw new RuntimeException("Erreur de conversion en CSV avec Jackson", e);
        }
    }
}
