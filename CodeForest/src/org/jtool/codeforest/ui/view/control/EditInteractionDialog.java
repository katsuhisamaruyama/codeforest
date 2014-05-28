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
    
    /**
     * The text area in which a user edits the description of the interaction record.
     */
    private Text text;
    
    /**
     * The description of the interaction record.
     */
    private String description;
    
    /**
     * The ok button of this dialog.
     */
    private Button okButton;
    
    /**
     * Creates an interaction edit dialog.
     * @param shell the shell
     * @param frame the main frame
     */
    public EditInteractionDialog(Shell shell) {
        super(shell);
    }
    
    /**
     * Configures a given shell.
     * @param shell the shell
     */
    protected void configureShell(Shell shell) {
        super.configureShell(shell);
        shell.setText("Edit Interaction");
    }
    
    /**
     * Returns the initial size of this dialog.
     * @return the initial size of the dialog
     */
    protected Point getInitialSize() {
        return new Point(330, 135);
    }
    
    /**
     *  Creates the area for this dialog.
     *  @param parent the parent of this dialog
     */
    protected Control createDialogArea(Composite parent) {
        Composite container = (Composite)super.createDialogArea(parent);
        
        Label label = new Label(container, SWT.NONE);
        label.setText("Edit Description");
        
        text = new Text(container, SWT.SINGLE | SWT.BORDER);
        text.setLayoutData(new GridData(GridData.FILL_BOTH));
        text.setTextLimit(300);
        text.addModifyListener(new ModifyListener() {
            
            /**
             * Invoked when the text is modified.
             * @param e an event containing information about the modify
             */
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
    
    /**
     * Adds buttons to this dialog's button bar.
     * @param the parent of this dialog
     */
    protected void createButtonsForButtonBar(Composite parent) {
        createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, true);
        okButton = createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
        okButton.setEnabled(false); 
    }
    
    /**
     * Invoked when the ok button is pressed.
     */
    protected void okPressed() {
        super.okPressed();
    }
    
    /**
     * Invoked when the cancel button is pressed.
     */
    protected void cancelPressed() {
        description = null;
        super.cancelPressed();
    }
    
    /**
     * Returns the description of an interaction record.
     * @return the description of the interaction record
     */
    String getDescription() {
        return description;
    }
}
