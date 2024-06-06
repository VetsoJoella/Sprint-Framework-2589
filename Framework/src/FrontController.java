package contoller.main ;

import util.Util;
import annotation.AnnotationController;
import annotation.Get;
import mapping.Mapping ;

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

    Map<String, Mapping> hashMap ;

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

    void processRequest(HttpServletRequest req, HttpServletResponse res)throws ServletException, IOException {

        PrintWriter out = res.getWriter();
        String host = "http://localhost:8080/";
        String link = req.getRequestURL().toString();
        // out.println("Init "+link);

        String contextPath = req.getContextPath();

        link = link.substring(host.length()+contextPath.length()-1);
        // out.println("C "+contextPath);
        // out.println("Without "+link);

        Mapping map = hashMap.get(link);

        if(map!=null){
            try {
                Class clazz = Class.forName(map.getClassName());
                Method method = clazz.getMethod(map.getMethod());
                String repMethod = (String)method.invoke(clazz.newInstance());
                // out.println("Nom de la classe associée a cette methode est "+map.getClassName());
                // out.println("Nom de la methode associée a cette methode est "+map.getMethod());
                out.println("Réponse de la methode est "+repMethod);
            }
           catch(Exception err){
                out.print( "Error "+err.getMessage());
                err.printStackTrace();
           }

        }
        else{
            out.println("404 , method not found  ");

        }
       

    }    


}