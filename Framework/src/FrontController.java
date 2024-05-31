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
        String param = this.getInitParameter("contollerPath") ;     // Getter la valeur de controller path : path d'mplacement du controller
        String path = context.getRealPath(param);                   // Getter le chemin absolu
        File file = new File(path);
        String[] classes = Util.loadData(param,file,AnnotationController.class);    // loader les classes annotées AnnotationController

        hashMap = new HashMap<>();
        
        for (String classe : classes){                                          
           
            UtilController.getMethodHavingAnnotation(hashMap,classe,Get.class);

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
        String host = this.getInitParameter("host");
        String link = req.getRequestURL().toString();
        // out.println("Init "+link);

        String contextPath = req.getContextPath();

        link = link.substring(host.length()+contextPath.length()-1);
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