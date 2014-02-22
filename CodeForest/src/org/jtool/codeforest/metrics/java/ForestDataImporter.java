/*
 *  Copyright 2014, Katsuhisa Maruyama (maru@jtool.org)
 */

package org.jtool.codeforest.metrics.java;

import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import java.io.File;

/**
 * A class importing metric values within a project from an XML file.
 * @author Katsuhisa Maruyama
 */
public class ForestDataImporter extends DefaultHandler {
    
    /**
     * The path name of the project to be imported.
     */
    private String path;
    
    /**
     * The current project metrics.
     */
    private ProjectMetrics projectMetrics;
    
    /**
     * The current package metrics.
     */
    private PackageMetrics packageMetrics ;
    
    /**
     * The current class metrics.
     */
    private ClassMetrics classMetrics;
    
    /**
     * The current method metrics.
     */
    private MethodMetrics methodMetrics;
    
    /**
     * The current field metrics.
     */
    private FieldMetrics fieldMetrics;
    
    /**
     * The parent of the current metrics.
     */
    private CommonMetrics parent;
    
    /**
     * Creates a new, empty object.
     * @path the path of the project to be imported
     */
    public ForestDataImporter(String path) {
        this.path = path;
    }
    
    /**
     * Receive notification of the beginning of the document.
     */
    public void startDocument() throws SAXException {
    }
    
    /**
     * Receive notification of the end of the document.
     */
    public void endDocument() throws SAXException {
        projectMetrics.collectMetricsInfoAfterXMLImport();
    }
    
    /**
     * Receive notification of the start of an element.
     * @param uri the namespace's URI
     * @param name the local name without prefix
     * @param qname the qualified name with prefix
     * @param attrs the attributes attached to the element
     */
    public void startElement(String uri, String name, String qname, Attributes attrs) {
        if (qname.equals(MetricsManager.ProjectElem)) {
            setProjectAttributes(attrs);
            parent = projectMetrics;
            return;
        }
        
        if (qname.equals(MetricsManager.PackageElem)) {
            setPackageAttributes(attrs);
            parent = packageMetrics;
            return;
        }
        
        if (qname.equals(MetricsManager.ClassElem)) {
            setClassAttributes(attrs);
            parent = classMetrics;
            return;
        }
        
        if (qname.equals(MetricsManager.MethodElem)) {
            setMethodAttributes(attrs);
            parent = methodMetrics;
            return;
        }
        
        if (qname.equals(MetricsManager.FieldElem)) {
            setFieldAttributes(attrs);
            parent = fieldMetrics;
            return;
        }
        
        if (qname.equals(MetricsManager.SuperClassElem)) {
            if (parent == classMetrics) {
                if (attrs.getQName(0).equals(MetricsManager.FqnAttr)) {
                    classMetrics.setSuperClassName(attrs.getValue(0));
                }
            }
            return;
        }
        
        if (qname.equals(MetricsManager.SuperInterfaceElem)) {
            if (parent == classMetrics) {
                if (attrs.getQName(0).equals(MetricsManager.FqnAttr)) {
                    classMetrics.addSuperInterfaceName(attrs.getValue(0));
                }
            }
            return;
        }
        
        if (qname.equals(MetricsManager.AfferentElem)) {
            if (parent == packageMetrics) {
                if (attrs.getQName(0).equals(MetricsManager.FqnAttr)) {
                    packageMetrics.addAfferentPackageName(attrs.getValue(0));
                }
            } else if (parent == classMetrics) {
                if (attrs.getQName(0).equals(MetricsManager.FqnAttr)) {
                    classMetrics.addAfferentClassName(attrs.getValue(0));
                }
            }
            return;
        }
        
        if (qname.equals(MetricsManager.EfferentElem)) {
            if (parent == packageMetrics) {
                if (attrs.getQName(0).equals(MetricsManager.FqnAttr)) {
                    packageMetrics.addEfferentPackageName(attrs.getValue(0));
                }
            } else if (parent == classMetrics) {
                if (attrs.getQName(0).equals(MetricsManager.FqnAttr)) {
                    classMetrics.addEfferentClassName(attrs.getValue(0));
                }
            }
            return;
        }
        
        if (qname.equals(MetricsManager.MetricsElem)) {
            setMetricAttributes(attrs);
            return;
        }
        
        if (qname.equals(MetricsManager.CodeElem)) {
            setCodeAttributes(attrs);
            return;
        }
    }
    
