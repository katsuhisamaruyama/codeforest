/*
 *  Copyright 2014, Katsuhisa Maruyama (maru@jtool.org)
 */

package org.jtool.codeforest.ui.view.forest;

import org.jtool.codeforest.ui.CodeForestFrame;
import javax.media.j3d.Background;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.Fog;
import javax.media.j3d.ExponentialFog;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3f;

/**
 * A 3D visual environment for a tree view.
 * @author Katsuhisa Maruyama
 */
public class TreeUniverse extends CodeForestUniverse {
    
    private BranchGroup shapeBranchGroup;
    
    public TreeUniverse(Canvas3D canvas, CodeForestFrame frame) {
        super(canvas, frame);
        this.shapeBranchGroup = new BranchGroup();
        
        setCapability(shapeBranchGroup);
        setConfigurations();
    }
    
    protected void setConfigurations() {
        super.setConfigurations();
        
        TransformGroup camera = getViewingPlatform().getViewPlatformTransform();
        Transform3D viewPos = new Transform3D();
        viewPos.setTranslation(new Vector3f(0.0f, 2.0f, 0.0f));
        camera.setTransform(viewPos);
    }
    
    public TreeCanvas3D getCanvas() {
        return getCanvas(0);
    }
    
    public TreeCanvas3D getCanvas(int i) {
        return (TreeCanvas3D)viewer[0].getCanvas3D(i);
    }
    
    public void setSceneGraph(ForestNode node) {
        if (shapeBranchGroup != null) {
            shapeBranchGroup.removeAllChildren();
        }
        
        BranchGroup bg = new BranchGroup();
        bg.setCapability(BranchGroup.ALLOW_DETACH);
        TransformGroup trans = new TransformGroup();
        bg.addChild(trans);
        
        createBackGround(trans);
        
        if (node != null) {
            TransformGroup group = node.createSceneGraphFractal();
            if (group != null) {
                bg.addChild(group);
            }
        }
        
        picker(bg);
        shapeBranchGroup.addChild(bg);
    }
    
    private void createBackGround(TransformGroup group) {
        BoundingSphere bounds = new BoundingSphere(new Point3d(), 100.0);
        Background background = new Background(new Color3f(0.95f, 0.95f, 0.85f));
        
        background.setApplicationBounds(bounds);
        group.addChild(background);
        
        Fog fog = new ExponentialFog(new Color3f(0.5f, 0.5f, 0.5f), 0.8f);
        group.addChild(fog);
    }
}
