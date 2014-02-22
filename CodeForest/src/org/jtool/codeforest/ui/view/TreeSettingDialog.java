/*
 *  Copyright 2014, Katsuhisa Maruyama (maru@jtool.org)
 */

package org.jtool.codeforest.ui.view;

import org.jtool.codeforest.metrics.IMetric;
import org.jtool.codeforest.ui.CodeForestFrame;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;

public class TreeSettingDialog extends TitleAreaDialog {
    
    private Font font11;
    
    private SettingData settingData;
    
    private Combo lnumberSel, lcolorSel;
    
    private IMetric lnumberMetric, lcolorMetric;
    
    private CodeForestFrame frame;
    
    public TreeSettingDialog(CodeForestFrame frame) {
        super(frame.getShell());
        
        this.frame = frame;
        this.settingData = frame.getSettingData();
    }
    
    protected void configureShell(Shell shell) {
        super.configureShell(shell);
        shell.setText("Tree View Configuration");
    }
    
    protected Point getInitialSize() {
        return new Point(330, 230);
    }
    
    public void create() {
        setHelpAvailable(false);
        setDialogHelpAvailable(false);
        super.create();
        setTitle("Configure settings of the tree view");
        setMessage("Change the sorts of metrics corresponding to respective properties.");
    }
    
    protected Control createDialogArea(Composite parent) {
        font11 = new Font(parent.getDisplay(), "", 11, SWT.NORMAL);
        
        Group settings = new Group(parent, SWT.NONE);
        GridLayout flayout = new GridLayout(2, true);
        flayout.horizontalSpacing = 0;
        flayout.verticalSpacing = 0;
        flayout.marginHeight = 0;
        flayout.marginWidth = 0;
        settings.setLayout(flayout);
        settings.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
        lnumberSel = createMetricSelection(settings, SettingData.LEAF_NUMBER, settingData.getMethodItems());
        lnumberSel.select(0);
        settingData.setLeafNumber(lnumberSel.getItem(0));
        lnumberSel.addSelectionListener(new LeafNumberSelectionListener());
        
        lcolorSel = createMetricSelection(settings, SettingData.LEAF_COLOR, settingData.getMethodItems());
        lcolorSel.select(0);
        settingData.setLeafColor(lcolorSel.getItem(0));
        lcolorSel.addSelectionListener(new LeafColorSelectionListener());
        
        lnumberMetric = settingData.getLeafNumber();
        lcolorMetric = settingData.getLeafColor();
        
        return parent;
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
    
    private void update() {
        frame.getTreeView().update();
    }
    
    protected void okPressed() {
        super.okPressed();
    }
    
    protected void cancelPressed() {
        settingData.setLeafNumber(lnumberMetric.getName());
        settingData.setLeafColor(lcolorMetric.getName());
        
        update();
        
        super.cancelPressed();
    }
}
