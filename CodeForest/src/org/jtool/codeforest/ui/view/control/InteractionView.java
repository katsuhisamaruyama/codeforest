/*
 *  Copyright 2014, Katsuhisa Maruyama (maru@jtool.org)
 */

package org.jtool.codeforest.ui.view.control;

import org.jtool.codeforest.ui.CodeForestFrame;
import org.jtool.codeforest.ui.view.SettingData;
import org.jtool.codeforest.util.Time;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Creates a view that displays the history of user interactions.
 * @author Katsuhisa Maruyama
 */
public class InteractionView {
    
    private List<InteractionRecord> interactionList;
    
    private CheckboxTableViewer tableViewer;
    
    /**
     * Information on the font.
     */
    private Font font11;
    
    private CodeForestFrame frame;
    
    public InteractionView(Composite parent, CodeForestFrame frame) {
        interactionList = new ArrayList<InteractionRecord>();
        sort(interactionList);
        
        this.frame = frame;
        
        createPane(parent);
    }
    
    private void retrive(String keyword) {
        for (InteractionRecord record : interactionList) {
            String description = record.getDescription();
            if (keyword.length() == 0) {
                tableViewer.setChecked(record, true);
            } else {
                if (description.indexOf(keyword) != -1) {
                    tableViewer.setChecked(record, true);
                } else {
                    tableViewer.setChecked(record, false);
                }
            }
        }
    }
    
    private void createPane(Composite parent) {
        final int MARGIN = 0;
        final int MARGIN2 = 2;
        final int TEXT_WIDTH = 150;
        font11 = new Font(parent.getDisplay(), "", 11, SWT.NORMAL);
        
        parent.setLayout(new FormLayout());
        
        Composite top = new Composite(parent, SWT.NONE);
        top.setLayout(new FormLayout());
        
        FormData topdata = new FormData();
        topdata.top = new FormAttachment(0, MARGIN);
        topdata.left = new FormAttachment(0, MARGIN);
        topdata.right = new FormAttachment(100, -MARGIN);
        top.setLayoutData(topdata);
        
        final Text text = new Text(top, SWT.BORDER | SWT.SINGLE);
        text.setTextLimit(40);
        text.addSelectionListener(new SelectionListener() {
            
            public void widgetSelected(SelectionEvent e) {
            }
            
            public void widgetDefaultSelected(SelectionEvent e) {
                retrive(text.getText());
            }
        });
        
        Button findButton = new Button(top, SWT.FLAT);
        findButton.setText("Find");
        findButton.addSelectionListener(new SelectionListener() {
            
            public void widgetSelected(SelectionEvent e) {
                retrive(text.getText());
            }
            
            public void widgetDefaultSelected(SelectionEvent e) {
            }
        });
        
        FormData btdata = new FormData();
        btdata.top = new FormAttachment(0, MARGIN2);
        btdata.right = new FormAttachment(100, -MARGIN2);
        findButton.setLayoutData(btdata);
        
        FormData ttdata = new FormData();
        ttdata.top = new FormAttachment(0, MARGIN2);
        ttdata.right = new FormAttachment(findButton, -(MARGIN2 + MARGIN2));
        ttdata.width = TEXT_WIDTH;
        text.setLayoutData(ttdata);
        
        tableViewer = CheckboxTableViewer.newCheckList(parent, SWT.BORDER | SWT.SINGLE);
        tableViewer.setContentProvider(new ArrayContentProvider());
        tableViewer.setLabelProvider(new InteractionLabelProvider());
        
        tableViewer.getTable().setLinesVisible(true);
        tableViewer.getTable().setHeaderVisible(true);
        tableViewer.getTable().setFont(font11);
        
        tableViewer.addDoubleClickListener(new IDoubleClickListener() {
            public void doubleClick(DoubleClickEvent event) {
                InteractionRecord record = getInteractionRecord();
                if (record != null) {
                    if (record.isWorkingSetInteraction() || record.isSettingInteraction() || record.isFocusClassInteraction()) {
                        frame.getSettingView().change(record);
                        recordWorkingSetAction(frame.getSettingData(), "change", "");
                    }
                }
            }
        });
        
        TableColumn timeColumn = new TableColumn(tableViewer.getTable(), SWT.LEFT);
        timeColumn.setText("time");
        timeColumn.setWidth(150);
        timeColumn.setResizable(true);
        
        TableColumn detailsColumn = new TableColumn(tableViewer.getTable(), SWT.LEFT);
        detailsColumn.setText("description");
        detailsColumn.setWidth(600);
        detailsColumn.setResizable(true);
        
        FormData tbdata = new FormData();
        tbdata.top = new FormAttachment(top, MARGIN);
        tbdata.bottom = new FormAttachment(100, -MARGIN);
        tbdata.left = new FormAttachment(0, MARGIN);
        tbdata.right = new FormAttachment(100, -MARGIN);
        tableViewer.getTable().setLayoutData(tbdata);
        
        makeContextMenuAction();
        
        parent.pack();
        
        tableViewer.setInput(interactionList);
    }
    
