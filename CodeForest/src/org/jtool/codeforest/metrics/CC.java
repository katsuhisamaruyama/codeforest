/*
 *  Copyright 2013, Katsuhisa Maruyama (maru@jtool.org)
 */

package org.jtool.codeforest.metrics;

import org.jtool.codeforest.java.metrics.ClassMetrics;
import org.jtool.codeforest.java.metrics.MethodMetrics;

/**
 * Measures the cyclomatic number for measuring complexity of this method.
 * @author Katsuhisa Maruyama
 */
public class CC extends Metric {
    
    public static final String Name = "CC";
    
    private static final String Description = "Cyclomatic complexity";
    
    /**
     * Creates an object returning a metric measurement.
     */
    public CC() {
        super(Name, Description);
    }
    
    /**
     * Returns the metric value with respect to a given method.
     * @param mmethod the class to be examined
     * @return the value of this metric
     * @throws UnsupportedMetricsException if this metric is not supported, that is the value is invalid
     */
    public double getValue(MethodMetrics mmethod) throws UnsupportedMetricsException {
        return mmethod.getMetricValueWithException(MetricSort.CYCLOMATIC_COMPLEXITY);
    }
    
    /**
     * Returns the total metric value with respect to a given method.
     * @param mmethod the method to be examined
     * @return the value of this metric
     * @throws UnsupportedMetricsException if this metric is not supported, that is the value is invalid
     */
    public double getTotalValue(MethodMetrics mmethod) throws UnsupportedMetricsException {
        ClassMetrics mclass = mmethod.getClassMetrics();
        return mclass.getMetricValueWithException(MetricSort.TOTAL_CYCLOMATIC_COMPLEXITY);
    }
    
    /**
     * Returns the maximum metric value with respect to a given method.
     * @param mmethod the method to be examined
     * @return the value of this metric
     * @throws UnsupportedMetricsException if this metric is not supported, that is the value is invalid
     */
    public double getMaximumValue(MethodMetrics mmethod) throws UnsupportedMetricsException {
        ClassMetrics mclass = mmethod.getClassMetrics();
        return mclass.getMetricValueWithException(MetricSort.MAX_CYCLOMATIC_COMPLEXITY);
    }
    
    /**
     * Test if this metric is related to a method.
     * @return always <code>true</code>
     */
    public boolean isMethodMetric() {
        return true;
    }
}
