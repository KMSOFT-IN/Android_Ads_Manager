package com.kmsoft.adsmanager.ads;

import static com.google.ads.AdRequest.LOGTAG;
import static com.kmsoft.adsmanager.Constants.Utils.sorting;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdListener;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.kmsoft.adsmanager.Constants.Utils;
import com.unity3d.ads.IUnityAdsInitializationListener;
import com.unity3d.ads.UnityAds;
import com.unity3d.services.banners.BannerErrorInfo;
import com.unity3d.services.banners.BannerView;
import com.unity3d.services.banners.UnityBannerSize;

import java.util.List;

public class BannerAd {

    Ad fbAd;
    AdView fbAdView;
    com.google.android.gms.ads.AdView googleAdView;
    BannerView unityBanner;
    boolean isGoogleAdLoaded = false;
    Context context;

    public BannerAd(Context context) {
        this.context = context;
    }

    private void loadGoogleAd() {
        googleAdView = new com.google.android.gms.ads.AdView(context);
        googleAdView.setAdUnitId(Utils.GOOGLE_BANNER_ID);
        googleAdView.setAdSize(new com.google.android.gms.ads.AdSize(-1, Utils.googleBannerHeight));
        AdRequest adRequest = new AdRequest.Builder().build();
        googleAdView.setAdListener(new com.google.android.gms.ads.AdListener() {
            @Override
            public void onAdLoaded() {
                isGoogleAdLoaded = true;
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                isGoogleAdLoaded = false;
            }
        });
        googleAdView.loadAd(adRequest);

        loadUnityAd();
    }

    public void loadFbAd() {
        fbAdView = new AdView(context, Utils.FB_BANNER_ID, new AdSize(-1, Utils.fbBannerHeight));
        AdListener fbAdListener = new AdListener() {
            @Override
            public void onError(Ad ad, AdError adError) {
                fbAd = null;
            }

            @Override
            public void onAdLoaded(Ad ad) {
                fbAd = ad;
            }

            @Override
            public void onAdClicked(Ad ad) {
            }

            @Override
            public void onLoggingImpression(Ad ad) {
            }
        };
        AdView.AdViewLoadConfig loadAdConfig = fbAdView.buildLoadAdConfig()
                .withAdListener(fbAdListener)
                .build();
        fbAdView.loadAd(loadAdConfig);

        loadGoogleAd();
    }

    public void loadUnityAd() {

        UnityAds.initialize(context, Utils.UNITY_GAME_ID, Utils.isUnityTest, new IUnityAdsInitializationListener() {
            @Override
            public void onInitializationComplete() {
                Log.v(LOGTAG, "Unity Ads initialization complete");



            }

            @Override
            public void onInitializationFailed(UnityAds.UnityAdsInitializationError error, String message) {
                Log.e(LOGTAG, "Unity Ads initialization failed: [" + error + "] " + message);
            }
        });


//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                showBannerAd();
//            }
//        }, 2500);
    }

    public void showBannerAd(ViewGroup adContainer, Activity activity) {

        unityBanner = new BannerView(activity, Utils.UNITY_BANNER_ID, new UnityBannerSize(Utils.unityBannerWidth,Utils.unityBannerHeight));
        unityBanner.setListener(new BannerView.Listener() {
            @Override
            public void onBannerLoaded(BannerView bannerAdView) {
                super.onBannerLoaded(bannerAdView);
                unityBanner = bannerAdView;

            }

            @Override
            public void onBannerFailedToLoad(BannerView bannerAdView, BannerErrorInfo errorInfo) {
                super.onBannerFailedToLoad(bannerAdView, errorInfo);
                unityBanner = null;
            }

            @Override
            public void onBannerClick(BannerView bannerAdView) {
                super.onBannerClick(bannerAdView);
            }

            @Override
            public void onBannerLeftApplication(BannerView bannerAdView) {
                super.onBannerLeftApplication(bannerAdView);
            }
        });
        unityBanner.load();

        List<Integer> priorityList = sorting();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < priorityList.size(); i++) {

                    if (priorityList.get(i) == Utils.fbPriority) {
                        if (fbAd != null) {
                            adContainer.addView(fbAdView);
//                            Toast.makeText(context, "fb Ad show", Toast.LENGTH_SHORT).show();
                            loadFbAd();
                            break;
                        }
                    }
                    else if (priorityList.get(i) == Utils.googlePriority) {
                        if (isGoogleAdLoaded) {
                            adContainer.addView(googleAdView);
//                            Toast.makeText(context, "google Ad show", Toast.LENGTH_SHORT).show();
                            loadGoogleAd();
                            break;
                        }
                    }
                    else if (priorityList.get(i) == Utils.unityPriority) {

                        if (unityBanner != null) {
                            adContainer.addView(unityBanner);
//                            Toast.makeText(context, "unity Ad show", Toast.LENGTH_SHORT).show();
                            break;
                        }

                    }
                }
            }
        },1000);



    }
}
