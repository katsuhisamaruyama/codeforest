/*
 *  Copyright 2014, Katsuhisa Maruyama (maru@jtool.org)
 */

package org.jtool.codeforest.ui.view;

import org.jtool.codeforest.metrics.java.ClassMetrics;
import org.jtool.codeforest.metrics.java.CommonMetrics;
import org.jtool.codeforest.ui.CodeForestFrame;
import org.jtool.codeforest.ui.view.forest.ForestView;
import org.jtool.codeforest.ui.view.forest.ForestNode;
import org.jtool.codeforest.ui.view.forest.TreeView;
import org.jtool.codeforest.ui.view.control.MemoView;
import org.jtool.codeforest.ui.view.control.InteractionView;
import org.jtool.codeforest.ui.shape.ForestTree;
import org.jtool.codeforest.ui.shape.Leaf;
import javax.media.j3d.Bounds;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.Node;
import javax.media.j3d.Transform3D;

import com.sun.j3d.utils.behaviors.mouse.MouseBehaviorCallback;
import com.sun.j3d.utils.picking.PickResult;
import com.sun.j3d.utils.picking.behaviors.PickMouseBehavior;

import java.awt.event.MouseEvent;

/**
 * Picks a mouse event on a view.
 * @author Daiki Todoroki
 * @author Katsuhisa Maruyama
 */
public class MousePicker extends PickMouseBehavior implements MouseBehaviorCallback {
    
    private CodeForestFrame frame;
    
    private Node pickedTree;
    
    public MousePicker(Canvas3D canvas3d, BranchGroup branchgroup, Bounds bounds, CodeForestFrame frame) {
        super(canvas3d, branchgroup, bounds);
        setSchedulingBounds(bounds);
        this.frame = frame;
    }
    
    public ForestView getForestView() {
        return frame.getForestView();
    }
    
    public TreeView getTreeView() {
        return frame.getTreeView();
    }
    
    public MemoView getMemoView() {
        return frame.getMemoView();
    }
    
    public PropertyView getPropertyView() {
        return frame.getPropertyView();
    }
    
    public InteractionView getInteractionView() {
        return frame.getInteractionView();
    }
    
    public SourceCodeView getSourceCodeView() {
        return frame.getSourceCodeView();
    }
    
    public void updateScene(int xpos, int ypos) {
        pickCanvas.setShapeLocation(xpos, ypos);
        PickResult pickResult = pickCanvas.pickClosest();
        
        if (pickResult != null) {
            Node pickedShape = pickResult.getNode(PickResult.TRANSFORM_GROUP);
            
            if (pickedShape instanceof ForestTree) {
                forestViewAction(pickedShape);
                
            } else if (pickedShape instanceof Leaf) {
                treeViewAction(pickedShape);
            }
        }
    }
    
    private void forestViewAction(Node pickedShape) {
        if (pickedTree != null) {
            ((ForestTree)pickedTree).changedSelected(false);
        }
        
        ((ForestTree)pickedShape).changedSelected(true);
        pickedTree = pickedShape;
        
        ForestNode node = (ForestNode)pickedShape.getUserData();
        
        if (mevent.getButton() == MouseEvent.BUTTON1) {
            pressLeftButton(node);
        } else if (mevent.getButton() == MouseEvent.BUTTON3) {
            pressRightButton(node);
        }
    }
    
    private void treeViewAction(Node pickedShape) {
        Leaf leaf = (Leaf)pickedShape.getUserData();
        
        if (mevent.getButton() == MouseEvent.BUTTON1) {
            System.out.println(leaf.getMethodMetrics().getSignature());
        }
    }
    
    private void pressLeftButton(ForestNode node) {
        getTreeView().setSceneGraph(node);
        getTreeView().repaint();
        
        CommonMetrics metrics = node.getMetrics();
        if (metrics instanceof ClassMetrics) {
            ClassMetrics mclass = (ClassMetrics)metrics;
            
            getSourceCodeView().changeSelection(mclass.getJavaClass());
            getPropertyView().changeSelection(mclass.getQualifiedName());
            getMemoView().changeSelection(mclass.getQualifiedName());
            
            getInteractionView().recordFocusClassAction(frame.getSettingData(), mclass.getQualifiedName());
        }
    }
    
    private void pressRightButton(ForestNode node) {
        getTreeView().setSceneGraph(node);
        getTreeView().repaint();
        
        CommonMetrics metrics = node.getMetrics();
        if (metrics instanceof ClassMetrics) {
            ClassMetrics mclass = (ClassMetrics)metrics;
            getMemoView().addMemo(mclass.getQualifiedName());
        }
    }
    
    public void transformChanged(int i, Transform3D transform3d) {
    }
}