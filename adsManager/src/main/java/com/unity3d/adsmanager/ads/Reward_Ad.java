package com.unity3d.adsmanager.ads;


import static com.google.ads.AdRequest.LOGTAG;
import static com.unity3d.adsmanager.Constants.Utils.sorting;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.RewardedVideoAd;
import com.facebook.ads.RewardedVideoAdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.unity3d.adsmanager.Constants.Utils;
import com.unity3d.adsmanager.listener.FbReward;
import com.unity3d.adsmanager.listener.GoogleReward;
import com.unity3d.adsmanager.listener.GoogleRewardItem;
import com.unity3d.ads.IUnityAdsInitializationListener;
import com.unity3d.ads.IUnityAdsLoadListener;
import com.unity3d.ads.IUnityAdsShowListener;
import com.unity3d.ads.UnityAds;
import com.unity3d.ads.UnityAdsShowOptions;

import java.util.List;

public class Reward_Ad {

    RewardedVideoAd fbRewardedVideoAd;
    RewardedAd googleRewardVideoAd;
    Context context;
    FbReward fbReward;
    boolean isUnityLoad = false;

    public Reward_Ad(Context context, FbReward fbReward ) {
        this.context = context;
        this.fbReward = fbReward;
    }

    public void loadFbRewardVideo() {
        fbRewardedVideoAd = new RewardedVideoAd(context, Utils.FB_REWARD);

        RewardedVideoAdListener rewardedVideoAdListener = new RewardedVideoAdListener() {
            @Override
            public void onRewardedVideoCompleted() {
                fbReward.onRewardedVideoCompleted();
            }

            @Override
            public void onRewardedVideoClosed() {
                fbReward.onRewardedVideoClosed();
            }

            @Override
            public void onError(Ad ad, AdError adError) {
//                Toast.makeText(context, "Sorry, error on loading the ad. Try again!", Toast.LENGTH_SHORT).show();
                fbRewardedVideoAd = null;
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

        fbRewardedVideoAd.loadAd(
                fbRewardedVideoAd.buildLoadAdConfig()
                        .withAdListener(rewardedVideoAdListener)
                        .build());

        loadGoogleRewardVideo();
    }

    public void showRewardAd(Activity activity,GoogleReward googleReward) {
        List<Integer> priorityList = sorting();

        for (int i = 0; i < priorityList.size(); i++) {

            if (priorityList.get(i) == Utils.fbPriority) {
                if (fbRewardedVideoAd != null) {
                    fbRewardedVideoAd.show();
//                    Toast.makeText(context, "fb Ad show", Toast.LENGTH_SHORT).show();
                    loadFbRewardVideo();
                    break;
                }

            }
            else if (priorityList.get(i) == Utils.googlePriority) {
                if (googleRewardVideoAd != null) {
//                    Toast.makeText(context, "google Ad show", Toast.LENGTH_SHORT).show();
                    googleRewardVideoAd.show(
                            activity, new OnUserEarnedRewardListener() {
                                @Override
                                public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                                    GoogleRewardItem googleRewardItem = new GoogleRewardItem();
                                    googleRewardItem.amount = rewardItem.getAmount();
                                    googleRewardItem.type = rewardItem.getType();
                                    googleReward.onUserEarnedReward(googleRewardItem);
                                    loadGoogleRewardVideo();
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
                            loadUnityRewardAd();
                        }
                    });

                    break;
                }
            }
        }
    }

    private void loadGoogleRewardVideo() {

        AdRequest adRequest = new AdRequest.Builder().build();
        RewardedAd.load(
                context,
                Utils.GOOGLE_REWARD,
                adRequest,
                new RewardedAdLoadCallback() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        googleRewardVideoAd = null;
                    }

                    @Override
                    public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                        googleRewardVideoAd = rewardedAd;
                    }
                });

        loadUnityRewardAd();
    }

    private void loadUnityRewardAd(){
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
