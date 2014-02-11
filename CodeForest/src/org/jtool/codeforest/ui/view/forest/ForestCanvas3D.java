/*
 *  Copyright 2014, Katsuhisa Maruyama (maru@jtool.org)
 */

package org.jtool.codeforest.ui.view.forest;

import org.jtool.codeforest.ui.CodeForestFrame;

import java.awt.GraphicsConfiguration;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.media.j3d.Canvas3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.Transform3D;
import javax.media.j3d.View;
import javax.vecmath.Vector3f;

/**
 * Represents a canvas for displaying a forest on a screen.
 * @author Daiki Todoroki
 * @author Katsuhisa Maruyama
 */
public class ForestCanvas3D extends Canvas3D implements MouseListener, MouseMotionListener {
    
    private static final long serialVersionUID = -5518735816058831747L;
    
    private ForestUniverse universe;
    
    public ForestCanvas3D(GraphicsConfiguration configuration, CodeForestFrame frame) {
        super(configuration);
        universe = new ForestUniverse(this, frame);
        universe.getViewer().getView().setBackClipPolicy(View.VIRTUAL_EYE);
        universe.getViewer().getView().setBackClipDistance(1000.0);
        
        addMouseListener(this);
        addMouseMotionListener(this);
    }
    
    public void setForest(Forest world) {
        universe.setForest(world);
    }
    
    public void repaint() {
        super.repaint();
        universe.repaint();
    }
    
    public void dispose() {
        universe.cleanup();
    }
    
    public void mouseClicked(MouseEvent evt) {
        if (evt.isShiftDown()) {
            TransformGroup camera = universe.getViewingPlatform().getViewPlatformTransform();
            
            Transform3D view_pos = new Transform3D();
            view_pos.setTranslation(new Vector3f(0.0f, 0.0f, 0.0f));
            camera.setTransform(view_pos);
        }
    }
    
    public void mousePressed(MouseEvent e) {
    }
    
    public void mouseReleased(MouseEvent e) {
    }
    
    public void mouseEntered(MouseEvent e) {
    }
    
    public void mouseExited(MouseEvent e) {
    }
    
    public void mouseDragged(MouseEvent e) {
    }
    
    public void mouseMoved(MouseEvent e) {
    }
}
