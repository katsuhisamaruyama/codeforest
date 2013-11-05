/*
 *  Copyright 2013, Katsuhisa Maruyama (maru@jtool.org)
 */

package org.jtool.codeforest.ui.layout;

/**
 * Implements an ordered tree-map layout.
 * @author Daiki Todoroki
 * @author Katsuhisa Maruyama
 */
public class OrderedTreemap implements IMapLayout {
    
    public static final String Name = "Ordered Treemap";
    
    static public final int PIVOT_BY_MIDDLE = 1;
    
    static public final int PIVOT_BY_SPLIT_SIZE = 2;
    
    static public final int PIVOT_BY_BIGGEST = 3;
    
    private IMappable[] items;
    
    private Rect layoutBox;
    
    private int pivotType;
    
    public OrderedTreemap() {
        this(PIVOT_BY_MIDDLE);
    }
    
    public OrderedTreemap(int pivotType) {
        this.pivotType = pivotType;
    }
    
    public String getName() {
        return Name;
    }
    
    public String getDescription() {
        return Name;
    }
    
    public void layout(IMapModel model, Rect bounds) {
        Rect r = new Rect(0,0,bounds.w,bounds.h);
        layoutAtOrigin(model, r);
        IMappable[] m = model.getItems();
        for (int i = 0; i < m.length; i++) {
            Rect rect = m[i].getBounds();
            rect.x = rect.x + bounds.x;
            rect.y = rect.y + bounds.y;
        }
    }
    
    public void layout(IMapModel model, double x, double y, double w, double h) {
        layout(model, new Rect(x, y, w, h));
    }
    
    public void layoutAtOrigin(IMapModel model, Rect bounds) {
        items = model.getItems();
        layoutBox = bounds;
        
        double totalSize = 0;
        double area = layoutBox.w * layoutBox.h;
        
        for (int i = 0; i < items.length; i++) {
            totalSize += items[i].getBaseSize();
        }
        
        double scaleFactor = Math.sqrt(area / totalSize);
        Rect box = new Rect(layoutBox);
        box.x = box.x / scaleFactor;
        box.y = box.y / scaleFactor;
        box.w = box.w / scaleFactor;
        box.h = box.h / scaleFactor;
        
        double[] sizes = new double[items.length];
        for (int i = 0; i < items.length; i++) {
            sizes[i] = items[i].getBaseSize();
        }
        Rect[] results = orderedLayoutRecurse(sizes, box);
        
        Rect rect;
        for (int i = 0; i < items.length; i++) {
            rect = (Rect)items[i].getBounds();
            rect.x = results[i].x * scaleFactor;
            rect.y = results[i].y * scaleFactor;
            rect.w = results[i].w * scaleFactor;
            rect.h = results[i].h * scaleFactor;
            
            rect.x = rect.x + bounds.x;
            rect.y = rect.y + bounds.y;
            items[i].setBounds(rect);
        }
    }
    
