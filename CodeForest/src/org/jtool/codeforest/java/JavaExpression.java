/*
 *  Copyright 2013, Katsuhisa Maruyama (maru@jtool.org)
 */

package org.jtool.codeforest.java;

import org.eclipse.jdt.core.dom.ASTNode;

/**
 * An object representing a statement. This is candidate for CFG node.
 * @author Katsuhisa Maruyama
 */
public class JavaExpression extends JavaElement {
    
    /**
     * Creates a new, empty object.
     */
    protected JavaExpression() {
        super();
    }
    
    /**
     * Creates a new object representing an expression.
     * @param node an AST node for this expression
     */
    public JavaExpression(ASTNode node) {
        super(node);
    }
    
    /**
     * Tests if a given expression equals to this.
     * @param je the Java expression
     * @return <code>true</code> if the given expression equals to this, otherwise <code>false</code>
     */
    public boolean equals(JavaExpression je) {
        if (je == null) {
            return false;
        }
        
        return this == je;
    }
    
    /**
     * Returns a hash code value for this expression.
     * @return the hash code value for the expression
     */
    public int hashCode() {
        return hashCode();
    }
    
    /**
     * Collects information about this expression.
     * @return the string for printing
     */
    public String toString() {
        if (astNode != null) {
            return astNode.getClass().getName();
        }
        return "JavaExpression";
    }
}
