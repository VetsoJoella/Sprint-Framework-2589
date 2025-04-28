package itu.springboot.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import jakarta.servlet.ServletContext ;

public class ConfigManager {
    
    // Configuration des noms utilisateurs
    static final String CONF_PATH = "conf.properties/"; 
    public static final String AUTH_REF = "user";
    
    // Configuration de la base de donnés 
    public static final String HOST = "db.host" ; 
    public static final String BASE = "db.name" ; 
    public static final String PWD = "db.password" ; 
    public static final String USER = "db.username" ; 
    public static final String PORT = "db.port" ; 
    public static final String MIN = "db.min" ; 
    public static final String MAX = "db.max" ; 
    public static final String MAXIDLE = "db.maxIdle" ; 
    public static final String SCND = "db.seconds" ; 

    private Properties properties;

    public ConfigManager(ServletContext context) throws IOException {

        String path = context.getRealPath("/WEB-INF/app.properties");
        try (InputStream inputStream = context.getResourceAsStream("/WEB-INF/app.properties")) {
            properties = new Properties();
            if (inputStream != null) {
                properties.load(inputStream);
            } else {
                throw new RuntimeException("Fichier app.properties introuvable à " + path);
            }
        }
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }

   

}
