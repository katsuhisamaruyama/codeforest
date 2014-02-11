/*
 *  Copyright 2014, Katsuhisa Maruyama (maru@jtool.org)
 */

package org.jtool.codeforest.ui.view.history;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;

/**
 * Creates buttons that control the history table.
 * @author Katsuhisa Maruyama
 */
public class ButtonControl {
    
    private HistoryView view;
    
    private Composite buttons;
    
    private Button backwardButton1, backwardButton2;
    
    private Button forwardButton1, forwardButton2;
    
    private SelectionListener selectionListener;
    
    ButtonControl(HistoryView view, Composite parent, Control bottom, int margin) {
        this.view = view;
        createButtons(parent, bottom, margin);
    }
    
    private void createButtons(Composite parent, Control bottom, final int MARGIN) {
        buttons = new Composite(parent, SWT.NONE);
        
        backwardButton2 = new Button(buttons, SWT.FLAT);
        backwardButton1 = new Button(buttons, SWT.FLAT);
        forwardButton1 = new Button(buttons, SWT.FLAT);
        forwardButton2 = new Button(buttons, SWT.FLAT);
        
        backwardButton2.setText("<<");
        backwardButton1.setText("<");
        forwardButton1.setText(">");
        forwardButton2.setText(">>");
        
        selectionListener = new ButtonSelectionListener();
        backwardButton1.addSelectionListener(selectionListener);
        backwardButton2.addSelectionListener(selectionListener);
        forwardButton1.addSelectionListener(selectionListener);
        forwardButton2.addSelectionListener(selectionListener);
        
        GridLayout btlayout = new GridLayout();
        btlayout.numColumns = 4;
        btlayout.makeColumnsEqualWidth = true;
        btlayout.marginWidth = 0;
        btlayout.marginHeight = 0;
        btlayout.horizontalSpacing = MARGIN;
        btlayout.marginHeight = MARGIN;
        btlayout.marginWidth = MARGIN;
        btlayout.marginLeft = MARGIN;
        btlayout.marginRight = MARGIN;
        btlayout.marginTop = MARGIN;
        btlayout.marginBottom = MARGIN;
        buttons.setLayout(btlayout);
        
        backwardButton1.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL));
        backwardButton2.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL));
        forwardButton1.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL));
        forwardButton2.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL));
        
        FormData btdata = new FormData();
        if (bottom != null) {
            btdata.bottom = new FormAttachment(bottom, -MARGIN);
        } else {
            btdata.bottom = new FormAttachment(100, -MARGIN);
        }
        btdata.left = new FormAttachment(0, MARGIN);
        btdata.right = new FormAttachment(100, -MARGIN);
        buttons.setLayoutData(btdata);
    }
    
    void dispose() {
        if (!backwardButton1.isDisposed()) {
            backwardButton1.removeSelectionListener(selectionListener);
        }
        if (!backwardButton2.isDisposed()) {
            backwardButton2.removeSelectionListener(selectionListener);
        }
        
        if (!forwardButton1.isDisposed()) {
            forwardButton1.removeSelectionListener(selectionListener);
        }
        if (!forwardButton2.isDisposed()) {
            forwardButton2.removeSelectionListener(selectionListener);
        }
        
        buttons.dispose();
    }
    
    Composite getButtons() {
        return buttons;
    }
    
    private class ButtonSelectionListener implements SelectionListener {
        
        ButtonSelectionListener() {
        }
        
        @Override
        public void widgetDefaultSelected(SelectionEvent evt) {
        }
        
        @Override
        public void widgetSelected(SelectionEvent evt) {
            Object source = evt.getSource();
            if (source == backwardButton1) {
                view.goPrev();
                
            } else if (source == backwardButton2) {
                view.goPrev2();
                
            } else if (source == forwardButton1) {
                view.goNext();
                
            } else if (source == forwardButton2) {
                view.goNext2();
            }
        }
    }
}
