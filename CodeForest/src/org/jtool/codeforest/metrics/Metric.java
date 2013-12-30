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
 * A default implementation for a metric measurement.
 * @author Katsuhisa Maruyama
 */
public class Metric implements IMetric {
    
    /**
     * The name of this metric.
     */
    private String name = "";
    
    /**
     * The description of this metric.
     */
    private String description = "";
    
    /**
     * Creates an object returning a metric measurement.
     */
    public Metric() {
    }
    
    /**
     * Creates an object returning a metric measurement.
     * @param name the name of the metric
     * @param description the description of the metric
     */
    protected Metric(String name, String description) {
        this.name = name;
        this.description = description;
    }
    
    /**
     * Returns the name of this metric.
     * @return the metric name
     */
    public String getName() {
        return name;
    }
    
    /**
     * Returns the description of this metric
     * @return the metric description
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * Returns the metric value with respect to a given project.
     * @param mproject the project to be examined
     * @return none because the exception always occurs
     * @throws UnsupportedMetricsException is always occurred
     */
    public double getValue(ProjectMetrics mproject) throws UnsupportedMetricsException {
        throw new UnsupportedMetricsException(this.getDescription() + " for a project " + mproject.getName());
    }
    
    /**
     * Returns the total metric value with respect to a given project.
     * @param mproject the project to be examined
     * @return none because the exception always occurs
     * @throws UnsupportedMetricsException if this metrics is not supported, that is the value is invalid
     */
    public double getTotalValue(ProjectMetrics mproject) throws UnsupportedMetricsException {
        throw new UnsupportedMetricsException(this.getDescription() + " for a project " + mproject.getName());
    }
    
    /**
     * Returns the maximum metric value with respect to a given project.
     * @param mproject the project to be examined
     * @return none because the exception always occurs
     * @throws UnsupportedMetricsException if this metrics is not supported, that is the value is invalid
     */
    public double getMaximumValue(ProjectMetrics mproject) throws UnsupportedMetricsException {
        throw new UnsupportedMetricsException(this.getDescription() + " for a project " + mproject.getName());
    }
    
    /**
     * Test if this metric is related to a project.
     * @return always <code>false</code>
     */
    public boolean isProjectMetric() {
        return false;
    }
    
    /**
     * Returns the metric value with respect to a given package.
     * @param mpackage the package to be examined
     * @return none because the exception always occurs
     * @throws UnsupportedMetricsException is always occurred
     */
    public double getValue(PackageMetrics mpackage) throws UnsupportedMetricsException {
        throw new UnsupportedMetricsException(this.getDescription() + " for a package " + mpackage.getName());
    }
    
    /**
     * Returns the total metric value with respect to a given package.
     * @param mpackage the package to be examined
     * @return none because the exception always occurs
     * @throws UnsupportedMetricsException if this metric is not supported, that is the value is invalid
     */
    public double getTotalValue(PackageMetrics mpackage) throws UnsupportedMetricsException {
        throw new UnsupportedMetricsException(this.getDescription() + " for a package " + mpackage.getName());
    }
    
    /**
     * Returns the maximum metric value with respect to a given package.
     * @param mpackage the package to be examined
     * @return none because the exception always occurs
     * @throws UnsupportedMetricsException if this metric is not supported, that is the value is invalid
     */
    public double getMaximumValue(PackageMetrics mpackage) throws UnsupportedMetricsException {
        throw new UnsupportedMetricsException(this.getDescription() + " for a package " + mpackage.getName());
    }
    
    /**
     * Test if this metric is related to a package.
     * @return always <code>false</code>
     */
    public boolean isPackageMetric() {
        return false;
    }
    
    /**
     * Returns the metric value with respect to a given class.
     * @param mclass the class to be examined
     * @return none because the exception always occurs
     * @throws UnsupportedMetricsException is always occurred
     */
    public double getValue(ClassMetrics mclass) throws UnsupportedMetricsException {
        throw new UnsupportedMetricsException(this.getDescription() + " for a class " + mclass.getQualifiedName());
    }
    
