package com.pixectra.app.Utils;

import android.media.Image;

import java.util.HashMap;
import java.util.HashSet;

public class CartHolder {

    private static CartHolder sSoleInstance;
    private  static HashMap<String,HashSet<Image>> data;
    private CartHolder(){
        data=new HashMap<>();
    }  //private constructor.

    public static CartHolder getInstance(){
        if (sSoleInstance == null){ //if there is no instance available... create new one
            sSoleInstance = new CartHolder();
        }

        return sSoleInstance;
    }
    
    public void addImage(String key,Image image){
        if (data.containsKey(key)){
                data.get(key).add(image);
        }else{
               HashSet<Image> set=new HashSet<Image>();
                set.add(image);
                data.put(key,set);
        }

    }
    public boolean isImagePresent(String key,Image image) {
        return data.containsKey(key) && data.get(key).contains(image);
    }
    public void removeImage(String key,Image image){
        if (isImagePresent(key,image)){
            data.get(key).remove(image);
        }
    }
}