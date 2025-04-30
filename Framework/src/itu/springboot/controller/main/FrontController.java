package itu.springboot.controller.main ;

import itu.springboot.util.*;
import itu.springboot.util.word.WordUtil;
import itu.springboot.view.ResponseView;
import itu.springboot.view.response.RedirectAttributes;
import itu.springboot.annotation.*;
import itu.springboot.annotation.model.ModelError;
import itu.springboot.classes.download.DownloadObject;
import itu.springboot.classes.mapping.Mapping;
import itu.springboot.classes.mapping.Verb;
import itu.springboot.classes.session.Session;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.lang.reflect.Method;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletContext;
import jakarta.servlet.annotation.MultipartConfig ;

import org.apache.log4j.Logger;

import itu.springboot.services.connection.UtilDb;
import itu.springboot.config.ConfigManager;
import itu.springboot.controller.security.RoleManager;
import itu.springboot.controller.session.SessionManager;
import itu.springboot.controller.wrapper.RequestWrapper;
import itu.springboot.exception.ConflictMethodException;
import itu.springboot.exception.ModelException;


@MultipartConfig
public class FrontController extends HttpServlet {

    HashMap<String, Mapping[]> hashMap ;
    ResponseView responseView ; 
    ConfigManager configManager ;
    RoleManager roleManager ; 

    public ConfigManager getConfigManager() {
        return configManager;
    }


    public void setConfigManager(ConfigManager configManager) {
        this.configManager = configManager;
    }

    void initLog() {
        Logger log = Logger.getLogger(getClass().getName());
        getServletContext().setAttribute("log", log);

    }

    void initConnection() {
        String host = configManager.getProperty(ConfigManager.HOST), port = configManager.getProperty(ConfigManager.PORT) ;
        String db = configManager.getProperty(ConfigManager.BASE), user = configManager.getProperty(ConfigManager.USER) ;
        String pwd = configManager.getProperty(ConfigManager.PWD) ;

        int max = Integer.valueOf(configManager.getProperty(ConfigManager.MAX)), maxIdle = Integer.valueOf(configManager.getProperty(ConfigManager.MAXIDLE));
        int min = Integer.valueOf(configManager.getProperty(ConfigManager.MIN)), seconds = Integer.valueOf(configManager.getProperty(ConfigManager.SCND)) ;
       
            
        UtilDb utilDb = new UtilDb(host, port, user, pwd, db, max, maxIdle, min, seconds);
        getServletContext().setAttribute("utilDb", utilDb);

       
    }

