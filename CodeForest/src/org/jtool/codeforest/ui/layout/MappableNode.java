/*
 *  Copyright 2014, Katsuhisa Maruyama (maru@jtool.org)
 */

package org.jtool.codeforest.ui.layout;

/**
 * Representing a mappable node.
 * @author Katsuhisa Maruyama
 * @author Daiki Todoroki
 */
public class MappableNode implements IMappableNode {
    
    /**
     * The base size of this mappable node.
     */
    protected double baseSize;
    
    /**
     * The area containing this mappable node.
     */
    protected RectArea area;
    
    /**
     * The index value indicating the order of this mappable node.
     */
    protected int order = 0;
    
    /**
     * The depth of this mappbale node.
     */
    protected int depth;
    
    /**
     * Creates a default mappable node.
     */
    public MappableNode() {
        this(1, 0);
    }
    
    /**
     * Creates a mappable node.
     * @param baseSize the base size of the mappable node
     * @param order the index value indicating the order of the mappable node
     */
    public MappableNode(double baseSize, int order) {
        this.baseSize = baseSize;
        this.order = order;
        area = new RectArea();
    }
    
    /**
     * Returns the base size of this mappable node.
     * @return the base size of the mappable node
     */
    public double getBaseSize() {
        return baseSize;
    }
    
    /**
     * Sets the base size of this mappable node.
     * @param size the base size of the mappable node
     */
    public void setBaseSize(double size) {
        this.baseSize = size;
    }
    
    /**
     * Increments the value of base size of this mappable node.
     */
    public void incrementBaseSize() {
        baseSize++;
    }
    
    /**
     * Returns the area containing this mappable node.
     * @return the area containing the mappable node
     */
    public RectArea getBounds() {
        return area;
    }
    
    /**
     * Sets the area containing this mappable node.
     * @param area the area containing the mappable node
     */
    public void setBounds(RectArea area) {
        this.area = area;
    }
    
    /**
     * Sets the bounds of this mappable node.
     * @param x the x-position of the mappable node
     * @param y the y-position of the mappable node
     * @param width the width of the mappable node
     * @param height the height of the mappable node
     */
    public void setBounds(double x, double y, double width, double height) {
        setBounds(new RectArea(x, y, width, height));
    }
    
    /**
     * Returns the depth of this mappable node.
     * @return the depth of the mappable node
     */
    public int getDepth() {
        return depth;
    }
    
    /**
     * Sets the depth of this mappable node.
     * @param depth the depth of the mappable node
     */
    public void setDepth(int depth) {
        this.depth = depth;
    }
    
    /**
     * Returns the order of this mappable node.
     * @return the order of the mappable node
     */
    public int getOrder() {
        return order;
    }
    
    /**
     * Sets the order of this mappable node.
     * @param order the index value indicating the order of the mappable node
     */
    public void setOrder(int order) {
        this.order = order;
    }
}
