package itu.springboot.controller.security;

import javax.naming.AuthenticationException;

import itu.springboot.annotation.security.auth.Auth;
import itu.springboot.annotation.security.auth.role.Role;
import itu.springboot.classes.mapping.Verb;
import itu.springboot.controller.session.SessionManager;
import itu.springboot.exception.security.auth.RoleException;

public class RoleManager {

    String authName; 

    public String getAuthName() {
        return authName;
    }

    public void setAuthName(String authName) {
        this.authName = authName;
    }

    public RoleManager(String auth) {
        setAuthName(auth);
    }

    public void verifyAccess(SessionManager sessionManager, Object object, Verb verb) throws Exception {
        System.out.println("Valeur de référence " + authName);
        Object userObject = sessionManager.getHttpSession().getAttribute(authName);
        if (requiresAuthentication(object, verb)) {
            if (userObject == null) {
                throw new AuthenticationException("Utilisateur non authentifié");
            }
        }
    
        if (verb.getMethod().isAnnotationPresent(Role.class)) {
            validateRoles(userObject, verb.getMethod().getDeclaredAnnotation(Role.class).value());
        }
    
        if (object.getClass().isAnnotationPresent(Role.class)) {
            validateRoles(userObject, object.getClass().getDeclaredAnnotation(Role.class).value());
        }
    }
    
    // Méthode pour vérifier si l'authentification est requise
    boolean requiresAuthentication(Object object, Verb verb) {
        return object.getClass().isAnnotationPresent(Auth.class)
            || object.getClass().isAnnotationPresent(Role.class)
            || verb.getMethod().isAnnotationPresent(Auth.class)
            || verb.getMethod().isAnnotationPresent(Role.class);
    }
    
    // Méthode pour valider les rôles
    private void validateRoles(Object userObject, Class<?>[] requiredRoles) throws RoleException {
        String roles = "" ;
        for (Class<?> roleClass : requiredRoles) {
            if (roleClass.isInstance(userObject)) {
                return; // Rôle valide
            } roles+= roleClass +", " ;
        }
        throw new RoleException(" Vous n'êtes pas autorisé à accéder à ces méthodes ( votre role "+userObject.getClass() +" requis : "+roles+" )");
    }
}
