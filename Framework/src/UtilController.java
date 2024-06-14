
package util;

import annotation.*;
import mapping.Mapping;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.io.File ;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Array;

public class UtilController {
    
    
    public static Object invoke(Mapping map, Map<String, String[]> formValue) throws Exception{

        Object response = null ;
        try {
            Class clazz = Class.forName(map.getClassName());
            Method method = clazz.getMethod(map.getMethod(),map.getParameterTypes());
            Object[] data = macthValues(method, formValue) ;
            response = method.invoke(clazz.newInstance(),data);
        }
        catch(Exception err){
            err.printStackTrace();
            throw err ;
        }
        return response ;
    }

    public static Object[] macthValues(Method method,Map<String,String[]> data){
        
        Object[] formValues = null ;

        try{ 
            if(data==null){
                return formValues ;
            }
            else {
                // for (Map.Entry<String, String[]> entry : data.entrySet()) {
                //     String paramName = entry.getKey();
                //     String[] paramValues = entry.getValue();
                // }
                formValues = new Object[data.size()];
                Parameter[] parameters = method.getParameters();

                for (int i = 0 ; i<parameters.length ; i++) {
                    
                    String[] value = null ;
                    if(parameters[i].isAnnotationPresent(Param.class)){

                        String name = ((Param)parameters[i].getAnnotation(Param.class)).name();
                        value = data.get(name);

                    } else {
                       
                        value = data.get(parameters[i].getName());
                    }

                    if(parameters[i].getType().isArray() || parameters[i].getClass().isArray()){
                        formValues[i] = arrayCast(value,parameters[i].getType()) ; 
                    }
                    else{
                        formValues[i] = convert(value[0],parameters[i].getType()) ; 
                    }
                }
            }
        }
        catch(Exception err){
            err.printStackTrace();
        }
        return formValues ;
    }

    // public static Method getMethodWithAnnotation(Class clazz,String url,Class<? extends Annotation>annotation){

    //     for (Method method : clazz.getDeclaredMethods()) {
    //         // Vérifier si la méthode est annotée avec l'annotation spécifique
    //         if (method.isAnnotationPresent(annotation) && url.equalsIgnoreCase(method.getAnnotation(annotation).url())) {
    //             // Si la méthode est annotée, renvoyer la méthode
    //             return method;
    //         }
    //     }
    //     return null ;
    // }


    public static <T> T[] arrayCast(Object o,Class<T> clazz) throws Exception{

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

        if (clazz==String.class) {
            return (T)str;
        }
        if (!clazz.isPrimitive()){
            Constructor<?> constructor = clazz.getConstructor(String.class);
            return (T)constructor.newInstance(str);

        } else{
            Method m = wrapper(clazz);
            return (T)m.invoke(null,str.toString());
        }
       
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

    
}