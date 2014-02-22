/*
 *  Copyright 2014, Katsuhisa Maruyama (maru@jtool.org)
 */

package org.jtool.codeforest.ui.view.control;

import org.jtool.codeforest.ui.CodeForestFrame;
import org.jtool.codeforest.ui.view.SettingView;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.jtool.codeforest.Activator;
import org.jtool.codeforest.ui.view.SettingData;

/**
 * Manages working sets of metrics.
 * @author Katsuhisa Maruyama
 */
public class WorkingSetDialog extends TitleAreaDialog {
    
    private CodeForestFrame frame;
    
    private TableViewer tableViewer;
    
    private Text text;
    
    private SettingView settingView;
    
    private SettingData settingData;
    
    private Button cancelButton;
    
    private String selectedName;
    
    public WorkingSetDialog(Shell shell, CodeForestFrame frame) {
        super(shell);
        
        this.frame = frame;
        this.settingView = frame.getSettingView();
        this.settingData = frame.getSettingData();
    }
    
    protected void configureShell(Shell shell) {
        super.configureShell(shell);
        shell.setText("Event Listener List Configuration");
    }
    
    protected Point getInitialSize() {
        return new Point(400, 500);
    }
    
    public void create() {
        setHelpAvailable(false);
        setDialogHelpAvailable(false);
        
        super.create();
        setTitle("Configure settings of working sets");
        setMessage("Add or remove working sets of metrics.");
    }
    
    protected Control createDialogArea(Composite parent) {
        Composite container = (Composite)super.createDialogArea(parent);
        
        Composite base = new Composite(container, SWT.NONE);
        base.setLayoutData(new GridData(GridData.FILL_BOTH));
        GridLayout layout = new GridLayout(2, false);
        base.setLayout(layout);
        
        Composite left = new Composite(base, SWT.NONE);
        left.setLayout(new FormLayout());
        
        tableViewer = new TableViewer(left, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
        tableViewer.setContentProvider(new ArrayContentProvider());
        tableViewer.setLabelProvider(new WorkingSetLabelProvider());
        tableViewer.addSelectionChangedListener(new ISelectionChangedListener() {
            
            public void selectionChanged(SelectionChangedEvent event) {
                IStructuredSelection selection = (IStructuredSelection)tableViewer.getSelection();
                Object obj = selection.getFirstElement();
                if (obj instanceof WorkingSet) {
                    WorkingSet workingSet = (WorkingSet)obj;
                    selectedName = workingSet.getName();
                    text.setText(workingSet.getDescription());
                } else {
                    selectedName = null;
                }
            }
        });
        
        tableViewer.addDoubleClickListener(new IDoubleClickListener() {
            
            public void doubleClick(DoubleClickEvent event) {
                IStructuredSelection selection = (IStructuredSelection)tableViewer.getSelection();
                Object obj = selection.getFirstElement();
                if (obj instanceof WorkingSet) {
                    WorkingSet workingSet = (WorkingSet)obj;
                    settingView.change(workingSet);
                    cancelButton.setEnabled(false);
                    
                    recordWorkingSetAction("change", selectedName);
                }
            }
        });
        
        FormData listdata = new FormData();
        listdata.top = new FormAttachment(0, 0);
        listdata.left = new FormAttachment(0, 0);
        listdata.width = 270;
        listdata.height = 270;
        tableViewer.getTable().setLayoutData(listdata);
        
        text = new Text(left, SWT.READ_ONLY);
        text.setBackground(left.getBackground());
        
        FormData textdata = new FormData();
        textdata.top = new FormAttachment(tableViewer.getTable(), 5);
        textdata.left = new FormAttachment(0, 0);
        textdata.right = new FormAttachment(100, 0);
        textdata.height = 50;
        text.setLayoutData(textdata);
        
        Composite buttons = new Composite(base, SWT.NONE);
        buttons.setLayoutData(new GridData(GridData.FILL_VERTICAL));
        buttons.setLayout(new GridLayout(1, false));
        
        Button changeButton = new Button(buttons, SWT.FLAT);
        changeButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        changeButton.setText("Change View");
        changeButton.addSelectionListener(new SelectionAdapter() {
            
            public void widgetSelected(SelectionEvent e) {
                if (selectedName != null) {
                    settingView.change(WorkingSetStore.getWorkingSet(selectedName));
                    cancelButton.setEnabled(false);
                    
                    recordWorkingSetAction("change", selectedName);
                }
            }
        });
        
        Button addButton = new Button(buttons, SWT.FLAT);
        addButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        addButton.setText("Add");
        addButton.addSelectionListener(new SelectionAdapter() {
            AddWorkingSetDialog dialog = new AddWorkingSetDialog(frame.getShell());
            
            public void widgetSelected(SelectionEvent e) {
                if (dialog.open() == Window.OK) {
                    WorkingSetStore.addWorkingSet(dialog.getName(), dialog.getDescription(), settingData);
                    cancelButton.setEnabled(false);
                    refresh();
                    
                    recordWorkingSetAction("add", dialog.getName());
                }
            }
        });
        
        Button removeButton = new Button(buttons, SWT.FLAT);
        removeButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        removeButton.setText("Remove");
        removeButton.addSelectionListener(new SelectionAdapter() {
            
            public void widgetSelected(SelectionEvent e) {
                if (selectedName != null) {
                    WorkingSetStore.removeWorkingSet(selectedName);
                    cancelButton.setEnabled(false);
                    refresh();
                }
            }
        });
        
        Button clearButton = new Button(buttons, SWT.FLAT);
        clearButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        clearButton.setText("Clear");
        clearButton.addSelectionListener(new SelectionAdapter() {
            
            public void widgetSelected(SelectionEvent e) {
                WorkingSetStore.clearWorkingSets();
                cancelButton.setEnabled(false);
                refresh();
            }
        });
        
        Button defaultButton = new Button(buttons, SWT.FLAT);
        defaultButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        defaultButton.setText("Set as Default");
        defaultButton.addSelectionListener(new SelectionAdapter() {
            
            public void widgetSelected(SelectionEvent e) {
                WorkingSetStore.setDefaultWorkingSet(settingData);
                cancelButton.setEnabled(false);
                refresh();
            }
        });
        
        parent.pack();
        
        refresh();
        return container;
    }
    
    protected void createButtonsForButtonBar(Composite parent) {
        cancelButton = createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, true);
        cancelButton.setEnabled(true);
        createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
    }
    
    void refresh() {
        tableViewer.setInput(WorkingSetStore.getWorkingSets());
        tableViewer.refresh(true, true);
    }
    
    private boolean ok = false;
    
    boolean isOk() {
        return ok;
    }
    
    protected void okPressed() {
        super.okPressed();
    }
    
    private void recordWorkingSetAction(String action, String name) {
        InteractionView interactionView = frame.getInteractionView();
        interactionView.recordWorkingSetAction(settingData, action, name);
    }
    
    private class WorkingSetLabelProvider extends LabelProvider {
        
        public String getText(Object obj) {
            if (obj instanceof WorkingSet) {
                WorkingSet settingData = (WorkingSet)obj;
                return settingData.getName();
            }
            return null;
        }
        
        public Image getImage(Object obj) {
            if (obj instanceof WorkingSet) {
                return Activator.getImage("workingset");
            }
            return null;
        }
    }
}
