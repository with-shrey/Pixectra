package com.pixectra.app.Utils;

import android.graphics.Bitmap;

import java.util.HashMap;
import java.util.HashSet;

public class CartHolder {

    private static CartHolder sSoleInstance;
    private static HashMap<String, HashSet<Bitmap>> data;
    private CartHolder(){
        data=new HashMap<>();
    }  //private constructor.

    public static CartHolder getInstance(){
        if (sSoleInstance == null){ //if there is no instance available... create new one
            sSoleInstance = new CartHolder();
        }

        return sSoleInstance;
    }

    public int addImage(String key, Bitmap image) {
        if (data.containsKey(key)){
                data.get(key).add(image);
        }else{
            HashSet<Bitmap> set = new HashSet<Bitmap>();
                set.add(image);
                data.put(key,set);
        }
        return data.get(key).size();

    }

    public boolean isImagePresent(String key, Bitmap image) {
        return data.containsKey(key) && data.get(key).contains(image);
    }

    public int removeImage(String key, Bitmap image) {
        if (isImagePresent(key,image)){
            data.get(key).remove(image);
        }
        if (data.containsKey(key))
            return data.get(key).size();
        else
            return 0;
    }
}