package com.pixectra.app.Utils;

import com.pixectra.app.Models.Images;

import java.util.HashMap;
import java.util.HashSet;

public class CartHolder {

    private static CartHolder sSoleInstance;
    private  static HashMap<String,HashSet<Images>> data;
    private CartHolder(){
        data=new HashMap<>();
    }  //private constructor.

    public static CartHolder getInstance(){
        if (sSoleInstance == null){ //if there is no instance available... create new one
            sSoleInstance = new CartHolder();
        }

        return sSoleInstance;
    }
    
    public void addImage(String key,Images image){
        if (data.containsKey(key)){
                data.get(key).add(image);
        }else{
               HashSet<Images> set=new HashSet<Images>();
                set.add(image);
                data.put(key,set);
        }

    }
    public boolean isImagePresent(String key,Images image) {
        return data.containsKey(key) && data.get(key).contains(image);
    }
    public void removeImage(String key,Images image){
        if (isImagePresent(key,image)){
            data.get(key).remove(image);
        }
    }
}