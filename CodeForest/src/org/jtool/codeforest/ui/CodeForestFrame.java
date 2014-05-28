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
 * Provides a main frame that lay outs forest and tree views.
 * @author Katsuhisa Maruyama
 */
public class CodeForestFrame {
    
    /**
     * The shell of this frame.
     */
    private Shell baseShell;
    
    /**
     * A tab frame that displays tree and memo views.
     */
    private CodeForestTopTabFrame topTabFrame;
    
    /**
     * A tab frame that displays property and interaction views.
     */
    private CodeForestBottomTabFrame bottomTabFrame;
    
    /**
     * The metrics of a project represented by a forest.
     */
    private ProjectMetrics projectMetrics;
    
    /**
     * A forest view.
     */
    private ForestView forestView;
    
    /**
     * A setting view.
     */
    private SettingView settingView;
    
    /**
     * A source code view.
     */
    private SourceCodeView sourceCodeView;
    
    /**
     * The repository that stores information on a forest.
     */
    private CodeForestRepository codeForestRepository;
    
    /**
     * Creates a main frame.
     * @param parent the parent of the frame
     * @param mproject the metrics of a project represented by a forest
     */
    public CodeForestFrame(Shell parent, ProjectMetrics mproject) {
        baseShell = new Shell(parent.getDisplay());
        baseShell.setLayout(new FormLayout());
        baseShell.setSize(1500, 1000);
        
        baseShell.setText(mproject.getName() + " - " + Time.toString(mproject.getTime()));
        
        projectMetrics = mproject;
        
        createPane();
    }
    
    /**
     * Creates a main frame.
     * @param parent the parent of the frame
     */
    public CodeForestFrame(Shell parent) {
        this(parent, null);
    }
    
    /**
     * Returns the shell of this frame.
     * @return the shell of the frame
     */
    public Shell getShell() {
        return baseShell;
    }
    
    /**
     * Obtains the metrics of a project represented by a forest.
     * @return the project metrics
     */
    public ProjectMetrics getProjectMetrics() {
        return projectMetrics;
    }
    
    /**
     * Obtains the setting data that forms a forest.
     * @return the setting data
     */
    public SettingData getSettingData() {
        return settingView.getSettingData();
    }
    
    /**
     * Obtains the forest view in this frame.
     * @return the forest view
     */
    public ForestView getForestView() {
        return forestView;
    }
    
    /**
     * Obtains the tree view in this frame.
     * @return the tree view
     */
    public TreeView getTreeView() {
        return topTabFrame.getTreeView();
    }
    
    /**
     * Obtains the memo view in this frame.
     * @return the memo view
     */
    public MemoView getMemoView() {
        return topTabFrame.getMemoView();
    }
    
    /**
     * Obtains the setting view in this frame.
     * @return the setting view
     */
    public SettingView getSettingView() {
        return settingView;
    }
    
    /**
     * Obtains the source code view in this frame.
     * @return the source code view
     */
    public SourceCodeView getSourceCodeView() {
        return sourceCodeView;
    }
    
    /**
     * Obtains the property view in this frame.
     * @return the property view
     */
    public PropertyView getPropertyView() {
        return bottomTabFrame.getPropertyView();
    }
    
    /**
     * Obtains the interaction view in this frame.
     * @return the interaction view
     */
    public InteractionView getInteractionView() {
        return bottomTabFrame.getInteractionView();
    }
    
    /**
     * Focuses on the tree view.
     */
    public void focusTreeView() {
        topTabFrame.focusTreeView();
    }
    
    /**
     * Focuses on the memo view.
     */
    public void focusMemoView() {
        topTabFrame.focusMemoView();
    }
    
    /**
     * Focuses on the property view.
     */
    public void focusPropertyView() {
        bottomTabFrame.focusPropertyView();
    }
    
    /**
     * Focuses on the interaction view.
     */
    public void focusInteractionView() {
        bottomTabFrame.focusInteractionView();
    }
    
    /**
     * Creates the pane of this frame.
     */
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
    
    /**
     * Creates the views on this frame.
     */
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
    
    /**
     * Waits until the disposal of this frame and then terminates it.
     */
    private void waitUntilDispose() {
        Display display = baseShell.getDisplay();
        while (!baseShell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
    }
    
    /**
     * Disposes this frame.
     */
    public void dispose() {
        baseShell.dispose();
        baseShell = null;
    }
}
