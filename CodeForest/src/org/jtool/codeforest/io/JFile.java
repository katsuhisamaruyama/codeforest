/*
 *  Copyright 2013, Katsuhisa Maruyama (maru@jtool.org)
 */

package org.jtool.codeforest.io;

import java.io.File;

/**
 * An object that represents a file or directory.
 * @author Katsuhisa Maruyama
 */
public class JFile extends File {
    private static final long serialVersionUID = -2380955639766453711L;
    
    /**
     * Creates a file or directory denoted by a given path name.
     * @param name the path name of a file or directory
     */
    public JFile(String name) {
        super(name);
    }
    
    /**
     * Creates a directory storing this file or directory.
     */
    public void makeDir() {
        int sep = getPath().lastIndexOf(File.separator);
        if (sep != -1) {
            String dirname = getPath().substring(0, sep);
            JFile file = new JFile(dirname);
            file.mkdirs();
        }
    }
    
    /**
     * Deletes this file or directory, and its sub-directories if it is a directory.
     * @param <code>true</code> if the file or directory is successfully deleted, otherwise <code>false</code>
     */
    public boolean delete() {
        if (isDirectory()) {
            String[] names = list();
            for (int i = 0; i < names.length; i++) {
                JFile file = new JFile(getPath() + File.separator + names[i]);
                file.delete();
            }
        }
        return delete();
    }
    
    /**
     * Obtains the relative path name of a given file or directory to a base directory. 
     * @param pathname the path name of the file or directory
     * @param basename the path name of the base directory
     * @return the relative pathname, or <code>null</code> if the file or directory is not contained a base directory or its sub-directories
     */
    public static String getRelativePath(String pathname, String basename) {
        if (pathname.startsWith(basename) && pathname.length() > basename.length()) {
            return pathname.substring(basename.length() + 1);
        }
        return null;
    }
    
    /**
     * Obtains the path name of a file the extension of which was replaced with a new one.
     * @param pathname the path name of the file
     * @param ext the new extension of the file
     * @return the replaced path name. The extension is simply added if the original file name has no extension
     */
    public static String changeExtension(String pathname, String ext) {
        int pos = pathname.lastIndexOf(".");
        if (pos != -1) {
            return pathname.substring(0, pos + 1) + ext;
        }
        return pathname + "." + ext;
    }
}
