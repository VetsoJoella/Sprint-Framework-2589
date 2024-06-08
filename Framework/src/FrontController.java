package contoller.main ;

import util.*;
import annotation.AnnotationController;
import annotation.Get;
import mapping.Mapping ;
import response.ModelView;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.File ;
import java.util.Vector;
import java.util.HashMap;
import java.util.Map;
import java.lang.reflect.Method;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;


public class FrontController extends HttpServlet {

    HashMap<String, Mapping> hashMap ;

    void initParameter() throws Exception{

        ServletContext context = getServletContext();
        String param = this.getInitParameter("contollerPath") ;
        String path = context.getRealPath(param); 
        String[] classes = Util.loadData(param,path,AnnotationController.class);
        hashMap = new HashMap<>();
        
        for (String classe : classes){
            Class clazz = Class.forName(classe);
            Method[] methods = clazz.getDeclaredMethods();

            for (Method method : methods) {
                if (method.isAnnotationPresent(Get.class)) {
                    Get annotation = method.getAnnotation(Get.class);

                    // Obtient la valeur de l'annotation
                    String key = annotation.url();
                    if(hashMap.containsKey(key)){
                        Mapping m = hashMap.get(key);
                        String erreur = "L'url "+key+" est dupliquée.\n Elle existe déja dans la classe "+m.getClassName()+" avec la methode "+m.getMethod(); 
                        hashMap.clear();
                        throw new Exception(erreur);
                        
                    }
                    else{
                        hashMap.put(key,new Mapping(classe,method.getName()));
                    }
                }
            }
        }
        // personnesMap.put("p1", new Personne("Alice", 30));

        
    }
    public void init(){

        try{
            initParameter(); 
        } catch(Exception ee){
            throw new RuntimeException(ee);
        }
    }

	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		
        processRequest(req,res);
		
	}

    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		
        processRequest(req,res);
		
	}

    void processRequest(HttpServletRequest req, HttpServletResponse res)throws ServletException, IOException {

        PrintWriter out = res.getWriter();
        String host = this.getInitParameter("host") ;
        String link = req.getRequestURL().toString();

        String contextPath = req.getContextPath();

        link = link.substring(host.length()+contextPath.length()-1);
        // out.println("C "+contextPath);
        // out.println("Without "+link);

        Mapping map = hashMap.get(link);

        if(map!=null){
            try {
                Object responseMethod = UtilController.invoke(map);
                
                if(responseMethod instanceof String){
                    out.println("Réponse de la methode est "+responseMethod);

                } else if(responseMethod instanceof ModelView){

                    RequestDispatcher rd = req.getRequestDispatcher(((ModelView)responseMethod).getUrl());

                    HashMap dataValues = ((ModelView)responseMethod).getData() ;
                    dataValues.forEach((key, value) -> req.setAttribute((String)key,dataValues.get(key)));
                    // for (String key : dataValues.values()) {
                    //     req.setAttribute(key,dataValues.get(key));
                    // }
                    rd.forward(req, res);

                } else{

                    out.println("Type de retour non appropriée");
                }
                // out.println("Nom de la classe associée a cette methode est "+map.getClassName());
                // out.println("Nom de la methode associée a cette methode est "+map.getMethod());
            }
           catch(Exception err){
                out.println( "Error "+err.getMessage());
                err.printStackTrace();
           }

        }
        else{
            out.println("404 , method not found  ");

        }
       

    }    


}