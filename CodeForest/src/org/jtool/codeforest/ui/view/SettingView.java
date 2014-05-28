/*
 *  Copyright 2014, Katsuhisa Maruyama (maru@jtool.org)
 */

package org.jtool.codeforest.ui.view;

import org.jtool.codeforest.Activator;
import org.jtool.codeforest.ui.view.control.InteractionRecord;
import org.jtool.codeforest.ui.view.control.InteractionView;
import org.jtool.codeforest.ui.view.control.WorkingSet;
import org.jtool.codeforest.ui.view.control.WorkingSetDialog;
import org.jtool.codeforest.ui.view.control.WorkingSetStore;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.jtool.codeforest.ui.CodeForestFrame;
import java.util.Stack;

/**
 * Provides a setting view for selecting metrics and a layout.
 * @author Katsuhisa Maruyama
 */
public class SettingView {
    
    /**
     * Information on the font.
     */
    private Font font11;
    
    /**
     * Information on the font.
     */
    private Font font12;
    
    /**
     * The main frame.
     */
    private CodeForestFrame frame;
    
    /**
     * The setting data currently displayed.
     */
    private SettingData settingData;
    
    /**
     * A combo that selects a metric for the trunk height.
     */
    private Combo theightSel;
    
    /**
     * A combo that selects a metric for the trunk radius.
     */
    private Combo tradiusSel;
    
    /**
     * A combo that selects a metric for the trunk color.
     */
    private Combo tcolorSel;
    
    /**
     * A combo that selects a metric for the foliage height.
     */
    private Combo fheightSel;
    
    /**
     * A combo that selects a metric for the foliage radius.
     */
    private Combo fradiusSel;
    
    /**
     * A combo that selects a metric for the foliage color.
     */
    private Combo fcolorSel;
    
    /**
     * A button for undoing.
     */
    private Button undoButton;
    
    /**
     * A button for redoing.
     */
    private Button redoButton;
    
    /**
     * The current setting data.
     */
    private SettingData curSettingData = null;
    
    /**
     * A stack that stores setting data for undoing.
     */
    private Stack<SettingData> settingDataUndoStack = new Stack<SettingData>();
    
    /**
     * A stack that stores setting data for redoing.
     */
    private Stack<SettingData> settingDataRedoStack = new Stack<SettingData>();
    
    /**
     * Creates a setting view.
     * @param parent the parent of the setting view
     * @param frame the main frame
     */
    public SettingView(Composite parent, CodeForestFrame frame) {
        this.frame = frame;
        createSettingData();
        createPane(parent);
    }
    
    /**
     * Creates the setting data.
     */
    public void createSettingData() {
        settingData = new SettingData();
        
        WorkingSet defaultWorkingSet = WorkingSetStore.getDefaultWorkingSet();
        if (defaultWorkingSet != null) {
            settingData.setTrunkHeight(defaultWorkingSet.getTrunkHeight());
            settingData.setTrunkRadius(defaultWorkingSet.getTrunkRadius());
            settingData.setTrunkColor(defaultWorkingSet.getTrunkColor());
            settingData.setFoliageHeight(defaultWorkingSet.getFoliageHeight());
            settingData.setFoliageRadius(defaultWorkingSet.getFoliageRadius());
            settingData.setFoliageColor(defaultWorkingSet.getFoliageColor());
        }
    }
    
    /**
     * Records the initial working set.
     */
    public void recordInitialization() {
        storeSettingData();
        
        WorkingSet defaultWorkingSet = WorkingSetStore.getDefaultWorkingSet();
        if (defaultWorkingSet != null) {
            recordWorkingSetAction("initialize", defaultWorkingSet.getName());
        } else {
            recordWorkingSetAction("initialize", "");
        }
    }
    
    /**
     * Returns the setting data.
     * @return the setting data selected in this setting view
     */
    public SettingData getSettingData() {
        return settingData;
    }
    
