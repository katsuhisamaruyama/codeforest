/*
 *  Copyright 2014, Katsuhisa Maruyama (maru@jtool.org)
 */

package org.jtool.codeforest.metrics.java;

import org.eclipse.jdt.core.dom.ASTVisitor;
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
import org.eclipse.jdt.core.dom.Statement;
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
 * Visits a Java program and stores information on statements appearing in a method.
 * 
 * AssertStatement
 * Block
 * BreakStatement
 * ConstructorInvocation
 * ContinueStatement
 * DoStatement
 * EmptyStatement
 * EnhancedForStatement
 * ExpressionStatement
 * ForStatement
 * IfStatement
 * LabeledStatement
 * ReturnStatement
 * SuperConstructorInvocation
 * SwitchCase
 * SwitchStatement
 * SynchronizedStatement
 * ThrowStatement
 * TryStatement
 * TypeDeclarationStatement
 * VariableDeclarationStatement
 * WhileStatement
 * 
 * @see org.eclipse.jdt.core.dom.Statement
 * @author Katsuhisa Maruyama
 */
public class CFStatementInfoCollector extends ASTVisitor {
    
    /**
     * The number of statements enclosed a method or field.
     */
    private int numberOfStatements;
    
    /**
     * The number of nesting with respect to statements enclosed a method.
     */
    private int numberOfNesting;
    
    /**
     * The maximum number of nesting with respect to statements.
     */
    private int maxNumberOfNesting;
    
    /**
     * The cyclomatic number for measuring complexity of a method.
     */
    private int cyclomaticNumber;
    
    /**
     * Creates a new object for collecting information on statements enclosed a method.
     */
    public CFStatementInfoCollector() {
        super();
        
        numberOfStatements = 0;
        numberOfNesting = 0;
        maxNumberOfNesting = 0;
        cyclomaticNumber = 1;
    }
    
    /**
     * Visits a method declaration node and stores its information.
     * @param node the method declaration node
     * @return <code>true</code> if this visit is continued inside, otherwise <code>false</code>
     */
    public boolean visit(Statement node) {
        return true;
    }
    
    /**
     * Visits an assert-statement.
     * @param node the assert-statement node
     * @return <code>true</code> if this visit is continued inside, otherwise <code>false</code>
     */
    public boolean visit(AssertStatement node) {
        numberOfStatements++;
        return true;
    }
    
    /**
     * Visits a block.
     * @param node the block node
     * @return <code>true</code> if this visit is continued inside, otherwise <code>false</code>
     */
    public boolean visit(Block node) {
        return true;
    }
    
    /**
     * Visits a break-statement.
     * @param node the break-statement node
     * @return <code>true</code> if this visit is continued inside, otherwise <code>false</code>
     */
    public boolean visit(BreakStatement node) {
        numberOfStatements++;
        return true;
    }
    
    /**
     * Visits a constructor invocation.
     * @param node the constructor invocation node
     * @return <code>true</code> if this visit is continued inside, otherwise <code>false</code>
     */
    public boolean visit(ConstructorInvocation node) {
        numberOfStatements++;
        return true;
    }
    
    /**
     * Visits a continue-statement.
     * @param node the continue-statement node
     * @return <code>true</code> if this visit is continued inside, otherwise <code>false</code>
     */
    public boolean visit(ContinueStatement node) {
        numberOfStatements++;
        return true;
    }
    
    /**
     * Visits a do-statement.
     * @param node the do-statement node
     * @return <code>true</code> if this visit is continued inside, otherwise <code>false</code>
     */
    public boolean visit(DoStatement node) {
        numberOfStatements++;
        cyclomaticNumber++;
        incNestCount();
        return true;
    }
    
    /**
     * Finishes the visit for a do-statement.
     */
    public void endVisit(DoStatement node) {
        decNestCount();
    }
    
    /**
     * Visits an empty-statement.
     * @param node the empty-statement node
     * @return <code>true</code> if this visit is continued inside, otherwise <code>false</code>
     */
    public boolean visit(EmptyStatement node) {
        return true;
    }
    
    /**
     * Visits an enhanced for-statement.
     * @param node the enhanced for-statement node
     * @return <code>true</code> if this visit is continued inside, otherwise <code>false</code>
     */
    public boolean visit(EnhancedForStatement node) {
        numberOfStatements++;
        cyclomaticNumber++;
        incNestCount();
        return true;
    }
    
    /**
     * Finishes the visit for a enhanced for-statement.
     */
    public void endVisit(EnhancedForStatement node) {
        decNestCount();
    }
    
    /**
     * Visits an expression-statement.
     * @param node the expression-statement node
     * @return <code>true</code> if this visit is continued inside, otherwise <code>false</code>
     */
    public boolean visit(ExpressionStatement node) {
        numberOfStatements++;
        return true;
    }
    
    /**
     * Visits a for-statement.
     * @param node the for-statement node
     * @return <code>true</code> if this visit is continued inside, otherwise <code>false</code>
     */
    public boolean visit(ForStatement node) {
        numberOfStatements++;
        cyclomaticNumber++;
        incNestCount();
        return true;
    }
    
