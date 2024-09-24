package mapping ;


public class Mapping {

    String className;
    String method ;
    Class<?>[] parameterTypes ;


    public Class<?>[] getParameterTypes() {
        return parameterTypes;
    }
    public void setParameterTypes(Class<?>[] parameterTypes) {
        this.parameterTypes = parameterTypes;
    }
    public String getClassName() {
        return className;
    }
    public void setClassName(String className) {
        this.className = className;
    }
    public String getMethod() {
        return method;
    }
    public void setMethod(String method) {
        this.method = method;
    }
    public Mapping(String className, String method,Class<?>[] parameterTypes) {
        setClassName(className);
        setMethod(method);
        setParameterTypes(parameterTypes);
    }
    

}