/*
 *  Copyright 2013, Katsuhisa Maruyama (maru@jtool.org)
 */

package org.jtool.codeforest.metrics;

import org.jtool.codeforest.metrics.java.ClassMetrics;
import org.jtool.codeforest.metrics.java.MethodMetrics;

/**
 * Measures the maximum number of nesting within this method.
 * @author Katsuhisa Maruyama
 */
public class MNON extends Metric {
    
    public static final String Name = "MNON";
    
    private static final String Description = "Maximum number of nesting";
    
    /**
     * Creates an object returning a metric measurement.
     */
    public MNON() {
        super(Name, Description);
    }
    
    /**
     * Returns the metric value with respect to a given method.
     * @param mmethod the class to be examined
     * @return the value of this metric
     * @throws UnsupportedMetricsException if this metric is not supported, that is the value is invalid
     */
    public double getValue(MethodMetrics mmethod) throws UnsupportedMetricsException {
        return mmethod.getMetricValueWithException(MetricSort.MAX_NUMBER_OF_NESTING);
    }
    
    /**
     * Returns the total metric value with respect to a given method.
     * @param mmethod the method to be examined
     * @return the value of this metric
     * @throws UnsupportedMetricsException if this metric is not supported, that is the value is invalid
     */
    public double getTotalValue(MethodMetrics mmethod) throws UnsupportedMetricsException {
        ClassMetrics mclass = mmethod.getClassMetrics();
        return mclass.getMetricValueWithException(MetricSort.TOTAL_MAX_NUMBER_OF_NESTING);
    }
    
    /**
     * Returns the maximum metric value with respect to a given method.
     * @param mmethod the method to be examined
     * @return the value of this metric
     * @throws UnsupportedMetricsException if this metric is not supported, that is the value is invalid
     */
    public double getMaximumValue(MethodMetrics mmethod) throws UnsupportedMetricsException {
        ClassMetrics mclass = mmethod.getClassMetrics();
        return mclass.getMetricValueWithException(MetricSort.MAX_MAX_NUMBER_OF_NESTING);
    }
    
    /**
     * Test if this metric is related to a method.
     * @return always <code>true</code>
     */
    public boolean isMethodMetric() {
        return true;
    }
}
