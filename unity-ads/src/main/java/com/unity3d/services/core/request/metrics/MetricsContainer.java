package com.unity3d.services.core.request.metrics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MetricsContainer {

	private static final String METRICS_CONTAINER = "m";
	private static final String METRICS_CONTAINER_TAGS = "t";
	private static final String METRIC_CONTAINER_SAMPLE_RATE = "msr";
	private static final String METRIC_CONTAINER_SESSION_TOKEN = "sTkn";

	private final MetricCommonTags _commonTags;
	private final List<Metric> _metrics;
	private final String _metricSampleRate;
	private final String _sTkn;

	public MetricsContainer(String metricSampleRate, MetricCommonTags commonTags, List<Metric> metrics, String sTkn) {
		this._metricSampleRate = metricSampleRate;
		this._commonTags = commonTags;
		this._metrics = metrics;
		this._sTkn = sTkn;
	}

	public Map<String, Object> asMap() {
		Map<String, Object> result = new HashMap<>();
		List<Object> metricsMaps = new ArrayList<>();

		for (Metric metric: _metrics) {
			metricsMaps.add(metric.asMap());
		}

		result.put(METRIC_CONTAINER_SAMPLE_RATE, _metricSampleRate);
		result.put(METRICS_CONTAINER, metricsMaps);
		result.put(METRICS_CONTAINER_TAGS, _commonTags.asMap());
		result.put(METRIC_CONTAINER_SESSION_TOKEN, _sTkn);

		return result;
	}
}
