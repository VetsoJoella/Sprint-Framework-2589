
package itu.springboot.view.response ;

import java.util.HashMap;

public class ModelView{


    String url ;
    HashMap<String, Object> data ;


    public ModelView() {}

    public ModelView(String url) {
        this.url = url;
        this.data = new HashMap<>();;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public HashMap<String, Object> getData() {
        return this.data;
    }

    public void setData(HashMap<String, Object> data) {
        this.data = data;
    }

    public void add(String url,Object object){

        data.put(url,object);
    }

    public String print() {
        return "{" +
            " url='" + getUrl() + "'" +
            ", data='" + getData() + "'" +
            "}";
    }
    
}