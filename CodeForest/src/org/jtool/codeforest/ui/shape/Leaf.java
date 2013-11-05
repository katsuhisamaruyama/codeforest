/*
 *  Copyright 2013, Katsuhisa Maruyama (maru@jtool.org)
 */

package org.jtool.codeforest.ui.shape;

import javax.media.j3d.Shape3D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Vector3f;

/**
 * Represents a leaf of a tree on a tree view.
 * @author Daiki Todoroki
 * @author Katsuhisa Maruyama
 */
public class Leaf extends FractalShape {
    
    private static final double LEAF_SCALE = 0.7;
    
    public Leaf(FractalTree tree, Leaves leaves, float moveY) {
        super();
        
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
}