    /**
     * Receive notification of the end of an element.
     * @param uri the namespace's URI
     * @param name the local name without prefix
     * @param qname the qualified name with prefix
     */
    public void endElement(String uri, String name, String qname) {
        if (qname.equals(MetricsManager.ProjectElem)) {
            parent = null;
            return;
        }
        
        if (qname.equals(MetricsManager.PackageElem)) {
            parent = projectMetrics;
            return;
        }
        
        if (qname.equals(MetricsManager.ClassElem)) {
            parent = packageMetrics;
            return;
        }
        
        if (qname.equals(MetricsManager.MethodElem)) {
            parent = classMetrics;
            return;
        }
        
        if (qname.equals(MetricsManager.FieldElem)) {
            parent = classMetrics;
            return;
        }
    }
    
    /**
     * Sets attributes for a project.
     * @param attrs the collection of attributes
     */
    private void setProjectAttributes(Attributes attrs) {
        String name = null;
        long time = -1;
        
        for (int i = 0; i < attrs.getLength(); i++) {
            if (attrs.getQName(i).equals(MetricsManager.NameAttr)) {
                name = attrs.getValue(i);
            } else if (attrs.getQName(i).equals(MetricsManager.TimeAttr)) {
                time = getLong(attrs.getValue(i));
            }
            
        }
        
        if (name != null && time > 0) {
            int lindex = path.lastIndexOf(File.separatorChar);
            String projectPath = path.substring(0, lindex);
            projectMetrics = new ProjectMetrics(name, projectPath, time);
        }
    }
    
    /**
     * Sets attributes for a package.
     * @param attrs the collection of attributes
     */
    private void setPackageAttributes(Attributes attrs) {
        String name = null;
        
        for (int i = 0; i < attrs.getLength(); i++) {
            if (attrs.getQName(i).equals(MetricsManager.NameAttr)) {
                name = attrs.getValue(i);
            }
        }
        
        if (name != null) {
            packageMetrics = new PackageMetrics(name, projectMetrics);
            projectMetrics.add(packageMetrics);
        }
    }
    
    /**
     * Sets attributes for a class.
     * @param attrs the collection of attributes
     */
    private void setClassAttributes(Attributes attrs) {
        String name = null;
        String fqn = null;
        int modifiers = 0;
        boolean isInterface = false;
        boolean isEnum = false;
        String path = null;
        
        for (int i = 0; i < attrs.getLength(); i++) {
            if (attrs.getQName(i).equals(MetricsManager.NameAttr)) {
                name = attrs.getValue(i);
            } else if (attrs.getQName(i).equals(MetricsManager.FqnAttr)) {
                fqn = attrs.getValue(i);
            } else if (attrs.getQName(i).equals(MetricsManager.ModifiersAttr)) {
                modifiers = getInteger(attrs.getValue(i));
            } else if (attrs.getQName(i).equals(MetricsManager.isInterfaceAttr)) {
                isInterface = getBoolean(attrs.getValue(i));
            } else if (attrs.getQName(i).equals(MetricsManager.isEnumAttr)) {
                isEnum = getBoolean(attrs.getValue(i));
            } else if (attrs.getQName(i).equals(MetricsManager.PathAttr)) {
                path = attrs.getValue(i);
            }
        }
        
        if (name != null && fqn != null) {
            classMetrics = new ClassMetrics(name, fqn, modifiers, isInterface, isEnum, path, packageMetrics);
            packageMetrics.add(classMetrics);
        }
    }
    
