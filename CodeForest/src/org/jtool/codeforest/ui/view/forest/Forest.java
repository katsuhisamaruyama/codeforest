/*
 *  Copyright 2013, Katsuhisa Maruyama (maru@jtool.org)
 */

package org.jtool.codeforest.ui.view.forest;

import org.jtool.codeforest.java.metrics.ProjectMetrics;
import org.jtool.codeforest.metrics.MetricSort;

import javax.media.j3d.Background;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a forest displaying on the screen.
 * @author Daiki Todoroki
 * @author Katsuhisa Maruyama
 */
public class Forest extends ForestNodeGroup {
   
    public double layoutSize;
    
    private List<ForestNodePair> pairs = new ArrayList<ForestNodePair>();
    
    protected Forest(ForestData fdata) {
        super(null, fdata);
    }
    
    protected boolean isHierarchy() {
        return true;
    }
    
    void setLayoutPosition(ProjectMetrics mproject) {
        layoutSize = Math.sqrt(getProjectMetrics().getMetricValue(MetricSort.NUMBER_OF_CLASSES));
        if (layoutSize > 16.0) {
            layoutSize = 16.0;
        }
        contentVisible = true;
        
        setBounds(0, 0, layoutSize, layoutSize);
        setBaseSize();
        setLayout();
    }
    
    public void addRelationNode(ForestNodeGroup group1, ForestNodeGroup group2) {
        pairs.add(new ForestNodePair(0, group1, group2));
    }
    
    private void createBackground(TransformGroup group) {
        BoundingSphere bounds = new BoundingSphere(new Point3d(), 100.0);
        Background background = new Background(new Color3f(0.95f, 0.95f, 0.95f));
        background.setApplicationBounds(bounds);
        group.addChild(background);
    }
    
    public void createSceneGraph(final BranchGroup forest) {
        setCapability(forest);
        
        TransformGroup background = new TransformGroup();
        forest.addChild(background);
        createBackground(background);
        
        TransformGroup transG = new TransformGroup();
        Transform3D trans3D = new Transform3D();
        trans3D.setTranslation(new Vector3d(-layoutSize / 2, 0.0d, -layoutSize / 2));
        transG.setTransform(trans3D);
        forest.addChild(transG);
        transG.addChild(createSceneGraph());
    }
    
    public TransformGroup createSceneGraph() {
        TransformGroup trans = new TransformGroup();
        
        for (int i = 0; i < nodes.size(); i++) {
            ForestNode node = nodes.get(i);
            TransformGroup group = node.createSceneGraph();
            if (group != null) {
                trans.addChild(group);
            }
        }
        return trans;
    }
    
    private void setCapability(BranchGroup branch) {
        branch.setCapability(BranchGroup.ALLOW_DETACH);
        branch.setCapability(BranchGroup.ALLOW_PICKABLE_READ);
        branch.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
        branch.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);
        branch.setCapability(BranchGroup.ALLOW_CHILDREN_READ);
    }
}
