package itu.springboot.util.date;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class DateUtil {
    
    public static Timestamp dateStrFormatTimestamp(String dateHeureDecollage){
        try {
            LocalDateTime localDateTime = LocalDateTime.parse(dateHeureDecollage);
            Timestamp tms = Timestamp.valueOf(localDateTime) ;
            return tms;

        } catch (Exception e) {
            return null ; 
        }
    }
}
