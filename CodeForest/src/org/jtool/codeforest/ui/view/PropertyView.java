/*
 *  Copyright 2014, Katsuhisa Maruyama (maru@jtool.org)
 */

package org.jtool.codeforest.ui.view;

import org.jtool.codeforest.Activator;
import org.jtool.eclipse.model.java.JavaClass;
import org.jtool.eclipse.model.java.JavaField;
import org.jtool.eclipse.model.java.JavaMethod;
import org.jtool.codeforest.metrics.java.ClassMetrics;
import org.jtool.codeforest.metrics.java.FieldMetrics;
import org.jtool.codeforest.metrics.java.MethodMetrics;
import org.jtool.codeforest.metrics.java.PackageMetrics;
import org.jtool.codeforest.metrics.java.ProjectMetrics;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

/**
 * Provides a property view that lists a metric values.
 * @author Katsuhisa Maruyama
 */
public class PropertyView {
    
    private Font font11;
    
    private TableViewer tableViewer;
    
    private List<PropertyData> propertyList = new ArrayList<PropertyData>();
    
    public PropertyView(Composite parent, ProjectMetrics mproject) {
        createPropertyData(mproject);
        createPane(parent);
    }
    
    public PropertyView(Composite parent) {
        createPane(parent);
    }
    
    public void dispose() {
        font11.dispose();
        
        tableViewer = null;
        
        propertyList.clear();
    }
    
    private void createPane(Composite parent) {
        font11 = new Font(parent.getDisplay(), "", 11, SWT.NORMAL);
        
        parent.setLayout(new FillLayout());
        
        tableViewer = new TableViewer(parent, SWT.BORDER | SWT.SINGLE);
        tableViewer.setContentProvider(new ArrayContentProvider());
        tableViewer.setLabelProvider(new PropertyLabelProvider());
        
        tableViewer.getTable().setLinesVisible(true);
        tableViewer.getTable().setHeaderVisible(true);
        tableViewer.getTable().setFont(font11);
        
        TableColumn nameColumn = new TableColumn(tableViewer.getTable(), SWT.LEFT);
        nameColumn.setText("Name");
        nameColumn.setWidth(250);
        nameColumn.setResizable(true);
        
        TableColumn valueColumn = new TableColumn(tableViewer.getTable(), SWT.RIGHT);
        valueColumn.setText("Value");
        valueColumn.setWidth(60);
        valueColumn.setResizable(true);
        
        tableViewer.setInput(propertyList);
        
        parent.pack();
    }
    
    private class PropertyLabelProvider extends LabelProvider implements ITableLabelProvider {
        
        public String getColumnText(Object obj, int columnIndex) {
            if (obj instanceof PropertyData) {
                PropertyData data = (PropertyData)obj;
                if (columnIndex == 0) {
                    if (data.getImage() != null) {
                        return data.getName();
                    } else {
                        return "      " + data.getName();
                    }
                } else if (columnIndex == 1) {
                    return data.getValue();
                }
            }
            return null;
        }
        
        public Image getColumnImage(Object obj, int columnIndex) {
            if (obj instanceof PropertyData) {
                PropertyData data = (PropertyData)obj;
                if (columnIndex == 0) {
                    return data.getImage();
                }
            }
            return null;
        }
    }
    
    private void createPropertyData(ProjectMetrics mproject) {
        Image image = Activator.getImage("project");
        propertyList.add(new PropertyData(mproject.getName(), "", false, image));
        
        List<PropertyData> properties = new ArrayList<PropertyData>();
        Map<String, Double> metrics = mproject.getMetricValues();
        for (String name : metrics.keySet()) {
            Double value = metrics.get(name);
            properties.add(new PropertyData(name, value));
        }
        
        sort(properties);
        propertyList.addAll(properties);
        
        for (PackageMetrics mpackage : mproject.getPackageMetrics()) {
            createPackagePropertyData(mpackage);
        }
    }
    
    private void createPackagePropertyData(PackageMetrics mpackage) {
        Image image = Activator.getImage("project");
        propertyList.add(new PropertyData(mpackage.getName(), "", false, image));
        
        List<PropertyData> properties = new ArrayList<PropertyData>();
        Map<String, Double> metrics = mpackage.getMetricValues();
        for (String name : metrics.keySet()) {
            Double value = metrics.get(name);
            properties.add(new PropertyData(name, value));
        }
        
        sort(properties);
        propertyList.addAll(properties);
        
        for (ClassMetrics mclass : mpackage.getClassMetrics()) {
            createClassPropertyData(mclass);
        }
    }
    
