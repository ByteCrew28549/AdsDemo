package com.pedometer.step.counter.app.ads;

import android.app.Activity;
import android.os.Handler;
import android.util.Log;

import com.ironsource.mediationsdk.IronSource;
import com.ironsource.mediationsdk.adunit.adapter.utility.AdInfo;
import com.ironsource.mediationsdk.logger.IronSourceError;
import com.ironsource.mediationsdk.sdk.LevelPlayInterstitialListener;

public class IronSourceAds {
    private static final String TAG = "IronSourceAds";
    AdsUtils adsUtils = new AdsUtils();
    InterstitialCallBack interstitialCallBack;

    public interface InterstitialCallBack {
        void callbackCall();
    }

    public void interstitialCallBack() {
        if (interstitialCallBack != null) {
            interstitialCallBack.callbackCall();
        }
    }

    private static IronSourceAds ironSourceAds;

    public static IronSourceAds getInstance() {
        if (ironSourceAds == null) {
            ironSourceAds = new IronSourceAds();
        }
        return ironSourceAds;
    }

    public void loadInterstitialAd(Activity activity) {
        Log.e(TAG, "loadInterstitialAd: ");
        if (adsUtils.isIronSourceInterstitialOn(activity)) {
            IronSource.init(activity, adsUtils.ironSourceAppKey(activity), IronSource.AD_UNIT.INTERSTITIAL);
            IronSource.loadInterstitial();
            IronSource.setLevelPlayInterstitialListener(new LevelPlayInterstitialListener() {
                @Override
                public void onAdReady(AdInfo adInfo) {
                    Log.e(TAG, "onAdReady: " + adInfo.getAdNetwork() + " ad Unit: " + adInfo.getAdUnit() + "ad Country: " + adInfo.getCountry());
                }

                @Override
                public void onAdLoadFailed(IronSourceError ironSourceError) {
                    Log.e(TAG, "onAdLoadFailed: " + ironSourceError.getErrorMessage());
                    loadAgain(activity);
                }

                @Override
                public void onAdOpened(AdInfo adInfo) {

                }

                @Override
                public void onAdShowSucceeded(AdInfo adInfo) {

                }

                @Override
                public void onAdShowFailed(IronSourceError ironSourceError, AdInfo adInfo) {
                    Log.e(TAG, "onAdShowFailed: " + adInfo.getAdNetwork() + " ad Unit: " + adInfo.getAdUnit() + "ad Country: " + adInfo.getCountry());
                    loadAgain(activity);
                }

                @Override
                public void onAdClicked(AdInfo adInfo) {

                }

                @Override
                public void onAdClosed(AdInfo adInfo) {
                    Log.e(TAG, "onAdClosed: " + adInfo.getAdNetwork() + " ad Unit: " + adInfo.getAdUnit() + "ad Country: " + adInfo.getCountry());
                    interstitialCallBack();
                    loadAgain(activity);
                }
            });
        }
    }

    private void loadAgain(Activity activity) {
        new Handler().postDelayed(() -> loadInterstitialAd(activity), adsUtils.interstitialInterval(activity));
    }

    public void showInterstitial(InterstitialCallBack interstitialCallBack) {
        this.interstitialCallBack = interstitialCallBack;
        if (IronSource.isInterstitialReady()) {
            IronSource.showInterstitial();
        } else {
            interstitialCallBack();
        }
    }
}