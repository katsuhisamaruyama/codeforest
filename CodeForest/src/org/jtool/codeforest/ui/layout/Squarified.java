/*
 *  Copyright 2013, Katsuhisa Maruyama (maru@jtool.org)
 */

package org.jtool.codeforest.ui.layout;

/**
 * Implements a squarified layout.
 * @author Daiki Todoroki
 * @author Katsuhisa Maruyama
 */
public class Squarified extends AbstractMapLayout {
    
    public static final String Name = "Squarified";
    
    public void layout(IMappable[] items, Rect bounds) {
        layout(sortDescending(items), 0, items.length - 1, (Rect)bounds);
    }
    
    public void layout(IMapModel model, Rect bounds) {
        layout(sortDescending(model.getItems()), 0, model.getItems().length - 1, (Rect)bounds);
    }
    
    public void layout(IMapModel model, double x, double y, double w, double h) {
        layout(sortDescending(model.getItems()), 0, model.getItems().length-1, new Rect(x, y, w, h));
    }
    
    public String getName() {
        return Name;
    }
    
    public String getDescription() {
        return Name;
    }
    
    public void layout(IMappable[] items, int start, int end, Rect bounds) {
        if (start > end) {
            int tmp = start;
            start = end;
            end = tmp;
        }
        
        if (end - start < 2) {
            SliceDice.layoutBest(items,start,end,bounds);
            return;
        }
        
        double x = bounds.x;
        double y = bounds.y;
        double w = bounds.w;
        double h = bounds.h;
        
        double total = sum(items, start, end);
        int mid = start;
        double a = items[start].getBaseSize() / total;
        double b = a;
        
        if (w < h) {
            while (mid <= end) {
                double aspect = normAspect(h,w,a,b);
                double q = items[mid].getBaseSize()/total;
                if (normAspect(h, w, a, b + q) > aspect) {
                    break;
                }
                mid++;
                b = b + q;
            }
            SliceDice.layoutBest(items, start, mid, new Rect(x, y, w, h * b));
            layout(items, mid + 1, end, new Rect(x, y + h * b, w, h * (1 - b)));
        
        } else {
            while (mid <= end) {
                double aspect = normAspect(w,h,a,b);
                double q = items[mid].getBaseSize() / total;
                if (normAspect(w, h, a, b + q) >aspect) {
                    break;
                }
                mid++;
                b = b + q;
            }
            SliceDice.layoutBest(items, start, mid, new Rect(x, y, w * b, h));
            layout(items, mid + 1, end, new Rect(x + w * b, y, w * (1 - b), h));
        }
    }
    
    private double aspect(double big, double small, double a, double b) {
        return (big * b) / (small * a / b);
    }
    
    private double normAspect(double big, double small, double a, double b) {
        double x = aspect(big,small,a,b);
        if (x < 1) {
            return 1 / x;
        }
        return x;
    }
    
    private double sum(IMappable[] items, int start, int end) {
        double sum = 0;
        for (int i = start; i <= end; i++) {
            sum = sum + items[i].getBaseSize();
        }
        return sum;
    }
}
