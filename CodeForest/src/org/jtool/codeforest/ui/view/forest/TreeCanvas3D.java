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
 * @author Daiki Todoroki
 */
public class TreeCanvas3D extends Canvas3D implements MouseListener, MouseMotionListener{
    
    private static final long serialVersionUID = 5451264060683533133L;
    
    /**
     * A virtual environment for Java3D.
     */
    private final TreeUniverse universe;
    
    /**
     * Creates a drawing canvas that displays a tree.
     * @param configuration the graphics configuration
     * @param frame the main frame
     */
    public TreeCanvas3D(GraphicsConfiguration configuration, CodeForestFrame frame) {
        super(configuration);
        universe = new TreeUniverse(this, frame);
        universe.getViewer().getView().setBackClipPolicy(View.VIRTUAL_EYE);
        universe.getViewer().getView().setBackClipDistance(1000.0);
        
        addMouseListener(this);
        addMouseMotionListener(this);
    }
    
    /**
     * Repaints this drawing canvas.
     */
    public void repaint() {
        super.repaint();
    }
    
    /**
     * Disposes this drawing canvas.
     */
    public void dispose() {
        universe.cleanup();
    }
    
    /**
     * Sets a given node in the scene graph.
     * @param shape the node added to the scene graph.
     */
    public void setSceneGraph(ForestNode shape) {
        universe.setSceneGraph(shape);
    }
    
    /**
     * Invoked when the mouse button has been clicked (pressed and released) on this tree canvas.
     * @param e an event which indicates that a mouse action
     */
    public void mouseClicked(MouseEvent e) {
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
