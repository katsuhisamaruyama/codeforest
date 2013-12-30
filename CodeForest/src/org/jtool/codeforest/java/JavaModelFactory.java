/*
 *  Copyright 2013, Katsuhisa Maruyama (maru@jtool.org)
 */

package org.jtool.codeforest.java;

import org.jtool.codeforest.Activator;
import org.jtool.codeforest.java.internal.JavaParser;
import org.jtool.codeforest.java.internal.JavaModelVisitor;
import org.jtool.codeforest.io.DetectCharset;
import org.jtool.codeforest.io.FileReader;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.ui.IWorkbenchWindow;
import java.util.Set;
import java.util.HashSet;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * Creates Java Model within the project.
 * @author Katsuhisa Maruyama
 */
public class JavaModelFactory {
    
    /**
     * A project in the workspace.
     */
    private IJavaProject project;
    
    /**
     * An object that stores information on project, which provides access all the information resulting from the analysis.
     */
    private JavaProject jproject;
    
    /**
     * Creates a factory object that creates models of Java programs.
     * @param project the project in the workspace
     */
    public JavaModelFactory(IJavaProject project) {
        super();
        
        JavaClass.clearAll();
        this.project = project;
        jproject = JavaProject.create(project);
    }
    
    /**
     * Creates models for Java programs within the project.
     * @return the created project information
     */
    public JavaProject create() {
        long start = System.currentTimeMillis();
        
        Set<ICompilationUnit> cunits = collectAllCompilationUnits();
        
        createJavaModel(cunits);
        JavaElement.setBindingLevel(1);
        
        collectLevel2Info();
        collectLevel3Info();
        
        long end = System.currentTimeMillis();
        
        long elapsedTime = end - start;
        double minutes = elapsedTime / (60 * 1000);
        double seconds = elapsedTime / 1000;
        
        System.out.println("execution time: " + minutes + "m / " + seconds + "s / " + elapsedTime + "ms");
        
        return jproject;
    }
    
    /**
     * Collects all Java files within a given project.
     * @return the collection of the files
     */
    private Set<ICompilationUnit> collectAllCompilationUnits() {
        Set<ICompilationUnit> files = new HashSet<ICompilationUnit>();
        try {
            IPackageFragment[] packages = project.getPackageFragments();
            for (int i = 0; i < packages.length; i++) {
                ICompilationUnit[] units = packages[i].getCompilationUnits();
                   
                for (int j = 0; j < units.length; j++) {
                    IResource res = units[j].getResource();
                    if (res.getType() == IResource.FILE) {
                        
                        String pathname = units[j].getPath().toString();
                        if (pathname.endsWith(".java")) { 
                            files.add(units[j]);
                        }
                    }
                }
            }
        } catch (JavaModelException e) {
            System.err.println("JavaModelException: " + e.getMessage());
        }
        
        return files;
    }
    
    /**
     * Creates a model from Java programs.
     * @param junits the collection of compilation unit that requires parsing
     */
    private void createJavaModel(final Set<ICompilationUnit> cunits) {
        try {
            IWorkbenchWindow workbenchWindow = Activator.getWorkbenchWindow();
            workbenchWindow.run(true, true, new IRunnableWithProgress() {
                
                /**
                 * Creates a model by parsing Java files.
                 * @param monitor the progress monitor to use to display progress and receive requests for cancellation
                 * @exception InvocationTargetException if the run method must propagate a checked exception
                 * @exception InterruptedException if the operation detects a request to cancel
                 */
                @Override
                public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
                    monitor.beginTask("Parsing files...: ", cunits.size());
                    
                    int idx = 1;
                    for (ICompilationUnit icu : cunits) {
                        monitor.subTask(idx + "/" + cunits.size() + " - " + icu.getPath().toString());
                        
                        createJavaModel(icu);
                        
                        if (monitor.isCanceled()) {
                            monitor.done();
                            throw new InterruptedException();
                        }
                        monitor.worked(1);
                        idx++;
                    }
                    monitor.done();
                }
                
            });
            
        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause();
            System.out.println("InvocationTargetException because " + cause);
        } catch (InterruptedException e) {
            return;
        }
    }
    
    /**
     * Creates a model from a given compilation unit.
     * @param icu the compilation unit that requires parsing
     * 
     */
    private void createJavaModel(ICompilationUnit icu) {
        JavaParser parser = new JavaParser();
        CompilationUnit cu = (CompilationUnit)parser.parse(icu);
        
        if (cu != null) {
            JavaModelVisitor visitor = new JavaModelVisitor(icu, jproject);
            cu.accept(visitor);
            visitor.close();
        }
    }
    
    /**
     * Creates a model from a Java program stored in a given file.
     * @param file the file that requires parsing
     * @param the project containing the file
     */
    protected void createJavaModel(File file, JavaProject jproject) {
        String contents = null;
        String encoding = null;
        try {
            contents = FileReader.read(file);
            encoding = DetectCharset.getCharsetName(contents.getBytes());
        } catch (FileNotFoundException e) {
            return;
        } catch (IOException e) {
            return;
        }
        
        if (contents != null && encoding != null) {
            createJavaModel(file.getAbsoluteFile().getName(), contents, encoding, jproject);
        }
    }
    
    /**
     * Creates a model from a file.
     * @param path the path name of a file corresponding to the compilation unit to be visited 
     * @param source source the contents of the file
     * @param encoding the name of the character-set encoding for the contents
     * @param jproject the project containing the file
     */
    private void createJavaModel(String path, String source, String encoding, JavaProject jproject) {
        String rootDir = jproject.getTopDir();
        
        JavaParser parser = new JavaParser();
        CompilationUnit cu = parser.standaloneParse(new String[]{ rootDir }, new String[]{ rootDir }, encoding, source, path);
        
        if (cu != null) {
            JavaModelVisitor visitor = new JavaModelVisitor(path, jproject);
            cu.accept(visitor);
        }
    }
    
    /**
     * Collects additional information on classes, methods, and fields within a project.
     */
    private void collectLevel2Info() {
        for (JavaClass jc : jproject.getJavaClasses()) {
            jc.collectLevel2Info();
            
            if (!jc.isBindingOk()) {
                System.err.println("some binding information was missed in a class: " + jc.getQualifiedName());
            }
            
            for (JavaMethod jm : jc.getJavaMethods()) {
                jm.collectLevel2Info();
                
                if (!jm.isBindingOk()) {
                    System.err.println("some binding information was missed in a method: " + jm.getQualifiedName());
                }
            }
            
            for (JavaField jf : jc.getJavaFields()) {
                jf.collectLevel2Info();
                
                if (!jf.isBindingOk()) {
                    System.err.println("some binding information was missed in a field: " + jf.getQualifiedName());
                }
            }
        }
    }
    
    /**
     * Collects additional information on packages.
     */
    private void collectLevel3Info() {
        for (JavaPackage jp : jproject.getJavaPackages()) {
            jp.collectLevel3Info();
            if (!jp.isBindingOk()) {
                System.err.println("some binding information was missed in a package: " + jp.getName());
            }
        }
    }
}
