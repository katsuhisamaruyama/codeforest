/*
 *  Copyright 2014, Katsuhisa Maruyama (maru@jtool.org)
 */

package org.jtool.codeforest.metrics;

import org.jtool.codeforest.metrics.java.ClassMetrics;
import org.jtool.codeforest.metrics.java.MethodMetrics;

/**
 * Measures the number of parameters of this method.
 * @author Katsuhisa Maruyama
 */
public class NOPT extends Metric {
    
    public static final String Name = "NOPT";
    
    private static final String Description = "Number of parameters";
    
    /**
     * Creates an object returning a metric measurement.
     */
    public NOPT() {
        super(Name, Description);
    }
    
    /**
     * Returns the metric value with respect to a given method.
     * @param mmethod the class to be examined
     * @return the value of this metric
     * @throws UnsupportedMetricsException if this metric is not supported, that is the value is invalid
     */
    public double getValue(MethodMetrics mmethod) throws UnsupportedMetricsException {
        return mmethod.getMetricValueWithException(MetricSort.NUMBER_OF_PARAMETERS);
    }
    
    /**
     * Returns the total metric value with respect to a given method.
     * @param mmethod the method to be examined
     * @return the value of this metric
     * @throws UnsupportedMetricsException if this metric is not supported, that is the value is invalid
     */
    public double getTotalValue(MethodMetrics mmethod) throws UnsupportedMetricsException {
        ClassMetrics mclass = mmethod.getClassMetrics();
        return mclass.getMetricValueWithException(MetricSort.TOTAL_NUMBER_OF_PARAMETERS);
    }
    
    /**
     * Returns the maximum metric value with respect to a given method.
     * @param mmethod the method to be examined
     * @return the value of this metric
     * @throws UnsupportedMetricsException if this metric is not supported, that is the value is invalid
     */
    public double getMaximumValue(MethodMetrics mmethod) throws UnsupportedMetricsException {
        ClassMetrics mclass = mmethod.getClassMetrics();
        return mclass.getMetricValueWithException(MetricSort.MAX_NUMBER_OF_PARAMETERS);
    }
    
    /**
     * Test if this metric is related to a method.
     * @return always <code>true</code>
     */
    public boolean isMethodMetric() {
        return true;
    }
}
