/*
 *  Copyright 2013, Katsuhisa Maruyama (maru@jtool.org)
 */

package org.jtool.codeforest.java.internal;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.eclipse.jdt.core.dom.AnonymousClassDeclaration;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Initializer;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.EnumConstantDeclaration;
import org.jtool.codeforest.java.JavaClass;
import org.jtool.codeforest.java.JavaField;
import org.jtool.codeforest.java.JavaFile;
import org.jtool.codeforest.java.JavaMethod;
import org.jtool.codeforest.java.JavaPackage;
import org.jtool.codeforest.java.JavaProject;
import org.jtool.codeforest.java.internal.JavaModelVisitor;
import java.util.List;
import java.util.ArrayList;
import java.util.Stack;

/**
 * Visits a Java program and stores its information.
 * @author Katsuhisa Maruyama
 */
public class JavaModelVisitor extends ASTVisitor {
    
    /**
     * A file corresponding to the compilation unit to be visited
     */
    protected JavaFile jfile;
    
    /**
     * A stack which stores a Java classes.
     */
    private Stack<JavaClass> classStack = new Stack<JavaClass>();
    
    /**
     * An AST node for a package declaration.
     */
    private PackageDeclaration packageNode = null;
    
    /**
     * Creates a new object for visiting a Java program. 
     * @param icu the compilation unit to be visited
     * @param jproject the project containing the file
     */
    public JavaModelVisitor(ICompilationUnit icu, JavaProject jproject) {
        super();
        
        jfile = new JavaFile(icu, jproject);
        jproject.addJavaFile(jfile);
    }
    
    /**
     * Creates a new object for visiting a Java program. 
     * @param path the path name of a file corresponding to the compilation unit to be visited 
     * @param jproject the project containing the file
     */
    public JavaModelVisitor(String path, JavaProject jproject) {
        super();
        
        jfile = new JavaFile(path, jproject);
        jproject.addJavaFile(jfile);
    }
    
    /**
     * Closes this visitor.
     */
    public void close() {
        classStack.clear();
        classStack = null;
        
        jfile = null;
        packageNode = null;
    }
    
    /**
     * Visits a package declaration node (<code>package</code>) and stores its information.
     * @param node the package declaration node
     * @return <code>true</code> if this visit is continued inside, otherwise <code>false</code>
     */
    public boolean visit(PackageDeclaration node) {
        packageNode = node;
        return true;
    }
    
    /**
     * Visits a type declaration node (<code>class</code> or <code>interface</code>) and stores its information.
     * @param node the type declaration node
     * @return <code>true</code> if this visit is continued inside, otherwise <code>false</code>
     */
    public boolean visit(TypeDeclaration node) {
        JavaPackage jpackage = JavaPackage.create(packageNode, jfile.getJavaProject());
        
        JavaClass jclass = JavaClass.create(node, jpackage);
        jclass.setJavaFile(jfile);
        
        if (!classStack.empty()) {
            JavaClass jc = classStack.peek();
            jc.addJavaInnerClass(jclass);
        }
        classStack.push(jclass);
        
        return true;
    }
    
    /**
     * Finishes the visit for a type declaration node.
     */
    public void endVisit(TypeDeclaration node) {
        if (!classStack.empty()) {
            classStack.pop();
        }
    }
    
    /**
     * Visits an enum declaration node (<code>class</code> or <code>interface</code>) and stores its information.
     * @param node the type declaration node
     * @return <code>true</code> if this visit is continued inside, otherwise <code>false</code>
     */
    public boolean visit(EnumDeclaration node) {
        JavaPackage jpackage = JavaPackage.create(packageNode, jfile.getJavaProject());
        
        JavaClass jclass = JavaClass.create(node, jpackage);
        jclass.setJavaFile(jfile);
        
        if (!classStack.empty()) {
            JavaClass jc = classStack.peek();
            jc.addJavaInnerClass(jclass);
        }
        
        classStack.push(jclass);
        
        return true;
    }
    
    /**
     * Finishes the visit for an enum declaration node.
     */
    public void endVisit(EnumDeclaration node) {
        classStack.pop();
    }
    
    /**
     * Visits an anonymous class declaration node and stores its information.
     * @param node the anonymous class declaration node
     * @return <code>true</code> if this visit is continued inside, otherwise <code>false</code>
     */
    public boolean visit(AnonymousClassDeclaration node) {
        JavaPackage jpackage = JavaPackage.create(packageNode, jfile.getJavaProject());
        
        JavaClass jclass = JavaClass.create(node, jpackage);
        jclass.setJavaFile(jfile);
        
        if (!classStack.empty()) {
            JavaClass jc = classStack.peek();
            jc.addJavaInnerClass(jclass);
        }
        
        classStack.push(jclass);
        
        return true;
    }
    
    /**
     * Finishes the visit for an anonymous class declaration node.
     */
    public void endVisit(AnonymousClassDeclaration node) {
        classStack.pop();
    }
    
    /**
     * Visits a method declaration node and stores its information.
     * @param node the method declaration node
     * @return <code>true</code> if this visit is continued inside, otherwise <code>false</code>
     */
    public boolean visit(MethodDeclaration node) {
        if (classStack.empty()) {
            return false;
        }
        
        JavaClass jclass = classStack.peek(); 
        new JavaMethod(node, jclass);
        
        return true;
    }
    
    /**
     * Finishes the visit for a method declaration node.
     */
    public void endVisit(MethodDeclaration node) {
    }
    
    /**
     * Visits an initializer node and stores its information.
     * @param node the initializer node
     * @return <code>true</code> if this visit is continued inside, otherwise <code>false</code>
     */
    public boolean visit(Initializer node) {
        if (classStack.empty()) {
            return false;
        }
        
        JavaClass jclass = classStack.peek();
        new JavaMethod(node, jclass);
        
        return true;
    }
    
    /**
     * Finishes the visit for an initializer node.
     */
    public void endVisit(Initializer node) {
    }
    
    /**
     * Visits a field declaration node and stores its information.
     * @param node the field declaration node
     * @return <code>true</code> if this visit is continued inside, otherwise <code>false</code>
     */
    @SuppressWarnings("unchecked")
    public boolean visit(FieldDeclaration node) {
        if (classStack.empty()) {
            return false;
        }
        
        JavaClass jclass = classStack.peek();
        for (VariableDeclarationFragment fragment : (List<VariableDeclarationFragment>)node.fragments()) {
            new JavaField(fragment, jclass);
        }
        
        return false;
    }
    
    /**
     * Visits an enum constant node and stores its information.
     * @param node the initializer node
     * @return <code>true</code> if this visit is continued inside, otherwise <code>false</code>
     */
    public boolean visit(EnumConstantDeclaration node) {
        if (classStack.empty()) {
            return false;
        }
        
        JavaClass jclass = classStack.peek();
        new JavaField(node, jclass);
        
        return false;
    }
}
