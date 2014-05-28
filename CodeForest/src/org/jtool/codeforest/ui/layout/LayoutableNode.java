/*
 *  Copyright 2014, Katsuhisa Maruyama (maru@jtool.org)
 */

package org.jtool.codeforest.ui.layout;

/**
 * Represents a node to be laid out.
 * @author Katsuhisa Maruyama
 * @author Daiki Todoroki
 */
public class LayoutableNode extends MappableNode {
    
    /**
     * The leftmost of the x-position of the area containing mappable nodes.
     */
    protected double left;
    
    /**
     * The rightmost of the x-position of the area containing mappable nodes.
     */
    protected double right;
    
    /**
     * The topmost of the y-position of the area containing mappable nodes.
     */
    protected double top;
    
    /**
     * The bottommost of the y-position of the area containing mappable nodes.
     */
    protected double bottom;
    
    /**
     * Creates a new, empty object.
     */
    public LayoutableNode() {
    }
    
    /**
     * Lays out mappable nodes in an area.
     * @param model the model managing mappable nodes
     * @param area the area
     */
    public void layout(IMapModel model, RectArea area) {
        MapLayout layout = new SquarifiedLayout();
        layout.layout(model, area);
    }
    
    /**
     * Calculates the box area of this node.
     */
    public void calculateBox() {
        left = area.x;
        top = area.y;
        right = left + area.width;
        bottom = top + area.height;
    }
    
    /**
     * Returns the width (the distance along the x-axis) of this node.
     * @return the width of the node
     */
    public double getWidth() {
        return Math.abs(this.left - this.right);
    }
    
    /**
     * Returns the length (the distance along the z-axis) of this node.
     * @return the length of the node
     */
    public double getLength() {
        return Math.abs(this.top - this.bottom);
    }
    
    /**
     * Returns the x-position of this node.
     * @return the x-position of the node
     */
    public double getX() {
        return (left + right) / 2;
    }
    
    /**
     * Returns the z-position of this node.
     * @return the z-position of the node
     */
    public double getZ() {
        return (top + bottom) / 2;
    }
}