    protected Rect[] orderedLayoutRecurse(double[] sizes, Rect box) {
        double[] l1 = null;
        double[] l2 = null;
        double[] l3 = null;
        double l1Size = 0;
        double l2Size = 0;
        double l3Size = 0;
        Rect r1 = null;
        Rect r2 = null;
        Rect r3 = null;
        Rect rp = null;
        int pivotIndex = computePivotIndex(sizes);
        double pivotSize = sizes[pivotIndex];
        Rect[] boxes = null;
        
        double boxAR = box.w / box.h;
        int d = sizes.length - pivotIndex - 1;
        
        if (sizes.length == 1) {
            boxes = new Rect[1];
            boxes[0] = box;
            return boxes;
        }
        
        if (sizes.length == 2) {
            boxes = new Rect[2];
            double ratio = sizes[0] / (sizes[0] + sizes[1]);
            if (boxAR >= 1) {
                double w = ratio * box.w;
                boxes[0] = new Rect(box.x, box.y, w, box.h);
                boxes[1] = new Rect(box.x + w, box.y, box.w - w, box.h);
            } else {
                double h = ratio * box.h;
                boxes[0] = new Rect(box.x, box.y, box.w, h);
                boxes[1] = new Rect(box.x, box.y + h, box.w, box.h - h);
            }
            return boxes;
        }
        
        l1 = new double[pivotIndex];
        System.arraycopy(sizes, 0, l1, 0, pivotIndex);
        l1Size = computeSize(l1);
        Rect box2;
        if (boxAR >= 1) {
            double h = box.h;
            double w = l1Size / h;
            r1 = new Rect(box.x, box.y, w, h);
            box2 = new Rect(r1.x + r1.w, box.y, 
                    box.w - r1.w, box.h);
        } else {
            double w = box.w;
            double h = l1Size / w;
            r1 = new Rect(box.x, box.y, w, h);
            box2 = new Rect(r1.x, r1.y + r1.h, 
                    box.w, box.h - r1.h);
        }
        
        // double box2AR = box2.w / box2.h;
        if (d >= 3) {
            boolean first = true;
            double bestAR = 0;
            double bestW = 0;
            double bestH = 0;
            int bestIndex = 0;
            for (int i = pivotIndex+1; i < sizes.length; i++) {
                l2Size = computeSize(sizes, pivotIndex+1, i);
                l3Size = computeSize(sizes, i+1, sizes.length-1);
                double ratio = (double)(pivotSize + l2Size) / (pivotSize + l2Size + l3Size);
                double w;
                double h;
                if (boxAR >= 1) {
                    w = ratio * box2.w;
                    ratio = (double)pivotSize / (pivotSize + l2Size);
                    h = ratio * box2.h;
                } else {
                    h = ratio * box2.h;
                    ratio = (double)pivotSize / (pivotSize + l2Size);
                    w = ratio * box2.w;
                }
                double pivotAR = w / h;
                if (first) {
                    first = false;
                    bestAR = pivotAR;
                    bestW = w;
                    bestH = h;
                    bestIndex = i;
                } else if (Math.abs(pivotAR - 1) < Math.abs(bestAR - 1)) {
                    bestAR = pivotAR;
                    bestW = w;
                    bestH = h;
                    bestIndex = i;
                }           
            }
            l2 = new double[bestIndex - pivotIndex];
            System.arraycopy(sizes, pivotIndex+1, l2, 0, l2.length);
            if ((sizes.length-1-bestIndex) > 0) {
                l3 = new double[sizes.length-1 - bestIndex];
                System.arraycopy(sizes, bestIndex+1, l3, 0, l3.length);
            } else {
                l3 = null;
            }
            if (boxAR >= 1) {
                rp = new Rect(box2.x, box2.y, bestW, bestH);
                r2 = new Rect(box2.x, box2.y + bestH, bestW, box2.h - bestH);
                if (l3 != null) {
                    r3 = new Rect(box2.x + bestW, box2.y, box2.w - bestW, box2.h);
                }
            } else {
                rp = new Rect(box2.x, box2.y, bestW, bestH);
                r2 = new Rect(box2.x + bestW, box2.y, box2.w - bestW, bestH);
                if (l3 != null) {
                    r3 = new Rect(box2.x, box2.y + bestH, box2.w, box2.h - bestH);
                }
            }
        } else if (d > 0) {
            // l3 is null
            l2 = new double[d];
            System.arraycopy(sizes, pivotIndex+1, l2, 0, d);
            double ratio = (double)pivotSize / (pivotSize + computeSize(l2));
            if (boxAR >= 1) {
                double h = ratio * box2.h;
                rp = new Rect(box2.x, box2.y, box2.w, h);
                r2 = new Rect(box2.x, box2.y + h, box2.w, box2.h - h);
            } else {
                double w = ratio * box2.w;
                rp = new Rect(box2.x, box2.y, w, box2.h);
                r2 = new Rect(box2.x+ w, box2.y, box2.w - w, box2.h);
            }
        } else {
            rp = box2;
        }
        
        // Finally, recurse on sublists
        Rect[] l1boxes = null;
        Rect[] l2boxes = null;
        Rect[] l3boxes = null;
        int numBoxes = 0;
        
        if (l1.length > 1) {
            l1boxes = orderedLayoutRecurse(l1, r1);
        } else if (l1.length == 1) {
            l1boxes = new Rect[1];
            l1boxes[0] = r1;
        }
        
        if (l2 != null) {
            if (l2.length > 1) {
                l2boxes = orderedLayoutRecurse(l2, r2);
            } else if (l2.length == 1) {
                l2boxes = new Rect[1];
                l2boxes[0] = r2;
                
            }
        }
        
        if (l3 != null) {
            if (l3.length > 1) {
                l3boxes = orderedLayoutRecurse(l3, r3);
            } else if (l3.length == 1) {
                l3boxes = new Rect[1];
                l3boxes[0] = r3;
            }
        }
        
        numBoxes = l1.length + 1;
        if (l2 != null) {
            numBoxes = numBoxes + l2.length;
        }
        if (l3 != null) {
            numBoxes = numBoxes + l3.length;
        }
        boxes = new Rect[numBoxes];
        int i = 0;
        if (l1boxes != null) {
            System.arraycopy(l1boxes, 0, boxes, 0, l1boxes.length);
            i = l1boxes.length;
        }
        boxes[i] = rp;
        i++;
        if (l2 != null) {
            System.arraycopy(l2boxes, 0, boxes, i, l2boxes.length);
        }
        if (l3 != null) {
            i = i + l2boxes.length;
            System.arraycopy(l3boxes, 0, boxes, i, l3boxes.length);
        }
        
        boxes = tryAlternativeLayouts(sizes, box, boxes);
        return boxes;
    }
    