    /**
     * Sets attributes for a method.
     * @param attrs the collection of attributes
     */
    private void setMethodAttributes(Attributes attrs) {
        String name = null;
        String sig = null;
        String type = null;
        int modifiers = 0;
        boolean isConstructor = false;
        boolean isInitializer = false;
        
        for (int i = 0; i < attrs.getLength(); i++) {
            if (attrs.getQName(i).equals(MetricsManager.NameAttr)) {
                 name = attrs.getValue(i);
            } else if (attrs.getQName(i).equals(MetricsManager.SignatureAttr)) {
                sig = attrs.getValue(i);
            } else if (attrs.getQName(i).equals(MetricsManager.TypeAttr)) {
                type = attrs.getValue(i);
            } else if (attrs.getQName(i).equals(MetricsManager.ModifiersAttr)) {
                modifiers = getInteger(attrs.getValue(i));
            } else if (attrs.getQName(i).equals(MetricsManager.isConstructorAttr)) {
                isConstructor = getBoolean(attrs.getValue(i));
            } else if (attrs.getQName(i).equals(MetricsManager.isInitializerAttr)) {
                isInitializer = getBoolean(attrs.getValue(i));
            }
        }
        
        if (sig != null) {
            methodMetrics = new MethodMetrics(name, sig, type, modifiers, isConstructor, isInitializer, classMetrics);
            classMetrics.add(methodMetrics);
        }
    }
    
    /**
     * Sets attributes for a field.
     * @param attrs the collection of attributes
     */
    private void setFieldAttributes(Attributes attrs) {
        String name = null;
        String type = null;
        int modifiers = 0;
        boolean isEnumConstant = false;
        
        for (int i = 0; i < attrs.getLength(); i++) {
            if (attrs.getQName(i).equals(MetricsManager.NameAttr)) {
                name = attrs.getValue(i);
            } else if (attrs.getQName(i).equals(MetricsManager.TypeAttr)) {
                type = attrs.getValue(i);
            } else if (attrs.getQName(i).equals(MetricsManager.ModifiersAttr)) {
                modifiers = getInteger(attrs.getValue(i));
            } else if (attrs.getQName(i).equals(MetricsManager.isEnumConstantAttr)) {
                isEnumConstant = getBoolean(attrs.getValue(i));
            }
        }
        
        if (name != null) {
            fieldMetrics = new FieldMetrics(name, type, modifiers, isEnumConstant, classMetrics);
            classMetrics.add(fieldMetrics);
        }
    }
    
    /**
     * Sets attributes for metrics with respect to an element.
     * @param attrs the collection of attributes
     */
    private void setMetricAttributes(Attributes attrs) {
        if (parent == null) {
            return;
        }
        
        for (int i = 0; i < attrs.getLength(); i++) {
            parent.putMetricValue(attrs.getQName(i), Double.parseDouble(attrs.getValue(i)));
        }
    }
    
    /**
     * Sets attributes for code within an element.
     * @param attrs the collection of attributes
     */
    private void setCodeAttributes(Attributes attrs) {
        if (parent == null) {
            return;
        }
        
        int start = 0;
        int len = 0;
        int upper = 0;
        int bottom = 0;
        
        for (int i = 0; i < attrs.getLength(); i++) {
            if (attrs.getQName(i).equals(MetricsManager.StartPositionAttr)) {
                start = getInteger(attrs.getValue(i));
            } else if (attrs.getQName(i).equals(MetricsManager.CodeLengthAttr)) {
                len = getInteger(attrs.getValue(i));
            } else if (attrs.getQName(i).equals(MetricsManager.UpperLineNumberAttr)) {
                upper = getInteger(attrs.getValue(i));
            } else if (attrs.getQName(i).equals(MetricsManager.BottomLineNumberAttr)) {
                bottom = getInteger(attrs.getValue(i));
            }
        }
        
        parent.setCodeProperties(start, len, upper, bottom);
    }
    
    /**
     * Returns the project metrics created during this parsing.
     * @return the project metrics
     */
    public ProjectMetrics getProjectMetrics() {
        return projectMetrics;
    }
    
    /**
     * Converts a boolean string into a boolean value.
     * @param value the boolean string
     * @return the boolean value
     */
    private boolean getBoolean(String value) {
        return value.equals(MetricsManager.Yes);
    }
    
    /**
     * Converts an integer string into an integer value.
     * @param value the integer string
     * @return the integer value
     */
    private int getInteger(String value) {
        return Integer.parseInt(value);
    }
    
    /**
     * Converts an integer string into a long value.
     * @param value the long string
     * @return the long value
     */
    private long getLong(String value) {
        return Long.parseLong(value);
    }
}
