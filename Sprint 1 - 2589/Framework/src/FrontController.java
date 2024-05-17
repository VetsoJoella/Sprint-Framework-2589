package contoller.main ;

import util.Util;
import annotation.AnnotationController;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.File ;
import java.util.Vector;


import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;



public class FrontController extends HttpServlet {

    String[] classes ;
    boolean dataLoaded; 

    void initParameter(){

        dataLoaded = false ;
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
        if(dataLoaded==false){

            ServletContext context = getServletContext();
            String param = this.getInitParameter("contollerPath") ;
            String path = context.getRealPath(param); 
            out.print("Loaded Data");
            File file = new File(path);
            classes = Util.loadData(param,file,AnnotationController.class);
            dataLoaded = true ;
        }
      
        out.println("Bienvenu dans le FrontController");
        out.println("Url tape est "+req.getRequestURL());

        // out.print("File path est "+file.getAbsolutePath());
        out.println("Liste des classes ");
        out.println("Length "+classes.length);
        for (String classe : classes){
            out.println(classe);
        }
        

    }

    String[] getClasses(){

        return classes;
        // Define base package in web.xml
        // parm-web : classes.controller
    }

    


}