/*
 *  Copyright 2013, Katsuhisa Maruyama (maru@jtool.org)
 */

package org.jtool.codeforest.ui.view.forest;

import org.jtool.codeforest.java.metrics.ClassMetrics;
import org.jtool.codeforest.java.metrics.PackageMetrics;
import org.jtool.codeforest.java.metrics.ProjectMetrics;

import java.util.HashMap;
import java.util.Map;

/**
 * Builds a forest displaying on the screen.
 * @author Daiki Todoroki
 * @author Katsuhisa Maruyama
 */
public class ForestBuilder {
    
    private ProjectMetrics projectMetrics;
    
    private String layoutName;
    
    public ForestBuilder() {
    }
    
    public void setProjectMetrics(ProjectMetrics mproject) {
        projectMetrics = mproject;
    }
    
    public void setLayout(String name) {
        layoutName = name;
    }
    
    public Forest build(ForestData fdata) {
        Forest forest = new Forest(fdata);
        forest.setData(projectMetrics);
        forest.setLayout(layoutName);
        
        setHierarchy(forest, createHierarchyNode(forest, fdata));
        forest.setLayoutPosition(projectMetrics);
        return forest;
    }
    
    
    private Map<String, ForestNodeGroup> createHierarchyNode(Forest forest, ForestData fdata) {
        Map<String, ForestNodeGroup> group = new HashMap<String, ForestNodeGroup>();
        
        for (PackageMetrics mpackage : projectMetrics.getPackageMetrics()) {
            ForestNodeGroup clump = new ForestNodeGroup(null, fdata);
            clump.setData(mpackage);
            group.put(mpackage.getName(), clump);
            
            for (ClassMetrics mclass : mpackage.getClassMetrics()) {
                ForestNode shape = new ForestNode(clump, fdata);
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
