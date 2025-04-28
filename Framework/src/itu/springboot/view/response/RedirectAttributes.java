package itu.springboot.view.response;

import java.util.HashMap;

public class RedirectAttributes {
    
    HashMap<String, Object> flashData ;

    public RedirectAttributes(){
        flashData = new HashMap<>();
    }

    public HashMap<String, Object> getFlashData() {
        return this.flashData;
    }

    public void setFlashData(HashMap<String, Object> data) {
        this.flashData = data;
    }

    public void addFlashAttribute(String url, Object object) {
        flashData.put(url, object);
        System.out.println("Valeur ajouté dans redirectAttributes "+ object);
    }


    // @Override
    // public String toString() {
    //     return "{" +
    //         " flashData='" + getFlashData() + "'" +
    //         "}";
    // }

}
