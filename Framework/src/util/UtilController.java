
package util;

import annotation.*;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Map;
import mapping.Verb;

public class UtilController {
    
    
    public static Object invoke(Object instanceOfClass, Verb verb, Map<String, String[]> formValue) throws Exception{

        Object response = null ;
        try {
            
            Method method = verb.getMethod();
            Object[] data = matchValues(method, formValue) ;
            response = method.invoke(instanceOfClass,data);
        }
        catch(Exception err){
            err.printStackTrace();
            throw err ;
        }
        return response ;
    }

    // public static Object invoke(Object instanceOfClass, Verb verb, HttpServletRequest request)parts throws Exception{

    //     Object response = null ;
    //     try {
            
    //         Method method = verb.getMethod();
    //         Object[] data = matchValues(method, request) ;
    //         response = method.invoke(instanceOfClass,data);
    //     }
    //     catch(Exception err){
    //         err.printStackTrace();
    //         throw err ;
    //     }
    //     return response ;
    // }

    // public static Object[] matchValues(Method method, HttpServletRequest request) throws Exception{
        
    //     Object[] formValues = null ;

    //     try{ 
    //         if(data==null){
    //             return formValues ;
    //         }
    //         else {
                
    //             Parameter[] parameters = method.getParameters();
    //             // checkAnnotation(parameters);
    //             formValues = new Object[parameters.length];
    //             Typing.instance(parameters, formValues);                                       // Instancier les attributs de la methode

    //             Map<String, String[]> parameterMap = request.getParameterMap();

    //             for (Map.Entry<String, String[]> entry : data.entrySet()) {
                       
    //                 String key = entry.getKey();
    //                 String[] splitKey = key.split("\\.");
    //                 String[] values = entry.getValue();
    //                 Object[] paramObject =getParameterForData(parameters,splitKey[0],Param.class);

    //                 Parameter param = (Parameter)paramObject[0] ; int indice = (int)paramObject[1]; String name = (String)paramObject[2];

    //                 if(splitKey.length==1){

    //                     if(param.getType().isArray() || param.getClass().isArray()){
    //                         formValues[indice] = Typing.arrayCast(values,param.getType(), name) ; 
    //                     }
    //                     else{
    //                         formValues[indice] = Typing.convert(values[0],param.getType(), name) ; 
    //                     }
    //                 }
    //                 else {
                       
    //                     Typing.setObject(formValues[indice],values,splitKey[1]);
                       
    //                 }
                    
    //             }
    //         }
    //     }
    //     catch(Exception err){
    //         err.printStackTrace();
    //         throw err ;
    //     }
    //     return formValues ;
    // }

    public static Object[] matchValues(Method method,Map<String,String[]> data) throws Exception{
        
        Object[] formValues = null ;

        try{ 
            if(data==null){
                return formValues ;
            }
            else {
                
                Parameter[] parameters = method.getParameters();
                // checkAnnotation(parameters);
                formValues = new Object[parameters.length];
                Typing.instance(parameters, formValues);                                       // Instancier les attributs de la methode
                
                for (Map.Entry<String, String[]> entry : data.entrySet()) {

                    String key = entry.getKey();
                    String[] splitKey = key.split("\\.");
                    String[] values = entry.getValue();
                    Object[] paramObject =getParameterForData(parameters,splitKey[0],Param.class);

                    Parameter param = (Parameter)paramObject[0] ; int indice = (int)paramObject[1]; String name = (String)paramObject[2];

                    if(splitKey.length==1){

                        if(param.getType().isArray() || param.getClass().isArray()){
                            formValues[indice] = Typing.arrayCast(values,param.getType(), name) ; 
                        }
                        else{
                            formValues[indice] = Typing.convert(values[0],param.getType(), name) ; 
                        }
                    }
                    else {
                       
                        Typing.setObject(formValues[indice],values,splitKey[1]);
                       
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
    

    public static Object[] getParameterForData(Parameter[] parameters,String name,Class annotation) throws Exception{

        for(int i =0; i<parameters.length ; i++){
            String nameParam = parameters[i].getName() ; 
            if(parameters[i].isAnnotationPresent(annotation)){
                nameParam = ((Param)parameters[i].getAnnotation(Param.class)).name();
            }
           if(nameParam.equalsIgnoreCase(name)){
                return new Object[]{parameters[i],i, nameParam};
           }
            // else {
            //     throw new Exception("ETU 2589 : annotation n'est pas présente sur l'attribut "+(parameters[i]).getName());
            // }
        }
        return null ;
    
    }

    public static void checkAnnotation(Parameter[] parameters) throws Exception{

        for(int i =0; i<parameters.length ; i++){
           
            if(parameters[i].isAnnotationPresent(Param.class)==false){
          
            
                throw new Exception("ETU 2589 : annotation n'est pas présente sur l'attribut "+(parameters[i]).getName());
            }
            
        }
       
    
    }
    
}