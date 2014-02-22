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
 * Provides a base frame that lays out forest and tree views.
 * @author Katsuhisa Maruyama
 */
public class CodeForestTopTabFrame {
    
    private CTabFolder tabFolder;
    
    private static final String TREE_VIEW_TITLE = "Tree ";
    
    private static final String MEMO_VIEW_TITLE = "Memo ";
    
    private TreeView treeView;
    
    private MemoView memoView;
    
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
    
    CTabFolder getTabFolder() {
        return tabFolder;
    }
    
    TreeView getTreeView() {
        return treeView;
    }
    
    MemoView getMemoView() {
        return memoView;
    }
    
    void focusTreeView() {
        tabFolder.setSelection(0);
    }
    
    void focusMemoView() {
        tabFolder.setSelection(1);
    }
    
    void dispose() {
        treeView.dispose();
        memoView.dispose();
        tabFolder.dispose();
    }
}
