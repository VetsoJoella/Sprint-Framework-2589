
package util;

import annotation.*;
import annotation.control.Required;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
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
            // err.printStackTrace();
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
            if (request.getHeader("Content-Type")!=null && request.getHeader("Content-Type").contains("multipart") && (request.getParts() == null || request.getParts().isEmpty())) return formValues ;
            
            else {
                
                List<Object> objectToCheck = new ArrayList<>();
                // checkAnnotation(parameters);
                formValues = new Object[parameters.length];
                Typing.instance(parameters, formValues, objectToCheck);                                           // Instancier les attributs de la methode
                Map<String, String[]> data = listOfValueInRequest(request);
            
                for (Map.Entry<String, String[]> entry : data.entrySet()) {

                    String key = entry.getKey();
                    System.out.println("Map est "+key);

                    String[] splitKey = key.split("\\.");
                    String[] values = entry.getValue();
                    CorrespondingNameParameter paramObject = getParameterForData(parameters, splitKey[0], Param.class);

                    if(splitKey.length==1){
                        
                        if(paramObject.getParameter().getType().isArray() || paramObject.getParameter().getClass().isArray()){
                            formValues[paramObject.getIndice()] = Typing.arrayCast(request, paramObject.getName(), paramObject.getParameter().getType(), (Object[])values) ; 
                        }
                        else{
                            if(paramObject.getParameter().getType()==MultiPart.class && request.getPart(splitKey[0])!=null) { 
                                formValues[paramObject.getIndice()] = Typing.convert(request.getPart(paramObject.getName()),paramObject.getParameter().getType()) ; 
                                System.out.println("Parametre avec multipart class");
                            }
                            else formValues[paramObject.getIndice()] = Typing.convert(request.getParameter(paramObject.getName()),paramObject.getParameter().getType()) ; 
                            // Object str, Class<T> clazz, String name
                        }
                    }
                    else {
                       
                        Typing.setObject(request, formValues[paramObject.getIndice()], splitKey[1], key);
                        System.out.println("Appel de la fonction convert dans setObject");

                    }
                    
                } 

                verifyRequired(objectToCheck);

            }
        }
        catch(Exception err){
            err.printStackTrace();
            throw err ;
        }
        return formValues ;
    }
    

    public static CorrespondingNameParameter getParameterForData(Parameter[] parameters, String name, Class annotation) throws Exception{

        for(int i =0; i<parameters.length ; i++){
            String nameParam = parameters[i].getName() ; 
            if(parameters[i].isAnnotationPresent(annotation)){
                nameParam = ((Param)parameters[i].getAnnotation(Param.class)).name();
            }
           if(nameParam.equalsIgnoreCase(name)){
                return new CorrespondingNameParameter(parameters[i], name,i);

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
    public static Map<String, String[]> listOfValueInRequest(HttpServletRequest request) throws Exception{

        Map<String, String[]> temMap = request.getParameterMap();

        Map<String, String[]> data = new HashMap<>();

        for (Map.Entry<String, String[]> entry : temMap.entrySet()) {
            data.put(entry.getKey(), entry.getValue());
        }

        if (request.getHeader("Content-Type")!=null &&  request.getHeader("Content-Type").contains("multipart")) {
            for (Part part : request.getParts()) {
                String name = part.getName();
                String value = new String(part.getInputStream().readAllBytes());
                data.put(name, new String[]{value});
            }
        }
      
        return data;
    }

    static void verifyRequired(List<Object> lists) throws Exception{

        System.out.println("Appel de verifyRequired");
        StringBuilder message = new StringBuilder();        
        
        for(Object o : lists) {
            for(Field f : o.getClass().getDeclaredFields()){
                Required requiredAnnotation = f.getDeclaredAnnotation(Required.class) ;
                if(requiredAnnotation!=null) {
                    f.setAccessible(true);
                    System.out.println("Field nom est "+f.getName());

                    Object fieldValue = f.get(o); // Obtenir la valeur du champ
                    if (fieldValue == null || fieldValue.equals(requiredAnnotation.defaultValue())) {
                        message.append(requiredAnnotation.message()).append(" ");
                    }
                }
            }
        }

        if(message.length() > 0) throw new Exception(message.toString().trim());

    }
    
}


class CorrespondingNameParameter{

    Parameter parameter ; 
    String name ; 
    int indice ; 


    public CorrespondingNameParameter() {}

    public CorrespondingNameParameter(Parameter param, String name, int indice) {
        setParameter(param); setName(name); setIndice(indice);
    }


    public Parameter getParameter() {
        return this.parameter;
    }

    public void setParameter(Parameter parameter) {
        this.parameter = parameter;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIndice() {
        return this.indice;
    }

    public void setIndice(int indice) {
        this.indice = indice;
    }


}