    /**
     * Returns the total metric value with respect to a given class.
     * @param mclass the class to be examined
     * @return none because the exception always occurs
     * @throws UnsupportedMetricsException if this metric is not supported, that is the value is invalid
     */
    public double getTotalValue(ClassMetrics mclass) throws UnsupportedMetricsException {
        throw new UnsupportedMetricsException(this.getDescription() + " for a class " + mclass.getQualifiedName());
    }
    
    /**
     * Returns the maximum metric value with respect to a given class.
     * @param mclass the class to be examined
     * @return none because the exception always occurs
     * @throws UnsupportedMetricsException if this metric is not supported, that is the value is invalid
     */
    public double getMaximumValue(ClassMetrics mclass) throws UnsupportedMetricsException {
        throw new UnsupportedMetricsException(this.getDescription() + " for a class " + mclass.getQualifiedName());
    }
    
    /**
     * Test if this metric is related to a class.
     * @return always <code>false</code>
     */
    public boolean isClassMetric() {
        return false;
    }
    
    /**
     * Returns the metric value with respect to a given method.
     * @param mmethod the method to be examined
     * @return none because the exception always occurs
     * @throws UnsupportedMetricsException is always occurred
     */
    public double getValue(MethodMetrics mmethod) throws UnsupportedMetricsException {
        throw new UnsupportedMetricsException(this.getDescription() + " for a method " + mmethod.getQualifiedName());
    }
    
    /**
     * Returns the total metric value with respect to a given method.
     * @param mmethod the method to be examined
     * @return none because the exception always occurs
     * @throws UnsupportedMetricsException if this metric is not supported, that is the value is invalid
     */
    public double getTotalValue(MethodMetrics mmethod) throws UnsupportedMetricsException {
        throw new UnsupportedMetricsException(this.getDescription() + " for a method " + mmethod.getQualifiedName());
    }
    
    /**
     * Returns the maximum metric value with respect to a given method.
     * @param mmethod the method to be examined
     * @return none because the exception always occurs
     * @throws UnsupportedMetricsException if this metric is not supported, that is the value is invalid
     */
    public double getMaximumValue(MethodMetrics mmethod) throws UnsupportedMetricsException {
        throw new UnsupportedMetricsException(this.getDescription() + " for a method " + mmethod.getQualifiedName());
    }
    
    /**
     * Test if this metric is related to a method.
     * @return always <code>false</code>
     */
    public boolean isMethodMetric() {
        return false;
    }
    
    /**
     * Returns the metric value with respect to a given field.
     * @param mfield the field to be examined
     * @return none because the exception always occurs
     * @throws UnsupportedMetricsException is always occurred
     */
    public double getValue(FieldMetrics mfield) throws UnsupportedMetricsException {
        throw new UnsupportedMetricsException(this.getDescription() + " for a field " + mfield.getQualifiedName());
    }
    
    /**
     * Returns the total metric value with respect to a given field.
     * @param mfield the field to be examined
     * @return none because the exception always occurs
     * @throws UnsupportedMetricsException if this metric is not supported, that is the value is invalid
     */
    public double getTotalValue(FieldMetrics mfield) throws UnsupportedMetricsException {
        throw new UnsupportedMetricsException(this.getDescription() + " for a field " + mfield.getQualifiedName());
    }
    
    /**
     * Returns the maximum metric value with respect to a given field.
     * @param mfield the field to be examined
     * @return none because the exception always occurs
     * @throws UnsupportedMetricsException if this metric is not supported, that is the value is invalid
     */
    public double getMaximumValue(FieldMetrics mfield) throws UnsupportedMetricsException {
        throw new UnsupportedMetricsException(this.getDescription() + " for a field " + mfield.getQualifiedName());
    }
    
    /**
     * Test if this metric is related to a field.
     * @return always <code>false</code>
     */
    public boolean isFieldMetric() {
        return false;
    }
    
    /**
     * Test if this metric is related to the height of a visual object.
     * @return always <code>false</code>
     */
    public boolean isHeightMetric() {
        return false;
    }
    
    /**
     * Test if this metric is related to the width of a visual object.
     * @return always <code>false</code>
     */
    public boolean isWidthMetric() {
        return false;
    }
}