/*
 *  Copyright 2014, Katsuhisa Maruyama (maru@jtool.org)
 */

package org.jtool.codeforest.ui.view.control;

import org.eclipse.swt.graphics.Image;
import org.jtool.codeforest.Activator;
import org.jtool.codeforest.ui.view.SettingData;
import org.jtool.codeforest.util.Time;

/**
 * Stores an interaction.
 * @author Katsuhisa Maruyama
 */
public class InteractionRecord {
    
    private long time;
    
    private String description;
    
    private int type;
    
    private SettingRecord settingRecord;
    
    private static final Image settingIcon = Activator.getImage("setting");
    private static final Image workingSetIcon = Activator.getImage("workset");
    private static final Image focusClassSetIcon = Activator.getImage("inspect");
    private static final Image memoIcon = Activator.getImage("editor");
    
    static final int SYSTEM = 0;
    static final int SETTING = 1;
    static final int WORKING_SET = 2;
    static final int FOCUS_CLASS = 3;
    static final int MEMO = 4;
    static final int OTHERS = 5;
    
    public InteractionRecord(long time, String description, int type, SettingData data) {
        this.time = time;
        this.description = description;
        this.type = type;
        settingRecord = new SettingRecord(data);
    }
    
    public long getTime() {
        return time;
    }
    
    public String getTimeRepresentation() {
        return Time.toString(time);
    }
    
    void setDescription(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
    
    public int getType() {
        return type;
    }
    
    public boolean isSettingInteraction() {
        return type == SETTING;
    }
    
    public boolean isWorkingSetInteraction() {
        return type == WORKING_SET;
    }
    
    public boolean isFocusClassInteraction() {
        return type == FOCUS_CLASS;
    }
    
    public Image getImage() {
        if (type == SETTING) {
            return settingIcon;
        } else if (type == WORKING_SET) {
            return workingSetIcon;
        } else if (type == FOCUS_CLASS) {
            return focusClassSetIcon;
        } else if (type == MEMO) {
            return memoIcon;
        }
        return null;
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
    
    public void print() {
        System.out.println("Record = " + getTimeRepresentation() + " " + getDescription());
        settingRecord.print();
    }
}
