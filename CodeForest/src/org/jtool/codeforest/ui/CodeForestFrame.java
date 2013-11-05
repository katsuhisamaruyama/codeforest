/*
 *  Copyright 2013, Katsuhisa Maruyama (maru@jtool.org)
 */

package org.jtool.codeforest.ui;

import org.jtool.codeforest.metrics.java.ProjectMetrics;
import org.jtool.codeforest.ui.view.PropertyView;
import org.jtool.codeforest.ui.view.SettingView;
import org.jtool.codeforest.ui.view.SourceCodeView;
import org.jtool.codeforest.ui.view.forest.Forest3DView;
import org.jtool.codeforest.ui.view.forest.ForestData;
import org.jtool.codeforest.ui.view.forest.Tree3DView;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;

/**
 * Provides a base frame that lays out forest and tree views.
 * @author Katsuhisa Maruyama
 */
public class CodeForestFrame {
    
    private Shell baseShell;
    
    private ProjectMetrics projectMetrics;
    
    private ForestData forestData;
    
    private Forest3DView forestView;
    
    private Tree3DView treeView;
    
    private SettingView settingView;
    
    private PropertyView propertyView;
    
    private SourceCodeView sourceCodeView;
    
    public CodeForestFrame(Shell parent, ProjectMetrics mproject) {
        baseShell = new Shell(parent.getDisplay());
        baseShell.setLayout(new FormLayout());
        baseShell.setSize(1500, 1000);
        
        projectMetrics = mproject;
    }
    
    public CodeForestFrame(Shell parent) {
        this(parent, null);
    }
    
    public Shell getShell() {
        return baseShell;
    }
    
    public ProjectMetrics getProjectMetrics() {
        return projectMetrics;
    }
    
    public ForestData getForestData() {
        return forestData;
    }
    
    public Forest3DView getForestView() {
        return forestView;
    }
    
    public Tree3DView getTreeView() {
        return treeView;
    }
    
    public SettingView getSettingView() {
        return settingView;
    }
    
    public PropertyView getPropertyView() {
        return propertyView;
    }
    
    public SourceCodeView getSourceCodeView() {
        return sourceCodeView;
    }
    
    public void createPane() {
        final int MARGIN = 3;
        final int TREEVIEW_WIDTH = 330;
        final int TREEVIEW_HEIGHT = 400;
        final int SETTINGVIEW_HEIGHT = 290;
        final int SOURCEVIEW_HEIGHT = 200;
        
        Composite treePanel = new Composite(baseShell, SWT.BORDER | SWT.EMBEDDED);
        FormData tdata = new FormData();
        tdata.left = new FormAttachment(0, MARGIN);
        tdata.top = new FormAttachment(0, MARGIN);
        tdata.width = TREEVIEW_WIDTH;
        tdata.height = TREEVIEW_HEIGHT;
        treePanel.setLayoutData(tdata);
        
        treeView = new Tree3DView();
        treeView.createPane(treePanel, this);
        
        Composite settingPanel = new Composite(baseShell, SWT.NONE);
        FormData cdata = new FormData();
        cdata.left = new FormAttachment(0, MARGIN);
        cdata.bottom = new FormAttachment(100, -MARGIN);
        cdata.width = TREEVIEW_WIDTH;
        cdata.height = SETTINGVIEW_HEIGHT;
        settingPanel.setLayoutData(cdata);
        
        settingView = new SettingView(settingPanel, this);
        
        Composite propertyPanel = new Composite(baseShell, SWT.NONE);
        FormData pdata = new FormData();
        pdata.top = new FormAttachment(treePanel, MARGIN, SWT.VERTICAL);
        pdata.left = new FormAttachment(0, MARGIN);
        pdata.width = TREEVIEW_WIDTH;
        pdata.bottom = new FormAttachment(settingPanel, -MARGIN);
        propertyPanel.setLayoutData(pdata);
        
        propertyView = new PropertyView(propertyPanel, projectMetrics);
        
        Composite sourceCodePanel = new Composite(baseShell, SWT.BORDER);
        FormData sdata = new FormData();
        sdata.left = new FormAttachment(treePanel, MARGIN, SWT.HORIZONTAL);
        sdata.right = new FormAttachment(100, -MARGIN);
        sdata.bottom = new FormAttachment(100, -MARGIN);
        sdata.height = SOURCEVIEW_HEIGHT;
        sourceCodePanel.setLayoutData(sdata);
        
        sourceCodeView = new SourceCodeView(sourceCodePanel);
        
        Composite forestPanel = new Composite(baseShell, SWT.BORDER | SWT.EMBEDDED);
        FormData fdata = new FormData();
        fdata.top = new FormAttachment(0, MARGIN);
        fdata.left = new FormAttachment(treePanel, MARGIN, SWT.HORIZONTAL);
        fdata.right = new FormAttachment(100, -MARGIN);
        fdata.bottom = new FormAttachment(sourceCodePanel, -MARGIN);
        forestPanel.setLayoutData(fdata);
        
        forestView = new Forest3DView();
        forestView.createPane(forestPanel, this);
        
        forestData = new ForestData(settingView.getSettingData());
        forestView.build(projectMetrics, forestData);
        
        baseShell.open();
        
        Display display = baseShell.getDisplay();
        while (!baseShell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
        
        forestPanel.dispose();
        treePanel.dispose();
        settingPanel.dispose();
        propertyPanel.dispose();
        sourceCodePanel.dispose();
        
        forestView.dispose();
        treeView.dispose();
        settingView.dispose();
        propertyView.dispose();
        sourceCodeView.dispose();
        
        projectMetrics = null;
        forestData = null;
    }
    
    public void dispose() {
        baseShell.dispose();
        baseShell = null;
    }
    
    public static void main(String[] args) {
        Display display = new Display();
        Shell shell = new Shell(display);
        
        CodeForestFrame frame = new CodeForestFrame(shell);
        frame.createPane();
        
        System.out.println("Code Forest fin.");
    }
}
