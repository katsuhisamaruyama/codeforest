/*
 *  Copyright 2014, Katsuhisa Maruyama (maru@jtool.org)
 */

package org.jtool.codeforest.ui.layout;

/**
 * Represents a rectangle for a layout.
 * @author Daiki Todoroki
 * @author Katsuhisa Maruyama
 */
public class Rect {
    
    double x, y;
    
    double w, h;
    
    Rect() {
        this(0, 0, 1, 1);
    }
    
    Rect(Rect r) {
        this(r.x, r.y, r.w, r.h);
    }
    
    Rect(double x, double y, double w, double h) {
        setRect(x, y, w, h);
    }
    
    void setRect(double x, double y, double w, double h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }
    
    double aspectRatio() {
        return Math.max(w /h , h / w);
    }
    
    double distance(Rect r) {
        double dx = r.x - x;
        double dy = r.y - y;
        double dw = r.w - w;
        double dh = r.h - h;
        return Math.sqrt(dx * dx + dy * dy + dw * dw + dh * dh);
    }
    
    Rect copy() {
        return new Rect(x, y, w, h);
    }
    
    public String toString() {
        return "Rect: " + x + ", " + y + ", " + w + ", " + h;
    }
}