    private void makeContextMenuAction() {
        Menu menu = new Menu(frame.getShell(), SWT.POP_UP);
        
        MenuItem editItem = new MenuItem(menu, SWT.PUSH);
        editItem.setText("Edit");
        editItem.addSelectionListener(new SelectionListener() {
            
            public void widgetSelected(SelectionEvent e) {
                InteractionRecord record = getInteractionRecord();
                if (record != null) {
                    EditInteractionDialog dialog = new EditInteractionDialog(frame.getShell());
                    dialog.create();
                    dialog.open();
                    
                    record.setDescription(dialog.getDescription());
                    tableViewer.setInput(interactionList);
                }
            }
            
            public void widgetDefaultSelected(SelectionEvent e) {
            }
        });
        
        MenuItem removeItem = new MenuItem(menu, SWT.PUSH);
        removeItem.setText("Remove");
        removeItem.addSelectionListener(new SelectionListener() {
            
            public void widgetSelected(SelectionEvent e) {
                InteractionRecord record = getInteractionRecord();
                if (record != null) {
                    interactionList.remove(record);
                    
                    tableViewer.setInput(interactionList);
                }
            }
            
            public void widgetDefaultSelected(SelectionEvent e) {
            }
        });
        
        tableViewer.getControl().setMenu(menu);
    }
    
    private InteractionRecord getInteractionRecord() {
        if (tableViewer.getSelection() instanceof IStructuredSelection) {
            IStructuredSelection selection = (IStructuredSelection)tableViewer.getSelection();
            Object elem = selection.getFirstElement();
            
            if (elem instanceof InteractionRecord) {
                return (InteractionRecord)elem;
            }
        }
        return null;
    }
    
    /**
     * Disposes this interaction view.
     */
    public void dispose() {
        font11.dispose();
        tableViewer = null;
        
        interactionList.clear();
    }
    
    public List<InteractionRecord> getInteractionRecordList() {
        return interactionList;
    }
    
    public void add(InteractionRecord record) {
        interactionList.add(record);
    }
    
    public void clear() {
        interactionList.clear();
    }
    
    public int size() {
        return interactionList.size();
    }
    
    public InteractionRecord getInteractionRecord(int index) {
        return interactionList.get(index);
    }
    
    public void sort() {
        sort(interactionList);
    }
    
