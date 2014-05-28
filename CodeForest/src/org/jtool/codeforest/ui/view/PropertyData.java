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
    
    /**
     * The name of the property data
     */
    private final String name;
    
    /**
     * The value of the property data
     */
    private final String value;
    
    /**
     * A flag indicating if this property data is related to a class or not
     */
    private final boolean isClass; 
    
    /**
     * The image corresponding to this property data.
     */
    private final Image image;
    
    /**
     * Creates a property data.
     * @param name the name of the property data
     * @param value the value of the property data
     * @param isClass <code>true</code> if this property data is related to a class, otherwise <code>false</code>
     * @param image the image corresponding to this property data
     */
    PropertyData(String name, String value, boolean isClass, Image image) {
        this.name = name;
        this.value = value;
        this.isClass = isClass;
        this.image = image;
    }
    
    /**
     * Creates a property data.
     * @param name the name of the property data
     * @param value the value of the property data
     */
    PropertyData(String name, Double value) {
        this.name = name;
        this.value = Double.toString(value);
        this.isClass = false;
        this.image = null;
    }
    
    /**
     * Creates a property data.
     * @param name the name of the property data
     * @param value the value of the property data
     */
    PropertyData(String name, double value) {
        this.name = name;
        this.value = Double.toString(value);
        this.isClass = false;
        this.image = null;
    }
    
    /**
     * Creates a property data.
     * @param name the name of the property data
     * @param value the value of the property data
     */
    PropertyData(String name, boolean value) {
        this.name = name;
        this.value = Boolean.toString(value);
        this.isClass = false;
        this.image = null;
    }
    
    /**
     * Returns the name of this property data.
     * @return the name of the property data
     */
    String getName() {
        return name;
    }
    
    /**
     * Returns the value of this property data.
     * @return the value of the property data
     */
    String getValue() {
        return value;
    }
    
    /**
     * Checks if this property data is related to a class.
     * @return <code>true</code> if this property data is related to a class, otherwise <code>false</code>
     */
    boolean isClass() {
        return isClass;
    }
    
    /**
     * Returns the image corresponding to this property data.
     * @return the image
     */
    Image getImage() {
        return image;
    }
}