    /**
     * Create the pane of this setting view.
     * @param parent the parent of the setting view
     */
    private void createPane(Composite parent) {
        font11 = new Font(parent.getDisplay(), "", 11, SWT.NORMAL);
        font12 = new Font(parent.getDisplay(), "", 12, SWT.NORMAL);
        
        GridLayout glayout = new GridLayout(1, true);
        glayout.verticalSpacing = 0;
        glayout.marginHeight = 0;
        glayout.marginWidth = 0;
        parent.setLayout(glayout);
        
        createTop(parent);
        createSettings(parent);
        
        update();
        
        parent.pack();
    }
    
    /**
     * Disposes this setting view.
     */
    public void dispose() {
        font11.dispose();
        font12.dispose();
        
        settingData = null;
    }
    
    /**
     * Creates settings that select metrics.
     * @param parent the parent of the setting view
     */
    private void createSettings(Composite parent) {
        Group settings = new Group(parent, SWT.NONE);
        GridLayout flayout = new GridLayout(2, true);
        flayout.horizontalSpacing = 0;
        flayout.verticalSpacing = 0;
        flayout.marginHeight = 0;
        flayout.marginWidth = 0;
        settings.setLayout(flayout);
        settings.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
        theightSel = createMetricSelection(settings, SettingData.TRUNK_HEIGHT, settingData.getClassItems());
        theightSel.addSelectionListener(new TrunkHeightSelectionListener());
        
        fheightSel = createMetricSelection(settings, SettingData.FOLIAGE_HEIGHT, settingData.getClassItems());
        fheightSel.addSelectionListener(new FoliageHeightSelectionListener());
        
        tradiusSel = createMetricSelection(settings, SettingData.TRUNK_RADIUS, settingData.getClassItems());
        tradiusSel.addSelectionListener(new TrunkRadiusSelectionListener());
        
        fradiusSel = createMetricSelection(settings, SettingData.FOLIAGE_RADIUS, settingData.getClassItems());
        fradiusSel.addSelectionListener(new FoliageRadiusSelectionListener());
        
        tcolorSel = createMetricSelection(settings, SettingData.TRUNK_COLOR, settingData.getClassItems());
        tcolorSel.addSelectionListener(new TrunkColorSelectionListener());
        
        fcolorSel = createMetricSelection(settings, SettingData.FOLIAGE_COLOR, settingData.getClassItems());
        fcolorSel.addSelectionListener(new FoliageColorSelectionListener());
    }
    
    /**
     * Creates a selection combo of a visual parameter.
     * @param parent the parent of this setting view
     * @param title the title of the metric
     * @param items the collection of the metrics
     * @return the created combo
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
     * Creates a separator in this setting view.
     * @param parent the parent of the setting view
     */
    protected void createSeparator(Composite parent) {
        Label label = new Label(parent, SWT.HORIZONTAL | SWT.SEPARATOR);
        
        GridData data = new GridData();
        data.horizontalAlignment = GridData.FILL;
        data.horizontalSpan = 2;
        data.grabExcessVerticalSpace = true;
        label.setLayoutData(data);
    }
    
