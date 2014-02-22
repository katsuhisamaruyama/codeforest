/*
 *  Copyright 2014, Katsuhisa Maruyama (maru@jtool.org)
 */

package org.jtool.codeforest.ui.view.control;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.ModifyEvent;

/**
 * Attaches a memo to a forest tree.
 * @author Katsuhisa Maruyama
 */
class EditInteractionDialog extends Dialog {
    
    private Text text;
    
    private String description;
    
    private Button okButton;
    
    public EditInteractionDialog(Shell shell) {
        super(shell);
    }
    
    protected void configureShell(Shell shell) {
        super.configureShell(shell);
        shell.setText("Edit Interaction");
    }
    
    protected Point getInitialSize() {
        return new Point(330, 135);
    }
    
    protected Control createDialogArea(Composite parent) {
        Composite container = (Composite)super.createDialogArea(parent);
        
        Label label = new Label(container, SWT.NONE);
        label.setText("Edit Description");
        
        text = new Text(container, SWT.SINGLE | SWT.BORDER);
        text.setLayoutData(new GridData(GridData.FILL_BOTH));
        text.setTextLimit(300);
        text.addModifyListener(new ModifyListener() {
            
            public void modifyText(ModifyEvent e) {
                description = text.getText();
                if (description == null || description.length() == 0) {
                    okButton.setEnabled(false);
                } else {
                    okButton.setEnabled(true);
                }
            }
        });
        
        return container;
    }
    
    protected void createButtonsForButtonBar(Composite parent) {
        createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, true);
        okButton = createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
        okButton.setEnabled(false); 
    }
    
    protected void okPressed() {
        super.okPressed();
    }
    
    protected void cancelPressed() {
        description = null;
        super.cancelPressed();
    }
    
    String getDescription() {
        return description;
    }
}
