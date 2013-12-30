/*
 *  Copyright 2013, Katsuhisa Maruyama (maru@jtool.org)
 */

package org.jtool.codeforest.io;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Compares the contents of two files.
 * @author Katsuhisa Maruyama
 */
public class FileCompare {
    
    /**
     * Compares the contents of two files.
     * @param name1 the name of one of the files to be compared
     * @param name2 the name of the other of the files to be compared
     * @return <code>true</code> if the contents are the same, otherwise <code>false</code>
     */
    public static boolean compare(String name1, String name2) {
        try {
            String content1 = FileReader.read(name1);
            String content2 = FileReader.read(name2);
            
            if (content1 == null || content2 == null) {
                return false;
            }
            
            if (content1.compareTo(content2) == 0) {
                return true;
            }
        } catch (FileNotFoundException e) {
            return false;
        } catch (IOException e) {
            return false;
        }
        return false;
    }
}
