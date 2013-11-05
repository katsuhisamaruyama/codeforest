/*
 *  Copyright 2013, Katsuhisa Maruyama (maru@jtool.org)
 */

package org.jtool.codeforest.ui.view.forest;

import org.jtool.codeforest.metrics.java.ProjectMetrics;
import org.jtool.codeforest.ui.CodeForestFrame;
import org.jtool.codeforest.ui.view.CodeForestUniverse;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;

/**
 * Displays a forest on the screen.
 * @author Daiki Todoroki
 * @author Katsuhisa Maruyama
 */
public class Forest3DView {
    
    private ForestCanvas3D canvas;
    
    private ForestBuilder builder;
    
    public Forest3DView() {
    }
    
    public void createPane(Composite parent, CodeForestFrame frame) {
        parent.setLayout(new FillLayout());
        
        // SWT_AWT.embeddedFrameClass = "sun.lwawt.macosx.CViewEmbeddedFrame";
        Frame awtFrame = SWT_AWT.new_Frame(parent);
        Rectangle bounds = parent.getBounds();
        awtFrame.setBounds(0, 0, bounds.width, bounds.height);
        awtFrame.setLayout(new BorderLayout());
        
        GraphicsConfiguration configuration = CodeForestUniverse.getGraphicsConfiguration(awtFrame);
        canvas = new ForestCanvas3D(configuration, frame);
        
        awtFrame.add(canvas, BorderLayout.CENTER);
        awtFrame.setVisible(true);
    }
    
    public void build(ProjectMetrics projectMetrics, ForestData fdata) {
        builder = new ForestBuilder();
        builder.setProjectMetrics(projectMetrics);
        
        builder.setLayout(fdata.getLayoutName());
        canvas.setForest(builder.build(fdata));
    }
    
    public void update(ForestData fdata) {
        builder.setLayout(fdata.getLayoutName());
        canvas.setForest(builder.build(fdata));
    }
    
    public void repaint() {
        canvas.repaint();
    }
    
    public void dispose() {
        canvas.dispose();
        canvas = null;
        builder = null;
    }
}
