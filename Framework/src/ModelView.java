
package response ;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ModelView{


    String url ;
    HashMap data ;


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

    public HashMap getData() {
        return this.data;
    }

    public void setData(HashMap data) {
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