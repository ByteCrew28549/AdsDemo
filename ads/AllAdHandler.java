package com.pedometer.step.counter.app.ads;

import android.app.Activity;
import android.widget.FrameLayout;

public class AllAdHandler {
    AdsUtils adsUtils = new AdsUtils();

    public interface OnAdClosedListener {
        void onAdClosed();
    }

    public void preLoadAds(Activity activity) {
        if (adsUtils.isGoogleInterstitialOn(activity) && adsUtils.isApplovinInterstitialOn(activity)) {
            GoogleAds.getInstance().loadInterstitialAd(activity);
        } else if (adsUtils.isGoogleInterstitialOn(activity)) {
            GoogleAds.getInstance().loadInterstitialAd(activity);
        } else if (adsUtils.isApplovinInterstitialOn(activity)) {
            ApplovinAds.getInstance().loadInterstitialAd(activity);
        } else if (adsUtils.isIronSourceInterstitialOn(activity)) {
            IronSourceAds.getInstance().loadInterstitialAd(activity);
        } else if (adsUtils.isMetaInterstitialOn(activity)) {
            MetaAds.getInstance().loadInterstitialAd(activity);
        }

        MyApplication.getInstance().loadAppOpenAd(activity);

        /*if (adsUtils.isGoogleNativeBannerOn(activity)) {

        } else if (adsUtils.isApplovinNativeBannerOn(activity)) {

        } else if (adsUtils.isIronSourceNativeBannerOn(activity)) {

        } else if (adsUtils.isMetaNativeBannerOn(activity)) {

        } else if (adsUtils.isGoogleBannerOn(activity)) {

        } else if (adsUtils.isApplovinBannerOn(activity)) {

        } else if (adsUtils.isIronSourceBannerOn(activity)) {

        } else if (adsUtils.isMetaBannerOn(activity)) {

        }

        if (adsUtils.isGoogleNativeOn(activity)) {

        } else if (adsUtils.isApplovinNativeOn(activity)) {

        } else if (adsUtils.isIronSourceNativeOn(activity)) {

        } else if (adsUtils.isMetaNativeOn(activity)) {

        }*/
    }

    public void showInterAds(Activity activity, OnAdClosedListener onAdClosedListener) {
        if (adsUtils.isGoogleInterstitialOn(activity)) {
            GoogleAds.getInstance().showInterstitialAd(activity, onAdClosedListener::onAdClosed, "main");
        } else if (adsUtils.isApplovinInterstitialOn(activity)) {
            ApplovinAds.getInstance().showInterstitialAd(activity, onAdClosedListener::onAdClosed, "main");
        } else if (adsUtils.isIronSourceInterstitialOn(activity)) {
            IronSourceAds.getInstance().showInterstitial(onAdClosedListener::onAdClosed);
        }else if (adsUtils.isMetaInterstitialOn(activity)) {
            MetaAds.getInstance().showInterstitial(onAdClosedListener::onAdClosed);
        } else {
            onAdClosedListener.onAdClosed();
        }
    }

    public void showBanner(Activity activity, FrameLayout nativeAdContainer) {
        if (adsUtils.isGoogleNativeBannerOn(activity)) {
            GoogleAds.getInstance().showNativeBanner(activity, nativeAdContainer);
        } else if (adsUtils.isApplovinNativeBannerOn(activity)) {
            ApplovinAds.getInstance().showNativeBanner(activity, nativeAdContainer);
        } else if (adsUtils.isIronSourceNativeBannerOn(activity)) {
        }else if (adsUtils.isMetaNativeBannerOn(activity)) {
        } else if (adsUtils.isGoogleBannerOn(activity)) {
            GoogleAds.getInstance().showBanner(activity, nativeAdContainer);
        } else if (adsUtils.isApplovinBannerOn(activity)) {
            ApplovinAds.getInstance().showBanner(activity, nativeAdContainer);
        } else if (adsUtils.isIronSourceBannerOn(activity)) {
        }else if (adsUtils.isMetaBannerOn(activity)) {
            MetaAds.getInstance().LoadBannerAd(activity,nativeAdContainer);
        }
    }

    public void showNative(Activity activity, FrameLayout nativeAdContainer) {
        if (adsUtils.isGoogleNativeOn(activity)) {
            GoogleAds.getInstance().showNative(activity, nativeAdContainer);
        } else if (adsUtils.isApplovinNativeOn(activity)) {
            ApplovinAds.getInstance().showNative(activity, nativeAdContainer);
        } else if (adsUtils.isIronSourceNativeOn(activity)) {
        }else if (adsUtils.isMetaNativeOn(activity)) {
            MetaAds.getInstance().showNative(activity, nativeAdContainer);
        }
    }
}