/*
 *  Copyright 2013, Katsuhisa Maruyama (maru@jtool.org)
 */

package org.jtool.codeforest.ui.layout;

/**
 * Switches a layout algorithm.
 * @author Daiki Todoroki
 * @author Katsuhisa Maruyama
 */
public class LayoutAlgorithm {
    
    public static final String[] LAYOUTS = {
        Squarified.Name,
        StripTreemap.Name, 
        OrderedTreemap.Name,
        BinalyTree.Name,
        SliceDice.Name,
        PivotByMiddle.Name,
        PivotBySize.Name,
        PivotBySplitSize.Name
    };
    
    public String layoutName;
    
    public LayoutAlgorithm() {
        layoutName = Squarified.Name;
    }
    
    public void setLayout(String name) {
        layoutName = name;
    }
    
    public void layout(IMapModel model, Rect bounds) {
        IMapLayout layout;
        if (layoutName.equals(Squarified.Name)) {
            layout = new Squarified();
            
        } else if (layoutName.equals(StripTreemap.Name)) {
            layout = new StripTreemap();
            
        } else if (layoutName.equals(OrderedTreemap.Name)) {
            layout = new OrderedTreemap();
            
        } else if (layoutName.equals(BinalyTree.Name)) {
            layout = new BinalyTree();
            
        } else if (layoutName.equals(SliceDice.Name)) {
            layout = new SliceDice();
            
        } else if (layoutName.equals(PivotByMiddle.Name)) {
            layout = new PivotByMiddle();
            
        } else if (layoutName.equals(PivotBySize.Name)) {
            layout = new PivotBySize();
            
        } else if (layoutName.equals(PivotBySplitSize.Name)) {
            layout = new PivotBySplitSize();
            
        } else {
            layout = new Squarified();
        }
        
        layout.layout(model, bounds);
    }
}
