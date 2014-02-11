/*
 *  Copyright 2014, Katsuhisa Maruyama (maru@jtool.org)
 */

package org.jtool.codeforest.ui.view.history;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;
import java.util.ArrayList;
import java.util.List;

/**
 * Creates a table that displays the history of user interactions.
 * @author Katsuhisa Maruyama
 */
public class TableControl {
    
    private HistoryView view;
    
    private Table table;
    
    private Font font11;
    
    private SelectionListener selectionListener;
    
    private KeyListener keyListener;
    
    private TraverseListener traverseListener;
    
    private List<InteractionData> interactionList = new ArrayList<InteractionData>();
    
    TableControl(HistoryView view, Composite parent, Control bottom, int margin) {
        this.view = view;
        createPane(parent, bottom, margin);
    }
    
    private void createPane(Composite parent, Control bottom, final int MARGIN) {
        font11 = new Font(parent.getDisplay(), "", 11, SWT.NORMAL);
        
        table = new Table(parent, SWT.BORDER | SWT.SINGLE);
        table.setLinesVisible(true);
        table.setHeaderVisible(true);
        // table.setBackground(new Color(null, 255, 255, 255));
        table.setFont(font11);
        
        selectionListener = new TableSelectionListener();
        table.addSelectionListener(selectionListener);
        keyListener = new TableKeyListener();
        table.addKeyListener(keyListener);
        traverseListener = new TableTraverseListener();
        table.addTraverseListener(traverseListener);
        
        TableColumn timeColumn = new TableColumn(table, SWT.LEFT);
        timeColumn.setText("time");
        timeColumn.setWidth(140);
        timeColumn.setResizable(false);
        
        TableColumn detailsColumn = new TableColumn(table, SWT.LEFT);
        detailsColumn.setText("description");
        detailsColumn.setWidth(200);
        detailsColumn.setResizable(true);
        
        FormData tbdata = new FormData();
        if (bottom != null) {
            tbdata.bottom = new FormAttachment(bottom, -MARGIN);
        } else {
            tbdata.bottom = new FormAttachment(100, -MARGIN);
        }
        tbdata.top = new FormAttachment(0, MARGIN);
        tbdata.left = new FormAttachment(0, MARGIN);
        tbdata.right = new FormAttachment(100, -MARGIN);
        table.setLayoutData(tbdata);
    }
    
    void dispose() {
        font11.dispose();
        table.removeSelectionListener(selectionListener);
        table.removeKeyListener(keyListener);
        table.removeTraverseListener(traverseListener);
        table.dispose();
        
        interactionList.clear();
    }
    
    Table getTable() {
        return table;
    }
    
    void addList(String time, String description, Image image) {
        InteractionData data = new InteractionData(time, description, image);
        interactionList.add(data);
        createTableItem(data);
    }
    
    void updateList() {
        for (int i = 0; i < interactionList.size(); i++) {
            InteractionData data = interactionList.get(i);
            createTableItem(data);
        }
    }
    
    private void createTableItem(InteractionData data) {
        TableItem item = new TableItem(table, SWT.NONE);
        
        if (data.getImage() != null) {
            item.setImage(data.getImage());
            item.setText(0, data.getTime());
        } else {
            item.setText(0, "        " + data.getTime());
        }
        item.setText(1, data.getDescription());
    }
    
    int getItemCount() {
        return table.getItemCount();
    }
    
    void resetTable() {
        table.deselectAll();
    }
    
    void changeSelection(final int index) {
        if (table != null && !(table.isDisposed())) {
            final int top = reveal(index);
            // final int top = center(index);
            table.getDisplay().syncExec(new Runnable() {
                
                public void run() {
                    try {
                        table.select(index);
                        table.setTopIndex(top);
                    } catch (Exception e) { /* empty */ }
                }
            });
        }
    }
    
    int reveal(int index) {
        Rectangle area = table.getClientArea();
        int num = area.height / table.getItemHeight() - 1;
        
        int top = table.getTopIndex();
        if (index < top) {
            top = index;
        } else if (index >= top + num) {
            top = index - num + 1;
        }
        
        return top;
    }
    
    int center(int index) {
        Rectangle area = table.getClientArea();
        int middle = area.height / table.getItemHeight() / 2 - 1;
        
        int top = index;
        if (index <= middle) {
            top = 0;
        } else if (table.getItemCount() - middle < index) {
            top = table.getItemCount() - middle * 2;
            
        } else {
            top = index - middle;
        }
        
        return top;
    }
    
    private class TableSelectionListener implements SelectionListener {
        
        TableSelectionListener() {
        }
        
        @Override
        public void widgetDefaultSelected(SelectionEvent evt) {
        }
        
        @Override
        public void widgetSelected(SelectionEvent evt) {
            int index = table.getSelectionIndex();
            view.goToDirectly(index);
        }
    }
    
    private class TableKeyListener implements KeyListener {
        
        TableKeyListener() {
        }
        
        @Override
        public void keyPressed(KeyEvent evt) {
        }
        
        @Override
        public void keyReleased(KeyEvent evt) {
            if (evt.keyCode == SWT.ARROW_UP) {
                view.goPrev();
            } else if (evt.keyCode == SWT.ARROW_DOWN) {
                view.goNext();
            } else if (evt.keyCode == SWT.ARROW_LEFT) {
                view.goPrev2();
            } else if (evt.keyCode == SWT.ARROW_RIGHT) {
                view.goNext2();
            }
        }
    }
    
    private class TableTraverseListener implements TraverseListener {
        
        TableTraverseListener() {
        }
        
        @Override
        public void keyTraversed(TraverseEvent evt) {
            if (evt.detail == SWT.TRAVERSE_ARROW_PREVIOUS || evt.detail == SWT.TRAVERSE_ARROW_NEXT) {
                evt.detail = SWT.TRAVERSE_NONE;
                evt.doit = true;
            }
        }
    }
    
    class InteractionData {
        
        private String time;
        
        private String description;
        
        private Image image;
        
        InteractionData(String time, String description, Image image) {
            this.time = time;
            this.description = description;
            this.image = image;
        }
        
        String getTime() {
            return time;
        }
        
        String getDescription() {
            return description;
        }
        
        Image getImage() {
            return image;
        }
    }
}
