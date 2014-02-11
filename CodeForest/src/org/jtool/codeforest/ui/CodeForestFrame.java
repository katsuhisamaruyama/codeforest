/*
 *  Copyright 2014, Katsuhisa Maruyama (maru@jtool.org)
 */

package org.jtool.codeforest.ui;

import org.jtool.codeforest.metrics.java.ProjectMetrics;
import org.jtool.codeforest.ui.view.PropertyView;
import org.jtool.codeforest.ui.view.SettingData;
import org.jtool.codeforest.ui.view.SettingView;
import org.jtool.codeforest.ui.view.SourceCodeView;
import org.jtool.codeforest.ui.view.forest.Forest3DView;
import org.jtool.codeforest.ui.view.forest.ForestData;
import org.jtool.codeforest.ui.view.tree.TreeView;
import org.jtool.codeforest.ui.view.history.HistoryView;
import org.jtool.codeforest.ui.view.memo.MemoView;
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
    
    private CodeForestTabFrame tabFrame;
    
    private ProjectMetrics projectMetrics;
    
    private ForestData forestData;
    
    private Forest3DView forestView;
    
    private SettingView settingView;
    
    private PropertyView propertyView;
    
    private SourceCodeView sourceCodeView;
    
    private SettingData settingData;
    
    public CodeForestFrame(Shell parent, ProjectMetrics mproject) {
        baseShell = new Shell(parent.getDisplay());
        baseShell.setLayout(new FormLayout());
        baseShell.setSize(1500, 1000);
        
        projectMetrics = mproject;
        settingData = new SettingData();
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
    
    public SettingData getSettingData() {
        return settingData;
    }
    
    public ForestData getForestData() {
        return forestData;
    }
    
    public Forest3DView getForestView() {
        return forestView;
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
    
    public TreeView getTreeView() {
        return tabFrame.getTreeView();
    }
    
    public HistoryView getHistoryView() {
        return tabFrame.getHistoryView();
    }
    
    public MemoView getMemoView() {
        return tabFrame.getMemoView();
    }
    
    public void createPane() {
        final int MARGIN = 3;
        final int TABFRAME_WIDTH = 330;
        final int TABFRAME_HEIGHT = 430;
        final int SETTINGVIEW_HEIGHT = 200;
        final int SOURCEVIEW_HEIGHT = 200;
        
        tabFrame = new CodeForestTabFrame(baseShell, this);
        FormData tdata = new FormData();
        tdata.right = new FormAttachment(100, -MARGIN);
        tdata.top = new FormAttachment(0, MARGIN);
        tdata.width = TABFRAME_WIDTH;
        tdata.height = TABFRAME_HEIGHT;
        tabFrame.getTabFolder().setLayoutData(tdata);
        
        Composite settingPanel = new Composite(baseShell, SWT.NONE);
        FormData cdata = new FormData();
        cdata.right = new FormAttachment(100, -MARGIN);
        cdata.bottom = new FormAttachment(100, -MARGIN);
        cdata.width = TABFRAME_WIDTH;
        cdata.height = SETTINGVIEW_HEIGHT;
        settingPanel.setLayoutData(cdata);
        
        settingView = new SettingView(settingPanel, this);
        
        Composite propertyPanel = new Composite(baseShell, SWT.NONE);
        FormData pdata = new FormData();
        pdata.top = new FormAttachment(tabFrame.getTabFolder(), MARGIN, SWT.VERTICAL);
        pdata.right = new FormAttachment(100, -MARGIN);
        pdata.width = TABFRAME_WIDTH;
        pdata.bottom = new FormAttachment(settingPanel, -MARGIN);
        propertyPanel.setLayoutData(pdata);
        
        propertyView = new PropertyView(propertyPanel, projectMetrics);
        
        Composite sourceCodePanel = new Composite(baseShell, SWT.BORDER);
        FormData sdata = new FormData();
        sdata.left = new FormAttachment(0, MARGIN);
        sdata.right = new FormAttachment(tabFrame.getTabFolder(), -MARGIN, SWT.HORIZONTAL);
        sdata.bottom = new FormAttachment(100, -MARGIN);
        sdata.height = SOURCEVIEW_HEIGHT;
        sourceCodePanel.setLayoutData(sdata);
        
        sourceCodeView = new SourceCodeView(sourceCodePanel);
        
        Composite forestPanel = new Composite(baseShell, SWT.BORDER | SWT.EMBEDDED);
        FormData fdata = new FormData();
        fdata.top = new FormAttachment(0, MARGIN);
        fdata.bottom = new FormAttachment(sourceCodePanel, -MARGIN);
        fdata.left = new FormAttachment(0, MARGIN);
        fdata.right = new FormAttachment(tabFrame.getTabFolder(), -MARGIN, SWT.HORIZONTAL);
        
        forestPanel.setLayoutData(fdata);
        
        forestView = new Forest3DView(forestPanel, this);
        
        forestData = new ForestData(settingData);
        forestView.build(projectMetrics, forestData);
        
        baseShell.open();
        
        Display display = baseShell.getDisplay();
        while (!baseShell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
        
        forestPanel.dispose();
        settingPanel.dispose();
        propertyPanel.dispose();
        sourceCodePanel.dispose();
        
        forestView.dispose();
        settingView.dispose();
        propertyView.dispose();
        sourceCodeView.dispose();
        
        tabFrame.getTabFolder().dispose();
        
        projectMetrics = null;
        forestData = null;
    }
    
    public void dispose() {
        baseShell = null;
    }
}
