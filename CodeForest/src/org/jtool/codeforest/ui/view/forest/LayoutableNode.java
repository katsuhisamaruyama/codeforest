/*
 *  Copyright 2013, Katsuhisa Maruyama (maru@jtool.org)
 */

package org.jtool.codeforest.ui.view.forest;

import org.jtool.codeforest.ui.layout.LayoutAlgorithm;
import org.jtool.codeforest.ui.layout.IMapModel;
import org.jtool.codeforest.ui.layout.SimpleMappableItem;
import org.jtool.codeforest.ui.layout.Rect;

/**
 * Represents a layoutable node.
 * @author Daiki Todoroki
 * @author Katsuhisa Maruyama 
 */
public class LayoutableNode extends SimpleMappableItem {
    
    private static final LayoutAlgorithm layoutAlgorithm = new LayoutAlgorithm();
    
    protected double boxLeft, boxTop, boxRight, boxBottom;
    
    public LayoutableNode() {
    }
    
    public void setLayout(String name){
        layoutAlgorithm.setLayout(name);
    }
    
    public void layout(IMapModel model, Rect bounds) {
        layoutAlgorithm.layout(model, bounds);
    }
    
    public void calculateBox() {
        boxLeft = x;
        boxTop = y;
        boxRight = boxLeft + w;
        boxBottom = boxTop + h;
    }
    
    public double getWidth() {
        return Math.abs(this.boxLeft - this.boxRight);
    }
    
    public double getLength() {
        return Math.abs(this.boxTop - this.boxBottom);
    }
    
    public double getX() {
        return (boxLeft + boxRight) / 2;
    }
    
    public double getZ() {
        return (boxTop + boxBottom) / 2;
    }
}
