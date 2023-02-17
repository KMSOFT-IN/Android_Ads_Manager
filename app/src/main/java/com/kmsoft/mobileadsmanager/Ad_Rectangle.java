package com.kmsoft.mobileadsmanager;

import static com.kmsoft.mobileadsmanager.ActivityBase.rectangleAd;

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