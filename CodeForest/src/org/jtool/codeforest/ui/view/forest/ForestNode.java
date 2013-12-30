/*
 *  Copyright 2013, Katsuhisa Maruyama (maru@jtool.org)
 */

package org.jtool.codeforest.ui.view.forest;

import org.jtool.codeforest.metrics.MetricSort;
import org.jtool.codeforest.metrics.IMetric;
import org.jtool.codeforest.metrics.UnsupportedMetricsException;
import org.jtool.codeforest.metrics.java.ClassMetrics;
import org.jtool.codeforest.metrics.java.CommonMetrics;
import org.jtool.codeforest.metrics.java.PackageMetrics;
import org.jtool.codeforest.metrics.java.ProjectMetrics;
import org.jtool.codeforest.ui.shape.ForestTree;
import org.jtool.codeforest.ui.shape.FractalTree;

import javax.media.j3d.TransformGroup;

/**
 * A visible item displaying on a forest.
 * @author Daiki Todoroki
 * @author Katsuhisa Maruyama
 */
public class ForestNode extends LayoutableNode {
    
    protected ForestNodeGroup parent;
    
    protected CommonMetrics commonMetrics;
    
    protected ForestData forestData;
    
    public ForestNode(ForestNodeGroup parent, ForestData fdata) {
        super();
        this.parent = parent;
        this.forestData = fdata;
    }
    
    public void changeParent(ForestNodeGroup parent) {
        this.parent = parent;
    }
    
    public void setLayout() {
        calculateBox();
    }
    
    public TransformGroup createSceneGraph() {
        if (!(commonMetrics instanceof ClassMetrics)) {
            return null;
        }
        
        ClassMetrics mclass = (ClassMetrics)commonMetrics;
        ForestTree tree = new ForestTree(getProjectMetrics(), mclass, forestData);
        tree.setUserData(this);
        
        tree.setLocation(getX(), 0, getZ());
        tree.createSceneGraph();
        
        return tree;
    }
    
    public TransformGroup createSceneGraphFractal() {
        if (!(commonMetrics instanceof ClassMetrics)) {
            return null;
        }
        
        ClassMetrics mclass = (ClassMetrics)commonMetrics;
        FractalTree tree = new FractalTree(getProjectMetrics(), mclass, forestData);
        tree.setUserData(this);
        
        tree.setLocation(0, 0, 0);
        tree.createSceneGraph();
        
        return tree;
    }
    
    // TODO
    protected void setBaseSize() {
        String parameter = "";
        double baseSize = 1;
        if(parameter != null){
            if (parameter.equals(MetricSort.NUMBER_OF_FIELDS)
                    || parameter.equals(MetricSort.NUMBER_OF_METHODS)){
                baseSize = baseSize + commonMetrics.getMetricValue(parameter);
                baseSize = baseSize * baseSize;
            } else {
                baseSize = baseSize + commonMetrics.getMetricValue(MetricSort.NUMBER_OF_FIELDS);
                baseSize = baseSize + commonMetrics.getMetricValue(MetricSort.NUMBER_OF_METHODS);
            }
        }
        
        setBaseSize(baseSize);
    }
    
    public void setData(CommonMetrics metrics) {
        this.commonMetrics = metrics;
    }
    
    public CommonMetrics getMetrics() {
        return commonMetrics;
    }
    
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
