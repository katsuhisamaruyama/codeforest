/*
 *  Copyright 2014, Katsuhisa Maruyama (maru@jtool.org)
 */

package org.jtool.codeforest.metrics.java;

import org.jtool.codeforest.util.XMLWriter;
import org.jtool.eclipse.model.java.JavaProject;
import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.w3c.dom.Document;
import java.io.File;
import java.io.IOException;

/**
 * An object that manages metric values within a project.
 * @author Katsuhisa Maruyama
 */
public class MetricsManager {
    
    private static final String XML_FILENAME = "codeforest";
    private static final String XML_FILENAME_EXT = ".xml";
    
    public static final String TopElem = "codeForest";
    
    public static final String ProjectElem = "project";
    public static final String PackageElem = "package";
    public static final String ClassElem = "class";
    public static final String MethodElem = "method";
    public static final String FieldElem = "field";
    
    public static final String SuperClassElem = "superClass";
    public static final String SuperInterfaceElem = "superInterface";
    public static final String AfferentElem = "afferent";
    public static final String EfferentElem = "efferent";
    
    public static final String CodeElem = "code";
    
    public static final String MetricsElem = "metrics";
    
    public static final String DateAttr = "date";
    public static final String TimeAttr = "time";
    
    public static final String NameAttr = "name";
    public static final String PathAttr = "path";
    public static final String TypeAttr = "type";
    public static final String SignatureAttr = "sig";
    public static final String FqnAttr = "fqn";
    public static final String ModifiersAttr = "modifiers";
    
    public static final String isInterfaceAttr = "isInterface";
    public static final String isEnumAttr = "isEnum";
    public static final String isConstructorAttr = "isConstructor";
    public static final String isInitializerAttr = "isInitializer";
    public static final String isEnumConstantAttr = "isEnumConstant";
    public static final String Yes = "yes";
    public static final String No = "no";
    
    public static final String StartPositionAttr = "start";
    public static final String ExtendedStartPositionAttr = "exstart";
    public static final String CodeLengthAttr = "len";
    public static final String ExtendedCodeLengthAttr = "exlen";
    public static final String UpperLineNumberAttr = "upper";
    public static final String ExtendedUpperLineNumberAttr = "exupper";
    public static final String BottomLineNumberAttr = "bottom";
    public static final String ExtendedBottomLineNumberAttr = "exbottom";
    
    /**
     * Creates a new, empty object.
     */
    public MetricsManager() {
    }
    
    /**
     * Collects metric values within a project and returns an object storing these values.
     * @param jproject the project whose metric values are collected
     * @return the project metric, or <code>null</code> if the creation fails
     */
    public ProjectMetrics create(JavaProject jproject) {
        if (jproject != null) {
            return new ProjectMetrics(jproject);
        }
        return null;
    }
    
    /**
     * Imports metric values within a project and returns an object storing these values.
     * @param jproject the project whose metric values are collected
     * @return the project metric, or <code>null</code> if the creation fails
     */
    public ProjectMetrics readXML(JavaProject jproject) {
        return readXML(jproject.getTopDir());
    }
    
    /**
     * Imports metric values within a project and returns an object storing these values.
     * @param path the path of the file storing metric values
     * @return the project metric, or <code>null</code> if the creation fails
     */
    public ProjectMetrics readXML(String path) {
        File file = new File(path);
        if (file.canRead()) {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            try {
                SAXParser parser = factory.newSAXParser();
                ForestDataImporter handler = new ForestDataImporter(path);
                parser.parse(file, handler);
                return handler.getProjectMetrics();
                
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    
    /**
     * Exports metric values within a project.
     * @param mproject the project metrics
     */
    public void writeXML(ProjectMetrics mproject) {
        writeXML(mproject.getJavaProject().getTopDir(), mproject);
    }
    
    /**
     * Exports metric values within a project.
     * @param topdir the path of the top directory for the project
     * @param mproject the project metrics
     */
    public void writeXML(String topdir, ProjectMetrics mproject) {
        String filename = XML_FILENAME + String.valueOf(mproject.getTime()) + XML_FILENAME_EXT;
        File file = new File(topdir + File.separator + filename);
        
        if (file.exists()) {
            file.delete();
        }
        
        Document doc = ForestDataExporter.getDocument(mproject);
        XMLWriter.write(file, doc);
        System.out.println("- Export metric values to xml file: " + file.getAbsolutePath());
    }
}
