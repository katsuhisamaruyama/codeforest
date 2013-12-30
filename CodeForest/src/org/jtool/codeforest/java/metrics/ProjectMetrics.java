/*
 *  Copyright 2013, Katsuhisa Maruyama (maru@jtool.org)
 */

package org.jtool.codeforest.java.metrics;

import org.jtool.codeforest.java.JavaPackage;
import org.jtool.codeforest.java.JavaProject;
import org.jtool.codeforest.metrics.MetricSort;
import org.jtool.codeforest.metrics.UnsupportedMetricsException;
import java.util.HashSet;
import java.util.Set;

/**
 * An object storing information on a project.
 * @author Katsuhisa Maruyama
 */
public class ProjectMetrics extends CommonMetrics {
    
    /**
     * The identifier that represents project metrics
     */
    public static final String Id = "ProjectMetrics";
    
    /**
     * An object representing a project.
     */
    private JavaProject jproject;
    
    /**
     * The collection of all package metrics for this project.
     */
    protected Set<PackageMetrics> packageMetrics = new HashSet<PackageMetrics>();
    
    /**
     * Creates a new object storing project metrics.
     * @param name the name of the project
     * @param path the top directory of the project
     */
    protected ProjectMetrics(String name, String path) {
        super();
        
        jproject = JavaProject.create(name, path);
    }
    
    /**
     * Creates a new object storing project metrics.
     * @param node an AST node for this project
     */
    protected ProjectMetrics(JavaProject jproject) {
        super();
        
        this.jproject = jproject;
        
        for (JavaPackage jp : jproject.getJavaPackages()) {
            PackageMetrics pm = new PackageMetrics(jp, this);
            packageMetrics.add(pm);
        }
        
        collectMetricInfo();
        collectMetricInfoForTotal();
        collectMetricInfoForMax();
    }
    
    /**
     * Returns the object representing this project.
     * @return the project object
     */
    public JavaProject getJavaProject() {
        return jproject;
    }
    
    /**
     * Obtains the collection of all package metrics for this project.
     * @return the collection of the package metrics
     */
    public Set<PackageMetrics> getPackageMetrics() {
        return packageMetrics;
    }
    
    /**
     * Stores a package metrics object.
     * @param pm the package metrics
     */
    protected void add(PackageMetrics pm) {
        packageMetrics.add(pm);
    }
    
    /**
     * Obtains the collection of all class metrics for this project.
     * @return the collection of the class metrics
     */
    protected Set<ClassMetrics> getClassMetrics() {
        Set<ClassMetrics> classes = new HashSet<ClassMetrics>();
        for (PackageMetrics pm : packageMetrics) {
            classes.addAll(pm.getClassMetrics());
        }
        return classes;
    }
    
    /**
     * Returns the name of this project.
     * @return the name
     */
    public String getName() {
        return jproject.getName();
    }
    
    /**
     * Sets the path of this project.
     * @return the path
     */
    public String getPath() {
        return jproject.getTopDir();
    }
    
    /**
     * Collects information about this project.
     * @return the string for printing
     */
    public String toString() {
        return getPath();
    }
    
