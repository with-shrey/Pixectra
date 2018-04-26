package com.pixectra.app.Utils;

import android.net.Uri;
import android.util.Log;
import android.util.Pair;

import com.pixectra.app.Models.CheckoutData;
import com.pixectra.app.Models.Coupon;
import com.pixectra.app.Models.Product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

public class CartHolder {

    private static CartHolder sSoleInstance;
    private static HashMap<String, Vector<Object>> data;
    private static HashMap<String, Product> details;
    private static HashMap<String, Boolean> selected;
    private static ArrayList<Pair<Product, Vector<Object>>> cart;
    private static ArrayList<Pair<Product, Uri>> video;
    private static ImageChangedListner listner;
    private static Pair<Integer, Pair<Integer, Double>> discount;
    private static Coupon sCoupon;
    private static int creditsUsed;
    private static CheckoutData sCheckoutData;


    private CartHolder(){
        data=new HashMap<>();
        cart = new ArrayList<>();
        details = new HashMap<>();
        discount = null;
        creditsUsed = 0;
    }  //private constructor.

    public static CartHolder getInstance(){
        if (sSoleInstance == null){ //if there is no instance available... create new one
            sSoleInstance = new CartHolder();
            listner = null;
        }

        return sSoleInstance;
    }

    public ArrayList<Pair<Product, Uri>> getVideo() {
        if (video == null)
            video = new ArrayList<>();
        return video;
    }

    public HashMap<String, Boolean> getSelected() {
        if (selected == null)
            selected = new HashMap<>();
        return selected;
    }

    public void clearSelected() {
        selected.clear();
    }

    public CheckoutData getCheckout() {
        return sCheckoutData;
    }

    public void setCheckout(CheckoutData checkoutData) {
        sCheckoutData = checkoutData;
    }

    public Coupon getCoupon() {
        return sCoupon;
    }

    public void setCoupon(Coupon coupon) {
        sCoupon = coupon;
    }

    public void setOnImageChangedListner(ImageChangedListner mlistner) {
        listner = mlistner;
    }

    public ArrayList<Pair<Product, Vector<Object>>> getCart() {
        if (cart == null) {
            cart = new ArrayList<>();
        }
        return cart;
    }

    public Pair<Integer, Pair<Integer, Double>> getDiscount() {
        return discount;
    }

    public void setDiscount(Pair<Integer, Pair<Integer, Double>> discount) {
        CartHolder.discount = discount;
    }

    public void addToCart(String key) {
        Pair<Product, Vector<Object>> temp = new Pair<>(details.get(key), new Vector<>(data.get(key)));
        cart.add(temp);
        data.remove(key);
        clearSelected();
    }

    public Product getDetails(String key) {
        return details.get(key);
    }

    public Product addDetails(String key, Product product) {
        return details.put(key, product);
    }

    public void addImage(String key, Object image) {
        Log.v("image", key);
        if (data.containsKey(key)){
            boolean added = !data.get(key).contains(image);
            if (added) {
                data.get(key).add(image);
                int size = data.get(key).size();
                listner.onImageAdded(image, size);
            } else {
                listner.alreadyPresent(image);
            }
        } else {
            Vector<Object> set = new Vector<>();
                set.add(image);
                data.put(key,set);
            int size = data.get(key).size();
            listner.onImageAdded(image, size);
        }
    }

    public boolean isImagePresent(String key, Object image) {
        return data.containsKey(key) && data.get(key).contains(image);
    }

    public void removeImage(String key, Object image) {
        int size = 0;
        if (isImagePresent(key,image)){
            data.get(key).remove(image);
            if (data.containsKey(key))
                size = data.get(key).size();
            listner.onImageDeleted(image, size);
        }

    }

    public Object getImage(String key, int index) {
        return data.get(key).get(index);
    }

    public void setImage(String key, int index, Object img) {
        data.get(key).set(index, img);
        listner.alreadyPresent(img);
    }

    public int getSize(String key) {
        if (data.containsKey(key))
            return data.get(key).size();
        else
            return 0;
    }

    public Vector<Object> getAllImages(String key) {
        if (data.containsKey(key)) {
            return new Vector<>(data.get(key));
        } else {
            return new Vector<>();
        }
    }

    public int getCreditsUsed() {
        return creditsUsed;
    }

    public void setCreditsUsed(int mcreditsUsed) {
        creditsUsed = mcreditsUsed;
    }

    public interface ImageChangedListner {
        void onImageAdded(Object img, int size);

        void onImageDeleted(Object img, int size);

        void alreadyPresent(Object img);
    }
}