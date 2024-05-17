
Sprint 1 : Analyse  (localhost:8080/Liste Employe/emp/liste)

    QUOi?
        -> faire pour que tout les liens soient tous associes a un controlleur 
        -> Traitement requete (url) :
            . ?controller (classe)
            . ?action (methode)

    COMMENT?

        Part I 
            Framework :

                -> creation de classe FrontController (servlet) : (1)
                    . processRequest(req,res)
                    . doGet(req,res)
                    . doPost(req,res)

            Test : 

                -> declaration du servlet dans le web.xml et url pattern : *

        Part II 
            Framework : 

                ->  Creation Annotation : 
                    . niveau class
                    . niveau method
                -> Creation de Servlet Scan : Scan , annotation (scope)/ (source/getter )
                    --> enre de servlet memoire gardant la correspondance entre les liens et la classe et methode appelle
                -> Creation de servlet Dispatch 
                    --> pour la Redirection vers le chemin de recherche du controlleur 

            Test :

                -> creation de classe annotee @annotationClass
                -> creation de methode annotee @annotationMethod
                -> appel d'un lien avec l'annotation de la classe
          