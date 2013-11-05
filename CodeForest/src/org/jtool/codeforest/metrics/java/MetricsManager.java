/*
 *  Copyright 2013, Katsuhisa Maruyama (maru@jtool.org)
 */

package org.jtool.codeforest.metrics.java;

import org.jtool.eclipse.model.java.JavaProject;
import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;

/**
 * An object that manages metric values within a project.
 * @author Katsuhisa Maruyama
 */
public class MetricsManager {
    
    private static final String XmlFileName = "codeforest.xml";
    
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
    public static final String CodeLengthAttr = "len";
    public static final String UpperLineNumberAttr = "upper";
    public static final String BottomLineNumberAttr = "bottom";
    
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
     * @param path the path of the project whose metric values are collected
     * @return the project metric, or <code>null</code> if the creation fails
     */
    public ProjectMetrics readXML(String path) {
        File file = new File(getFileName(path));
        if (file.canRead()) {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            try {
                SAXParser parser = factory.newSAXParser();
                XMLImporter handler = new XMLImporter(path);
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
     * @param jproject the project whose metric values are collected
     * @param mproject the project metrics
     */
    public void writeXML(JavaProject jproject, ProjectMetrics mproject) {
        writeXML(jproject.getTopDir(), mproject);
    }
    
    /**
     * Exports metric values within a project.
     * @param path the path of the project
     * @param mproject the project metrics
     */
    public void writeXML(String path, ProjectMetrics mproject) {
        File file = new File(getFileName(path));
        
        if (file.exists()) {
            file.delete();
        }
        
        XMLExporter exporter = new XMLExporter();
        exporter.export(file, mproject);
        System.out.println("- Export metric values to xml file: " + file.getAbsolutePath());
    }
    
    /**
     * Obtains the name of a file storing metric values within a project
     * @param path the path of the project
     * @return the file name
     */
    private String getFileName(String path) {
        return path + "/" + XmlFileName;
    }
}
