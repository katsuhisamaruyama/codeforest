/*
 *  Copyright 2014, Katsuhisa Maruyama (maru@jtool.org)
 */

package org.jtool.codeforest.ui.shape;

import javax.media.j3d.Appearance;
import javax.media.j3d.Geometry;
import javax.media.j3d.Shape3D;
import javax.media.j3d.TransformGroup;

/**
 * Represents a visual object displayed on a tree view.
 * @author Katsuhisa Maruyama
 * @author Daiki Todoroki
 */
public class FractalShape extends TransformGroup {
    
    /**
     * The visual object displayed on a tree view.
     */
    protected Shape3D shape;
    
    /**
     * Creates a visual object.
     */
    protected FractalShape() {
        shape = new Shape3D();
        setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
    }
    
    /**
     * Sets the geometry of a visual object.
     * @param g the geometry of the visual object
     */
    public void setGeometry(Geometry g) {
        shape.setGeometry(g);
    }
    
    /**
     * Returns the appearance of a visual object.
     * @return the appearance of the visual object
     */
    public Appearance getAppearance() {
        return shape.getAppearance();
    }
    
    /**
     * Sets the appearance of a visual object.
     * @param a the appearance of the visual object
     */
    public void setAppearance(Appearance a) {
        shape.setAppearance(a);
    }
}