    /**
     * Collects information on this project.
     */
    private void collectMetricInfo() {
        metrics.put(MetricSort.NUMBER_OF_FILES, new Double(jproject.getJavaFiles().size()));
        metrics.put(MetricSort.NUMBER_OF_PACKAGES, new Double(jproject.getJavaPackages().size()));
        metrics.put(MetricSort.NUMBER_OF_CLASSES, new Double(jproject.getJavaClasses().size()));
        
        try {
            metrics.put(MetricSort.LINES_OF_CODE, sum(MetricSort.LINES_OF_CODE));
            metrics.put(MetricSort.NUMBER_OF_METHODS, sum(MetricSort.NUMBER_OF_METHODS));
            metrics.put(MetricSort.NUMBER_OF_FIELDS, sum(MetricSort.NUMBER_OF_FIELDS));
            metrics.put(MetricSort.NUMBER_OF_METHODS_AND_FIELDS, sum(MetricSort.NUMBER_OF_METHODS_AND_FIELDS));
            metrics.put(MetricSort.NUMBER_OF_STATEMENTS, sum(MetricSort.NUMBER_OF_STATEMENTS));
        } catch (UnsupportedMetricsException e) {
            System.out.println(e.getMessage() + " in the project: " + getName());
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
        for (PackageMetrics pm : packageMetrics) {
            value = value + pm.getMetricValueWithException(sort);
        }
        return new Double(value);
    }
    
    /**
     * Collects information on this project, which is related to total metric values.
     */
    protected void collectMetricInfoForTotal() {
        double totalLOC = 0;
        double totalNOST = 0;
        double totalNOMD = 0;
        double totalNOFD = 0;
        double totalNOMDFD = 0;
        
        double totalNOPM = 0;
        double totalNOACL = 0;
        double totalNOECL = 0;
        
        double totalNOC = 0;
        double totalDIT = 0;
        double totalRFC = 0;
        double totalCBO = 0;
        double totalLCOM = 0;
        double totalWMC = 0;
        
        Set<ClassMetrics> classes = getClassMetrics();
        try {
            for (ClassMetrics cm : classes) {
                totalLOC = totalLOC + cm.getMetricValueWithException(MetricSort.LINES_OF_CODE);
                totalNOST = totalNOST + cm.getMetricValueWithException(MetricSort.NUMBER_OF_STATEMENTS);
                totalNOMD = totalNOMD + cm.getMetricValueWithException(MetricSort.NUMBER_OF_METHODS);
                totalNOFD = totalNOFD + cm.getMetricValueWithException(MetricSort.NUMBER_OF_FIELDS);
                totalNOMDFD = totalNOMDFD + cm.getMetricValueWithException(MetricSort.NUMBER_OF_METHODS_AND_FIELDS);
                
                totalNOPM = totalNOPM + cm.getMetricValueWithException(MetricSort.NUMBER_OF_PUBLIC_METHODS);
                totalNOACL = totalNOACL + cm.getMetricValueWithException(MetricSort.NUMBER_OF_AFFERENT_CLASSES);
                totalNOECL = totalNOECL + cm.getMetricValueWithException(MetricSort.NUMBER_OF_EFFERENT_CLASSES);
                
                totalNOC = totalNOC + cm.getMetricValueWithException(MetricSort.NUMBER_OF_CHILDREN);
                totalDIT = totalDIT + cm.getMetricValueWithException(MetricSort.DEPTH_OF_INHERITANCE_TREE);
                totalRFC = totalRFC + cm.getMetricValueWithException(MetricSort.RESPONSE_FOR_CLASS);
                totalCBO = totalCBO + cm.getMetricValueWithException(MetricSort.COUPLING_BETWEEN_OBJECTS);
                totalLCOM = totalLCOM + cm.getMetricValueWithException(MetricSort.LACK_OF_COHESION_OF_METHODS);
                totalWMC = totalWMC + cm.getMetricValueWithException(MetricSort.WEIGHTED_METHODS_PER_CLASS);
            }
        } catch (UnsupportedMetricsException e) {
            System.out.println(e.getMessage() + "in the class: " + getName());
        }
        
        putMetricValue(MetricSort.TOTAL_LINE_OF_CODE, totalLOC);
        putMetricValue(MetricSort.TOTAL_NUMBER_OF_STATEMENTS, totalNOST);
        putMetricValue(MetricSort.TOTAL_NUMBER_OF_METHODS, totalNOMD);
        putMetricValue(MetricSort.TOTAL_NUMBER_OF_FIELDS, totalNOFD);
        putMetricValue(MetricSort.TOTAL_NUMBER_OF_METHODS_AND_FIELDS, totalNOMDFD);
        
        putMetricValue(MetricSort.TOTAL_NUMBER_OF_PUBLIC_METHODS, totalNOPM);
        putMetricValue(MetricSort.TOTAL_NUMBER_OF_AFFERENT_CLASSES, totalNOACL);
        putMetricValue(MetricSort.TOTAL_NUMBER_OF_EFFERENT_CLASSES, totalNOECL);
        
        putMetricValue(MetricSort.TOTAL_NUMBER_OF_CHILDREN, totalNOC);
        putMetricValue(MetricSort.TOTAL_DEPTH_OF_INHERITANCE_TREE, totalDIT);
        putMetricValue(MetricSort.TOTAL_RESPONSE_FOR_CLASS, totalRFC);
        putMetricValue(MetricSort.TOTAL_COUPLING_BETWEEN_OBJECTS, totalCBO);
        putMetricValue(MetricSort.TOTAL_LACK_OF_COHESION_OF_METHODS, totalLCOM);
        putMetricValue(MetricSort.TOTAL_WEIGHTED_METHODS_PER_CLASS, totalWMC);
        
        for (ClassMetrics cm : classes) {
            cm.collectMetricInfoForTotal();
        }
    }
    
    /**
     * Collects information on this project, which is related to maximum metric values.
     */
    protected void collectMetricInfoForMax() {
        double maxLOC = 0;
        double maxNOST = 0;
        double maxNOMD = 0;
        double maxNOFD = 0;
        double maxNOMDFD = 0;
        
        double maxNOPM = 0;
        double maxNOACL = 0;
        double maxNOECL = 0;
        
        double maxNOC = 0;
        double maxDIT = 0;
        double maxRFC = 0;
        double maxCBO = 0;
        double maxLCOM = 0;
        double maxWMC = 0;
        
        Set<ClassMetrics> classes = getClassMetrics();
        try {
            for (ClassMetrics cm : classes) {
                maxLOC = Math.max(maxLOC, cm.getMetricValueWithException(MetricSort.LINES_OF_CODE));
                maxNOST = Math.max(maxNOST, cm.getMetricValueWithException(MetricSort.NUMBER_OF_STATEMENTS));
                maxNOMD = Math.max(maxNOMD, cm.getMetricValueWithException(MetricSort.NUMBER_OF_METHODS));
                maxNOFD = Math.max(maxNOFD, cm.getMetricValueWithException(MetricSort.NUMBER_OF_FIELDS));
                maxNOMDFD = Math.max(maxNOMDFD, cm.getMetricValueWithException(MetricSort.NUMBER_OF_METHODS_AND_FIELDS));
                
                maxNOPM = Math.max(maxNOPM, cm.getMetricValueWithException(MetricSort.NUMBER_OF_PUBLIC_METHODS));
                maxNOACL = Math.max(maxNOACL, cm.getMetricValueWithException(MetricSort.NUMBER_OF_AFFERENT_CLASSES));
                maxNOECL = Math.max(maxNOECL, cm.getMetricValueWithException(MetricSort.NUMBER_OF_EFFERENT_CLASSES));
                
                maxNOC = Math.max(maxNOC, cm.getMetricValueWithException(MetricSort.NUMBER_OF_CHILDREN));
                maxDIT = Math.max(maxDIT, cm.getMetricValueWithException(MetricSort.DEPTH_OF_INHERITANCE_TREE));
                maxRFC = Math.max(maxRFC, cm.getMetricValueWithException(MetricSort.RESPONSE_FOR_CLASS));
                maxCBO = Math.max(maxCBO, cm.getMetricValueWithException(MetricSort.COUPLING_BETWEEN_OBJECTS));
                maxLCOM = Math.max(maxLCOM, cm.getMetricValueWithException(MetricSort.LACK_OF_COHESION_OF_METHODS));
                maxWMC = Math.max(maxWMC, cm.getMetricValueWithException(MetricSort.WEIGHTED_METHODS_PER_CLASS));
            }
        } catch (UnsupportedMetricsException e) {
            System.out.println(e.getMessage() + "in the class: " + getName());
        }
        
        putMetricValue(MetricSort.MAX_LINE_OF_CODE, maxLOC);
        putMetricValue(MetricSort.MAX_NUMBER_OF_STATEMENTS, maxNOST);
        putMetricValue(MetricSort.MAX_NUMBER_OF_METHODS, maxNOMD);
        putMetricValue(MetricSort.MAX_NUMBER_OF_FIELDS, maxNOFD);
        putMetricValue(MetricSort.MAX_NUMBER_OF_METHODS_AND_FIELDS, maxNOMDFD);
        
        putMetricValue(MetricSort.MAX_NUMBER_OF_PUBLIC_METHODS, maxNOPM);
        putMetricValue(MetricSort.MAX_NUMBER_OF_AFFERENT_CLASSES, maxNOACL);
        putMetricValue(MetricSort.MAX_NUMBER_OF_EFFERENT_CLASSES, maxNOECL);
        
        putMetricValue(MetricSort.MAX_NUMBER_OF_CHILDREN, maxNOC);
        putMetricValue(MetricSort.MAX_DEPTH_OF_INHERITANCE_TREE, maxDIT);
        putMetricValue(MetricSort.MAX_RESPONSE_FOR_CLASS, maxRFC);
        putMetricValue(MetricSort.MAX_COUPLING_BETWEEN_OBJECTS, maxCBO);
        putMetricValue(MetricSort.MAX_LACK_OF_COHESION_OF_METHODS, maxLCOM);
        putMetricValue(MetricSort.MAX_WEIGHTED_METHODS_PER_CLASS, maxWMC);
        
        for (ClassMetrics cm : classes) {
            cm.collectMetricInfoForMax();
        }
    }
    
    /**
     * Collects information on this project. This method must be called after importing XML data.
     */
    public void collectMetricsInfoAfterXMLImport() {
        collectMetricInfoForTotal();
        collectMetricInfoForMax();
    }
}
