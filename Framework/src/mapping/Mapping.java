package mapping ;
import java.lang.reflect.Method;
import java.util.HashMap;

public class Mapping {

    String className;
    // String method ;
    // Class<?>[] parameterTypes ;
    Method method ;
    String verb ;


    // public Class<?>[] getParameterTypes() {
    //     return parameterTypes;
    // }

    // public void setParameterTypes(Class<?>[] parameterTypes) {
    //     this.parameterTypes = parameterTypes;
    // }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public String getVerb(){return verb;}

    public void setVerb(String verb){
        this.verb = verb ; 
    }

    public static Mapping get(HashMap<String, Mapping> hashMap, String link, String verb) throws Exception{

        Mapping map = hashMap.get(link);
        if(map==null){
            throw new Exception("La méthode mappé avec l'url "+link+" n'existe pas");
        } if(!map.getVerb().equalsIgnoreCase(verb)){
            throw new Exception("L'url "+link+" n'existe pas avec la méthode "+verb);
        }
        return map ;

    }

    public Mapping(){}

    public Mapping(String className, Method method, String verb) {

        setClassName(className);
        setMethod(method);
        setVerb(verb);
    }
    

}