package com.pedometer.step.counter.app.ads;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ProcessLifecycleOwner;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.admanager.AdManagerAdRequest;
import com.google.android.gms.ads.appopen.AppOpenAd;

import java.util.Date;

class AppOpenManager implements Application.ActivityLifecycleCallbacks, DefaultLifecycleObserver {
    AdsUtils adsUtils = new AdsUtils();
    private static final String TAG = "AppOpenAdManager";
    private AppOpenAd appOpenAd = null;
    private boolean isLoadingAd = false;
    private boolean isShowingAd = false;
    private long loadTime = 0;
    private Activity activity;

    public AppOpenManager(Activity activity, MyApplication myApplication) {
        this.activity = activity;

        myApplication.registerActivityLifecycleCallbacks(this);
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
    }

    public void loadAd() {
        Log.e(TAG, "loadAd: ");
        if (adsUtils.googleAppOpenId(activity) == null || adsUtils.googleAppOpenId(activity).isEmpty() || !adsUtils.isGoogleAppOpenOn(activity)) {
            return;
        }

        if (isLoadingAd || isAdAvailable()) {
            return;
        }

        isLoadingAd = true;
        Log.e(TAG, "isLoadingAd: " + true);
        AdManagerAdRequest request = new AdManagerAdRequest.Builder().build();
        AppOpenAd.load(activity, adsUtils.googleAppOpenId(activity), request, new AppOpenAd.AppOpenAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull AppOpenAd ad) {
                Log.e(TAG, "AppOpenAd onAdLoaded.");
                appOpenAd = ad;
                isLoadingAd = false;
                loadTime = (new Date()).getTime();
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                Log.e(TAG, "AppOpenAd onAdFailedToLoad: " + loadAdError.getMessage());
                isLoadingAd = false;
                MyApplication.appOpenRequestCount++;

                if (MyApplication.appOpenRequestCount < adsUtils.googleAppOpenRequestCount(activity)) {
                    new Handler().postDelayed(() -> loadAd(), adsUtils.googleAppOpenInterval(activity));
                }

            }
        });
    }

    private boolean wasLoadTimeLessThanNHoursAgo() {
        long dateDifference = (new Date()).getTime() - loadTime;
        long numMilliSecondsPerHour = 3600000;
        return (dateDifference < (numMilliSecondsPerHour * (long) 4));
    }

    private boolean isAdAvailable() {
        return appOpenAd != null && wasLoadTimeLessThanNHoursAgo();
    }


    private void showAdIfAvailable(@NonNull final Activity activity) {
        Log.e(TAG, "showAdIfAvailable: ");
        if (isShowingAd) {
            Log.e(TAG, "The app open ad is already showing.");
            return;
        }

        if (!isAdAvailable()) {
            Log.e(TAG, "The app open ad is not ready yet.");
            if (MyApplication.appOpenRequestCount < adsUtils.googleAppOpenRequestCount(activity)) {
                new Handler().postDelayed(this::loadAd, adsUtils.googleAppOpenInterval(activity));
            }
            return;
        }

        Log.e(TAG, "Will show ad.");

        appOpenAd.setFullScreenContentCallback(new FullScreenContentCallback() {
            @Override
            public void onAdDismissedFullScreenContent() {
                Log.e(TAG, "AppOpenAd onAdDismissedFullScreenContent.");
                appOpenAd = null;
                isShowingAd = false;
                new Handler().postDelayed(() -> loadAd(), adsUtils.googleAppOpenInterval(activity));
            }

            @Override
            public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                Log.e(TAG, "AppOpenAd onAdFailedToShowFullScreenContent: " + adError.getMessage());
                appOpenAd = null;
                isShowingAd = false;
                if (MyApplication.appOpenRequestCount < adsUtils.googleAppOpenRequestCount(activity)) {
                    new Handler().postDelayed(() -> loadAd(), adsUtils.googleAppOpenInterval(activity));
                }
            }

            @Override
            public void onAdShowedFullScreenContent() {
                Log.e(TAG, "AppOpenAd onAdShowedFullScreenContent.");
            }
        });

        isShowingAd = true;
        appOpenAd.show(activity);
    }

    @Override
    public void onStart(@NonNull LifecycleOwner owner) {
        DefaultLifecycleObserver.super.onStart(owner);
        Log.e(TAG, "onStart: ");
        if (activity == null) {
            return;
        }

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Log.e(TAG, "onStart: " + e.getMessage());
        }

        if (adsUtils.isGoogleAppOpenOn(activity)) {
            showAdIfAvailable(activity);
        }
    }

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
        Log.e(TAG, "onActivityCreated: ");
    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {
        Log.e(TAG, "onActivityResumed: ");
    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {
        Log.e(TAG, "onActivityPaused: ");
    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {
        Log.e(TAG, "onActivityStopped: ");
    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {
        Log.e(TAG, "onActivitySaveInstanceState: ");
    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {
        Log.e(TAG, "onActivityDestroyed: ");
    }
}