    /**
     * Finishes the visit for a for-statement.
     */
    public void endVisit(ForStatement node) {
        decNestCount();
    }
    
    /**
     * Visits an if-statement.
     * @param node the if-statement node
     * @return <code>true</code> if this visit is continued inside, otherwise <code>false</code>
     */
    public boolean visit(IfStatement node) {
        numberOfStatements++;
        cyclomaticNumber++;
        incNestCount();
        return true;
    }
    
    /**
     * Finishes the visit for an if-statement.
     */
    public void endVisit(IfStatement node) {
        decNestCount();
    }
    
    /**
     * Visits a labeled-statement.
     * @param node the labeled-statement node
     * @return <code>true</code> if this visit is continued inside, otherwise <code>false</code>
     */
    public boolean visit(LabeledStatement node) {
        numberOfStatements++;
        return true;
    }
    
    /**
     * Visits a return-statement.
     * @param node the return-statement node
     * @return <code>true</code> if this visit is continued inside, otherwise <code>false</code>
     */
    public boolean visit(ReturnStatement node) {
        numberOfStatements++;
        return true;
    }
    
    /**
     * Visits a super-constructor invocation.
     * @param node the super-constructor invocation node
     * @return <code>true</code> if this visit is continued inside, otherwise <code>false</code>
     */
    public boolean visit(SuperConstructorInvocation node) {
        numberOfStatements++;
        return true;
    }
    
    /**
     * Visits a switch-case.
     * @param node the switch-case node
     * @return <code>true</code> if this visit is continued inside, otherwise <code>false</code>
     */
    public boolean visit(SwitchCase node) {
        numberOfStatements++;
        cyclomaticNumber++;
        incNestCount();
        return true;
    }
    
    /**
     * Finishes the visit for a switch-case.
     */
    public void endVisit(SwitchCase node) {
        decNestCount();
    }
    
    /**
     * Visits a switch-statement.
     * @param node the switch-statement node
     * @return <code>true</code> if this visit is continued inside, otherwise <code>false</code>
     */
    public boolean visit(SwitchStatement node) {
        numberOfStatements++;
        return true;
    }
    
    /**
     * Visits a synchronized-statement.
     * @param node the synchronized-statement node
     * @return <code>true</code> if this visit is continued inside, otherwise <code>false</code>
     */
    public boolean visit(SynchronizedStatement node) {
        numberOfStatements++;
        return true;
    }
    
    /**
     * Visits a throw-statement.
     * @param node the throw-statement node
     * @return <code>true</code> if this visit is continued inside, otherwise <code>false</code>
     */
    public boolean visit(ThrowStatement node) {
        numberOfStatements++;
        return true;
    }
    
    /**
     * Visits a try-statement.
     * @param node the try-statement node
     * @return <code>true</code> if this visit is continued inside, otherwise <code>false</code>
     */
    public boolean visit(TryStatement node) {
        numberOfStatements++;
        cyclomaticNumber++;
        incNestCount();
        return true;
    }
    
    /**
     * Finishes the visit for a try-statement.
     */
    public void endVisit(TryStatement node) {
        decNestCount();
    }
    
    /**
     * Visits a type-declaration statement.
     * @param node the type-declaration statement node
     * @return <code>true</code> if this visit is continued inside, otherwise <code>false</code>
     */
    public boolean visit(TypeDeclarationStatement node) {
        numberOfStatements++;
        return true;
    }
    
    /**
     * Visits a variable-declaration statement.
     * @param node the variable-declaration statement node
     * @return <code>true</code> if this visit is continued inside, otherwise <code>false</code>
     */
    public boolean visit(VariableDeclarationStatement node) {
        numberOfStatements++;
        return true;
    }
    
    /**
     * Visits a while-statement.
     * @param node the while-statement node
     * @return <code>true</code> if this visit is continued inside, otherwise <code>false</code>
     */
    public boolean visit(WhileStatement node) {
        numberOfStatements++;
        cyclomaticNumber++;
        incNestCount();
        return true;
    }
    
    /**
     * Finishes the visit for a while-statement.
     */
    public void endVisit(WhileStatement node) {
        decNestCount();
    }
    
    /**
     * Increments the count of nesting.
     */
    private void incNestCount() {
        numberOfNesting++;
        
        if (maxNumberOfNesting < numberOfNesting) {
            maxNumberOfNesting = numberOfNesting;
        }
    }
    
    /**
     * Decrements the count of nesting.
     */
    private void decNestCount() {
        numberOfNesting--;
    }
    
    /**
     * Returns the number of statements enclosed a method.
     * @return the number of statements
     */
    public int getNumberOfStatements() {
        return numberOfStatements;
    }
    
    /**
     * Returns the maximum number of nesting with respect to statements enclosed a method.
     * @return the maximum number of nesting with respect to statements
     */
    public int getMaximumNuberOfNesting() {
        return maxNumberOfNesting;
    }
    
    /**
     * Returns the cyclomatic number for measuring complexity of a method.
     * @return the cyclomatic number
     */
    public int getCyclomaticNumber() {
        return cyclomaticNumber;
    }
}
