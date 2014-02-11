/*
 *  Copyright 2014, Katsuhisa Maruyama (maru@jtool.org)
 */

package org.jtool.codeforest.ui.view.memo;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;

/**
 * Creates a view that displays the history of user interactions.
 * @author Katsuhisa Maruyama
 */
public class MemoView {
    
    private Table table;
    
    public MemoView(Composite parent) {
        createPane(parent);
    }
    
    private void createPane(Composite parent) {
        parent.setLayoutData(new GridData(GridData.FILL_BOTH));
        GridLayout layout = new GridLayout(2, false);
        parent.setLayout(layout);
        
        table = new Table(parent, SWT.BORDER | SWT.SINGLE | SWT.CHECK | SWT.VIRTUAL | SWT.H_SCROLL | SWT.V_SCROLL);
        table.setLinesVisible(false);
        table.setHeaderVisible(false);
        
        table.setLayoutData(new GridData(GridData.FILL_BOTH));
        
        Composite buttons = new Composite(parent, SWT.NONE);
        buttons.setLayoutData(new GridData(GridData.FILL_VERTICAL));
        buttons.setLayout(new GridLayout(1, false));
        
        /*
        Button addButton = new Button(buttons, SWT.FLAT);
        addButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        addButton.setText("Add");
        addButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                AddEventListenerDialog dialog = new AddEventListenerDialog(shell);
                
                if (dialog.open() == Window.OK) {
                    String fqn = dialog.getSelectedFqn();
                    String[] handlers = dialog.getSelectedHandlers();
                    EventListenerStore.addEventListener(fqn, handlers, false);
                    refresh();
                    
                    EventListenerListItem item = EventListenerStore.getEventListenerItem(fqn);
                    tableViewer.setChecked(item, true);
                }
            }
        });
        
        Button removeButton = new Button(buttons, SWT.FLAT);
        removeButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        removeButton.setText("Remove");
        removeButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                RemoveEventListenerDialog dialog = new RemoveEventListenerDialog(shell);
                if (dialog.open() == Window.OK) {
                    String fqn = dialog.getSelectedFqn();
                    EventListenerStore.removeEventListener(fqn);
                    refresh();
                }
            }
        });
        
        Button defaultButton = new Button(buttons, SWT.FLAT);
        defaultButton.setText(" Restore Defaults  ");
        defaultButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent event) {
                EventListenerStore.doLoadDefault();
                refresh();
                updateSelection();
            }
        });
        
        tableViewer.setInput(EventListenerStore.getEventListeners());
        updateSelection();
        
        */
    }
    
    public void dispose() {
        table.dispose();
    }
}
