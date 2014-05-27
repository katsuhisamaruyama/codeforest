/*
 *  Copyright 2014, Katsuhisa Maruyama (maru@jtool.org)
 */

package org.jtool.codeforest.ui.view.forest;

import org.jtool.codeforest.metrics.java.ProjectMetrics;
import org.jtool.codeforest.ui.CodeForestFrame;
import org.jtool.codeforest.ui.view.SettingData;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;

/**
 * Displays a forest on the screen.
 * @author Katsuhisa Maruyama
 */
public class ForestView {
    
    /**
     * A drawing canvas for this forest view.
     */
    private ForestCanvas3D canvas;
    
    /**
     * A builder that builds a forest.
     */
    private ForestBuilder builder;
    
    /**
     * Creates a forest view.
     * @param parent the parent of the forest view
     * @param frame the main frame
     */
    public ForestView(Composite parent, CodeForestFrame frame) {
        createPane(parent, frame);
    }
    
    /**
     * Creates the pane of a forest view.
     * @param parent the parent of the forest view
     * @param frame the main frame
     */
    private void createPane(Composite parent, CodeForestFrame frame) {
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
    
    /**
     * Builds a forest.
     * @param projectMetrics the metrics of the project related to the forest
     * @param data the setting data that forms the forest
     */
    public void build(ProjectMetrics projectMetrics, SettingData data) {
        builder = new ForestBuilder(projectMetrics);
        canvas.setForest(builder.build(data));
    }
    
    /**
     * Repaints this forest view.
     */
    public void repaint() {
        canvas.repaint();
    }
    
    /**
     * Updates this forest view.
     * @param data the setting data that forms a forest
     */
    public void update(SettingData data) {
        canvas.setForest(builder.build(data));
    }
    
    /**
     * Disposes this forest view.
     */
    public void dispose() {
        canvas.dispose();
        canvas = null;
        builder = null;
    }
}
