/*
 *  Copyright 2013, Katsuhisa Maruyama (maru@jtool.org)
 */

package org.jtool.codeforest.ui.layout;

import java.util.Comparator;
import java.util.Arrays;

/**
 * Abstract class for a map layout.
 * @author Katsuhisa Maruyama & Daiki Todoroki
 */
public abstract class AbstractMapLayout implements IMapLayout {
    
    public static final int VERTICAL = 0;
    
    public static final int HORIZONTAL = 1;
    
    public static final int ASCENDING = 0;
    
    public static final int DESCENDING = 1;
    
    public abstract void layout(IMappable[] items, Rect bounds);
    
    public void layout(IMapModel model, Rect bounds) {
        layout(model.getItems(), bounds);
    }
    
    public void layout(IMapModel model, double x, double y, double w, double h) {
        layout(model, new Rect(x, y, w, h));
    }
    
    public String getName() {
        return "ABSTRACT";
    }
    
    public String getDescription() {
        return "ABSTRACT";
    }
    
    public static double totalSize(IMappable[] items) {
        return totalSize(items, 0, items.length - 1);
    }
    
    public static double totalSize(IMappable[] items, int start, int end) {
        double sum = 0;
        for (int i = start; i <= end; i++) {
            sum = sum + items[i].getBaseSize();
        }
        return sum;
    }
    
    public IMappable[] sortDescending(IMappable[] items) {
        IMappable[] sortedItems = new IMappable[items.length];
        System.arraycopy(items,0, sortedItems, 0, items.length);
        
        Arrays.sort(sortedItems, new ItemComparator());
        return sortedItems;
    }
    
    class ItemComparator implements Comparator<IMappable> {
        public int compare(IMappable o1, IMappable o2) {
            if (o1.getBaseSize() > o2.getBaseSize()) {
                return -1;
            } else if (o1.getBaseSize() == o2.getBaseSize()) {
                return 0;
            } else {
                return 1;
            }
        }
    }
}
