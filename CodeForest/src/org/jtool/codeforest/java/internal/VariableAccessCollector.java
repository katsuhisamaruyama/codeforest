/*
 *  Copyright 2013, Katsuhisa Maruyama (maru@jtool.org)
 */

package org.jtool.codeforest.java.internal;

import org.jtool.codeforest.java.JavaMethod;
import org.jtool.codeforest.java.JavaVariableAccess;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.SimpleName;
import java.util.Set;
import java.util.HashSet;

/**
 * Visits a Java program and stores information on variable access appearing in a method or field.
 * 
 * Name:
 *   SimpleName
 *   QualifiedName
 * 
 * @see org.eclipse.jdt.core.dom.Expression
 * @author Katsuhisa Maruyama
 */
public class VariableAccessCollector extends ASTVisitor {
    
    /**
     * The collection of variables accessed.
     */
    private Set<JavaVariableAccess> accessedVariables = new HashSet<JavaVariableAccess>();
    
    /**
     * The method containing this variable access.
     */
    protected JavaMethod declaringMethod;
    
    /**
     * A flag that indicates all bindings for variables were found.
     */
    private boolean bindingOk = true;
    
    /**
     * Creates a new object for collecting variables accessed by this method or field.
     * @param jm the method containing this invocation
     */
    public VariableAccessCollector(JavaMethod jm) {
        super();
        declaringMethod = jm;
    }
    
    /**
     * Visits a name node and stores its information.
     * @param node the name node representing the variable access
     * @return <code>true</code> if this visit is continued inside, otherwise <code>false</code>
     */
    public boolean visit(Name node) {
        return true;
    }
    
    /**
     * Returns all the accessed variables.
     * @return the collection of the accessed variables
     */
    public Set<JavaVariableAccess> getAccessedVariables() {
        return accessedVariables;
    }
    
    /**
     * Tests if all method bindings were found.
     * @return <code>true</code> if all the method bindings were found
     */
    public boolean isBindingOk() {
        return bindingOk;
    }
    
    /**
     * Visits a name node and stores its information.
     * @param node the name node representing the variable access
     * @return <code>true</code> if this visit is continued inside, otherwise <code>false</code>
     */
    public boolean visit(SimpleName node) { 
        IBinding binding = node.resolveBinding();
        if (binding != null && binding.getKind() == IBinding.VARIABLE) {
            addJavaVariableAccess(node);
        }
        return false;
    }
    
    /**
     * Visits a name node and stores its information.
     * @param node the name node representing the variable access
     * @return <code>true</code> if this visit is continued inside, otherwise <code>false</code>
     */
    public boolean visit(QualifiedName node) {
        IBinding binding = node.resolveBinding();
        if (binding != null && binding.getKind() == IBinding.VARIABLE) {
            addJavaVariableAccess(node);
        }
        return false;
    }
    
    /**
     * Collects the variable access information.
     * @param the variable binding
     */
    private void addJavaVariableAccess(Name node) {
        IBinding binding = node.resolveBinding();
        if (binding != null) {
            JavaVariableAccess jacc = new JavaVariableAccess(node, declaringMethod);
            
            if (jacc != null && !accessedVariables.contains(jacc)) {
                accessedVariables.add(jacc);
            }
        }
        
        if (binding == null) {
            bindingOk = false;
        }
    }
}
