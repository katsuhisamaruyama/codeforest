/*
 *  Copyright 2013, Katsuhisa Maruyama (maru@jtool.org)
 */

package org.jtool.codeforest.metrics;

import org.jtool.codeforest.metrics.java.ClassMetrics;
import org.jtool.codeforest.metrics.java.FieldMetrics;
import org.jtool.codeforest.metrics.java.MethodMetrics;
import org.jtool.codeforest.metrics.java.PackageMetrics;
import org.jtool.codeforest.metrics.java.ProjectMetrics;

/**
 * Measures nothing and returns always default values.
 * @author Katsuhisa Maruyama
 */
public class Default extends Metric {
    
    public static final String Name = "(default)";
    
    private static final String Description = "Default metric";
    
    /**
     * Creates an object returning a metric measurement.
     */
    public Default() {
        super(Name, Description);
    }
    
    /**
     * Returns the metric value with respect to a given project.
     * @param mproject the project to be examined
     * @return always the default value (<code>-1</code>)
     * @throws UnsupportedMetricsException is always occurred
     */
    public double getValue(ProjectMetrics mproject) throws UnsupportedMetricsException {
        return -1;
    }
    
    /**
     * Returns the total metric value with respect to a given project.
     * @param mproject the project to be examined
     * @return always the default value (<code>-1</code>)
     * @throws UnsupportedMetricsException if this metrics is not supported, that is the value is invalid
     */
    public double getTotalValue(ProjectMetrics mproject) throws UnsupportedMetricsException {
        return -1;
    }
    
    /**
     * Returns the maximum metric value with respect to a given project.
     * @param mproject the project to be examined
     * @return always the default value (<code>-1</code>)
     * @throws UnsupportedMetricsException if this metrics is not supported, that is the value is invalid
     */
    public double getMaximumValue(ProjectMetrics mproject) throws UnsupportedMetricsException {
        return -1;
    }
    
    /**
     * Test if this metric is related to a project.
     * @return always <code>true</code>
     */
    public boolean isProjectMetric() {
        return true;
    }
    
    /**
     * Returns the metric value with respect to a given package.
     * @param mpackage the package to be examined
     * @return always the default value (<code>-1</code>)
     * @throws UnsupportedMetricsException is always occurred
     */
    public double getValue(PackageMetrics mpackage) throws UnsupportedMetricsException {
        return -1;
    }
    
    /**
     * Returns the total metric value with respect to a given package.
     * @param mpackage the package to be examined
     * @return always the default value (<code>-1</code>)
     * @throws UnsupportedMetricsException if this metric is not supported, that is the value is invalid
     */
    public double getTotalValue(PackageMetrics mpackage) throws UnsupportedMetricsException {
        return -1;
    }
    
    /**
     * Returns the maximum metric value with respect to a given package.
     * @param mpackage the package to be examined
     * @return always the default value (<code>-1</code>)
     * @throws UnsupportedMetricsException if this metric is not supported, that is the value is invalid
     */
    public double getMaximumValue(PackageMetrics mpackage) throws UnsupportedMetricsException {
        return -1;
    }
    
    /**
     * Test if this metric is related to a package.
     * @return always <code>true</code>
     */
    public boolean isPackageMetric() {
        return true;
    }
    
    /**
     * Returns the metric value with respect to a given class.
     * @param mclass the class to be examined
     * @return always the default value (<code>-1</code>)
     * @throws UnsupportedMetricsException is always occurred
     */
    public double getValue(ClassMetrics mclass) throws UnsupportedMetricsException {
        return -1;
    }
    
    /**
     * Returns the total metric value with respect to a given class.
     * @param mclass the class to be examined
     * @return always the default value (<code>-1</code>)
     * @throws UnsupportedMetricsException if this metric is not supported, that is the value is invalid
     */
    public double getTotalValue(ClassMetrics mclass) throws UnsupportedMetricsException {
        return -1;
    }
    
    /**
     * Returns the maximum metric value with respect to a given class.
     * @param mclass the class to be examined
     * @return always the default value (<code>-1</code>)
     * @throws UnsupportedMetricsException if this metric is not supported, that is the value is invalid
     */
    public double getMaximumValue(ClassMetrics mclass) throws UnsupportedMetricsException {
        return -1;
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
     * @param mmethod the method to be examined
     * @return always the default value (<code>-1</code>)
     * @throws UnsupportedMetricsException is always occurred
     */
    public double getValue(MethodMetrics mmethod) throws UnsupportedMetricsException {
        return -1;
    }
    
    /**
     * Returns the total metric value with respect to a given method.
     * @param mmethod the method to be examined
     * @return always the default value (<code>-1</code>)
     * @throws UnsupportedMetricsException if this metric is not supported, that is the value is invalid
     */
    public double getTotalValue(MethodMetrics mmethod) throws UnsupportedMetricsException {
        return -1;
    }
    
    /**
     * Returns the maximum metric value with respect to a given method.
     * @param mmethod the method to be examined
     * @return always the default value (<code>-1</code>)
     * @throws UnsupportedMetricsException if this metric is not supported, that is the value is invalid
     */
    public double getMaximumValue(MethodMetrics mmethod) throws UnsupportedMetricsException {
        return -1;
    }
    
    /**
     * Test if this metric is related to a method.
     * @return always <code>true</code>
     */
    public boolean isMethodMetric() {
        return true;
    }
    
    /**
     * Returns the metric value with respect to a given field.
     * @param mfield the field to be examined
     * @return always the default value (<code>-1</code>)
     * @throws UnsupportedMetricsException is always occurred
     */
    public double getValue(FieldMetrics mfield) throws UnsupportedMetricsException {
        return -1;
    }
    
    /**
     * Returns the total metric value with respect to a given field.
     * @param mfield the field to be examined
     * @return always the default value (<code>-1</code>)
     * @throws UnsupportedMetricsException if this metric is not supported, that is the value is invalid
     */
    public double getTotalValue(FieldMetrics mfield) throws UnsupportedMetricsException {
        return -1;
    }
    
    /**
     * Returns the maximum metric value with respect to a given field.
     * @param mfield the field to be examined
     * @return always the default value (<code>-1</code>)
     * @throws UnsupportedMetricsException if this metric is not supported, that is the value is invalid
     */
    public double getMaximumValue(FieldMetrics mfield) throws UnsupportedMetricsException {
        return -1;
    }
    
    /**
     * Test if this metric is related to a field.
     * @return always <code>true</code>
     */
    public boolean isFieldMetric() {
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
     * @return always <code>false</code>
     */
    public boolean isWidthMetric() {
        return true;
    }
}