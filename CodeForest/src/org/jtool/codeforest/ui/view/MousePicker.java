/*
 *  Copyright 2014, Katsuhisa Maruyama (maru@jtool.org)
 */

package org.jtool.codeforest.ui.view;

import org.jtool.codeforest.ui.CodeForestFrame;
import org.jtool.codeforest.ui.view.forest.Forest3DView;
import org.jtool.codeforest.ui.view.forest.ForestNode;
import org.jtool.codeforest.ui.view.tree.Tree3DView;
import org.jtool.codeforest.ui.shape.ForestTree;
import javax.media.j3d.Bounds;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.Node;
import javax.media.j3d.Transform3D;
import com.sun.j3d.utils.behaviors.mouse.MouseBehaviorCallback;
import com.sun.j3d.utils.picking.PickResult;
import com.sun.j3d.utils.picking.behaviors.PickMouseBehavior;

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
    
    public Forest3DView getForest3DView() {
        return frame.getForestView();
    }
    
    public Tree3DView getTree3DView() {
        return frame.getTreeView().getTree3DView();
    }
    
    public PropertyView getPropertyView() {
        return frame.getPropertyView();
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
                if (pickedTree != null) {
                    ((ForestTree)pickedTree).changedSelected(false);
                }
                
                ((ForestTree)pickedShape).changedSelected(true);
                pickedTree = pickedShape;
                
                ForestNode node = (ForestNode)pickedShape.getUserData();
                
                getTree3DView().setSceneGraph(node);
                getTree3DView().repaint();
                
                getSourceCodeView().setMetrics(node.getMetrics());
                getPropertyView().setMetrics(node.getMetrics());
            }
        }
    }
    
    public void transformChanged(int i, Transform3D transform3d) {
    }
}