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
    
    /**
     * Information on the font.
     */
    private Font font11;
    
    /**
     * The setting data that forms a tree.
     */
    private SettingData settingData;
    
    /**
     * The combo of selection of the leaf numbers.
     */
    private Combo lnumberSel;
    
    /**
     * The combo of selection of the leaf color.
     */
    private Combo lcolorSel;
    
    /**
     * The metrics of the leaf.
     */
    private IMetric lnumberMetric;
    
    /**
     * The metrics of the color
     */
    private IMetric lcolorMetric;
    
    /**
     * The main frame.
     */
    private CodeForestFrame frame;
    
    /**
     * Creates a tree setting dialog.
     * @param frame the main frame
     */
    public TreeSettingDialog(CodeForestFrame frame) {
        super(frame.getShell());
        
        this.frame = frame;
        this.settingData = frame.getSettingData();
    }
    
    /**
     * Configures a given shell.
     * @param shell the shell
     */
    protected void configureShell(Shell shell) {
        super.configureShell(shell);
        shell.setText("Tree View Configuration");
    }
    
    /**
     * Returns the initial size of this dialog.
     * @return the initial size of the dialog
     */
    protected Point getInitialSize() {
        return new Point(330, 230);
    }
    
    /**
     * Creates this dialog.
     */
    public void create() {
        setHelpAvailable(false);
        setDialogHelpAvailable(false);
        super.create();
        setTitle("Configure settings of the tree view");
        setMessage("Change the sorts of metrics corresponding to respective properties.");
    }
    
    /**
     *  Creates the area for this dialog.
     *  @param parent the parent of this dialog
     */
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
    
    /**
     * Creates a combo to select the metric sort.
     * @param parent the parent of this dialog
     * @param title the title of this dialog
     * @param items the array of items to be selected
     * @return the selection combo
     */
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
    
    /**
     * A listener for the selection of the number of the leaves.
     * @author Katsuhisa Maruyama
     */
    class LeafNumberSelectionListener implements SelectionListener {
        
        /**
         * Invoked when selection occurs in the control.
         * @param e an event containing information about the selection
         */
        public void widgetSelected(SelectionEvent e) {
            Combo combo = (Combo)e.getSource();
            String name = combo.getItem(combo.getSelectionIndex());
            settingData.setLeafNumber(name);
            
            update();
        }
        
        /**
         * Invoked when default selection occurs in the control.
         * @param e an event containing information about the default selection
         */
        public void widgetDefaultSelected(SelectionEvent e) {
        }
    }
    
    /**
     * A listener for the selection of leaf color.
     * @author Katsuhisa Maruyama
     */
    class LeafColorSelectionListener implements SelectionListener {
        
        /**
         * Invoked when selection occurs in the control.
         * @param e an event containing information about the selection
         */
        public void widgetSelected(SelectionEvent e) {
            Combo combo = (Combo)e.getSource();
            String name = combo.getItem(combo.getSelectionIndex());
            settingData.setLeafColor(name);
            
            update();
        }
        
        /**
         * Invoked when default selection occurs in the control.
         * @param e an event containing information about the default selection
         */
        public void widgetDefaultSelected(SelectionEvent e) {
        }
    }
    
    /**
     * Updates this setting dialog.
     */
    private void update() {
        frame.getTreeView().update();
    }
    
    /**
     * Invoked when the ok button is pressed.
     */
    protected void okPressed() {
        super.okPressed();
    }
    
    /**
     * Invoked when the cancel button is pressed.
     */
    protected void cancelPressed() {
        settingData.setLeafNumber(lnumberMetric.getName());
        settingData.setLeafColor(lcolorMetric.getName());
        
        update();
        
        super.cancelPressed();
    }
}
