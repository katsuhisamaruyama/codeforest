/*
 *  Copyright 2014, Katsuhisa Maruyama (maru@jtool.org)
 */

package org.jtool.codeforest.ui;

import org.jtool.codeforest.ui.view.history.HistoryView;
import org.jtool.codeforest.ui.view.memo.MemoView;
import org.jtool.codeforest.ui.view.tree.TreeView;
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
public class CodeForestTabFrame {
    
    private CTabFolder tabFolder;
    
    private SelectionListener tabFrameSelectionListener;
    
    private static final String TREE_VIEW_TITLE = "Tree View";
    
    private static final String HISTORY_VIEW_TITLE = "History View";
    
    private static final String MEMO_VIEW_TITLE = "Momo View";
    
    private TreeView treeView;
    
    private HistoryView historyView;
    
    private MemoView memoView;
    
    public CodeForestTabFrame(Shell shell, CodeForestFrame frame) {
        tabFolder = new CTabFolder(shell, SWT.BORDER);
        tabFolder.setTabHeight(24);
        tabFolder.setMaximizeVisible(false);
        tabFolder.setMinimizeVisible(false);
        tabFolder.setUnselectedCloseVisible(false);
        tabFolder.setSimple(false);
        tabFolder.setSelectionBackground(new Color[] {
                shell.getDisplay().getSystemColor(SWT.COLOR_WIDGET_LIGHT_SHADOW),
                shell.getDisplay().getSystemColor(SWT.COLOR_WIDGET_LIGHT_SHADOW)}, new int[] { 50 });
        Rectangle bounds = tabFolder.getBounds();
        
        Composite treePanel = new Composite(tabFolder, SWT.BORDER | SWT.EMBEDDED);
        treePanel.setBounds(0, 0, bounds.width, bounds.height);
        treeView = new TreeView(treePanel, frame);
        
        CTabItem treeViewTab = new CTabItem(tabFolder, SWT.NONE);
        treeViewTab.setText(TREE_VIEW_TITLE);
        treeViewTab.setControl(treePanel);
        // tabFolder.setSelection(treeViewTab);
        
        Composite historyPanel = new Composite(tabFolder, SWT.BORDER | SWT.EMBEDDED);
        historyPanel.setBounds(0, 0, bounds.width, bounds.height);
        historyView = new HistoryView(historyPanel);
        
        CTabItem historyViewTab = new CTabItem(tabFolder, SWT.NONE);
        historyViewTab.setText(HISTORY_VIEW_TITLE);
        historyViewTab.setControl(historyPanel);
        tabFolder.setSelection(historyViewTab);
        
        Composite memoPanel = new Composite(tabFolder, SWT.BORDER | SWT.EMBEDDED);
        memoPanel.setBounds(0, 0, bounds.width, bounds.height);
        memoView = new MemoView(memoPanel);
        
        CTabItem memoViewTab = new CTabItem(tabFolder, SWT.NONE);
        memoViewTab.setText(MEMO_VIEW_TITLE);
        memoViewTab.setControl(memoPanel);
        
        tabFrameSelectionListener = new TabFrameSelectionListener();
        tabFolder.addSelectionListener(tabFrameSelectionListener);
    }
    
    public CTabFolder getTabFolder() {
        return tabFolder;
    }
    
    public TreeView getTreeView() {
        return treeView;
    }
    
    public HistoryView getHistoryView() {
        return historyView;
    }
    
    public MemoView getMemoView() {
        return memoView;
    }
    
    public void dispose() {
        treeView.dispose();
        historyView.dispose();
        memoView.dispose();
        
        for (CTabItem item : tabFolder.getItems()) {
            item.dispose();
        }
        tabFolder.removeSelectionListener(tabFrameSelectionListener);
        tabFolder.dispose();
    }
    
    private class TabFrameSelectionListener implements SelectionListener {
        
        @Override
        public void widgetSelected(SelectionEvent evt) {
            CTabItem tab = tabFolder.getSelection();
            String title = tab.getText();
            if (title.compareTo(TREE_VIEW_TITLE) == 0) {
                System.out.println("Tree View Selected!");
                
            } else if (title.compareTo(HISTORY_VIEW_TITLE) == 0) {
                System.out.println("History View Selected!");
                
            } else if (title.compareTo(MEMO_VIEW_TITLE) == 0) {
                System.out.println("Memo View Selected!");
            }
        }
        
        @Override
        public void widgetDefaultSelected(SelectionEvent e) {
        }
    }
}
