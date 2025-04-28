package itu.springboot.services.connection;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.Duration;

import org.apache.commons.dbcp2.BasicDataSource;

public class UtilDb {

    private static BasicDataSource dataSource;
    public UtilDb(){

    }

    public UtilDb(String host, String port, String user, String pwd, String db, int max, int maxIdle, int min, int seconds) {
        
        System.out.println("Instanciation de utilDB : "+host+" "+port+" "+user+" "+pwd+" "+db+" "+max);
        dataSource = new BasicDataSource();
        
        // Configuration du pool pour PostgreSQL
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl("jdbc:postgresql://"+host+":"+port+"/"+db);
        dataSource.setUsername(user);
        dataSource.setPassword(pwd);
        
        // Paramètres du pool
        dataSource.setMaxTotal(max);
        dataSource.setMaxIdle(maxIdle);
        dataSource.setMinIdle(min);
        dataSource.setMaxWait(Duration.ofSeconds(seconds)); // 10 secondes
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
    
}