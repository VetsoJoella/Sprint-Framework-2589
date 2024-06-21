package contoller.main ;

import util.*;
import annotation.*;
import mapping.Mapping ;
import response.ModelView;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.File ;
import java.util.Vector;
import java.util.HashMap;
import java.util.Map;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.http.HttpClient;

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
            try {
                Class clazz = Class.forName(classe);
                Method[] methods = clazz.getDeclaredMethods();
    
                for (Method method : methods) {
                    if (Util.isAnnotationPresent(method,Get.class)) {
                        Get annotation = method.getAnnotation(Get.class);
                        String key = annotation.url();

                        if(Util.isDuplicated(hashMap, key)==false){ 
                            hashMap.put(key,new Mapping(classe,method.getName(),method.getParameterTypes()));
                        }
                    }
                }
            }
            catch(Exception err){
                throw err ;
            }
            // personnesMap.put("p1", new Personne("Alice", 30));
        }
    }
    public void init(){

        //Appel de la fonction loadData
        try {
            initParameter(); 

        } catch(Exception err){
            err.printStackTrace();
            System.exit(1);
        }
    
    }

	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		
        processRequest(req,res);
		
	}

    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		
        processRequest(req,res);
		
	}

    Map<String, String[]> getValueSend(HttpServletRequest request, HttpServletResponse response){

        Map<String, String[]> parameterMap = request.getParameterMap();
        return parameterMap ;
    }

    void processRequest(HttpServletRequest req, HttpServletResponse res)throws ServletException, IOException {

        PrintWriter out = res.getWriter();
        String host = this.getInitParameter("host") ;
        String link = req.getRequestURL().toString();
        String contextPath = req.getContextPath();

        link = link.substring(host.length()+contextPath.length()-1);
        
        System.out.println("Lien est "+link);
        Mapping map = hashMap.get(link);

        if(map!=null){
            try {
                
                Map<String, String[]> formValue = getValueSend(req, res); 
                Object responseMethod = UtilController.invoke(map,formValue);
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
            catch (InvocationTargetException e) {
                // e.printStackTrace();
                System.out.println("Cause réelle: " + e.getTargetException().getMessage());
            }
           catch(Exception err){
                out.println( "Error "+err.getMessage());
                err.printStackTrace();
                // err.getTargetException().getMessage();
           }

        }
        else{
            out.println("404 , method not found  ");

        }

    }    
}