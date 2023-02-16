package com.unity3d.services.ads.operation;

import com.unity3d.services.core.request.metrics.ISDKMetrics;
import com.unity3d.services.core.webview.bridge.IWebViewSharedObject;
import com.unity3d.services.core.webview.bridge.WebViewBridgeSharedObjectStore;
import com.unity3d.services.core.webview.bridge.invocation.WebViewBridgeInvocationSingleThreadedExecutor;

import java.util.concurrent.ExecutorService;

public abstract class AdModule<T extends IWebViewSharedObject, T2> extends WebViewBridgeSharedObjectStore<T> implements IAdModule<T, T2> {
	protected ISDKMetrics _sdkMetrics;
	protected ExecutorService _executorService;

	protected AdModule(ISDKMetrics sdkMetrics) {
		super();
		_sdkMetrics = sdkMetrics;
		_executorService = WebViewBridgeInvocationSingleThreadedExecutor.getInstance().getExecutorService();
	}

	public ISDKMetrics getMetricSender() {
		return _sdkMetrics;
	}
}
