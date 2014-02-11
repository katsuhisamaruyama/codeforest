/*
 *  Copyright 2014, Katsuhisa Maruyama (maru@jtool.org)
 */

package org.jtool.codeforest.ui.layout;

/**
 * Represents a layoutable node.
 * @author Daiki Todoroki
 * @author Katsuhisa Maruyama 
 */
public class LayoutableNode extends MappableItem {
    
    protected double boxLeft, boxTop, boxRight, boxBottom;
    
    public LayoutableNode() {
    }
    
    public void setLayout(IMapModel model, Rect bounds) {
        MapLayout layout = new Squarified();
        layout.layout(model, bounds);
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
