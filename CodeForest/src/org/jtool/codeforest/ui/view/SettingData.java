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
    
    public static final String TRUNK_HEIGHT = "Trunk height";
    public static final String TRUNK_RADIUS = "Trunk radius";
    public static final String TRUNK_COLOR = "Trunk color";
    public static final String FOLIAGE_HEIGHT = "Foliage height";
    public static final String FOLIAGE_RADIUS = "Foliage radius";
    public static final String FOLIAGE_COLOR = "Foliage color";
    
    public static final String LEAF_NUMBER = "Leaf number";
    public static final String LEAF_COLOR = "Leaf color";
    
    private IMetric trunkHeight = null;
    private IMetric trunkRadius = null;
    private IMetric trunkColor = null;
    private IMetric foliageHeight = null;
    private IMetric foliageRadius = null;
    private IMetric foliageColor = null;
    
    private IMetric leafNumber = null;
    private IMetric leafColor = null;
    
    private boolean needsUpdateForestView = false;
    
    private boolean needsUpdateTreeView = false;
    
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
    
    public SettingData(IMetric th, IMetric tr, IMetric tc, IMetric fh, IMetric fr, IMetric fc, IMetric ln, IMetric lc) {
        setData(th, tr, tc, fh, fr, fc, ln, lc);
    }
    
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
    
    public SettingData cloneSettingData() {
        return new SettingData(trunkHeight, trunkRadius, trunkColor, foliageHeight, foliageRadius, foliageColor, leafNumber, leafColor);
    }
    
    public void setTrunkHeight(String name) {
        needsUpdateForestView = !isSame(trunkHeight, name);
        if (needsUpdateForestView) {
            trunkHeight = getMetric(name);
        }
        
        needsUpdateTreeView = needsUpdateForestView;
    }
    
    public void setTrunkRadius(String name) {
        needsUpdateForestView = !isSame(trunkRadius, name);
        if (needsUpdateForestView) {
            trunkRadius = getMetric(name);
        }
        
        needsUpdateTreeView = needsUpdateForestView;
    }
    
    public void setTrunkColor(String name) {
        needsUpdateForestView = !isSame(trunkColor, name);
        if (needsUpdateForestView) {
            trunkColor = getMetric(name);
        }
        
        needsUpdateTreeView = needsUpdateForestView;
    }
    
    public void setFoliageHeight(String name) {
        needsUpdateForestView = !isSame(foliageHeight, name);
        if (needsUpdateForestView) {
            foliageHeight = getMetric(name);
        }
        
        needsUpdateTreeView = false;
    }
    
    public void setFoliageRadius(String name) {
        needsUpdateForestView = !isSame(foliageRadius, name);
        if (needsUpdateForestView) {
            foliageRadius = getMetric(name);
        }
        
        needsUpdateTreeView = false;
    }
    
    public void setFoliageColor(String name) {
        needsUpdateForestView = !isSame(foliageColor, name);
        if (needsUpdateForestView) {
            foliageColor = getMetric(name);
        }
        
        needsUpdateTreeView = false;
    }
    
    public void setLeafNumber(String name) {
        needsUpdateTreeView = !isSame(leafNumber, name);
        if (needsUpdateTreeView) {
            leafNumber = getMetric(name);
        }
        
        needsUpdateForestView = false;
    }
    
    public void setLeafColor(String name) {
        needsUpdateTreeView = !isSame(leafColor, name);
        if (needsUpdateTreeView) {
            leafColor = getMetric(name);
        }
        
        needsUpdateForestView = false;
    }
    
    public IMetric getTrunkHeight() {
        return trunkHeight;
    }
    
    public IMetric getTrunkRadius() {
        return trunkRadius;
    }
    
    public IMetric getTrunkColor() {
        return trunkColor;
    }
    
    public IMetric getFoliageHeight() {
        return foliageHeight;
    }
    
    public IMetric getFoliageRadius() {
        return foliageRadius;
    }
    
    public IMetric getFoliageColor() {
        return foliageColor;
    }
    
    public IMetric getLeafNumber() {
        return leafNumber;
    }
    
    public IMetric getLeafColor() {
        return leafColor;
    }
    
    public boolean needsUpdateForestView() {
        return needsUpdateForestView;
    }
    
    public boolean needsUpdateTreeView() {
        return needsUpdateTreeView;
    }
    
    private boolean isSame(IMetric metric, String name) {
        if (metric == null || name == null) {
            return false;
        }
        
        return metric.getName().compareTo(name) == 0;
    }
    
    private IMetric getMetric(String name) {
        for (int i = 0; i < MetricSort.ALL_SELECTABLE.length; i++) {
            IMetric metric = MetricSort.ALL_SELECTABLE[i];
            if (metric.getName().compareTo(name) == 0) {
                return metric;
            }
        }
        return MetricSort.DEFAULT_METRIC;
    }
    
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
    
    public int getHeightItemIndex(String name) {
        String[] items = getHeightItems();
        for (int i = 0; i < items.length; i++) {
            if (items[i].compareTo(name) == 0) {
                return i;
            }
        }
        return 0;
    }
    
    public int getWidthItemIndex(String name) {
        String[] items = getWidthItems();
        for (int i = 0; i < items.length; i++) {
            if (items[i].compareTo(name) == 0) {
                return i;
            }
        }
        return 0;
    }
    
    public int getClassItemIndex(String name) {
        String[] items = getClassItems();
        for (int i = 0; i < items.length; i++) {
            if (items[i].compareTo(name) == 0) {
                return i;
            }
        }
        return 0;
    }
    
    public int getMethodItemIndex(String name) {
        String[] items = getMethodItems();
        for (int i = 0; i < items.length; i++) {
            if (items[i].compareTo(name) == 0) {
                return i;
            }
        }
        return 0;
    }
    
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
