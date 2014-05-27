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
 * @author Daiki Todoroki
 */
public class ForestBuilder {
    
    /**
     * The metrics of a project corresponding to a forest.
     */
    private ProjectMetrics projectMetrics;
    
    /**
     * Creates a builder that builds a forest.
     * @param mproject the project metrics
     */
    public ForestBuilder(ProjectMetrics mproject) {
        projectMetrics = mproject;
    }
    
    /**
     * Builds a forest.
     * @param data the setting data that forms a forest
     * @return the forest
     */
    public Forest build(SettingData data) {
        Forest forest = new Forest(data);
        forest.setMetrics(projectMetrics);
        
        setHierarchy(forest, createHierarchyNode(forest, data));
        forest.setLayoutPosition(projectMetrics);
        return forest;
    }
    
    /**
     * Creates a node with the hierarchy regarding packages.
     * @param forest the forest
     * @param data the setting data that forms the forest
     * @return the map between the name of package and its containing visual objects
     */
    private Map<String, ForestNodeGroup> createHierarchyNode(Forest forest, SettingData data) {
        Map<String, ForestNodeGroup> map = new HashMap<String, ForestNodeGroup>();
        
        for (PackageMetrics mpackage : projectMetrics.getPackageMetrics()) {
            ForestNodeGroup clump = new ForestNodeGroup(null, data);
            clump.setMetrics(mpackage);
            map.put(mpackage.getName(), clump);
            
            for (ClassMetrics mclass : mpackage.getClassMetrics()) {
                ForestNode shape = new ForestNode(clump, data);
                shape.setMetrics(mclass);
                clump.add(shape);
            }
        }
        
        return map;
    }
    
    /**
     * Sets the hierarchy within the forest.
     * @param forest the forest
     * @param map the map between the name of package and its containing visual objects
     */
    private void setHierarchy(Forest forest, Map<String, ForestNodeGroup> map) {
        if (forest.isHierarchy()) {
            for (ForestNodeGroup clump : map.values()) {
                String name = clump.getPackageName();
                
                while (true) {
                    if (name.lastIndexOf(".") == -1) {
                        clump.changeParent(forest);
                        forest.add(clump);
                        break;
                    }
                    
                    name = name.substring(0, name.lastIndexOf("."));
                    ForestNodeGroup parent = map.get(name);
                    if (parent != null) {
                        clump.changeParent(parent);
                        parent.add(clump);
                        break;
                    }
                }
            }
            
        } else {
            for (ForestNodeGroup clump : map.values()) {
                forest.add(clump);
                clump.changeParent(forest);
            }
        }
    }
}
