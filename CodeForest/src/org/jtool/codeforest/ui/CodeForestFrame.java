/*
 *  Copyright 2014, Katsuhisa Maruyama (maru@jtool.org)
 */

package org.jtool.codeforest.ui;

import org.jtool.codeforest.metrics.java.ProjectMetrics;
import org.jtool.codeforest.ui.view.PropertyView;
import org.jtool.codeforest.ui.view.SettingData;
import org.jtool.codeforest.ui.view.SettingView;
import org.jtool.codeforest.ui.view.SourceCodeView;
import org.jtool.codeforest.ui.view.control.InteractionView;
import org.jtool.codeforest.ui.view.control.MemoView;
import org.jtool.codeforest.ui.view.forest.ForestView;
import org.jtool.codeforest.ui.view.forest.TreeView;
import org.jtool.codeforest.util.Time;
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
    
    private CodeForestTopTabFrame topTabFrame;
    
    private CodeForestBottomTabFrame bottomTabFrame;
    
    private ProjectMetrics projectMetrics;
    
    private ForestView forestView;
    
    private SettingView settingView;
    
    private SourceCodeView sourceCodeView;
    
    private CodeForestRepository codeForestRepository;
    
    public CodeForestFrame(Shell parent, ProjectMetrics mproject) {
        baseShell = new Shell(parent.getDisplay());
        baseShell.setLayout(new FormLayout());
        baseShell.setSize(1500, 1000);
        
        baseShell.setText(mproject.getName() + " - " + Time.toString(mproject.getTime()));
        
        projectMetrics = mproject;
        
        createPane();
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
        return settingView.getSettingData();
    }
    
    public ForestView getForestView() {
        return forestView;
    }
    
    public TreeView getTreeView() {
        return topTabFrame.getTreeView();
    }
    
    public MemoView getMemoView() {
        return topTabFrame.getMemoView();
    }
    
    public SettingView getSettingView() {
        return settingView;
    }
    
    public SourceCodeView getSourceCodeView() {
        return sourceCodeView;
    }
    
    public PropertyView getPropertyView() {
        return bottomTabFrame.getPropertyView();
    }
    
    public InteractionView getInteractionView() {
        return bottomTabFrame.getInteractionView();
    }
    
    public void focusTreeView() {
        topTabFrame.focusTreeView();
    }
    
    public void focusMemoView() {
        topTabFrame.focusMemoView();
    }
    
    public void focusPropertyView() {
        bottomTabFrame.focusPropertyView();
    }
    
    public void focusInteractionView() {
        bottomTabFrame.focusInteractionView();
    }
    
    private void createPane() {
        createViews();
        
        codeForestRepository = new CodeForestRepository(this);
        codeForestRepository.readXML();
        
        getSettingView().recordInitialization();
        getInteractionView().refreshInteractionList();
        getMemoView().refreshMemoList();
        
        baseShell.open();
        
        waitUntilDispose();
        
        codeForestRepository.writeXML();
        
        forestView.dispose();
        settingView.dispose();
        sourceCodeView.dispose();
        
        topTabFrame.dispose();
        bottomTabFrame.dispose();
    }
    
    private void createViews() {
        final int MARGIN = 2;
        final int TREE_VIEW_WIDTH = 330;
        final int TREE_VIEW_HEIGHT = 330;
        final int SETTING_VIEW_HEIGHT = 170;
        final int SOURCE_VIEW_HEIGHT = 200;
        
        topTabFrame = new CodeForestTopTabFrame(this);
        FormData ttbdata = new FormData();
        ttbdata.top = new FormAttachment(0, MARGIN);
        ttbdata.right = new FormAttachment(100, -MARGIN);
        ttbdata.width = TREE_VIEW_WIDTH;
        ttbdata.height = TREE_VIEW_HEIGHT;
        topTabFrame.getTabFolder().setLayoutData(ttbdata);
        
        Composite settingPanel = new Composite(baseShell, SWT.NONE);
        FormData stdata = new FormData();
        stdata.top = new FormAttachment(topTabFrame.getTabFolder(), MARGIN, SWT.VERTICAL);
        stdata.right = new FormAttachment(100, -MARGIN);
        stdata.width = TREE_VIEW_WIDTH;
        stdata.height = SETTING_VIEW_HEIGHT;
        settingPanel.setLayoutData(stdata);
        
        settingView = new SettingView(settingPanel, this);
        
        bottomTabFrame = new CodeForestBottomTabFrame(this);
        FormData btbdata = new FormData();
        btbdata.top = new FormAttachment(settingPanel, MARGIN, SWT.VERTICAL);
        btbdata.right = new FormAttachment(100, -MARGIN);
        btbdata.width = TREE_VIEW_WIDTH;
        btbdata.bottom = new FormAttachment(100, -MARGIN);
        bottomTabFrame.getTabFolder().setLayoutData(btbdata);
        
        Composite sourceCodePanel = new Composite(baseShell, SWT.BORDER);
        FormData sdata = new FormData();
        sdata.left = new FormAttachment(0, MARGIN);
        sdata.right = new FormAttachment(topTabFrame.getTabFolder(), -MARGIN, SWT.HORIZONTAL);
        sdata.bottom = new FormAttachment(100, -MARGIN);
        sdata.height = SOURCE_VIEW_HEIGHT;
        sourceCodePanel.setLayoutData(sdata);
        
        sourceCodeView = new SourceCodeView(sourceCodePanel);
        
        Composite forestPanel = new Composite(baseShell, SWT.BORDER | SWT.EMBEDDED);
        FormData fdata = new FormData();
        fdata.top = new FormAttachment(0, MARGIN);
        fdata.bottom = new FormAttachment(sourceCodePanel, -MARGIN);
        fdata.left = new FormAttachment(0, MARGIN);
        fdata.right = new FormAttachment(topTabFrame.getTabFolder(), -MARGIN, SWT.HORIZONTAL);
        
        forestPanel.setLayoutData(fdata);
        
        forestView = new ForestView(forestPanel, this);
        
        forestView.build(projectMetrics, settingView.getSettingData());
        getTreeView().setSceneGraph(null);
    }
    
    private void waitUntilDispose() {
        Display display = baseShell.getDisplay();
        while (!baseShell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
    }
    
    public void dispose() {
        baseShell.dispose();
        baseShell = null;
    }
}
