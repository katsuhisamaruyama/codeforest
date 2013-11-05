/*
 *  Copyright 2013, Katsuhisa Maruyama (maru@jtool.org)
 */

package org.jtool.codeforest.ui.layout;

/**
 * Represents a 2D vector.
 * @author Daiki Todoroki
 * @author Katsuhisa Maruyama
 */
public class Vector2D {
      
    protected boolean valid;
    
    protected double x, y, z;
    
    public static Vector2D Z = new Vector2D(0, 0, 1.0);
    
    public Vector2D() {
        z = 0;
        y = 0;
        z = 0;
        valid = true;
    }
    
    public Vector2D(double nx, double ny) {
        x = nx;
        y = ny;
        z = 0;
        valid = true;
    }
    
    public Vector2D(Vector2D p0, Vector2D p1) {
        x = p1.x - p0.x;
        y = p1.y - p0.y;
    }
    
    Vector2D(double nx, double ny, double nz) {
        x = nx;
        y = ny;
        z = nz;
        valid = true;
    }
    
    protected Vector2D(boolean v) {
        x = 0;
        y = 0;
        z = 0;
        valid = v;
    }
    
    void normalize(double length) {
        normalize();
        scaleBy(length);
    }
    
    double normalize() {
        double mag = magnitude();
        if (mag != 0.0) {
          x = x / mag;
          y = y / mag;
          z = z / mag;
        }
        return mag;
    }
    
    double magnitude() {
        return Math.sqrt (x * x + y * y + z * z);
    }
    
    public boolean isNull() {
        return x == 0.0 && y == 0.0 && z == 0.0;
    }
    
    public static Vector2D cross(Vector2D v, Vector2D w) {
        return new Vector2D (v.y * w.z - v.z * w.y, v.z * w.x - v.x * w.z, v.x * w.y - v.y * w.x);
    }
    
    public static double dot(Vector2D v, Vector2D w) {
        return v.x * w.x + v.y * w.y + v.z * w.z;
    }
    
    public static Vector2D rotate(Vector2D v, Vector2D axis, double angle) {
        Vector2D n = (Vector2D) axis.clone();
        n.normalize();
        n.scaleBy(Vector2D.dot(n, v));
        n.add((Vector2D.difference(v, n)).scaleBy(Math.cos(angle)));
        n.add(cross(n, v).scaleBy(Math.sin(angle)));
        
        return n;
    }
    
    public Object clone() {
        return new Vector2D(x, y, z);
    }
    
    public void rescale(double length) {
        normalize();
        scaleBy(length);
    }
    
    public static Vector2D add(Vector2D v, Vector2D w) {
        return new Vector2D (v.x + w.x, v.y + w.y, v.z + w.z);
    }
    
    public void add(Vector2D rhs) {
        x = x + rhs.x;
        y = y + rhs.y;
        z = z + rhs.z;
    }
    public Vector2D scaleBy(double factor) {
        x = x * factor;
        y = y * factor;
        z = z * factor;
        return this;
    }
    
    public void lengthenBy(double val) {
        double mag = magnitude();
        scaleBy((mag + val) / mag);
    }
    
    public void shortenBy(double val) {
        lengthenBy(-val);
    }
    
    public static Vector2D difference(Vector2D v, Vector2D w) {
        return new Vector2D(v.x - w.x, v.y - w.y, v.z - w.z);
    }
}
