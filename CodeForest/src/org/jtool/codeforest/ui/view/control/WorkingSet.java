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
    
    private String name;
    
    private String description;
    
    private SettingRecord settingRecord;
    
    public WorkingSet(String[] strings) {
        if (strings.length == 8) {
            name = strings[0];
            description = strings[1];
            settingRecord = new SettingRecord(strings[2], strings[3], strings[4], strings[5], strings[6], strings[7]);
        }
    }
    
    public WorkingSet(String name, String description, SettingData data) {
        this.name = name;
        this.description = description;
        settingRecord = new SettingRecord(data);
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
    
    public String getTrunkHeight() {
        return settingRecord.getTrunkHeight();
    }
    
    public String getTrunkRadius() {
        return settingRecord.getTrunkRadius();
    }
    
    public String getTrunkColor() {
        return settingRecord.getTrunkColor();
    }
    
    public String getFoliageHeight() {
        return settingRecord.getFoliageHeight();
    }
    
    public String getFoliageRadius() {
        return settingRecord.getFoliageRadius();
    }
    
    public String getFoliageColor() {
        return settingRecord.getFoliageColor();
    }
    
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
    
    public void print() {
        System.out.println("WS Name = " + name);
        System.out.println(description);
        settingRecord.print();
    }
}
