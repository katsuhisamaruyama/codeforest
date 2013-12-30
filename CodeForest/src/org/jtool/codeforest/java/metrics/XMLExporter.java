/*
 *  Copyright 2013, Katsuhisa Maruyama (maru@jtool.org)
 */

package org.jtool.codeforest.java.metrics;

import org.jtool.codeforest.java.JavaElement;
import java.io.File;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

/**
 * A class exporting metric values within a project.
 * @author Katsuhisa Maruyama
 *
 */
public class XMLExporter {
    
    /**
     * The contents to be exported.
     */
    private StringBuffer contents = new StringBuffer();
    
    /**
     * Creates a new, empty object.
     */
    public XMLExporter() {
    }
    
    /**
     * Returns the contents of the file to be exported.
     * @return the contents of the file
     */
    public String getContents() {
        return contents.toString();
    }
    
    /**
     * Exports metric values within a project.
     * @param file the file storing metric values
     * @param mproject the metrics for the project
     */
    public void export(File file, ProjectMetrics mproject) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            export(mproject);
            
            writer.write(contents.toString());
            writer.flush();
            writer.close();
        } catch (IOException e) { /* empty */ }
    }
    
    /**
     * Exports metric values with respect to a project.
     * @param mproject project metrics
     */
    public void export(ProjectMetrics mproject) {
        contents.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        contents.append("<" + MetricsManager.TopElem + ">\n");
        
        contents.append("  <" + MetricsManager.ProjectElem);
        contents.append(" " + MetricsManager.NameAttr + "=\"" + convert(mproject.getName()) + "\"");
        contents.append(">\n");
        
        contents.append("     ");
        contents.append(getMetricAttributes(mproject.getMetricValues()));
        
        for (PackageMetrics pm : mproject.getPackageMetrics()) {
            export(pm);
        }
        
        contents.append("  </" + MetricsManager.ProjectElem +">\n");
        
        contents.append("</" + MetricsManager.TopElem + ">\n");
    }
    
    /**
     * Exports metric values with respect to a package.
     * @param mpackage package metrics
     */
    private void export(PackageMetrics mpackage) {
        contents.append("    <" + MetricsManager.PackageElem);
        contents.append(" " + MetricsManager.NameAttr + "=\"" + convert(mpackage.getName()) + "\"");
        contents.append(">\n");
        
        for (String name : mpackage.getAfferentPackageNames()) {
            contents.append("         <" + MetricsManager.AfferentElem);
            contents.append(" " + MetricsManager.FqnAttr + "=\"" + convert(name) + "\"/>\n");
        }
        for (String name : mpackage.getEfferentPackageNames()) {
            contents.append("         <" + MetricsManager.EfferentElem);
            contents.append(" " + MetricsManager.FqnAttr + "=\"" + convert(name) + "\"/>\n");
        }
        
        contents.append("       ");
        contents.append(getMetricAttributes(mpackage.getMetricValues()));
        
        for (ClassMetrics cm : mpackage.getClassMetrics()) {
            export(cm);
        }
        
        contents.append("    </" + MetricsManager.PackageElem +">\n");
    }
    
    /**
     * Exports metric values with respect to a class.
     * @param mclass class metrics
     */
    private void export(ClassMetrics mclass) {
        contents.append("      <" + MetricsManager.ClassElem);
        
        contents.append(" " + MetricsManager.NameAttr + "=\"" + convert(mclass.getName()) + "\"");
        contents.append(" " + MetricsManager.FqnAttr + "=\"" + convert(mclass.getQualifiedName()) + "\"");
        contents.append(" " + MetricsManager.ModifiersAttr + "=\"" + String.valueOf(mclass.getModifiers()) + "\"");
        contents.append(" " + MetricsManager.isInterfaceAttr + "=\"" + getBoolean(mclass.isInterface()) + "\"");
        contents.append(" " + MetricsManager.isEnumAttr + "=\"" + getBoolean(mclass.isEnum()) + "\"");
        contents.append(" " + MetricsManager.PathAttr + "=\"" + convert(mclass.getPath()) + "\"");
        contents.append(">\n");
        
        contents.append("         ");
        contents.append(getCodeAttributes(mclass.getJavaClass()));
        
        contents.append("         <" + MetricsManager.SuperClassElem);
        contents.append(" " + MetricsManager.FqnAttr + "=\"" + convert(mclass.getSuperClassName()) + "\"/>\n");
        for (String name : mclass.getSuperInterfaceNames()) {
            contents.append("         <" + MetricsManager.SuperInterfaceElem);
            contents.append(" " + MetricsManager.FqnAttr + "=\"" + convert(name) + "\"/>\n");
        }
        
        for (String name : mclass.getAfferentClassNames()) {
            contents.append("         <" + MetricsManager.AfferentElem);
            contents.append(" " + MetricsManager.FqnAttr + "=\"" + convert(name) + "\"/>\n");
        }
        for (String name : mclass.getEfferentClassNames()) {
            contents.append("         <" + MetricsManager.EfferentElem);
            contents.append(" " + MetricsManager.FqnAttr + "=\"" + convert(name) + "\"/>\n");
        }
        
        contents.append("         ");
        contents.append(getMetricAttributes(mclass.getMetricValues()));
        
        for (MethodMetrics mm : mclass.getMethodMetrics()) {
            export(mm);
        }
        for (FieldMetrics fm : mclass.getFieldMetrics()) {
            export(fm);
        }
        
        contents.append("      </" + MetricsManager.ClassElem +">\n");
    }
    
    /**
     * Exports metric values with respect to a method.
     * @param mmethod method metrics
     */
    private void export(MethodMetrics mmethod) {
        contents.append("        <" + MetricsManager.MethodElem);
        
        contents.append(" " + MetricsManager.NameAttr + "=\"" + convert(mmethod.getName()) + "\"");
        contents.append(" " + MetricsManager.SignatureAttr + "=\"" + convert(mmethod.getSignature()) + "\"");
        contents.append(" " + MetricsManager.TypeAttr + "=\"" + convert(mmethod.getType()) + "\"");
        contents.append(" " + MetricsManager.ModifiersAttr + "=\"" + String.valueOf(mmethod.getModifiers()) + "\"");
        contents.append(" " + MetricsManager.isConstructorAttr + "=\"" + getBoolean(mmethod.isConstructor()) + "\"");
        contents.append(" " + MetricsManager.isInitializerAttr + "=\"" + getBoolean(mmethod.isInitializer()) + "\"");
        contents.append(">\n");
        
        contents.append("           ");
        contents.append(getCodeAttributes(mmethod.getJavaMethod()));
        
        contents.append("           ");
        contents.append(getMetricAttributes(mmethod.getMetricValues()));
        
        contents.append("        </" + MetricsManager.MethodElem +">\n");
    }
    
    /**
     * Exports metric values with respect to a field.
     * @param mfield field metrics
     */
    private void export(FieldMetrics mfield) {
        contents.append("        <" + MetricsManager.FieldElem);
        
        contents.append(" " + MetricsManager.NameAttr + "=\"" + convert(mfield.getName()) + "\"");
        contents.append(" " + MetricsManager.TypeAttr + "=\"" + convert(mfield.getType()) + "\"");
        contents.append(" " + MetricsManager.ModifiersAttr + "=\"" + String.valueOf(mfield.getModifiers()) + "\"");
        contents.append(" " + MetricsManager.isEnumConstantAttr + "=\"" + getBoolean(mfield.isEnumConstant()) + "\"");
        contents.append(">\n");
        
        contents.append("           ");
        contents.append(getCodeAttributes(mfield.getJavaField()));
        
        contents.append("           ");
        contents.append(getMetricAttributes(mfield.getMetricValues()));
        
        contents.append("        </" + MetricsManager.FieldElem +">\n");
    }
    
    /**
     * Obtains the string of attributes representing metric values
     * @param metrics the metric values to be exported
     * @return the string of the attributes
     */
    private StringBuffer getCodeAttributes(JavaElement jelem) {
        StringBuffer buf = new StringBuffer();
        buf.append("<" + MetricsManager.CodeElem);
        buf.append(" " + MetricsManager.StartPositionAttr + "=\"" + String.valueOf(jelem.getStartPosition()) + "\"");
        buf.append(" " + MetricsManager.CodeLengthAttr + "=\"" + String.valueOf(jelem.getCodeLength()) + "\"");
        buf.append(" " + MetricsManager.UpperLineNumberAttr + "=\"" + String.valueOf(jelem.getUpperLineNumber()) + "\"");
        buf.append(" " + MetricsManager.BottomLineNumberAttr+ "=\"" + String.valueOf(jelem.getBottomLineNumber()) + "\"");
        buf.append("/>\n");
        return buf;
    }
    
    /**
     * Obtains the string of attributes representing metric values
     * @param metrics the metric values to be exported
     * @return the string of the attributes
     */
    private StringBuffer getMetricAttributes(Map<String, Double> metrics) {
        StringBuffer buf = new StringBuffer();
        buf.append("<" + MetricsManager.MetricsElem);
        for (String sort : metrics.keySet()) {
            double value = metrics.get(sort).doubleValue();
            buf.append(" " + sort + "=\"" + String.valueOf(value) + "\"");
        }
        buf.append("/>\n");
        return buf;
    }
    
    /**
     * Converts the value of an attribute into suitable one.
     * @param str the original string to be stored into an attribute
     * @return the text suitable for the attribute value
     */
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
    private String getBoolean(boolean bool) {
        if (bool) {
            return MetricsManager.Yes;
        }
        return MetricsManager.No;
    }
}
