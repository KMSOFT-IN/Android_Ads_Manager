package com.kmsoft.adsmanager.ads;


import static com.google.ads.AdRequest.LOGTAG;
import static com.kmsoft.adsmanager.Constants.Utils.sorting;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.RewardedInterstitialAd;
import com.facebook.ads.RewardedInterstitialAdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAdLoadCallback;
import com.kmsoft.adsmanager.Constants.Utils;
import com.kmsoft.adsmanager.listener.FbRewardInterstitial;
import com.kmsoft.adsmanager.listener.GoogleReward;
import com.kmsoft.adsmanager.listener.GoogleRewardItem;
import com.unity3d.ads.IUnityAdsInitializationListener;
import com.unity3d.ads.IUnityAdsLoadListener;
import com.unity3d.ads.IUnityAdsShowListener;
import com.unity3d.ads.UnityAds;
import com.unity3d.ads.UnityAdsShowOptions;

import java.util.List;

public class RewardInterstitialAd {

    Context context;
    RewardedInterstitialAd fbRewardedInterstitialAd;
    com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAd googleRewardInterstitialAd;
    FbRewardInterstitial fbRewardInterstitial;
    boolean isUnityLoad = false;

    public RewardInterstitialAd(Context context, FbRewardInterstitial fbRewardInterstitial) {
        this.context = context;
        this.fbRewardInterstitial = fbRewardInterstitial;
    }

    public void loadFbRewardedInterstitial() {
        fbRewardedInterstitialAd = new RewardedInterstitialAd(context, Utils.FB_REWARD_INTERSTITIAL);
        RewardedInterstitialAdListener rewardedInterstitialAdListener = new RewardedInterstitialAdListener() {
            @Override
            public void onRewardedInterstitialCompleted() {
                fbRewardInterstitial.onRewardedInterstitialCompleted();
            }

            @Override
            public void onRewardedInterstitialClosed() {
                fbRewardInterstitial.onRewardedInterstitialClosed();
            }

            @Override
            public void onError(Ad ad, AdError adError) {
//                Toast.makeText(context, "Sorry, error on loading the ad. Try again!", Toast.LENGTH_SHORT).show();
                fbRewardedInterstitialAd = null;
            }

            @Override
            public void onAdLoaded(Ad ad) {

            }

            @Override
            public void onAdClicked(Ad ad) {

            }

            @Override
            public void onLoggingImpression(Ad ad) {

            }
        };
        fbRewardedInterstitialAd.loadAd(
                fbRewardedInterstitialAd.buildLoadAdConfig()
                        .withAdListener(rewardedInterstitialAdListener)
                        .build());

        loadGoogleRewardInterstitial();
    }

    private void loadGoogleRewardInterstitial() {

        AdRequest adRequest = new AdRequest.Builder().build();
        // Use the test ad unit ID to load an ad.
        com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAd.load(
                context,
                Utils.GOOGLE_REWARD_INTERSTITIAL,
                adRequest,
                new RewardedInterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAd ad) {
                        googleRewardInterstitialAd = ad;
                    }

                    @Override
                    public void onAdFailedToLoad(LoadAdError loadAdError) {

                        // Handle the error.
                        googleRewardInterstitialAd = null;
                    }
                });

        loadUnityRewardInterstitialAd();

    }

    public void showRewardInterstitialAd(Activity activity,GoogleReward googleReward) {
        List<Integer> priorityList = sorting();

        for (int i = 0; i < priorityList.size(); i++) {

            if (priorityList.get(i) == Utils.fbPriority) {
                if (fbRewardedInterstitialAd != null) {
                    fbRewardedInterstitialAd.show();
//                    Toast.makeText(context, "fb Ad show", Toast.LENGTH_SHORT).show();
                    loadFbRewardedInterstitial();
                    break;
                }

            }
            else if (priorityList.get(i) == Utils.googlePriority) {
                if (googleRewardInterstitialAd != null) {
//                    Toast.makeText(context, "google Ad show", Toast.LENGTH_SHORT).show();
                    googleRewardInterstitialAd.show(
                            activity, new OnUserEarnedRewardListener() {
                                @Override
                                public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                                    GoogleRewardItem googleRewardItem = new GoogleRewardItem();
                                    googleRewardItem.amount = rewardItem.getAmount();
                                    googleRewardItem.type = rewardItem.getType();
                                    googleReward.onUserEarnedReward(googleRewardItem);
                                    loadGoogleRewardInterstitial();
                                }
                            });
                    break;
                }

            }
            else if (priorityList.get(i) == Utils.unityPriority){

                if (isUnityLoad){
                    UnityAds.show(activity, Utils.UNITY_REWARD_ID, new UnityAdsShowOptions(), new IUnityAdsShowListener() {
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
                            loadUnityRewardInterstitialAd();
                        }
                    });
                    break;
                }
            }
        }
    }

    private void loadUnityRewardInterstitialAd(){
        UnityAds.initialize(context, Utils.UNITY_GAME_ID, Utils.isUnityTest, new IUnityAdsInitializationListener() {
            @Override
            public void onInitializationComplete() {
                Log.v(LOGTAG, "Unity Ads initialization complete");

                UnityAds.load(Utils.UNITY_REWARD_ID, new IUnityAdsLoadListener() {
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
