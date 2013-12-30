/*
 *  Copyright 2013, Katsuhisa Maruyama (maru@jtool.org)
 */

package org.jtool.codeforest.java.internal;

import org.eclipse.jdt.core.dom.IPackageBinding;
import org.jtool.codeforest.java.JavaPackage;
import java.util.HashMap;

/**
 * An object representing a package whose source code exists outside the project.
 * @author Katsuhisa Maruyama
 */
public class ExternalJavaPackage extends JavaPackage {
    
    /**
     * The cache for all objects of packages.
     */
    protected static HashMap<String, ExternalJavaPackage> cache = new HashMap<String, ExternalJavaPackage>();
    
    /**
     * Prohibits creating an empty object.
     */
    private ExternalJavaPackage() {
    }
    
    /**
     * Creates a new object representing a package.
     * @param name the name of the package
     */
    private ExternalJavaPackage(String name) {
        super();
        
        this.name = name;
    }
    
    /**
     * Creates a new object representing a package.
     * @param binding a package binding for this package
     * @return the created object
     */
    public static ExternalJavaPackage create(IPackageBinding binding) {
        String name = binding.getName();
        ExternalJavaPackage jpackage = cache.get(name);
        if (jpackage != null) {
            return jpackage;
        }
        
        jpackage = new ExternalJavaPackage(name);
        cache.put(name, jpackage);
        return jpackage;
    }
    
    /**
     * Tests if this package exists in the project.
     * @return always <code>false</code>
     */
    public boolean isInProject() {
        return false;
    }
    
    /**
     * Tests if a given package equals to this.
     * @param jp the Java package
     * @return <code>true</code> if the given package equals to this, otherwise <code>false</code>
     */
    public boolean equals(ExternalJavaPackage jp) {
        if (jp == null) {
            return false;
        }
        
        return this == jp || getName().compareTo(jp.getName()) == 0; 
    }
    
    /**
     * Returns a hash code value for this package.
     * @return the hash code value for the package
     */
    public int hashCode() {
        return getName().hashCode();
    }
    
    /**
     * Collects information about this package.
     * @return the string for printing
     */
    public String toString() {
        return "!" + name;
    }
}
