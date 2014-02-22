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
    
    private Font font11;
    
    private Font font12;
    
    private CodeForestFrame frame;
    
    private SettingData settingData;
    
    private Combo theightSel, tradiusSel, tcolorSel, fheightSel, fradiusSel, fcolorSel;
    
    private Button undoButton, redoButton;
    
    private SettingData curSettingData = null;
    
    private Stack<SettingData> settingDataUndoStack = new Stack<SettingData>();
    
    private Stack<SettingData> settingDataRedoStack = new Stack<SettingData>();
    
    public SettingView(Composite parent, CodeForestFrame frame) {
        this.frame = frame;
        createSettingData();
        createPane(parent);
    }
    
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
    
    public void recordInitialization() {
        storeSettingData();
        
        WorkingSet defaultWorkingSet = WorkingSetStore.getDefaultWorkingSet();
        if (defaultWorkingSet != null) {
            recordWorkingSetAction("initialize", defaultWorkingSet.getName());
        } else {
            recordWorkingSetAction("initialize", "");
        }
    }
    
    public SettingData getSettingData() {
        return settingData;
    }
    
    public void dispose() {
        font11.dispose();
        font12.dispose();
        
        settingData = null;
    }
    
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
        settingData.setTrunkColor(tcolorSel.getItem(0));
        tcolorSel.addSelectionListener(new TrunkColorSelectionListener());
        
        fcolorSel = createMetricSelection(settings, SettingData.FOLIAGE_COLOR, settingData.getClassItems());
        settingData.setFoliageColor(fcolorSel.getItem(0));
        fcolorSel.addSelectionListener(new FoliageColorSelectionListener());
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
    
    protected void createSeparator(Composite parent) {
        Label label = new Label(parent, SWT.HORIZONTAL | SWT.SEPARATOR);
        
        GridData data = new GridData();
        data.horizontalAlignment = GridData.FILL;
        data.horizontalSpan = 2;
        data.grabExcessVerticalSpace = true;
        label.setLayoutData(data);
    }
    
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
            
            public void widgetSelected(SelectionEvent e) {
                WorkingSetDialog dialog = new WorkingSetDialog(frame.getShell(), frame);
                dialog.create();
                dialog.open();
            }
            
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
            
            public void widgetSelected(SelectionEvent e) {
                undoSettingData();
            }
            
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
            
            public void widgetSelected(SelectionEvent e) {
                redoSettingData();
            }
            
            public void widgetDefaultSelected(SelectionEvent e) {
            }
        });
    }
    
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
    
    public void change(SettingData data) {
        settingData.setData(data.getTrunkHeight(), data.getTrunkRadius(), data.getTrunkColor(),
        data.getFoliageHeight(), data.getFoliageRadius(), data.getFoliageColor(), 
        data.getLeafNumber(), data.getLeafColor());
        
        update();
        updateForest();
    }
    
    private void update() {
        theightSel.select(settingData.getHeightItemIndex(settingData.getTrunkHeight().getName()));
        tradiusSel.select(settingData.getWidthItemIndex(settingData.getTrunkRadius().getName()));
        tcolorSel.select(settingData.getClassItemIndex(settingData.getTrunkColor().getName()));
        fheightSel.select(settingData.getHeightItemIndex(settingData.getFoliageHeight().getName()));
        fradiusSel.select(settingData.getWidthItemIndex(settingData.getFoliageRadius().getName()));
        fcolorSel.select(settingData.getClassItemIndex(settingData.getFoliageColor().getName()));
    }
    
    private void updateForest() {
        SettingData data = frame.getSettingData();
        
        if (settingData.needsUpdateForestView()) {
            frame.getForestView().update(data);
        }
        
        if (settingData.needsUpdateTreeView()) {
            frame.getTreeView().update();
        }
    }
    
    private void recordSettingAction(String sort, String value) {
        InteractionView interactionView = frame.getInteractionView();
        interactionView.recordSettingAction(settingData, sort, value);
    }
    
    private void recordWorkingSetAction(String action, String name) {
        InteractionView interactionView = frame.getInteractionView();
        interactionView.recordWorkingSetAction(settingData, action, name);
    }
    
    private void recordOtherAction(String action) {
        InteractionView interactionView = frame.getInteractionView();
        interactionView.recordOtherAction(settingData, action);
    }
    
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
    
    class TrunkHeightSelectionListener implements SelectionListener {
        
        public void widgetSelected(SelectionEvent e) {
            Combo combo = (Combo)e.getSource();
            String name = combo.getItem(combo.getSelectionIndex());
            settingData.setTrunkHeight(name);
            storeSettingData();
            
            recordSettingAction("Trunk Height", name);
            updateForest();
        }
        
        public void widgetDefaultSelected(SelectionEvent e) {
        }
    }
    
    class TrunkRadiusSelectionListener implements SelectionListener {
        
        public void widgetSelected(SelectionEvent e) {
            Combo combo = (Combo)e.getSource();
            String name = combo.getItem(combo.getSelectionIndex());
            settingData.setTrunkRadius(name);
            storeSettingData();
            
            recordSettingAction("Trunk Radius", name);
            updateForest();
        }
        
        public void widgetDefaultSelected(SelectionEvent e) {
        }
    }
    
    class TrunkColorSelectionListener implements SelectionListener {
        
        public void widgetSelected(SelectionEvent e) {
            Combo combo = (Combo)e.getSource();
            String name = combo.getItem(combo.getSelectionIndex());
            settingData.setTrunkColor(name);
            storeSettingData();
            
            recordSettingAction("Trunk Color", name);
            updateForest();
        }
        
        public void widgetDefaultSelected(SelectionEvent e) {
        }
    }
    
    class FoliageHeightSelectionListener implements SelectionListener {
        
        public void widgetSelected(SelectionEvent e) {
            Combo combo = (Combo)e.getSource();
            String name = combo.getItem(combo.getSelectionIndex());
            settingData.setFoliageHeight(name);
            storeSettingData();
            
            recordSettingAction("Foliage Height", name);
            updateForest();
        }
        
        public void widgetDefaultSelected(SelectionEvent e) {
        }
    }
    
    class FoliageRadiusSelectionListener implements SelectionListener {
        
        public void widgetSelected(SelectionEvent e) {
            Combo combo = (Combo)e.getSource();
            String name = combo.getItem(combo.getSelectionIndex());
            settingData.setFoliageRadius(name);
            storeSettingData();
            
            recordSettingAction("Foliage Radius", name);
            updateForest();
        }
        
        public void widgetDefaultSelected(SelectionEvent e) {
        }
    }
    
    class FoliageColorSelectionListener implements SelectionListener {
        
        public void widgetSelected(SelectionEvent e) {
            Combo combo = (Combo)e.getSource();
            String name = combo.getItem(combo.getSelectionIndex());
            settingData.setFoliageColor(name);
            storeSettingData();
            
            recordSettingAction("Foliage Color", name);
            updateForest();
        }
        
        public void widgetDefaultSelected(SelectionEvent e) {
        }
    }
}
