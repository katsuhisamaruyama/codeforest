/*
 *  Copyright 2014, Katsuhisa Maruyama (maru@jtool.org)
 */

package org.jtool.codeforest.ui.view.forest;

import org.jtool.codeforest.ui.layout.IMapModel;
import org.jtool.codeforest.ui.layout.IMappable;
import org.jtool.codeforest.ui.shape.Ground;
import org.jtool.codeforest.ui.view.SettingData;
import org.jtool.codeforest.metrics.java.PackageMetrics;

import javax.media.j3d.TransformGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Groups nodes constructing a forest.
 * @author Katsuhisa Maruyama
 */
public class ForestNodeGroup extends ForestNode implements IMapModel {
    
    /**
     * A flag indicating the layout for this group is valid or not. 
     */
    private boolean layoutValid;
    
    /**
     * A flag indicating that objects within this group are visible or not.
     */
    protected boolean contentVisible = true;
    
    /**
     * The collection of visual objects within this group. 
     */
    protected List<ForestNode> nodes;
    
    /**
     * Creates a group containing visual objects.
     * @param parent the parent of this group
     * @param data the setting data that forms visual objects within this group
     */
    public ForestNodeGroup(ForestNodeGroup parent, SettingData data) {
        super(parent, data);
        nodes = new ArrayList<ForestNode>();
    }
    
    /**
     * Sets the base size of this group.
     */
    protected void setBaseSize() {
        double baseSize = 0;
        for (int i = 0; i < nodes.size(); i++) {
            ForestNode shape = (ForestNode)nodes.get(i);
            shape.setBaseSize();
            baseSize = baseSize + shape.getBaseSize();
        }
        setBaseSize(baseSize);
    }
    
    /**
     * Obtains the mappable items within this group.
     * @return the array of mappable items
     */
    public IMappable[] getItems() {
        IMappable[] mappable = new IMappable[nodes.size()];
        for(int i = 0; i < nodes.size(); i++) {
            mappable[i] = nodes.get(i);
        }
        return mappable;
    }
    
    /**
     * Obtains children of this group.
     * @return the array of visual objects
     */
    public ForestNode[] getChildren() {
        return nodes.toArray(new ForestNode[0]);
    }
    
    /**
     * Checks the layout  for this group.
     */
    protected void checkLayout() {
        if (!layoutValid) {
            if (nodes.size() != 0) {
                setLayout(this, getBounds());
            }
            layoutValid = true;
        }
    }
    
    /**
     * Sets the layout for this group.
     */
    public void setLayout() {
        checkLayout();
        calculateBox();
        if (contentVisible) {
            for (int i = 0; i < nodes.size(); i++) {
                nodes.get(i).setLayout();
            }
        }
    }
    
    /**
     * Creates the scene graph containing visual objects in this group.
     */
    public TransformGroup createSceneGraph() {
        checkLayout();
        
        Ground shape = new Ground(getWidth(), getLength(), 0.01d);
        shape.setRiver(true);
        shape.setLocation(getX(), 0, getZ());
        shape.createSceneGraph();
        
        TransformGroup trans = new TransformGroup();
        trans.addChild(shape);
        
        for (int i = 0; i < nodes.size(); i++) {
            TransformGroup group = ((ForestNode)nodes.get(i)).createSceneGraph();
            if (group != null) {
                trans.addChild(group);
            }
        }
        
        return trans;
    }
    
    /**
     * Adds a visual object into this group.
     * @param shape
     */
    public void add(ForestNode shape) {
        nodes.add(shape);
    }
    
    /**
     * Obtains the name of the package corresponding to this group.
     * @return the package name
     */
    public String getPackageName() {
        PackageMetrics pm = (PackageMetrics)commonMetrics;
        return pm.getName();
    }
}
