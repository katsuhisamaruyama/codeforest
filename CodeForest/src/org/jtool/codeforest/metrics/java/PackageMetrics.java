/*
 *  Copyright 2013, Katsuhisa Maruyama (maru@jtool.org)
 */

package org.jtool.codeforest.metrics.java;

import org.jtool.codeforest.metrics.MetricSort;
import org.jtool.codeforest.metrics.UnsupportedMetricsException;
import org.jtool.eclipse.model.java.JavaPackage;
import org.jtool.eclipse.model.java.JavaClass;

import java.util.HashSet;
import java.util.Set;

/**
 * An object storing information on a package.
 * @author Katsuhisa Maruyama
 */
public class PackageMetrics extends CommonMetrics {
    
    /**
     * The identifier that represents package metrics
     */
    public static final String Id = "PackageMetrics";
    
    /**
     * An object representing a package.
     */
    private JavaPackage jpackage;
    
    /**
     * A metrics object for a project containing this package.
     */
    private ProjectMetrics projectMetrics;
    
    /**
     * The collection of all class metrics for this package.
     */
    protected Set<ClassMetrics> classMetrics = new HashSet<ClassMetrics>();
    
    /**
     * The collection of names of afferent packages for this package.
     */
    protected Set<String> afferentPackageNames = new HashSet<String>();
    
    /**
     * The collection of names of afferent packages for this package.
     */
    protected Set<String> efferentPackageNames = new HashSet<String>();
    
    /**
     * Creates a new object representing a package.
     * @param name the name of the package
     * @param pm a metrics object for a project containing the package
     */
    protected PackageMetrics(String name, ProjectMetrics pm) {
        super();
        
        jpackage = JavaPackage.create(name, pm.getJavaProject());
        this.projectMetrics = pm;
    }
    
    /**
     * Creates a new object representing a package.
     * @param node an AST node for this package
     * @param pm a metrics object for a project containing this package
     */
    protected PackageMetrics(JavaPackage jpackage, ProjectMetrics pm) {
        super();
        
        this.jpackage = jpackage;
        this.projectMetrics = pm;
        
        for (JavaClass jc : jpackage.getJavaClasses()) {
            ClassMetrics cm = new ClassMetrics(jc, this);
            classMetrics.add(cm);
        }
        
        for (JavaPackage jp : jpackage.getAfferentJavaPackages()) {
            afferentPackageNames.add(jp.getName());
        }
        for (JavaPackage jp : jpackage.getEfferentJavaPackages()) {
            efferentPackageNames.add(jp.getName());
        }
        
        collectMetricInfo();
    }
    
    /**
     * Returns the metrics object for a project containing this package.
     * @return the project metrics
     */
    public ProjectMetrics getProjectMetrics() {
        return projectMetrics;
    }
    
    /**
     * Obtains the collection of class metrics for this package.
     * @return the collection of class metrics
     */
    public Set<ClassMetrics> getClassMetrics() {
        return classMetrics;
    }
    
    /**
     * Stores a class metrics object.
     * @param pm the class metrics
     */
    protected void add(ClassMetrics cm) {
        classMetrics.add(cm);
    }
    
    /**
     * Returns the object representing this package.
     * @return the package object
     */
    public JavaPackage getJavaPackage() {
        return jpackage;
    }
    
    /**
     * Returns the name of this package.
     * @return the name
     */
    public String getName() {
        return jpackage.getName();
    }
    
    /**
     * Adds the name of an afferent package for this package.
     * @param name the afferent package name
     */
    public void addAfferentPackageName(String name) {
        afferentPackageNames.add(name);
    }
    
    /**
     * Returns the names of afferent packages for this package.
     * @return the collection of the afferent package names
     */
    public Set<String> getAfferentPackageNames() {
        return afferentPackageNames;
    }
    
    /**
     * Adds the name of an efferent package for this package.
     * @param name the efferent package name
     */
    public void addEfferentPackageName(String name) {
        efferentPackageNames.add(name);
    }
    
    /**
     * Returns the names of efferent packages for this package.
     * @return the collection of the efferent package names
     */
    public Set<String> getEfferentPackageNames() {
        return efferentPackageNames;
    }
    
    /**
     * Collects information about this package.
     * @return the string for printing
     */
    public String toString() {
        return getName();
    }
    
    /**
     * Collects information on this package.
     */
    private void collectMetricInfo() {
        metrics.put(MetricSort.NUMBER_OF_CLASSES, new Double(jpackage.getJavaClasses().size()));
        metrics.put(MetricSort.NUMBER_OF_AFFERENT_PACKAGES, new Double(jpackage.getAfferentJavaPackages().size()));
        metrics.put(MetricSort.NUMBER_OF_EFFERENT_PACKAGES, new Double(jpackage.getEfferentJavaPackages().size()));
        
        try {
            metrics.put(MetricSort.NUMBER_OF_METHODS, sum(MetricSort.NUMBER_OF_METHODS));
            metrics.put(MetricSort.NUMBER_OF_FIELDS, sum(MetricSort.NUMBER_OF_FIELDS));
            metrics.put(MetricSort.NUMBER_OF_METHODS_AND_FIELDS, sum(MetricSort.NUMBER_OF_METHODS_AND_FIELDS));
            metrics.put(MetricSort.LINES_OF_CODE, sum(MetricSort.LINES_OF_CODE));
            metrics.put(MetricSort.NUMBER_OF_STATEMENTS, sum(MetricSort.NUMBER_OF_STATEMENTS));
        } catch (UnsupportedMetricsException e) {
            System.out.println(e.getMessage() + " in the package: " + getName());
        }
    }
    
    /**
     * Obtains the sum of values with respect to a metric with a given name.
     * @param sort the metric name
     * @return the sum of the values
     * @throws UnsupportedMetricsException if the metric value is not obtained
     */
    private Double sum(String sort) throws UnsupportedMetricsException {
        double value = 0;
        for (ClassMetrics cm : classMetrics) {
            value = value + cm.getMetricValueWithException(sort);
        }
        return new Double(value);
    }
}
