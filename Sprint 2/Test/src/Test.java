package test ;

import annotation.AnnotationController ;
import annotation.Get ;

@AnnotationController


public class Test {
    
    @Get(url="/test")

    public void test(){

    }
}
