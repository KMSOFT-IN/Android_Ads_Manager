package com.unity3d.mobileadsmanager;

import static com.unity3d.mobileadsmanager.ActivityBase.rewardInterstitialAd;
import static com.unity3d.mobileadsmanager.ActivityBase.reward_ad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.kmsoft.adsmanager.listener.GoogleReward;
import com.kmsoft.adsmanager.listener.GoogleRewardItem;

public class Ad_Reward extends AppCompatActivity implements GoogleReward {

    boolean isVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_reward2);

        isVideo = getIntent().getBooleanExtra("isVideo", true);


        if (isVideo) {
            reward_ad.showRewardAd(this,this);
        } else {
            rewardInterstitialAd.showRewardInterstitialAd(this,this);
        }

    }

    @Override
    public void onUserEarnedReward(@NonNull GoogleRewardItem rewardItem) {
        int rewardAmount = rewardItem.getAmount();
        String rewardType = rewardItem.getType();

//        Toast.makeText(this, "amount : " + rewardAmount, Toast.LENGTH_SHORT).show();
//        Toast.makeText(this, "type : " + rewardType, Toast.LENGTH_SHORT).show();
    }
}