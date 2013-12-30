/*
 *  Copyright 2013, Katsuhisa Maruyama (maru@jtool.org)
 */

package org.jtool.codeforest.io;

import java.io.InputStreamReader;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Reads the contents of a file.
 * @author Katsuhisa Maruyama
 */
public class FileReader {
    
    /**
     * Reads a file and returns its textual contents.
     * @param name the name of a file to be read
     * @return the textual contents of the read file
     * @throws FileNotFoundException if the file does not exist or cannot be opened for reading
     * @throws IOException if an I/O error occurred
     */
    public static String read(String name) throws FileNotFoundException, IOException {
        return read(new File(name));
    }            

    /**
     * Reads a file and returns its textual contents.
     * @param name the name of a file to be read
     * @param charsetName the name of a character-set
     * @return the textual contents of the read file
     * @throws FileNotFoundException if the file does not exist or cannot be opened for reading
     * @throws IOException if an I/O error occurred
     * @throws UnsupportedEncodingException if the character-set is not supported
     */
    public static String read(String name, String charsetName) throws FileNotFoundException, IOException,
                                                                      UnsupportedEncodingException {
        return read(new File(name), charsetName);
    }

    /**
     * Reads a file and returns its textual contents.
     * @param file the file to be read
     * @return the textual contents of the read file
     * @throws FileNotFoundException if the file does not exist or cannot be opened for reading
     * @throws IOException if an I/O error occurred
     */
    public static String read(File file) throws FileNotFoundException, IOException {
        return read(new InputStreamReader(new FileInputStream(file)));
    }
            
    /**
     * Reads a file and returns its textual contents.
     * @param file the file to be read
     * @param charsetName the name of a character-set
     * @return the textual contents of the read file
     * @throws FileNotFoundException if the file does not exist or cannot be opened for reading
     * @throws IOException if an I/O error occurred
     * @throws UnsupportedEncodingException if the character-set is not supported
     */
    public static String read(File file, String charsetName) throws FileNotFoundException, IOException,
                                                                    UnsupportedEncodingException {
        return read(new InputStreamReader(new FileInputStream(file), charsetName));
    }
    
    /**
     * Reads a file and returns its textual contents through a given reader.
     * @param reader the reader for the read file
     * @return the textual contents of the read file
     * @throws IOException if an I/O error occurred
     * @throws UnsupportedEncodingException if the character-set is not supported
     */
    private static String read(Reader reader) throws IOException, UnsupportedEncodingException {
        StringBuffer content = new StringBuffer();
        BufferedReader breader = new BufferedReader(reader);
        
        while (breader.ready()) {
            String oneLineString = breader.readLine();
            if (oneLineString == null) {
                break;
            }
            content.append(oneLineString + "\n");
        }
        breader.close();
        return new String(content);
    }
}
