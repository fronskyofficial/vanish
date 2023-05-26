package com.fronsky.vanish.logic.metrics;

public interface IMetrics {
    /**
     * Starts the metrics recording.
     */
    void start();

    /**
     * Stops the metrics recording.
     */
    void stop();

    /**
     * Records a metric with the given name and value.
     *
     * @param metric the name of the metric.
     * @param value  the value of the metric.
     */
    void record(String metric, String value);

    /**
     * Retrieves the value of the metric with the given name.
     *
     * @param metric the name of the metric to retrieve.
     * @return the value of the metric.
     */
    String get(String metric);
}
