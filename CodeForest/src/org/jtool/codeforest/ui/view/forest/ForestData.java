/*
 *  Copyright 2014, Katsuhisa Maruyama (maru@jtool.org)
 */

package org.jtool.codeforest.ui.view.forest;

import org.jtool.codeforest.ui.view.SettingData;
import org.jtool.codeforest.metrics.IMetric;

/**
 * Manages setting data with respect to metrics and a layout.
 * @author Katsuhisa Maruyama
 */
public class ForestData {
    
    private SettingData settingData;
    
    private IMetric trunkHeight;
    private IMetric trunkRadius;
    private IMetric trunkColor;
    private IMetric foliageHeight;
    private IMetric foliageRadius;
    private IMetric foliageColor;
    
    private IMetric branchLength;
    private IMetric leafNumber;
    private IMetric leafSize;
    private IMetric leafColor;
    
    public ForestData(SettingData data) {
        settingData = data;
        
        update();
    }
    
    public ForestData(SettingData data, ForestData fdata) {
        settingData = data;
        
        trunkHeight = fdata.getTrunkHeight();
        trunkRadius = fdata.getTrunkRadius();
        trunkColor = fdata.getTrunkColor();
        foliageHeight = fdata.getFoliageHeight();
        foliageRadius = fdata.getFoliageRadius();
        foliageColor = fdata.getFoliageColor();
        
        branchLength = fdata.getBranchLength();
        leafNumber = fdata.getLeafNumber();
        leafSize = fdata.getLeafNumber();
        leafColor = fdata.getLeafColor();
    }
    
    public void update() {
        trunkHeight = settingData.getTrunkHeight();
        trunkRadius = settingData.getTrunkRadius();
        trunkColor = settingData.getTrunkColor();
        foliageHeight = settingData.getFoliageHeight();
        foliageRadius = settingData.getFoliageRadius();
        foliageColor = settingData.getFoliageColor();
        
        branchLength = settingData.getBranchLength();
        leafNumber = settingData.getLeafNumber();
        leafSize = settingData.getLeafNumber();
        leafColor = settingData.getLeafColor();
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
    }
}
