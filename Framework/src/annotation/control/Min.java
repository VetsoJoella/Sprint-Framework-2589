package annotation.control;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME) // Rend l'annotation disponible au runtime
@Target(ElementType.FIELD) // Permet l'utilisation uniquement sur les champs

public @interface Min {

    String message() default "Valeur inférieur" ;
    double defaultValue() default 0;

}


