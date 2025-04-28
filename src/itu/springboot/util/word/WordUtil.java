package itu.springboot.util.word;

public class WordUtil {
    
    public static String extractAfter(String fullWord, String referenceWord) {

        // Trouver l'index du mot
        int index = fullWord.indexOf(referenceWord);
        String result = null ; 

        if (index != -1) result = fullWord.substring(index + referenceWord.length()).trim();
        
        return result ;
    }
    
}
