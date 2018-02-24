package com.pixectra.app.Utils;

import android.graphics.Bitmap;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Vector;

public class CartHolder {

    private static CartHolder sSoleInstance;
    private static HashMap<String, HashSet<Bitmap>> data;
    private static HashMap<String, HashSet<Bitmap>> cart;
    private static ImageChangedListner listner;
    private CartHolder(){
        data=new HashMap<>();
    }  //private constructor.

    public static CartHolder getInstance(){
        if (sSoleInstance == null){ //if there is no instance available... create new one
            sSoleInstance = new CartHolder();
            listner = null;
        }

        return sSoleInstance;
    }

    public void setOnImageChangedListner(ImageChangedListner mlistner) {
        listner = mlistner;
    }

    public void addImage(String key, Bitmap image) {
        if (data.containsKey(key)){
            boolean added = data.get(key).add(image);
            if (added) {
                int size = data.get(key).size();
                listner.onImageAdded(image, size);

            }
        }else{
            HashSet<Bitmap> set = new HashSet<Bitmap>();
                set.add(image);
                data.put(key,set);
            int size = data.get(key).size();
            listner.onImageAdded(image, size);
        }
    }

    public boolean isImagePresent(String key, Bitmap image) {
        return data.containsKey(key) && data.get(key).contains(image);
    }

    public void removeImage(String key, Bitmap image) {
        int size = 0;
        if (isImagePresent(key,image)){
            data.get(key).remove(image);
            if (data.containsKey(key))
                size = data.get(key).size();
            listner.onImageDeleted(image, size);
        }

    }

    public int getSize(String key) {
        if (data.containsKey(key))
            return data.get(key).size();
        else
            return 0;
    }

    public Vector<Bitmap> getAllImages(String key) {
        if (data.containsKey(key)) {
            return new Vector<>(data.get(key));
        } else {
            return new Vector<>();
        }
    }

    public interface ImageChangedListner {
        void onImageAdded(Bitmap img, int size);

        void onImageDeleted(Bitmap img, int size);

        void alreadyPresent(Bitmap img);
    }
}