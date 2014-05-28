/*
 *  Copyright 2014, Katsuhisa Maruyama (maru@jtool.org)
 */

package org.jtool.codeforest.ui.view;

import org.jtool.codeforest.metrics.IMetric;
import org.jtool.codeforest.metrics.MetricSort;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages setting data with respect to metrics and a layout.
 * @author Katsuhisa Maruyama
 */
public class SettingData {
    
    /**
     * The label representing the trunk height.
     */
    public static final String TRUNK_HEIGHT = "Trunk height";
    
    /**
     * The label representing the trunk radius.
     */
    public static final String TRUNK_RADIUS = "Trunk radius";
    
    /**
     * The label representing the trunk color.
     */
    public static final String TRUNK_COLOR = "Trunk color";
    
    /**
     * The label representing the foliage height.
     */
    public static final String FOLIAGE_HEIGHT = "Foliage height";
    
    /**
     * The label representing the foliage radius.
     */
    public static final String FOLIAGE_RADIUS = "Foliage radius";
    
    /**
     * The label representing the foliage color.
     */
    public static final String FOLIAGE_COLOR = "Foliage color";
    
    /**
     * The label representing the number of leaves.
     */
    public static final String LEAF_NUMBER = "Leaf number";
    
    /**
     * The label representing the leaf color.
     */
    public static final String LEAF_COLOR = "Leaf color";
    
    /**
     * The metric of the trunk height.
     */
    private IMetric trunkHeight = null;
    
    /**
     * The metric of the trunk radius.
     */
    private IMetric trunkRadius = null;
    
    /**
     * The metric of the trunk color.
     */
    private IMetric trunkColor = null;
    
    /**
     * The metric of the foliage height.
     */
    private IMetric foliageHeight = null;
    
    /**
     * The metric of the foliage radius.
     */
    private IMetric foliageRadius = null;
    
    /**
     * The metric of the foliage color.
     */
    private IMetric foliageColor = null;
    
    /**
     * The metric of the number of leaves.
     */
    private IMetric leafNumber = null;
    
    /**
     * The metric of the leaf color.
     */
    private IMetric leafColor = null;
    
    /**
     * A flag indicating if the update of a forest view is needed or not.
     */
    private boolean needsUpdateForestView = false;
    
    /**
     * A flag indicating if the update of a tree view is needed or not.
     */
    private boolean needsUpdateTreeView = false;
    
    /**
     * Creates a setting data.
     */
    public SettingData() {
        setTrunkHeight(getClassItems()[0]);
        setTrunkRadius(getClassItems()[0]);
        setTrunkColor(getClassItems()[0]);
        setFoliageHeight(getClassItems()[0]);
        setFoliageRadius(getClassItems()[0]);
        setFoliageColor(getClassItems()[0]);
        
        setLeafNumber(getMethodItems()[0]);
        setLeafColor(getMethodItems()[0]);
    }
    
    /**
     * Creates a setting data.
     * @param th the metric of the trunk height
     * @param tr the metric of the trunk radius
     * @param tc the metric of the trunk color
     * @param fh the metric of the foliage height
     * @param fr the metric of the foliage radius
     * @param fc the metric of the foliage color
     */
    public SettingData(String th, String tr, String tc, String fh, String fr, String fc) {
        setTrunkHeight(th);
        setTrunkRadius(th);
        setTrunkColor(tc);
        setFoliageHeight(fh);
        setFoliageRadius(fr);
        setFoliageColor(tc);
        
        setLeafNumber(getMethodItems()[0]);
        setLeafColor(getMethodItems()[0]);
    }
    
    /**
     * Creates a setting data.
     * @param th the metric of the trunk height
     * @param tr the metric of the trunk radius
     * @param tc the metric of the trunk color
     * @param fh the metric of the foliage height
     * @param fr the metric of the foliage radius
     * @param fc the metric of the foliage color
     * @param ln the metric of the number of leaves
     * @param lc the metric of the leaf color
     */
    public SettingData(IMetric th, IMetric tr, IMetric tc, IMetric fh, IMetric fr, IMetric fc, IMetric ln, IMetric lc) {
        setData(th, tr, tc, fh, fr, fc, ln, lc);
    }
    
    /**
     * Sets the metrics on visual parameters.
     * @param th the metric of the trunk height
     * @param tr the metric of the trunk radius
     * @param tc the metric of the trunk color
     * @param fh the metric of the foliage height
     * @param fr the metric of the foliage radius
     * @param fc the metric of the foliage color
     * @param ln the metric of the number of leaves
     * @param lc the metric of the leaf color
     */
    public void setData(IMetric th, IMetric tr, IMetric tc, IMetric fh, IMetric fr, IMetric fc, IMetric ln, IMetric lc) {
        trunkHeight = th;
        trunkRadius = tr;
        trunkColor = tc;
        foliageHeight = fh;
        foliageRadius = fr;
        foliageColor = fc;
        
        leafNumber = ln;
        leafColor = lc;
        
        needsUpdateForestView = true;
        needsUpdateTreeView = false;
    }
    
