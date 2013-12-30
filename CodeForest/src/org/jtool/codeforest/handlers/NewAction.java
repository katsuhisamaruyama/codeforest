/*
 *  Copyright 2013, Katsuhisa Maruyama (maru@jtool.org)
 */

package org.jtool.codeforest.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;
import org.jtool.codeforest.metrics.java.MetricsManager;
import org.jtool.codeforest.metrics.java.ProjectMetrics;
import org.jtool.codeforest.metrics.java.XMLExporter;
import org.jtool.codeforest.ui.CodeForestFrame;

/**
 * Performs an action for a project.
 * @author Katsuhisa Maruyama
 */
public class NewAction extends AbstractHandler {
    
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
    public NewAction() {
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
        String path = "/Users/maru/Desktop/eclipse-4.3.1-CodeForest/runtime-EclipseApplication/sample";
        MetricsManager manager = new MetricsManager();
        ProjectMetrics mproject = manager.readXML(path);
        
        if (mproject == null) {
        	System.out.println("Not found: " + path);
        	return null;
        }
        
        System.out.println("PROJECT NAME = " + mproject.getName());
        
        CodeForestFrame frame = new CodeForestFrame(window.getShell(), mproject);
        
        org.jtool.codeforest.metrics.java.XMLExporter exporter = new XMLExporter();
        exporter.export(frame.getProjectMetrics());
        // System.out.println(exporter.getContents());
        
        frame.createPane();
        
        frame.dispose();
        System.out.println("Code Forest fin.");
        
        return null;
    }
}
