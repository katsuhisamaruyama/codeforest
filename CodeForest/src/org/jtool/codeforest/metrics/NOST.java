/*
 *  Copyright 2013, Katsuhisa Maruyama (maru@jtool.org)
 */

package org.jtool.codeforest.metrics;

import org.jtool.codeforest.java.metrics.ClassMetrics;
import org.jtool.codeforest.java.metrics.MethodMetrics;
import org.jtool.codeforest.java.metrics.ProjectMetrics;

/**
 * Measures the numbers of statements for respective Java elements.
 * @author Katsuhisa Maruyama
 */
public class NOST extends Metric {
    
    public static final String Name = "NOST";
    
    private static final String Description = "Number of statements";
    
    /**
     * Creates an object returning a metric measurement.
     */
    public NOST() {
        super(Name, Description);
    }
    
    /**
     * Returns the metric value with respect to a given class.
     * @param mclass the class to be examined
     * @return the value of this metric
     * @throws UnsupportedMetricsException if this metric is not supported, that is the value is invalid
     */
    public double getValue(ClassMetrics mclass) throws UnsupportedMetricsException {
        return mclass.getMetricValueWithException(MetricSort.NUMBER_OF_STATEMENTS);
    }
    
    /**
     * Returns the total metric value with respect to a given class.
     * @param mclass the class to be examined
     * @return the value of this metric
     * @throws UnsupportedMetricsException if this metric is not supported, that is the value is invalid
     */
    public double getTotalValue(ClassMetrics mclass) throws UnsupportedMetricsException {
        ProjectMetrics mproject = mclass.getPackageMetrics().getProjectMetrics();
        return mproject.getMetricValueWithException(MetricSort.TOTAL_NUMBER_OF_STATEMENTS);
    }
    
    /**
     * Returns the maximum metric value with respect to a given class.
     * @param mclass the class to be examined
     * @return the value of this metric
     * @throws UnsupportedMetricsException if this metric is not supported, that is the value is invalid
     */
    public double getMaximumValue(ClassMetrics mclass) throws UnsupportedMetricsException {
        ProjectMetrics mproject = mclass.getPackageMetrics().getProjectMetrics();
        return mproject.getMetricValueWithException(MetricSort.MAX_NUMBER_OF_STATEMENTS);
    }
    
    /**
     * Test if this metric is related to a class.
     * @return always <code>true</code>
     */
    public boolean isClassMetric() {
        return true;
    }
    
    /**
     * Returns the metric value with respect to a given method.
     * @param mmethod the class to be examined
     * @return the value of this metric
     * @throws UnsupportedMetricsException if this metric is not supported, that is the value is invalid
     */
    public double getValue(MethodMetrics mmethod) throws UnsupportedMetricsException {
        return mmethod.getMetricValueWithException(MetricSort.NUMBER_OF_STATEMENTS);
    }
    
    /**
     * Returns the total metric value with respect to a given method.
     * @param mmethod the method to be examined
     * @return the value of this metric
     * @throws UnsupportedMetricsException if this metric is not supported, that is the value is invalid
     */
    public double getTotalValue(MethodMetrics mmethod) throws UnsupportedMetricsException {
        ClassMetrics mclass = mmethod.getClassMetrics();
        return mclass.getMetricValueWithException(MetricSort.TOTAL_NUMBER_OF_STATEMENTS);
    }
    
    /**
     * Returns the maximum metric value with respect to a given method.
     * @param mmethod the method to be examined
     * @return the value of this metric
     * @throws UnsupportedMetricsException if this metric is not supported, that is the value is invalid
     */
    public double getMaximumValue(MethodMetrics mmethod) throws UnsupportedMetricsException {
        ClassMetrics mclass = mmethod.getClassMetrics();
        return mclass.getMetricValueWithException(MetricSort.MAX_NUMBER_OF_STATEMENTS);
    }
    
    /**
     * Test if this metric is related to a method.
     * @return always <code>true</code>
     */
    public boolean isMethodMetric() {
        return true;
    }
    
    /**
     * Test if this metric is related to the height of a visual object.
     * @return always <code>true</code>
     */
    public boolean isHeightMetric() {
        return true;
    }
}