    /**
     * Creates the top area of this setting view.
     * @param parent the parent of the setting view
     */
    private void createTop(Composite parent) {
        Composite top = new Composite(parent, SWT.NONE);
        GridLayout blayout = new GridLayout(5, false);
        blayout.verticalSpacing = 0;
        blayout.marginHeight = 2;
        top.setLayout(blayout);
        
        Label label = new Label(top, SWT.NONE);
        label.setFont(font12);
        label.setText("Forest Setting");
        
        Button lock = new Button(top, SWT.CHECK);
        lock.setFont(font11);
        lock.setText("Lock");
        lock.addSelectionListener(new SelectionListener() {
            
            /**
             * Invoked when selection occurs in the control.
             * @param e an event containing information about the selection
             */
            public void widgetSelected(SelectionEvent e) {
                Button button = (Button)e.getSource();
                if (button.getSelection()) {
                    theightSel.setEnabled(false);
                    tradiusSel.setEnabled(false);
                    tcolorSel.setEnabled(false);
                    fheightSel.setEnabled(false);
                    fradiusSel.setEnabled(false);
                    fcolorSel.setEnabled(false);
                } else {
                    theightSel.setEnabled(true);
                    tradiusSel.setEnabled(true);
                    tcolorSel.setEnabled(true);
                    fheightSel.setEnabled(true);
                    fradiusSel.setEnabled(true);
                    fcolorSel.setEnabled(true);
                }
            }
            
            /**
             * Invoked when default selection occurs in the control.
             * @param e an event containing information about the default selection
             */
            public void widgetDefaultSelected(SelectionEvent e) {
            }
        });
        
        Button workingSetButton = new Button(top, SWT.PUSH);
        workingSetButton.setFont(font11);
        workingSetButton.setText("Working Set");
        GridData ldata = new GridData();
        ldata.horizontalIndent = 25;
        workingSetButton.setLayoutData(ldata);
        
        workingSetButton.addSelectionListener(new SelectionListener() {
            
            /**
             * Invoked when selection occurs in the control.
             * @param e an event containing information about the selection
             */
            public void widgetSelected(SelectionEvent e) {
                WorkingSetDialog dialog = new WorkingSetDialog(frame.getShell(), frame);
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
        
        undoButton = new Button(top, SWT.PUSH | SWT.FLAT);
        undoButton.setImage(Activator.getImage("undo"));
        undoButton.setText("");
        undoButton.setEnabled(false);
        GridData undata = new GridData();
        undoButton.setLayoutData(undata);
        
        undoButton.addSelectionListener(new SelectionListener() {
            
            /**
             * Invoked when selection occurs in the control.
             * @param e an event containing information about the selection
             */
            public void widgetSelected(SelectionEvent e) {
                undoSettingData();
            }
            
            /**
             * Invoked when default selection occurs in the control.
             * @param e an event containing information about the default selection
             */
            public void widgetDefaultSelected(SelectionEvent e) {
            }
        });
        
        redoButton = new Button(top, SWT.PUSH | SWT.FLAT);
        redoButton.setImage(Activator.getImage("redo"));
        redoButton.setText("");
        redoButton.setEnabled(false);
        GridData redata = new GridData();
        redoButton.setLayoutData(redata);
        
        redoButton.addSelectionListener(new SelectionListener() {
            
            /**
             * Invoked when selection occurs in the control.
             * @param e an event containing information about the selection
             */
            public void widgetSelected(SelectionEvent e) {
                redoSettingData();
            }
            
            /**
             * Invoked when default selection occurs in the control.
             * @param e an event containing information about the default selection
             */
            public void widgetDefaultSelected(SelectionEvent e) {
            }
        });
    }
    
    /**
     * Changes a specified interaction record.
     * @param record the changed interaction record
     */
    public void change(InteractionRecord record) {
        settingData.setTrunkHeight(record.getTrunkHeight());
        settingData.setTrunkRadius(record.getTrunkRadius());
        settingData.setTrunkColor(record.getTrunkColor());
        settingData.setFoliageHeight(record.getFoliageHeight());
        settingData.setFoliageRadius(record.getFoliageRadius());
        settingData.setFoliageColor(record.getFoliageColor());
        storeSettingData();
        
        update();
        updateForest();
    }
    
    /**
     * Changes a specified working set.
     * @param workingSet the changed working set
     */
    public void change(WorkingSet workingSet) {
        settingData.setTrunkHeight(workingSet.getTrunkHeight());
        settingData.setTrunkRadius(workingSet.getTrunkRadius());
        settingData.setTrunkColor(workingSet.getTrunkColor());
        settingData.setFoliageHeight(workingSet.getFoliageHeight());
        settingData.setFoliageRadius(workingSet.getFoliageRadius());
        settingData.setFoliageColor(workingSet.getFoliageColor());
        storeSettingData();
        
        update();
        updateForest();
    }
    
    /**
     * Changes a specified setting.
     * @param data the changed setting data
     */
    public void change(SettingData data) {
        settingData.setData(data.getTrunkHeight(), data.getTrunkRadius(), data.getTrunkColor(),
        data.getFoliageHeight(), data.getFoliageRadius(), data.getFoliageColor(), 
        data.getLeafNumber(), data.getLeafColor());
        
        update();
        updateForest();
    }
    
    /**
     * Updates the settings.
     */
    private void update() {
        theightSel.select(settingData.getClassItemIndex(settingData.getTrunkHeight().getName()));
        tradiusSel.select(settingData.getClassItemIndex(settingData.getTrunkRadius().getName()));
        tcolorSel.select(settingData.getClassItemIndex(settingData.getTrunkColor().getName()));
        fheightSel.select(settingData.getClassItemIndex(settingData.getFoliageHeight().getName()));
        fradiusSel.select(settingData.getClassItemIndex(settingData.getFoliageRadius().getName()));
        fcolorSel.select(settingData.getClassItemIndex(settingData.getFoliageColor().getName()));
    }
    
    /**
     * Updates a forest in the forest and tree views.
     */
    private void updateForest() {
        SettingData data = frame.getSettingData();
        
        if (settingData.needsUpdateForestView()) {
            frame.getForestView().update(data);
        }
        
        if (settingData.needsUpdateTreeView()) {
            frame.getTreeView().update();
        }
    }
    
    /**
     * Records an action related to the setting.
     * @param action the action to be recorded
     * @param the value of the setting
     */
    private void recordSettingAction(String sort, String value) {
        InteractionView interactionView = frame.getInteractionView();
        interactionView.recordSettingAction(settingData, sort, value);
    }
    
    /**
     * Records an action related to the working set.
     * @param action the action to be recorded
     * @param the name of the working set
     */
    private void recordWorkingSetAction(String action, String name) {
        InteractionView interactionView = frame.getInteractionView();
        interactionView.recordWorkingSetAction(settingData, action, name);
    }
    
    /**
     * Records other action.
     * @param action the action to be recorded
     */
    private void recordOtherAction(String action) {
        InteractionView interactionView = frame.getInteractionView();
        interactionView.recordOtherAction(settingData, action);
    }
    
    /**
     * Stores the setting data.
     */
    private void storeSettingData() {
        SettingData data = settingData.cloneSettingData();
        if (curSettingData != null) {
            settingDataUndoStack.push(curSettingData);
            undoButton.setEnabled(true);
        }
        
        curSettingData = data;
        settingDataRedoStack.clear();
        redoButton.setEnabled(false);
    }
    
    /**
     * Undoes the setting data.
     */
    private void undoSettingData() {
        SettingData data = settingDataUndoStack.pop();
        if (settingDataUndoStack.size() == 0) {
            undoButton.setEnabled(false);
        }
        
        settingDataRedoStack.push(curSettingData);
        curSettingData = data;
        redoButton.setEnabled(true);
        
        change(data);
        recordOtherAction("undo");
    }
    
    /**
     * Redoes the setting data.
     */
    private void redoSettingData() {
        SettingData data = settingDataRedoStack.pop();
        if (settingDataRedoStack.size() == 0) {
            redoButton.setEnabled(false);
        }
        
        settingDataUndoStack.push(curSettingData);
        curSettingData = data;
        undoButton.setEnabled(true);
        
        change(data);
        recordOtherAction("redo");
    }
    
    /**
     * A listener for the selection of the trunk height.
     * @author Katsuhisa Maruyama
     */
    class TrunkHeightSelectionListener implements SelectionListener {
        
        /**
         * Invoked when selection occurs in the control.
         * @param e an event containing information about the selection
         */
        public void widgetSelected(SelectionEvent e) {
            Combo combo = (Combo)e.getSource();
            String name = combo.getItem(combo.getSelectionIndex());
            settingData.setTrunkHeight(name);
            storeSettingData();
            
            recordSettingAction("Trunk Height", name);
            updateForest();
        }
        
        /**
         * Invoked when default selection occurs in the control.
         * @param e an event containing information about the default selection
         */
        public void widgetDefaultSelected(SelectionEvent e) {
        }
    }
    
    /**
     * A listener for the selection of the trunk radius.
     * @author Katsuhisa Maruyama
     */
    class TrunkRadiusSelectionListener implements SelectionListener {
        
        /**
         * Invoked when selection occurs in the control.
         * @param e an event containing information about the selection
         */
        public void widgetSelected(SelectionEvent e) {
            Combo combo = (Combo)e.getSource();
            String name = combo.getItem(combo.getSelectionIndex());
            settingData.setTrunkRadius(name);
            storeSettingData();
            
            recordSettingAction("Trunk Radius", name);
            updateForest();
        }
        
        /**
         * Invoked when default selection occurs in the control.
         * @param e an event containing information about the default selection
         */
        public void widgetDefaultSelected(SelectionEvent e) {
        }
    }
    
    /**
     * A listener for the selection of the trunk color.
     * @author Katsuhisa Maruyama
     */
    class TrunkColorSelectionListener implements SelectionListener {
        
        /**
         * Invoked when selection occurs in the control.
         * @param e an event containing information about the selection
         */
        public void widgetSelected(SelectionEvent e) {
            Combo combo = (Combo)e.getSource();
            String name = combo.getItem(combo.getSelectionIndex());
            settingData.setTrunkColor(name);
            storeSettingData();
            
            recordSettingAction("Trunk Color", name);
            updateForest();
        }
        
        /**
         * Invoked when default selection occurs in the control.
         * @param e an event containing information about the default selection
         */
        public void widgetDefaultSelected(SelectionEvent e) {
        }
    }
    
    /**
     * A listener for the selection of the foliage height.
     * @author Katsuhisa Maruyama
     */
    class FoliageHeightSelectionListener implements SelectionListener {
        
        /**
         * Invoked when selection occurs in the control.
         * @param e an event containing information about the selection
         */
        public void widgetSelected(SelectionEvent e) {
            Combo combo = (Combo)e.getSource();
            String name = combo.getItem(combo.getSelectionIndex());
            settingData.setFoliageHeight(name);
            storeSettingData();
            
            recordSettingAction("Foliage Height", name);
            updateForest();
        }
        
        /**
         * Invoked when default selection occurs in the control.
         * @param e an event containing information about the default selection
         */
        public void widgetDefaultSelected(SelectionEvent e) {
        }
    }
    
    /**
     * A listener for the selection of the foliage radius.
     * @author Katsuhisa Maruyama
     */
    class FoliageRadiusSelectionListener implements SelectionListener {
        
        /**
         * Invoked when selection occurs in the control.
         * @param e an event containing information about the selection
         */
        public void widgetSelected(SelectionEvent e) {
            Combo combo = (Combo)e.getSource();
            String name = combo.getItem(combo.getSelectionIndex());
            settingData.setFoliageRadius(name);
            storeSettingData();
            
            recordSettingAction("Foliage Radius", name);
            updateForest();
        }
        
        /**
         * Invoked when default selection occurs in the control.
         * @param e an event containing information about the default selection
         */
        public void widgetDefaultSelected(SelectionEvent e) {
        }
    }
    
    /**
     * A listener for the selection of the foliage color.
     * @author Katsuhisa Maruyama
     */
    class FoliageColorSelectionListener implements SelectionListener {
        
        /**
         * Invoked when selection occurs in the control.
         * @param e an event containing information about the selection
         */
        public void widgetSelected(SelectionEvent e) {
            Combo combo = (Combo)e.getSource();
            String name = combo.getItem(combo.getSelectionIndex());
            settingData.setFoliageColor(name);
            storeSettingData();
            
            recordSettingAction("Foliage Color", name);
            updateForest();
        }
        
        /**
         * Invoked when default selection occurs in the control.
         * @param e an event containing information about the default selection
         */
        public void widgetDefaultSelected(SelectionEvent e) {
        }
    }
}
