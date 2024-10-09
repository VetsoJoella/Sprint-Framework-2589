package mapping ;
import java.util.ArrayList;
import java.util.List;

public class Mapping {

    String className;
    // String verb ;
    List<Verb> verbs = new ArrayList<>() ;


    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }
    
    public List<Verb> getVerbs() {
        return verbs;
    }

    public void setVerbs(List<Verb> verbs) {
        this.verbs = verbs;
    }

    public void add(Verb verb){
        getVerbs().add(verb);
    }

    public Verb get(String verb) throws Exception{

        for(Verb v : getVerbs()){
            if(v.getVerb().equalsIgnoreCase(verb)){
                return v ;
            }
        }
       throw new Exception("La méthode recherché avec l'url et la methode "+verb+" n'existe pas");

    }

    public Mapping(){}

    public Mapping(String className) {

        setClassName(className);
    }

    
    // Vérifcation du doublon

    // public static boolean isDuplicated(HashMap hashMap,String key) throws Exception{

    //     if(hashMap.containsKey(key)){
    //         Mapping m = (Mapping)hashMap.get(key);
    //         String erreur = "L'url "+key+" est dupliquée.\n Elle existe déja dans la classe "+m.getClassName()+" avec la methode "+m.getMethod(); 
    //         hashMap.clear();
    //         throw new Exception(erreur);
            
    //     }
    //     return false ;
    // }
  
    

}