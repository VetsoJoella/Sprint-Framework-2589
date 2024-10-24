package mapping ;

import exception.NotFoudException;
import java.util.HashSet; 
import java.util.Iterator;

public class Mapping {

    String className;
    // String verb ;
    // List<Verb> verbs = new ArrayList<>() ;
    HashSet<Verb> verbs = new HashSet<>();


    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }
    
    // public List<Verb> getVerbs() {
    //     return verbs;
    // }

    // public void setVerbs(List<Verb> verbs) {
    //     this.verbs = verbs;
    // }

    // public void add(Verb verb){
    //     getVerbs().add(verb);
    // }

    public Verb get(String verb) throws Exception{

        Iterator<Verb> iterator = verbs.iterator();

        while (iterator.hasNext()) {
            Verb v = iterator.next();
            // System.out.println("Verb est "+ v.getVerb()+" method est "+v.getMethod().getName());
            if(v.getVerb().equalsIgnoreCase(verb)){
                return v ;
            }
        }
        // for(Verb v : getVerbs()){
        //     if(v.getVerb().equalsIgnoreCase(verb)){
        //         return v ;
        //     }
        // }
       throw new NotFoudException("La méthode recherché avec l'url et la methode "+verb+" n'existe pas");

    }

    public HashSet<Verb> getVerbs(){
        return verbs ;
    }

    public void setVerbs(HashSet<Verb> hashSet){
        this.verbs = hashSet ;
    }

    public Mapping(){}

    public Mapping(String className) {

        setClassName(className);
    }

}