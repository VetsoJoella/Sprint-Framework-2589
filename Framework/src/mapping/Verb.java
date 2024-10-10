
// Code enregistrant le verb et la méthode associer avec
package mapping;

import java.lang.reflect.Method;

public class Verb {
    
    String verb ; 
    Method method ; 

    public Verb() {}

    public Verb(String verb, Method method) {
       setVerb(verb); setMethod(method);
    }

    public String getVerb() {
        return this.verb;
    }

    public void setVerb(String verb) {
        this.verb = verb;
    }

    public Method getMethod() {
        return this.method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Verb verb(String verb) {
        setVerb(verb);
        return this;
    }

    public Verb method(Method method) {
        setMethod(method);
        return this;
    }


    // Surcharge de la méthode equals
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof  Verb){
            if(getVerb().equalsIgnoreCase(((Verb)obj).getVerb())){
                return true ; 
            }
            // if(getMethod().getName().equalsIgnoreCase(((Verb)obj).getMethod().getName())){
            //     return true ; 
            // }
        }
        return false ;
    }

    // Surcharge de la méthode hashCode
    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + (verb != null ? verb.hashCode() : 0);
        // result = 31 * result + age;
        return result;
    }

   

    
}
