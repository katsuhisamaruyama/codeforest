/*
 *  Copyright 2013, Katsuhisa Maruyama (maru@jtool.org)
 */

package org.jtool.codeforest.io;

import java.io.OutputStreamWriter;
import java.io.Writer;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Writes the contents of a file.
 * @author Katsuhisa Maruyama
 */
public class FileWriter {

    /**
     * Writes the textual contents of a file.
     * @param name the name of the file to be written
     * @param text the textual contents of the file
     * @throws FileNotFoundException if the file cannot be opened for writing
     * @throws IOException if an I/O error occurred
     */
    public static void write(String name, String text) throws FileNotFoundException, IOException {
        write(new File(name), text);
    }

    /**
     * Writes the textual contents of a file.
     * @param name the name of the file to be written
     * @param text the textual contents of the file
     * @param charsetName the name of a character-set
     * @throws FileNotFoundException if the file cannot be opened for writing
     * @throws IOException if an I/O error occurred
     * @throws UnsupportedEncodingException if the character-set is not supported
     */
    public static void write(String name, String text, String charsetName) throws FileNotFoundException, IOException,
                                                                                  UnsupportedEncodingException {
        write(new File(name), text, charsetName);
    }            

    /**
     * Writes characters of a text into a file.
     * @param file the file to be written
     * @param text the textual contents of the file
     * @throws FileNotFoundException if the file cannot be opened for writing
     * @throws IOException if an I/O error occurred.
     */
    public static void write(File file, String text) throws FileNotFoundException, IOException {
        write(new OutputStreamWriter(new FileOutputStream(file)), text);
    }

    /**
     * Writes characters of a text into the file.
     * @param file the file to be written.
     * @param text the textual contents of the file
     * @param charsetName the name of a character-set
     * @throws FileNotFoundException if the file cannot be opened for writing
     * @throws IOException if an I/O error occurred.
     * @throws UnsupportedEncodingException if the character-set is not supported
     */
    public static void write(File file, String text, String charsetName) throws FileNotFoundException, IOException,
                                                                                UnsupportedEncodingException {
        write(new OutputStreamWriter(new FileOutputStream(file), charsetName), text);
    }
    
    /**
     * Writes the textual contents through a given writer.
     * @param writer the writer for the written file
     * @param text the textual contents of the file
     * @throws IOException if an I/O error occurred
     * @throws UnsupportedEncodingException if the character-set is not supported
     */
    private static void write(Writer writer, String text) throws IOException, UnsupportedEncodingException {
        BufferedWriter bwriter = new BufferedWriter(writer);
        bwriter.write(text);
        bwriter.flush();
        bwriter.close();
    }
}
