/*
 *  Copyright 2014, Katsuhisa Maruyama (maru@jtool.org)
 */

package org.jtool.codeforest.ui.shape;

import org.jtool.codeforest.metrics.IMetric;
import org.jtool.codeforest.metrics.MetricSort;
import org.jtool.codeforest.metrics.UnsupportedMetricsException;
import org.jtool.codeforest.metrics.java.MethodMetrics;

/**
 * Represents a leaf with metrics values.
 * @author Katsuhisa Maruyama
 */
public abstract class MetricsLeaf {
    
    /**
     * The metrics of a method represented by this leaf.
     */
    protected final MethodMetrics methodMetrics;
    
    /**
     * Creates a leaf with metric values.
     * @param mclass the metrics of a method represented by this leaf
     */
    protected MetricsLeaf(MethodMetrics mmethod) {
        methodMetrics = mmethod;
    }
    
    /**
     * Returns the metrics of a method represented by this leaf.
     * @return the method metrics
     */
    public MethodMetrics getMethodMetrics() {
        return methodMetrics;
    }
    
    /**
     * Obtains the value of a specified metric.
     * @param metric the metric whose value will be obtained
     * @return the metric value
     */
    protected double getMetricValue(IMetric metric) {
        try {
            double value = metric.getValue(methodMetrics);
            if (value < 0) {
                return -1;
            }
            
            return value;
            
        } catch (UnsupportedMetricsException e) {
            System.out.println(e.getMessage());
        }
        
        return -1;
    }
    
    /**
     * Obtains the value of a specified metric.
     * @param metric the metric whose value will be obtained
     * @return the metric value per the average of all the values
     */
    protected double getMetricValuePerAverage(IMetric metric) {
        try {
            double value = metric.getValue(methodMetrics);
            if (value < 0) {
                return -1;
            }
            
            double cnum = methodMetrics.getClassMetrics().getMetricValue(MetricSort.NUMBER_OF_CLASSES);
            double average = metric.getTotalValue(methodMetrics) / cnum;
            if (average != 0) {
                value = value / average / 1;
            } else {
                value = 0;
            }
            
            return value;
            
        } catch (UnsupportedMetricsException e) {
            System.out.println(e.getMessage());
        }
        
        return -1;
    }
    
    /**
     * Obtains the value of a specified metric.
     * @param metric the metric whose value will be obtained
     * @return the metric value per the maximum of all the values
     */
    protected double getMetricValuePerMax(IMetric metric) {
        try {
            double value = metric.getValue(methodMetrics);
            if (value < 0) {
                return -1;
            }
            
            double max = metric.getMaximumValue(methodMetrics);
            if (max != 0) {
                value = value / max;
            } else {
                value = 0;
            }
            
            return value;
            
        } catch (UnsupportedMetricsException e) {
            System.out.println(e.getMessage());
        }
        
        return -1;
    }
    
    /**
     * Adjusts the metric value.
     * @param value the real value
     * @return the value after adjustment
     */
    protected double adjust(double value) {
        if (value < 0.1) {
            value = 0.1;
        } else if (value > 10) {
            value = 10;
        }
        return value;
    }
}
