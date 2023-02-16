package com.unity3d.scar.adapter.v2100;

import android.content.Context;

import com.unity3d.scar.adapter.common.IAdsErrorHandler;
import com.unity3d.scar.adapter.common.IScarAdapter;
import com.unity3d.scar.adapter.common.IScarInterstitialAdListenerWrapper;
import com.unity3d.scar.adapter.common.IScarRewardedAdListenerWrapper;
import com.unity3d.scar.adapter.common.ScarAdapterBase;
import com.unity3d.scar.adapter.common.requests.RequestExtras;
import com.unity3d.scar.adapter.common.scarads.IScarLoadListener;
import com.unity3d.scar.adapter.common.scarads.ScarAdMetadata;
import com.unity3d.scar.adapter.v2100.requests.AdRequestFactory;
import com.unity3d.scar.adapter.v2100.scarads.ScarInterstitialAd;
import com.unity3d.scar.adapter.v2100.scarads.ScarRewardedAd;
import com.unity3d.scar.adapter.v2100.signals.SignalsCollector;

import static com.unity3d.scar.adapter.common.Utils.runOnUiThread;

public class ScarAdapter extends ScarAdapterBase implements IScarAdapter {
	private AdRequestFactory _adRequestFactory;

	public ScarAdapter(IAdsErrorHandler adsErrorHandler, String unityVersionName) {
		super(adsErrorHandler);
		_adRequestFactory = new AdRequestFactory(new RequestExtras(unityVersionName));
		_signalCollector = new SignalsCollector(_adRequestFactory);
	}

	public void loadInterstitialAd(Context context, final ScarAdMetadata scarAd, final IScarInterstitialAdListenerWrapper adListenerWrapper) {
		final ScarInterstitialAd interstitialAd = new ScarInterstitialAd(context, _adRequestFactory, scarAd, _adsErrorHandler, adListenerWrapper);
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				interstitialAd.loadAd(new IScarLoadListener() {
					@Override
					public void onAdLoaded() {
						_loadedAds.put(scarAd.getPlacementId(), interstitialAd);
					}
				});
			}
		});
	}

	public void loadRewardedAd(Context context, final ScarAdMetadata scarAd, final IScarRewardedAdListenerWrapper adListenerWrapper) {
		final ScarRewardedAd rewardedAd = new ScarRewardedAd(context, _adRequestFactory, scarAd, _adsErrorHandler, adListenerWrapper);
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				rewardedAd.loadAd(new IScarLoadListener() {
					@Override
					public void onAdLoaded() {
						_loadedAds.put(scarAd.getPlacementId(), rewardedAd);
					}
				});
			}
		});
	}

}
