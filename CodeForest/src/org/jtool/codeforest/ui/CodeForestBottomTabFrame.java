/*
 *  Copyright 2014, Katsuhisa Maruyama (maru@jtool.org)
 */

package org.jtool.codeforest.ui;

import org.jtool.codeforest.Activator;
import org.jtool.codeforest.metrics.java.ProjectMetrics;
import org.jtool.codeforest.ui.view.PropertyView;
import org.jtool.codeforest.ui.view.control.InteractionView;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;

/**
 * Provides a base frame that lays out forest and tree views.
 * @author Katsuhisa Maruyama
 */
public class CodeForestBottomTabFrame {
    
    private CTabFolder tabFolder;
    
    private SelectionListener tabFrameSelectionListener;
    
    private static final String PROPERTY_VIEW_TITLE = "Properties";
    
    private static final String INTERACTION_VIEW_TITLE = "Interaction";
    
    private PropertyView propertyView;
    
    private InteractionView interactionView;
    
    CodeForestBottomTabFrame(CodeForestFrame frame) {
        Shell shell = frame.getShell();
        ProjectMetrics mproject = frame.getProjectMetrics();
        
        tabFolder = new CTabFolder(shell, SWT.BORDER);
        tabFolder.setTabHeight(24);
        tabFolder.setMaximizeVisible(false);
        tabFolder.setMinimizeVisible(false);
        tabFolder.setUnselectedCloseVisible(false);
        tabFolder.setSelectionBackground(new Color[] {
                shell.getDisplay().getSystemColor(SWT.COLOR_WIDGET_LIGHT_SHADOW),
                shell.getDisplay().getSystemColor(SWT.COLOR_WIDGET_LIGHT_SHADOW)}, new int[] { });
        Rectangle bounds = tabFolder.getBounds();
        
        Composite propertyPanel = new Composite(tabFolder, SWT.NONE);
        propertyPanel.setBounds(0, 0, bounds.width, bounds.height);
        propertyView = new PropertyView(propertyPanel, mproject);
        
        CTabItem propertyViewTab = new CTabItem(tabFolder, SWT.NONE);
        propertyViewTab.setText(PROPERTY_VIEW_TITLE);
        propertyViewTab.setControl(propertyPanel);
        propertyViewTab.setImage(Activator.getImage("properties"));
        tabFolder.setSelection(propertyViewTab);
        
        Composite interactionPanel = new Composite(tabFolder, SWT.NONE);
        interactionPanel.setBounds(0, 0, bounds.width, bounds.height);
        interactionView = new InteractionView(interactionPanel, frame);
        
        CTabItem interactionViewTab = new CTabItem(tabFolder, SWT.NONE);
        interactionViewTab.setText(INTERACTION_VIEW_TITLE);
        interactionViewTab.setControl(interactionPanel);
        interactionViewTab.setImage(Activator.getImage("task"));
        
        tabFrameSelectionListener = new TabFrameSelectionListener();
        tabFolder.addSelectionListener(tabFrameSelectionListener);
    }
    
    CTabFolder getTabFolder() {
        return tabFolder;
    }
    
    PropertyView getPropertyView() {
        return propertyView;
    }
    
    InteractionView getInteractionView() {
        return interactionView;
    }
    
    void focusPropertyView() {
        tabFolder.setSelection(0);
    }
    
    void focusInteractionView() {
        tabFolder.setSelection(1);
    }
    
    void dispose() {
        propertyView.dispose();
        interactionView.dispose();
        tabFolder.dispose();
    }
    
    private class TabFrameSelectionListener implements SelectionListener {
        
        public void widgetSelected(SelectionEvent evt) {
            /*
            CTabItem tab = tabFolder.getSelection();
            String title = tab.getText();
            
            if (title.compareTo(INTERACTION_VIEW_TITLE) == 0) {
            } else if (title.compareTo(MEMO_VIEW_TITLE) == 0) {
            }
            */
        }
        
        public void widgetDefaultSelected(SelectionEvent e) {
        }
    }
}
