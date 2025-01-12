package config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import jakarta.servlet.ServletContext ;

public class ConfigManager {
    
    static final String CONF_PATH = "conf.properties/"; 
    public static final String AUTH_REF = "user"; 
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
