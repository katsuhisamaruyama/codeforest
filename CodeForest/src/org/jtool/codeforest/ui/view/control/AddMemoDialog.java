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
class AddMemoDialog extends Dialog {
    
    /**
     * The text area in which a user writes comments.
     */
    private Text text;
    
    /**
     * The comments written by a user.
     */
    private String comments;
    
    /**
     * The ok button of this dialog.
     */
    private Button okButton;
    
    /**
     * The name of the class which a memo is attached to.
     */
    private String className;
    
    /**
     * Creates a memo dialog.
     * @param shell the shell
     * @param frame the main frame
     */
    public AddMemoDialog(Shell shell, String className) {
        super(shell);
        this.className = className;
    }
    
    /**
     * Configures a given shell.
     * @param shell the shell
     */
    protected void configureShell(Shell shell) {
        super.configureShell(shell);
        shell.setText("Add Memo");
    }
    
    /**
     * Returns the initial size of this dialog.
     * @return the initial size of the dialog
     */
    protected Point getInitialSize() {
        return new Point(330, 200);
    }
    
    /**
     *  Creates the area for this dialog.
     *  @param parent the parent of this dialog
     */
    protected Control createDialogArea(Composite parent) {
        Composite container = (Composite)super.createDialogArea(parent);
        
        Label label = new Label(container, SWT.NONE);
        label.setText("Comments for " + className + ":");
        
        text = new Text(container, SWT.MULTI | SWT.BORDER | SWT.WRAP);
        text.setLayoutData(new GridData(GridData.FILL_BOTH));
        text.setTextLimit(300);
        text.addModifyListener(new ModifyListener() {
            
            /**
             * Invoked when the text is modified.
             * @param e an event containing information about the modify
             */
            public void modifyText(ModifyEvent e) {
                comments = text.getText();
                if (comments == null || comments.length() == 0) {
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
        comments = null;
        super.cancelPressed();
    }
    
    /**
     * Returns the comments written by a user
     * @return the comments
     */
    String getComments() {
        return comments;
    }
}
