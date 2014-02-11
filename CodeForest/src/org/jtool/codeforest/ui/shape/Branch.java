/*
 *  Copyright 2014, Katsuhisa Maruyama (maru@jtool.org)
 */

package org.jtool.codeforest.ui.shape;

import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;

/**
 * Represents a branch of a tree on a tree view.
 * @author Daiki Todoroki
 * @author Katsuhisa Maruyama
 */
public class Branch extends FractalShape {
    
    public Branch(FractalTree tree, int generation) {
        super();
        
        setGeometry(tree.branchGeometry);
        setAppearance(tree.branchAppearance);
        
        Transform3D branchTransform = new Transform3D();
        getTransform(branchTransform);
        
        Transform3D groupTransform = new Transform3D();
        groupTransform.mul(branchTransform);
        groupTransform.mul(tree.transform);
        
        TransformGroup group = new TransformGroup();
        group.setTransform(groupTransform);
        group.addChild(shape);
        addChild(group);
        
        if (generation < tree.branchLevelLimit) {
            Transform3D child1Transform = new Transform3D();
            child1Transform.mul(branchTransform);
            
            Transform3D child2Transform =  new Transform3D();
            child2Transform.mul(branchTransform);
            
            FractalShape child1 = new Branch(tree, generation + 1);
            child1Transform.mul(tree.childTransform[0]);
            
            FractalShape child2 = new Branch(tree, generation + 1);
            child2Transform.mul(tree.childTransform[1]);
            
            child1.setTransform(child1Transform);
            addChild(child1);
            
            child2.setTransform(child2Transform);
            addChild(child2);
            
        } else if (generation == tree.branchLevelLimit) {
            
            Leaves leaves = new Leaves(tree);
            leaves.draw(tree, this, branchTransform);
            
        } else {
            return;
        }
    }
}
