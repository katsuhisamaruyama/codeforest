/*
 *  Copyright 2013, Katsuhisa Maruyama (maru@jtool.org)
 */

package org.jtool.codeforest.metrics.java;

import org.jtool.codeforest.metrics.MetricSort;
import org.jtool.eclipse.model.java.JavaClass;
import org.jtool.eclipse.model.java.JavaField;

/**
 * An object storing metric information on a field.
 * @author Katsuhisa Maruyama
 */
public class FieldMetrics extends CommonMetrics {
    
    /**
     * The identifier that represents field metrics
     */
    public static final String Id = "FieldMetrics";
    
    /**
     * An object representing a field.
     */
    private JavaField jfield;
    
    /**
     * A metrics object for a class containing this class.
     */
    private ClassMetrics classMetrics;
    
    /**
     * Creates a new object representing a field.
     * @param name the name of the field
     * @param type the type of the field
     * @param modifiers The modifiers of the field
     * @param isEnumConstant <code>true> if the field is an enum constant, otherwise <code>false</code>
     * @param cm a metrics object for a class containing the field
     */
    protected FieldMetrics(String name, String type, int modifiers, boolean isEnumConstant, ClassMetrics cm) {
        super();
        
        JavaClass jclass = cm.getJavaClass();
        jfield = new JavaField(name, type, modifiers, isEnumConstant, jclass);
        this.classMetrics = cm;
    }
    
    /**
     * Sets the code properties with respect to positions and line numbers of this field.
     * @param start the character index indicating where the code fragment for this field begins
     * @param len the length in characters of the code fragment for this field
     * @param upper the upper line number of code fragment for this field
     * @param bottom the bottom line number of code fragment for this field
     */
    protected void setCodeProperties(int start, int len, int upper, int bottom) {
        jfield.setCodeProperties(start, len, upper, bottom);
    }
    
    /**
     * Creates a new object representing a field.
     * @param node an AST node for this field
     * @param cm a metrics object for a class containing this field
     */
    protected FieldMetrics(JavaField jfield, ClassMetrics cm) {
        super();
        
        this.jfield = jfield;
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
     * Returns the object representing this field.
     * @return the field object
     */
    public JavaField getJavaField() {
        return jfield;
    }
    
    /**
     * Returns the name of this field.
     * @return the name
     */
    public String getName() {
        return jfield.getName();
    }
    
    /**
     * Returns the qualified name of this field.
     * @return the qualified name
     */
    public String getQualifiedName() {
        return jfield.getQualifiedName();
    }
    
    /**
     * Returns the return type of this field.
     * @return the return type
     */
    public String getType() {
        return jfield.getType();
    }
    
    /**
     * Returns the value representing modifiers of this field.
     * @return the modifiers value
     */
    public int getModifiers() {
        return jfield.getModifiers();
    }
    
    /**
     * Returns the name of a class containing this field.
     * @return the name of the class
     */
    public String getClassName() {
        return classMetrics.getQualifiedName();
    }
    
    /**
     * Tests if this object represents an enum constant.
     * @return <code>true</code> if the object represents an enum constant, otherwise <code>false</code>
     */
    public boolean isEnumConstant() {
        return jfield.isEnumConstant();
    }
    
    /**
     * Collects information about this field.
     * @return the string for printing
     */
    public String toString() {
        return jfield.toString();
    }
    
    /**
     * Collects metric information on this field.
     */
    private void collectMetricInfo() {
        metrics.put(MetricSort.LINES_OF_CODE, new Double(jfield.getLoc()));
        metrics.put(MetricSort.NUMBER_OF_STATEMENTS, new Double(1));
        metrics.put(MetricSort.NUMBER_OF_AFFERENT_METHODS, new Double(jfield.getAccessingJavaMethods().size()));
        metrics.put(MetricSort.NUMBER_OF_EFFERENT_METHODS, new Double(jfield.getCalledJavaMethods().size()));
        metrics.put(MetricSort.NUMBER_OF_AFFERENT_FIELDS, new Double(jfield.getAccessingJavaFields().size()));
        metrics.put(MetricSort.NUMBER_OF_EFFERENT_FIELDS, new Double(jfield.getAccessedJavaFields().size()));
    }
}
