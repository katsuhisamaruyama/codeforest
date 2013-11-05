/*
 *  Copyright 2013, Katsuhisa Maruyama (maru@jtool.org)
 */

package org.jtool.codeforest.metrics.java;

import org.jtool.codeforest.metrics.UnsupportedMetricsException;
import java.util.HashMap;
import java.util.Map;

/**
 * An object storing information on metrics.
 * @author Katsuhisa Maruyama
 */
public class CommonMetrics {
    
    /**
     * The collection of metric names and their values.
     */
    protected Map<String, Double> metrics = new HashMap<String, Double>();
    
    /**
     * Creates a new object storing metrics.
     */
    protected CommonMetrics() {
    }
    
    /**
     * Returns the value of a metric with a given sort.
     * @param sort the sort of the metric
     * @return the metric value
     * @throws UnsupportedMetricsException if the metric value is not obtained
     */
    public double getMetricValueWithException(String sort) throws UnsupportedMetricsException {
        Double value = metrics.get(sort);
        if (value != null) {
            return value.doubleValue();
        }
        throw new UnsupportedMetricsException("Cannot obtain the metric value of " + sort);
    }
    
    /**
     * Returns the value of a metric with a given sort.
     * @param sort the sort of the metric
     * @return the metric value
     */
    public double getMetricValue(String sort) {
        Double value = metrics.get(sort);
        if (value != null) {
            return value.doubleValue();
        }
        return -1.0;
    }
    
    /**
     * Stores the value of a metric with a given sort.
     * @param sort the sort of the metric
     * @param value the metric value
     */
    protected void putMetricValue(String sort, double value) {
        metrics.put(sort, new Double(value));
    }
    
    /**
     * Obtains the map storing metric values.
     * @return the metric values
     */
    public Map<String, Double> getMetricValues() {
        return metrics;
    }
    
    /**
     * Sets the code properties with respect to positions and line numbers of this element.
     * @param start the character index indicating where the code fragment for this element begins
     * @param len the length in characters of the code fragment for this element
     * @param upper the upper line number of code fragment for this element
     * @param bottom the bottom line number of code fragment for this element
     */
    protected void setCodeProperties(int start, int len, int upper, int bottom) {
    }
}
