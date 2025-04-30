
package itu.springboot.util;

import itu.springboot.annotation.control.Max;
import itu.springboot.annotation.control.Min;
import itu.springboot.classes.mulitpart.MultiPart;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.lang.reflect.Parameter;
import java.sql.Timestamp;
import java.lang.reflect.Array;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Part ;
import itu.springboot.exception.ModelException;
import itu.springboot.util.date.DateUtil; 


public class Typing {

    static int isADependance(List<Object> dependances, Parameter parameter) {

        for (int i = 0; i<dependances.size(); i++) {
            // System.out.println("Type de parametre est "+parameter.getType().getName()+"  dependance est "+dependances[i].getClass().getName());
            if (parameter.getType().isInstance(dependances.get(i))) {
                return i ;
            } 
        }
        return -1 ; 
    }
    
    @SuppressWarnings("deprecation")
    public static void instance(Parameter[] parameters, Object[] formValues, List<Object> objectToCheck, List<Object> dependances) throws Exception{
        // System.out.println(" initialization ...");

        for (int i = 0 ; i<parameters.length ; i++) {
            int indice = isADependance(dependances, parameters[i]);

            if((parameters[i].getType().isArray() || parameters[i].getClass().isArray())){
                formValues[i] = allocate(0,parameters[i].getType()); 

            }
            else if(parameters[i].getType().isPrimitive()){
                formValues[i] = initiateValue(parameters[i].getType()); 
               
            } 
            else if(indice!=-1){
                // System.out.println("Adresse de redirect coté framework MATCHVALUES "+dependances[indice]);
                formValues[i] = dependances.get(indice) ;
            }
            else{
                try{
                    formValues[i] = parameters[i].getType().newInstance(); 
                } catch(Exception err) {
                    formValues[i] = null; 
                }
                objectToCheck.add(formValues[i]);
            }

        }
    }

    public static Object[] createInstance(Parameter[] parameters, List<Object> dependances) throws Exception{

        System.out.println(" Instantiation des données ...");
        List<Object> objects = new ArrayList<>() ;
        Object formValues = null ;

        for (int i = 0 ; i<parameters.length ; i++) {
            int indice = isADependance(dependances, parameters[i]);

            if((parameters[i].getType().isArray() || parameters[i].getClass().isArray())){
                formValues = allocate(0,parameters[i].getType()); 

            }
            else if(parameters[i].getType().isPrimitive()){
                formValues = initiateValue(parameters[i].getType()); 
               
            } 
            else if(indice!=-1){
                // System.out.println("Adresse de redirect coté framework MATCHVALUES "+dependances[indice]);
                formValues = dependances.get(indice) ;
            }
            // else{
            //     formValues = parameters[i].getType().newInstance(); 
            // }
            objects.add(formValues);

        }
        System.out.println("Longueur du tableaue est "+objects.size());
        return objects.toArray(new Object[0]) ; 
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

    // @SuppressWarnings("unchecked")
    public static <T> T arrayCast(HttpServletRequest request, String nameReference, Class<T> clazz, Object o) throws Exception {
        // System.out.println("Appel de arrayCast");
    
        if (clazz == MultiPart.class) {
            o = request.getParts().stream()
                    .filter(p -> p.getName().equals(nameReference))
                    .toArray(Part[]::new);
        }
    
        // // Vérification si `o` est un tableau
        // if (!o.getClass().isArray()) {
        //     throw new IllegalArgumentException("L'objet fourni n'est pas un tableau.");
        // }
    
        // Récupération du type des éléments
        Class<?> componentType = clazz.getComponentType();
        // if (componentType == null) {
        //     throw new IllegalArgumentException("La classe fournie n'est pas un tableau.");
        // }
    
        int length = Array.getLength(o);
        Object array = Array.newInstance(componentType, length);
    
        for (int i = 0; i < length; i++) {
            Object element = Array.get(o, i);
            Array.set(array, i, convert(element, componentType));
        }
    
        // return (T[]) array;
        return clazz.cast(array);

    }

    public static Object allocatePrimitiveArray(int nb, Class<?> clazz) {
        if (!clazz.isPrimitive()) {
            throw new IllegalArgumentException("La classe doit être un type primitif.");
        }
        return Array.newInstance(clazz, nb);
    }

    public static Object allocate(int nb, Class<?> clazz) {
        // Récupère le type des éléments du tableau
        Class<?> componentType = clazz.getComponentType();

        // Alloue un tableau du type approprié
        return Array.newInstance(componentType, nb);
    }

    @SuppressWarnings("unchecked")
    public static <T> T convert(Object str, Class<T> clazz) throws Exception {

        System.out.println("Valeur à caster est "+str);
       if(clazz == MultiPart.class){
            Constructor<?> constructor = clazz.getConstructor(Part.class);
            return (T)constructor.newInstance((Part)str);
        }

        if (clazz==String.class) {
            return (T)str;
        }

        if (!clazz.isPrimitive()){
            return makeAppropriateCast(str, clazz) ; 
        } 
        else{
            Method m = wrapper(clazz);
            try {
                return (T)m.invoke(null,str.toString());

            } catch (Exception e) {
                System.out.println("Erreur de cast "+e.getMessage());
                e.printStackTrace();
                return null ; 
            }
        }
       
    }

    @SuppressWarnings("unchecked")
    static <T> T makeAppropriateCast(Object str, Class<T> clazz) throws Exception {

        // Cas spécifique pour Timestamp (gérer le format avec 'T')
        if (clazz == Timestamp.class) {
            String datetimeStr = str.toString();
            return (T) DateUtil.dateStrFormatTimestamp(datetimeStr); // Convertir la chaîne en Timestamp
        }
    
        // Vérifier si un constructeur avec String existe
        try {
            Constructor<T> constructor = clazz.getConstructor(String.class);
            return constructor.newInstance(str.toString());
        } catch (NoSuchMethodException ignored) { }
    
        // Vérifier si la classe a une méthode valueOf(String)
        try {
            Method method = clazz.getMethod("valueOf", String.class);
            return (T) method.invoke(null, str.toString());
        } catch (NoSuchMethodException ignored) { }
    
        // Vérifier s'il existe une méthode de type parseNomClasse(String)
        try {
            Method method = clazz.getMethod("parse" + clazz.getSimpleName(), String.class);
            return (T) method.invoke(null, str.toString());
        } catch(NoSuchMethodException ignored) {
            throw new NoSuchMethodException("Aucun constructeur ou méthode 'valueOf' ou 'parse' trouvé pour cette classe: " + clazz.getName());
        }
    }
    


    @SuppressWarnings("unchecked")
    public static <T> T initiateValue(Class<T> clazz){

        // System.out.println(clazz.getName());
        return (T) Array.get(Array.newInstance(clazz, 1),0);
    }

    
    public static void setObject(HttpServletRequest request, Object o, String methodName, String nameReference) throws Exception{

        String methodNameCapitalize = Util.capitalize(methodName);
        Method m = getMethod(o,"set"+methodNameCapitalize);
        // System.out.println("Methode name est "+methodNameCapitalize);

        Field fieldRelatedToMethod = o.getClass().getDeclaredField(methodName);        
        
        if(fieldRelatedToMethod!=null) {
            valueControl(fieldRelatedToMethod, request.getParameter(nameReference));
        }
        Parameter[] parameters = m.getParameters();
        if(parameters==null || parameters.length==0) return ;

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
