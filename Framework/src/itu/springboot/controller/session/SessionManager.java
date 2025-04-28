package itu.springboot.controller.session;

import java.lang.reflect.Field;
import java.util.Enumeration;
import java.util.Map;

import itu.springboot.classes.session.Session;
import jakarta.servlet.http.HttpSession;
import itu.springboot.controller.reflect.Reflect;

public class SessionManager {
    
    HttpSession httpSession ;
    
    public HttpSession getHttpSession() {
        
        return httpSession;
    }
    public void setHttpSession(HttpSession httpSession) {
        this.httpSession = httpSession;
    }
    public SessionManager(HttpSession httpSession){
        setHttpSession(httpSession);
    }

    void clean(){
        Enumeration<String> attributeNames = getHttpSession().getAttributeNames();

        while (attributeNames.hasMoreElements()) {
            String attributeName = attributeNames.nextElement();
            getHttpSession().removeAttribute(attributeName);
        }
    }

    public void updateSession(Session session, Object[] dependances) {
        clean();
        updateSession(session);
        // updateSession(dependances);
    }

    void updateSession(Object[] dependances) {

         for (Object object : dependances) {
            if(object instanceof Session) {
                Map<String, Object> data = ((Session)object).getMap() ;
                for(String key : data.keySet()) {
                    // System.out.println("Valeur dans session update [] est "+key +"="+data.get(key));
                    getHttpSession().setAttribute(key, data.get(key)) ;
                }
            }
        }
    }

    public void updateSession(Session session){

        // System.out.println("Adresse de session : "+session);
        if(session!=null){
            Map<String,Object> sessionValues = session.getMap() ;
            for(String key : sessionValues.keySet()) {
                // System.out.println("Valeur dans session update est "+key +"="+sessionValues.get(key));
                getHttpSession().setAttribute(key, sessionValues.get(key)) ;
            }
        }
   }

   // Création de la session en mettant la valeur du hhtpSession dans une classe session 
   public Session createSession(){

        Session session = new Session();
        System.out.println("Appel de create session ");
        Enumeration<String> attributeNames = getHttpSession().getAttributeNames();
        while (attributeNames.hasMoreElements()) {
            String attributeName = attributeNames.nextElement();
            // System.out.println("Dans SESSION : key "+attributeName+" valeur : "+getHttpSession().getAttribute(attributeName));
            session.add(attributeName, getHttpSession().getAttribute(attributeName));
        }
        return session ;

   }

   // Modification de la session
   public Session setSession(Object instanceOfClass) throws Exception{
        Field sessionField = Reflect.fieldExist(instanceOfClass.getClass(),Session.class);
        Session session = createSession() ;
        if(sessionField != null ){               // Vérifier si la classe a un field Session pour l'injection de dépendance
            Reflect.setObject(instanceOfClass, sessionField.getName(),session);
        }
        return session ;
    }
}
