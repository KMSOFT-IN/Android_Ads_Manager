package com.unity3d.adsmanager.Constants;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;

import com.facebook.ads.AdSettings;
import com.facebook.ads.AudienceNetworkAds;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Utils {


//    static String FB_BANNER_90 = "910721003297246_910723819963631";
    public static String FB_RECTANGLE;
    public static String FB_NATIVE;
    public static String FB_BANNER_ID;
    public static String FB_INTERSTITIAL;
    public static String FB_REWARD;
    public static String FB_REWARD_INTERSTITIAL;

    public static String GOOGLE_APPLICATION_ID;
    public static String GOOGLE_BANNER_ID;
    public static String GOOGLE_INTERSTITIAL;
    public static String GOOGLE_REWARD;
    public static String GOOGLE_REWARD_INTERSTITIAL;

    public static String UNITY_GAME_ID;
    public static boolean isUnityTest;

    public static String UNITY_BANNER_ID;
    public static String UNITY_INTERSTITIAL_ID;
    public static String UNITY_REWARD_ID;

    // TODO for highPriority set highNumber
    public static int fbPriority;
    public static int googlePriority;
    public static int unityPriority;

    public static int fbBannerHeight;
    public static int googleBannerHeight;
    public static int unityBannerHeight;
    public static int unityBannerWidth;
    public static Context baseContext;

    public static void initialize(Context context){
        baseContext = context;
        AudienceNetworkAds.initialize(context);

        // Initialize the Mobile Ads SDK.
        MobileAds.initialize(context, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {
            }
        });


    }

    public static void setTestMode(boolean result){
        AdSettings.setTestMode(result);
        isUnityTest = result;
    }

    public static void setGoogleApplicationId(String googleApplicationId) {
        GOOGLE_APPLICATION_ID = googleApplicationId;

        try {
            ApplicationInfo ai = baseContext.getPackageManager().getApplicationInfo(baseContext.getPackageName(), PackageManager.GET_META_DATA);
            ai.metaData.putString("com.google.android.gms.ads.APPLICATION_ID", GOOGLE_APPLICATION_ID);//you can replace your key APPLICATION_ID here
        } catch (PackageManager.NameNotFoundException | NullPointerException ignored) {

        }
    }

    public static void setUnityBannerId(String unityBannerId) {
        UNITY_BANNER_ID = unityBannerId;
    }

    public static void setUnityInterstitialId(String unityInterstitialId) {
        UNITY_INTERSTITIAL_ID = unityInterstitialId;
    }

    public static void setUnityRewardId(String unityRewardId) {
        UNITY_REWARD_ID = unityRewardId;
    }

    public static void setFbNative(String fbNative) {
        FB_NATIVE = fbNative;
    }

    public static void setFbRectangle(String fbRectangle) {
        FB_RECTANGLE = fbRectangle;
    }

    public static void setFbBannerId(String fbBannerId) {
        FB_BANNER_ID = fbBannerId;
    }

    public static void setFbInterstitial(String fbInterstitial) {
        FB_INTERSTITIAL = fbInterstitial;
    }

    public static void setFbReward(String fbReward) {
        FB_REWARD = fbReward;
    }

    public static void setFbRewardInterstitial(String fbRewardInterstitial) {
        FB_REWARD_INTERSTITIAL = fbRewardInterstitial;
    }

    public static void setGoogleBannerId(String googleBannerId) {
        GOOGLE_BANNER_ID = googleBannerId;
    }

    public static void setGoogleInterstitial(String googleInterstitial) {
        GOOGLE_INTERSTITIAL = googleInterstitial;
    }

    public static void setGoogleReward(String googleReward) {
        GOOGLE_REWARD = googleReward;
    }

    public static void setGoogleRewardInterstitial(String googleRewardInterstitial) {
        GOOGLE_REWARD_INTERSTITIAL = googleRewardInterstitial;
    }

    public static void setFbPriority(int fbPriority) {
        Utils.fbPriority = fbPriority;
    }

    public static void setGooglePriority(int googlePriority) {
        Utils.googlePriority = googlePriority;
    }

    public static void setUnityPriority(int unityPriority) {
        Utils.unityPriority = unityPriority;
    }

    public static void setGoogleBannerHeight(int googleBannerHeight) {

        if (googleBannerHeight < 0 && googleBannerHeight != -2 && googleBannerHeight != -4) {
            StringBuilder var5 = new StringBuilder();
            var5.append("Invalid height for AdSize: ");
            var5.append(googleBannerHeight);
            throw new IllegalArgumentException(var5.toString());
        }else {
            Utils.googleBannerHeight = googleBannerHeight;
        }
    }

    public static void setFbBannerHeight(int fbBannerHeight) {
        if (fbBannerHeight == 50 || fbBannerHeight == 90 || fbBannerHeight == 250){
            Utils.fbBannerHeight = fbBannerHeight;
        }else {
            throw new IllegalArgumentException("Can't create AdSize using this height. height should be 50,90,250");
        }

    }

    public static void setUnityBannerHeight(int unityBannerHeight) {

        if (unityBannerHeight == 50 || unityBannerHeight == 60 || unityBannerHeight == 90){
            Utils.unityBannerHeight = unityBannerHeight;
        }else {
            throw new IllegalArgumentException("Can't create AdSize using this height. height should be 50,90,60");
        }
    }

    public static void setUnityBannerWidth(int unityBannerWidth) {

        if (unityBannerWidth == 320 || unityBannerWidth == 468 || unityBannerWidth == 728){
            Utils.unityBannerWidth = unityBannerWidth;
        }else {
            throw new IllegalArgumentException("Can't create AdSize using this height. height should be 320,468,728");
        }
    }

    public static void setUnityGameId(String unityGameId) {
        UNITY_GAME_ID = unityGameId;
    }

    public static List<Integer> sorting(){

        List<Integer> integerList = new ArrayList<>();

        integerList.add(Utils.fbPriority);
        integerList.add(Utils.googlePriority);
        integerList.add(Utils.unityPriority);

        Collections.sort(integerList, Collections.reverseOrder());

        return integerList;
    }
}