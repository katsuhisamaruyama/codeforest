/*
 *  Copyright 2014, Katsuhisa Maruyama (maru@jtool.org)
 */

package org.jtool.codeforest.ui.layout;

import java.util.Comparator;
import java.util.Arrays;

/**
 * A class for a map layout.
 * @author Katsuhisa Maruyama
 * @author Daiki Todoroki
 */
public abstract class MapLayout {
    
    public static final int VERTICAL = 0;
    
    public static final int HORIZONTAL = 1;
    
    public static final int ASCENDING = 0;
    
    public static final int DESCENDING = 1;
    
    public abstract void layout(IMappableNode[] items, RectArea bounds);
    
    /**
     * Lays out mappable nodes in an area.
     * @param model the model managing mappable nodes
     * @param bounds the bounds of the area
     */
    public void layout(IMapModel model, RectArea area) {
        layout(model.getNodes(), area);
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
        layout(model, new RectArea(x, y, width, height));
    }
    
    /**
     * Obtains the total of the base sizes of all mappable nodes.
     * @param nodes the array of all the mappable nodes
     * @return the total of the base sizes
     */
    public static double getTotalBaseSize(IMappableNode[] nodes) {
        return getTotalBaseSize(nodes, 0, nodes.length - 1);
    }
    
    /**
     * Obtains the total of the base sizes of mappable nodes.
     * @param nodes the array of all the mappable nodes
     * @param start the index number of the starting point of the mappable nodes affecting the total size
     * @param end the the index number of the ending point of the mappable nodes affecting the total size
     * @return the total of the base sizes
     */
    public static double getTotalBaseSize(IMappableNode[] nodes, int start, int end) {
        double total = 0;
        for (int i = start; i <= end; i++) {
            total = total + nodes[i].getBaseSize();
        }
        return total;
    }
    
    /**
     * Obtains the mappable nodes in descending order.
     * @param nodes the array of the mappable nodes to be sorted
     * @return the array of the mappable nodes in descending order
     */
    public IMappableNode[] sortDescending(IMappableNode[] nodes) {
        IMappableNode[] sortedItems = new IMappableNode[nodes.length];
        System.arraycopy(nodes, 0, sortedItems, 0, nodes.length);
        
        Arrays.sort(sortedItems, new NodeComparator());
        return sortedItems;
    }
    
    /**
     * Compares mappable nodes
     * @author Katsuhisa Maruyama
     * @author Daiki Todoriki
     */
    class NodeComparator implements Comparator<IMappableNode> {
        
        /**
         * Compares the base sizes of two mappable nodes and returns their order.
         * @return a negative integer, zero, or a positive integer as the first argument is less than, equal to, or greater than the second
         */
        public int compare(IMappableNode n1, IMappableNode n2) {
            if (n1.getBaseSize() > n2.getBaseSize()) {
                return -1;
            } else if (n1.getBaseSize() == n2.getBaseSize()) {
                return 0;
            } else {
                return 1;
            }
        }
    }
}
