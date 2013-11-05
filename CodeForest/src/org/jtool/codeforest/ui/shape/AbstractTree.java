/*
 *  Copyright 2013, Katsuhisa Maruyama (maru@jtool.org)
 */

package org.jtool.codeforest.ui.shape;

import org.jtool.codeforest.ui.view.forest.ForestData;
import org.jtool.codeforest.metrics.IMetric;
import org.jtool.codeforest.metrics.MetricSort;
import org.jtool.codeforest.metrics.UnsupportedMetricsException;
import org.jtool.codeforest.metrics.java.ClassMetrics;
import org.jtool.codeforest.metrics.java.ProjectMetrics;

/**
 * Represents an abstract tree which concrete trees are intended to be inherited from.
 * @author Daiki Todoroki
 * @author Katsuhisa Maruyama
 */
public abstract class AbstractTree extends AbstractShape {
    
    protected final ProjectMetrics projectMetrics;
    
    protected final ClassMetrics classMetrics;
    
    protected final ForestData forestData;
    
    protected AbstractTree(ProjectMetrics mproject, ClassMetrics mclass, ForestData fdata) {
        super();
        projectMetrics = mproject;
        classMetrics = mclass;
        forestData = fdata;
    }
    
    protected double getMetricValue(IMetric metric, ClassMetrics mclass) {
        try {
            double value = metric.getValue(mclass);
            if (value < 0) {
                return -1;
            }
            
            double cnum = projectMetrics.getMetricValue(MetricSort.NUMBER_OF_CLASSES);
            if (cnum > 1000) {
                value = value / 50;
            } else{
                value = value / 10;
            }
            
            return value;
            
        } catch (UnsupportedMetricsException e) {
            System.out.println(e.getMessage());
        }
        
        return -1;
    }
    
    protected double getMetricValuePerAverage(IMetric metric, ClassMetrics mclass) {
        try {
            double value = metric.getValue(mclass);
            if (value < 0) {
                return -1;
            }
            
            double cnum = projectMetrics.getMetricValue(MetricSort.NUMBER_OF_CLASSES);
            double average = metric.getTotalValue(mclass) / cnum;
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
    
    protected double getMetricValuePerMax(IMetric metric, ClassMetrics mclass) {
        try {
            double value = metric.getValue(mclass);
            if (value < 0) {
                return -1;
            }
            
            double max = metric.getMaximumValue(mclass);
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
