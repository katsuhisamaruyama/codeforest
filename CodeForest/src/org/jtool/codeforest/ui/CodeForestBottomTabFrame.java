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

/**
 * Provides a tab frame that displays property and interaction views.
 * @author Katsuhisa Maruyama
 */
public class CodeForestBottomTabFrame {
    
    /**
     * A folder that holds tabs display property and interaction views.
     */
    private CTabFolder tabFolder;
    
    /**
     * The title string that represents the property view.
     */
    private static final String PROPERTY_VIEW_TITLE = "Properties";
    
    /**
     * The title string that represents the interaction view.
     */
    private static final String INTERACTION_VIEW_TITLE = "Interaction";
    
    /**
     * A property view.
     */
    private PropertyView propertyView;
    
    /**
     * An interaction view.
     */
    private InteractionView interactionView;
    
    /**
     * Creates tab frame display on the bottom of the window.
     * @param frame the main frame
     */
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
    }
    
    /**
     * Obtains the tab folder.
     * @return the tab folder
     */
    CTabFolder getTabFolder() {
        return tabFolder;
    }
    
    /**
     * Obtains the property view.
     * @return the property view
     */
    PropertyView getPropertyView() {
        return propertyView;
    }
    
    /**
     * Obtains the interaction view.
     * @return the interaction view.
     */
    InteractionView getInteractionView() {
        return interactionView;
    }
    
    /**
     * Focuses on the property view.
     */
    void focusPropertyView() {
        tabFolder.setSelection(0);
    }
    
    /**
     * Focuses on the interaction view.
     */
    void focusInteractionView() {
        tabFolder.setSelection(1);
    }
    
    /**
     * Disposes this tab frame.
     */
    void dispose() {
        propertyView.dispose();
        interactionView.dispose();
        tabFolder.dispose();
    }
}
