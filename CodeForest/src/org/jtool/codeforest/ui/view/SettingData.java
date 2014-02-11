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
    
    static final String TRUNK_HEIGHT = "Trunk height";
    static final String TRUNK_RADIUS = "Trunk radius";
    static final String TRUNK_COLOR = "Trunk color";
    static final String FOLIAGE_HEIGHT = "Foliage height";
    static final String FOLIAGE_RADIUS = "Foliage radius";
    static final String FOLIAGE_COLOR = "Foliage color";
    
    static final String BRANCH_LENGTH = "Branch length";
    static final String LEAF_NUMBER = "Leaf number";
    static final String LEAF_SIZE = "Leaf size";
    static final String LEAF_COLOR = "Leaf color";
    
    private IMetric trunkHeight = null;
    private IMetric trunkRadius = null;
    private IMetric trunkColor = null;
    private IMetric foliageHeight = null;
    private IMetric foliageRadius = null;
    private IMetric foliageColor = null;
    
    private IMetric branchLength = null;
    private IMetric leafNumber = null;
    private IMetric leafSize = null;
    private IMetric leafColor = null;
    
    private boolean needsUpdateForestView = false;
    
    private boolean needsUpdateTreeView = false;
    
    public SettingData() {
    }
    
    void setTrunkHeight(String name) {
        needsUpdateForestView = !isSame(trunkHeight, name);
        if (needsUpdateForestView) {
            trunkHeight = getMetric(name);
        }
        
        needsUpdateTreeView = needsUpdateForestView;
    }
    
    void setTrunkRadius(String name) {
        needsUpdateForestView = !isSame(trunkRadius, name);
        if (needsUpdateForestView) {
            trunkRadius = getMetric(name);
        }
        
        needsUpdateTreeView = needsUpdateForestView;
    }
    
    void setTrunkColor(String name) {
        needsUpdateForestView = !isSame(trunkColor, name);
        if (needsUpdateForestView) {
            trunkColor = getMetric(name);
        }
        
        needsUpdateTreeView = needsUpdateForestView;
    }
    
    void setFoliageHeight(String name) {
        needsUpdateForestView = !isSame(foliageHeight, name);
        if (needsUpdateForestView) {
            foliageHeight = getMetric(name);
        }
        
        needsUpdateTreeView = false;
    }
    
    void setFoliageRadius(String name) {
        needsUpdateForestView = !isSame(foliageRadius, name);
        if (needsUpdateForestView) {
            foliageRadius = getMetric(name);
        }
        
        needsUpdateTreeView = false;
    }
    
    void setFoliageColor(String name) {
        needsUpdateForestView = !isSame(foliageColor, name);
        if (needsUpdateForestView) {
            foliageColor = getMetric(name);
        }
        
        needsUpdateTreeView = false;
    }
    
    void setBranchLength(String name) {
        needsUpdateTreeView = !isSame(branchLength, name);
        if (needsUpdateTreeView) {
            branchLength = getMetric(name);
        }
        
        needsUpdateForestView = false;
    }
    
    void setLeafNumber(String name) {
        needsUpdateTreeView = !isSame(leafNumber, name);
        if (needsUpdateTreeView) {
            leafNumber = getMetric(name);
        }
        
        needsUpdateForestView = false;
    }
    
    void setLeafSize(String name) {
        needsUpdateTreeView = !isSame(leafSize, name);
        if (needsUpdateTreeView) {
            leafSize = getMetric(name);
        }
        
        needsUpdateForestView = false;
    }
    
    void setLeafColor(String name) {
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
    
    public IMetric getBranchLength() {
        return branchLength;
    }
    
    public IMetric getLeafNumber() {
        return leafNumber;
    }
    
    public IMetric getLeafSize() {
        return leafSize;
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
    
    String[] getHeightItems(boolean bool) {
        IMetric[] metrics = MetricSort.ALL_SELECTABLE;
        List<String> items = new ArrayList<String>();
        
        if (bool) {
            items.add(MetricSort.DEFAULT_METRIC.getName());
        }
        for (int i = 0; i < metrics.length; i++) {
            if (metrics[i].isHeightMetric()) {
                items.add(metrics[i].getName());
            }
        }
        
        return (String[])items.toArray(new String[0]);
    }
    
    String[] getWidthItems(boolean bool) {
        IMetric[] metrics = MetricSort.ALL_SELECTABLE;
        List<String> items = new ArrayList<String>();
        
        if (bool) {
            items.add(MetricSort.DEFAULT_METRIC.getName());
        }
        for (int i = 0; i < metrics.length; i++) {
            if (metrics[i].isWidthMetric()) {
                items.add(metrics[i].getName());
            }
        }
        
        return (String[])items.toArray(new String[0]);
    }
    
    String[] getClassItems(boolean bool) {
        List<String> items = new ArrayList<String>();
        IMetric[] metrics = MetricSort.ALL_SELECTABLE;
        
        if (bool) {
            items.add(MetricSort.DEFAULT_METRIC.getName());
        }
        
        for (int i = 0; i < metrics.length; i++) {
            if (metrics[i].isClassMetric()) {
                items.add(metrics[i].getName());
            }
        }
        
        return (String[])items.toArray(new String[0]);
    }
    
    String[] getMethodItems(boolean bool) {
        IMetric[] metrics = MetricSort.ALL_SELECTABLE;
        List<String> items = new ArrayList<String>();
        
        if (bool) {
            items.add(MetricSort.DEFAULT_METRIC.getName());
        }
        for (int i = 0; i < metrics.length; i++) {
            if (metrics[i].isMethodMetric()) {
                items.add(metrics[i].getName());
            }
        }
        
        return (String[])items.toArray(new String[0]);
    }
    
    public void print() {
        System.out.println("Trunk height       = " + trunkHeight.getName());
        System.out.println("Trunk radius       = " + trunkRadius.getName());
        System.out.println("Trunk color        = " + trunkColor.getName());
        System.out.println("Foliage height     = " + foliageHeight.getName());
        System.out.println("Foliage radius     = " + foliageRadius.getName());
        System.out.println("Foliage color      = " + foliageColor.getName());
        
        System.out.println("Branch length      = " + branchLength.getName());
        System.out.println("Leaf number        = " + leafNumber.getName());
        System.out.println("Leaf size          = " + leafSize.getName());
        System.out.println("Leaf color         = " + leafColor.getName());
        
        System.out.println("Update forest view = " + needsUpdateForestView);
        System.out.println("Update tree view   = " + needsUpdateTreeView);
    }
}
