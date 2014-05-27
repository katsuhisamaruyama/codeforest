/*
 *  Copyright 2014, Katsuhisa Maruyama (maru@jtool.org)
 */

package org.jtool.codeforest.ui.shape;

import org.jtool.codeforest.metrics.IMetric;
import org.jtool.codeforest.metrics.MetricSort;
import org.jtool.codeforest.metrics.UnsupportedMetricsException;
import org.jtool.codeforest.metrics.java.ClassMetrics;
import org.jtool.codeforest.metrics.java.ProjectMetrics;

/**
 * Represents a tree with metrics values.
 * @author Katsuhisa Maruyama
 */
public abstract class MetricsTree extends AbstractShape {
    
    /**
     * The metrics of a class represented by this tree.
     */
    protected final ClassMetrics classMetrics;
    
    /**
     * Creates a tree with metric values.
     * @param mclass the metrics of a class represented by this tree
     */
    protected MetricsTree(ClassMetrics mclass) {
        super();
        classMetrics = mclass;
    }
    
    /**
     * Returns the metrics of a class represented by this tree.
     * @return the class metrics
     */
    public ClassMetrics getClassMetrics() {
        return classMetrics;
    }
    
    /**
     * Obtains the value of a specified metric.
     * @param metric the metric whose value will be obtained
     * @return the metric value
     */
    protected double getMetricValue(IMetric metric) {
        try {
            double value = metric.getValue(classMetrics);
            if (value < 0) {
                return -1;
            }
            
            /*
            double cnum = projectMetrics.getMetricValue(MetricSort.NUMBER_OF_CLASSES);
            if (cnum > 1000) {
                value = value / 50;
            } else{
                value = value / 10;
            }
            */
            
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
            double value = metric.getValue(classMetrics);
            if (value < 0) {
                return -1;
            }
            
            ProjectMetrics mproject = classMetrics.getPackageMetrics().getProjectMetrics();
            double cnum = mproject.getMetricValue(MetricSort.NUMBER_OF_CLASSES);
            double average = metric.getTotalValue(classMetrics) / cnum;
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
            double value = metric.getValue(classMetrics);
            if (value < 0) {
                return -1;
            }
            
            double max = metric.getMaximumValue(classMetrics);
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
