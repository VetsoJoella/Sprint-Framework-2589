
# Reflection concernant controle des valeurs dans sprint : 


## framework

1. **Création d'annotaion** : 

    - @IntegerValue
    - @DoubleValue
    - @Min
    - @Max 
    - @Required 
        * tous avec des un champ message dedans * 
         chaque interface a un champ associatedClasse(class)
        * chaque interface a un champ associatedMethod *

2. **Création d'une classe** : ControlValue 
    - prenant en argument un objet
    - method : verify() 
        - boucle des fields
        - vérification si annotation existe et annotation pas une instance de Required: 
            * si oui appel de la methode dynamiquement 
                * verification de la valeur avec associatedMethod 
                + si erreur return de la valeur de message dans interface
            * sinon on passe
        - vérification si annotation existe et annotation une instance de Required: 
            * si oui appel de la methode dynamiquement 
                + verifie que la valeur n'est pas nulle ; 
            * sinon on passe

    
## Test

- creation d'une classe utilisé pour un formulaire 
- annotation des fields dedans 
- mettre des valeurs erronées dans la form
   
--- 

[Lien git du grojet ](https://github.com/VetsoJoella/Sprint-Framework-2589)

---

i