    /**
     * Creates a copy of this setting data.
     * @return the copy of the setting data
     */
    public SettingData cloneSettingData() {
        return new SettingData(trunkHeight, trunkRadius, trunkColor, foliageHeight, foliageRadius, foliageColor, leafNumber, leafColor);
    }
    
    /**
     * Sets the metric on the trunk height.
     * @param name the name of the metric
     */
    public void setTrunkHeight(String name) {
        needsUpdateForestView = !isSame(trunkHeight, name);
        if (needsUpdateForestView) {
            trunkHeight = getMetric(name);
        }
        
        needsUpdateTreeView = needsUpdateForestView;
    }
    
    /**
     * Sets the metric on the trunk radius.
     * @param name the name of the metric
     */
    public void setTrunkRadius(String name) {
        needsUpdateForestView = !isSame(trunkRadius, name);
        if (needsUpdateForestView) {
            trunkRadius = getMetric(name);
        }
        
        needsUpdateTreeView = needsUpdateForestView;
    }
    
    /**
     * Sets the metric on the trunk color.
     * @param name the name of the metric
     */
    public void setTrunkColor(String name) {
        needsUpdateForestView = !isSame(trunkColor, name);
        if (needsUpdateForestView) {
            trunkColor = getMetric(name);
        }
        
        needsUpdateTreeView = needsUpdateForestView;
    }
    
    /**
     * Sets the metric on the foliage height.
     * @param name the name of the metric
     */
    public void setFoliageHeight(String name) {
        needsUpdateForestView = !isSame(foliageHeight, name);
        if (needsUpdateForestView) {
            foliageHeight = getMetric(name);
        }
        
        needsUpdateTreeView = false;
    }
    
    /**
     * Sets the metric on the foliage radius.
     * @param name the name of the metric
     */
    public void setFoliageRadius(String name) {
        needsUpdateForestView = !isSame(foliageRadius, name);
        if (needsUpdateForestView) {
            foliageRadius = getMetric(name);
        }
        
        needsUpdateTreeView = false;
    }
    
    /**
     * Sets the metric on the foliage color.
     * @param name the name of the metric
     */
    public void setFoliageColor(String name) {
        needsUpdateForestView = !isSame(foliageColor, name);
        if (needsUpdateForestView) {
            foliageColor = getMetric(name);
        }
        
        needsUpdateTreeView = false;
    }
    
    /**
     * Sets the metric on the number of leaves.
     * @param name the name of the metric
     */
    public void setLeafNumber(String name) {
        needsUpdateTreeView = !isSame(leafNumber, name);
        if (needsUpdateTreeView) {
            leafNumber = getMetric(name);
        }
        
        needsUpdateForestView = false;
    }
    
    /**
     * Sets the metric on the leaf color.
     * @param name the name of the metric
     */
    public void setLeafColor(String name) {
        needsUpdateTreeView = !isSame(leafColor, name);
        if (needsUpdateTreeView) {
            leafColor = getMetric(name);
        }
        
        needsUpdateForestView = false;
    }
    
    /**
     * Returns the metric of the trunk height.
     * @return the metric of the trunk height
     */
    public IMetric getTrunkHeight() {
        return trunkHeight;
    }
    
    /**
     * Returns the metric of the trunk radius.
     * @return the metric of the trunk radius
     */
    public IMetric getTrunkRadius() {
        return trunkRadius;
    }
    
    /**
     * Returns the metric of the trunk color.
     * @return the metric of the trunk color
     */
    public IMetric getTrunkColor() {
        return trunkColor;
    }
    
    /**
     * Returns the metric of the foliage height.
     * @return the metric of the foliage height
     */
    public IMetric getFoliageHeight() {
        return foliageHeight;
    }
    
    /**
     * Returns the metric of the foliage radius.
     * @return the metric of the foliage radius
     */
    public IMetric getFoliageRadius() {
        return foliageRadius;
    }
    
    /**
     * Returns the metric of the foliage color.
     * @return the metric of the foliage color
     */
    public IMetric getFoliageColor() {
        return foliageColor;
    }
    
    /**
     * Returns the metric of the number of leaves.
     * @return the metric of the number of the leaves
     */
    public IMetric getLeafNumber() {
        return leafNumber;
    }
    
    /**
     * Returns the metric of the leaf color.
     * @return the metric of the leaf color
     */
    public IMetric getLeafColor() {
        return leafColor;
    }
    
    /**
     * Checks if the update of a forest view is needed or not.
     * @return <code>true</code> if the update is needed, otherwise <code>false</code>
     */
    public boolean needsUpdateForestView() {
        return needsUpdateForestView;
    }
    
    /**
     * Checks if the update of a tree view is needed or not.
     * @return <code>true</code> if the update is needed, otherwise <code>false</code>
     */
    public boolean needsUpdateTreeView() {
        return needsUpdateTreeView;
    }
    
