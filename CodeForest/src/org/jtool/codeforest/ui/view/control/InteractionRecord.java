/*
 *  Copyright 2014, Katsuhisa Maruyama (maru@jtool.org)
 */

package org.jtool.codeforest.ui.view.control;

import org.eclipse.swt.graphics.Image;
import org.jtool.codeforest.Activator;
import org.jtool.codeforest.ui.view.SettingData;
import org.jtool.codeforest.util.Time;

/**
 * Represents an interaction record.
 * @author Katsuhisa Maruyama
 */
public class InteractionRecord {
    
    /**
     * The time when the interaction occurred
     */
    private long time;
    
    /**
     * The description of the interaction.
     */
    private String description;
    
    /**
     * The type of the interaction.
     */
    private int type;
    
    /**
     * The type of the interaction.
     */
    private SettingRecord settingRecord;
    
    /**
     * Icons representing respective interactions.
     */
    private static final Image settingIcon = Activator.getImage("setting");
    private static final Image workingSetIcon = Activator.getImage("workset");
    private static final Image focusClassSetIcon = Activator.getImage("inspect");
    private static final Image memoIcon = Activator.getImage("editor");
    
    /**
     * The sorts of interactions.
     */
    static final int SYSTEM = 0;
    static final int SETTING = 1;
    static final int WORKING_SET = 2;
    static final int FOCUS_CLASS = 3;
    static final int MEMO = 4;
    static final int OTHERS = 5;
    
    /**
     * Creates an interaction record.
     * @param time the time when the interaction occurred
     * @param description the description of the interaction
     * @param type the type of the interaction
     * @param data the setting data when the interaction occurred
     */
    public InteractionRecord(long time, String description, int type, SettingData data) {
        this.time = time;
        this.description = description;
        this.type = type;
        settingRecord = new SettingRecord(data);
    }
    
    /**
     * Returns the time when the interaction occurred
     * @return the interaction time
     */
    public long getTime() {
        return time;
    }
    
    /**
     * Returns the string of the time when the interaction occurred
     * @return the interaction time string in the usual form
     */
    public String getTimeRepresentation() {
        return Time.toString(time);
    }
    
    /**
     * Sets the description of the interaction.
     * @param description the description of the interaction
     */
    void setDescription(String description) {
        this.description = description;
    }
    
    /**
     * Returns the description of the interaction.
     * @return the description of the interaction
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * Returns the type of the interaction.
     * @return the type of the interaction
     */
    public int getType() {
        return type;
    }
    
    /**
     * Checks if the interaction type is related the setting.
     * @return <code>true</code> if the interaction type is related the setting, otherwise <code>false</code>
     */
    public boolean isSettingInteraction() {
        return type == SETTING;
    }
    
    /**
     * Checks if the interaction type is related the working set.
     * @return <code>true</code> if the interaction type is related the working set, otherwise <code>false</code>
     */
    public boolean isWorkingSetInteraction() {
        return type == WORKING_SET;
    }
    
    /**
     * Checks if the interaction type is related the focus.
     * @return <code>true</code> if the interaction type is related the focus, otherwise <code>false</code>
     */
    public boolean isFocusInteraction() {
        return type == FOCUS_CLASS;
    }
    
    /**
     * Checks if the interaction type is related the memo.
     * @return <code>true</code> if the interaction type is related the memo, otherwise <code>false</code>
     */
    public boolean isMemoInteraction() {
        return type == MEMO;
    }
    
    /**
     * Returns the image corresponding to the interaction.
     * @return the image
     */
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
     * Displays information on this interaction record.
     */
    public void print() {
        System.out.println("Record = " + getTimeRepresentation() + " " + getDescription());
        settingRecord.print();
    }
}
