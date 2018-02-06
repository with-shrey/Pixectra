package com.pixectra.app;



import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by ShreyGupta on 8/2/17.
 * This Class Manages Singlton Version Of Volley Request Queue
 */

public class MySingleton {
    private static MySingleton mInstance;
    private static Context mContext;
    private RequestQueue mRequestQueue;

    private MySingleton(Context context){
        // Specify the application context
        mContext = context;
        // Get the request queue
        mRequestQueue = getRequestQueue();
    }

    /**
     * Get Instance Of Eqisting Object If Exist Or Create New One
     * @param context
     * @return
     */
    public static synchronized MySingleton getInstance(Context context){
        // If Instance is null then initialize new Instance
        if(mInstance == null){
            mInstance = new MySingleton(context);
        }
        // Return MySingleton new Instance
        return mInstance;
    }

    /**
     * Get Request Queue Use getInstance().getRequestQueue
     * @return
     */
    public RequestQueue getRequestQueue(){
        // If RequestQueue is null the initialize new RequestQueue
        if(mRequestQueue == null){
            mRequestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
        }

        // Return RequestQueue
        return mRequestQueue;
    }

    /**
     * Add Directly To request Queue
     * @param request
     * @param <T>
     */
    public<T> void addToRequestQueue(Request<T> request){
        // Add the specified request to the request queue
        getRequestQueue().add(request);
    }
}

