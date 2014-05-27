/*
 *  Copyright 2014, Katsuhisa Maruyama (maru@jtool.org)
 */

package org.jtool.codeforest.ui.view.forest;

import org.jtool.codeforest.ui.view.SettingData;
import org.jtool.codeforest.metrics.MetricSort;
import org.jtool.codeforest.metrics.java.ProjectMetrics;
import javax.media.j3d.Background;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

/**
 * Represents a forest displaying on the screen.
 * @author Katsuhisa Maruyama
 * @author Daiki Todoroki
 */
public class Forest extends ForestNodeGroup {
    
    /**
     * The size for the layout for a forest.
     */
    public double layoutSize;
    
    /**
     * Creates a forest with the setting data.
     * @param data the setting data that forms the forest
     */
    protected Forest(SettingData data) {
        super(null, data);
    }
    
    /**
     * This node has the hierarchy.
     * @return always <code>true</code>
     */
    protected boolean isHierarchy() {
        return true;
    }
    
    /**
     * Sets the layout position.
     * @param mproject the project corresponding to a forest
     */
    void setLayoutPosition(ProjectMetrics mproject) {
        layoutSize = Math.sqrt(mproject.getMetricValue(MetricSort.NUMBER_OF_CLASSES));
        if (layoutSize > 160.0) {
            layoutSize = 160.0;
        }
        contentVisible = true;
        
        setBounds(0, 0, layoutSize, layoutSize);
        setBaseSize();
        setLayout();
    }
    
    /**
     * Creates a scene graph containing a given branch group.
     * @param forest the branch group representing a forest
     */
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
    
    /**
     * Creates the background of a tree view.
     * @param group the transform group of the scene graph
     */
    private void createBackground(TransformGroup group) {
        BoundingSphere bounds = new BoundingSphere(new Point3d(), 100.0);
        Background background = new Background(new Color3f(0.95f, 0.95f, 0.95f));
        background.setApplicationBounds(bounds);
        group.addChild(background);
    }
    
    /**
     * Creates a scene graph and returns its transform group.
     * @return the transform group of the scene graph
     */
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
    
    /**
     * Sets capabilities for the branch group of the scene graph.
     * @param bg the branch group of the scene graph
     */
    private void setCapability(BranchGroup branch) {
        branch.setCapability(BranchGroup.ALLOW_DETACH);
        branch.setCapability(BranchGroup.ALLOW_PICKABLE_READ);
        branch.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
        branch.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);
        branch.setCapability(BranchGroup.ALLOW_CHILDREN_READ);
    }
}
