/*
 *  Copyright 2014, Katsuhisa Maruyama (maru@jtool.org)
 */

package org.jtool.codeforest.ui.shape;

import javax.media.j3d.Appearance;
import javax.media.j3d.Geometry;
import javax.media.j3d.Shape3D;
import javax.media.j3d.TransformGroup;

/**
 * Represents a fractal shape.
 * @author Katsuhisa Maruyama
 */
public class FractalShape extends TransformGroup {
    
    protected Shape3D shape;
    
    protected FractalShape() {
        shape = new Shape3D();
        setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
    }
    
    public void setGeometry(Geometry g) {
        shape.setGeometry(g);
    }
    
    public Appearance getAppearance() {
        return shape.getAppearance();
    }
    
    public void setAppearance(Appearance a) {
        shape.setAppearance(a);
    }
}
