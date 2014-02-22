/*
 *  Copyright 2014, Katsuhisa Maruyama (maru@jtool.org)
 */
 
package org.jtool.codeforest.handlers;

import org.jtool.eclipse.model.java.JavaModelFactory;
import org.jtool.eclipse.model.java.JavaProject;
import org.jtool.codeforest.metrics.java.CFFileInfoCollector;
import org.jtool.codeforest.metrics.java.MetricsManager;
import org.jtool.codeforest.metrics.java.ProjectMetrics;
import org.jtool.codeforest.ui.CodeForestFrame;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchSite;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

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
     * An active menu selection.
     */
    protected ISelection selection;
    
    /**
     * The a factory object that creates models of Java programs.
     */
    private JavaModelFactory factory;
    
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
        
        selection = HandlerUtil.getActiveMenuSelection(event);
        
        if (selection instanceof IStructuredSelection) {
            IStructuredSelection structured = (IStructuredSelection)selection;
            
            IJavaProject project = null;
            Object elem = structured.getFirstElement();
            if (elem instanceof IJavaProject) {
                project = (IJavaProject)elem;
            } else if (elem instanceof IProject) {
                project = (IJavaProject)JavaCore.create((IProject)elem);
            }
            
            if (project != null) {
                factory = new JavaModelFactory(project);
                factory.setJavaASTVisitor(new CFFileInfoCollector());
                JavaProject jproject = factory.create();
                
                MetricsManager manager = new MetricsManager();
                ProjectMetrics mproject = manager.create(jproject);
                manager.writeXML(mproject);
                
                CodeForestFrame frame = new CodeForestFrame(window.getShell(), mproject);
                frame.dispose();
                
                System.out.println("Code Forest fin.");
            }
        }
        return null;
    }
    
    /**
     * Returns the shell in which the workbench of this editor site resides.
     * @param part the workbench part
     * @return the corresponding shell
     */
    protected Shell getShell(IWorkbenchPart part) {
        IWorkbenchSite ws = part.getSite();
        return ws.getShell();
    }
}
