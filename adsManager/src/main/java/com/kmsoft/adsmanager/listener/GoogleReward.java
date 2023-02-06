package com.kmsoft.adsmanager.listener;

import androidx.annotation.NonNull;

public interface GoogleReward {

    void onUserEarnedReward(@NonNull GoogleRewardItem rewardItem);
}
