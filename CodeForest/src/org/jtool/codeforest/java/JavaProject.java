/*
 *  Copyright 2013, Katsuhisa Maruyama (maru@jtool.org)
 */

package org.jtool.codeforest.java;

import org.eclipse.jdt.core.IJavaProject;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;

/**
 * An object representing a project.
 * @author Katsuhisa Maruyama
 */
public class JavaProject {
    
    /**
     * The collection of all files.
     */
    private Map<String, JavaFile> files = new HashMap<String, JavaFile>();
    
    /**
     * The collection of all packages.
     */
    private HashMap<String, JavaPackage> packages = new HashMap<String, JavaPackage>();
    
    /**
     * The information of which stored in this project.
     */
    private IJavaProject project;
    
    /**
     * The name of this project.
     */
    private String name;
    
    /**
     * The name of the top directory of this project.
     */
    private String topDir;
    
    /**
     * The time when the project information was created lastly.
     */
    private long lastCreatedTime;
    
    /**
     * Creates an object that will store information about a project.
     * @param project the project information
     * @param name the name of the project
     * @param dir the top directory of the project
     */
    private JavaProject(IJavaProject project, String name, String dir) {
        this.project = project;
        this.name = name;
        this.topDir = dir;
        this.lastCreatedTime = System.currentTimeMillis();
    }
    
    /**
     * Creates an object that will store information about a project.
     * @param project the project information
     */
    public static JavaProject create(IJavaProject project) {
        String name = project.getProject().getName();
        String dir = project.getProject().getLocation().toString();
        return create(project, name, dir);
    }
    
    /**
     * Creates an object that will store information about a project.
     * @param name the name of the project
     * @param dir the top directory of the project
     */
    public static JavaProject create(String name, String dir) {
        return create(null, name, dir);
    }
    
    /**
     * Creates an object that will store information about a project.
     * @param project the project information
     * @param name the name of the project
     * @param dir the top directory of the project
     */
    private static JavaProject create(IJavaProject project, String name, String dir) {
        if (dir != null) {
            return new JavaProject(project, name, dir);
        }
        return null;
    }
    
    /**
     * Returns the time when the project information was created lastly.
     * @return the last created time
     */
    public long getLastCreatedTime() {
        return lastCreatedTime;
    }
    
    /**
     * Tests if this project exists in the Eclipse's workspace
     * @return <code>true</code> if this project lays on the workspace, otherwise <code>false</code>
     */
    public boolean isInWorkspace() {
        return project != null;
    }
    
    /**
     * Returns the name of this project.
     * @return the name of the project
     */
    public String getName() {
        return name;
    }
    
    /**
     * Returns the top directory of this project.
     * @return the name of the top directory of the project
     */
    public String getTopDir() {
        return topDir;
    }
    
    /**
     * Adds a file contained in this project.
     * @param jfile the file to be added
     */
    public void addJavaFile(JavaFile jfile) {
        if (files.get(jfile.getPath()) == null) {
            files.put(jfile.getPath(), jfile);
        }
    }
    
    /**
     * Returns all the files in this project.
     * @return the collection of the files
     */
    public Set<JavaFile> getJavaFiles() {
        Set<JavaFile> sets = new HashSet<JavaFile>();
        for (JavaFile jf : files.values()) {
            sets.add(jf);
        }
        return sets;
    }
    
    /**
     * Returns a file with a given path name.
     * @param path the path name of the file
     * @return the found object, or <code>null</code> if none 
     */
    public JavaFile getJavaFile(String path) {
        return files.get(path);
    }
    
    /**
     * Adds a package contained in this project.
     * @param jpackage the package to be added
     */
    public void addJavaPackage(JavaPackage jpackage) {
        if (packages.get(jpackage.getName()) == null) {
            packages.put(jpackage.getName(), jpackage);
        }
    }
    
    /**
     * Returns all the packages in this project.
     * @return the collection of the packages
     */
    public Set<JavaPackage> getJavaPackages() {
        Set<JavaPackage> sets = new HashSet<JavaPackage>();
        for (JavaPackage jp : packages.values()) {
            sets.add(jp);
        }
        return sets;
    }
    
    /**
     * Returns a package with a given name.
     * @param name the name of the package
     * @return the found object, or <code>null</code> if none 
     */
    public JavaPackage getJavaPackage(String name) {
        return packages.get(name);
    }
    
    /**
     * Returns all the classes in this project.
     * @return the collection of the classes
     */
    public Set<JavaClass> getJavaClasses() {
        return JavaClass.getAllClassesInCache();
    }
    
    /**
     * Returns an object corresponding to a class with a given name.
     * @param fqn the fully qualified name of a class or an interface to be retrieved
     * @return the found object, or <code>null</code> if no class was found
     */
    public JavaClass getJavaClass(String fqn) {
        return JavaClass.getJavaClass(fqn);
    }
    
    /**
     * Tests if a given package equals to this.
     * @param jp the Java package
     * @return <code>true</code> if the given package equals to this, otherwise <code>false</code>
     */
    public boolean equals(JavaProject jproj) {
        if (jproj == null) {
            return false;
        }
        
        return this == jproj || getTopDir().compareTo(jproj.getTopDir()) == 0; 
    }
    
    /**
     * Returns a hash code value for this project.
     * @return the hash code value for the project
     */
    public int hashCode() {
        return getTopDir().hashCode();
    }
    
    /**
     * Collects information about this package.
     * @return the string for printing
     */
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("PROJECT: ");
        buf.append(getName());
        buf.append(" [");
        buf.append(getTopDir());
        buf.append("]");
        buf.append("\n");
        
        return buf.toString();
    }
}
