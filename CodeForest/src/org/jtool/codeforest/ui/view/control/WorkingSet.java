/*
 *  Copyright 2014, Katsuhisa Maruyama (maru@jtool.org)
 */

package org.jtool.codeforest.ui.view.control;

import org.jtool.codeforest.ui.view.SettingData;

/**
 * Stores setting data as a working set of metrics.
 * @author Katsuhisa Maruyama
 */
public class WorkingSet {
    
    /**
     * The name the name of this working set.
     */
    private String name;
    
    /**
     * The description on this working set.
     */
    private String description;
    
    /**
     * The setting data stored in this working set.
     */
    private SettingRecord settingRecord;
    
    /**
     * Creates a working set.
     * @param strings the array of strings containing information on the working set
     */
    public WorkingSet(String[] strings) {
        if (strings.length == 8) {
            name = strings[0];
            description = strings[1];
            settingRecord = new SettingRecord(strings[2], strings[3], strings[4], strings[5], strings[6], strings[7]);
        }
    }
    
    /**
     * Creates a working set.
     * @param name the name of the working set
     * @param description the description on the working set
     * @param data the setting data stored in the working set
     */
    public WorkingSet(String name, String description, SettingData data) {
        this.name = name;
        this.description = description;
        settingRecord = new SettingRecord(data);
    }
    
    /**
     * Sets the name of the working set.
     * @param name the name of the working set
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * Returns the name of the working set.
     * @return the name of the working set
     */
    public String getName() {
        return name;
    }
    
    /**
     * Sets the description on the working set.
     * @param description the description on the working set
     */
    public void setDescription(String description) {
        this.description = description;
    }
    
    /**
     * Returns the description on the working set.
     * @return the description on the working set
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * Returns the name of the metric of the trunk height.
     * @return the metric name
     */
    public String getTrunkHeight() {
        return settingRecord.getTrunkHeight();
    }
    
    /**
     * Returns the name of the metric of the trunk radius.
     * @return the metric name
     */
    public String getTrunkRadius() {
        return settingRecord.getTrunkRadius();
    }
    
    /**
     * Returns the name of the metric of the trunk color.
     * @return the metric name
     */
    public String getTrunkColor() {
        return settingRecord.getTrunkColor();
    }
    
    /**
     * Returns the name of the metric of the foliage height.
     * @return the metric name
     */
    public String getFoliageHeight() {
        return settingRecord.getFoliageHeight();
    }
    
    /**
     * Returns the name of the metric of the foliage radius.
     * @return the metric name
     */
    public String getFoliageRadius() {
        return settingRecord.getFoliageRadius();
    }
    
    /**
     * Returns the name of the metric of the foliage color.
     * @return the metric name
     */
    public String getFoliageColor() {
        return settingRecord.getFoliageColor();
    }
    
    /**
     * Obtains the array of strings containing information on the working set.
     * @return the string array
     */
    public String[] getStrings() {
        String[] strings = new String[8];
        strings[0] = name;
        strings[1] = description;
        
        strings[2] = getTrunkHeight();
        strings[3] = getTrunkRadius();
        strings[4] = getTrunkColor();
        strings[5] = getFoliageHeight();
        strings[6] = getFoliageRadius();
        strings[7] = getFoliageColor();
        
        return strings;
    }
    
    /**
     * Displays information on the working set.
     */
    public void print() {
        System.out.println("WS Name = " + name);
        System.out.println(description);
        settingRecord.print();
    }
}
