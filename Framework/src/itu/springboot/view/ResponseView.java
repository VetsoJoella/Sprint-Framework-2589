package itu.springboot.view;

import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.HashMap;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import itu.springboot.view.response.ModelView;
import jakarta.servlet.RequestDispatcher;
import java.io.IOException;
import com.google.gson.Gson;


public class ResponseView {
    
    public void statusCode( HttpServletResponse response, int code, String head, String message) throws IOException{

        response.setStatus(code);
        response.setContentType("text/html"); 
        response.getWriter().println("<html>");
        response.getWriter().println("<head><title>"+head+"</title></head>");
        response.getWriter().println("<body>");
        response.getWriter().println("<h1>Erreur "+code+" : Page Non Trouvée</h1>");
        response.getWriter().println("<p>"+message+"</p>");
        response.getWriter().println("</body></html>");
    }

    public void giveResponse(Object responseMethod, HttpServletRequest req, HttpServletResponse res) throws Exception{ // Voix de réponse normal

        PrintWriter out = res.getWriter();
        // if(responseMethod instanceof String){
        //     // out.println("Réponse de la methode est "+responseMethod);

        // } else 
        if(responseMethod instanceof ModelView){

            RequestDispatcher rd = req.getRequestDispatcher(((ModelView)responseMethod).getUrl());
            HashMap dataValues = ((ModelView)responseMethod).getData() ;
            dataValues.forEach((key, value) -> req.setAttribute((String)key,dataValues.get(key)));
            Enumeration<String> attributeNames = req.getAttributeNames();

            while (attributeNames.hasMoreElements()) {
                String attributeName = attributeNames.nextElement();
                Object attributeValue = req.getAttribute(attributeName);
                req.setAttribute(attributeName,attributeValue);
                System.out.println("Attribut : " + attributeName + " => " + attributeValue);
            }

            rd.forward(req, res);
        } else{
            out.println("Type de retour non appropriée");
        }
   }

   public void giveResponse(Object responseMethod, HttpServletResponse res) throws Exception{      // Réponse en json
        
        Gson gson = new Gson();
        String jsonResponse = gson.toJson(responseMethod) ; 
        if(responseMethod instanceof ModelView){
            jsonResponse = gson.toJson(((ModelView)responseMethod).getData()) ;
        }
        printJson(jsonResponse, res);
   }

    private void printJson(String json, HttpServletResponse response) throws Exception{
        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        out.print(json);
   }

   public void redirect(HttpServletRequest request, HttpServletResponse response, String redirectURI) throws Exception{

        System.out.println("Url sans URI est "+redirectURI);
        // String referer = request.getHeader("Referer");
        // System.out.println("Référence est "+referer);
        request.getRequestDispatcher(redirectURI).forward(request, response);
   }

   public void redirect(HttpServletRequest request, HttpServletResponse response, String redirectURI, boolean isErrorPresent) throws Exception{

    System.out.println("Url sans URI est "+redirectURI);
    // String referer = request.getHeader("Referer");
    // System.out.println("Référence est "+referer);
    request.getRequestDispatcher(redirectURI).forward(request, response);
}
}
