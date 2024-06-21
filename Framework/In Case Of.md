   for (int i = 0 ; i<parameters.length ; i++) {
                    
                    String[] value = parameters[i].getName() ;
                    String name = null ;
                    if(parameters[i].isAnnotationPresent(Param.class)){

                        name = ((Param)parameters[i].getAnnotation(Param.class)).name();
                        value = data.get(name);

                    }
                    value = data.get(name);
                    if(parameters[i].getType().isArray() || parameters[i].getClass().isArray()){
                        formValues[i] = arrayCast(value,parameters[i].getType()) ; 
                    }
                    else{
                        formValues[i] = convert(value[0],parameters[i].getType()) ; 
                    }
                }