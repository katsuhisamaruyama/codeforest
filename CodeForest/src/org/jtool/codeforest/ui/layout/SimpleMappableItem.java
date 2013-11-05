/*
 *  Copyright 2013, Katsuhisa Maruyama (maru@jtool.org)
 */

package org.jtool.codeforest.ui.layout;

/**
 * Representing a mappable item.
 * @author Daiki Todoroki
 * @author Katsuhisa Maruyama
 */
public class SimpleMappableItem implements IMappable {
    
    protected double baseSize;
    
    protected Rect bounds;
    
    protected int order = 0;
    
    protected int depth;
    
    protected float x, y;
    
    protected float w, h;
    
    public SimpleMappableItem() {
        this(1, 0);
    }
    
    public SimpleMappableItem(double baseSize, int order) {
        this.baseSize = baseSize;
        this.order = order;
        bounds = new Rect();
    }
    
    public double getBaseSize() {
        return baseSize;
    }
    
    public void setBaseSize(double size) {
        this.baseSize = size;
    }
    
    public void incrementBaseSize() {
        baseSize++;
    }
    
    public Rect getBounds() {
        return bounds;
    }
    
    public void setBounds(Rect bounds) {
        this.bounds = bounds;
        
        x = (float) bounds.x;
        y = (float) bounds.y;
        w = (float) bounds.w;
        h = (float) bounds.h;
    }
    
    public void setBounds(double bx, double by, double bw, double bh) {
        setBounds(new Rect(bx, by, bw, bh));
    }
    
    public int getOrder() {
        return order;
    }
    
    public void setOrder(int order) {
        this.order = order;
    }
    
    public void setDepth(int depth) {
        this.depth = depth;
    }
    
    public int getDepth() {
        return depth;
    }
}
