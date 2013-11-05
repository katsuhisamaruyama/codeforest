/*
 *  Copyright 2013, Katsuhisa Maruyama (maru@jtool.org)
 */

package org.jtool.codeforest.ui.layout;

/**
 * Implements a binary tree layout.
 * @author Daiki Todoroki
 * @author Katsuhisa Maruyama
 */
public class BinalyTree extends AbstractMapLayout {
    
    public static final String Name = "Binary Tree";
    
    public BinalyTree() {
    }
    
    public String getName() {
        return Name;
    }
    
    public String getDescription() {
        return Name;
    }
    
    public void layout(IMapModel model, Rect bounds) {
        layout(model.getItems(), bounds);
    }
    
    public void layout(IMappable[] items, Rect bounds) {
        layout(items, 0 , items.length - 1, bounds);
    }
    
    public void layout(IMappable[] items, int start, int end, Rect bounds) {
        layout(items, start, end, bounds, true);
    }
    
    public void layout(IMappable[] items, int start, int end, Rect bounds, boolean vertical) {
        if (start == end) {
            items[start].setBounds(bounds);
            return;
        }
        
        if (start > end) {
            int temp = start;
            start = end;
            end = temp;
        }
        
        int mid = (start+end) / 2;
        
        double total = sum(items, start, end);
        double first = sum(items, start, mid);
        
        double a = first / total;
        double x = bounds.x;
        double y = bounds.y;
        double w = bounds.w;
        double h = bounds.h;
        
        if (vertical) {
            Rect rect1 = new Rect(x, y, w * a, h);
            Rect rect2 = new Rect(x + w * a, y, w * (1 - a), h);
            layout(items, start, mid, rect1, !vertical);
            layout(items, mid + 1, end, rect2, !vertical);
        } else {
            Rect rect1 =new Rect(x, y, w, h * a);
            Rect rect2 =new Rect(x, y + h * a, w, h * (1 - a));
            layout(items, start, mid, rect1, !vertical);
            layout(items, mid + 1,end, rect2, !vertical);
        }
    }
    
    private double sum(IMappable[] items, int start, int end) {
        double sum = 0;
        for (int i = start; i <= end; i++) {
            sum = sum + items[i].getBaseSize();
        }
        return sum;
    }
}
