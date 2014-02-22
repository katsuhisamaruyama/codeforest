/*
 *  Copyright 2014, Katsuhisa Maruyama (maru@jtool.org)
 */

package org.jtool.codeforest.ui.view.control;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.ModifyEvent;

/**
 * Adds a working set of metrics.
 * @author Katsuhisa Maruyama
 */
class AddWorkingSetDialog extends Dialog {
    
    private Text text;
    
    private Text text2;
    
    private String name;
    
    private String description;
    
    private Button okButton;
    
    public AddWorkingSetDialog(Shell shell) {
        super(shell);
    }
    
    protected void configureShell(Shell shell) {
        super.configureShell(shell);
        shell.setText("Add Working Set");
    }
    
    protected Point getInitialSize() {
        return new Point(300, 250);
    }
    
    protected Control createDialogArea(Composite parent) {
        Composite container = (Composite)super.createDialogArea(parent);
        
        Composite base = new Composite(container, SWT.NONE);
        base.setLayoutData(new GridData(GridData.FILL_BOTH));
        GridLayout layout = new GridLayout(1, false);
        layout.marginWidth = 0;
        layout.marginHeight = 0;
        base.setLayout(layout);
        
        Label label = new Label(base, SWT.NONE);
        label.setText("Name:");
        
        text = new Text(base, SWT.SINGLE | SWT.BORDER);
        text.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        text.setTextLimit(20);
        text.addModifyListener(new ModifyListener() {
            
            public void modifyText(ModifyEvent e) {
                name = text.getText();
                if (name == null || name.length() == 0) {
                    okButton.setEnabled(false);
                } else if (WorkingSetStore.getWorkingSet(name) != null) {
                    text.setToolTipText("The same name already exists");
                    okButton.setEnabled(false);
                } else {
                    okButton.setEnabled(true);
                }
            }
        });
        
        Label label2 = new Label(base, SWT.NONE);
        label2.setText("Description (not use \"[\" and \"]\" ):");
        
        text2 = new Text(base, SWT.MULTI | SWT.BORDER | SWT.WRAP);
        text2.setLayoutData(new GridData(GridData.FILL_BOTH));
        text.setTextLimit(200);
        
        return container;
    }
    
    protected void createButtonsForButtonBar(Composite parent) {
        createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, true);
        okButton = createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
        okButton.setEnabled(false); 
    }
    
    protected void okPressed() {
        description = text2.getText();
        description.replace('[', '{');
        description.replace(']', '}');
        super.okPressed();
    }
    
    protected void cancelPressed() {
        name = null;
        super.cancelPressed();
    }
    
    String getName() {
        return name;
    }
    
    String getDescription() {
        return description;
    }
}
