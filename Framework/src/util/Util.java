package util;

import annotation.Get;
import annotation.Post;
import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Vector ;

public class Util {
    
    // Load des methodes
    public static String[] loadData(String param, String path, Class annotation) throws Exception{

        Vector<String> liste = new Vector<>();
        Vector<String> filterList = new Vector<>();
        File file = new File(path);
        
        if (file.exists()==false) {
            throw new Exception("Le dossier controllerPath n'existe pas ");
        }
        findClassFiles(file, param , liste);
        for(String l:liste){
            
            String pack = l.replace(param+".","");

            Class<?> clazz = Class.forName(pack);
            boolean isClassAnnotationPresent = clazz.isAnnotationPresent(annotation);

            if (isClassAnnotationPresent) {
                filterList.add(pack);
                // System.out.println(pack);
            }
        }
        if(filterList.size()==0){
            throw new Exception("Il n'y aucune classe dans le dossier contollerPath ");
        }
        //Call to getController + writing in listeClass.txt
      
        return filterList.toArray(new String[filterList.size()]);
    
    }

    // Recherche des classes ayant une annotation controller

    public static void findClassFiles(File file, String currentPath,Vector<String> liste){

        if(file.isDirectory()){
            File[] files = file.listFiles();
            if(files!=null){
                for(File f : files){
                    String actu = currentPath ;
                    if(f.isDirectory()){
                        actu+= "."+f.getName();
                        findClassFiles(f,actu,liste);
                    }
                    else if(f.isFile() && f.getName().endsWith(".class")){
                        actu += "."+ f.getName().replace(".class", "");
                        // System.out.println("Dossier est "+currentPath);
                        liste.add(actu);
                    }
                }
            }
        }
    }


    // Vérification de la présence d'une annotation dans une méthode
    public static boolean isAnnotationPresent(Method method, Class<? extends Annotation>annotation){
         if(method.isAnnotationPresent(annotation)){
            return true ;
        }
        return false ;
    }

    // Vérification de la présence d'une annotation sur un objet
    public static boolean isAnnotationPresent(Object object, Class<? extends Annotation>annotation){
        if(object.getClass().isAnnotationPresent(annotation)){
           return true ;
       }
       return false ;
   }

    // Vérification de la présence d'une annotation sur un field
   public static boolean isAnnotationPresent(Field field, Class<? extends Annotation>annotation){
        if(field.isAnnotationPresent(annotation)){
            return true ;
        }
        return false ;
    }

    // Vérification si les annotations dans le tableau des annotation suivant sont présents 
    public static boolean isAnnotationPresent(Field field, Class<? extends Annotation>[]annotations){
        for(Class<? extends Annotation> annotation : annotations) {

            if(isAnnotationPresent(field, annotation)){
                return true ; 
            } 

        } return false  ; 
        // if(field.isAnnotationPresent(annotation)){
        //     return true ;
        // }
        // return false ;
    }


    // Fonction de capitalisation 
    public static String capitalize(String string){
        return Character.toUpperCase(string.charAt(0)) + string.substring(1);
    }

    // Fonction de décapitalisation 
    public static String decapitalize(String string){
        return string.toLowerCase();
    }

    // Get de l'annotation présent sur la méhode
    public static String getVerbFromAnnotation(Method method){

        String annotation = "Get";
        Class<? extends Annotation>[] listesAnnotationPossible = new Class[]{Get.class, Post.class};
        Annotation[] annotations = method.getDeclaredAnnotations();
        
        for (Annotation a : annotations) {
            for (Class<? extends Annotation> possibleAnnotation : listesAnnotationPossible) {
                if (a.annotationType().equals(possibleAnnotation)) {
                    annotation = a.annotationType().getSimpleName(); 
                    return annotation ;
                }
            }
        }
        return annotation;
    }

    // public Map getMethod

    // public static Mapping getFromM()
  
  
}
