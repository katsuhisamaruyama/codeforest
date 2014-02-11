/*
 *  Copyright 2014, Katsuhisa Maruyama (maru@jtool.org)
 */

package org.jtool.codeforest.ui.view.history;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FormLayout;

/**
 * Creates a view that displays the history of user interactions.
 * @author Katsuhisa Maruyama
 */
public class HistoryView {
    
    private TableControl tableControl;
    
    private ButtonControl buttonControl;
    
    private int focal;
    
    public HistoryView(Composite parent) {
        createPane(parent);
    }
    
    private void createPane(Composite parent) {
        parent.setLayout(new FormLayout());
        
        buttonControl = new ButtonControl(this, parent, null, 2);
        tableControl = new TableControl(this, parent, buttonControl.getButtons(), 2);
        
        focal = -1;
    }
    
    public void dispose() {
        tableControl.dispose();
        buttonControl.dispose();
    }
    
    public TableControl getTableControl() {
        return tableControl;
    }
    
    public ButtonControl getButtonControl() {
        return buttonControl;
    }
    
    public void resetTable() {
        tableControl.resetTable();
    }
    
    void addList(String time, String contents, Image image) {
        tableControl.addList(time, contents, image);
    }
    
    void updateList() {
        tableControl.updateList();
    }
    
    private void goTo(int index) {
        focal = index;
        tableControl.changeSelection(focal);
    }
    
    boolean goToDirectly(int index) {
        if (0 < index && index < tableControl.getItemCount()) {
            goTo(index);
            return true;
        }
        return false;
    }
    
    boolean goPrev() {
        if (0 < focal) {
            goTo(focal--);
            return true;
        }
        return false;
    }
    
    boolean goNext() {
        if (focal < tableControl.getItemCount() - 1) {
            goTo(focal++);
            return true;
        }
        return false;
    }
    
    boolean goPrev2() {
        if (0 < focal) {
            goTo(focal--);
            return true;
        }
        return false;
    }
    
    boolean goNext2() {
        if (focal < tableControl.getItemCount() - 1) {
            goTo(focal++);
            return true;
        }
        return false;
    }
    
    boolean goFirst() {
        goTo(0);
        return true;
    }
    
    boolean goLast() {
        goTo(tableControl.getItemCount() - 1);
        return true;
    }
}
