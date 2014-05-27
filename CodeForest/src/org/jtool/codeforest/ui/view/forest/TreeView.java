/*
 *  Copyright 2014, Katsuhisa Maruyama (maru@jtool.org)
 */

package org.jtool.codeforest.ui.view.forest;

import org.jtool.codeforest.ui.CodeForestFrame;
import org.jtool.codeforest.ui.view.TreeSettingDialog;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.SWT;
import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;

/**
 * Displays a tree on the screen.
 * @author Katsuhisa Maruyama
 */
public class TreeView {
    
    /**
     * A node on the scene graph, which is a visual object.
     */
    private ForestNode shape;
    
    /**
     * A drawing canvas for this tree view.
     */
    private TreeCanvas3D canvas;
    
    /**
     * Font information.
     */
    private Font font11;
    
    /**
     * Creates a tree view.
     * @param parent the parent of the tree view
     * @param frame the main frame
     */
    public TreeView(Composite parent, CodeForestFrame frame) {
        createPane(parent, frame);
    }
    
    /**
     * Creates the pane of a tree view.
     * @param parent the parent of the tree view
     * @param frame the main frame
     */
    private void createPane(Composite parent, final CodeForestFrame frame) {
        final int MARGIN = 0;
        font11 = new Font(parent.getDisplay(), "", 11, SWT.NORMAL);
        
        parent.setLayout(new FormLayout());
        
        Button treeSettingButton = new Button(parent, SWT.PUSH);
        treeSettingButton.setFont(font11);
        treeSettingButton.setText("Tree Setting");
        treeSettingButton.addSelectionListener(new SelectionListener() {
            
            /**
             * Invoked when selection occurs in the control.
             * @param e an event containing information about the selection
             */
            public void widgetSelected(SelectionEvent e) {
                TreeSettingDialog dialog = new TreeSettingDialog(frame);
                dialog.create();
                dialog.open();
            }
            
            /**
             * Invoked when default selection occurs in the control.
             * @param e an event containing information about the default selection
             */
            public void widgetDefaultSelected(SelectionEvent e) {
            }
        });
        
        FormData btdata = new FormData();
        btdata.bottom = new FormAttachment(100, -MARGIN);
        btdata.left = new FormAttachment(0, MARGIN);
        btdata.right = new FormAttachment(100, -MARGIN);
        treeSettingButton.setLayoutData(btdata);
        
        Composite base = new Composite(parent, SWT.BORDER | SWT.EMBEDDED);
        base.setLayout(new FillLayout());
        
        FormData trdata = new FormData();
        trdata.top = new FormAttachment(0, MARGIN);
        trdata.bottom = new FormAttachment(treeSettingButton, -MARGIN);
        trdata.left = new FormAttachment(0, MARGIN);
        trdata.right = new FormAttachment(100, -MARGIN);
        base.setLayoutData(trdata);
        
        // SWT_AWT.embeddedFrameClass = "sun.lwawt.macosx.CViewEmbeddedFrame";
        Frame awtFrame = SWT_AWT.new_Frame(base);
        Rectangle bounds = base.getBounds();
        awtFrame.setBounds(0, 0, bounds.width, bounds.height);
        awtFrame.setLayout(new BorderLayout());
        
        GraphicsConfiguration configuration = CodeForestUniverse.getGraphicsConfiguration(awtFrame);
        canvas = new TreeCanvas3D(configuration, frame); 
        
        awtFrame.add(canvas, BorderLayout.CENTER);
        awtFrame.setVisible(true);
        
        parent.pack();
        
        repaint();
    }
    
    /**
     * Sets a given node in the scene graph.
     * @param shape the node added to the scene graph.
     */
    public void setSceneGraph(ForestNode shape) {
        this.shape = shape;
        canvas.setSceneGraph(shape);
    }
    
    /**
     * Repaints this tree view.
     */
    public void repaint() {
        canvas.repaint();
    }
    
    /**
     * Updates this tree view.
     */
    public void update() {
        if (shape != null) {
            canvas.setSceneGraph(shape);
        }
    }
    
    /**
     * Disposes this tree view.
     */
    public void dispose() {
        canvas.dispose();
        canvas = null;
    }
}
