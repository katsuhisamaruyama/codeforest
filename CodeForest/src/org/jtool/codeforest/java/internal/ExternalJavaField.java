/*
 *  Copyright 2013, Katsuhisa Maruyama (maru@jtool.org)
 */

package org.jtool.codeforest.java.internal;

import org.jtool.codeforest.java.JavaClass;
import org.jtool.codeforest.java.JavaField;
import org.eclipse.jdt.core.dom.IVariableBinding;
import java.util.Map;
import java.util.HashMap;

/**
 * An object representing a field whose source code exists outside the project.
 * @author Katsuhisa Maruyama
 */
public class ExternalJavaField extends JavaField {
    
    /**
     * The cache for all external fields.
     */
    protected static Map<String, ExternalJavaField> cache = new HashMap<String, ExternalJavaField>();
    
    /**
     * Creates a new, empty object.
     */
    protected ExternalJavaField() {
        super();
    }
    
    /**
     * Creates a new object representing a field
     * @param binding a variable binding for the field
     */
    protected ExternalJavaField(IVariableBinding binding) {
        super();
        
        name = binding.getName();
        type = binding.getType().getQualifiedName();
        declaringClass = ExternalJavaClass.create(binding.getDeclaringClass());
    }
    
    /**
     * Creates a new object representing a field.
     * @param binding a variable binding for the field
     * @return the created object
     */
    public static ExternalJavaField create(IVariableBinding binding) {
        ExternalJavaField jfield = cache.get(binding.getKey());
        if (jfield != null) {
            return jfield;
        }
        
        jfield = new ExternalJavaField(binding);
        cache.put(binding.getKey(), jfield);
        return jfield;
    }
    
    /**
     * Tests if this field exists in the project.
     * @return always <code>false</code>
     */
    public boolean isInProject() {
        return false;
    }
    
    /**
     * Returns the class that declares this field
     * @return the class that declares this field, <code>null</code> if none
     */
    public JavaClass getDeclaringJavaClass() {
        return (ExternalJavaClass)declaringClass;
    }
    
    /**
     * Tests if a given field equals to this.
     * @param jf the Java field
     * @return <code>true</code> if the given field equals to this, otherwise <code>false</code>
     */
    public boolean equals(ExternalJavaField jf) {
        if (jf == null) {
            return false;
        }
        
        return this == jf || (getDeclaringJavaClass().equals(jf.getDeclaringJavaClass()) &&
                              getName().compareTo(jf.getName()) == 0); 
    }
    
    /**
     * Returns a hash code value for this field.
     * @return the hash code value for the field
     */
    public int hashCode() {
        return getName().hashCode();
    }
    
    /**
     * Collects information about this field.
     * @return the string for printing
     */
    public String toString() {
        return "!" + name;
    }
}
