/*
 *  Copyright 2014, Katsuhisa Maruyama (maru@jtool.org)
 */

package org.jtool.codeforest.ui.layout;

/**
 * Represents a rectangle area for a layout.
 * @author Katsuhisa Maruyama
 * @author Daiki Todoroki
 */
public class RectArea {
    
    /**
     * The x-position of this area.
     */
    double x;
    
    /**
     * The y-position of this area.
     */
    double y;
    
    /**
     * The width of this area.
     */
    double width;
    
    /**
     * The height of this area.
     */
    double height;
    
    /**
     * Creates a default area.
     */
    RectArea() {
        this(0, 0, 1, 1);
    }
    
    /**
     * Creates a copy of a given area.
     * @param area the area to be copied
     */
    RectArea(RectArea area) {
        this(area.x, area.y, area.width, area.height);
    }
    
    /**
     * Creates a area.
     * @param x the x-position of the area
     * @param y the y-position of the area
     * @param width the width of the area
     * @param height the height of the area
     */
    RectArea(double x, double y, double width, double height) {
        setRect(x, y, width, height);
    }
    
    /**
     * Sets the boundary information on this area.
     * @param x the x-position of the area
     * @param y the y-position of the area
     * @param width the width of the area
     * @param height the height of the area
     */
    void setRect(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
    
    /**
     * Obtains the aspect ratio of this area.
     * @return the aspect ratio of the area
     */
    double aspectRatio() {
        return Math.max(width / height , height / width);
    }
    
    /**
     * Obtains the distance between a given area and this area.
     * @param area the area to be compared
     * @return the distance
     */
    double distance(RectArea area) {
        double dx = area.x - x;
        double dy = area.y - y;
        double dw = area.width - width;
        double dh = area.height - height;
        return Math.sqrt(dx * dx + dy * dy + dw * dw + dh * dh);
    }
    
    /**
     * Obtains a copy of this area.
     * @return the copied area
     */
    RectArea copy() {
        return new RectArea(x, y, width, height);
    }
}
