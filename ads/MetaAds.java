package com.pedometer.step.counter.app.ads;

import android.app.Activity;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdOptionsView;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;
import com.facebook.ads.MediaView;
import com.facebook.ads.NativeAd;
import com.facebook.ads.NativeAdLayout;
import com.facebook.ads.NativeAdListener;
import com.pedometer.step.counter.app.R;

import java.util.ArrayList;
import java.util.List;

public class MetaAds {
    public InterstitialAd interstitial;
    private static MetaAds mInstance;
    private static final String TAG = "Meta_Ads";
    private InterstitialCallBack interstitialCallBack;

    private boolean isNativeBannerAdLoaded = false;
    private boolean isNativeBannerAdLoading = false;

    private boolean isNativeAdLoaded = false;
    private boolean isNativeAdLoading = false;

    private boolean isBannerAdLoaded = false;
    private boolean isBannerAdLoading = false;
    AdsUtils adsUtils = new AdsUtils();

    public interface InterstitialCallBack {
        void callbackCall();
    }

    public void interstitialCallBack() {
        if (interstitialCallBack != null) {
            interstitialCallBack.callbackCall();
        }
    }

    public static MetaAds getInstance() {
        if (mInstance == null) {
            mInstance = new MetaAds();
        }
        return mInstance;
    }

    public void loadInterstitialAd(Activity activity) {
        if (adsUtils.isMetaInterstitialOn(activity) && adsUtils.metaInterstitialId(activity) != null && !adsUtils.metaInterstitialId(activity).isEmpty()) {
            interstitial = new InterstitialAd(activity, adsUtils.metaInterstitialId(activity));
            InterstitialAdListener interstitialAdListener = new InterstitialAdListener() {
                @Override
                public void onInterstitialDisplayed(Ad ad) {
                }

                @Override
                public void onInterstitialDismissed(Ad ad) {
                    Log.e(TAG, "onInterstitialDismissed: ");
                    interstitial = null;
                    interstitialCallBack();
                    loadAgain(activity);
                }

                @Override
                public void onError(Ad ad, AdError adError) {
                    Log.e(TAG, "interstitial onError: " + adError.getErrorMessage());
                    interstitial = null;
                    loadAgain(activity);
                }

                @Override
                public void onAdLoaded(Ad ad) {
                    Log.e(TAG, "onAdLoaded: " + ad.getPlacementId());
                }

                @Override
                public void onAdClicked(Ad ad) {
                }

                @Override
                public void onLoggingImpression(Ad ad) {
                }
            };
            interstitial.loadAd(interstitial.buildLoadAdConfig().withAdListener(interstitialAdListener).build());
        }
    }

    private void loadAgain(Activity activity) {
        interstitial = null;
        new Handler().postDelayed(() -> loadInterstitialAd(activity), adsUtils.interstitialInterval(activity));
    }

    public void showInterstitial(InterstitialCallBack interstitialCallBack) {
        this.interstitialCallBack = interstitialCallBack;
        if (interstitial != null && interstitial.isAdLoaded()) {
            interstitial.show();
        } else {
            interstitialCallBack();
        }
    }

    public void showNative(Activity activity, FrameLayout AdContainer) {
        if (adsUtils.isMetaNativeOn(activity) && adsUtils.metaNativeId(activity) != null && !adsUtils.metaNativeId(activity).isEmpty()) {
            final LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(activity).inflate(R.layout.native_loader, AdContainer, false);
            AdContainer.removeAllViews();
            AdContainer.addView(linearLayout);

            NativeAd nativeAdDDD = new NativeAd(activity, adsUtils.metaNativeId(activity));
            NativeAdListener nativeAdListener = new NativeAdListener() {
                @Override
                public void onMediaDownloaded(Ad ad) {
                }

                @Override
                public void onError(Ad ad, AdError adError) {
                    Log.e(TAG, "onError: " + adError.getErrorMessage() + ad.getPlacementId());
                }

                @Override
                public void onAdLoaded(Ad ad) {
                    if (nativeAdDDD != ad) {
                        return;
                    }

                    AdContainer.removeAllViews();
                    NativeAdLayout adView = new NativeAdLayout(activity);
                    FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                    );
                    AdContainer.addView(adView, layoutParams);
                    inflateAd(nativeAdDDD, activity, adView);
                }

                @Override
                public void onAdClicked(Ad ad) {

                }

                @Override
                public void onLoggingImpression(Ad ad) {

                }
            };
            nativeAdDDD.loadAd(nativeAdDDD.buildLoadAdConfig().withAdListener(nativeAdListener).build());
        }
    }

    private void inflateAd(NativeAd nativeAd, Activity activity, NativeAdLayout frameLayout) {
        nativeAd.unregisterView();

        LayoutInflater inflater = LayoutInflater.from(activity);
        LinearLayout adView = (LinearLayout) inflater.inflate(R.layout.meta_native_ad, frameLayout, false);

        frameLayout.removeAllViews();
        frameLayout.addView(adView);

        FrameLayout adChoicesContainer = adView.findViewById(R.id.ad_choices_container);
        AdOptionsView adOptionsView = new AdOptionsView(activity, nativeAd, frameLayout);
        adChoicesContainer.removeAllViews();
        adChoicesContainer.addView(adOptionsView, 0);

        MediaView nativeAdIcon = adView.findViewById(R.id.native_ad_icon);
        TextView nativeAdTitle = adView.findViewById(R.id.native_ad_title);
        MediaView nativeAdMedia = adView.findViewById(R.id.native_ad_media);
        TextView nativeAdSocialContext = adView.findViewById(R.id.native_ad_social_context);
        TextView nativeAdBody = adView.findViewById(R.id.native_ad_body);
        //  TextView sponsoredLabel = adView.findViewById(R.id.native_ad_sponsored_label);
        Button nativeAdCallToAction = adView.findViewById(R.id.native_ad_call_to_action);

        nativeAdTitle.setText(nativeAd.getAdvertiserName());
        nativeAdBody.setText(nativeAd.getAdBodyText());
        nativeAdSocialContext.setText(nativeAd.getAdSocialContext());
        nativeAdCallToAction.setVisibility(nativeAd.hasCallToAction() ? View.VISIBLE : View.INVISIBLE);
        nativeAdCallToAction.setText(nativeAd.getAdCallToAction());
        //   sponsoredLabel.setText(nativeAd.getSponsoredTranslation());

        List<View> clickableViews = new ArrayList<>();
        clickableViews.add(nativeAdTitle);
        clickableViews.add(nativeAdCallToAction);

        nativeAd.registerViewForInteraction(adView, nativeAdMedia, nativeAdIcon, clickableViews);
    }

    public AdView LoadBannerAd(Activity activity, FrameLayout adContainer) {
        if (adsUtils.isMetaBannerOn(activity) && adsUtils.metaBannerId(activity) != null && !adsUtils.metaBannerId(activity).isEmpty()) {
            adContainer.removeAllViews();
            AdView adView = new AdView(activity, adsUtils.metaBannerId(activity), AdSize.BANNER_HEIGHT_50);
            adContainer.addView(adView);
            adView.loadAd();
            return adView;
        }
        return null;
    }
}