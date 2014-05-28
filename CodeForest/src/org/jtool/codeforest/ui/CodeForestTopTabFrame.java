/*
 *  Copyright 2014, Katsuhisa Maruyama (maru@jtool.org)
 */

package org.jtool.codeforest.ui;

import org.jtool.codeforest.Activator;
import org.jtool.codeforest.ui.view.control.MemoView;
import org.jtool.codeforest.ui.view.forest.TreeView;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;

/**
 * Provides a tab frame that displays tree and memo views.
 * @author Katsuhisa Maruyama
 */
public class CodeForestTopTabFrame {
    
    /**
     * A folder that holds tabs display tree and memo views.
     */
    private CTabFolder tabFolder;
    
    /**
     * The title string that represents the tree view.
     */
    private static final String TREE_VIEW_TITLE = "Tree ";
    
    /**
     * The title string that represents the memo view.
     */
    private static final String MEMO_VIEW_TITLE = "Memo ";
    
    /**
     * A tree view.
     */
    private TreeView treeView;
    
    /**
     * A memo view.
     */
    private MemoView memoView;
    
    /**
     * Creates tab frame display on the top of the window.
     * @param frame the main frame
     */
    CodeForestTopTabFrame(CodeForestFrame frame) {
        Shell shell = frame.getShell();
        
        tabFolder = new CTabFolder(shell, SWT.BORDER);
        tabFolder.setTabHeight(24);
        tabFolder.setMaximizeVisible(false);
        tabFolder.setMinimizeVisible(false);
        tabFolder.setUnselectedCloseVisible(false);
        tabFolder.setSelectionBackground(new Color[] {
                shell.getDisplay().getSystemColor(SWT.COLOR_WIDGET_LIGHT_SHADOW),
                shell.getDisplay().getSystemColor(SWT.COLOR_WIDGET_LIGHT_SHADOW)}, new int[] { });
        Rectangle bounds = tabFolder.getBounds();
        
        Composite treePanel = new Composite(tabFolder, SWT.NONE);
        treePanel.setBounds(0, 0, bounds.width, bounds.height);
        treeView = new TreeView(treePanel, frame);
        
        CTabItem treeViewTab = new CTabItem(tabFolder, SWT.NONE);
        treeViewTab.setText(TREE_VIEW_TITLE);
        treeViewTab.setControl(treePanel);
        treeViewTab.setImage(Activator.getImage("tree3"));
        tabFolder.setSelection(treeViewTab);
        
        Composite memoPanel = new Composite(tabFolder, SWT.NONE);
        memoPanel.setBounds(0, 0, bounds.width, bounds.height);
        memoView = new MemoView(memoPanel, frame);
        
        CTabItem memoViewTab = new CTabItem(tabFolder, SWT.NONE);
        memoViewTab.setText(MEMO_VIEW_TITLE);
        memoViewTab.setControl(memoPanel);
        memoViewTab.setImage(Activator.getImage("memo"));
    }
    
    /**
     * Obtains the tab folder.
     * @return the tab folder
     */
    CTabFolder getTabFolder() {
        return tabFolder;
    }
    
    /**
     * Obtains the tree view.
     * @return the tree view
     */
    TreeView getTreeView() {
        return treeView;
    }
    
    /**
     * Obtains the memo view.
     * @return the memo view
     */
    MemoView getMemoView() {
        return memoView;
    }
    
    /**
     * Focuses on the tree view.
     */
    void focusTreeView() {
        tabFolder.setSelection(0);
    }
    
    /**
     * Focuses on the memo view.
     */
    void focusMemoView() {
        tabFolder.setSelection(1);
    }
    
    /**
     * Disposes this tab frame.
     */
    void dispose() {
        treeView.dispose();
        memoView.dispose();
        tabFolder.dispose();
    }
}
