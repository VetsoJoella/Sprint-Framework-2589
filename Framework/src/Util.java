package util;

import annotation.AnnotationController;
import mapping.Mapping;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.io.File ;
import java.util.HashMap;
import java.util.Vector;

public class Util {
    
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
            }
        }
        if(filterList.size()==0){
            throw new Exception("Il n'y aucune classe dans le dossier contollerPath ");
        }
        //Call to getController + writing in listeClass.txt
      
        return filterList.toArray(new String[filterList.size()]);
    
    }

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

    public static boolean isDuplicated(HashMap hashMap,String key) throws Exception{

        if(hashMap.containsKey(key)){
            Mapping m = (Mapping)hashMap.get(key);
            String erreur = "L'url "+key+" est dupliquée.\n Elle existe déja dans la classe "+m.getClassName()+" avec la methode "+m.getMethod(); 
            hashMap.clear();
            throw new Exception(erreur);
            
        }
        return false ;
    }

  

    public static boolean isAnnotationPresent(Method method, Class<? extends Annotation>annotation){

        if(method.isAnnotationPresent(annotation)){
            return true ;
        }
        return false ;

    }

    public static String capitalize(String string){
        return Character.toUpperCase(string.charAt(0)) + string.substring(1);
    }
  
  
}
