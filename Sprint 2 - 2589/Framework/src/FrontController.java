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

    void initParameter(){

        ServletContext context = getServletContext();
        String param = this.getInitParameter("contollerPath") ;
        String path = context.getRealPath(param); 
        File file = new File(path);
        String[] classes = Util.loadData(param,file,AnnotationController.class);

        hashMap = new HashMap<>();
        
        for (String classe : classes){
            try {
                Class clazz = Class.forName(classe);
                Method[] methods = clazz.getDeclaredMethods();
    
                for (Method method : methods) {
                    if (method.isAnnotationPresent(Get.class)) {
                        Get annotation = method.getAnnotation(Get.class);
    
                        // Obtient la valeur de l'annotation
                        String value = annotation.url();
                        hashMap.put(value,new Mapping(classe,method.getName()));
                    }
                }
            }
            catch(Exception err){}
        
            // personnesMap.put("p1", new Personne("Alice", 30));
        }
        
    }
    public void init(){

        //Appel de la fonction loadData

        initParameter(); 


    
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
            out.println("Nom de la classe associée a cette methode est "+map.getClassName());
            out.println("Nom de la methode associée a cette methode est "+map.getMethod());

        }
        else{
            out.println("404 , method not found  ");

        }
       

    }    


}