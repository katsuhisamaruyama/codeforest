/*
 *  Copyright 2013, Katsuhisa Maruyama (maru@jtool.org)
 */

package org.jtool.codeforest.java;

import org.jtool.codeforest.io.FileReader;
import org.eclipse.jdt.core.ICompilationUnit;
import java.io.IOException;

/**
 * Stores information of a Java source file.
 * @author Katsuhisa Maruyama
 */
public class JavaFile {
    
    /**
     * The path name of this file.
     */
    private String path;
    
    /**
     * A project containing this file.
     */
    private JavaProject jproject;
    
    /**
     * The contents of this file.
     */
    private String source;
    
    /**
     * Creates an object that will store information about a given file.
     * @param icu the compilation unit.
     * @param jproject the project containing this file
     */
    public JavaFile(ICompilationUnit icu, JavaProject jproject) {
        this.path = icu.getPath().toString();
        this.jproject = jproject;
    }
    
    /**
     * Creates an object that will store information about a given file.
     * @param path the path name of the file
     * @param jproject the project containing this file
     */
    public JavaFile(String path, JavaProject jproject) {
        this.path = path;
        this.jproject = jproject;
        
    }
    
    /**
     * Returns the path name of this file
     * @return the path name
     */
    public String getPath() {
        return path;
    }
    
    /**
     * Returns the source code for this file.
     * @return the source code
     */
    public String getSource() {
        return source;
    }
    
    /**
     * Returns the project containing this file.
     * @return the project
     */
    public JavaProject getJavaProject() {
        return jproject;
    }
    
    /**
     * Tests if a given class or interface equals to this.
     * @param jc the Java class
     * @return <code>true</code> if the given class equals to this, otherwise <code>false</code>
     */
    public boolean equals(JavaFile jcu) {
        if (jcu == null) {
            return false;
        }
        
        return this == jcu || getPath().compareTo(jcu.getPath()) == 0; 
    }
    
    /**
     * Returns a hash code value for this file.
     * @return the hash code value for the file
     */
    public int hashCode() {
        return getPath().hashCode();
    }
    
    /**
     * Obtains the relative path name of a given file or directory to a base directory. 
     * @param path the path name of the file or directory
     * @param base the path name of the base directory
     * @return the relative pathname, or <code>null</code> if the file or directory is not contained a base directory or its sub-directories
     */
    public static String getRelativePath(String path, String base) {
        if (path.startsWith(base) && path.length() > base.length()) {
            return path.substring(base.length() + 1);
        }
        return null;
    }
    
    /**
     * Obtains the contents of the current source code for this file.
     * @return the contents of the source code
     */
    public String getCurrentCode() {
        if (source != null) {
            return source;
        }
        
        String name = getFileName(jproject.getTopDir());
        try {
            source = FileReader.read(name);
        } catch (IOException e) {
            return "";
        }
        
        return source;
    }
    
    /**
     * Obtains the path of the source code stored in this file.
     * @param progName the project name
     * @return the file name of the source code
     */
    private String getFileName(String projName) {
        projName = projName.substring(0, projName.length());
        int lindex = projName.lastIndexOf('/');
        String progDir = projName.substring(0, lindex);
        return progDir + path;
    }
}
