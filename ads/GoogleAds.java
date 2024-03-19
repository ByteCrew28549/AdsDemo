package com.pedometer.step.counter.app.ads;

import android.app.Activity;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.VideoOptions;
import com.google.android.gms.ads.admanager.AdManagerAdRequest;
import com.google.android.gms.ads.admanager.AdManagerAdView;
import com.google.android.gms.ads.admanager.AdManagerInterstitialAd;
import com.google.android.gms.ads.admanager.AdManagerInterstitialAdLoadCallback;
import com.google.android.gms.ads.nativead.MediaView;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.pedometer.step.counter.app.R;

public class GoogleAds {
    private static final String TAG = "AdManager GoogleAds";
    AdsUtils adsUtils = new AdsUtils();
    private AdManagerInterstitialAd mInterstitialAd;
    private static GoogleAds mInstance;
    private InterstitialCallBack interstitialCallBack;

    public interface InterstitialCallBack {
        void callbackCall();
    }

    public void interstitialCallBack() {
        if (interstitialCallBack != null) {
            interstitialCallBack.callbackCall();
        }
    }

    public static GoogleAds getInstance() {
        if (mInstance == null) {
            mInstance = new GoogleAds();
        }
        return mInstance;
    }

    public void loadInterstitialAd(Activity activity) {
        Log.e(TAG, "loadInterstitialAd: ");
        if (adsUtils.isGoogleInterstitialOn(activity) && adsUtils.googleInterstitialId(activity) != null && !adsUtils.googleInterstitialId(activity).isEmpty()) {
            if (mInterstitialAd == null) {
                AdManagerAdRequest adManagerAdRequest = new AdManagerAdRequest.Builder().build();
                AdManagerInterstitialAd.load(activity, adsUtils.googleInterstitialId(activity), adManagerAdRequest, new AdManagerInterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull AdManagerInterstitialAd interstitialAd) {
                        mInterstitialAd = interstitialAd;
                        Log.e(TAG, "loadInterstitialAd : onAdLoaded : " + interstitialAd);

                        mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                            @Override
                            public void onAdDismissedFullScreenContent() {
                                Log.e(TAG, "loadInterstitialAd : onAdDismissedFullScreenContent ");
                                interstitialCallBack();
                                mInterstitialAd = null;
                                new Handler().postDelayed(() -> loadInterstitialAd(activity), adsUtils.interstitialInterval(activity));
                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                                Log.e(TAG, "loadInterstitialAd : onAdFailedToShowFullScreenContent : " + adError.getMessage());
                                loadAgain(activity);
                            }

                            @Override
                            public void onAdShowedFullScreenContent() {
                                Log.e(TAG, "loadInterstitialAd :onAdShowedFullScreenContent");
                            }
                        });
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        Log.e(TAG, "loadInterstitialAd : onAdFailedToLoad : " + loadAdError.getMessage());
                        MyApplication.interstitialRequestCount++;
                        Log.e(TAG, "onAdFailedToLoad: " + MyApplication.interstitialRequestCount);
                        loadAgain(activity);
                    }
                });
            }
        }
    }

    private void loadAgain(Activity activity) {
        Log.e(TAG, "loadAgain: ");
        mInterstitialAd = null;
        if (adsUtils.isApplovinInterstitialOn(activity)) {
            new Handler().postDelayed(() -> {
                ApplovinAds.getInstance().loadInterstitialAd(activity);
            }, adsUtils.interstitialInterval(activity));
        } else {
            new Handler().postDelayed(() -> {
                if (MyApplication.interstitialRequestCount < adsUtils.interstitialRequestCount(activity)) {
                    loadInterstitialAd(activity);
                }
            }, adsUtils.interstitialInterval(activity));
        }
    }

    public void showInterstitialAd(Activity activity, InterstitialCallBack interstitialCallBack, String from) {
        this.interstitialCallBack = interstitialCallBack;
        if (mInterstitialAd != null) {
            mInterstitialAd.show(activity);
        } else if (from.equals("applovin")) {
            interstitialCallBack();
        } else {
            if (adsUtils.isApplovinInterstitialOn(activity)) {
                ApplovinAds.getInstance().showInterstitialAd(activity, this::interstitialCallBack, "google");
            } else {
                interstitialCallBack();
            }
        }
    }

    public void showNativeBanner(Activity activity, final FrameLayout AdContainer) {
        if (adsUtils.isGoogleNativeBannerOn(activity) && adsUtils.googleNativeId(activity) != null && !adsUtils.googleNativeId(activity).isEmpty()) {
            AdLoader.Builder builder = new AdLoader.Builder(activity, adsUtils.googleNativeId(activity));
            VideoOptions videoOptions = new VideoOptions.Builder().setStartMuted(true).build();
            AdLoader adLoader = builder.forNativeAd(nativeBannerAd -> {
                Log.e(TAG, "Native Banner onAdLoaded: ");
                inflateNativeBannerAd(activity, nativeBannerAd, AdContainer);
            }).withAdListener(new AdListener() {
                @Override
                public void onAdFailedToLoad(@NonNull LoadAdError adError) {
                    Log.e(TAG, "Native Banner onAdFailedToLoad: " + adError.getMessage());
                }
            }).withNativeAdOptions(new com.google.android.gms.ads.nativead.NativeAdOptions.Builder().setVideoOptions(videoOptions).build()).build();
            adLoader.loadAd(new AdRequest.Builder().build());
        }
    }

    public void inflateNativeBannerAd(Activity activity, NativeAd nativeAd, FrameLayout AdContainer) {
        LayoutInflater inflater = LayoutInflater.from(activity);
        View view = inflater.inflate(R.layout.native_banner_google, null);
        NativeAdView adView = view.findViewById(R.id.uadview);

        if (adView.findViewById(R.id.ad_headline) != null) {
            adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        }

        if (adView.findViewById(R.id.ad_body) != null) {
            adView.setBodyView(adView.findViewById(R.id.ad_body));
        }

        if (adView.findViewById(R.id.ad_call_to_action) != null) {
            adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
        }

        if (adView.findViewById(R.id.ad_app_icon) != null) {
            adView.setIconView(adView.findViewById(R.id.ad_app_icon));
        }

        if (adView.getIconView() != null) {
            if (nativeAd.getIcon() == null) {
                adView.getIconView().setVisibility(View.GONE);
            } else {
                ((ImageView) adView.getIconView()).setImageDrawable(nativeAd.getIcon().getDrawable());
                adView.getIconView().setVisibility(View.VISIBLE);
            }
        }

        if (adView.getHeadlineView() != null) {
            if (nativeAd.getHeadline() == null) {
                adView.getHeadlineView().setVisibility(View.GONE);
            } else {
                adView.getHeadlineView().setVisibility(View.VISIBLE);
                ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());
            }
        }

        if (adView.getBodyView() != null) {
            if (nativeAd.getBody() == null) {
                adView.getBodyView().setVisibility(View.GONE);
            } else {
                adView.getBodyView().setVisibility(View.VISIBLE);
                ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
            }
        }

        if (adView.getCallToActionView() != null) {
            if (nativeAd.getCallToAction() == null) {
                adView.getCallToActionView().setVisibility(View.GONE);
            } else {
                adView.getCallToActionView().setVisibility(View.VISIBLE);
                ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
            }
        }

        adView.setNativeAd(nativeAd);
        AdContainer.removeAllViews();
        AdContainer.addView(view);
    }

    public void showNative(Activity activity, final FrameLayout AdContainer) {
        if (adsUtils.isGoogleNativeOn(activity) && adsUtils.googleNativeId(activity) != null && !adsUtils.googleNativeId(activity).isEmpty()) {
            final LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(activity).inflate(R.layout.native_loader, AdContainer, false);
            AdContainer.removeAllViews();
            AdContainer.addView(linearLayout);

            AdLoader.Builder builder = new AdLoader.Builder(activity, adsUtils.googleNativeId(activity));
            VideoOptions videoOptions = new VideoOptions.Builder().setStartMuted(true).build();
            AdLoader adLoader = builder.forNativeAd(nativeAd -> {
                Log.e(TAG, "Native onAdLoaded: ");
                inflateNativeAd(activity, nativeAd, AdContainer);
            }).withAdListener(new AdListener() {
                @Override
                public void onAdFailedToLoad(@NonNull LoadAdError adError) {
                    Log.e(TAG, "Native onAdFailedToLoad: " + adError.getMessage());
                    AdContainer.removeAllViews();
                    ApplovinAds.getInstance().showNative(activity, AdContainer);

                }
            }).withNativeAdOptions(new com.google.android.gms.ads.nativead.NativeAdOptions.Builder().setVideoOptions(videoOptions).build()).build();
            adLoader.loadAd(new AdRequest.Builder().build());
        }
    }

    public void inflateNativeAd(Activity activity, NativeAd nativeAd, ViewGroup AdContainer) {
        final LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(activity).inflate(R.layout.native_loader, AdContainer, false);
        AdContainer.removeAllViews();
        AdContainer.addView(linearLayout);

        LayoutInflater inflater = LayoutInflater.from(activity);
        View view = inflater.inflate(R.layout.native_big_google, null);
        NativeAdView adView = view.findViewById(R.id.uadview);

        if (adView.findViewById(R.id.ad_media) != null) {
            MediaView mediaView = adView.findViewById(R.id.ad_media);
            mediaView.setImageScaleType(ImageView.ScaleType.CENTER_CROP);
            adView.setMediaView(mediaView);
        }

        if (adView.findViewById(R.id.ad_headline) != null) {
            adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        }

        if (adView.findViewById(R.id.ad_body) != null) {
            adView.setBodyView(adView.findViewById(R.id.ad_body));
        }

        if (adView.findViewById(R.id.ad_call_to_action) != null) {
            adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
        }

        if (adView.findViewById(R.id.ad_app_icon) != null) {
            adView.setIconView(adView.findViewById(R.id.ad_app_icon));
        }

        if (adView.findViewById(R.id.ad_stars) != null) {
            adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
        }

        if (adView.findViewById(R.id.ad_advertiser) != null) {
            adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));
        }

        if (adView.findViewById(R.id.ad_price) != null) {
            adView.setPriceView(adView.findViewById(R.id.ad_price));
        }

        if (adView.findViewById(R.id.ad_store) != null) {
            adView.setStoreView(adView.findViewById(R.id.ad_store));
        }

        if (adView.getStarRatingView() != null) {
            if (nativeAd.getStarRating() == null) {
                adView.getStarRatingView().setVisibility(View.GONE);
            } else {
                adView.getStarRatingView().setVisibility(View.VISIBLE);
                ((RatingBar) adView.getStarRatingView()).setRating(Float.parseFloat(String.valueOf(nativeAd.getStarRating())));
            }
        }

        if (adView.getIconView() != null) {
            if (nativeAd.getIcon() == null) {
                adView.getIconView().setVisibility(View.GONE);
            } else {
                ((ImageView) adView.getIconView()).setImageDrawable(nativeAd.getIcon().getDrawable());
                adView.getIconView().setVisibility(View.VISIBLE);
            }
        }

        if (adView.getHeadlineView() != null) {
            if (nativeAd.getHeadline() == null) {
                adView.getHeadlineView().setVisibility(View.GONE);
            } else {
                adView.getHeadlineView().setVisibility(View.VISIBLE);
                ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());
            }
        }

        if (adView.getBodyView() != null) {
            if (nativeAd.getBody() == null) {
                adView.getBodyView().setVisibility(View.GONE);
            } else {
                adView.getBodyView().setVisibility(View.VISIBLE);
                ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
            }
        }

        if (adView.getCallToActionView() != null) {
            if (nativeAd.getCallToAction() == null) {
                adView.getCallToActionView().setVisibility(View.GONE);
            } else {
                adView.getCallToActionView().setVisibility(View.VISIBLE);
                ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
            }
        }

        adView.setNativeAd(nativeAd);
        AdContainer.removeAllViews();
        AdContainer.addView(view);
    }

    private AdSize getAdSize(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);
        float widthPixels = outMetrics.widthPixels;
        float density = outMetrics.density;
        int adWidth = (int) (widthPixels / density);
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(activity, adWidth);
    }

    public void showBanner(Activity activity, final FrameLayout AdContainer) {
        if (adsUtils.isGoogleBannerOn(activity) && adsUtils.googleBannerId(activity) != null && !adsUtils.googleBannerId(activity).isEmpty()) {
            AdManagerAdView adManagerAdView = new AdManagerAdView(activity);
            adManagerAdView.setAdUnitId(adsUtils.googleBannerId(activity));
            AdManagerAdRequest adRequest = new AdManagerAdRequest.Builder().build();
            AdSize adSize = getAdSize(activity);
            adManagerAdView.setAdSize(adSize);
            adManagerAdView.loadAd(adRequest);
            adManagerAdView.setAdListener(new AdListener() {
                @Override
                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                    super.onAdFailedToLoad(loadAdError);
                    Log.e(TAG, "Banner onAdFailedToLoad: ");
                    AdContainer.removeAllViews();
                    ApplovinAds.getInstance().showBanner(activity, AdContainer);
                }

                @Override
                public void onAdLoaded() {
                    super.onAdLoaded();
                    Log.e(TAG, "Banner onAdLoaded: ");
                    AdContainer.removeAllViews();
                    AdContainer.addView(adManagerAdView);
                }
            });
        }
    }
}