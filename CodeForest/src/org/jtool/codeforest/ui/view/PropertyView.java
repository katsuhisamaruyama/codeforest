/*
 *  Copyright 2013, Katsuhisa Maruyama (maru@jtool.org)
 */

package org.jtool.codeforest.ui.view;

import org.jtool.codeforest.Activator;
import org.jtool.codeforest.java.JavaClass;
import org.jtool.codeforest.java.JavaField;
import org.jtool.codeforest.java.JavaMethod;
import org.jtool.codeforest.java.metrics.ClassMetrics;
import org.jtool.codeforest.java.metrics.CommonMetrics;
import org.jtool.codeforest.java.metrics.FieldMetrics;
import org.jtool.codeforest.java.metrics.MethodMetrics;
import org.jtool.codeforest.java.metrics.PackageMetrics;
import org.jtool.codeforest.java.metrics.ProjectMetrics;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
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
    
    private Table propertyTable;
    
    private List<PropertyData> propertyList = new ArrayList<PropertyData>();
    
    public PropertyView(Composite parent, ProjectMetrics mproject) {
        createPropertyData(mproject);
        createPane(parent);
    }
    
    public PropertyView(Composite parent) {
        createPane(parent);
    }
    
    public void dispose() {
        font11 = null;
        propertyTable = null;
        propertyList.clear();
        propertyList = null;
    }
    
    private void createPane(Composite parent) {
        font11 = new Font(parent.getDisplay(), "", 11, SWT.NORMAL);
        
        parent.setLayout(new FillLayout());
        
        propertyTable = new Table(parent, SWT.BORDER | SWT.SINGLE);
        propertyTable.setLinesVisible(true);
        propertyTable.setHeaderVisible(true);
        propertyTable.setFont(font11);
        
        TableColumn nameColumn = new TableColumn(propertyTable, SWT.LEFT);
        nameColumn.setText("Name");
        nameColumn.setWidth(250);
        nameColumn.setResizable(true);
        
        TableColumn valueColumn = new TableColumn(propertyTable, SWT.RIGHT);
        valueColumn.setText("Value");
        valueColumn.setWidth(60);
        valueColumn.setResizable(true);
        
        for (int i = 0; i < propertyList.size(); i++) {
            PropertyData data = propertyList.get(i);
            
            TableItem item = new TableItem(propertyTable, SWT.NONE);
            if (data.getImage() != null) {
                item.setImage(data.getImage());
                item.setText(0, data.getName());
            } else {
                item.setText(0, "        " + data.getName());
            }
            item.setText(1, data.getValue());
            
        }
        
        parent.pack();
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
        String sig = mmethod.getSignature() + " : " + mmethod.getType();
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
    
    public void setMetrics(CommonMetrics metrics) {
        if (!(metrics instanceof ClassMetrics)) {
            return;
        }
        
        ClassMetrics mclass = (ClassMetrics)metrics;
        changeSelection(mclass.getQualifiedName());
    }
    
    private void changeSelection(String name) {
        for (int i = 0; i < propertyList.size(); i++) {
            PropertyData data = propertyList.get(i);
            if (data.isClass() && data.getName().compareTo(name) == 0) {
                changeSelection(i);
                break;
            }
        }
    }
    
    private void changeSelection(final int index) {
        if (propertyTable != null && !(propertyTable.isDisposed())) {
            propertyTable.getDisplay().syncExec(new Runnable() {
                
                public void run() {
                    try {
                        propertyTable.select(index);
                        propertyTable.setTopIndex(index);
                        // reveal(index);
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
