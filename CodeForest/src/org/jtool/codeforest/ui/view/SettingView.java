/*
 *  Copyright 2014, Katsuhisa Maruyama (maru@jtool.org)
 */

package org.jtool.codeforest.ui.view;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
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
import org.jtool.codeforest.ui.view.forest.ForestData;

/**
 * Provides a setting view for selecting metrics and a layout.
 * @author Katsuhisa Maruyama
 */
public class SettingView {
    
    private Font font11;
    
    private CodeForestFrame frame;
    
    private SettingData settingData;
    
    private Combo theightSel, tradiusSel, tcolorSel, fheightSel, fradiusSel, fcolorSel;
    
    public SettingView(Composite parent, CodeForestFrame frame) {
        this.frame = frame;
        settingData = frame.getSettingData();
        createPane(parent);
    }
    
    public void dispose() {
        font11.dispose();
        frame.dispose();
        
        settingData = null;
    }
    
    private void createPane(Composite parent) {
        font11 = new Font(parent.getDisplay(), "", 11, SWT.NORMAL);
        
        GridLayout glayout = new GridLayout(1, true);
        glayout.verticalSpacing = 0;
        glayout.marginHeight = 0;
        parent.setLayout(glayout);
        
        Group settings = new Group(parent, SWT.NONE);
        GridLayout flayout = new GridLayout(2, true);
        flayout.horizontalSpacing = 0;
        flayout.verticalSpacing = 0;
        glayout.marginHeight = 0;
        settings.setLayout(flayout);
        settings.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
        theightSel = createMetricSelection(settings, SettingData.TRUNK_HEIGHT, settingData.getHeightItems(false));
        theightSel.select(0);
        settingData.setTrunkHeight(theightSel.getItem(0));
        theightSel.addSelectionListener(new TrunkHeightSelectionListener());
        
        tradiusSel = createMetricSelection(settings, SettingData.TRUNK_RADIUS, settingData.getWidthItems(false));
        tradiusSel.select(0);
        settingData.setTrunkRadius(tradiusSel.getItem(0));
        tradiusSel.addSelectionListener(new TrunkRadiusSelectionListener());
        
        tcolorSel = createMetricSelection(settings, SettingData.TRUNK_COLOR, settingData.getClassItems(true));
        tcolorSel.select(0);
        settingData.setTrunkColor(tcolorSel.getItem(0));
        tcolorSel.addSelectionListener(new TrunkColorSelectionListener());
        
        fheightSel = createMetricSelection(settings, SettingData.FOLIAGE_HEIGHT, settingData.getClassItems(true));
        fheightSel.select(0);
        settingData.setFoliageHeight(fheightSel.getItem(0));
        fheightSel.addSelectionListener(new FoliageHeightSelectionListener());
        
        fradiusSel = createMetricSelection(settings, SettingData.FOLIAGE_RADIUS, settingData.getClassItems(true));
        fradiusSel.select(0);
        settingData.setFoliageRadius(fradiusSel.getItem(0));
        fradiusSel.addSelectionListener(new FoliageRadiusSelectionListener());
        
        fcolorSel = createMetricSelection(settings, SettingData.FOLIAGE_COLOR, settingData.getClassItems(true));
        fcolorSel.select(0);
        settingData.setFoliageColor(fcolorSel.getItem(0));
        fcolorSel.addSelectionListener(new FoliageColorSelectionListener());
        
        createButtons(parent);
        
        parent.pack();
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
    
    private void createButtons(Composite parent) {
        Composite buttons = new Composite(parent, SWT.NONE);
        GridLayout blayout = new GridLayout(3, false);
        blayout.verticalSpacing = 0;
        blayout.marginHeight = 2;
        buttons.setLayout(blayout);
        
        Button lock = new Button(buttons, SWT.CHECK);
        lock.setFont(font11);
        lock.setText("Lock");
        lock.addSelectionListener(new LockButtonListener());
        
        Button syncCopy = new Button(buttons, SWT.PUSH);
        syncCopy.setText("Sync Copy");
        syncCopy.setFont(font11);
        GridData ldata = new GridData();
        ldata.horizontalIndent = 70;
        syncCopy.setLayoutData(ldata);
        syncCopy.addSelectionListener(new SyncCopyButtonListener());
        
        Button asyncCopy = new Button(buttons, SWT.PUSH);
        asyncCopy.setFont(font11);
        asyncCopy.setText("Async Copy");
        asyncCopy.addSelectionListener(new AsyncCopyButtonListener());
    }
    
    private void update() {
        ForestData fdata = frame.getForestData();
        fdata.update();
        
        if (settingData.needsUpdateForestView()) {
            frame.getForestView().update(fdata);
            
            System.out.println("Forest update");
        }
    }
    
    class TrunkHeightSelectionListener implements SelectionListener {
        
        public void widgetSelected(SelectionEvent e) {
            Combo combo = (Combo)e.getSource();
            String name = combo.getItem(combo.getSelectionIndex());
            settingData.setTrunkHeight(name);
            
            update();
        }
        
        public void widgetDefaultSelected(SelectionEvent e) {
        }
    }
    
    class TrunkRadiusSelectionListener implements SelectionListener {
        
        public void widgetSelected(SelectionEvent e) {
            Combo combo = (Combo)e.getSource();
            String name = combo.getItem(combo.getSelectionIndex());
            settingData.setTrunkRadius(name);
            
            update();
        }
        
        public void widgetDefaultSelected(SelectionEvent e) {
        }
    }
    
    class TrunkColorSelectionListener implements SelectionListener {
        
        public void widgetSelected(SelectionEvent e) {
            Combo combo = (Combo)e.getSource();
            String name = combo.getItem(combo.getSelectionIndex());
            settingData.setTrunkColor(name);
            
            update();
        }
        
        public void widgetDefaultSelected(SelectionEvent e) {
        }
    }
    
    class FoliageHeightSelectionListener implements SelectionListener {
        
        public void widgetSelected(SelectionEvent e) {
            Combo combo = (Combo)e.getSource();
            String name = combo.getItem(combo.getSelectionIndex());
            settingData.setFoliageHeight(name);
            
            update();
        }
        
        public void widgetDefaultSelected(SelectionEvent e) {
        }
    }
    
    class FoliageRadiusSelectionListener implements SelectionListener {
        
        public void widgetSelected(SelectionEvent e) {
            Combo combo = (Combo)e.getSource();
            String name = combo.getItem(combo.getSelectionIndex());
            settingData.setFoliageRadius(name);
            
            update();
        }
        
        public void widgetDefaultSelected(SelectionEvent e) {
        }
    }
    
    class FoliageColorSelectionListener implements SelectionListener {
        
        public void widgetSelected(SelectionEvent e) {
            Combo combo = (Combo)e.getSource();
            String name = combo.getItem(combo.getSelectionIndex());
            settingData.setFoliageColor(name);
            
            update();
        }
        
        public void widgetDefaultSelected(SelectionEvent e) {
        }
    }
    
    class LockButtonListener implements SelectionListener {
        
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
    }
    
    class SyncCopyButtonListener implements SelectionListener {
        
        public void widgetSelected(SelectionEvent e) {
            System.out.println("Sync copy");
        }
        
        public void widgetDefaultSelected(SelectionEvent e) {
        }
    }
    
    class AsyncCopyButtonListener implements SelectionListener {
        
        public void widgetSelected(SelectionEvent e) {
            System.out.println("Async copy");
        }
        
        public void widgetDefaultSelected(SelectionEvent e) {
        }
    }
}
