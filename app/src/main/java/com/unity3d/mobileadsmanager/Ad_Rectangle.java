package com.unity3d.mobileadsmanager;

import static com.unity3d.mobileadsmanager.ActivityBase.rectangleAd;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.RelativeLayout;

public class Ad_Rectangle extends AppCompatActivity {
    RelativeLayout adContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_rectangle2);

        adContainer = findViewById(R.id.ad_rectangle);

        rectangleAd.showRectangleAd(adContainer,this);
    }
}