package itu.springboot.classes.session ;

import java.util.*;

public class Session {
    private Map<String, Object> map;

    // Constructeur par défaut initialisant une HashMap
    public Session() {
        this.map = new HashMap<>();
    }

    // Getter pour récupérer le Map complet
    public Map<String, Object> getMap() {
        return map;
    }

    // Setter pour initialiser le Map avec une instance existante
    public void setMap(Map<String, Object> map) {
        this.map = map;
    }

    // Getter pour récupérer la valeur associée à une clé
    public Object get(String key) {
        if(!map.containsKey(key)){
            return null ;
        }
        return map.get(key);
    }

    // Méthode pour ajouter une nouvelle entrée dans le Map
    public void add(String key, Object value) {
        System.out.println("Ajout de valeur dans data session "+key+" valeur est "+ value);
        map.put(key, value);
    }

    // Méthode pour supprimer une entrée dans le Map
    public void remove(String key) {
        map.remove(key);
    }



    @Override
    public String toString() {
        String string = "" ;
        Map<String,Object> sessionValues = getMap() ;
        for(String key : sessionValues.keySet()) {
            string+= "Cles "+key +" : "+sessionValues.get(key);
        }
        return string ;
        // return "{" +
            
        //     "}";
    }

    
}
