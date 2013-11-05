/*
 *  Copyright 2013, Katsuhisa Maruyama (maru@jtool.org)
 */

package org.jtool.codeforest.ui.layout;

/**
 * Implements a slice/dice layout.
 * @author Daiki Todoroki
 * @author Katsuhisa Maruyama
 */
public class SliceDice extends AbstractMapLayout {
    
    public static final String Name = "Slice and Dice";
    
    public static final int BEST = 2;
    public static final int ALTERNATE = 3;
    
    private int orientation;
    
    public SliceDice() {
        this(ALTERNATE);
    }
    
    public SliceDice(int orientation) {
        this.orientation = orientation;
    }
    
    public String getName() {
        return Name;
    }
    
    public String getDescription() {
        return Name;
    }
    
    public void layout(IMappable[] items, Rect bounds) {
        if (items.length == 0) {
            return;
        }
        
        if (orientation == BEST) {
            layoutBest(items, 0, items.length - 1, bounds);
        } else if (orientation == ALTERNATE) {
            layout(items, bounds,items[0].getDepth() % 2);
        } else {
            layout(items, bounds, orientation);
        }
    }
    
    public static void layoutBest(IMappable[] items, int start, int end, Rect bounds) {
        int orient = bounds.w > bounds.h ? HORIZONTAL : VERTICAL;
        sliceLayout(items, start, end, bounds, orient);
    }
    
    public static void layoutBest(IMappable[] items, int start, int end, Rect bounds, int order) {
        int orient = bounds.w > bounds.h ? HORIZONTAL : VERTICAL;
        sliceLayout(items,start,end,bounds, orient, order);
    }
    
    public static void layout(IMappable[] items, Rect bounds, int orient) {
        sliceLayout(items, 0, items.length - 1, bounds, orient);
    }
    
    public static void sliceLayout(IMappable[] items, int start, int end, Rect bounds, int orient) {
        sliceLayout(items, start, end, bounds, orient, ASCENDING);
    }
    
    public static void sliceLayout(IMappable[] items, int start, int end, Rect bounds, int orient, int order) {
        double total = totalSize(items, start, end);
        double a = 0;
        boolean vertical = (orient ==VERTICAL);
        
        for (int i = start; i <= end; i++) {
            Rect rect = new Rect();
            double b = items[i].getBaseSize() / total;
            if (vertical) {
                rect.x = bounds.x;
                rect.w = bounds.w;
                if (order == ASCENDING) {
                    rect.y = bounds.y + bounds.h * a;
                } else {
                    rect.y = bounds.y + bounds.h * (1 - a - b);
                }
                rect.h = bounds.h * b;
            
            } else {
                if (order == ASCENDING) {
                    rect.x = bounds.x + bounds.w * a;
                } else {
                    rect.x = bounds.x + bounds.w * (1-a-b);
                }
                rect.w = bounds.w * b;
                rect.y = bounds.y;
                rect.h = bounds.h;
            }
            items[i].setBounds(rect);
            a = a + b;
        }
    }
}
