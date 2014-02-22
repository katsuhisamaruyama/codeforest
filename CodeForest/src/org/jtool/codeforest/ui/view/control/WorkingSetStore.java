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
    
    private static final String defaultWSName = "(deault)";
    
    private static final String defaultWSDescription = "This is a default description."; 
    
    private static Map<String, WorkingSet> workingSets = new HashMap<String, WorkingSet>();
    
    public static void clearWorkingSets() {
        workingSets.clear();
    }
    
    public static List<WorkingSet> getWorkingSets() {
        List<WorkingSet> ws = new ArrayList<WorkingSet>(workingSets.values());
        sort(ws);
        return ws;
    }
    
    private static void sort(List<? extends WorkingSet> sets) {
        Collections.sort(sets, new Comparator<WorkingSet>() {
            
            public int compare(WorkingSet r1, WorkingSet r2) {
                return r1.getName().compareTo(r2.getName());
            }
        });
    }
    
    public static WorkingSet getWorkingSet(String name) {
        return workingSets.get(name);
    }
    
    public static void addWorkingSet(String name, String description, SettingData data) {
        WorkingSet newWorkingSet = new WorkingSet(name, description, data);
        workingSets.put(name, newWorkingSet);
    }
    
    public static void removeWorkingSet(String name) {
        workingSets.remove(name);
    }
    
    public static WorkingSet getDefaultWorkingSet() {
        return workingSets.get(defaultWSName);
    }
    
    public static void setDefaultWorkingSet(SettingData data) {
        WorkingSet newWorkingSet = new WorkingSet(defaultWSName, defaultWSDescription, data);
        workingSets.put(defaultWSName, newWorkingSet);
    }
    
    public static void setDefaultWorkingSet(WorkingSet workingSet) {
        WorkingSet newWorkingSet = new WorkingSet(workingSet.getStrings());
        newWorkingSet.setName(defaultWSName);
        newWorkingSet.setDescription(defaultWSDescription);
        workingSets.put(defaultWSName, newWorkingSet);
    }
    
    public static boolean exitsDefaultWorkingSet() {
        WorkingSet defaultWorkingSet = WorkingSetStore.getDefaultWorkingSet();
        return defaultWorkingSet != null;
    }
    
    public static String getPreference() {
        String pref = parse(workingSets);
        return pref;
    }
    
    public static void setPreference(String preference) {
        if (preference.length() == 0) {
            return;
        }
        
        createWorkingSets(preference);
    }
    
    private static void createWorkingSets(String preference) {
        String[] strings = preference.split("\\]");
        for (String str : strings) {
            createWorkingSet(str);
        }
    }
    
    private static void createWorkingSet(String str) {
        String[] strings = str.split("\\[");
        WorkingSet workingSet = new WorkingSet(strings);
        workingSets.put(workingSet.getName(), workingSet);
    }
    
    private static String parse(Map<String, WorkingSet> sets) {
        StringBuilder sb = new StringBuilder();
        List<WorkingSet> ws = new ArrayList<WorkingSet>(workingSets.values());
        for (int i = 0; i < ws.size(); i++) {
            WorkingSet workingSet = ws.get(i);
            sb.append(parse(workingSet.getStrings()));
            if (i != sets.size() - 1) {
                sb.append("]");
            }
        }
        return sb.toString();
    }
    
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
