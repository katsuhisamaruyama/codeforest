/*
 *  Copyright 2014, Katsuhisa Maruyama (maru@jtool.org)
 */

package org.jtool.codeforest.metrics;

import org.jtool.codeforest.metrics.java.ClassMetrics;
import org.jtool.codeforest.metrics.java.ProjectMetrics;

/**
 * Measures the numbers of fields within respective Java elements.
 * @author Katsuhisa Maruyama
 */
public class NOFD extends Metric {
    
    public static final String Name = "NOFD";
    
    private static final String Description = "Number of fields";
    
    /**
     * Creates an object returning a metric measurement.
     */
    public NOFD() {
        super(Name, Description);
    }
    
    /**
     * Returns the metric value with respect to a given class.
     * @param mclass the class to be examined
     * @return the value of this metric
     * @throws UnsupportedMetricsException if this metric is not supported, that is the value is invalid
     */
    public double getValue(ClassMetrics mclass) throws UnsupportedMetricsException {
        return mclass.getMetricValueWithException(MetricSort.NUMBER_OF_FIELDS);
    }
    
    /**
     * Returns the total metric value with respect to a given class.
     * @param mclass the class to be examined
     * @return the value of this metric
     * @throws UnsupportedMetricsException if this metric is not supported, that is the value is invalid
     */
    public double getTotalValue(ClassMetrics mclass) throws UnsupportedMetricsException {
        ProjectMetrics mproject = mclass.getPackageMetrics().getProjectMetrics();
        return mproject.getMetricValueWithException(MetricSort.TOTAL_NUMBER_OF_FIELDS);
    }
    
    /**
     * Returns the maximum metric value with respect to a given class.
     * @param mclass the class to be examined
     * @return the value of this metric
     * @throws UnsupportedMetricsException if this metric is not supported, that is the value is invalid
     */
    public double getMaximumValue(ClassMetrics mclass) throws UnsupportedMetricsException {
        ProjectMetrics mproject = mclass.getPackageMetrics().getProjectMetrics();
        return mproject.getMetricValueWithException(MetricSort.MAX_NUMBER_OF_FIELDS);
    }
    
    /**
     * Test if this metric is related to a class.
     * @return always <code>true</code>
     */
    public boolean isClassMetric() {
        return true;
    }
    
    /**
     * Test if this metric is related to the height of a visual object.
     * @return always <code>true</code>
     */
    public boolean isHeightMetric() {
        return true;
    }
    
    /**
     * Test if this metric is related to the width of a visual object.
     * @return always <code>true</code>
     */
    public boolean isWidthMetric() {
        return true;
    }
}
