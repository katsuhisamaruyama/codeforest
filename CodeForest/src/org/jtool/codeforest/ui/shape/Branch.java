/*
 *  Copyright 2014, Katsuhisa Maruyama (maru@jtool.org)
 */

package org.jtool.codeforest.ui.shape;

import org.jtool.codeforest.metrics.java.MethodMetrics;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;

/**
 * Represents a branch of a tree on a tree view.
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
        
        if (generation < tree.getBranchLevelLimit() - 1) {
            branch(tree, tree.childTransform[0], branchTransform, generation);
            branch(tree, tree.childTransform[1], branchTransform, generation);
            
        } else if (generation == tree.getBranchLevelLimit() - 1) {
            
            if (tree.lessThanBranchMax()) {
                
                if (tree.branchTobeCreated()) {
                    MethodMetrics mmethod = tree.getMethodMetrics();
                    
                    branch(tree, tree.childTransform[0], branchTransform, generation);
                    
                    Leaves leaves = new Leaves(tree, mmethod);
                    leaves.draw(tree, this, branchTransform);
                    
                }
                
                tree.incrementBranch();
            }
            
            if (tree.lessThanBranchMax()) {
                
                if (tree.branchTobeCreated()) {
                    MethodMetrics mmethod = tree.getMethodMetrics();
                    
                    branch(tree, tree.childTransform[1], branchTransform, generation);
                    
                    Leaves leaves = new Leaves(tree, mmethod);
                    leaves.draw(tree, this, branchTransform);
                }
                
                tree.incrementBranch();
            }
        } else {
            return;
        }
    }
    
    private void branch(FractalTree tree, Transform3D trans, Transform3D branchTransform, int generation) {
        Transform3D child2Transform =  new Transform3D();
        child2Transform.mul(branchTransform);
        
        FractalShape child2 = new Branch(tree, generation + 1);
        child2Transform.mul(trans);
        
        child2.setTransform(child2Transform);
        addChild(child2);
    }
}
