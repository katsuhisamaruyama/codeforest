/*
 *  Copyright 2013, Katsuhisa Maruyama (maru@jtool.org)
 */

package org.jtool.codeforest.ui.layout;


/**
 * Interface for an item can be mapped.
 * @author Daiki Todoroki
 * @author Katsuhisa Maruyama
 */
public interface IMappable {
    
    public double getBaseSize();
    
    public void setBaseSize(double size);
    
    public Rect getBounds();
    
    public void setBounds(Rect bounds);
    
    public void setBounds(double x, double y, double w, double h);
    
    public int getOrder();
    
    public void setOrder(int order);
    
    public int getDepth();
    
    public void setDepth(int depth);
}
