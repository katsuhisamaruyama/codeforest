/*
 *  Copyright 2014, Katsuhisa Maruyama (maru@jtool.org)
 */

package org.jtool.codeforest.ui.view.control;

import org.jtool.codeforest.ui.view.SettingData;

/**
 * Stores setting data as a working set of metrics.
 * @author Katsuhisa Maruyama
 */
public class SettingRecord {
    
    private String trunkHeightMetricName;
    private String trunkRadiusMetricName;
    private String trunkColorMetricName;
    private String foliageHeightMetricName;
    private String foliageRadiusMetricName;
    private String foliageColorMetricName;
    
    public SettingRecord(String th, String tr, String tc, String fh, String fr, String fc) {
        trunkHeightMetricName = th;
        trunkRadiusMetricName = tr;
        trunkColorMetricName = tc;
        foliageHeightMetricName = fh;
        foliageRadiusMetricName = fr;
        foliageColorMetricName = fc;
    }
    
    public SettingRecord(SettingData data) {
        trunkHeightMetricName = data.getTrunkHeight().getName();
        trunkRadiusMetricName = data.getTrunkRadius().getName();
        trunkColorMetricName = data.getTrunkColor().getName();
        foliageHeightMetricName = data.getFoliageHeight().getName();
        foliageRadiusMetricName = data.getFoliageRadius().getName();
        foliageColorMetricName = data.getFoliageColor().getName();
    }
    
    public String getTrunkHeight() {
        return trunkHeightMetricName;
    }
    
    public String getTrunkRadius() {
        return trunkRadiusMetricName;
    }
    
    public String getTrunkColor() {
        return trunkColorMetricName;
    }
    
    public String getFoliageHeight() {
        return foliageHeightMetricName;
    }
    
    public String getFoliageRadius() {
        return foliageRadiusMetricName;
    }
    
    public String getFoliageColor() {
        return foliageColorMetricName;
    }
    
    public void print() {
        System.out.println("Trunk height  = " + trunkHeightMetricName);
        System.out.println("Trunk radius  = " + trunkRadiusMetricName);
        System.out.println("Trunk color   = " + trunkColorMetricName);
        System.out.println("Foliage height = " + foliageHeightMetricName);
        System.out.println("Foliage radius = " + foliageRadiusMetricName);
        System.out.println("Foliage color  = " + foliageColorMetricName);
    }
}
