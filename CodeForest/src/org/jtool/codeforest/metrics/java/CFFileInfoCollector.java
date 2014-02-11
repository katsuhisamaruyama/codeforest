/*
 *  Copyright 2014, Katsuhisa Maruyama (maru@jtool.org)
 */

package org.jtool.codeforest.metrics.java;

import org.jtool.eclipse.model.java.JavaASTVisitor;
import org.jtool.eclipse.model.java.JavaPackage;
import org.jtool.eclipse.model.java.JavaClass;
import org.jtool.eclipse.model.java.JavaMethod;
import org.jtool.eclipse.model.java.JavaField;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.eclipse.jdt.core.dom.AnonymousClassDeclaration;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Initializer;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.EnumConstantDeclaration;
import java.util.List;
import java.util.Stack;

/**
 * Visits a Java program and stores its information.
 * @author Katsuhisa Maruyama
 */
public class CFFileInfoCollector extends JavaASTVisitor {
    
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
     */
    public CFFileInfoCollector() {
        super();
    }
    
    /**
     * Closes this visitor.
     */
    public void close() {
        super.close();
        
        classStack.clear();
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
        jclass.clearASTNode();
        
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
        jclass.clearASTNode();
        
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
        jclass.clearASTNode();
        
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
        JavaMethod jmethod = new CFJavaMethod(node, jclass);
        jmethod.clearASTNode();
        
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
        JavaMethod jmethod = new CFJavaMethod(node, jclass);
        jmethod.clearASTNode();
        
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
            JavaField jfield = new CFJavaField(fragment, jclass);
            jfield.clearASTNode();
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
        JavaField jfield = new CFJavaField(node, jclass);
        jfield.clearASTNode();
        
        return false;
    }
}
