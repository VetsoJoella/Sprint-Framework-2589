package itu.springboot.annotation.security.auth;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE}) // Peut être utilisé sur une méthode ou une classe
@Retention(RetentionPolicy.RUNTIME) // Disponible à l'exécution
public @interface Auth {
    // Vous pouvez ajouter des éléments ici si nécessaire
}

