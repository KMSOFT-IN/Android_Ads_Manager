package com.kmsoft.mobileadsmanager;

import static com.kmsoft.mobileadsmanager.ActivityBase.interstitialAd;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

public class Ad_Interstitial extends AppCompatActivity {
    ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_interstitial2);

        progress = findViewById(R.id.interstitial_progress);
        progress.setVisibility(View.GONE);

        interstitialAd.showInterstitialAd(this);

    }
}