/*
 *  Copyright 2013, Katsuhisa Maruyama (maru@jtool.org)
 */

package org.jtool.codeforest.java.internal;

import org.jtool.codeforest.java.JavaMethod;
import org.jtool.codeforest.java.JavaMethodInvocation;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.ConstructorInvocation;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.SuperConstructorInvocation;
import org.eclipse.jdt.core.dom.SuperMethodInvocation;
import java.util.Set;
import java.util.HashSet;

/**
 * Visits a Java program and stores information on method invocation appearing in a method or field.
 * 
 * MethodInvocation
 * SuperMethodInvocation
 * ConstructorInvocation
 * SuperConstructorInvocation
 * ClassInstanceCreation
 * 
 * @see org.eclipse.jdt.core.dom.Expression
 * @author Katsuhisa Maruyama
 */
public class MethodInvocationCollector extends ASTVisitor {
    
    /**
     * The collection of method invocations.
     */
    private Set<JavaMethod> methodInvocations = new HashSet<JavaMethod>();
    
    /**
     * The method containing this method invocation.
     */
    protected JavaMethod declaringMethod;
    
    /**
     * A flag that indicates all bindings for methods were found.
     */
    private boolean bindingOk = true;
    
    /**
     * Creates a new object for collecting methods called by this method.
     * @param jm the method containing this invocation
     */
    public MethodInvocationCollector(JavaMethod jm) {
        super();
        declaringMethod = jm;
    }
    
    /**
     * Returns all the methods that this method calls.
     * @return the collection of the methods
     */
    public Set<JavaMethod> getMethodInvocations() {
        return methodInvocations;
    }
    
    /**
     * Tests if all method bindings were found.
     * @return <code>true</code> if all the method bindings were found
     */
    public boolean isBindingOk() {
        return bindingOk;
    }
    
    /**
     * Visits a method invocation node and stores its information.
     * @param node the method invocation node
     * @return <code>true</code> if this visit is continued inside, otherwise <code>false</code>
     */
    public boolean visit(MethodInvocation node) {
        IMethodBinding binding = node.resolveMethodBinding();
        if (binding != null) {
            JavaMethodInvocation jinv = new JavaMethodInvocation(node, binding, declaringMethod);
            addJavaMethodInvocation(jinv, node.resolveMethodBinding());
        }
        return false;
    }
    
    /**
     * Visits a super-method invocation node and stores its information.
     * @param node the super-method invocation node
     * @return <code>true</code> if this visit is continued inside, otherwise <code>false</code>
     */
    public boolean visit(SuperMethodInvocation node) {
        IMethodBinding binding = node.resolveMethodBinding();
        if (binding != null) {
            JavaMethodInvocation jinv = new JavaMethodInvocation(node, binding, declaringMethod);
            addJavaMethodInvocation(jinv, node.resolveMethodBinding());
        }
        return false;
    }
    
    /**
     * Visits a constructor invocation node and stores its information.
     * @param node the constructor invocation node
     * @return <code>true</code> if this visit is continued inside, otherwise <code>false</code>
     */
    public boolean visit(ConstructorInvocation node) {
        IMethodBinding binding = node.resolveConstructorBinding();
        if (binding != null) {
            JavaMethodInvocation jinv = new JavaMethodInvocation(node, binding, declaringMethod);
            addJavaMethodInvocation(jinv, node.resolveConstructorBinding());
        }
        return false;
    }
    
    /**
     * Visits a super-constructor invocation node and stores its information.
     * @param node the super-constructor invocation node
     * @return <code>true</code> if this visit is continued inside, otherwise <code>false</code>
     */
    public boolean visit(SuperConstructorInvocation node) {
        IMethodBinding binding = node.resolveConstructorBinding();
        if (binding != null) {
            JavaMethodInvocation jinv = new JavaMethodInvocation(node, binding, declaringMethod);
            addJavaMethodInvocation(jinv, node.resolveConstructorBinding());
        }
        return false;
    }
    
    /**
     * Visits a method invocation node and stores its information.
     * @param node the method invocation node
     * @return <code>true</code> if this visit is continued inside, otherwise <code>false</code>
     */
    public boolean visit(ClassInstanceCreation node) {
        IMethodBinding binding = node.resolveConstructorBinding();
        if (binding != null) {
            JavaMethodInvocation jinv = new JavaMethodInvocation(node, binding, declaringMethod);
            addJavaMethodInvocation(jinv, node.resolveConstructorBinding());
        }
        return false;
    }
    
    /**
     * Collects the method invocation information.
     * @param jinv the method invocation
     * @param the method binding for this method invocation
     */
    private void addJavaMethodInvocation(JavaMethodInvocation jinv, IMethodBinding binding) {
        JavaMethod jm = jinv.getJavaMethod();
        if (jm != null && !methodInvocations.contains(jm)) {
            methodInvocations.add(jm);
        }
        
        if (binding == null) {
            bindingOk = false;
        }
    }
}
