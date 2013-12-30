/*
 *  Copyright 2013, Katsuhisa Maruyama (maru@jtool.org)
 */

package org.jtool.codeforest.metrics.java;

import org.jtool.eclipse.model.java.JavaClass;
import org.jtool.eclipse.model.java.JavaMethod;
import org.jtool.codeforest.metrics.MetricSort;

/**
 * An object storing metric information on a method, a constructor, or an initializer.
 * @author Katsuhisa Maruyama
 */
public class MethodMetrics extends CommonMetrics {
    
    /**
     * The identifier that represents method metrics
     */
    public static final String Id = "MethodMetrics";
    
    /**
     * An object representing a method, a constructor, or an initializer.
     */
    private JavaMethod jmethod;
    
    /**
     * A metrics object for a class containing this method.
     */
    private ClassMetrics classMetrics;
    
    /**
     * Creates a new object representing a method, a constructor, or an initializer
     * @param name the name of this method
     * @param sig the signature of this method
     * @param type the type of this method.
     * @param modifiers the modifiers of this method.
     * @param isConstructor <code>true</code> if this method is a constructor, otherwise <code>false</code>
     * @param isInitializer <code>true</code> if this method is a initializer, otherwise <code>false</code>
     * @param cm a metrics object for a class containing the method
     */
    protected MethodMetrics(String name, String sig, String type, int modifiers, boolean isConstructor, boolean isInitializer, ClassMetrics cm) {
        super();
        
        JavaClass jc = cm.getJavaClass();
        jmethod = new JavaMethod(name, sig, type, modifiers, isConstructor, isInitializer, jc);
        this.classMetrics = cm;
    }
    
    /**
     * Sets the code properties with respect to positions and line numbers of this method.
     * @param start the character index indicating where the code fragment for this method begins
     * @param len the length in characters of the code fragment for this method
     * @param upper the upper line number of code fragment for this method
     * @param bottom the bottom line number of code fragment for this method
     */
    protected void setCodeProperties(int start, int len, int upper, int bottom) {
        jmethod.setCodeProperties(start, len, upper, bottom);
    }
    
    /**
     * Creates a new object representing a method, a constructor, or an initializer.
     * @param node an AST node for this method or initializer
     * @param cm a metrics object for a class containing this method
     */
    protected MethodMetrics(JavaMethod jmethod, ClassMetrics cm) {
        super();
        
        this.jmethod = jmethod;
        classMetrics = cm;
        
        collectMetricInfo();
    }
    
    /**
     * Returns the metrics object for a class containing this method.
     * @return the class metrics
     */
    public ClassMetrics getClassMetrics() {
        return classMetrics;
    }
    
    /**
     * Returns the object representing this method.
     * @return the method object
     */
    public JavaMethod getJavaMethod() {
        return jmethod;
    }
    
    /**
     * Returns the name of this method.
     * @return the name
     */
    public String getName() {
        return jmethod.getName();
    }
    
    /**
     * Returns the signature of this method.
     * @return the signature
     */
    public String getSignature() {
        return jmethod.getSignature();
    }
    
    /**
     * Returns the qualified name of this method.
     * @return the qualified name
     */
    public String getQualifiedName() {
        return jmethod.getQualifiedName();
    }
    
    /**
     * Returns the return type of this method.
     * @return the return type
     */
    public String getType() {
        return jmethod.getType();
    }
    
    /**
     * Returns the value representing modifiers of this method.
     * @return the modifiers value
     */
    public int getModifiers() {
        return jmethod.getModifiers();
    }
    
    /**
     * Returns the name of a class containing this method.
     * @return the name of the class
     */
    public String getClassName() {
        return classMetrics.getQualifiedName();
    }
    
    /**
     * Tests if this object represents a constructor.
     * @return <code>true</code> if the object represents a constructor, otherwise <code>false</code>
     */
    public boolean isConstructor() {
        return jmethod.isConstructor();
    }
    
    /**
     * Tests if this object represents an initializer.
     * @return <code>true</code> if the object represents an initializer, otherwise <code>false</code>
     */
    public boolean isInitializer() {
        return jmethod.isInitializer();
    }
    
    /**
     * Collects information about this method.
     * @return the string for printing
     */
    public String toString() {
        return getQualifiedName();
    }
    
    /**
     * Collects information on this method.
     */
    private void collectMetricInfo() {
        metrics.put(MetricSort.LINES_OF_CODE, new Double(jmethod.getLoc()));
        metrics.put(MetricSort.NUMBER_OF_AFFERENT_METHODS, new Double(jmethod.getCallingJavaMethods().size()));
        metrics.put(MetricSort.NUMBER_OF_EFFERENT_METHODS, new Double(jmethod.getCalledJavaMethods().size()));
        metrics.put(MetricSort.NUMBER_OF_AFFERENT_FIELDS, new Double(jmethod.getAccessingJavaFields().size()));
        metrics.put(MetricSort.NUMBER_OF_EFFERENT_FIELDS, new Double(jmethod.getAccessedJavaFields().size()));
        metrics.put(MetricSort.NUMBER_OF_PARAMETERS, new Double(jmethod.getParameters().size()));
        
        StatementInfoCollector mvisitor = new StatementInfoCollector();
        jmethod.getASTNode().accept(mvisitor);
        
        metrics.put(MetricSort.NUMBER_OF_STATEMENTS, new Double(mvisitor.getNumberOfStatements()));
        metrics.put(MetricSort.CYCLOMATIC_COMPLEXITY, new Double(mvisitor.getCyclomaticNumber()));
        metrics.put(MetricSort.MAX_NUMBER_OF_NESTING, new Double(mvisitor.getMaximumNuberOfNesting()));
    }
}
