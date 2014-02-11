/*
 *  Copyright 2014, Katsuhisa Maruyama (maru@jtool.org)
 */

package org.jtool.codeforest.handlers;

import org.jtool.codeforest.Activator;
import org.jtool.codeforest.metrics.java.MetricsManager;
import org.jtool.codeforest.metrics.java.ProjectMetrics;
import org.jtool.codeforest.ui.CodeForestFrame;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import java.io.File;

/**
 * Performs an action for a project.
 * @author Katsuhisa Maruyama
 */
public class OpenAction extends AbstractHandler {
    
    /**
     * A workbench window.
     */
    protected IWorkbenchWindow window;
    
    /**
     * A workbench part.
     */
    protected IWorkbenchPart part;
    
    /**
     * Creates a new, empty object.
     */
    public OpenAction() {
    }
    
    /**
     * Executes a command with information obtained from the application context.
     * @param event an event containing all the information about the current state of the application
     * @return the result of the execution.
     * @throws ExecutionException if an exception occurred during execution
     */
    public Object execute(ExecutionEvent event) throws ExecutionException {
        part = HandlerUtil.getActivePart(event);
        window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
        
        // String path = "/Users/maru/Desktop/eclipse-4.3.1-CodeForest/runtime-EclipseApplication/CodeForest";
        // String path = "/Users/maru/Desktop/eclipse-4.3.1-CodeForest/runtime-EclipseApplication/org.jtool.eclipse";
        // String path = "/Users/maru/Desktop/eclipse-4.3.1-CodeForest/runtime-EclipseApplication/sample";
        String path = open();
        
        MetricsManager manager = new MetricsManager();
        ProjectMetrics mproject = manager.readXML(path);
        
        if (mproject == null) {
            System.out.println("Cannot read: " + path);
            return null;
        }
        
        CodeForestFrame frame = new CodeForestFrame(window.getShell(), mproject);
        frame.createPane();
        frame.dispose();
        
        System.out.println("Code Forest fin.");
        
        return null;
    }
    
    public String open() {
        FileDialog dialog = new FileDialog(window.getShell(), SWT.NULL);
        String path = dialog.open();
        
        if (path != null) {
            File file = new File(path);
            while (!file.isFile()) {
                MessageDialog.openError(null, Activator.PLUGIN_ID, "Please specify a file.");
                dialog.setFilterPath(path);
                path = dialog.open();
                if (path == null) {
                    return null;
                }
                file = new File(path);
            }
        }
        
        return path;
    }
}
