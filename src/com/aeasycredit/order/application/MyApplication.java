
package com.aeasycredit.order.application;

import com.aeasycredit.order.volley.MultiPartHttpStack;
import com.aeasycredit.order.volley.RequestQueue;
import com.aeasycredit.order.volley.toolbox.Volley;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

public class MyApplication extends Application {

    public static RequestQueue mRequestQueue;
    public static String appVersion;

    @Override
    public void onCreate() {
        super.onCreate();
        mRequestQueue = Volley.newRequestQueue(getApplicationContext(), new MultiPartHttpStack());
        PackageManager manager = getApplicationContext().getPackageManager();
        ImageLoaderConfiguration config = ImageLoaderConfiguration.createDefault(getApplicationContext());
        ImageLoader.getInstance().init(config);
        try {
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            appVersion = info.versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
    }
}
