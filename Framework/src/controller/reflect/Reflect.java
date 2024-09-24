package controller.reflect ;

import java.lang.reflect.Field; 

import util.Util ; 

public class Reflect {


    /* public static boolean fieldExist(Class<?> clazz, Class<?> toCheck) {
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            if (field.getType().equals(toCheck)) {
                return true;
            }
        }
        return false;
    } */

    
    public static Field fieldExist(Class<?> clazz, Class<?> toCheck) {
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            if (field.getType().equals(toCheck)) {
                return field;
            }
        }
        return null;
    }

    public static void setObject(Object obj, String fieldName, Object value) throws Exception {
        Class<?> clazz = obj.getClass();
        Field field = clazz.getDeclaredField(Util.decapitalize(fieldName));
        field.setAccessible(true); // Permet d'accéder aux champs privés

        // Définit la valeur du champ spécifié pour l'objet donné
        field.set(obj, value);
    }
}