    /**
     * Checks if this metric equals to a given metric.
     * @param metric the metric to be compared
     * @param name the name of the metric to be compared
     * @return <code>true</code> if both teht metrics are the same, otherwise <code>false</code>
     */
    private boolean isSame(IMetric metric, String name) {
        if (metric == null || name == null) {
            return false;
        }
        
        return metric.getName().compareTo(name) == 0;
    }
    
    /**
     * Obtains the metric having a specified name.
     * @param name the name of the metric
     * @return the metric
     */
    private IMetric getMetric(String name) {
        for (int i = 0; i < MetricSort.ALL_SELECTABLE.length; i++) {
            IMetric metric = MetricSort.ALL_SELECTABLE[i];
            if (metric.getName().compareTo(name) == 0) {
                return metric;
            }
        }
        return MetricSort.DEFAULT_METRIC;
    }
    
    /**
     * Obtains all the items related to the height.
     * @return the array of the items
     */
    public String[] getHeightItems() {
        IMetric[] metrics = MetricSort.ALL_SELECTABLE;
        List<String> items = new ArrayList<String>();
        
        for (int i = 0; i < metrics.length; i++) {
            if (metrics[i].isHeightMetric()) {
                items.add(metrics[i].getName());
            }
        }
        
        return (String[])items.toArray(new String[0]);
    }
    
    /**
     * Obtains all the items related to the width.
     * @return the array of the items
     */
    public String[] getWidthItems() {
        IMetric[] metrics = MetricSort.ALL_SELECTABLE;
        List<String> items = new ArrayList<String>();
        
        for (int i = 0; i < metrics.length; i++) {
            if (metrics[i].isWidthMetric()) {
                items.add(metrics[i].getName());
            }
        }
        
        return (String[])items.toArray(new String[0]);
    }
    
    /**
     * Obtains all the items related to a class.
     * @return the array of the class items
     */
    public String[] getClassItems() {
        List<String> items = new ArrayList<String>();
        IMetric[] metrics = MetricSort.ALL_SELECTABLE;
        
        for (int i = 0; i < metrics.length; i++) {
            if (metrics[i].isClassMetric()) {
                items.add(metrics[i].getName());
            }
        }
        
        return (String[])items.toArray(new String[0]);
    }
    
    /**
     * Obtains all the items related to a method.
     * @return the array of the method items
     */
    public String[] getMethodItems() {
        IMetric[] metrics = MetricSort.ALL_SELECTABLE;
        List<String> items = new ArrayList<String>();
        
        for (int i = 0; i < metrics.length; i++) {
            if (metrics[i].isMethodMetric()) {
                items.add(metrics[i].getName());
            }
        }
        
        return (String[])items.toArray(new String[0]);
    }
    
    /**
     * Obtains the index number of a specified item related to the height.
     * @param name the name of the item
     * @return the index number
     */
    public int getHeightItemIndex(String name) {
        String[] items = getHeightItems();
        for (int i = 0; i < items.length; i++) {
            if (items[i].compareTo(name) == 0) {
                return i;
            }
        }
        return 0;
    }
    
    /**
     * Obtains the index number of a specified item related to the width.
     * @param name the name of the item
     * @return the index number
     */
    public int getWidthItemIndex(String name) {
        String[] items = getWidthItems();
        for (int i = 0; i < items.length; i++) {
            if (items[i].compareTo(name) == 0) {
                return i;
            }
        }
        return 0;
    }
    
    /**
     * Obtains the index number of a specified item related to a class.
     * @param name the name of the class item
     * @return the index number of the class item
     */
    public int getClassItemIndex(String name) {
        String[] items = getClassItems();
        for (int i = 0; i < items.length; i++) {
            if (items[i].compareTo(name) == 0) {
                return i;
            }
        }
        return 0;
    }
    
    /**
     * Obtains the index number of a specified item related to a method.
     * @param name the name of the method item
     * @return the index number of the method item
     */
    public int getMethodItemIndex(String name) {
        String[] items = getMethodItems();
        for (int i = 0; i < items.length; i++) {
            if (items[i].compareTo(name) == 0) {
                return i;
            }
        }
        return 0;
    }
    
    /**
     * Displays information on the setting data.
     */
    public void print() {
        System.out.println("Trunk height       = " + trunkHeight.getName());
        System.out.println("Trunk radius       = " + trunkRadius.getName());
        System.out.println("Trunk color        = " + trunkColor.getName());
        System.out.println("Foliage height     = " + foliageHeight.getName());
        System.out.println("Foliage radius     = " + foliageRadius.getName());
        System.out.println("Foliage color      = " + foliageColor.getName());
        
        System.out.println("Leaf number        = " + leafNumber.getName());
        System.out.println("Leaf color         = " + leafColor.getName());
        
        System.out.println("Update forest view = " + needsUpdateForestView);
        System.out.println("Update tree view   = " + needsUpdateTreeView);
    }
}