    Rect[] tryAlternativeLayouts(double[] sizes, Rect box, Rect[] layoutBoxes) {
        Rect[] boxes = layoutBoxes;
        Rect[] nboxes = null;
        double boxAR = box.w / box.h;
        
        if (sizes.length == 3) {
            nboxes = new Rect[3];
            double ratio1 = (double)(sizes[0]) / (sizes[0] + sizes[1] + sizes[2]);
            double ratio2 = (double)(sizes[1]) / (sizes[0] + sizes[1] + sizes[2]);
            double ratio3 = (double)(sizes[2]) / (sizes[0] + sizes[1] + sizes[2]);
            
            if (boxAR >= 1) {
                double h = box.h;
                double w1 = ratio1 * box.w;
                double w2 = ratio2 * box.w;
                double w3 = ratio3 * box.w;
                nboxes[0] = new Rect(box.x, box.y, w1, h);
                nboxes[1] = new Rect(box.x + w1, box.y, w2, h);
                nboxes[2] = new Rect(box.x + w1 + w2, box.y, w3, h);
                
            } else {
                double w = box.w;
                double h1 = ratio1 * box.h;
                double h2 = ratio2 * box.h;
                double h3 = ratio3 * box.h;
                nboxes[0] = new Rect(box.x, box.y, w, h1);
                nboxes[1] = new Rect(box.x, box.y + h1, w, h2);
                nboxes[2] = new Rect(box.x, box.y + h1 + h2, w, h3);
            }
            
            double origAvgAR = computeAverageAspectRatio(boxes);
            double newAvgAR = computeAverageAspectRatio(nboxes);
            if (newAvgAR < origAvgAR) {
                boxes = nboxes;
            }
        }
        
        if (sizes.length == 4) {
            nboxes = new Rect[4];
            double ratio1 = (double)(sizes[0] + sizes[1]) / (sizes[0] + sizes[1] + sizes[2] + sizes[3]);
            
            if (boxAR >= 1) {
                double w = ratio1 * box.w;
                double ratio2 = (double)(sizes[0]) / (sizes[0] + sizes[1]);
                double h = ratio2 * box.h;
                nboxes[0] = new Rect(box.x, box.y, w, h);
                nboxes[1] = new Rect(box.x, box.y + h, w, box.h - h);
                ratio2 = (double)(sizes[2]) / (sizes[2] + sizes[3]);
                h = ratio2 * box.h;
                nboxes[2] = new Rect(box.x + w, box.y, box.w - w, h);
                nboxes[3] = new Rect(box.x + w, box.y + h, box.w - w, box.h - h);
                
            } else {
                double h = ratio1 * box.h;
                double ratio2 = (double)(sizes[0]) / (sizes[0] + sizes[1]);
                double w = ratio2 * box.w;
                nboxes[0] = new Rect(box.x, box.y, w, h);
                nboxes[1] = new Rect(box.x, box.y + h, w, box.h - h);
                ratio2 = (double)(sizes[2]) / (sizes[2] + sizes[3]);
                h = ratio2 * box.h;
                nboxes[2] = new Rect(box.x + w, box.y, box.w - w, h);
                nboxes[3] = new Rect(box.x + w, box.y + h, box.w - w, box.h - h);
            }
            
            double origAvgAR = computeAverageAspectRatio(boxes);
            double newAvgAR = computeAverageAspectRatio(nboxes);
            if (newAvgAR < origAvgAR) {
                boxes = nboxes;
            }
            
            nboxes = new Rect[4];
            ratio1 = (double)(sizes[0]) / (sizes[0] + sizes[1] + sizes[2] + sizes[3]);
            double ratio2 = (double)(sizes[1]) / (sizes[0] + sizes[1] + sizes[2] + sizes[3]);
            double ratio3 = (double)(sizes[2]) / (sizes[0] + sizes[1] + sizes[2] + sizes[3]);
            double ratio4 = (double)(sizes[3]) / (sizes[0] + sizes[1] + sizes[2] + sizes[3]);
            
            if (boxAR >= 1) {
                double h = box.h;
                double w1 = ratio1 * box.w;
                double w2 = ratio2 * box.w;
                double w3 = ratio3 * box.w;
                double w4 = ratio4 * box.w;
                nboxes[0] = new Rect(box.x, box.y, w1, h);
                nboxes[1] = new Rect(box.x + w1, box.y, w2, h);
                nboxes[2] = new Rect(box.x + w1 + w2, box.y, w3, h);
                nboxes[3] = new Rect(box.x + w1 + w2 + w3, box.y, w4, h);
                
            } else {
                double w = box.w;
                double h1 = ratio1 * box.h;
                double h2 = ratio2 * box.h;
                double h3 = ratio3 * box.h;
                double h4 = ratio4 * box.h;
                nboxes[0] = new Rect(box.x, box.y, w, h1);
                nboxes[1] = new Rect(box.x, box.y + h1, w, h2);
                nboxes[2] = new Rect(box.x, box.y + h1 + h2, w, h3);
                nboxes[3] = new Rect(box.x, box.y + h1 + h2 + h3, w, h4);
            }
            
            origAvgAR = computeAverageAspectRatio(boxes);
            newAvgAR = computeAverageAspectRatio(nboxes);
            if (newAvgAR < origAvgAR) {
                boxes = nboxes;
            }
        }
        
        if (sizes.length == 5) {
            nboxes = new Rect[5];
            double ratio1 = (double)(sizes[0]) / (sizes[0] + sizes[1] + sizes[2] + sizes[3] + sizes[4]);
            double ratio2 = (double)(sizes[1]) / (sizes[0] + sizes[1] + sizes[2] + sizes[3] + sizes[4]);
            double ratio3 = (double)(sizes[2]) / (sizes[0] + sizes[1] + sizes[2] + sizes[3] + sizes[4]);
            double ratio4 = (double)(sizes[3]) / (sizes[0] + sizes[1] + sizes[2] + sizes[3] + sizes[4]);
            double ratio5 = (double)(sizes[4]) / (sizes[0] + sizes[1] + sizes[2] + sizes[3] + sizes[4]);
            
            if (boxAR >= 1) {
                double h = box.h;
                double w1 = ratio1 * box.w;
                double w2 = ratio2 * box.w;
                double w3 = ratio3 * box.w;
                double w4 = ratio4 * box.w;
                double w5 = ratio5 * box.w;
                nboxes[0] = new Rect(box.x, box.y, w1, h);
                nboxes[1] = new Rect(box.x + w1, box.y, w2, h);
                nboxes[2] = new Rect(box.x + w1 + w2, box.y, w3, h);
                nboxes[3] = new Rect(box.x + w1 + w2 + w3, box.y, w4, h);
                nboxes[4] = new Rect(box.x + w1 + w2 + w3 + w4, box.y, w5, h);
                
            } else {
                double w = box.w;
                double h1 = ratio1 * box.h;
                double h2 = ratio2 * box.h;
                double h3 = ratio3 * box.h;
                double h4 = ratio4 * box.h;
                double h5 = ratio5 * box.h;
                nboxes[0] = new Rect(box.x, box.y, w, h1);
                nboxes[1] = new Rect(box.x, box.y + h1, w, h2);
                nboxes[2] = new Rect(box.x, box.y + h1 + h2, w, h3);
                nboxes[3] = new Rect(box.x, box.y + h1 + h2 + h3, w, h4);
                nboxes[4] = new Rect(box.x, box.y + h1 + h2 + h3 + h4, w, h5);
            }
            
            double origAvgAR = computeAverageAspectRatio(boxes);
            double newAvgAR = computeAverageAspectRatio(nboxes);
            if (newAvgAR < origAvgAR) {
                boxes = nboxes;
            }
        }
        return boxes;
    }
    
