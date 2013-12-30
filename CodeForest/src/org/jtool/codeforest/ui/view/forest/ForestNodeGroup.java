/*
 *  Copyright 2013, Katsuhisa Maruyama (maru@jtool.org)
 */

package org.jtool.codeforest.ui.view.forest;

import org.jtool.codeforest.ui.layout.IMapModel;
import org.jtool.codeforest.ui.layout.IMappable;
import org.jtool.codeforest.ui.shape.Ground;
import org.jtool.codeforest.metrics.java.PackageMetrics;

import javax.media.j3d.TransformGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Groups nodes constructing a forest.
 * @author Daiki Todoroki
 * @author Katsuhisa Maruyama
 */
public class ForestNodeGroup extends ForestNode implements IMapModel {
    private boolean layoutValid;
    
    protected boolean contentVisible = true;
    
    protected List<ForestNode> nodes;
    
    public ForestNodeGroup(ForestNodeGroup parent, ForestData fdata) {
        super(parent, fdata);
        nodes = new ArrayList<ForestNode>();
    }
    
    protected void setBaseSize() {
        double baseSize = 0;
        for (int i = 0; i < nodes.size(); i++) {
            ForestNode shape = (ForestNode)nodes.get(i);
            shape.setBaseSize();
            baseSize = baseSize + shape.getBaseSize();
        }
        setBaseSize(baseSize);
    }
    
    public IMappable[] getItems() {
        IMappable[] mappable = new IMappable[nodes.size()];
        for(int i = 0; i < nodes.size(); i++) {
            mappable[i] = nodes.get(i);
        }
        return mappable;
    }
    
    public ForestNode[] getChildren() {
        return nodes.toArray(new ForestNode[0]);
    }
    
    protected void checkLayout() {
        if (!layoutValid) {
            if (nodes.size() != 0) {
                layout(this, getBounds());
            }
            layoutValid = true;
        }
    }
    
    public void setLayout() {
        checkLayout();
        calculateBox();
        if (contentVisible) {
            for (int i = 0; i < nodes.size(); i++) {
                nodes.get(i).setLayout();
            }
        }
    }
    
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
    
    public void add(ForestNode shape) {
        nodes.add(shape);
    }
    
    public String getPackageName() {
        PackageMetrics pm = (PackageMetrics)commonMetrics;
        return pm.getName();
    }
}
