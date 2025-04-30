
package itu.springboot.util;

import itu.springboot.annotation.*;
import itu.springboot.annotation.control.Required;
import itu.springboot.classes.mapping.Verb;
import itu.springboot.classes.mulitpart.MultiPart;
import itu.springboot.exception.ModelException;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Part ; 

public class UtilController {
    
    
    public Object invoke(Object instanceOfClass, Verb verb, HttpServletRequest request, List<Object> dependances) throws Exception{

        Object response = null ;
        try {
            Method method = verb.getMethod();
            // System.out.println("L'instance de classe est "+instanceOfClass.getClass().getName());
            Object[] data = matchValues(method, request, dependances) ;
            response = method.invoke(instanceOfClass,data);
        }
        catch(Exception err){
            // err.printStackTrace();
            throw err ;
        }
        return response ;
    }

 
    // public static Object[] matchValues(Method method,Map<String,String[]> data) throws Exception{
    public Object[] matchValues(Method method, HttpServletRequest request, List<Object> dependances) throws Exception{
        
        Object[] formValues = null ;
        Parameter[] parameters = method.getParameters();

        if (parameters==null || parameters.length == 0 ) return formValues ;

        try{ 
          
            if ((request.getParameterMap() == null || request.getParameterMap().isEmpty())) { 
                return Typing.createInstance(parameters, dependances);
            }            
            if (request.getHeader("Content-Type")!=null && request.getHeader("Content-Type").contains("multipart") && (request.getParts() == null || request.getParts().isEmpty())) return formValues ;
            
            else {
                List<Object> objectToCheck = new ArrayList<>();
                StringBuilder messageVerification = new StringBuilder();
                formValues = new Object[parameters.length];

                try {
                    matchValues(objectToCheck, parameters, formValues, request, dependances);
                } catch(ModelException mErr) {
                    messageVerification.append(mErr.getMessage()).append("\n");
                } catch(Exception exc) {
                    throw exc;
                } try {
                    verifyRequired(objectToCheck);
                } catch (ModelException mErr) {
                    messageVerification.append(mErr.getMessage()).append("\n");
                    request.setAttribute("error",mErr.getMessage());
                }
                if(messageVerification.length()>0) throw new ModelException(messageVerification.toString());
            }
        }
        catch(Exception err){
            err.printStackTrace();
            throw err ;
        }
        return formValues ;
    }
    
    void matchValues(List<Object> objectToCheck, Parameter[]parameters, Object[] formValues, HttpServletRequest request, List<Object> dependances) throws Exception {

        Typing.instance(parameters, formValues, objectToCheck, dependances);                        // Instancier les attributs de la methode
        Map<String, String[]> data = listOfValueInRequest(request);
        StringBuilder messageVerification = new StringBuilder();
    
        for (Map.Entry<String, String[]> entry : data.entrySet()) {

            String key = entry.getKey();
            // System.out.println("Map est "+key);

            String[] splitKey = key.split("\\.");
            String[] values = entry.getValue();
            CorrespondingNameParameter paramObject = getParameterForData(parameters, splitKey[0], Param.class);
            if(paramObject==null) continue ; 
            try {
                if(splitKey.length==1){
                
                    if(paramObject.getParameter().getType().isArray() || paramObject.getParameter().getClass().isArray()){
                        formValues[paramObject.getIndice()] = Typing.arrayCast(request, paramObject.getName(), paramObject.getParameter().getType(), (Object[])values) ; 
                    }
                    else{
                        if(paramObject.getParameter().getType()==MultiPart.class && request.getPart(splitKey[0])!=null) { 
                            formValues[paramObject.getIndice()] = Typing.convert(request.getPart(paramObject.getName()),paramObject.getParameter().getType()) ; 
                            // System.out.println("Parametre avec multipart class");
                        } 
                        else formValues[paramObject.getIndice()] = Typing.convert(request.getParameter(paramObject.getName()),paramObject.getParameter().getType()) ; 
                        // Object str, Class<T> clazz, String name
                    }
                }
                else {
                    
                    Typing.setObject(request, formValues[paramObject.getIndice()], splitKey[1], key);
                    // System.out.println("Appel de la fonction convert dans setObject");

                }
            } catch(ModelException mErr ){
                messageVerification.append(mErr.getMessage()).append("\n");
                request.setAttribute("error-"+key, mErr.getMessage());
            } catch(Exception exc){
                throw exc ;
            }  
        } 
        if (messageVerification.length()>0) {
            throw new ModelException(messageVerification.toString());
        }
    }

    @SuppressWarnings("unchecked")
    static CorrespondingNameParameter getParameterForData(Parameter[] parameters, String name, @SuppressWarnings("rawtypes") Class annotation) throws Exception{

        // System.out.println("Name to search "+name); 

        for(int i =0; i<parameters.length ; i++){
            String nameParam = parameters[i].getName() ;
            // System.out.println("Key values "+nameParam); 
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
    static Map<String, String[]> listOfValueInRequest(HttpServletRequest request) throws Exception{

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

    void verifyRequired(List<Object> lists) throws Exception{

        // System.out.println("Appel de verifyRequired");
        StringBuilder message = new StringBuilder();  
        for(Object o : lists) {
            if(o!=null) {
                try {
                    for(Field f : o.getClass().getDeclaredFields()){
                        Required requiredAnnotation = f.getDeclaredAnnotation(Required.class) ;
                        if(requiredAnnotation!=null) {
                            f.setAccessible(true);
                            // System.out.println("Field nom est "+f.getName());
        
                            Object fieldValue = f.get(o); // Obtenir la valeur du champ
                            if (fieldValue == null || fieldValue.equals(requiredAnnotation.defaultValue())) {
                                message.append(f.getName()+" : "+requiredAnnotation.message()).append(" ");
                            }
                        }
                    }
                }
                catch(Exception err) {
                    throw err ;
                }
            }
          
        } 
        if(message.length() > 0) throw new ModelException(message.toString().trim());

    }
    

    // void needModelView(Parameter[] parameters){

    //     for (Parameter parameter : parameters) {
    //         if(parameter.getType() instanceof ModelView){

    //         }
    //     }

    // }
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