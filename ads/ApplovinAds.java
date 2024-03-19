package com.pedometer.step.counter.app.ads;

import android.app.Activity;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxAdListener;
import com.applovin.mediation.MaxAdViewAdListener;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.ads.MaxAdView;
import com.applovin.mediation.ads.MaxInterstitialAd;
import com.applovin.mediation.nativeAds.MaxNativeAdListener;
import com.applovin.mediation.nativeAds.MaxNativeAdLoader;
import com.applovin.mediation.nativeAds.MaxNativeAdView;
import com.applovin.mediation.nativeAds.MaxNativeAdViewBinder;
import com.pedometer.step.counter.app.R;

public class ApplovinAds {
    private static final String TAG = "AdManager ApplovinAds";
    AdsUtils adsUtils = new AdsUtils();
    private InterstitialCallBack interstitialCallBack;
    private static ApplovinAds mInstance;
    private MaxInterstitialAd interstitialAd;

    public interface InterstitialCallBack {
        void callbackCall();
    }

    public void interstitialCallBack() {
        if (interstitialCallBack != null) {
            interstitialCallBack.callbackCall();
        }
    }

    public static ApplovinAds getInstance() {
        if (mInstance == null) {
            mInstance = new ApplovinAds();
        }
        return mInstance;
    }

    public void loadInterstitialAd(Activity activity) {
        Log.e(TAG, "loadInterstitialAd: ");
        if (adsUtils.isApplovinInterstitialOn(activity) && adsUtils.applovinInterstitialId(activity) != null && !adsUtils.applovinInterstitialId(activity).isEmpty()) {
            if (interstitialAd == null) {
                interstitialAd = new MaxInterstitialAd(adsUtils.applovinInterstitialId(activity), activity);
                interstitialAd.setListener(new MaxAdListener() {
                    @Override
                    public void onAdLoaded(MaxAd maxAd) {
                        Log.e(TAG, "Interstitial onAdLoaded: ");
                    }

                    @Override
                    public void onAdDisplayed(MaxAd maxAd) {

                    }

                    @Override
                    public void onAdHidden(MaxAd maxAd) {
                        Log.e(TAG, "Interstitial onAdHidden: ");
                        interstitialCallBack();
                        interstitialAd = null;
                        new Handler().postDelayed(() -> loadInterstitialAd(activity), adsUtils.interstitialInterval(activity));
                    }

                    @Override
                    public void onAdClicked(MaxAd maxAd) {

                    }

                    @Override
                    public void onAdLoadFailed(String s, MaxError maxError) {
                        Log.e(TAG, "Interstitial onAdLoadFailed: " + maxError.getMessage());
                        loadAgain(activity);
                    }

                    @Override
                    public void onAdDisplayFailed(MaxAd maxAd, MaxError maxError) {
                        Log.e(TAG, "Interstitial onAdDisplayFailed: " + maxError.getMessage());
                        loadAgain(activity);
                    }
                });
                interstitialAd.loadAd();
            }
        }
    }

    private void loadAgain(Activity activity) {
        Log.e(TAG, "loadAgain: ");
        interstitialAd = null;
        if (adsUtils.isGoogleInterstitialOn(activity)) {
            new Handler().postDelayed(() -> {
                if (MyApplication.interstitialRequestCount < adsUtils.interstitialRequestCount(activity)) {
                    GoogleAds.getInstance().loadInterstitialAd(activity);
                }
            }, adsUtils.interstitialInterval(activity));
        } else {
            new Handler().postDelayed(() -> {
                loadInterstitialAd(activity);
            }, adsUtils.interstitialInterval(activity));
        }
    }

    public void showInterstitialAd(Activity activity, InterstitialCallBack interstitialCallBack, String from) {
        this.interstitialCallBack = interstitialCallBack;
        if (interstitialAd != null && interstitialAd.isReady()) {
            interstitialAd.showAd();
        } else if (from.equals("google")) {
            interstitialCallBack();
        } else {
            if (adsUtils.isGoogleInterstitialOn(activity)) {
                GoogleAds.getInstance().showInterstitialAd(activity, this::interstitialCallBack, "applovin");
            } else {
                interstitialCallBack();
            }
        }
    }


    public void showNativeBanner(Activity activity, final FrameLayout AdContainer) {
        MaxNativeAdLoader loader = new MaxNativeAdLoader(adsUtils.applovinNativeId(activity), activity);
        loader.setNativeAdListener(new MaxNativeAdListener() {
            @Override
            public void onNativeAdLoaded(@Nullable MaxNativeAdView maxNativeAdView, MaxAd ad) {
                super.onNativeAdLoaded(maxNativeAdView, ad);
                Log.e(TAG, "onNativeBannerAdLoaded: ");
                createNativeBannerAdView(activity, loader, AdContainer, ad);
            }

            @Override
            public void onNativeAdLoadFailed(String s, MaxError maxError) {
                super.onNativeAdLoadFailed(s, maxError);
                Log.e(TAG, "onNativeBannerAdLoadFailed: " + maxError);
            }
        });
        loader.loadAd();
    }

