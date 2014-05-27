/*
 *  Copyright 2014, Katsuhisa Maruyama (maru@jtool.org)
 */

package org.jtool.codeforest.ui.shape;

import org.jtool.codeforest.metrics.java.MethodMetrics;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Vector3f;

/**
 * Represents a leaf of a tree on a tree view.
 * @author Katsuhisa Maruyama
 * @author Daiki Todoroki
 */
public class Leaf extends TransformGroup {
    
    /**
     * The scale of the leaf size.
     */
    private static final double LEAF_SCALE = 0.75;
    
    /**
     * Leaves containing this leaf.
     */
    private Leaves leaves;
    
    /**
     * Creates a leaf.
     * @param tree a tree having the leaf
     * @param leaves leaves on the tree
     * @param moveY the value of moving along y-axis
     */
    public Leaf(FractalTree tree, Leaves leaves, float moveY) {
        this.leaves = leaves;
        
        setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        
        TransformGroup transform = new TransformGroup();
        createLeaf(transform, tree, leaves, moveY);
        
        Transform3D thisTransform = new Transform3D();
        getTransform(thisTransform);
        
        Transform3D groupTransform = new Transform3D();
        groupTransform.mul(thisTransform);
        groupTransform.mul(tree.transform);
        
        TransformGroup group = new TransformGroup();
        group.setTransform(groupTransform);
        group.addChild(transform);
        addChild(group);
    }
    
    /**
     * Creates a leaf.
     * @param transform the transformation for the leaf
     * @param tree a tree having the leaf
     * @param leaves leaves on the tree
     * @param moveY the value of moving along y-axis
     */
    private void createLeaf(TransformGroup transform, FractalTree tree, Leaves leaves, float moveY) {
        TransformGroup branchTransform = new TransformGroup();
        Shape3D branch = new Shape3D();
        branch.addGeometry(tree.branchGeometry);
        branch.setAppearance(tree.branchAppearance);
        
        branchTransform.addChild(branch);
        transform.addChild(branchTransform);
        
        TransformGroup leafTransform = new TransformGroup();
        Shape3D leaf = new Shape3D();
        leaf.addGeometry(leaves.leafGeometry);
        leaf.setAppearance(leaves.leafAppearance);
        
        Transform3D scaleTransform = new Transform3D();
        scaleTransform.setScale(LEAF_SCALE);
        
        Transform3D moveTransform = new Transform3D();
        moveTransform.setTranslation(new Vector3f(0.0f, 1.0f - moveY * 3, 0.0f));
        moveTransform.mul(scaleTransform);
        leafTransform.setTransform(moveTransform);
        
        leafTransform.addChild(leaf);
        transform.addChild(leafTransform);
    }
    
    /**
     * Obtains the metrics for a method.
     * @return the method metrics
     */
    public MethodMetrics getMethodMetrics() {
        return leaves.getMethodMetrics();
    }
}
