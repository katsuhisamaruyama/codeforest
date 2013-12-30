/*
 *  Copyright 2013, Katsuhisa Maruyama (maru@jtool.org)
 */

package org.jtool.codeforest.java.internal;

import org.jtool.codeforest.java.JavaClass;
import org.jtool.codeforest.java.JavaField;
import org.jtool.codeforest.java.JavaMethod;
import org.jtool.codeforest.java.JavaVariableAccess;

/**
 * A special variable for formal-in, formal-out, actual-in, and actual-out nodes of a CFG.
 * @author Katsuhisa Maruyama
 */
public class JavaSpecialVariable extends JavaVariableAccess {
    
    /**
     * A class containing this variable. 
     */
    private JavaClass jclass = null;
    
    /**
     * A method containing this variable. 
     */
    private JavaMethod jmethod = null;
    
    /**
     * A field containing this variable. 
     */
    private JavaField jfield = null;
    
    /**
     * Creates a new, empty object.
     */
    protected JavaSpecialVariable() {
        super();
    }
    
    /**
     * Creates a new object representing a special variable.
     * @param name the name of this variable
     * @param type the type of this variable
     * @param jc the class containing this variable
     */
    public JavaSpecialVariable(String name, String type, JavaClass jc) {
        this.name = name;
        this.type = type;
        jclass = jc;
    }
    
    /**
     * Creates a new object representing a special variable.
     * @param name the name of this variable
     * @param type the type of this variable
     * @param jm the method containing this variable
     */
    public JavaSpecialVariable(String name, String type, JavaMethod jm) {
        this.name = name;
        this.type = type;
        jmethod = jm;
    }
    
    /**
     * Creates a new object representing a special variable.
     * @param name the name of this variable
     * @param type the type of this variable
     * @param jf the field containing this variable
     */
    public JavaSpecialVariable(String name, String type, JavaField jf) {
        this.name = name;
        this.type = type;
        jfield = jf;
    }
    
    /**
     * Returns the name of this variable.
     * @return The name string
     */
    public String getName() {
        return name;
    }
    
    /**
     * Returns the fully-qualified name of this variable.
     * @return The fully-qualified name string
     */
    public String getType() {
        return type;
    }
    
    /**
     * Returns the Java class enclosing this variable.
     * @return the enclosing class for this variable
     */
    public JavaClass getJavaClassOfAccessedVariable() {
        if (jclass != null) {
            return jclass;
        }
        if (jmethod != null) {
            return jmethod.getDeclaringJavaClass();
        }
        if (jfield != null) {
            return jfield.getDeclaringJavaClass();
        }
        return null;
    }
    
    /**
     * Returns the Java method enclosing this variable.
     * @return the enclosing method for this variable
     */
    public JavaMethod getJavaMethodOfAccessedVariable() {
        return jmethod;
    }
    
    /**
     * Tests if a given variable equals to this.
     * @param jv the Java variable
     * @return <code>true</code> if the given variable equals to this, otherwise <code>false</code>
     */
    public boolean equals(JavaVariableAccess jv) {
        if (jv == null) {
            return false;
        }
        if (this == jv) {
            return true;
        }
        
        return this == jv || getName().compareTo(jv.getName()) == 0;
    }
    
    /**
     * Returns a hash code value for this special variable.
     * @return the hash code value for the variable
     */
    public int hashCode() {
        return getName().hashCode();
    }
    
    /**
     * Collects information about the name of this variable.
     */
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append(getName());
        buf.append("@");
        buf.append(getType());
        
        return buf.toString();
    }
}
