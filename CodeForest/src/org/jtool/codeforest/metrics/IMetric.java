/*
 *  Copyright 2014, Katsuhisa Maruyama (maru@jtool.org)
 */

package org.jtool.codeforest.metrics;

import org.jtool.codeforest.metrics.java.ClassMetrics;
import org.jtool.codeforest.metrics.java.FieldMetrics;
import org.jtool.codeforest.metrics.java.MethodMetrics;
import org.jtool.codeforest.metrics.java.PackageMetrics;
import org.jtool.codeforest.metrics.java.ProjectMetrics;

/**
 * An interface for metric measurements.
 * @author Katsuhisa Maruyama
 */
public interface IMetric {
    
    /**
     * Returns the name of this metric.
     * @return the metric name
     */
    public String getName();
    
    /**
     * Returns the metric value with respect to a given project.
     * @param mproject the project to be examined
     * @return the value of this metric
     * @throws UnsupportedMetricsException if this metrics is not supported, that is the value is invalid
     */
    public double getValue(ProjectMetrics mproject) throws UnsupportedMetricsException;
    
    /**
     * Returns the total metric value with respect to a given project.
     * @param mproject the project to be examined
     * @return the value of this metric
     * @throws UnsupportedMetricsException if this metrics is not supported, that is the value is invalid
     */
    public double getTotalValue(ProjectMetrics mproject) throws UnsupportedMetricsException;
    
    /**
     * Returns the maximum metric value with respect to a given project.
     * @param mproject the project to be examined
     * @return the value of this metric
     * @throws UnsupportedMetricsException if this metrics is not supported, that is the value is invalid
     */
    public double getMaximumValue(ProjectMetrics mproject) throws UnsupportedMetricsException;
    
    /**
     * Test if this metric is related to a project.
     * @return <code>true</code> if this metric is related to a project, otherwise <code>false</code>
     */
    public boolean isProjectMetric();
    
    /**
     * Returns the metric value with respect to a given package.
     * @param mpackage the package to be examined
     * @return the value of this metric
     * @throws UnsupportedMetricsException if this metric is not supported, that is the value is invalid
     */
    public double getValue(PackageMetrics mpackage) throws UnsupportedMetricsException;
    
    /**
     * Returns the total metric value with respect to a given package.
     * @param mpackage the package to be examined
     * @return the value of this metric
     * @throws UnsupportedMetricsException if this metric is not supported, that is the value is invalid
     */
    public double getTotalValue(PackageMetrics mpackage) throws UnsupportedMetricsException;
    
    /**
     * Returns the maximum metric value with respect to a given package.
     * @param mpackage the package to be examined
     * @return the value of this metric
     * @throws UnsupportedMetricsException if this metric is not supported, that is the value is invalid
     */
    public double getMaximumValue(PackageMetrics mpackage) throws UnsupportedMetricsException;
    
    /**
     * Test if this metric is related to a package.
     * @return <code>true</code> if this metric is related to a package, otherwise <code>false</code>
     */
    public boolean isPackageMetric();
    
    /**
     * Returns the metric value with respect to a given class.
     * @param mclass the class to be examined
     * @return the value of this metric
     * @throws UnsupportedMetricsException if this metric is not supported, that is the value is invalid
     */
    public double getValue(ClassMetrics mclass) throws UnsupportedMetricsException;
    
    /**
     * Returns the total metric value with respect to a given class.
     * @param mclass the class to be examined
     * @return the value of this metric
     * @throws UnsupportedMetricsException if this metric is not supported, that is the value is invalid
     */
    public double getTotalValue(ClassMetrics mclass) throws UnsupportedMetricsException;
    
    /**
     * Returns the maximum metric value with respect to a given class.
     * @param mclass the class to be examined
     * @return the value of this metric
     * @throws UnsupportedMetricsException if this metric is not supported, that is the value is invalid
     */
    public double getMaximumValue(ClassMetrics mclass) throws UnsupportedMetricsException;
    
    /**
     * Test if this metric is related to a class.
     * @return <code>true</code> if this metric is related to a class, otherwise <code>false</code>
     */
    public boolean isClassMetric();
    
    /**
     * Returns the metric value with respect to a given method.
     * @param mmethod the method to be examined
     * @return the value of this metric
     * @throws UnsupportedMetricsException if this metric is not supported, that is the value is invalid
     */
    public double getValue(MethodMetrics mmethod) throws UnsupportedMetricsException;
    
    /**
     * Returns the total metric value with respect to a given method.
     * @param mmethod the method to be examined
     * @return the value of this metric
     * @throws UnsupportedMetricsException if this metric is not supported, that is the value is invalid
     */
    public double getTotalValue(MethodMetrics mmethod) throws UnsupportedMetricsException;
    
    /**
     * Returns the maximum metric value with respect to a given method.
     * @param mmethod the method to be examined
     * @return the value of this metric
     * @throws UnsupportedMetricsException if this metric is not supported, that is the value is invalid
     */
    public double getMaximumValue(MethodMetrics mmethod) throws UnsupportedMetricsException;
    
    /**
     * Test if this metric is related to a method.
     * @return <code>true</code> if this metric is related to a method, otherwise <code>false</code>
     */
    public boolean isMethodMetric();
    
    /**
     * Returns the metric value with respect to a given field.
     * @param mfield the field to be examined
     * @return the value of this metric
     * @throws UnsupportedMetricsException if this metric is not supported, that is the value is invalid
     */
    public double getValue(FieldMetrics mfield) throws UnsupportedMetricsException;
    
    /**
     * Returns the total metric value with respect to a given field.
     * @param mfield the field to be examined
     * @return the value of this metric
     * @throws UnsupportedMetricsException if this metric is not supported, that is the value is invalid
     */
    public double getTotalValue(FieldMetrics mfield) throws UnsupportedMetricsException;
    
    /**
     * Returns the maximum metric value with respect to a given field.
     * @param mfield the field to be examined
     * @return the value of this metric
     * @throws UnsupportedMetricsException if this metric is not supported, that is the value is invalid
     */
    public double getMaximumValue(FieldMetrics mfield) throws UnsupportedMetricsException;
    
    /**
     * Test if this metric is related to a field.
     * @return <code>true</code> if this metric is related to a field, otherwise <code>false</code>
     */
    public boolean isFieldMetric();
    
    /**
     * Test if this metric is related to the height of a visual object.
     * @return <code>true</code> if this metric is related to the height, otherwise <code>false</code>
     */
    public boolean isHeightMetric();
    
    /**
     * Test if this metric is related to the width of a visual object.
     * @return <code>true</code> if this metric is related to the width, otherwise <code>false</code>
     */
    public boolean isWidthMetric();
}