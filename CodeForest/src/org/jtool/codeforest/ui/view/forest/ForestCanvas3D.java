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
 * @author Katsuhisa Maruyama
 */
public class ForestCanvas3D extends Canvas3D implements MouseListener, MouseMotionListener {
    
    private static final long serialVersionUID = -5518735816058831747L;
    
    /**
     * A virtual environment for Java3D.
     */
    private ForestUniverse universe;
    
    /**
     * Creates a drawing canvas that displays a forest.
     * @param configuration the graphics configuration
     * @param frame the main frame
     */
    public ForestCanvas3D(GraphicsConfiguration configuration, CodeForestFrame frame) {
        super(configuration);
        universe = new ForestUniverse(this, frame);
        universe.getViewer().getView().setBackClipPolicy(View.VIRTUAL_EYE);
        universe.getViewer().getView().setBackClipDistance(1000.0);
        
        addMouseListener(this);
        addMouseMotionListener(this);
    }
    
    /**
     * Sets a forest on this drawing canvas.
     * @param forest a forest to be drawn
     */
    public void setForest(Forest forest) {
        universe.setForest(forest);
    }
    
    /**
     * Repaints this drawing canvas.
     */
    public void repaint() {
        super.repaint();
        universe.repaint();
    }
    
    /**
     * Disposes this drawing canvas.
     */
    public void dispose() {
        universe.cleanup();
    }
    
    /**
     * Invoked when the mouse button has been clicked (pressed and released) on this tree canvas.
     * @param e an event which indicates that a mouse action
     */
    public void mouseClicked(MouseEvent evt) {
        if (evt.isShiftDown()) {
            TransformGroup camera = universe.getViewingPlatform().getViewPlatformTransform();
            
            Transform3D view_pos = new Transform3D();
            view_pos.setTranslation(new Vector3f(0.0f, 0.0f, 0.0f));
            camera.setTransform(view_pos);
        }
    }
    
    /**
     * Invoked when a mouse button has been pressed on this tree canvas.
     * @param e an event which indicates that a mouse action
     */
    public void mousePressed(MouseEvent e) {
    }
    
    /**
     * Invoked when a mouse button has been released on this tree canvas.
     * @param e an event which indicates that a mouse action
     */
    public void mouseReleased(MouseEvent e) {
    }
    
    /**
     * Invoked when the mouse enters this tree canvas.
     * @param e an event which indicates that a mouse action
     */
    public void mouseEntered(MouseEvent e) {
    }
    
    /**
     * Invoked when the mouse exits this tree canvas.
     * @param e an event which indicates that a mouse action
     */
    public void mouseExited(MouseEvent e) {
    }
    
    /**
     * Invoked when a mouse button is pressed on this tree canvas and then dragged.
     * @param e an event which indicates that a mouse action
     */
    public void mouseDragged(MouseEvent e) {
    }
    
    /**
     * Invoked when the mouse cursor has been moved onto this tree canvas but no buttons have been pushed.
     * @param e an event which indicates that a mouse action
     */
    public void mouseMoved(MouseEvent e) {
    }
}
