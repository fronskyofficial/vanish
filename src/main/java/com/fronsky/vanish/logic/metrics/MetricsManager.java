package com.fronsky.vanish.logic.metrics;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MetricsManager {
    private final Map<UUID, IMetrics> metrics = new HashMap<>();

    public void addMetric(UUID id, IMetrics metric) {
        metric.start();
        metrics.put(id, metric);
    }

    public void stopMetric(UUID id) {
        if (metrics.containsKey(id)) {
            metrics.get(id).stop();
            metrics.remove(id);
        }
    }

    public void recordMetric(UUID id, String metric, String value) {
        if (metrics.containsKey(id)) {
            metrics.get(id).record(metric, value);
        }
    }

    public String getMetric(UUID id, String metric) {
        if (metrics.containsKey(id)) {
            return metrics.get(id).get(metric);
        }
        return null;
    }
}
