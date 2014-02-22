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
    
    private ForestNode shape;
    
    private TreeCanvas3D canvas;
    
    private Font font11;
    
    public TreeView(Composite parent, CodeForestFrame frame) {
        createPane(parent, frame);
    }
    
    private void createPane(Composite parent, final CodeForestFrame frame) {
        final int MARGIN = 0;
        font11 = new Font(parent.getDisplay(), "", 11, SWT.NORMAL);
        
        parent.setLayout(new FormLayout());
        
        Button treeSettingButton = new Button(parent, SWT.PUSH);
        treeSettingButton.setFont(font11);
        treeSettingButton.setText("Tree Setting");
        treeSettingButton.addSelectionListener(new SelectionListener() {
            
            public void widgetSelected(SelectionEvent e) {
                TreeSettingDialog dialog = new TreeSettingDialog(frame);
                dialog.create();
                dialog.open();
            }
            
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
    
    public void setSceneGraph(ForestNode shape) {
        this.shape = shape;
        canvas.setSceneGraph(shape);
    }
    
    public void repaint() {
        canvas.repaint();
    }
    
    public void update() {
        if (shape != null) {
            canvas.setSceneGraph(shape);
        }
    }
    
    public void dispose() {
        canvas.dispose();
        canvas = null;
    }
}