    private static void sort(List<InteractionRecord> records) {
        Collections.sort(records, new Comparator<InteractionRecord>() {
            
            public int compare(InteractionRecord r1, InteractionRecord r2) {
                long time1 = r1.getTime();
                long time2 = r2.getTime();
                
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
    
    private static final String SYSTEM_ACTION = "%s for %s";
    private static final String SETTING_ACTION = "change %s into %s";
    private static final String WORKING_SET_ACTION = "%s a working set %s";
    private static final String FOCUS_CLASS_ACTION = "select %s";
    private static final String MEMO_ACTION = "%s a memo of %s";
    
    public void recordSystemAction(SettingData data, String action, String projectName) {
        long time = Time.getCurrentTime();
        String description = String.format(SYSTEM_ACTION, action, projectName);
        InteractionRecord record = new InteractionRecord(time, description, InteractionRecord.SYSTEM, data);
        
        interactionList.add(record);
        
        refreshInteractionList();
    }
    
    public void recordSettingAction(SettingData data, String sort, String value) {
        long time = Time.getCurrentTime();
        String description = String.format(SETTING_ACTION, sort, value);
        InteractionRecord record = new InteractionRecord(time, description, InteractionRecord.SETTING, data);
        
        interactionList.add(record);
        
        refreshInteractionList();
    }
    
    public void recordWorkingSetAction(SettingData data, String action, String name) {
        long time = Time.getCurrentTime();
        String description = String.format(WORKING_SET_ACTION, action, name);
        InteractionRecord record = new InteractionRecord(time, description, InteractionRecord.WORKING_SET, data);
        
        interactionList.add(record);
        
        refreshInteractionList();
    }
    
    public void recordFocusClassAction(SettingData data, String className) {
        long time = Time.getCurrentTime();
        String description = String.format(FOCUS_CLASS_ACTION, className);
        InteractionRecord record = new InteractionRecord(time, description, InteractionRecord.FOCUS_CLASS, data);
        
        interactionList.add(record);
        refreshInteractionList();
    }
    
    public void recordMemoAction(SettingData data, String action, String className) {
        long time = Time.getCurrentTime();
        String description = String.format(MEMO_ACTION, action, className);
        InteractionRecord record = new InteractionRecord(time, description, InteractionRecord.MEMO, data);
        
        interactionList.add(record);
        refreshInteractionList();
    }
    
    public void recordOtherAction(SettingData data, String description) {
        long time = Time.getCurrentTime();
        InteractionRecord record = new InteractionRecord(time, description, InteractionRecord.OTHERS, data);
        
        interactionList.add(record);
        refreshInteractionList();
    }
    
    public void refreshInteractionList() {
        if (tableViewer.getTable() != null && !(tableViewer.getTable().isDisposed())) {
            tableViewer.getTable().getDisplay().syncExec(new Runnable() {
                
                public void run() {
                    try {
                        tableViewer.setInput(interactionList);
                        tableViewer.refresh(true, true);
                    } catch (Exception e) { /* empty */ }
                }
            });
        }
    }
    
    public int getItemCount() {
        return tableViewer.getTable().getItemCount();
    }
    
    public void changeSelection(int index) {
        tableViewer.getTable().select(index);
        
        int top = reveal(index);
        tableViewer.getTable().setTopIndex(top);
    }
    
    /**
     * Reveals an interaction record with a given index.
     * @param index the index value of the revealed the interaction record
     * @return the index value of the top of the interaction records
     */
    private int reveal(int index) {
        Rectangle area = tableViewer.getTable().getClientArea();
        int num = area.height / tableViewer.getTable().getItemHeight() - 1;
        
        int top = tableViewer.getTable().getTopIndex();
        if (index < top) {
            top = index;
        } else if (index >= top + num) {
            top = index - num + 1;
        }
        
        return top;
    }
    
    /**
     * Centers an interaction record with a given index.
     * @param index index the index value of the revealed the interaction record
     * @return the index value of the top of the interaction records
     */
    @SuppressWarnings("unused")
    private int center(int index) {
        Rectangle area = tableViewer.getTable().getClientArea();
        int middle = area.height / tableViewer.getTable().getItemHeight() / 2 - 1;
        
        int top = index;
        if (index <= middle) {
            top = 0;
        } else if (tableViewer.getTable().getItemCount() - middle < index) {
            top = tableViewer.getTable().getItemCount() - middle * 2;
            
        } else {
            top = index - middle;
        }
        
        return top;
    }
    
    /**
     * A label provider that provides label texts of a table.
     * @author Katsuhisa Maruyama
     */
    private class InteractionLabelProvider extends LabelProvider implements ITableLabelProvider {
        
        /**
         * Returns the label text for the given column of the given element.
         * @param obj the object representing the entire row
         * @param columnIndex the zero-based index of the column in which the label appears
         * @return the label text or or <code>null</code> if there is no label text
         */
        public String getColumnText(Object obj, int columnIndex) {
            if (obj instanceof InteractionRecord) {
                InteractionRecord record = (InteractionRecord)obj;
                if (columnIndex == 0) {
                    if (record.getImage() != null) {
                        return record.getTimeRepresentation();
                    } else {
                        return "      " + record.getTimeRepresentation();
                    }
                } else if (columnIndex == 1) {
                    return record.getDescription();
                }
            }
            return null;
        }
        
        /**
         * Returns the label image for the given column of the given element.
         * @param obj the object representing the entire row
         * @param columnIndex the zero-based index of the column in which the label appears
         * @return the image or <code>null</code> if there is no image
         */
        public Image getColumnImage(Object obj, int columnIndex) {
            if (obj instanceof InteractionRecord) {
                InteractionRecord record = (InteractionRecord)obj;
                if (columnIndex == 0) {
                    return record.getImage();
                }
            }
            return null;
        }
    }
}
