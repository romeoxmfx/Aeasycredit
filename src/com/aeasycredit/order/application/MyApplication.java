package com.aeasycredit.order.application;

import com.aeasycredit.order.volley.MultiPartHttpStack;
import com.aeasycredit.order.volley.RequestQueue;
import com.aeasycredit.order.volley.toolbox.Volley;

import android.app.Application;

public class MyApplication extends Application {

    public static RequestQueue mRequestQueue;
    
    @Override
    public void onCreate() {
        super.onCreate();
        mRequestQueue = Volley.newRequestQueue(getApplicationContext(),new MultiPartHttpStack());
    }
}
