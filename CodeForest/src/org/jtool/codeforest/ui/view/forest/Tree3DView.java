/*
 *  Copyright 2013, Katsuhisa Maruyama (maru@jtool.org)
 */

package org.jtool.codeforest.ui.view.forest;

import org.jtool.codeforest.ui.CodeForestFrame;
import org.jtool.codeforest.ui.view.CodeForestUniverse;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;

import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;

/**
 * Displays a tree on the screen.
 * @author Daiki Todoroki
 * @author Katsuhisa Maruyama
 */
public class Tree3DView {
    
    private TreeCanvas3D canvas;
    
    public Tree3DView() {
    }
    
    public void createPane(Composite parent, CodeForestFrame frame) {
        parent.setLayout(new FillLayout());
        
        // SWT_AWT.embeddedFrameClass = "sun.lwawt.macosx.CViewEmbeddedFrame";
        Frame awtFrame = SWT_AWT.new_Frame(parent);
        Rectangle bounds = parent.getBounds();
        awtFrame.setBounds(0, 0, bounds.width, bounds.height);
        awtFrame.setLayout(new BorderLayout());
        
        GraphicsConfiguration configuration = CodeForestUniverse.getGraphicsConfiguration(awtFrame);
        canvas = new TreeCanvas3D(configuration, frame); 
        
        awtFrame.add(canvas, BorderLayout.CENTER);
        awtFrame.setVisible(true);
    }
    
    public void setSceneGraph(ForestNode shape) {
        canvas.setSceneGraph(shape);
    }
    
    public void repaint() {
        canvas.repaint();
    }
    
    public void dispose() {
        canvas.dispose();
        canvas = null;
    }
}
