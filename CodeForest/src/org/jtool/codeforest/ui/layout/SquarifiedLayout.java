/*
 *  Copyright 2014, Katsuhisa Maruyama (maru@jtool.org)
 */

package org.jtool.codeforest.ui.layout;

/**
 * Implements a squarified layout.
 * @author Katsuhisa Maruyama
 * @author Daiki Todoroki
 */
public class SquarifiedLayout extends MapLayout {
    
    /**
     * Lays out mappable nodes in an area.
     * @param nodes nodes the array of all the mappable nodes
     * @param area the area containing the mappable nodes
     */
    public void layout(IMappableNode[] nodes, RectArea area) {
        layout(sortDescending(nodes), 0, nodes.length - 1, area);
    }
    
    /**
     * Lays out mappable nodes in an area.
     * @param model the model managing mappable nodes
     * @param area the area containing the mappable nodes
     */
    public void layout(IMapModel model, RectArea area) {
        layout(sortDescending(model.getNodes()), 0, model.getNodes().length - 1, area);
    }
    
    /**
     * Lays out mappable nodes in an area.
     * @param model the model managing mappable nodes
     * @param x the x-position of the area
     * @param y the x-position of the area
     * @param width the width of the area
     * @param height the height of the area
     */
    public void layout(IMapModel model, double x, double y, double width, double height) {
        layout(sortDescending(model.getNodes()), 0, model.getNodes().length - 1, new RectArea(x, y, width, height));
    }
    
    /**
     * Lays out mappable nodes in the area.
     * @param nodes nodes the array of all the mappable nodes
     * @param start the index number of the starting point of the mappable nodes to be laid out
     * @param end the index number of the ending point of the mappable nodes to be laid out
     * @param area the area containing the mappable nodes
     */
    public void layout(IMappableNode[] nodes, int start, int end, RectArea area) {
        if (start > end) {
            int tmp = start;
            start = end;
            end = tmp;
        }
        
        if (end - start < 2) {
            sliceDiceLayout(nodes, start, end, area);
            return;
        }
        
        double x = area.x;
        double y = area.y;
        double w = area.width;
        double h = area.height;
        
        double total = getTotalBaseSize(nodes, start, end);
        int mid = start;
        double a = nodes[start].getBaseSize() / total;
        double b = a;
        
        if (w < h) {
            while (mid <= end) {
                double aspect = normalizeAspect(h, w, a, b);
                double q = nodes[mid].getBaseSize()/total;
                if (normalizeAspect(h, w, a, b + q) > aspect) {
                    break;
                }
                mid++;
                b = b + q;
            }
            
            sliceDiceLayout(nodes, start, mid, new RectArea(x, y, w, h * b));
            layout(nodes, mid + 1, end, new RectArea(x, y + h * b, w, h * (1 - b)));
            
        } else {
            while (mid <= end) {
                double aspect = normalizeAspect(w, h, a, b);
                double q = nodes[mid].getBaseSize() / total;
                if (normalizeAspect(w, h, a, b + q) >aspect) {
                    break;
                }
                mid++;
                b = b + q;
            }
            
            sliceDiceLayout(nodes, start, mid, new RectArea(x, y, w * b, h));
            layout(nodes, mid + 1, end, new RectArea(x + w * b, y, w * (1 - b), h));
        }
    }
    
    /**
     * Normalizes the aspect of the area containing mappable nodes
     * @param big the large number of the area
     * @param small the small number of the area
     * @param a factor of the aspect ratio
     * @param b the factor of the aspect ratio
     * @return the aspect ratio of the area
     */
    private double normalizeAspect(double large, double small, double a, double b) {
        double aspect = (large * b) / (small * a / b);
        if (aspect < 1) {
            return 1 /aspect;
        }
        return aspect;
    }
    
    /**
     * Lays out mappable nodes by the slice-dice layout.
     * @param nodes the array of the mappable nodes
     * @param start the index number of the starting point of the mappable nodes to be laid out
     * @param end the index number of the ending point of the mappable nodes to be laid out
     * @param area the area containing the mappable nodes
     */
    private void sliceDiceLayout(IMappableNode[] items, int start, int end, RectArea area) {
        if (area.width > area.height) {
            sliceDiceLayout(items, start, end, area, HORIZONTAL, ASCENDING);
        } else {
            sliceDiceLayout(items, start, end, area, VERTICAL, ASCENDING);
        }
    }
    
    /**
     * Lays out mappable nodes by the slice-dice layout.
     * @param nodes the array of the mappable nodes
     * @param start the index number of the starting point of the mappable nodes to be laid out
     * @param end the index number of the ending point of the mappable nodes to be laid out
     * @param area the area containing the mappable nodes
     * @param orient the orientation of arrangement of the mappable nodes
     * @param order the order of arrangement of the mappable nodes
     */
    private void sliceDiceLayout(IMappableNode[] nodes, int start, int end, RectArea area, int orientation, int order) {
        double total = getTotalBaseSize(nodes, start, end);
        double a = 0;
        
        for (int i = start; i <= end; i++) {
            RectArea rect = new RectArea();
            double b = nodes[i].getBaseSize() / total;
            if (orientation == VERTICAL) {
                rect.x = area.x;
                rect.width = area.width;
                if (order == ASCENDING) {
                    rect.y = area.y + area.height * a;
                } else {
                    rect.y = area.y + area.height * (1 - a - b);
                }
                rect.height = area.height * b;
                
            } else {
                if (order == ASCENDING) {
                    rect.x = area.x + area.width * a;
                } else {
                    rect.x = area.x + area.width * (1 - a - b);
                }
                rect.width = area.width * b;
                rect.y = area.y;
                rect.height = area.height;
            }
            nodes[i].setBounds(rect);
            a = a + b;
        }
    }
}
