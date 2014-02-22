/*
 *  Copyright 2014, Katsuhisa Maruyama (maru@jtool.org)
 */

package org.jtool.codeforest.ui.view.forest;

import org.jtool.codeforest.ui.view.SettingData;
import org.jtool.codeforest.metrics.java.ClassMetrics;
import org.jtool.codeforest.metrics.java.PackageMetrics;
import org.jtool.codeforest.metrics.java.ProjectMetrics;

import java.util.HashMap;
import java.util.Map;

/**
 * Builds a forest displaying on the screen.
 * @author Katsuhisa Maruyama
 */
public class ForestBuilder {
    
    private ProjectMetrics projectMetrics;
    
    public ForestBuilder() {
    }
    
    public void setProjectMetrics(ProjectMetrics mproject) {
        projectMetrics = mproject;
    }
    
    public Forest build(SettingData data) {
        Forest forest = new Forest(data);
        forest.setData(projectMetrics);
        
        setHierarchy(forest, createHierarchyNode(forest, data));
        forest.setLayoutPosition(projectMetrics);
        return forest;
    }
    
    
    private Map<String, ForestNodeGroup> createHierarchyNode(Forest forest, SettingData data) {
        Map<String, ForestNodeGroup> group = new HashMap<String, ForestNodeGroup>();
        
        for (PackageMetrics mpackage : projectMetrics.getPackageMetrics()) {
            ForestNodeGroup clump = new ForestNodeGroup(null, data);
            clump.setData(mpackage);
            group.put(mpackage.getName(), clump);
            
            for (ClassMetrics mclass : mpackage.getClassMetrics()) {
                ForestNode shape = new ForestNode(clump, data);
                shape.setData(mclass);
                clump.add(shape);
            }
        }
        
        for (PackageMetrics mpackage : projectMetrics.getPackageMetrics()) {
            for (String name : mpackage.getEfferentPackageNames()) {
                if (group.containsKey(name)){
                    forest.addRelationNode(group.get(mpackage.getName()), group.get(name));
                }
            }
        }       
        return group;
    }
    
    private void setHierarchy(Forest forest, Map<String, ForestNodeGroup> group) {
        if (forest.isHierarchy()) {
            for (ForestNodeGroup clump : group.values()) {
                String name = clump.getPackageName();
                
                while (true) {
                    if (name.lastIndexOf(".") == -1) {
                        clump.changeParent(forest);
                        forest.add(clump);
                        break;
                    }
                    
                    name = name.substring(0, name.lastIndexOf("."));
                    ForestNodeGroup parent = group.get(name);
                    if (parent != null) {
                        clump.changeParent(parent);
                        parent.add(clump);
                        break;
                    }
                }
            }
            
        } else {
            for (ForestNodeGroup clump : group.values()) {
                forest.add(clump);
                clump.changeParent(forest);
            }
        }
    }
}
