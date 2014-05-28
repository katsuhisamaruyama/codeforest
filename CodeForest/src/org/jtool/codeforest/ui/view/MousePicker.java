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
 * @author Katsuhisa Maruyama
 * @author Daiki Todoroki
 */
public class MousePicker extends PickMouseBehavior implements MouseBehaviorCallback {
    
    /**
     * The main frame.
     */
    private CodeForestFrame frame;
    
    /**
     * A tree currently picked.
     */
    private Node pickedTree;
    
    /**
     * Creates a picker of a mouse event on a view.
     * @param canvas3d the canvas in which a mouse event occurs
     * @param branchgroup the branch group of the scene graph
     * @param bounds the bounds of the canvas
     * @param frame the main frame
     */
    public MousePicker(Canvas3D canvas3d, BranchGroup branchgroup, Bounds bounds, CodeForestFrame frame) {
        super(canvas3d, branchgroup, bounds);
        setSchedulingBounds(bounds);
        this.frame = frame;
    }
    
    /**
     * Returns the forest view.
     * @return the forest view
     */
    public ForestView getForestView() {
        return frame.getForestView();
    }
    
    /**
     * Returns the tree view.
     * @return the tree view
     */
    public TreeView getTreeView() {
        return frame.getTreeView();
    }
    
    /**
     * Returns the memo view.
     * @return the memo view
     */
    public MemoView getMemoView() {
        return frame.getMemoView();
    }
    
    /**
     * Returns the property view.
     * @return the property view
     */
    public PropertyView getPropertyView() {
        return frame.getPropertyView();
    }
    
    /**
     * Returns the interaction view.
     * @return the interaction view
     */
    public InteractionView getInteractionView() {
        return frame.getInteractionView();
    }
    
    /**
     * Returns the source code view.
     * @return the source code view
     */
    public SourceCodeView getSourceCodeView() {
        return frame.getSourceCodeView();
    }
    
    /**
     * Updates the scene graph.
     * @param x the x-position of a visual node on a view
     * @param y the y-position of a visual node on a view
     */
    public void updateScene(int x, int y) {
        pickCanvas.setShapeLocation(x, y);
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
    
    /**
     * Invoked when a visual object in the forest view is picked.
     * @param pickedShape the visual node currently picked
     */
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
    
    /**
     * Invoked when a visual object in the tree view is picked.
     * @param pickedShape the visual node currently picked
     */
    private void treeViewAction(Node pickedShape) {
        Leaf leaf = (Leaf)pickedShape.getUserData();
        
        if (mevent.getButton() == MouseEvent.BUTTON1) {
            // For future extension
            System.out.println(leaf.getMethodMetrics().getSignature());
        }
    }
    
    /**
     * Invoked when left button of the mouse is pressed.
     * @param node the visual node in the scene graph
     */
    private void pressLeftButton(ForestNode node) {
        getTreeView().setSceneGraph(node);
        getTreeView().repaint();
        
        CommonMetrics metrics = node.getMetrics();
        if (metrics instanceof ClassMetrics) {
            ClassMetrics mclass = (ClassMetrics)metrics;
            
            getSourceCodeView().changeSelection(mclass.getJavaClass());
            getPropertyView().changeSelection(mclass.getQualifiedName());
            getMemoView().changeSelection(mclass.getQualifiedName());
            
            getInteractionView().recordFocusAction(frame.getSettingData(), mclass.getQualifiedName());
        }
    }
    
    /**
     * Invoked when right button of the mouse is pressed.
     * @param node the visual node in the scene graph
     */
    private void pressRightButton(ForestNode node) {
        getTreeView().setSceneGraph(node);
        getTreeView().repaint();
        
        CommonMetrics metrics = node.getMetrics();
        if (metrics instanceof ClassMetrics) {
            ClassMetrics mclass = (ClassMetrics)metrics;
            getMemoView().writeMemo(mclass.getQualifiedName());
        }
    }
    
    /**
     * Invoked every time the mouse behavior updates the the transformation of visual nodes.
     * @param type the type of the transformation
     * @param transform3d the transformation of the node
     */
    public void transformChanged(int type, Transform3D transform3d) {
    }
}
