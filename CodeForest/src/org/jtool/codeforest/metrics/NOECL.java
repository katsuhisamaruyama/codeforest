/*
 *  Copyright 2013, Katsuhisa Maruyama (maru@jtool.org)
 */

package org.jtool.codeforest.metrics;

import org.jtool.codeforest.java.metrics.ClassMetrics;
import org.jtool.codeforest.java.metrics.ProjectMetrics;

/**
 * Measures the number of efferent classes for this class.
 * @author Katsuhisa Maruyama
 */
public class NOECL extends Metric {
    
    public static final String Name = "NOECL";
    
    private static final String Description = "Number of efferent classes";
    
    /**
     * Creates an object returning a metric measurement.
     */
    public NOECL() {
        super(Name, Description);
    }
    
    /**
     * Returns the metric value with respect to a given class.
     * @param mclass the class to be examined
     * @return the value of this metric
     * @throws UnsupportedMetricsException if this metric is not supported, that is the value is invalid
     */
    public double getValue(ClassMetrics mclass) throws UnsupportedMetricsException {
        return mclass.getMetricValueWithException(MetricSort.NUMBER_OF_EFFERENT_CLASSES);
    }
    
    /**
     * Returns the total metric value with respect to a given class.
     * @param mclass the class to be examined
     * @return the value of this metric
     * @throws UnsupportedMetricsException if this metric is not supported, that is the value is invalid
     */
    public double getTotalValue(ClassMetrics mclass) throws UnsupportedMetricsException {
        ProjectMetrics mproject = mclass.getPackageMetrics().getProjectMetrics();
        return mproject.getMetricValueWithException(MetricSort.TOTAL_NUMBER_OF_EFFERENT_CLASSES);
    }
    
    /**
     * Returns the maximum metric value with respect to a given class.
     * @param mclass the class to be examined
     * @return the value of this metric
     * @throws UnsupportedMetricsException if this metric is not supported, that is the value is invalid
     */
    public double getMaximumValue(ClassMetrics mclass) throws UnsupportedMetricsException {
        ProjectMetrics mproject = mclass.getPackageMetrics().getProjectMetrics();
        return mproject.getMetricValueWithException(MetricSort.MAX_NUMBER_OF_EFFERENT_CLASSES);
    }
    
    /**
     * Test if this metric is related to a class.
     * @return always <code>true</code>
     */
    public boolean isClassMetric() {
        return true;
    }
}
