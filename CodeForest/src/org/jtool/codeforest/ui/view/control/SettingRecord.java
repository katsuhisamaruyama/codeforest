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
    
    /**
     * Returns the name of the metric of the trunk height.
     * @return the metric name
     */
    public String getTrunkHeight() {
        return trunkHeightMetricName;
    }
    
    /**
     * Returns the name of the metric of the trunk radius.
     * @return the metric name
     */
    public String getTrunkRadius() {
        return trunkRadiusMetricName;
    }
    
    /**
     * Returns the name of the metric of the trunk color.
     * @return the metric name
     */
    public String getTrunkColor() {
        return trunkColorMetricName;
    }
    
    /**
     * Returns the name of the metric of the foliage height.
     * @return the metric name
     */
    public String getFoliageHeight() {
        return foliageHeightMetricName;
    }
    
    /**
     * Returns the name of the metric of the foliage radius.
     * @return the metric name
     */
    public String getFoliageRadius() {
        return foliageRadiusMetricName;
    }
    
    /**
     * Returns the name of the metric of the foliage color.
     * @return the metric name
     */
    public String getFoliageColor() {
        return foliageColorMetricName;
    }
    
    /**
     * Displays information on this setting record.
     */
    public void print() {
        System.out.println("Trunk height  = " + trunkHeightMetricName);
        System.out.println("Trunk radius  = " + trunkRadiusMetricName);
        System.out.println("Trunk color   = " + trunkColorMetricName);
        System.out.println("Foliage height = " + foliageHeightMetricName);
        System.out.println("Foliage radius = " + foliageRadiusMetricName);
        System.out.println("Foliage color  = " + foliageColorMetricName);
    }
}
