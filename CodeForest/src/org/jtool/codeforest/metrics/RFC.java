/*
 *  Copyright 2014, Katsuhisa Maruyama (maru@jtool.org)
 */

package org.jtool.codeforest.metrics;

import org.jtool.codeforest.metrics.java.ClassMetrics;
import org.jtool.codeforest.metrics.java.ProjectMetrics;

/**
 * Measures the number of methods of a class plus the number of methods called by any method.
 * @author Katsuhisa Maruyama
 */
public class RFC extends Metric {
    
    public static final String Name = "RFC";
    
    private static final String Description = "Response for classes";
    
    /**
     * Creates an object returning a metric measurement.
     */
    public RFC() {
        super(Name, Description);
    }
    
    /**
     * Returns the metric value with respect to a given class.
     * @param mclass the class to be examined
     * @return the value of this metric
     * @throws UnsupportedMetricsException if this metric is not supported, that is the value is invalid
     */
    public double getValue(ClassMetrics mclass) throws UnsupportedMetricsException {
        return mclass.getMetricValueWithException(MetricSort.RESPONSE_FOR_CLASS);
    }
    
    /**
     * Returns the total metric value with respect to a given class.
     * @param mclass the class to be examined
     * @return the value of this metric
     * @throws UnsupportedMetricsException if this metric is not supported, that is the value is invalid
     */
    public double getTotalValue(ClassMetrics mclass) throws UnsupportedMetricsException {
        ProjectMetrics mproject = mclass.getPackageMetrics().getProjectMetrics();
        return mproject.getMetricValueWithException(MetricSort.TOTAL_RESPONSE_FOR_CLASS);
    }
    
    /**
     * Returns the maximum metric value with respect to a given class.
     * @param mclass the class to be examined
     * @return the value of this metric
     * @throws UnsupportedMetricsException if this metric is not supported, that is the value is invalid
     */
    public double getMaximumValue(ClassMetrics mclass) throws UnsupportedMetricsException {
        ProjectMetrics mproject = mclass.getPackageMetrics().getProjectMetrics();
        return mproject.getMetricValueWithException(MetricSort.MAX_RESPONSE_FOR_CLASS);
    }
    /**
     * Test if this metric is related to a class.
     * @return always <code>true</code>
     */
    public boolean isClassMetric() {
        return true;
    }
}
