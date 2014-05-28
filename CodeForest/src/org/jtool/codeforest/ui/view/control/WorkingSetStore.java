/*
 *  Copyright 2014, Katsuhisa Maruyama (maru@jtool.org)
 */

package org.jtool.codeforest.ui.view.control;

import org.jtool.codeforest.ui.view.SettingData;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Stores working sets of metrics.
 * @author Katsuhisa Maruyama
 */
public class WorkingSetStore {
    
    /**
     * The name of a default working set.
     */
    private static final String defaultWSName = "(deault)";
    
    /**
     * The description of a default working set.
     */
    private static final String defaultWSDescription = "This is a default description."; 
    
    /**
     * A map storing information working sets.
     */
    private static Map<String, WorkingSet> workingSets = new HashMap<String, WorkingSet>();
    
    /**
     * Clears the working sets.
     */
    public static void clearWorkingSets() {
        workingSets.clear();
    }
    
    /**
     * Returns the working sets.
     * @return the list of the working sets
     */
    public static List<WorkingSet> getWorkingSets() {
        List<WorkingSet> ws = new ArrayList<WorkingSet>(workingSets.values());
        sort(ws);
        return ws;
    }
    
    /**
     * Sorts working sets.
     * @param sets the list of the working sets
     */
    private static void sort(List<? extends WorkingSet> sets) {
        Collections.sort(sets, new Comparator<WorkingSet>() {
            
            /**
             * Compares its two working sets for order.
             * @param r1 the first working set to be compared
             * @param r2 the second working set to be compared
             * @return the negative integer, zero, or a positive integer as the first argument is less than, equal to, or greater than the second
             */
            public int compare(WorkingSet r1, WorkingSet r2) {
                return r1.getName().compareTo(r2.getName());
            }
        });
    }
    
    /**
     * Returns a working set having a given name.
     * @param name the name of the working set to be retrieved
     */
    public static WorkingSet getWorkingSet(String name) {
        return workingSets.get(name);
    }
    
    /**
     * Adds a working set having a given name.
     * @param name the name of the working set to be added
     * @param description the description on the working set
     * @param data the setting data stored in the working set
     */
    public static void addWorkingSet(String name, String description, SettingData data) {
        WorkingSet newWorkingSet = new WorkingSet(name, description, data);
        workingSets.put(name, newWorkingSet);
    }
    
    /**
     * Removes a working set having a given name.
     * @param name the name of the working set to be removed
     */
    public static void removeWorkingSet(String name) {
        workingSets.remove(name);
    }
    
    /**
     * Returns a default working set.
     * @return the default working set
     */
    public static WorkingSet getDefaultWorkingSet() {
        return workingSets.get(defaultWSName);
    }
    
    /**
     * Sets a default working set.
     * @param data the setting data set to be set as a default
     */
    public static void setDefaultWorkingSet(SettingData data) {
        WorkingSet newWorkingSet = new WorkingSet(defaultWSName, defaultWSDescription, data);
        workingSets.put(defaultWSName, newWorkingSet);
    }
    
    /**
     * Sets a default working set.
     * @param workingSet the working set to be set as a default
     */
    public static void setDefaultWorkingSet(WorkingSet workingSet) {
        WorkingSet newWorkingSet = new WorkingSet(workingSet.getStrings());
        newWorkingSet.setName(defaultWSName);
        newWorkingSet.setDescription(defaultWSDescription);
        workingSets.put(defaultWSName, newWorkingSet);
    }
    
    /**
     * Checks if a default working set exists.
     * @return <code>true</code> if the default working set exists, otherwise <code>false</code>
     */
    public static boolean exitsDefaultWorkingSet() {
        WorkingSet defaultWorkingSet = WorkingSetStore.getDefaultWorkingSet();
        return defaultWorkingSet != null;
    }
    
    /**
     * Obtains the string containing information on the preference of the plug-in.
     * @return the string
     */
    public static String getPreference() {
        String pref = parse();
        return pref;
    }
    
    /**
     * Sets the preference of the plug-in.
     * @param preference the preference
     */
    public static void setPreference(String preference) {
        if (preference.length() == 0) {
            return;
        }
        
        createWorkingSets(preference);
    }
    
    /**
     * Creates the working set from a given preference.
     * @param preference the preference of the plug-in.
     */
    private static void createWorkingSets(String preference) {
        String[] strings = preference.split("\\]");
        for (String str : strings) {
            createWorkingSet(str);
        }
    }
    
    /**
     * Creates the working set from a given string.
     * @param str the string containing information on the working set
     */
    private static void createWorkingSet(String str) {
        String[] strings = str.split("\\[");
        WorkingSet workingSet = new WorkingSet(strings);
        workingSets.put(workingSet.getName(), workingSet);
    }
    
    /**
     * Parses the map containing information on the working set and returns its string.
     * @return the string containing information on the working set
     */
    private static String parse() {
        StringBuilder sb = new StringBuilder();
        List<WorkingSet> ws = new ArrayList<WorkingSet>(workingSets.values());
        for (int i = 0; i < ws.size(); i++) {
            WorkingSet workingSet = ws.get(i);
            sb.append(parse(workingSet.getStrings()));
            if (i != workingSets.size() - 1) {
                sb.append("]");
            }
        }
        return sb.toString();
    }
    
    /**
     * Parses the array of strings containing information on the working set and returns its string.
     * @param strings the array of strings containing information on the working set.
     * @return the string containing information on the working set
     */
    private static String parse(String[] strings) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < strings.length; i++) {
            sb.append(strings[i]);
            if (i != strings.length - 1) {
                sb.append("[");
            }
        }
        return sb.toString();
    }
}
