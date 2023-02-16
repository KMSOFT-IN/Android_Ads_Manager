package com.unity3d.mobileadsmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.RelativeLayout;

public class Ad_Banner extends AppCompatActivity {
    RelativeLayout adContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_banner2);

        adContainer = findViewById(R.id.ad_banner_50);

        ActivityBase.bannerAd.showBannerAd(adContainer,Ad_Banner.this);

    }
}