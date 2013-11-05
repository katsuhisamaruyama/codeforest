/*
 *  Copyright 2013, Katsuhisa Maruyama (maru@jtool.org)
 */

package org.jtool.codeforest.ui.layout;

/**
 * Implements an ordered tree-map layout with a pivot by the middle.
 * @author Daiki Todoroki
 * @author Katsuhisa Maruyama
 */
public class PivotByMiddle implements IMapLayout {
    
    public static final String Name = "Pivot by Middle";
    
    private OrderedTreemap orderedTreemap;
    
    public PivotByMiddle() {
        orderedTreemap = new OrderedTreemap(OrderedTreemap.PIVOT_BY_MIDDLE);
    }
    
    public String getName() {
        return Name;
    }
    
    public String getDescription() {
        return Name;
    }
    
    public void layout(IMapModel model, Rect bounds) {
        orderedTreemap.layout(model, bounds);
    }
    
    public void layout(IMapModel model, double x, double y, double w, double h) {
        layout(model, new Rect(x, y, w, h));
    }
}
