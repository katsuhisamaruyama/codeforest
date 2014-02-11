/*
 *  Copyright 2014, Katsuhisa Maruyama (maru@jtool.org)
 */

package org.jtool.codeforest.ui.view.tree;

import org.jtool.codeforest.ui.CodeForestFrame;
import org.jtool.codeforest.ui.view.CodeForestUniverse;
import org.jtool.codeforest.ui.view.forest.ForestNode;

import javax.media.j3d.Background;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3f;

/**
 * A 3D visual environment for a tree view.
 * @author Daiki Todoroki
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
        viewPos.setTranslation(new Vector3f(0.0f, -0.5f, 0.0f));
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
        // createAxes(trans);
        TransformGroup group = node.createSceneGraphFractal();
        if (group != null) {
            bg.addChild(group);
        }
        
        picker(bg);
        shapeBranchGroup.addChild(bg);
    }
    
    private void createBackGround(TransformGroup group) {
        BoundingSphere bounds = new BoundingSphere(new Point3d(), 100.0);
        Background background = new Background(new Color3f(0.9f, 0.9f, 0.9f));
        
        background.setApplicationBounds(bounds);
        group.addChild(background);
    }
    
    /*
    private void createAxes(TransformGroup tg) {
        Point3f[] vertex = new Point3f[6];
        vertex[0] = new Point3f(-1.0f, 0.0f, 0.0f);
        vertex[1] = new Point3f( 1.0f, 0.0f, 0.0f);
        vertex[2] = new Point3f( 0.0f,-1.0f, 0.0f);
        vertex[3] = new Point3f( 0.0f, 1.0f, 0.0f);
        vertex[4] = new Point3f( 0.0f, 0.0f,-1.0f);
        vertex[5] = new Point3f( 0.0f, 0.0f, 1.0f);
        
        Appearance ap = new Appearance();
        ColoringAttributes ca = new ColoringAttributes();
        ca.setColor(new Color3f(0.0f, 0.0f, 0.0f));
        ap.setColoringAttributes(ca);
        
        LineArray LineA = new LineArray(vertex.length, GeometryArray.COORDINATES);
        LineA.setCoordinates(0, vertex);
        Shape3D line3D = new Shape3D(LineA, ap);
        tg.addChild(line3D);
        
        Vector3f[] vector = new Vector3f[3];
        vector[0] = new Vector3f(  0.8f, 0.0f, 0.0f);
        vector[1] = new Vector3f( 0.05f, 0.8f, 0.0f);
        vector[2] = new Vector3f(-0.05f, 0.0f, 0.8f);
        
        String[] axis = {"x","y","z"};
        Transform3D trans = new Transform3D();
        for (int i = 0; i < 3; i++) {
            trans.setTranslation(vector[i]);
            TransformGroup transG = new TransformGroup(trans);
            Text2D text2d = new Text2D(axis[i], new Color3f(0.0f, 0.0f, 0.0f), "Serif", 30, Font.BOLD);
            transG.addChild(text2d);
            tg.addChild(transG);
        }
    }
    */
}
