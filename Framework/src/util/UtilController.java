
package util;

import annotation.*;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import mapping.Verb;
import mulitpart.MultiPart;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Part ; 

public class UtilController {
    
    
    public static Object invoke(Object instanceOfClass, Verb verb, HttpServletRequest request) throws Exception{

        Object response = null ;
        try {
            
            Method method = verb.getMethod();
            Object[] data = matchValues(method, request) ;
            response = method.invoke(instanceOfClass,data);
        }
        catch(Exception err){
            err.printStackTrace();
            throw err ;
        }
        return response ;
    }

    // public static Object[] matchValues(Method method,Map<String,String[]> data) throws Exception{
    public static Object[] matchValues(Method method, HttpServletRequest request) throws Exception{
        
        Object[] formValues = null ;
        Parameter[] parameters = method.getParameters();

        if (parameters==null || parameters.length == 0 ) return formValues ;

        try{ 
            // if(data==null){
            //     return formValues ;
            // }
            if ((request.getParameterMap() == null || request.getParameterMap().isEmpty())) {                
                return formValues ;
            }
            if (request.getHeader("Content-Type").contains("multipart") && (request.getParts() == null || request.getParts().isEmpty())) return formValues ;
            
            else {
                
                
                // checkAnnotation(parameters);
                formValues = new Object[parameters.length];
                Typing.instance(parameters, formValues);                                           // Instancier les attributs de la methode
                Map<String, String[]> data = listOfName(request);
            



                for (Map.Entry<String, String[]> entry : data.entrySet()) {

                    String key = entry.getKey();
                    System.out.println("Map est "+key);

                    String[] splitKey = key.split("\\.");
                    String[] values = entry.getValue();
                    Object[] paramObject = getParameterForData(parameters,splitKey[0],Param.class);

                    Parameter param = (Parameter)paramObject[0] ; int indice = (int)paramObject[1]; String name = (String)paramObject[2];

                    if(splitKey.length==1){

                        System.out.println(request.getHeader("Content-Type").contains("multipart"));
                        
                        if(param.getType().isArray() || param.getClass().isArray()){
                            formValues[indice] = Typing.arrayCast(request, splitKey[0], param.getType(), (Object[])values) ; 
                        }
                        else{
                            if(param.getType()==MultiPart.class && request.getPart(splitKey[0])!=null) { 
                                formValues[indice] = Typing.convert(request.getPart(splitKey[0]),param.getType()) ; 
                                System.out.println("Parametre avec multipart class");
                            }
                            else  formValues[indice] = Typing.convert(request.getParameter(splitKey[0]),param.getType()) ; 

                        }
                    }
                    else {
                       
                        Typing.setObject(request, formValues[indice], splitKey[1], key);
                        System.out.println("Appel de la fonction convert dans setObject");

                    }
                    
                } 
            }
        }
        catch(Exception err){
            err.printStackTrace();
            throw err ;
        }
        return formValues ;
    }
    

    public static Object[] getParameterForData(Parameter[] parameters, String name, Class annotation) throws Exception{

        for(int i =0; i<parameters.length ; i++){
            String nameParam = parameters[i].getName() ; 
            if(parameters[i].isAnnotationPresent(annotation)){
                nameParam = ((Param)parameters[i].getAnnotation(Param.class)).name();
            }
           if(nameParam.equalsIgnoreCase(name)){
           }
            // else {
            //     throw new Exception("ETU 2589 : annotation n'est pas présente sur l'attribut "+(parameters[i]).getName());
            // }
        }
        return null ;

        // for(int i =0; i<parameters.length ; i++){
           
        //     if(parameters[i].isAnnotationPresent(Param.class)==false){
          
            
        //         throw new Exception("ETU 2589 : annotation n'est pas présente sur l'attribut "+(parameters[i]).getName());
        //     }
            
        // }
       
    
    }
    public static Map<String, String[]> listOfName(HttpServletRequest request) throws Exception{

        Map<String, String[]> temMap = request.getParameterMap();

        Map<String, String[]> data = new HashMap<>();

        for (Map.Entry<String, String[]> entry : temMap.entrySet()) {
            data.put(entry.getKey(), entry.getValue());
        }

        for (Part part : request.getParts()) {
            String name = part.getName();
            String value = new String(part.getInputStream().readAllBytes());
            data.put(name, new String[]{value});
        }
        return data;
    }
    
}