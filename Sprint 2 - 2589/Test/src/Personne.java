package com.controller ;

import annotation.AnnotationController ;
import annotation.Get ;

import java.io.IOException;
import java.io.File ;
import java.util.Vector;

@AnnotationController

public class Personne{

    @Get(url="/find/classes")
    private void findClassFiles(File file, String currentPath,Vector<String> liste){

        if(file.isDirectory()){
            System.out.println("Name is "+file.getName());
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

    
    public static void main(String[] args) {
        // Personne p = new Personne();
        // System.out.println("Hello");
        String path = "test";
        File file = new File(path);
        Vector<String> liste = new Vector<>();
        new Personne().findClassFiles(file,path,liste);

        System.out.println("Liste est : ");

        for(int i = 0; i<liste.size(); i++){
            System.out.println(liste.get(i));
        }
        

    }
}