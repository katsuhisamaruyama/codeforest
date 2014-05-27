/*
 *  Copyright 2014, Katsuhisa Maruyama (maru@jtool.org)
 */

package org.jtool.codeforest.ui.view.forest;

import org.jtool.codeforest.ui.CodeForestFrame;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;

/**
 * A visual environment for a forest view.
 * @author Katsuhisa Maruyama
 * @author Daiki Todoroki
 */
public class ForestUniverse extends CodeForestUniverse {
    
    /**
     * The branch group of the scene graph.
     */
    private final BranchGroup forestBranchGroup;
    
    /**
     * A forest.
     */
    private Forest forest;
    
    /**
     * Creates a visual environment for a forest view.
     * @param canvas a drawing canvas
     * @param frame the main frame
     */
    public ForestUniverse(Canvas3D canvas, CodeForestFrame frame) {
        super(canvas, frame);
        forestBranchGroup = new BranchGroup();
        
        setCapability(forestBranchGroup);
        setConfiguration();
    }
    
    /**
     * Sets configuration of a forest view.
     */
    protected void setConfiguration() {
        super.setConfiguration();
    }
    
    /**
     * Repaints this virtual environment.
     */
    public void repaint() {
        setForest(forest);
    }
    
    /**
     * Sets a given forest on the scene graph.
     * @param forest the forest
     */
    public void setForest(Forest forest) {
        this.forest = forest;
        if (forestBranchGroup != null) {
            forestBranchGroup.removeAllChildren();
        }
        
        BranchGroup bg = new BranchGroup();
        this.forest.createSceneGraph(bg);
        
        picker(bg);
        forestBranchGroup.addChild(bg);
    }
}
