
Sprint 0 : Analyse  

    Creation de bracnhe Sprint0-2589
    Repertoire de travail : 
        Framework
            . script.bat [deplacr dans le lib dans le dossier test]
        Test
            . declaration de web.xml [declaration du FrontController, url-param : /] 
            . test local : taper npq lien et verifier que ca doit atterir dans le FrontController
            
    QUOi?
      -> n'importe quel projet doivent atterir dans un seul servlet

    COMMENT?

        -> creation d'une classe FrontController extends servlet 
        -> Method doGet - doPost appellent tous une methode processRequest + affichage du lien tape

        