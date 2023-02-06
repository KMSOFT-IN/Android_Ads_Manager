package com.kmsoft.mobileadsmanager;


import android.app.Application;

import com.kmsoft.adsmanager.Constants.Utils;
import com.kmsoft.adsmanager.ads.BannerAd;
import com.kmsoft.adsmanager.ads.InterstitialAd;
import com.kmsoft.adsmanager.ads.RectangleAd;
import com.kmsoft.adsmanager.ads.RewardInterstitialAd;
import com.kmsoft.adsmanager.ads.Reward_Ad;
import com.kmsoft.adsmanager.listener.FbReward;
import com.kmsoft.adsmanager.listener.FbRewardInterstitial;

public class ActivityBase extends Application implements FbReward, FbRewardInterstitial {

    public static BannerAd bannerAd;
    public static RectangleAd rectangleAd;
    public static InterstitialAd interstitialAd;
    public static Reward_Ad reward_ad;
    public static RewardInterstitialAd rewardInterstitialAd;

    @Override
    public void onCreate() {
        super.onCreate();
//        AudienceNetworkAds.initialize(this);
        Utils.initialize(this);

        //This will make the ad run on the test device, let's say your Android AVD emulator
        if (BuildConfig.DEBUG){
            Utils.setTestMode(true);
        }

        //TODO set applicationId for Google
        Utils.setGoogleApplicationId("ca-app-pub-3940256099942544~3347511713");

        // TODO set Banner Ad Data
        Utils.setFbBannerId("910721003297246_910723819963631");
        Utils.setGoogleBannerId("ca-app-pub-3940256099942544/6300978111");
        Utils.setUnityBannerId("Banner123");

        // TODO set Interstitial Ad data
        Utils.setGoogleInterstitial("ca-app-pub-3940256099942544/1033173712");
        Utils.setFbInterstitial("910721003297246_910776049958408");
        Utils.setUnityInterstitialId("Interstitial123");

        // TODO set Reward Video Ad data
        Utils.setGoogleReward("ca-app-pub-3940256099942544/5224354917");
        Utils.setFbReward("910721003297246_910776606625019");
        Utils.setUnityRewardId("Reward123");

        // TODO set Reward Interstitial Ad data
        Utils.setGoogleRewardInterstitial("ca-app-pub-3940256099942544/5354046379");
        Utils.setFbRewardInterstitial("910721003297246_910785149957498");

        Utils.setFbRectangle("910721003297246_910775713291775");

        // TODO set Unity GameId
        Utils.setUnityGameId("5147445");

        // TODO set size
        Utils.setFbBannerHeight(50);
        Utils.setGoogleBannerHeight(50);
        Utils.setUnityBannerHeight(50);
        Utils.setUnityBannerWidth(320);

        // TODO set Priority
        Utils.setFbPriority(4);
        Utils.setGooglePriority(6);
        Utils.setUnityPriority(5);

        // TODO load Ads
        bannerAd = new BannerAd(this);
        rectangleAd = new RectangleAd(this);
        interstitialAd = new InterstitialAd(this);
        reward_ad = new Reward_Ad(this,this);
        rewardInterstitialAd = new RewardInterstitialAd(this,this);
        bannerAd.loadFbAd();
        rectangleAd.loadFbAd();
        interstitialAd.FbInterstitialAd();
        reward_ad.loadFbRewardVideo();
        rewardInterstitialAd.loadFbRewardedInterstitial();
    }

    @Override
    public void onRewardedVideoCompleted() {
//        Toast.makeText(this, "complete", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoClosed() {
//        Toast.makeText(this, "closed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedInterstitialCompleted() {
//        Toast.makeText(this, "complete", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedInterstitialClosed() {
//        Toast.makeText(this, "closed", Toast.LENGTH_SHORT).show();
    }
}