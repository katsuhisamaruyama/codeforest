/*
 *  Copyright 2014, Katsuhisa Maruyama (maru@jtool.org)
 */

package org.jtool.codeforest.util;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import java.io.File;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;

/**
 * A class writing the contents of a DOM instance into an XML file.
 * @author Katsuhisa Maruyama
 */
public class XMLWriter {
    
    /**
     * Writes the contents of a DOM instance into an XML file.
     * @param file the XML file
     * @param doc the DOM instance
     */
    public static void write(File file, Document doc) {
        try {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource src = new DOMSource(doc);
            
            StringWriter writer = new StringWriter();
            transformer.transform(src, new StreamResult(writer));
            
            BufferedWriter bwriter = new BufferedWriter(new FileWriter(file));
            bwriter.write(writer.toString());
            bwriter.flush();
            bwriter.close();
            
        } catch (IOException e) {
            System.err.println("DOM: Export error occurred: " + e.getMessage() + ".");
            
        } catch (TransformerException e) {
            System.err.println("DOM: Export error occurred: " + e.getMessage() + ".");
        }
    }
}
