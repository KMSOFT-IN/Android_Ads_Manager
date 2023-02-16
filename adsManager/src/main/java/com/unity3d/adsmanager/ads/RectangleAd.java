package com.unity3d.adsmanager.ads;

import static com.google.ads.AdRequest.LOGTAG;
import static com.unity3d.adsmanager.Constants.Utils.sorting;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdListener;
import com.facebook.ads.AdView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.LoadAdError;
import com.unity3d.adsmanager.Constants.Utils;
import com.unity3d.ads.IUnityAdsInitializationListener;
import com.unity3d.ads.UnityAds;
import com.unity3d.services.banners.BannerErrorInfo;
import com.unity3d.services.banners.BannerView;
import com.unity3d.services.banners.UnityBannerSize;
//import IUnityAdsInitializationListener;
//import UnityAds;
//import UnityAds;
//import com.unity3d.com.unity3d.scar.services.banners.BannerErrorInfo;
//import com.unity3d.com.unity3d.scar.services.banners.BannerView;
//import com.unity3d.com.unity3d.scar.services.banners.UnityBannerSize;

import java.util.List;

public class RectangleAd {

    Ad fbAd;
    AdView fbAdView;
    com.google.android.gms.ads.AdView googleAdView;
    BannerView unityBanner;
    boolean isGoogleAdLoaded = false;
    boolean isUnityAdLoaded = false;
    Context context;


    public RectangleAd(Context context) {
        this.context = context;
    }

    private void loadGoogleAd() {
        googleAdView = new com.google.android.gms.ads.AdView(context);
        googleAdView.setAdUnitId(Utils.GOOGLE_BANNER_ID);
        googleAdView.setAdSize(AdSize.MEDIUM_RECTANGLE);
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
        fbAdView = new AdView(context, Utils.FB_RECTANGLE, com.facebook.ads.AdSize.RECTANGLE_HEIGHT_250);
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

    public void showRectangleAd(ViewGroup adContainer, Activity activity) {

        unityBanner = new BannerView(activity, Utils.UNITY_BANNER_ID, new UnityBannerSize(Utils.unityBannerWidth,Utils.unityBannerHeight));
        unityBanner.setListener(new BannerView.Listener() {
            @Override
            public void onBannerLoaded(BannerView bannerAdView) {
                super.onBannerLoaded(bannerAdView);
                unityBanner = bannerAdView;
                isUnityAdLoaded = true;
            }

            @Override
            public void onBannerFailedToLoad(BannerView bannerAdView, BannerErrorInfo errorInfo) {
                super.onBannerFailedToLoad(bannerAdView, errorInfo);
                isUnityAdLoaded = false;
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
                    } else if (priorityList.get(i) == Utils.googlePriority) {
                        if (isGoogleAdLoaded) {
                            adContainer.addView(googleAdView);
//                            Toast.makeText(context, "google Ad show", Toast.LENGTH_SHORT).show();
                            loadGoogleAd();
                            break;
                        }
                    } else if (priorityList.get(i) == Utils.unityPriority) {
                        if (isUnityAdLoaded) {
                            adContainer.addView(unityBanner);
//                            Toast.makeText(context, "unity Ad show", Toast.LENGTH_SHORT).show();
                            break;
                        }
                    }
                }
            }
        },1000);



    }

    private void loadUnityAd() {

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
    }
}