    protected int computePivotIndex(double[] sizes) {
        int index = 0;
        double bestRatio = 0;
        boolean first = true;
        
        switch (pivotType) {
        case PIVOT_BY_MIDDLE:
            index = (sizes.length - 1) / 2;
            break;
        
        case PIVOT_BY_SPLIT_SIZE:
            double leftSize = 0;
            double rightSize = computeSize(sizes);
            
            for (int i = 0; i < sizes.length; i++) {
                double ratio = Math.max(((double)leftSize / rightSize), ((double)rightSize / leftSize));
                if (first || (ratio < bestRatio)) {
                    first = false;
                    bestRatio = ratio;
                    index = i;
                }
                
                leftSize = leftSize + sizes[i];
                rightSize = rightSize - sizes[i];
            }
            break;
            
        case PIVOT_BY_BIGGEST:
            double biggest = 0;
            for (int i = 0; i < sizes.length; i++) {
                if (first || (sizes[i] > biggest)) {
                    first = false;
                    biggest = sizes[i];
                    index = i;
                }
            }
            break;
        }
        
        return index;
    }
    
    double computeSize(double[] sizes) {
        double size = 0;
        for (int i = 0; i < sizes.length; i++) {
            size = size + sizes[i];
        }
        return size;
    }
    
    double computeSize(double[] sizes, int i1, int i2) {
        double size = 0;
        for (int i = i1; i <= i2; i++) {
            size = size + sizes[i];
        }
        return size;
    }
    
    double computeAverageAspectRatio(Rect[] rects) {
        double tar = 0;
        int numRects = 0;
        
        for (int i = 0; i < rects.length; i++) {
            double w = rects[i].w;
            double h = rects[i].h;
            if ((w != 0) && (h != 0)) {
                double ar = Math.max((w / h), (h / w));
                tar = tar + ar;
                numRects++;
            }
        }
        tar = tar / numRects;
        
        return tar;
    }
}
