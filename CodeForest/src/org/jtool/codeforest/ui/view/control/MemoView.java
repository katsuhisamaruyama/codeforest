/*
 *  Copyright 2014, Katsuhisa Maruyama (maru@jtool.org)
 */

package org.jtool.codeforest.ui.view.control;

import org.jtool.codeforest.Activator;
import org.jtool.codeforest.ui.CodeForestFrame;
import org.jtool.codeforest.ui.view.SettingData;
import org.jtool.codeforest.util.Time;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.custom.ViewForm;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;

/**
 * Creates a view that displays memos.
 * @author Katsuhisa Maruyama
 */
public class MemoView {
    
    private CodeForestFrame frame;
    
    private List<Memo> memoList;
    
    private Composite memoPanel;
    
    private Font font11;
    
    public MemoView(Composite parent, CodeForestFrame frame) {
        this.frame = frame;
        memoList = new ArrayList<Memo>();
        
        createPane(parent);
    }
    
    private void createPane(Composite parent) {
        final int MEMO_VIEW_HEIGHT = 5000;
        font11 = new Font(parent.getDisplay(), "", 11, SWT.NORMAL);
        
        parent.setLayout(new FillLayout());
        
        ScrolledComposite sc = new ScrolledComposite(parent, SWT.NONE | SWT.V_SCROLL);
        sc.setLayout(new FillLayout());
        sc.setMinHeight(MEMO_VIEW_HEIGHT);
        sc.setExpandVertical(true);
        sc.setExpandHorizontal(true);
        
        memoPanel = new Composite(sc, SWT.NONE);
        memoPanel.setLayout(new GridLayout(1, true));
        sc.setContent(memoPanel);
    }
    
    public void dispose() {
        memoPanel.dispose();
        memoPanel = null;
        
        memoList.clear();
    }
    
    public List<Memo> getMemoList(String className) {
        List<Memo> list = new ArrayList<Memo>();
        for (Memo memo : memoList) {
            if (memo.getClassName().compareTo(className) == 0) {
                list.add(memo);
            }
        }
        return list;
    }
    
    public List<Memo> getMemoList() {
        return memoList;
    }
    
    public void add(Memo memo) {
        memoList.add(memo);
    }
    
    public void clear() {
        memoList.clear();
    }
    
    public int size() {
        return memoList.size();
    }
    
    public Memo getMemo(int index) {
        return memoList.get(index);
    }
    
    public void sort() {
        sort(memoList);
    }
    
    private static void sort(List<Memo> memos) {
        Collections.sort(memos, new Comparator<Memo>() {
            
            public int compare(Memo m1, Memo m2) {
                long time1 = m1.getTime();
                long time2 = m2.getTime();
                
                if (time1 > time2) {
                    return 1;
                } else if (time1 < time2) {
                    return -1;
                } else {
                    return 0;
                }
            }
        });
    }
    
    public void refreshMemoList() {
    }
    
    public void addMemo(final String className) {
        if (memoPanel != null && !(memoPanel.isDisposed())) {
            memoPanel.getDisplay().syncExec(new Runnable() {
                public void run() {
                    try {
                        frame.focusMemoView();
                        showMemoList(className);
                        
                        AddMemoDialog dialog = new AddMemoDialog(frame.getShell(), className);
                        dialog.create();
                        dialog.open();
                        
                        String comments = dialog.getComments();
                        if (comments != null) {
                            Memo memo = new Memo(Time.getCurrentTime(), className, comments);
                            add(memo);
                            recordMemoAction("add", memo.getClassName());
                            showMemoList(className);
                        }
                        
                    } catch (Exception e) { /* empty */ }
                }
            });
        }
    }
    
    public void changeSelection(final String className) {
        if (memoPanel != null && !(memoPanel.isDisposed())) {
            memoPanel.getDisplay().syncExec(new Runnable() {
                
                public void run() {
                    try {
                        showMemoList(className);
                    } catch (Exception e) { /* empty */ }
                }
            });
        }
    }
    
    private void showMemoList(String className) {
        clearMemoListView();
        
        for (int i = memoList.size() - 1; i >= 0; i--) {
            Memo memo = memoList.get(i);
            if (memo.getClassName().compareTo(className) == 0) {
                createMemoText(memo);
            }
        }
    }
    
    private void clearMemoListView() {
        for (Control control : memoPanel.getChildren()) {
            control.dispose();
        }
    }
    
    private void createMemoText(final Memo memo) {
        final int MEMO_HEIGHT = 50;
        
        final ViewForm viewForm = new ViewForm(memoPanel, SWT.BORDER);
        GridData vfdata = new GridData(GridData.FILL_HORIZONTAL);
        vfdata.heightHint = MEMO_HEIGHT;
        viewForm.setLayoutData(vfdata);
        
        CLabel label = new CLabel(viewForm, SWT.NONE);
        label.setFont(font11);
        label.setText(Time.toString(memo.getTime()) + " - " + memo.getClassName());
        label.setAlignment(SWT.LEFT);
        viewForm.setTopLeft(label);
        
        final Text text = new Text(viewForm, SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
        text.setFont(font11);
        text.setText(memo.getComments());
        text.setEditable(false);
        viewForm.setContent(text);
        
        ToolBar toolBarMenu = new ToolBar(viewForm, SWT.FLAT);
        final ToolItem toolItem = new ToolItem(toolBarMenu, SWT.PUSH);
        toolItem.setImage(Activator.getImage("menu"));
        viewForm.setTopRight(toolBarMenu);
        
        final Menu menu = new Menu(toolBarMenu);
        toolItem.addSelectionListener(new SelectionListener() {
            
            public void widgetSelected(SelectionEvent e) {
                menu.setVisible(true);
            }
            
            public void widgetDefaultSelected(SelectionEvent e) {
            }
        });
        
        MenuItem edit = new MenuItem(menu, SWT.NONE);
        edit.setText("Edit");
        edit.addSelectionListener(new SelectionListener() {
            
            public void widgetSelected(SelectionEvent e) {
                text.setEditable(true);
                recordMemoAction("edit", memo.getClassName());
            }
            
            public void widgetDefaultSelected(SelectionEvent e) {
            }
        });
        
        MenuItem lock = new MenuItem(menu, SWT.NONE);
        lock.setText("Lock");
        lock.addSelectionListener(new SelectionListener() {
            
            public void widgetSelected(SelectionEvent e) {
                text.setEditable(false);
            }
            
            public void widgetDefaultSelected(SelectionEvent e) {
            }
        });
        
        MenuItem remove = new MenuItem(menu, SWT.NONE);
        remove.setText("Remove");
        remove.addSelectionListener(new SelectionListener() {
            
            public void widgetSelected(SelectionEvent e) {
                memoList.remove(memo);
                viewForm.dispose();
                showMemoList(memo.getClassName());
            }
            
            public void widgetDefaultSelected(SelectionEvent e) {
            }
        });
        
        memoPanel.layout();
    }
    
    private void recordMemoAction(String action, String className) {
        SettingData data = frame.getSettingData();
        InteractionView interactionView = frame.getInteractionView();
        interactionView.recordMemoAction(data, action, className);
    }
}
