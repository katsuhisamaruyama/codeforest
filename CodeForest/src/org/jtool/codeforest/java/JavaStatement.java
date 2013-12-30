/*
 *  Copyright 2013, Katsuhisa Maruyama (maru@jtool.org)
 */

package org.jtool.codeforest.java;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.AssertStatement;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.BreakStatement;
import org.eclipse.jdt.core.dom.ConstructorInvocation;
import org.eclipse.jdt.core.dom.ContinueStatement;
import org.eclipse.jdt.core.dom.DoStatement;
import org.eclipse.jdt.core.dom.EmptyStatement;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.LabeledStatement;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.SuperConstructorInvocation;
import org.eclipse.jdt.core.dom.SwitchCase;
import org.eclipse.jdt.core.dom.SwitchStatement;
import org.eclipse.jdt.core.dom.SynchronizedStatement;
import org.eclipse.jdt.core.dom.ThrowStatement;
import org.eclipse.jdt.core.dom.TryStatement;
import org.eclipse.jdt.core.dom.TypeDeclarationStatement;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.WhileStatement;

/**
 * An object representing a statement. This is candidate for CFG node.
 * @author Katsuhisa Maruyama
 */
public class JavaStatement extends JavaElement {
    
    /**
     * Creates a new, empty object.
     */
    public JavaStatement() {
        super();
    }
    
    /**
     * Creates a new object representing a statement.
     * @param node an AST node for this statement
     */
    public JavaStatement(ASTNode node) {
        super(node);
    }
    
    
    /**
     * Tests if this object represents a statement.
     * @return always <code>true</code>
     */
    public boolean isJavaStatement() {
        return true;
    }
    
    /**
     * Returns the Java class that declares this statement.
     * @return always <code>null</code> because no method declares this field
     */
    public JavaClass getDeclaringJavaClass() {
        return null;
    }
    
    /**
     * Returns the Java method that declares this field.
     * @return always <code>null</code> because no method declares this field
     */
    public JavaMethod getDeclaringJavaMethod() {
        return null;
    }
    
    /**
     * Tests if this is an assert-statement.
     * @return <true> if this is an assert-statement, otherwise <code>false</code>
     */
    public boolean isAssertStatement() {
        return astNode instanceof AssertStatement;
    }
    
    /**
     * Tests if this is a block.
     * @return <true> if this is a block, otherwise <code>false</code>
     */
    public boolean isBlock() {
        return astNode instanceof Block;
    }
    
    /**
     * Tests if this is a break-statement.
     * @return <true> if this is a break-statement, otherwise <code>false</code>
     */
    public boolean isBreakStatement() {
        return astNode instanceof BreakStatement;
    }
    
    /**
     * Tests if this is a constructor invocation.
     * @return <true> if this is a constructor invocation, otherwise <code>false</code>
     */
    public boolean isConstructorInvocation() {
        return astNode instanceof ConstructorInvocation;
    }
    
    /**
     * Tests if this is a continue-statement.
     * @return <true> if this is a continue-statement, otherwise <code>false</code>
     */
    public boolean isContinueStatement() {
        return astNode instanceof ContinueStatement;
    }
    
    /**
     * Tests if this is a do-statement.
     * @return <true> if this is a do-statement, otherwise <code>false</code>
     */
    public boolean isDoStatement() {
        return astNode instanceof DoStatement;
    }
    
    /**
     * Tests if this is an empty-statement.
     * @return <true> if this is an empty-statement, otherwise <code>false</code>
     */
    public boolean isEmptyStatement() {
        return astNode instanceof EmptyStatement;
    }
    
    /**
     * Tests if this is an empty-statement.
     * @return <true> if this is an empty-statement, otherwise <code>false</code>
     */
    public boolean isnhancedForStatement() {
        return astNode instanceof EnhancedForStatement;
    }
    
    /**
     * Tests if this is an expression-statement.
     * @return <true> if this is an expression-statement, otherwise <code>false</code>
     */
    public boolean isExpressionStatement() {
        return astNode instanceof ExpressionStatement;
    }
    
    /**
     * Tests if this is a for-statement.
     * @return <true> if this is a for-statement, otherwise <code>false</code>
     */
    public boolean isForStatement() {
        return astNode instanceof ForStatement;
    }
    
    /**
     * Tests if this is an if-statement.
     * @return <true> if this is an if-statement, otherwise <code>false</code>
     */
    public boolean isIfStatement() {
        return astNode instanceof IfStatement;
    }
    
    /**
     * Tests if this is a labeled-statement.
     * @return <true> if this is a labeled-statement, otherwise <code>false</code>
     */
    public boolean isLabeledStatement() {
        return astNode instanceof LabeledStatement;
    }
    
    /**
     * Tests if this is a return-statement.
     * @return <true> if this is a return-statement, otherwise <code>false</code>
     */
    public boolean isReturnStatement() {
        return astNode instanceof ReturnStatement;
    }
    
    /**
     * Tests if this is a super-constructor invocation.
     * @return <true> if this is a super-constructor invocation, otherwise <code>false</code>
     */
    public boolean isSuperConstructorInvocation() {
        return astNode instanceof SuperConstructorInvocation;
    }
    
    /**
     * Tests if this is a switch-case.
     * @return <true> if this is a switch-case, otherwise <code>false</code>
     */
    public boolean isSwichCase() {
        return astNode instanceof SwitchCase;
    }
    
    /**
     * Tests if this is a switch-statement.
     * @return <true> if this is a switch-statement, otherwise <code>false</code>
     */
    public boolean isSwitchStatement() {
        return astNode instanceof SwitchStatement;
    }
    
    /**
     * Tests if this is a synchronized-statement.
     * @return <true> if this is a synchronized-statement, otherwise <code>false</code>
     */
    public boolean isSynchronizedStatement() {
        return astNode instanceof SynchronizedStatement;
    }
    
    /**
     * Tests if this is a throw-statement.
     * @return <true> if this is a throw-statement, otherwise <code>false</code>
     */
    public boolean isThrowStatement() {
        return astNode instanceof ThrowStatement;
    }
    
    /**
     * Tests if this is a try-statement.
     * @return <true> if this is a try-statement, otherwise <code>false</code>
     */
    public boolean isTryStatement() {
        return astNode instanceof TryStatement;
    }
    
    /**
     * Tests if this is a type declaration-statement.
     * @return <true> if this is a type declaration-statement, otherwise <code>false</code>
     */
    public boolean isTypeDeclarationStatement() {
        return astNode instanceof TypeDeclarationStatement;
    }
    
    /**
     * Tests if this is a variable declaration-statement.
     * @return <true> if this is a variable declaration-statement, otherwise <code>false</code>
     */
    public boolean isVariableDeclarationStatement() {
        return astNode instanceof VariableDeclarationStatement;
    }
    
    /**
     * Tests if this is a while-statement.
     * @return <true> if this is a while-statement, otherwise <code>false</code>
     */
    public boolean isWhileStatement() {
        return astNode instanceof WhileStatement;
    }
    
    /**
     * Tests if a given statement equals to this.
     * @param jelem the Java statement
     * @return <code>true</code> if the given statement equals to this, otherwise <code>false</code>
     */
    public boolean equals(JavaStatement js) {
        if (js == null) {
            return false;
        }
        
        return this == js;
    }
    
    /**
     * Returns a hash code value for this statement.
     * @return the hash code value for the statement
     */
    public int hashCode() {
        return super.hashCode();
    }
    
    /**
     * Collects information about this statement.
     * @return the string for printing
     */
    public String toString() {
        return getASTNode().getClass().getName();
    }
}
