/*
 *  Copyright 2014, Katsuhisa Maruyama (maru@jtool.org)
 */

package org.jtool.codeforest.ui.view.forest;

import org.jtool.codeforest.ui.CodeForestFrame;
import org.jtool.codeforest.ui.view.CodeForestUniverse;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;

/**
 * A 3D visual environment for a forest view.
 * @author Katsuhisa Maruyama
 */
public class ForestUniverse extends CodeForestUniverse {
    
    private final BranchGroup forestBranchGroup;
    
    private Forest forest;
    
    public ForestUniverse(Canvas3D canvas, CodeForestFrame frame) {
        super(canvas, frame);
        forestBranchGroup = new BranchGroup();
        
        setCapability(forestBranchGroup);
        setConfigurations();
    }
    
    public void repaint() {
        setForest(forest);
    }
    
    public void setForest(Forest world) {
        this.forest = world;
        if (forestBranchGroup != null) {
            forestBranchGroup.removeAllChildren();
        }
        
        BranchGroup bg = new BranchGroup();
        forest.createSceneGraph(bg);
        
        picker(bg);
        forestBranchGroup.addChild(bg);
    }
}
