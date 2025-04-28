package itu.springboot.view;

import java.util.Enumeration;

import jakarta.servlet.http.HttpServletRequest;

public class Bloom {
    
    public static Object out(HttpServletRequest request, String name){
        // System.out.println("Valeur recherchée est "+request.getParameter(name));

        return request.getAttribute(name) ;
        // return request.getParameter(name) != null ? request.getAttribute(name) : "";
    }

    public static String wilt(HttpServletRequest request, String name){
        // System.out.println("Nom recherche est "+name);
        
        Enumeration<String> attributeNames = request.getAttributeNames();
        while (attributeNames.hasMoreElements()) {
            String attributeName = attributeNames.nextElement();
            Object attributeValue = request.getAttribute(attributeName);
            // System.out.println("Attribut : " + attributeName + " => " + attributeValue);
        }

        return (String)request.getAttribute("error-"+name) != null ? (String)request.getAttribute("error-"+name) : "";
    }

    public static String wilt(HttpServletRequest request){
        // System.out.println("Erreur générale est ");

        return (String)request.getAttribute("error") != null ? (String)request.getAttribute("error") : "";
    }

}
