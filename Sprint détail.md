#### But créer : un framework comme spring boot en utilisant java 

## Sprint : 
### Sprint 0 : 
    - But : n'importe quel projet doivent atterir dans un seul servlet
    - Etapes : 
        . creation d'une classe FrontController extends servlet 
        . Method doGet - doPost appellent tous une methode processRequest + affichage du lien tape

### Sprint 1 :
    - But : faire pour que tout les liens soient tous associes a un controlleur 
    - Etapes : 
        . creation de classe FrontController (servlet)

### Sprint 2 : 
    - But : afficher la classe et la methode associée aà l'url tapé 
    - Etapes : 
        . creation de classe FrontController (servlet)

### Sprint 3 : 
    - But : afficher appelé la méthode de l'url tap
    - Etapes : 
        . appel de la methode via reflect dans process request 

### Sprint 4 : 
    - But : envoyer les données des method évoquées dans les vues 
    - Etapes :
        . creation de classe ModelView : String className, String method
        . vérification de l'instance du return 

### Sprint 5 : 
    - But : throwint the errors during build and response 
    - Etapes : 
        . Ajout de vérification de l'existence de la classe et de l'url dans le mapping 
        . vérification de l'instance du return

### Sprint 6 : 
    - But : 
        . getter les valeurs en parametres dans le frontcontroller 
        . matcher les valeurs avec les arguments des méthodes
        . invoquer la fonction 
        . afficher le type de retour
    - Etapes : 
        . création de match des arguments 

### Sprint 7 : 
    - But : 
        . envoyer un en argument 
        . instancier les attributs dans l'objet
  

### Sprint 8 : 
    - But : 
        . insérer, modifier des elements de la session  
    - Etapes : 
        . création de la classe session : __attr __ : map String,objet
        . vérification des attributs de la classe si il y a un objet de type session

### Sprint 9 : 
    - But : renvoyer une réponse json au lieu de ModelAndView  
    - Etapes : 
        . création de la classe annotation RestApi 
        . vérification de la classe si annotée RestApi 
            -> OUI : 
                -> get de la réponse : 
                    * reponse instanceOf ModelAndView get des data en json
                    * sinon object caste en gson

### Sprint 10 : 
    - But : utiliser des methodes post
    - Etapes :
 