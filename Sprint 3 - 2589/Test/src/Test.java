package test ;

import annotation.AnnotationController ;
import annotation.Get ;

@AnnotationController


public class Test {
    
    @Get(url="/test")

    public String test(){

        return "Bienvenu sur test";
    }

    @Get(url="/test")

    public String another(){

        return "Bienvenu sur test dans ";
    }
}
