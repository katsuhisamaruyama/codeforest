/*
 *  Copyright 2014, Katsuhisa Maruyama (maru@jtool.org)
 */

package org.jtool.codeforest.ui.view;

import org.eclipse.swt.graphics.Image;

/**
 * Stores property data displaying on a property view.
 * @author Katsuhisa Maruyama
 */
public class PropertyData {
    
    private final String name;
    
    private final String value;
    
    private final boolean isClass; 
    
    private final Image image;
    
    PropertyData(String name, String value, boolean isClass, Image image) {
        this.name = name;
        this.value = value;
        this.isClass = isClass;
        this.image = image;
    }
    
    PropertyData(String name, Double value) {
        this.name = name;
        this.value = Double.toString(value);
        this.isClass = false;
        this.image = null;
    }
    
    PropertyData(String name, double value) {
        this.name = name;
        this.value = Double.toString(value);
        this.isClass = false;
        this.image = null;
    }
    
    PropertyData(String name, boolean value) {
        this.name = name;
        this.value = Boolean.toString(value);
        this.isClass = false;
        this.image = null;
    }
    
    String getName() {
        return name;
    }
    
    String getValue() {
        return value;
    }
    
    boolean isClass() {
        return isClass;
    }
    
    Image getImage() {
        return image;
    }
}
