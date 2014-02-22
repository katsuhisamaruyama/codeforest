/*
 *  Copyright 2014, Katsuhisa Maruyama (maru@jtool.org)
 */

package org.jtool.codeforest.util;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.w3c.dom.Document;
import java.io.File;

/**
 * A class reading XML file.
 * @author Katsuhisa Maruyama
 */
public class XMLReader {
    
    /**
     * Reads an XML file and returns a DOM instance that stores the read contents.
     * @param file the XML file
     * @param doc the DOM instance
     */
    public static Document read(final File file) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setIgnoringElementContentWhitespace(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            
            builder.setErrorHandler(new ErrorHandler() {
                
                public void error(SAXParseException exception) throws SAXException {
                    System.err.println("[ERROR]file:" + file.getPath() + "line:" + exception.getLineNumber() + ", " +
                                       "column:" + exception.getColumnNumber() + ", " + exception.getMessage());
                }
                
                public void fatalError(SAXParseException exception) throws SAXException {
                    System.err.println("[FATAL]file:" + file.getPath() + "line:" + exception.getLineNumber() + ", " +
                                       "column:" + exception.getColumnNumber() + ", " + exception.getMessage());
                }
                
                public void warning(SAXParseException exception) throws SAXException {
                    System.err.println("[WARNING]file:" + file.getPath() + "line:" + exception.getLineNumber() + ", " +
                                       "column:" + exception.getColumnNumber() + ", " + exception.getMessage());
                }
            });
            
            return builder.parse(file);
            
        } catch (Exception e) {
            System.out.println("DOM: Parse error occurred: " + e.getMessage() + ".");
        }
        return null;
    }
}