    private void createClassPropertyData(ClassMetrics mclass) {
        if (mclass.isInterface()) {
            Image image = Activator.getImage("interface");
            propertyList.add(new PropertyData(mclass.getQualifiedName(), "", true, image));
        } else if (mclass.isEnum()) {
            Image image = Activator.getImage("enum");
            propertyList.add(new PropertyData(mclass.getQualifiedName(), "", true, image));
        } else {
            Image image = Activator.getImage("class");
            propertyList.add(new PropertyData(mclass.getQualifiedName(), "", true, image));
        }
        
        propertyList.add(new PropertyData("Abstract", getBoolean(mclass.getJavaClass().isAbstract()), false, null));
        propertyList.add(new PropertyData("Visibility", getVisibility(mclass.getJavaClass()), false, null));
        
        List<PropertyData> properties = new ArrayList<PropertyData>();
        Map<String, Double> metrics = mclass.getMetricValues();
        for (String name : metrics.keySet()) {
            Double value = metrics.get(name);
            properties.add(new PropertyData(name, value));
        }
        
        sort(properties);
        propertyList.addAll(properties);
        
        for (FieldMetrics mfield : mclass.getFieldMetrics()) {
            createFieldPropertyData(mfield);
        }
        
        for (MethodMetrics mmethod : mclass.getMethodMetrics()) {
            createMethodPropertyData(mmethod);
        }
    }
    
    private String getVisibility(JavaClass jclass) {
        if (jclass.isPrivate()) {
            return "private";
        } else if (jclass.isProtected()) {
            return "protected";
        } else if (jclass.isPublic()) {
            return "public";
        } else if (jclass.isDefault()) {
            return "package";
        }
        return "";
    }
    
    private void createFieldPropertyData(FieldMetrics mfield) {
        String sig = mfield.getName() + " : " + mfield.getType();
        if (mfield.isEnumConstant()) {
            Image image = Activator.getImage("field_pri");
            propertyList.add(new PropertyData(sig, "", false, image));
        } else {
            Image image = Activator.getImage("field_pub");
            propertyList.add(new PropertyData(sig, "", false, image));
        }
        
        propertyList.add(new PropertyData("Final", getBoolean(mfield.getJavaField().isFinal()), false, null));
        propertyList.add(new PropertyData("Visibility", getVivibility(mfield.getJavaField()), false, null));
        
        List<PropertyData> properties = new ArrayList<PropertyData>();
        Map<String, Double> metrics = mfield.getMetricValues();
        for (String name : metrics.keySet()) {
            Double value = metrics.get(name);
            properties.add(new PropertyData(name, value));
        }
        
        sort(properties);
        propertyList.addAll(properties);
    }
    
    private String getVivibility(JavaField jfield) {
        if (jfield.isPrivate()) {
            return "private";
        } else if (jfield.isProtected()) {
            return "protected";
        } else if (jfield.isPublic()) {
            return "public";
        } else if (jfield.isDefault()) {
            return "package";
        }
        return "";
    }
    
    private void createMethodPropertyData(MethodMetrics mmethod) {
        String sig = mmethod.getSignature() + " : " + mmethod.getReturnType();
        if (mmethod.isConstructor()) {
            Image image = Activator.getImage("method_pri");
            propertyList.add(new PropertyData(sig, "", false, image));
        } else {
            Image image = Activator.getImage("method_pub");
            propertyList.add(new PropertyData(sig, "", false, image));
        }
        
        propertyList.add(new PropertyData("Abstract", getBoolean(mmethod.getJavaMethod().isAbstract()), false, null));
        propertyList.add(new PropertyData("Static", getBoolean(mmethod.getJavaMethod().isStatic()), false, null));
        propertyList.add(new PropertyData("Visibility", getVisibility(mmethod.getJavaMethod()), false, null));
        
        List<PropertyData> properties = new ArrayList<PropertyData>();
        Map<String, Double> metrics = mmethod.getMetricValues();
        for (String name : metrics.keySet()) {
            Double value = metrics.get(name);
            properties.add(new PropertyData(name, value));
        }
        
        sort(properties);
        propertyList.addAll(properties);
    }
    
    private String getVisibility(JavaMethod jmethod) {
        if (jmethod.isPrivate()) {
            return "private";
        } else if (jmethod.isProtected()) {
            return "protected";
        } else if (jmethod.isPublic()) {
            return "public";
        } else if (jmethod.isDefault()) {
            return "package";
        }
        return "";
    }
    
    private String getBoolean(boolean bool) {
        if (bool) {
            return "yes";
        } else {
            return "no";
        }
    }
    
    public void changeSelection(String className) {
        for (int i = 0; i < propertyList.size(); i++) {
            PropertyData data = propertyList.get(i);
            if (data.isClass() && data.getName().compareTo(className) == 0) {
                changeSelection(i);
                break;
            }
        }
    }
    
    private void changeSelection(final int index) {
        if (tableViewer.getTable() != null && !(tableViewer.getTable().isDisposed())) {
            tableViewer.getTable().getDisplay().syncExec(new Runnable() {
                
                public void run() {
                    try {
                        tableViewer.getTable().select(index);
                        tableViewer.getTable().setTopIndex(index);
                    } catch (Exception e) { /* empty */ }
                }
            });
        }
    }
    
    private void sort(List<PropertyData> ops) {
        Collections.sort(ops, new Comparator<PropertyData>() {
            public int compare(PropertyData d1, PropertyData d2) {
                String name1 = d1.getName();
                String name2 = d2.getName();
                return name1.compareTo(name2);
            }
        });
    }
}
