/*
 *  Copyright 2014, Katsuhisa Maruyama (maru@jtool.org)
 */

package org.jtool.codeforest.ui.layout;

/**
 * Interface for a mappable node to be laid out.
 * @author Katsuhisa Maruyama
 * @author Daiki Todoroki
 */
public interface IMappableNode {
    
    /**
     * Returns the base size of this mappable node.
     * @return the base size of the mappable node
     */
    public double getBaseSize();
    
    /**
     * Sets the base size of this mappable node.
     * @param size the base size of the mappable node
     */
    public void setBaseSize(double size);
    
    /**
     * Returns the bounds of this mappable node.
     * @return the bounds of the mappable node
     */
    public RectArea getBounds();
    
    /**
     * Sets the bounds of this mappable node.
     * @param bounds the bounds of the mappable node
     */
    public void setBounds(RectArea bounds);
    
    /**
     * Sets the bounds of this mappable node.
     * @param x the x-position of the mappable node
     * @param y the y-position of the mappable node
     * @param width the width of the mappable node
     * @param height the height of the mappable node
     */
    public void setBounds(double x, double y, double width, double height);
    
    /**
     * Returns the depth of this mappable node.
     * @return the depth of the mappable node
     */
    public int getDepth();
    
    /**
     * Sets the depth of this mappable node.
     * @param depth the depth of the mappable node
     */
    public void setDepth(int depth);
    
    /**
     * Returns the order of this mappable node.
     * @return the order of the mappable node
     */
    public int getOrder();
    
    /**
     * Sets the order of this mappable node.
     * @param order the index value indicating the order of the mappable node
     */
    public void setOrder(int order);
}