    private void createNativeBannerAdView(Activity activity, MaxNativeAdLoader maxNativeAdLoader, FrameLayout adContainer, MaxAd ad) {
        MaxNativeAdViewBinder binder = new MaxNativeAdViewBinder.Builder(R.layout.native_banner_applovin)
                .setTitleTextViewId(R.id.ad_headline)
                .setBodyTextViewId(R.id.ad_body)
                .setIconImageViewId(R.id.ad_app_icon)
                .setOptionsContentViewGroupId(R.id.options_view)
                .setCallToActionButtonId(R.id.ad_call_to_action)
                .build();

        MaxNativeAdView adView = new MaxNativeAdView(binder, activity);
        maxNativeAdLoader.render(adView, ad);
        adContainer.removeAllViews();
        adContainer.addView(adView);
    }

    public void showNative(Activity activity, final FrameLayout AdContainer) {
        if (adsUtils.isApplovinNativeOn(activity) && adsUtils.applovinNativeId(activity) != null && !adsUtils.applovinNativeId(activity).isEmpty()) {
            final LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(activity).inflate(R.layout.native_loader, AdContainer, false);
            AdContainer.removeAllViews();
            AdContainer.addView(linearLayout);

            MaxNativeAdLoader loader = new MaxNativeAdLoader(adsUtils.applovinNativeId(activity), activity);
            loader.setNativeAdListener(new MaxNativeAdListener() {
                @Override
                public void onNativeAdLoaded(@Nullable MaxNativeAdView maxNativeAdView, MaxAd ad) {
                    super.onNativeAdLoaded(maxNativeAdView, ad);
                    Log.e(TAG, "onNativeAdLoaded: ");
                    createNativeAdView(activity, loader, AdContainer, ad);
                }

                @Override
                public void onNativeAdLoadFailed(String s, MaxError maxError) {
                    super.onNativeAdLoadFailed(s, maxError);
                    Log.e(TAG, "onNativeAdLoadFailed: " + maxError);
                }

                @Override
                public void onNativeAdClicked(MaxAd maxAd) {
                    super.onNativeAdClicked(maxAd);
                }

            });
            loader.loadAd();
        }
    }

    private void createNativeAdView(Activity activity, MaxNativeAdLoader maxNativeAdLoader, FrameLayout adContainer, MaxAd ad) {
        MaxNativeAdViewBinder binder = new MaxNativeAdViewBinder.Builder(R.layout.native_big_applovin)
                .setTitleTextViewId(R.id.ad_headline)
                .setBodyTextViewId(R.id.ad_body)
                .setAdvertiserTextViewId(R.id.secondary)
                .setIconImageViewId(R.id.ad_app_icon)
                .setMediaContentViewGroupId(R.id.ad_media)
                .setOptionsContentViewGroupId(R.id.options_view)
                .setCallToActionButtonId(R.id.ad_call_to_action)
                .build();

        MaxNativeAdView adView = new MaxNativeAdView(binder, activity);
        maxNativeAdLoader.render(adView, ad);
        adContainer.removeAllViews();
        adContainer.addView(adView);
    }

    public void showBanner(Activity activity, FrameLayout AdContainer) {
        if (adsUtils.isApplovinBannerOn(activity) && adsUtils.applovinBannerId(activity) != null && !adsUtils.applovinBannerId(activity).isEmpty()) {
            MaxAdView bannerAdView = new MaxAdView(adsUtils.applovinBannerId(activity), activity);
            bannerAdView.setListener(new MaxAdViewAdListener() {
                @Override
                public void onAdExpanded(MaxAd maxAd) {

                }

                @Override
                public void onAdCollapsed(MaxAd maxAd) {

                }

                @Override
                public void onAdLoaded(MaxAd maxAd) {
                    Log.e(TAG, "Pre Banner onAdLoaded: ");
                    int width = ViewGroup.LayoutParams.MATCH_PARENT;
                    int heightPx = activity.getResources().getDimensionPixelSize(com.intuit.sdp.R.dimen._45sdp);
                    bannerAdView.setLayoutParams(new FrameLayout.LayoutParams(width, heightPx, Gravity.BOTTOM));
                    AdContainer.removeAllViews();
                    AdContainer.setBackgroundColor(activity.getResources().getColor(R.color.native_banner_bg_color));
                    AdContainer.addView(bannerAdView);
                }

                @Override
                public void onAdDisplayed(MaxAd maxAd) {

                }

                @Override
                public void onAdHidden(MaxAd maxAd) {

                }

                @Override
                public void onAdClicked(MaxAd maxAd) {

                }

                @Override
                public void onAdLoadFailed(String s, MaxError maxError) {
                    Log.e(TAG, "Pre Banner onAdFailedToLoad: " + maxError.getMessage());

                }

                @Override
                public void onAdDisplayFailed(MaxAd maxAd, MaxError maxError) {
                    Log.e(TAG, "Pre Banner onAdDisplayFailed: " + maxError.getMessage());
                }
            });
            bannerAdView.loadAd();
        }
    }
}