    void initParameter() throws Exception{

        configManager = new ConfigManager(getServletContext());
        responseView = new ResponseView();
        roleManager = new RoleManager(getConfigManager().getProperty(ConfigManager.AUTH_REF));

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
                        List<Mapping> mapping = hashMap.containsKey(key) ? new ArrayList<>(Arrays.asList(hashMap.get(key))) : new ArrayList<>(); // Initialiser une nouvelle liste si key n'existe pas
                
                        if (Mapping.getMappingWithVerb(mapping, verb) != null) {
                            throw new ConflictMethodException("L'URL " + key + " avec le verbe " + verb + " est dupliqué");
                        }
                        mapping.add(new Mapping(classe, new Verb(verb, method)));
                        hashMap.put(key, mapping.toArray(new Mapping[0])); // Convertir en tableau
                

                    }
                }
                
                // hashMap.forEach((key, value) -> System.out.println("Clé : " + key + ", Valeur : " + value.length));

            }
            catch(Exception err){
                throw err ;
            }
        }
        responseView = new ResponseView();
    }


    //Initialisation : get de toutes les classes annotées controller
    public void init(){ 
        
        try {
            // initLog() ;
            initParameter(); 
            initConnection();

        } catch(Exception err){
            
            getServletContext().setAttribute("buildError",err.getMessage());
            // ((Logger)getServletContext().getAttribute("log")).fatal(err.getMessage());
            err.printStackTrace();
            System.err.println("Erreur dans l'initialisation des controllers");
            
        }
    
    }

	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		
        try {
            processRequest(req,res);
        } catch (Exception e) {
            // ((Logger)getServletContext().getAttribute("log")).fatal(e.getMessage());
            responseView.statusCode(res, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Erreur dans la méthode", e.getMessage());
        }
	}

    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		
        try {
            processRequest(req,res);
        } catch (Exception e) {
            // ((Logger)getServletContext().getAttribute("log")).fatal(e.getMessage());
            responseView.statusCode(res, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Erreur dans la méthode", e.getMessage());
        }
	}

    // Get des valeurs dans envoyes dans les urls
    Map<String, String[]> getValueSend(HttpServletRequest request, HttpServletResponse response){

        Map<String, String[]> parameterMap = request.getParameterMap();
        return parameterMap ;
    }

    void processRequest(HttpServletRequest req, HttpServletResponse res)throws ServletException, IOException, Exception {

        if(getServletContext().getAttribute("buildError")!=null){
            responseView.statusCode(res, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Problème interne du serveur",(String)getServletContext().getAttribute("buildError"));

        } else {
            String link = req.getRequestURL().toString();
            String contextPath = req.getContextPath();
            link = WordUtil.extractAfter(link, contextPath); 
            doAction(link, req, res);            
        }
    }  

    String isARedirection(String url) {

        return WordUtil.extractAfter(url, "redirect:");
    }

    void doRedirection(String link, HttpServletRequest req, HttpServletResponse res) throws Exception{
        
        // System.out.println("Appel de redirection "+link);
        String stringVerb = "GET" ;
        process(stringVerb, link, req, res);

    }

    void doAction(String link, HttpServletRequest req, HttpServletResponse res) throws Exception{

        String stringVerb = req.getMethod() ;
        process(stringVerb, link, req, res);
      
    }

    void process(String stringVerb, String link, HttpServletRequest req, HttpServletResponse res) throws Exception{

        Mapping[] maps = hashMap.get(link);
        Mapping map = Mapping.getMappingWithVerb(maps, stringVerb) ;

        if(map!=null){

            Verb verb = map.getVerb() ;
            SessionManager sessionManager = new SessionManager(req.getSession());
            Session session = null ;
            try {
                Object instanceOfClass = Class.forName(map.getClassName()).newInstance();
                roleManager.verifyAccess(sessionManager,instanceOfClass, verb);

                // RedirectAttributes redirectAttributes =new RedirectAttributes();
                session = sessionManager.setSession(instanceOfClass);
                List<Object> dependances = getDependance(session) ;
                dependances.add(req) ; dependances.add(res) ;

                Object responseMethod = new UtilController().invoke(instanceOfClass, verb, req, dependances);
                sessionManager.updateSession(session);
                updateRedirectAttributes(dependances, req) ;

                if(Util.isAnnotationPresent(instanceOfClass, RestApi.class) || Util.isAnnotationPresent(verb.getMethod(), RestApi.class)){
                    responseView.giveResponse(responseMethod, res);
                } else if(responseMethod instanceof DownloadObject){
                    DownloadObject dwn = ((DownloadObject)responseMethod) ; 
                    dwn.download(res);

                }
                else if(responseMethod instanceof String) {
                    doRedirection(isARedirection(responseMethod.toString()), req, res);
                } else { 
                    responseView.giveResponse(responseMethod, req, res);
                }

            } catch(ModelException mErr){
                try {
                    String returnURL = Util.getAnnotation(verb.getMethod(),ModelError.class,"value");
                    if(verb.getVerb().compareToIgnoreCase("POST")==0) responseView.redirect(req, res,returnURL);
                    else {
                        HttpServletRequest wrappedRequest = new RequestWrapper(req, "GET");
                        responseView.redirect(wrappedRequest, res,returnURL);

                    }
                    
                } catch (Exception err) {throw err ;}
            }
            catch(Exception err){
               
            //    ((Logger)getServletContext().getAttribute("log")).error(err.getMessage());

                err.printStackTrace();
                RequestDispatcher rd = req.getRequestDispatcher("/views/error.jsp");
                req.setAttribute("error",err.getMessage());
                rd.forward(req, res);
            }
        }
        else{
            responseView.statusCode(res, HttpServletResponse.SC_NOT_FOUND, "Page Non Trouvée", "La ressource que vous cherchez n'existe pas : "+link);
        }
    } 

    void updateRedirectAttributes(List<Object> dependances, HttpServletRequest req) {

        for (Object object : dependances) {
            if(object instanceof RedirectAttributes) {
                HashMap<String, Object> data = ((RedirectAttributes)object).getFlashData();
                data.forEach((cle, valeur) -> {
                    req.setAttribute(cle, valeur) ;
                    // System.out.println("Clé : " + cle + ", Valeur : " + valeur);
                });
                break ;
            }
        }

    }

    List<Object> getDependance(Session session) {
        List<Object> dependances = new ArrayList<>();
        dependances.add(new RedirectAttributes());
        dependances.add((UtilDb) getServletContext().getAttribute("utilDb"));
        dependances.add(session);
        return dependances;
    }
    
    

    
}