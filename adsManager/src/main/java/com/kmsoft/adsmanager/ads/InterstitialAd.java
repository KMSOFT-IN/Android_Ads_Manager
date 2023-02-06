package com.kmsoft.adsmanager.ads;

import static com.google.ads.AdRequest.LOGTAG;
import static com.kmsoft.adsmanager.Constants.Utils.sorting;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.facebook.ads.AbstractAdListener;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.kmsoft.adsmanager.Constants.Utils;
import com.unity3d.ads.IUnityAdsInitializationListener;
import com.unity3d.ads.IUnityAdsLoadListener;
import com.unity3d.ads.IUnityAdsShowListener;
import com.unity3d.ads.UnityAds;
import com.unity3d.ads.UnityAdsShowOptions;

import java.util.List;

public class InterstitialAd {

    com.facebook.ads.InterstitialAd FbInterstitialAd;
    com.google.android.gms.ads.interstitial.InterstitialAd googleInterstitialAd;
    Context context;
    boolean isUnityLoad = false;

    public InterstitialAd(Context context) {
        this.context = context;
    }

    private void GoogleInterstitialAd() {

        AdRequest adRequest = new AdRequest.Builder().build();
        com.google.android.gms.ads.interstitial.InterstitialAd.load(
                context,
                Utils.GOOGLE_INTERSTITIAL,
                adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull com.google.android.gms.ads.interstitial.InterstitialAd interstitialAd) {
                        googleInterstitialAd = interstitialAd;
                        interstitialAd.setFullScreenContentCallback(
                                new FullScreenContentCallback() {
                                    @Override
                                    public void onAdDismissedFullScreenContent() {
                                        Log.d("TAG", "The ad was dismissed.");
                                    }

                                    @Override
                                    public void onAdFailedToShowFullScreenContent(com.google.android.gms.ads.AdError adError) {
                                        Log.d("TAG", "The ad failed to show.");
                                    }

                                    @Override
                                    public void onAdShowedFullScreenContent() {
                                        // Called when fullscreen content is shown.
                                        Log.d("TAG", "The ad was shown.");
                                    }
                                });
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        String error = String.format("domain: %s, code: %d, message: %s", loadAdError.getDomain(), loadAdError.getCode(), loadAdError.getMessage());
                        System.out.println("onAdFailedToLoad() with error: " + error);
//                        Toast.makeText(context, "onAdFailedToLoad() with error: " + error, Toast.LENGTH_SHORT).show();
                    }
                });

        UnityInterstitialAd();

    }

    public void showInterstitialAd(Activity activity) {

        List<Integer> priorityList = sorting();

        for (int i = 0; i < priorityList.size(); i++) {

            if (priorityList.get(i) == Utils.fbPriority){
                if (FbInterstitialAd != null) {
                    FbInterstitialAd.show();
//                    Toast.makeText(context, "fb Ad show", Toast.LENGTH_SHORT).show();
                    FbInterstitialAd();
                    break;
                }
            } else if (priorityList.get(i) == Utils.googlePriority){
                if (googleInterstitialAd != null) {
                    googleInterstitialAd.show(activity);
//                    Toast.makeText(context, "google Ad show", Toast.LENGTH_SHORT).show();
                    GoogleInterstitialAd();
                    break;
                }
            } else if (priorityList.get(i) == Utils.unityPriority){

                if (isUnityLoad){
                    UnityAds.show(activity, Utils.UNITY_INTERSTITIAL_ID, new UnityAdsShowOptions(), new IUnityAdsShowListener() {
                        @Override
                        public void onUnityAdsShowFailure(String placementId, UnityAds.UnityAdsShowError error, String message) {
                            Log.e(LOGTAG, "onUnityAdsShowFailure: " + error + " - " + message);
                        }

                        @Override
                        public void onUnityAdsShowStart(String placementId) {
                            Log.v(LOGTAG, "onUnityAdsShowStart: " + placementId);
                        }

                        @Override
                        public void onUnityAdsShowClick(String placementId) {
                            Log.v(LOGTAG,"onUnityAdsShowClick: " + placementId);
                        }

                        @Override
                        public void onUnityAdsShowComplete(String placementId, UnityAds.UnityAdsShowCompletionState state) {
                            Log.v(LOGTAG,"onUnityAdsShowComplete: " + placementId);
                            UnityInterstitialAd();
                        }
                    });
                    break;
                }
            }
        }
    }

    public void FbInterstitialAd() {
        FbInterstitialAd = new com.facebook.ads.InterstitialAd(context, Utils.FB_INTERSTITIAL);
        AbstractAdListener adListener = new AbstractAdListener() {
            @Override
            public void onError(Ad ad, AdError error) {
//                Toast.makeText(context, "Error loading ad: " + error.getErrorMessage(), Toast.LENGTH_SHORT).show();
                super.onError(ad, error);
                FbInterstitialAd = null;
            }

            @Override
            public void onAdLoaded(Ad ad) {
                super.onAdLoaded(ad);

            }

            @Override
            public void onAdClicked(Ad ad) {
                super.onAdClicked(ad);
            }

            @Override
            public void onInterstitialDisplayed(Ad ad) {
                super.onInterstitialDisplayed(ad);
            }

            @Override
            public void onInterstitialDismissed(Ad ad) {
                super.onInterstitialDismissed(ad);
            }
        };
        com.facebook.ads.InterstitialAd.InterstitialLoadAdConfig interstitialLoadAdConfig = FbInterstitialAd.buildLoadAdConfig()
                .withAdListener(adListener)
                .build();
        FbInterstitialAd.loadAd(interstitialLoadAdConfig);

        GoogleInterstitialAd();
    }

    private void UnityInterstitialAd(){
        UnityAds.initialize(context, Utils.UNITY_GAME_ID, Utils.isUnityTest, new IUnityAdsInitializationListener() {
            @Override
            public void onInitializationComplete() {
                Log.v(LOGTAG, "Unity Ads initialization complete");

                UnityAds.load(Utils.UNITY_INTERSTITIAL_ID, new IUnityAdsLoadListener() {
                    @Override
                    public void onUnityAdsAdLoaded(String placementId) {
                        Log.v(LOGTAG, "Ad for " + placementId + " loaded");
                        isUnityLoad = true;
                    }

                    @Override
                    public void onUnityAdsFailedToLoad(String placementId, UnityAds.UnityAdsLoadError error, String message) {
                        Log.e(LOGTAG, "Ad for " + placementId + " failed to load: [" + error + "] " + message);
                        isUnityLoad = false;
                    }
                });
            }

            @Override
            public void onInitializationFailed(UnityAds.UnityAdsInitializationError error, String message) {
                Log.e(LOGTAG, "Unity Ads initialization failed: [" + error + "] " + message);
                isUnityLoad = false;
            }
        });
    }
}
