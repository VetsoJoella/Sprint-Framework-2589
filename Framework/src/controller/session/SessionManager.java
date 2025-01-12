package controller.session;

import java.lang.reflect.Field;
import java.util.Enumeration;
import java.util.Map;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import controller.reflect.Reflect;
import session.Session;

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

    public void updateSession(Session session){

        if(session!=null){

            Enumeration<String> attributeNames = getHttpSession().getAttributeNames();

            while (attributeNames.hasMoreElements()) {
                String attributeName = attributeNames.nextElement();
                getHttpSession().removeAttribute(attributeName);
            }

            Map<String,Object> sessionValues = session.getMap() ;
            for(String key : sessionValues.keySet()) {
                System.out.println("Valeur dans session est "+key +"="+sessionValues.get(key));
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
            session.add(attributeName, getHttpSession().getAttribute(attributeName));
        }
        return session ;

   }

   // Modification de la session
   public Session setSession(Object instanceOfClass) throws Exception{
        Field sessionField = Reflect.fieldExist(instanceOfClass.getClass(),Session.class);
        Session session = null ;
        if(sessionField != null ){               // Vérifier si la classe a un field Session pour l'injection de dépendance
            session = createSession();
            Reflect.setObject(instanceOfClass, sessionField.getName(),session);
        }
        return session ;
    }
}
