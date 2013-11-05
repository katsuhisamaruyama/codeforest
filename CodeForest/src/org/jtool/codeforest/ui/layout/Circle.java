/*
 *  Copyright 2013, Katsuhisa Maruyama (maru@jtool.org)
 */

package org.jtool.codeforest.ui.layout;


/**
 * Class representing a circle for a layout.
 * @author Katsuhisa Maruyama & Daiki Todoroki
 */
public class Circle {
    
    final static double EPSILON = 0.000001;
    
    private double x;
    
    private double y;
    
    private double r;
    
    static boolean doIntersect(Circle c0, Circle c1) {
        double diff = Vector2D.difference(c1.center(), c0.center()).magnitude();
        return (diff < (c0.r + c1.r - EPSILON));
        }
    
    public Circle(double cx, double cy, double r) {
        this.x = cx;
        this.y = cy;
        this.r = r;
    }
    
    Circle (Vector2D center, double r) {
        this(center.x, center.y, r);
    }
    
    Circle(double r) {
        this(0.0, 0.0, r);
    }
    
    Vector2D center() {
        return new Vector2D(x, y);
    }
    
    public double getX() {
        return x;
    }
    
    public double getY() {
        return y;
    }
    
    public double getR() {
       return r; 
    }
    
    public void incRadius(double dr) {
        r = r + dr;
    }
    
    public void decRadius(double dr) {
        r = r - dr;
    }
    
    public void translate(Vector2D v) {
        x = x + v.x;
        y = y + v.y;
    }
    
    public static Circle combine(Circle c0, Circle c1) {
        Vector2D vr = Circle.centerMinus(c1, c0);
        vr.lengthenBy(c0.r + c1.r);
        vr.scaleBy(0.5);
        
        double r = vr.magnitude();
        if (r >= c0.r) {
          vr.shortenBy(c0.r);
          vr.add(c0.center());
          return new Circle (vr, r);
        }
        
        if (c0.r >= c1.r) {
            return c0;
        }
        return c1;
    }
    
    public void scale(double factor) {
        r = r * factor;
    }
    
    public void scaleFull(double factor) {
        x = x * factor;
        y = y * factor;
        r = r * factor;
    }
    
    void rotate(double angle) {
        Vector2D new_center = Vector2D.rotate (new Vector2D(x, y), Vector2D.Z, angle);
        x = new_center.x;
        y = new_center.y;
      }
    
    void rotate(Vector2D ref, double angle) {
        Vector2D new_center = (Vector2D.rotate (Vector2D.difference(new Vector2D(x, y), ref), Vector2D.Z, angle));
        new_center.add(ref);
        x = new_center.x;
        y = new_center.y;
      }
    
    public static double distanceOfCenters(Circle c0, Circle c1) {
        return Math.sqrt((c0.x - c1.x) * (c0.x - c1.x) + (c0.y - c1.y) * (c0.y - c1.y)); 
    }
    
    public static Vector2D centerMinus(Circle lhs, Circle rhs) {
        return new Vector2D(lhs.x - rhs.x, lhs.y - rhs.y); 
    }
    
    public boolean equals(Object o) {
        Circle other = (Circle)o;
        return (x == other.x) && (y == other.y) && (r == other.r);
    }
    
    public Circle[] getGapCircles(Circle in1, Circle in2, double r) {
        double r1 = in1.r + r, r2 = in2.r + r;
        double d = Circle.centerMinus(in1, in2).magnitude();
        if ((d > r1 + r2) | (d < Math.abs(r1 - r2))) {
            return null;
        }
        double a = (r1 * r1 - r2 * r2 + d * d) / (2 * d);
        double h = Math.sqrt(r1 * r1 - a * a);
        Vector2D p2 = in1.center();
        p2.add(in2.center().scaleBy(a));
        p2.add(in1.center().scaleBy(-1.0/d));
        
        double x0 = in1.x, x1 = in2.x, x2 = p2.x;
        double y0 = in1.y, y1 = in2.y, y2 = p2.y;
        
        Circle[] result = new Circle[2];
        result[0] = new Circle(x2 + h * ( y1 - y0 ) / d, y2 - h * ( x1 - x0 ) / d, r);
        result[1] = new Circle(x2 - h * ( y1 - y0 ) / d, y2 + h * ( x1 - x0 ) / d, r);
        
        return result;
    }
      
    public boolean includesPoint(Vector2D point) {
        return Vector2D.difference(point, new Vector2D(x, y)).magnitude() <= r;
    }
}
