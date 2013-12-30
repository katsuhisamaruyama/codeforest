/*
 *  Copyright 2013, Katsuhisa Maruyama (maru@jtool.org)
 */

package org.jtool.codeforest.java.internal;

import org.jtool.codeforest.java.JavaLocal;
import org.jtool.codeforest.java.JavaMethod;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.VariableDeclaration;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import java.util.Set;
import java.util.HashSet;

/**
 * Visits a Java program and stores information on local declarations appearing in a field.
 * 
 * SingleVariableDeclaration node
 * VariableDeclarationFragment
 * 
 * @see org.eclipse.jdt.core.dom.Expression
 * @author Katsuhisa Maruyama
 */
public class LocalDeclarationCollector extends ASTVisitor {
    
    /**
     * A method declaring local variables to be collected.
     */
    private JavaMethod declaringMethod;
    
    /**
     * The collection of local variables.
     */
    private Set<JavaLocal> localDeclarations = new HashSet<JavaLocal>();
    
    /**
     * A flag that indicates all bindings for local variables were found.
     */
    private boolean bindingOk = true;
    
    /**
     * Creates a new object for collecting local variables declared in this method.
     * @param the method declaring local variables.
     */
    public LocalDeclarationCollector(JavaMethod jm) {
        super();
        declaringMethod = jm;
    }
    
    /**
     * Returns all the local variables declared in this method.
     * @return the collection of the local variables
     */
    public Set<JavaLocal> getLocalDeclarations() {
        return localDeclarations;
    }
    
    /**
     * Tests if all local variable bindings were found.
     * @return <code>true</code> if all the local variable bindings were found
     */
    public boolean isBindingOk() {
        return bindingOk;
    }
    
    /**
     * Visits a single variable declaration node and stores its information.
     * @param node the single variable declaration node
     * @return <code>true</code> if this visit is continued inside, otherwise <code>false</code>
     */
    public boolean visit(SingleVariableDeclaration node) {
        visitVariableDeclaration(node);
        
        return false;
    }
    
    /**
     * Visits a variable declaration fragment node and stores its information.
     * @param node the variable declaration fragment node
     * @return <code>true</code> if this visit is continued inside, otherwise <code>false</code>
     */
    public boolean visit(VariableDeclarationFragment node) {
        visitVariableDeclaration(node);
        
        return false;
    }
    
    /**
     * Visits a variable declaration fragment node and stores its information.
     * @param node the variable declaration node
     */
    private void visitVariableDeclaration(VariableDeclaration node) {
        if (isLocal(node.getName())) {
            JavaLocal jlocal = new JavaLocal(node, declaringMethod);
            localDeclarations.add(jlocal);
        }
    }
    
    /**
     * Tests if a given name represents a field.
     * @param node an AST node for the name
     * @return <code>true</code> if the name represents a field, otherwise <code>false</code>
     */
    public boolean isLocal(Name node) {
        IBinding binding = node.resolveBinding();
        if (binding != null) {
            if (binding.getKind() == IBinding.VARIABLE) {
                IVariableBinding vbinding = (IVariableBinding)binding;
                return vbinding != null && !vbinding.isField() && !vbinding.isEnumConstant();
            }
            
        } else {
            bindingOk = false;
        }
        return false;
    }
}
