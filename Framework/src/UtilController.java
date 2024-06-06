
package util;

import annotation.AnnotationController;
import mapping.Mapping;

import java.io.IOException;
import java.lang.reflect.Method;
import java.io.File ;
import java.util.HashMap;
import java.util.Vector;
import java.lang.annotation.Annotation;

public class UtilController {
    
    public static void getMethodHavingAnnotation(HashMap hashMap, String classe, Class<? extends Annotation> annotation) throws Exception{

        Class clazz = Class.forName(classe);
        Method[] methods = clazz.getDeclaredMethods();

        for (Method method : methods) {
            if (method.isAnnotationPresent(annotation)) {

                Annotation annotationCoresp = method.getAnnotation(annotation);
                // Use reflection to call the 'url' method on the annotation
                Method urlMethod = annotation.getDeclaredMethod("url");
                String value = (String) urlMethod.invoke(annotationCoresp);
              
                if(hashMap.containsKey(value)){
                    hashMap.clear();
                    throw new Exception("L'url "+value+" est dupliquée");
                    
                }
                else{
                    hashMap.put(value,new Mapping(classe,method.getName()));
                }
            }
        }

    }

    public static Object invoke(Mapping map) throws Exception{

        Object response = null ;
        try {
            Class clazz = Class.forName(map.getClassName());
            Method method = clazz.getMethod(map.getMethod());
            response = method.invoke(clazz.newInstance());
        }
        catch(Exception err){
            err.printStackTrace();
            throw err ;
        }
        return response ;
    }
}