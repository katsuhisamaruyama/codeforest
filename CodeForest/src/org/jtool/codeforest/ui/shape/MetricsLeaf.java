/*
 *  Copyright 2014, Katsuhisa Maruyama (maru@jtool.org)
 */

package org.jtool.codeforest.ui.shape;

import org.jtool.codeforest.metrics.IMetric;
import org.jtool.codeforest.metrics.MetricSort;
import org.jtool.codeforest.metrics.UnsupportedMetricsException;
import org.jtool.codeforest.metrics.java.ClassMetrics;
import org.jtool.codeforest.metrics.java.MethodMetrics;

/**
 * Represents a leaf with metrics values.
 * @author Katsuhisa Maruyama
 */
public abstract class MetricsLeaf {
    
    protected final MethodMetrics methodMetrics;
    
    protected MetricsLeaf(MethodMetrics mmethod) {
        methodMetrics = mmethod;
    }
    
    public MethodMetrics getMethodMetrics() {
        return methodMetrics;
    }
    
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
    
    protected double adjust(double value) {
        if (value < 0.1) {
            value = 0.1;
        } else if (value > 10) {
            value = 10;
        }
        return value;
    }
}
