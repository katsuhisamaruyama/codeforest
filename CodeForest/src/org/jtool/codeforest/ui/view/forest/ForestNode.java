/*
 *  Copyright 2014, Katsuhisa Maruyama (maru@jtool.org)
 */

package org.jtool.codeforest.ui.view.forest;

import org.jtool.codeforest.metrics.MetricSort;
import org.jtool.codeforest.metrics.java.ClassMetrics;
import org.jtool.codeforest.metrics.java.CommonMetrics;
import org.jtool.codeforest.metrics.java.PackageMetrics;
import org.jtool.codeforest.metrics.java.ProjectMetrics;
import org.jtool.codeforest.ui.layout.LayoutableNode;
import org.jtool.codeforest.ui.shape.ForestTree;
import org.jtool.codeforest.ui.shape.FractalTree;
import org.jtool.codeforest.ui.view.SettingData;
import javax.media.j3d.TransformGroup;

/**
 * A visual object displaying on a forest.
 * @author Katsuhisa Maruyama
 */
public class ForestNode extends LayoutableNode {
    
    /**
     * The parent of this visual object.
     */
    protected ForestNodeGroup parent;
    
    /**
     * The metrics for this visual object.
     */
    protected CommonMetrics commonMetrics;
    
    /**
     * The setting data that forms a forest
     */
    protected SettingData settingData;
    
    /**
     * Creates a visual object within a forest.
     * @param parent the parent of the visual object
     * @param data the setting data that forms the forest
     */
    public ForestNode(ForestNodeGroup parent, SettingData data) {
        super();
        this.parent = parent;
        this.settingData = data;
    }
    
    /**
     * Changes the parent of this visual object.
     * @param parent the parent of this visual object
     */
    public void changeParent(ForestNodeGroup parent) {
        this.parent = parent;
    }
    
    /**
     * Set the layout for this visual object.
     */
    public void setLayout() {
        calculateBox();
    }
    
    /**
     * Creates the scene graph for a tree within a forest and returns the tree.
     * @return the tree within the forest
     */
    public TransformGroup createSceneGraph() {
        if (!(commonMetrics instanceof ClassMetrics)) {
            return null;
        }
        
        ClassMetrics mclass = (ClassMetrics)commonMetrics;
        ForestTree tree = new ForestTree(mclass, settingData);
        tree.setUserData(this);
        
        tree.setLocation(getX(), 0, getZ());
        tree.createSceneGraph();
        
        return tree;
    }
    
    /**
     * Creates the scene graph for a fractal object and returns its object.
     * @return the visual object representing a tree.
     */
    public TransformGroup createSceneGraphFractal() {
        if (!(commonMetrics instanceof ClassMetrics)) {
            return null;
        }
        
        ClassMetrics mclass = (ClassMetrics)commonMetrics;
        FractalTree tree = new FractalTree(mclass, settingData);
        tree.setUserData(this);
        
        tree.setLocation(0, 0, 0);
        tree.createSceneGraph();
        
        return tree;
    }
    
    /**
     * Sets the base size for this visual object.
     */
    protected void setBaseSize() {
        String parameter = "";
        double baseSize = 1;
        if (parameter != null){
            if (parameter.equals(MetricSort.NUMBER_OF_FIELDS) || parameter.equals(MetricSort.NUMBER_OF_METHODS)){
                baseSize = baseSize + commonMetrics.getMetricValue(parameter);
                baseSize = baseSize * baseSize;
            } else {
                baseSize = baseSize + commonMetrics.getMetricValue(MetricSort.NUMBER_OF_FIELDS);
                baseSize = baseSize + commonMetrics.getMetricValue(MetricSort.NUMBER_OF_METHODS);
            }
        }
        
        setBaseSize(baseSize);
    }
    
    /**
     * Sets the metrics.
     * @param the metrics
     */
    public void setMetrics(CommonMetrics metrics) {
        this.commonMetrics = metrics;
    }
    
    /**
     * Returns the metrics.
     * @return the metrics
     */
    public CommonMetrics getMetrics() {
        return commonMetrics;
    }
    
    /**
     * Obtains the metrics of the project.
     * @return the project metrics
     */
    public ProjectMetrics getProjectMetrics() {
        if (commonMetrics instanceof ProjectMetrics) {
            return (ProjectMetrics)commonMetrics;
        } else if (commonMetrics instanceof PackageMetrics) {
            PackageMetrics pm = (PackageMetrics)commonMetrics;
            return pm.getProjectMetrics();
        } else if (commonMetrics instanceof ClassMetrics) {
            ClassMetrics cm = (ClassMetrics)commonMetrics;
            return cm.getPackageMetrics().getProjectMetrics();
        }
        return null;
    }
}
