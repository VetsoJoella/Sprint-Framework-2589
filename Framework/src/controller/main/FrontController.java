package contoller.main ;

import util.*;
import annotation.*;
import mapping.Mapping ;
import mapping.Verb;
import response.ModelView;
import session.Session;
import controller.reflect.Reflect;
import exception.ConflictMethodException;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.File ;
import java.util.Vector;
import java.util.HashMap;
import java.util.Enumeration;
import java.util.Map;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.http.HttpClient;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.ServletException;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.annotation.MultipartConfig ;
import com.google.gson.Gson;

@MultipartConfig
public class FrontController extends HttpServlet {

    HashMap<String, Mapping> hashMap ;

    void initParameter() throws Exception{

        ServletContext context = getServletContext();
        String param = this.getInitParameter("contollerPath") ;
        String path = context.getRealPath(param); 
        String[] classes = Util.loadData(param,path,AnnotationController.class);
        hashMap = new HashMap<>();
        
        for (String classe : classes){              // boucle des classes
            try {
                Class clazz = Class.forName(classe);
                Method[] methods = clazz.getDeclaredMethods();
    
                for (Method method : methods) {     // boucles des méthodes présentes dans la clasee
                    if (Util.isAnnotationPresent(method,Url.class)) {       // vérification de la présence de l'url
                    
                        String verb = Util.getVerbFromAnnotation(method);
                        Url annotation = method.getAnnotation(Url.class);
                        String key = annotation.url();
                        Mapping mapping = hashMap.get(key) ;
                        if(mapping==null){
                            mapping = new Mapping(classe);
                            hashMap.put(key,mapping); 
                        }

                        if(!(mapping.getVerbs().add(new Verb(verb, method)))){
                            throw new ConflictMethodException("L'url "+key+ "avec le verb "+verb+" est dupliqué");
                        }
                    }
                }
            }
            catch(Exception err){
                throw err ;
            }
        }
    }


