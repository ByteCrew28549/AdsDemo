package com.pedometer.step.counter.app.ads;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

public class AdsUtils {
    public long interstitialInterval(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("adsPref", MODE_PRIVATE);
        return prefs.getLong("interstitialInterval", 60000);
    }

    public long googleAppOpenInterval(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("adsPref", MODE_PRIVATE);
        return prefs.getLong("googleAppOpenInterval", 60000);
    }

    public long interstitialRequestCount(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("adsPref", MODE_PRIVATE);
        return prefs.getLong("interstitialRequestCount", 3);
    }

    public long googleAppOpenRequestCount(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("adsPref", MODE_PRIVATE);
        return prefs.getLong("googleAppOpenRequestCount", 3);
    }

    public long openRequestCount(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("adsPref", MODE_PRIVATE);
        return prefs.getLong("openRequestCount", 5);
    }

    public Boolean isGoogleInterstitialOn(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("adsPref", MODE_PRIVATE);
        return prefs.getBoolean("isGoogleInterstitialOn", false);
    }

    public boolean isApplovinInterstitialOn(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("adsPref", MODE_PRIVATE);
        return prefs.getBoolean("isApplovinInterstitialOn", false);
    }

    public boolean isIronSourceInterstitialOn(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("adsPref", MODE_PRIVATE);
        return prefs.getBoolean("isIronSourceInterstitialOn", false);
    }

    public boolean isMetaInterstitialOn(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("adsPref", MODE_PRIVATE);
        return prefs.getBoolean("isMetaInterstitialOn", false);
    }

    public boolean isGoogleNativeOn(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("adsPref", MODE_PRIVATE);
        return prefs.getBoolean("isGoogleNativeOn", false);
    }

    public boolean isApplovinNativeOn(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("adsPref", MODE_PRIVATE);
        return prefs.getBoolean("isApplovinNativeOn", false);
    }

    public boolean isMetaNativeOn(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("adsPref", MODE_PRIVATE);
        return prefs.getBoolean("isMetaNativeOn", false);
    }

    public boolean isIronSourceNativeOn(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("adsPref", MODE_PRIVATE);
        return prefs.getBoolean("isIronSourceNativeOn", false);
    }

    public boolean isGoogleNativeBannerOn(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("adsPref", MODE_PRIVATE);
        return prefs.getBoolean("isGoogleNativeBannerOn", false);
    }

    public boolean isApplovinNativeBannerOn(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("adsPref", MODE_PRIVATE);
        return prefs.getBoolean("isApplovinNativeBannerOn", false);
    }

    public boolean isIronSourceNativeBannerOn(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("adsPref", MODE_PRIVATE);
        return prefs.getBoolean("isIronSourceNativeBannerOn", false);
    }

    public boolean isMetaNativeBannerOn(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("adsPref", MODE_PRIVATE);
        return prefs.getBoolean("isMetaNativeBannerOn", false);
    }

    public boolean isGoogleBannerOn(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("adsPref", MODE_PRIVATE);
        return prefs.getBoolean("isGoogleBannerOn", false);
    }

    public boolean isApplovinBannerOn(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("adsPref", MODE_PRIVATE);
        return prefs.getBoolean("isApplovinBannerOn", false);
    }

    public boolean isIronSourceBannerOn(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("adsPref", MODE_PRIVATE);
        return prefs.getBoolean("isIronSourceBannerOn", false);
    }

    public boolean isMetaBannerOn(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("adsPref", MODE_PRIVATE);
        return prefs.getBoolean("isMetaBannerOn", false);
    }

    public boolean isGoogleAppOpenOn(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("adsPref", MODE_PRIVATE);
        return prefs.getBoolean("isGoogleAppOpenOn", false);
    }

    public String googleInterstitialId(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("adsPref", MODE_PRIVATE);
        return prefs.getString("googleInterstitialId", "");
    }

    public String googleBannerId(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("adsPref", MODE_PRIVATE);
        return prefs.getString("googleBannerId", "");
    }

    public String googleNativeId(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("adsPref", MODE_PRIVATE);
        return prefs.getString("googleNativeId", "");
    }

    public String googleAppOpenId(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("adsPref", MODE_PRIVATE);
        return prefs.getString("googleAppOpenId", "");
    }

    public String applovinInterstitialId(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("adsPref", MODE_PRIVATE);
        return prefs.getString("applovinInterstitialId", "");
    }

    public String applovinBannerId(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("adsPref", MODE_PRIVATE);
        return prefs.getString("applovinBannerId", "");
    }

    public String applovinNativeId(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("adsPref", MODE_PRIVATE);
        return prefs.getString("applovinNativeId", "");
    }

    public String ironSourceAppKey(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("adsPref", MODE_PRIVATE);
        return prefs.getString("ironSourceAppKey", "");
    }
    public String metaInterstitialId(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("adsPref", MODE_PRIVATE);
        return prefs.getString("metaInterstitialId", "");
    }

    public String metaBannerId(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("adsPref", MODE_PRIVATE);
        return prefs.getString("metaBannerId", "");
    }

    public String metaNativeId(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("adsPref", MODE_PRIVATE);
        return prefs.getString("metaNativeId", "");
    }
}