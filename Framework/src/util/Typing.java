
package util;

import annotation.*;
import annotation.control.Max;
import annotation.control.Min;
import annotation.control.Required;
import exception.ModelException;
import mapping.Mapping;
import mulitpart.MultiPart;
import util.Util;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.io.File ;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;
import java.lang.reflect.Array;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Part ; 


public class Typing {
    
    public static void instance(Parameter[] parameters, Object[] formValues, List<Object> objectToCheck) throws Exception{
        // System.out.println(" initialization ...");

        for (int i = 0 ; i<parameters.length ; i++) {
            
            if(parameters[i].getType().isArray() || parameters[i].getClass().isArray()){
                formValues[i] = allocate(0,parameters[i].getType()); 

            }
            else if(parameters[i].getType().isPrimitive()){
                formValues[i] = initiateValue(parameters[i].getType()); 
               
            }
            else{
                formValues[i] = parameters[i].getType().newInstance(); 
                objectToCheck.add(formValues[i]);
            }

        }
    }

    // public static <T> T[] arrayCast(Object o,Class<T> clazz) throws Exception{

    //     int length = ((Object[]) o).length;
    //     T[] array = allocate(((Object[])o).length, clazz) ; 
    //     for (int i = 0; i < length; i++) {
    //         Object element = ((Object[]) o)[i];
    //         // Convert each element individually, assuming convert method exists
    //         array[i] = (T) convert(element, clazz.getComponentType());
    //     }
    //     return array;
      
    // }

    public static <T> T[] arrayCast(HttpServletRequest request, String nameReference, Class<T> clazz, Object o) throws Exception{

        if(clazz == MultiPart.class){
            o = request.getParts().stream().filter(p -> p.getName().equals(nameReference)).toArray(Part[]::new);
            
        }

        int length = ((Object[]) o).length;
        T[] array = allocate(((Object[])o).length, clazz) ; 
        for (int i = 0; i < length; i++) {
            Object element = ((Object[]) o)[i];
            // Convert each element individually, assuming convert method exists
            array[i] = (T) convert(element, clazz.getComponentType());
        }
        return array;
      
    }

    public static <T>T[] allocate(int nb, Class<T> clazz){

        T[] array = (T[]) Array.newInstance(clazz.getComponentType(), nb);
        return array;
    }

    public static <T> T convert(Object str, Class<T> clazz) throws Exception {

       if(clazz == MultiPart.class){
            Constructor<?> constructor = clazz.getConstructor(Part.class);
            return (T)constructor.newInstance((Part)str);
        }

        if (clazz==String.class) {
            return (T)str;
        }
        if (!clazz.isPrimitive()){
            System.out.println("Classe n'est pas primivie "+clazz);

            Constructor<?> constructor = clazz.getConstructor(String.class);
            return (T)constructor.newInstance(str);
        } 
        else{
            Method m = wrapper(clazz);
            return (T)m.invoke(null,str.toString());
        }
       
    }


    public static <T> T initiateValue(Class<T> clazz){

        // System.out.println(clazz.getName());
        return (T) Array.get(Array.newInstance(clazz, 1),0);
    }

    
    public static void setObject(HttpServletRequest request, Object o, String methodName, String nameReference) throws Exception{

        String methodNameCapitalize = Util.capitalize(methodName);
        Method m = getMethod(o,"set"+methodNameCapitalize);
        System.out.println("Methode name est "+methodNameCapitalize);

        Field fieldRelatedToMethod = o.getClass().getDeclaredField(methodName);        
        
        if(fieldRelatedToMethod!=null) {
            valueControl(fieldRelatedToMethod, request.getParameter(nameReference));
        }
        Parameter[] parameters = m.getParameters();
        if(parameters==null && parameters.length==0) return ;

        if(parameters[0].getType().isArray() || parameters[0].getClass().isArray()){
            // m.invoke(o, arrayCast(data,parameters[0].getType()) );
            m.invoke(o, arrayCast(request, nameReference, parameters[0].getType(), o));

        }
        else {
            if((parameters[0].getType()==MultiPart.class) && request.getHeader("Content-Type").contains("multipart")) m.invoke(o,convert(request.getPart(nameReference), parameters[0].getType()));

            else m.invoke(o,convert(request.getParameter(nameReference), parameters[0].getType()));
        }

    }

    static Method getMethod(Object o, String methodName){

        Method[] methods = o.getClass().getDeclaredMethods();
        for(int i =0; i<methods.length ;i++){
            if(methods[i].getName().equalsIgnoreCase(methodName)){
                return methods[i];
            }
        }
        return null ;
    }

    public static Method wrapper(Class<?> clazz) throws Exception{

        if (clazz == int.class) return Integer.class.getMethod("parseInt", String.class);
        if (clazz == long.class) return Long.class.getMethod("parseLong", String.class);
        if (clazz == float.class) return Float.class.getMethod("parseFloat", String.class);
        if (clazz == double.class) return Double.class.getMethod("parseDouble", String.class);
        if (clazz == byte.class) return Byte.class.getMethod("parseByte", String.class);
        if (clazz == short.class) return Short.class.getMethod("parseShort", String.class);
        return null;
        
    }  

    static void valueControl(Field field, Object fieldValue) throws ModelException {        // Control des valeurs lors des setObject
        StringBuilder message = new StringBuilder();
        // Vérification de l'annotation @Min
        Min minAnnotation = field.getAnnotation(Min.class);
        if (minAnnotation != null) {
            field.setAccessible(true);
            if (Double.valueOf(fieldValue.toString()) < minAnnotation.defaultValue()) {
                message.append(minAnnotation.message()).append(" ");
            }
        }
    
        // Vérification de l'annotation @Max
        Max maxAnnotation = field.getAnnotation(Max.class);
        if (maxAnnotation != null) {
            if (Double.valueOf(fieldValue.toString()) > maxAnnotation.defaultValue()) {
                message.append(maxAnnotation.message()).append(" ");
            }
        }
    
        if (message.length() > 0) {
            throw new ModelException(field.getName()+" : "+message.toString().trim());
        }
    }    

}
