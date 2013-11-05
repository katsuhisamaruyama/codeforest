/*
 *  Copyright 2013, Katsuhisa Maruyama (maru@jtool.org)
 */

package org.jtool.codeforest.ui.layout;

/**
 * Implements a strip tree-map layout.
 * @author Daiki Todoroki
 * @author Katsuhisa Maruyama
 */
public class StripTreemap implements IMapLayout {
    
    public static final String Name = "Strip Treemap";
    
    private IMappable[] items;
    
    private Rect layoutBox;
    
    private boolean lookahead = true;
    
    public StripTreemap() {
    }
    
    public String getName() {
        return Name;
    }
    
    public String getDescription() {
        return Name;
    }
    
    public void setLookahead(boolean lookahead) {
        this.lookahead = lookahead;
    }
    
    public void layout(IMapModel model, double x, double y, double w, double h) {
        layout(model, new Rect(x, y, w, h));
    }
    
    public void layout(IMapModel model, Rect bounds) {
        items = model.getItems();
        layoutBox = bounds;
        
        double totalSize = 0;
        for (int i = 0; i < items.length; i++) {
            totalSize = totalSize + items[i].getBaseSize();
        }
        
        double area = layoutBox.w * layoutBox.h;
        double scaleFactor = Math.sqrt(area / totalSize);
        
        int finishedIndex = 0;
        int numItems = 0;
        double yoffset = 0;
        Rect box = new Rect(layoutBox);
        box.x = box.x / scaleFactor;
        box.y = box.y / scaleFactor;
        box.w = box.w / scaleFactor;
        box.h = box.h / scaleFactor;
        
        while (finishedIndex < items.length) {
            numItems = layoutStrip(box, finishedIndex);
            
            if (lookahead) {
                if ((finishedIndex + numItems) < items.length) {
                    int numItems2;
                    double ar2a;
                    double ar2b;
                    
                    numItems2 = layoutStrip(box, finishedIndex + numItems);
                    ar2a = computeAverageAspectRatio(finishedIndex, numItems + numItems2);
                    
                    computeHorizontalBoxLayout(box, finishedIndex, numItems + numItems2);
                    ar2b = computeAverageAspectRatio(finishedIndex, numItems + numItems2);
                    
                    if (ar2b < ar2a) {
                        numItems = numItems + numItems2;
                    } else {
                        computeHorizontalBoxLayout(box, finishedIndex, numItems);
                    }
                }
            }
            
            for (int i = finishedIndex; i < (finishedIndex+numItems); i++) {
                Rect rect = (Rect)items[i].getBounds();
                rect.y = rect.y + yoffset;
            }
            double height = ((Rect)items[finishedIndex].getBounds()).h;
            yoffset = yoffset + height;
            box.y = box.y + height;
            box.h = box.h - height;
            
            finishedIndex = finishedIndex + numItems;
        }
        
        Rect rect;
        for (int i = 0; i < items.length; i++) {
            rect = (Rect)items[i].getBounds();
            rect.x = rect.x * scaleFactor;
            rect.y = rect.y * scaleFactor;
            rect.w = rect.w * scaleFactor;
            rect.h = rect.h * scaleFactor;
            
            rect.x = rect.x + bounds.x;
            rect.y = rect.y + bounds.y;
            items[i].setBounds(rect);
        }
    }
    
    protected int layoutStrip(Rect box, int index) {
        int numItems = 0;
        double prevAR;
        double ar = Double.MAX_VALUE;
        
        do {
            prevAR = ar;
            numItems++;
            // double height = computeHorizontalBoxLayout(box, index, numItems);
            ar = computeAverageAspectRatio(index, numItems);
        } while ((ar < prevAR) && ((index + numItems) < items.length));
        
        if (ar >= prevAR) {
            numItems--;
           // double height = computeHorizontalBoxLayout(box, index, numItems);
            ar = computeAverageAspectRatio(index, numItems);
        }
        
        return numItems;
    }
    
    protected double computeHorizontalBoxLayout(Rect box, int index, int numItems) {
        double totalSize = computeSize(index, numItems);
        double height = totalSize / box.w;
        double x = 0;
        
        for (int i = 0; i < numItems; i++) {
            double width = items[i + index].getBaseSize() / height;
            items[i + index].setBounds(x, 0, width, height);
            x = x + width;
        }
        return height;
    }
    
    double computeSize(int index, int num) {
        double size = 0;
        for (int i = 0; i < num; i++) {
            size = size + items[i+index].getBaseSize();
        }
        return size;
    }
    
    double computeAverageAspectRatio(int index, int numItems) {
        double tar = 0;
        for (int i = 0; i < numItems; i++) {
            Rect rect = (Rect)items[i + index].getBounds();
            double ar = Math.max((rect.w / rect.h), (rect.h / rect.w));
            tar = tar + ar;
        }
        tar = tar / numItems;
        return tar;
    }
    
    double computeAspectRatio(int index) {
        Rect rect = (Rect)items[index].getBounds();
        double ar = Math.max((rect.w / rect.h), (rect.h / rect.w));
        return ar;
    }
}
