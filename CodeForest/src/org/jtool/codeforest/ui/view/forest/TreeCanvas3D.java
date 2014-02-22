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
import javax.media.j3d.View;

/**
 * Represents a canvas for displaying a tree on a screen.
 * @author Katsuhisa Maruyama
 */
public class TreeCanvas3D extends Canvas3D implements MouseListener, MouseMotionListener{
    
    private static final long serialVersionUID = 5451264060683533133L;
    
    private final TreeUniverse universe;
    
    public TreeCanvas3D(GraphicsConfiguration configuration, CodeForestFrame frame) {
        super(configuration);
        universe = new TreeUniverse(this, frame);
        universe.getViewer().getView().setBackClipPolicy(View.VIRTUAL_EYE);
        universe.getViewer().getView().setBackClipDistance(1000.0);
        
        addMouseListener(this);
        addMouseMotionListener(this);
    }
    
    public void repaint() {
        super.repaint();
    }
    
    public void dispose() {
        universe.cleanup();
    }
    
    public void setSceneGraph(ForestNode shape) {
        universe.setSceneGraph(shape);
    }
    
    public void mouseClicked(MouseEvent e) {
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