    //Initialisation : get de toutes les classes annotées controller
    public void init(){ 
        
        try {
            initParameter(); 

        } catch(Exception err){
            
            getServletContext().setAttribute("buildError",err.getMessage());
            System.err.println("Erreur dans l'initialisation des controllers");
        }
    
    }

	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		
        try {
            processRequest(req,res);

        } catch (Exception e) {
            handleError(e, req, res);
        }
		
	}

    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		
        try {
            processRequest(req,res);

        } catch (Exception e) {
            handleError(e, req, res);
        }
		
	}

    // Get des valeurs dans envoyes dans les urls
    Map<String, String[]> getValueSend(HttpServletRequest request, HttpServletResponse response){

        Map<String, String[]> parameterMap = request.getParameterMap();
        return parameterMap ;
    }

    void processRequest(HttpServletRequest req, HttpServletResponse res)throws ServletException, IOException, Exception {

        if(getServletContext().getAttribute("buildError")!=null){
            System.err.println(getServletContext().getAttribute("buildError"));
            statusCode(res, 500, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Problème interne du serveur",(String)getServletContext().getAttribute("buildError"));
            // res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

        } else {

            PrintWriter out = res.getWriter();
            String host = getInitParameter("host") ;
            String link = req.getRequestURL().toString();
            String contextPath = req.getContextPath();
            link = link.substring(host.length()+contextPath.length()-1);

            Mapping map = hashMap.get(link);

            if(map!=null && map.get(req.getMethod())!=null){
                try {
                    Verb verb = map.get(req.getMethod()) ;
                    Object instanceOfClass = Class.forName(map.getClassName()).newInstance();
                    HttpSession httpSession = req.getSession();
                    Session session = null ;
                    session = setSession(instanceOfClass, httpSession);
                    Object responseMethod = UtilController.invoke(instanceOfClass, verb, req);
                    updateSession(session, httpSession);

                    if(Util.isAnnotationPresent(instanceOfClass, RestApi.class) || Util.isAnnotationPresent(verb.getMethod(), RestApi.class)){
                        giveResponse(responseMethod, res);
                    } else { 
                        giveResponse(responseMethod, req, res);
                    }
                }
                catch(Exception err){
                    RequestDispatcher rd = req.getRequestDispatcher("/views/error.jsp");
                    req.setAttribute("error",err.getMessage());
                    rd.forward(req, res);
                }
                // e.printStackTrace();
                
            }
            else{
                // out.println("404 , method not found  ");
                statusCode(res, 404, HttpServletResponse.SC_NOT_FOUND, "Page Non Trouvée", "La ressource que vous cherchez n'existe pas.");
            }
        }
        
    }    

    // mise à jour de la session
    void updateSession(Session session, HttpSession httpSession){

        if(session!=null){

            Enumeration<String> attributeNames = httpSession.getAttributeNames();

            while (attributeNames.hasMoreElements()) {
                String attributeName = attributeNames.nextElement();
                httpSession.removeAttribute(attributeName);
            }

            Map<String,Object> sessionValues = session.getMap() ;
            for(String key : sessionValues.keySet()) {
                httpSession.setAttribute(key, sessionValues.get(key)) ;
            }
        }
   }

   // Création de la session en mettant la valeur du hhtpSession dans une classe session 
   Session createSession(HttpSession httpSession){

        Session session = new Session();
        System.out.println("Appel de create session ");
        Enumeration<String> attributeNames = httpSession.getAttributeNames();
        while (attributeNames.hasMoreElements()) {
            String attributeName = attributeNames.nextElement();
            session.add(attributeName, httpSession.getAttribute(attributeName));
        }
        return session ;

   }

   // Modification de la session
   Session setSession(Object instanceOfClass,  HttpSession httpSession) throws Exception{
        Field sessionField = Reflect.fieldExist(instanceOfClass.getClass(),Session.class);
        Session session = null ;
        if(sessionField != null ){               // Vérifier si la classe a un field Session pour l'injection de dépendance
            session = createSession(httpSession);
            Reflect.setObject(instanceOfClass, sessionField.getName(),session);
        }
        return session ;
    }

    // Print de réponse en format json
    void printJson(String json, HttpServletResponse response) throws Exception{
        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        out.print(json);
   }
  
    void giveResponse(Object responseMethod, HttpServletRequest req, HttpServletResponse res) throws Exception{

        PrintWriter out = res.getWriter();
        if(responseMethod instanceof String){
            out.println("Réponse de la methode est "+responseMethod);

        } else if(responseMethod instanceof ModelView){

            RequestDispatcher rd = req.getRequestDispatcher(((ModelView)responseMethod).getUrl());
            HashMap dataValues = ((ModelView)responseMethod).getData() ;
            dataValues.forEach((key, value) -> req.setAttribute((String)key,dataValues.get(key)));
            rd.forward(req, res);
        } else{
            out.println("Type de retour non appropriée");
        }
   }

   void giveResponse(Object responseMethod, HttpServletResponse res) throws Exception{
        
        Gson gson = new Gson();
        String jsonResponse = gson.toJson(responseMethod) ; 
        if(responseMethod instanceof ModelView){
            jsonResponse = gson.toJson(((ModelView)responseMethod).getData()) ;
        }
        printJson(jsonResponse, res);
   }

   void handleError(Exception e, HttpServletRequest req, HttpServletResponse res)
            throws IOException {
        // Logique pour gérer les erreurs
        res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // 500 Internal Server Error
        res.setContentType("text/html");

        // Générer une page d'erreur comme le fait Tomcat
        PrintWriter out = res.getWriter();
        out.println("<html><head><title>Erreur interne</title></head><body>");
        out.println("<h1>Une erreur s'est produite</h1>");
        out.println("<p>" + e.getMessage() + "</p>");

        // Afficher la stack trace (comme le fait Tomcat)
        out.println("<pre>");
        e.printStackTrace(out);
        out.println("</pre>");

        out.println("</body></html>");
    }

    void statusCode( HttpServletResponse response, int code, int httpServletResponse, String head, String message) throws Exception{

        response.setStatus(httpServletResponse);
        response.setContentType("text/html"); 
        response.getWriter().println("<html>");
        response.getWriter().println("<head><title>"+head+"</title></head>");
        response.getWriter().println("<body>");
        response.getWriter().println("<h1>Erreur "+code+" : Page Non Trouvée</h1>");
        response.getWriter().println("<p>"+message+"</p>");
        response.getWriter().println("</body></html>");
    }

}