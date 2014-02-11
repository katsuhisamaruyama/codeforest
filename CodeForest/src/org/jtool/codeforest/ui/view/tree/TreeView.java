/*
 *  Copyright 2014, Katsuhisa Maruyama (maru@jtool.org)
 */

package org.jtool.codeforest.ui.view.tree;

import org.jtool.codeforest.ui.CodeForestFrame;
import org.jtool.codeforest.ui.view.SettingData;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;


/**
 * Provides a setting view for selecting metrics and a layout.
 * @author Katsuhisa Maruyama
 */
public class TreeView {
    
    private Font font11;
    
    private Tree3DView treeView;
    
    private SettingData settingData;
    
    private Combo blengthSel, lnumberSel, lsizeSel, lcolorSel;
    
    public TreeView(Composite parent, CodeForestFrame frame) {
        settingData = frame.getSettingData();
        createPane(parent, frame);
    }
    
    private void createPane(Composite parent, CodeForestFrame frame) {
        font11 = new Font(parent.getDisplay(), "", 11, SWT.NORMAL);
        
        parent.setLayout(new FormLayout());
        
        Control settings = createSettings(parent, 2);
        createTree3DView(parent, frame, settings, 2);
    }
    
    private void createTree3DView(Composite parent, CodeForestFrame frame, Control bottom, final int MARGIN) {
        Composite treePanel = new Composite(parent, SWT.BORDER | SWT.EMBEDDED);
        FormData tvdata = new FormData();
        tvdata.top = new FormAttachment(0, MARGIN);
        tvdata.bottom = new FormAttachment(bottom, -MARGIN);
        tvdata.left = new FormAttachment(0, MARGIN);
        tvdata.right = new FormAttachment(100, -MARGIN);
        treePanel.setLayoutData(tvdata);
        
        treeView = new Tree3DView(treePanel, frame);
    }
    
    private Control createSettings(Composite parent, final int MARGIN) {
        Composite settingPanel = new Composite(parent, SWT.NONE);
        FormData btdata = new FormData();
        btdata.bottom = new FormAttachment(100, -MARGIN);
        btdata.left = new FormAttachment(0, MARGIN);
        btdata.right = new FormAttachment(100, -MARGIN);
        settingPanel.setLayoutData(btdata);
        
        GridLayout glayout = new GridLayout(1, true);
        glayout.verticalSpacing = 0;
        glayout.marginHeight = 0;
        settingPanel.setLayout(glayout);
        
        Group settings = new Group(settingPanel, SWT.NONE);
        GridLayout flayout = new GridLayout(2, true);
        flayout.horizontalSpacing = 0;
        flayout.verticalSpacing = 0;
        glayout.marginHeight = 0;
        settings.setLayout(flayout);
        settings.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
        blengthSel = createMetricSelection(settings, SettingData.BRANCH_LENGTH, settingData.getHeightItems(false));
        blengthSel.select(0);
        settingData.setBranchLength(blengthSel.getItem(0));
        blengthSel.addSelectionListener(new BranchLengthSelectionListener());
        
        lnumberSel = createMetricSelection(settings, SettingData.LEAF_NUMBER, settingData.getMethodItems(true));
        lnumberSel.select(0);
        settingData.setLeafNumber(lnumberSel.getItem(0));
        lnumberSel.addSelectionListener(new LeafNumberSelectionListener());
        
        lsizeSel = createMetricSelection(settings, SettingData.LEAF_SIZE, settingData.getMethodItems(true));
        lsizeSel.select(0);
        settingData.setLeafSize(lsizeSel.getItem(0));
        lsizeSel.addSelectionListener(new LeafSizeSelectionListener());
        
        lcolorSel = createMetricSelection(settings, SettingData.LEAF_COLOR, settingData.getMethodItems(true));
        lcolorSel.select(0);
        settingData.setLeafColor(lcolorSel.getItem(0));
        lcolorSel.addSelectionListener(new LeafColorSelectionListener());
        
        return settingPanel;
    }
    
    public void dispose() {
        font11.dispose();
        
        settingData = null;
    }
    
    private Combo createMetricSelection(Composite parent, String title, String[] items) {
        Composite panel = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout(1, true);
        layout.horizontalSpacing = 0;
        layout.verticalSpacing = 0;
        layout.marginHeight = 2;
        panel.setLayout(layout);
        panel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
        Label label = new Label(panel, SWT.NONE);
        label.setFont(font11);
        label.setText(title);
        GridData ldata = new GridData(GridData.FILL_HORIZONTAL);
        label.setLayoutData(ldata);
        
        Combo combo = new Combo(panel, SWT.READ_ONLY);
        combo.setFont(font11);
        combo.setItems(items);
        combo.select(0);
        GridData cdata = new GridData(GridData.FILL_HORIZONTAL);
        combo.setLayoutData(cdata);
        
        return combo;
    }
    
    public Tree3DView getTree3DView() {
        return treeView;
    }
    
    private void update() {
        if (settingData.needsUpdateTreeView()) {
            System.out.println("Tree update");
        }
    }
    
    class BranchLengthSelectionListener implements SelectionListener {
        
        public void widgetSelected(SelectionEvent e) {
            Combo combo = (Combo)e.getSource();
            String name = combo.getItem(combo.getSelectionIndex());
            settingData.setBranchLength(name);
            
            update();
        }
        
        public void widgetDefaultSelected(SelectionEvent e) {
        }
    }
    
    class LeafNumberSelectionListener implements SelectionListener {
        
        public void widgetSelected(SelectionEvent e) {
            Combo combo = (Combo)e.getSource();
            String name = combo.getItem(combo.getSelectionIndex());
            settingData.setLeafNumber(name);
            
            update();
        }
        
        public void widgetDefaultSelected(SelectionEvent e) {
        }
    }
    
    class LeafSizeSelectionListener implements SelectionListener {
        
        public void widgetSelected(SelectionEvent e) {
            Combo combo = (Combo)e.getSource();
            String name = combo.getItem(combo.getSelectionIndex());
            settingData.setLeafSize(name);
            
            update();
        }
        
        public void widgetDefaultSelected(SelectionEvent e) {
        }
    }
    
    class LeafColorSelectionListener implements SelectionListener {
        
        public void widgetSelected(SelectionEvent e) {
            Combo combo = (Combo)e.getSource();
            String name = combo.getItem(combo.getSelectionIndex());
            settingData.setLeafColor(name);
            
            update();
        }
        
        public void widgetDefaultSelected(SelectionEvent e) {
        }
    }
}
