/*
 *  Copyright 2013, Katsuhisa Maruyama (maru@jtool.org)
 */

package org.jtool.codeforest.metrics;

import org.jtool.codeforest.java.metrics.ClassMetrics;
import org.jtool.codeforest.java.metrics.ProjectMetrics;

/**
 * Measures the dissimilarity of methods in a class via instanced variables.
 * @author Katsuhisa Maruyama
 */
public class LCOM extends Metric {
    
    public static final String Name = "LCOM";
    
    private static final String Description = "Lack of cohesion methods";
    
    /**
     * Creates an object returning a metric measurement.
     */
    public LCOM() {
        super(Name, Description);
    }
    
    /**
     * Returns the metric value with respect to a given class.
     * @param mclass the class to be examined
     * @return the value of this metric
     * @throws UnsupportedMetricsException if this metric is not supported, that is the value is invalid
     */
    public double getValue(ClassMetrics mclass) throws UnsupportedMetricsException {
        return mclass.getMetricValueWithException(MetricSort.LACK_OF_COHESION_OF_METHODS);
    }
    
    /**
     * Returns the total metric value with respect to a given class.
     * @param mclass the class to be examined
     * @return the value of this metric
     * @throws UnsupportedMetricsException if this metric is not supported, that is the value is invalid
     */
    public double getTotalValue(ClassMetrics mclass) throws UnsupportedMetricsException {
        ProjectMetrics mproject = mclass.getPackageMetrics().getProjectMetrics();
        return mproject.getMetricValueWithException(MetricSort.TOTAL_LACK_OF_COHESION_OF_METHODS);
    }
    
    /**
     * Returns the maximum metric value with respect to a given class.
     * @param mclass the class to be examined
     * @return the value of this metric
     * @throws UnsupportedMetricsException if this metric is not supported, that is the value is invalid
     */
    public double getMaximumValue(ClassMetrics mclass) throws UnsupportedMetricsException {
        ProjectMetrics mproject = mclass.getPackageMetrics().getProjectMetrics();
        return mproject.getMetricValueWithException(MetricSort.MAX_LACK_OF_COHESION_OF_METHODS);
    }
    
    /**
     * Test if this metric is related to a class.
     * @return always <code>true</code>
     */
    public boolean isClassMetric() {
        return true;
    }
}
