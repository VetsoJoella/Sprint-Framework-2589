package util;

import annotation.AnnotationController;

import java.io.IOException;
import java.io.File ;
import java.util.Vector;

public class Util {

    
    public static String[] loadData(String param, File file, Class annotation){

        // ServletContext context = getServletContext();
        // String path = context.getRealPath(param); 

        Vector<String> liste = new Vector<>();
        Vector<String> filterList = new Vector<>();
       

        try {
            findClassFiles(file, param , liste);
            for(String l:liste){
                
                String pack = l.replace(param+".","");

                Class<?> clazz = Class.forName(pack);
                boolean isClassAnnotationPresent = clazz.isAnnotationPresent(annotation);
    
                if (isClassAnnotationPresent) {
                    filterList.add(pack);
                }
            }
            //Call to getController + writing in listeClass.txt
        }
        catch(Exception err){}
        finally{
            return filterList.toArray(new String[filterList.size()]);

        }
      
    
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
}
