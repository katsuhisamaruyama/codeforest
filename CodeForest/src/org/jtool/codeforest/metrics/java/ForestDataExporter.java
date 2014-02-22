/*
 *  Copyright 2014, Katsuhisa Maruyama (maru@jtool.org)
 */

package org.jtool.codeforest.metrics.java;

import org.jtool.codeforest.util.Time;
import org.jtool.eclipse.model.java.JavaElement;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import java.util.Map;

/**
 * A class exporting metric values within a project.
 * @author Katsuhisa Maruyama
 */
public class ForestDataExporter {
    
    /**
     * Creates a new, empty object.
     */
    public ForestDataExporter() {
    }
    
    /**
     * Creates a DOM instance that stores information about a project.
     * @param mproject the project whose information will be exported
     * @return the DOM instance
     */
    public static Document getDocument(ProjectMetrics mproject) {
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.newDocument();
            
            export(doc, mproject);
            return doc;
            
        } catch (ParserConfigurationException e) {
            System.err.println("DOM: Export error occurred: " + e.getMessage() + ".");
        }
        return null;
    }
    
    /**
     * Exports metric values with respect to a project.
     * @param doc the content of the DOM instance
     * @param mproject project metrics
     */
    private static void export(Document doc, ProjectMetrics mproject) {
        Element rootElem = doc.createElement(MetricsManager.TopElem);
        rootElem.setAttribute(MetricsManager.DateAttr, Time.toString(mproject.getTime()));
        doc.appendChild(rootElem);
        
        Element projectElem = doc.createElement(MetricsManager.ProjectElem);
        projectElem.setAttribute(MetricsManager.NameAttr, mproject.getName());
        projectElem.setAttribute(MetricsManager.TimeAttr, String.valueOf(mproject.getTime()));
        rootElem.appendChild(projectElem);
        
        exportMetricAttributes(doc, projectElem, mproject.getMetricValues());
        
        mproject.sortPackages();
        for (PackageMetrics pm : mproject.getPackageMetrics()) {
            export(doc, projectElem, pm);
        }
    }
    
    /**
     * Exports metric values with respect to a package.
     * @param doc the content of the DOM instance
     * @param parent the parent DOM element
     * @param mpackage package metrics
     */
    private static void export(Document doc, Element parent, PackageMetrics mpackage) {
        Element packageElem = doc.createElement(MetricsManager.PackageElem);
        packageElem.setAttribute(MetricsManager.NameAttr, mpackage.getName());
        parent.appendChild(packageElem);
        
        mpackage.sort(mpackage.getAfferentPackageNames());
        for (String name : mpackage.getAfferentPackageNames()) {
            Element afferentElem = doc.createElement(MetricsManager.AfferentElem);
            afferentElem.setAttribute(MetricsManager.FqnAttr, name);
            packageElem.appendChild(afferentElem);
        }
        mpackage.sort(mpackage.getEfferentPackageNames());
        for (String name : mpackage.getEfferentPackageNames()) {
            Element efferentElem = doc.createElement(MetricsManager.EfferentElem);
            efferentElem.setAttribute(MetricsManager.FqnAttr, name);
            packageElem.appendChild(efferentElem);
        }
        
        exportMetricAttributes(doc, packageElem, mpackage.getMetricValues());
        
        mpackage.sortClasses();
        for (ClassMetrics cm : mpackage.getClassMetrics()) {
            export(doc, packageElem, cm);
        }
    }
    
    /**
     * Exports metric values with respect to a class.
     * @param doc the content of the DOM instance
     * @param parent the parent DOM element
     * @param mclass class metrics
     */
    private static void export(Document doc, Element parent, ClassMetrics mclass) {
        Element classElem = doc.createElement(MetricsManager.ClassElem);
        classElem.setAttribute(MetricsManager.NameAttr, mclass.getName());
        classElem.setAttribute(MetricsManager.FqnAttr, mclass.getQualifiedName());
        classElem.setAttribute(MetricsManager.ModifiersAttr, String.valueOf(mclass.getModifiers()));
        classElem.setAttribute(MetricsManager.isInterfaceAttr, getBoolean(mclass.isInterface()));
        classElem.setAttribute(MetricsManager.isEnumAttr, getBoolean(mclass.isEnum()));
        classElem.setAttribute(MetricsManager.PathAttr, mclass.getPath());
        parent.appendChild(classElem);
        
        exportCodeAttributes(doc, classElem, mclass.getJavaClass());
        
        Element superclassElem = doc.createElement(MetricsManager.SuperClassElem);
        superclassElem.setAttribute(MetricsManager.FqnAttr, mclass.getSuperClassName());
        classElem.appendChild(superclassElem);
        
        for (String name : mclass.getSuperInterfaceNames()) {
            Element superinterfaceElem = doc.createElement(MetricsManager.SuperInterfaceElem);
            superinterfaceElem.setAttribute(MetricsManager.FqnAttr, name);
            classElem.appendChild(superinterfaceElem);
        }
        
        mclass.sort(mclass.getAfferentClassNames());
        for (String name : mclass.getAfferentClassNames()) {
            Element afferentElem = doc.createElement(MetricsManager.AfferentElem);
            afferentElem.setAttribute(MetricsManager.FqnAttr, name);
            classElem.appendChild(afferentElem);
        }
        mclass.sort(mclass.getEfferentClassNames());
        for (String name : mclass.getEfferentClassNames()) {
            Element efferentElem = doc.createElement(MetricsManager.EfferentElem);
            efferentElem.setAttribute(MetricsManager.FqnAttr, name);
            classElem.appendChild(efferentElem);
        }
        
        exportMetricAttributes(doc, classElem, mclass.getMetricValues());
        
        mclass.sortMethods();
        for (MethodMetrics mm : mclass.getMethodMetrics()) {
            export(doc, classElem, mm);
        }
        mclass.sortFields();
        for (FieldMetrics fm : mclass.getFieldMetrics()) {
            export(doc, classElem, fm);
        }
    }
    
    /**
     * Exports metric values with respect to a method.
     * @param doc the content of the DOM instance
     * @param parent the parent DOM element
     * @param mmethod method metrics
     */
    private static void export(Document doc, Element parent, MethodMetrics mmethod) {
        Element methodElem = doc.createElement(MetricsManager.MethodElem);
        methodElem.setAttribute(MetricsManager.NameAttr, mmethod.getName());
        methodElem.setAttribute(MetricsManager.SignatureAttr, mmethod.getSignature());
        methodElem.setAttribute(MetricsManager.TypeAttr, mmethod.getReturnType());
        methodElem.setAttribute(MetricsManager.ModifiersAttr, String.valueOf(mmethod.getModifiers()));
        methodElem.setAttribute(MetricsManager.isConstructorAttr, getBoolean(mmethod.isConstructor()));
        methodElem.setAttribute(MetricsManager.isInitializerAttr, getBoolean(mmethod.isInitializer()));
        parent.appendChild(methodElem);
        
        exportCodeAttributes(doc, methodElem, mmethod.getJavaMethod());
        
        exportMetricAttributes(doc, methodElem, mmethod.getMetricValues());
        
    }
    
    /**
     * Exports metric values with respect to a field.
     * @param doc the content of the DOM instance
     * @param parent the parent DOM element
     * @param mfield field metrics
     */
    private static void export(Document doc, Element parent, FieldMetrics mfield) {
        Element fieldElem = doc.createElement(MetricsManager.FieldElem);
        fieldElem.setAttribute(MetricsManager.NameAttr, mfield.getName());
        fieldElem.setAttribute(MetricsManager.TypeAttr, mfield.getType());
        fieldElem.setAttribute(MetricsManager.ModifiersAttr, String.valueOf(mfield.getModifiers()));
        fieldElem.setAttribute(MetricsManager.isEnumConstantAttr, getBoolean(mfield.isEnumConstant()));
        parent.appendChild(fieldElem);
        
        exportCodeAttributes(doc, fieldElem, mfield.getJavaField());
        
        exportMetricAttributes(doc, fieldElem, mfield.getMetricValues());
    }
    
    /**
     * Exports code information
     * @param doc the content of the DOM instance
     * @param parent the parent DOM element
     * @param metrics the metric values to be exported
     */
    private static void exportCodeAttributes(Document doc, Element parent, JavaElement jelem) {
        Element codeElem = doc.createElement(MetricsManager.CodeElem);
        codeElem.setAttribute(MetricsManager.StartPositionAttr, String.valueOf(jelem.getStartPosition()));
        //codeElem.setAttribute(MetricsManager.ExtendedStartPositionAttr, String.valueOf(jelem.getExtendedStartPosition()));
        codeElem.setAttribute(MetricsManager.CodeLengthAttr, String.valueOf(jelem.getCodeLength()));
        //codeElem.setAttribute(MetricsManager.ExtendedCodeLengthAttr, String.valueOf(jelem.getExtendedCodeLength()));
        codeElem.setAttribute(MetricsManager.UpperLineNumberAttr, String.valueOf(jelem.getUpperLineNumber()));
        //codeElem.setAttribute(MetricsManager.ExtendedUpperLineNumberAttr, String.valueOf(jelem.getUpperLineNumber()));
        codeElem.setAttribute(MetricsManager.BottomLineNumberAttr, String.valueOf(jelem.getBottomLineNumber()));
        //codeElem.setAttribute(MetricsManager.ExtendedBottomLineNumberAttr, String.valueOf(jelem.getBottomLineNumber()));
        parent.appendChild(codeElem);
    }
    
    /**
     * Exports metric values
     * @param doc the content of the DOM instance
     * @param parent the parent DOM element
     * @param metrics the metric values to be exported
     * @return the string of the attributes
     */
    private static void exportMetricAttributes(Document doc, Element parent, Map<String, Double> metrics) {
        Element metricsElem = doc.createElement(MetricsManager.MetricsElem);
        for (String sort : metrics.keySet()) {
            double value = metrics.get(sort).doubleValue();
            metricsElem.setAttribute(sort, String.valueOf(value));
        }
        parent.appendChild(metricsElem);
    }
    
    /**
     * Converts the value of an attribute into suitable one.
     * @param str the original string to be stored into an attribute
     * @return the text suitable for the attribute value
     */
    @SuppressWarnings("unused")
    private static String convert(String str) {
        if (str == null || str.length() == 0) {
            return str;
        }
        
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);
            
            if (ch == '&') {
                buf.append("&amp;");
                
            } else if (ch == '<') {
                buf.append("&lt;");
                
            } else if (ch == '>') {
                buf.append("&gt;");
                
            } else if (ch == '\'') {
                 buf.append("&apos;");
                 
            } else if (ch == '"') {
                buf.append("&quot;");
                
            } else {
                buf.append(ch);
            }
        }
        return buf.toString();
    }
    
    /**
     * Converts a boolean value into a boolean string.
     * @param bool the boolean value
     * @return the boolean string
     */
    private static String getBoolean(boolean bool) {
        if (bool) {
            return MetricsManager.Yes;
        }
        return MetricsManager.No;
    }
}
