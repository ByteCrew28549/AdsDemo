package com.pedometer.step.counter.app.ads;

import android.app.Activity;
import android.app.Application;
import android.util.Log;

import com.applovin.sdk.AppLovinSdk;
import com.facebook.ads.AdSettings;
import com.facebook.ads.AudienceNetworkAds;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.AdapterStatus;

import java.util.Map;

public class MyApplication extends Application {
    private static final String TAG = "AdManager MyApplication";
    public static int interstitialRequestCount = 0;
    public static int appOpenRequestCount = 0;
    public static MyApplication instance;

    public static MyApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        AppLovinSdk.getInstance(this).setMediationProvider("max");
        AppLovinSdk.initializeSdk(this, configuration -> {
            Log.d(TAG, "AppLovin SDK is initialized :");
        });

        MobileAds.initialize(this, initializationStatus -> {
            Map<String, AdapterStatus> statusMap = initializationStatus.getAdapterStatusMap();
            for (String adapterClass : statusMap.keySet()) {
                AdapterStatus status = statusMap.get(adapterClass);
                if (status != null) {
                    Log.d("MyApp", String.format("Adapter name: %s, Description: %s, Latency: %d", adapterClass, status.getDescription(), status.getLatency()));
                }
            }
        });
        AdSettings.setDataProcessingOptions(new String[]{});
        AudienceNetworkAds.initialize(this);
    }

    //TODO: init open ad
    public void loadAppOpenAd(Activity activity) {
        new AppOpenManager(activity, this);
    }
}