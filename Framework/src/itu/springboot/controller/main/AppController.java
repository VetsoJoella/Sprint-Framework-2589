package itu.springboot.controller.main;
// package controller.main;

// import config.ConfigManager;
// import jakarta.servlet.ServletContextEvent;
// import jakarta.servlet.ServletContextListener;
// import jakarta.servlet.annotation.WebListener;

// // import com.service.config.Configuration;
// import services.connection.UtilDb;

// @WebListener
// public class AppController implements ServletContextListener {

//     ConfigManager configManager ;

//     public ConfigManager getConfigManager() {
//         return this.configManager;
//     }

//     public void setConfigManager(ConfigManager configManager) {
//         this.configManager = configManager;
//     }

//     @Override
//     public void contextInitialized(ServletContextEvent sce) {

//         try {
//             setConfigManager(new ConfigManager(sce.getServletContext()));

//             String host = configManager.getProperty(ConfigManager.HOST), port = configManager.getProperty(ConfigManager.PORT) ;
//             String db = configManager.getProperty(ConfigManager.BASE), user = configManager.getProperty(ConfigManager.USER) ;
//             String pwd = configManager.getProperty(ConfigManager.PWD) ;

//             int max = Integer.valueOf(configManager.getProperty(ConfigManager.MAX)), maxIdle = Integer.valueOf(configManager.getProperty(ConfigManager.MAXIDLE));
//             int min = Integer.valueOf(configManager.getProperty(ConfigManager.MIN)) ; 
            
//             UtilDb utilDb = new UtilDb(host, port, user, pwd, db, max, maxIdle, min, min);
//             sce.getServletContext().setAttribute("utilDb", utilDb);

//         } catch(Exception err) {
//             err.printStackTrace();
//         }
      
//     }

//     @Override
//     public void contextDestroyed(ServletContextEvent sce) {
        
//     }
// }

