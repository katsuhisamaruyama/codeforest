/*
 *  Copyright 2013, Katsuhisa Maruyama (maru@jtool.org)
 */

package org.jtool.codeforest.java.internal;

import org.jtool.codeforest.java.JavaField;
import org.jtool.codeforest.java.JavaVariableAccess;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.IVariableBinding;
import java.util.Set;
import java.util.HashSet;

/**
 * Visits a Java program and stores information on field initializers appearing in a field.
 * 
 * VariableDeclarationFragment
 * 
 * @see org.eclipse.jdt.core.dom.Expression
 * @author Katsuhisa Maruyama
 */
public class FieldInitializerCollector extends ASTVisitor {
    
    /**
     * The collection of fields accessed by this field.
     */
    private Set<JavaField> accessedFields = new HashSet<JavaField>();
    
    /**
     * A flag that indicates all bindings for fields were found.
     */
    private boolean bindingOk = true;
    
    /**
     * Creates a new object for collecting variables accessed by this field.
     */
    public FieldInitializerCollector() {
        super();
    }
    
    /**
     * Returns all the fields accessed by this field.
     * @return the collection of the accessed fields
     */
    public Set<JavaField> getAccessedFields() {
        return accessedFields;
    }
    
    /**
     * Tests if all field bindings were found.
     * @return <code>true</code> if all the field bindings were found
     */
    public boolean isBindingOk() {
        return bindingOk;
    }
    
    /**
     * Visits a variable declaration fragment node and stores its information.
     * @param node the variable declaration fragment node
     * @return <code>true</code> if this visit is continued inside, otherwise <code>false</code>
     */
    public boolean visit(VariableDeclarationFragment node) {
        IVariableBinding binding = node.resolveBinding();
        if (binding != null && binding.isField()) {
            Expression initializer = node.getInitializer();
            if (initializer != null) {
                initializer.accept(this);
            }
        }
        
        return false;
    }
    
    /**
     * Visits a name node and stores its information.
     * @param node the name node representing the variable access
     * @return <code>true</code> if this visit is continued inside, otherwise <code>false</code>
     */
    public boolean visit(SimpleName node) {
        IBinding binding = node.resolveBinding();
        if (binding != null && binding.getKind() == IBinding.VARIABLE) {
            JavaVariableAccess jacc = new JavaVariableAccess(node, null);
            if (jacc.isField()) {
                JavaField jf = jacc.getJavaField();
                if (jf != null) {
                    if (!accessedFields.contains(jf)) {
                        accessedFields.add(jf);
                    }
                } else {
                    bindingOk = false;
                }
            }
        }
        
        return false;
    }
}
