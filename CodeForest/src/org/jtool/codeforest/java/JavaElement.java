/*
 *  Copyright 2013, Katsuhisa Maruyama (maru@jtool.org)
 */

package org.jtool.codeforest.java;

import org.jtool.codeforest.java.internal.ExternalJavaClass;
import org.jtool.codeforest.java.internal.ExternalJavaField;
import org.jtool.codeforest.java.internal.ExternalJavaMethod;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.Initializer;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

/**
 * A root object for all Java program elements.
 * @author Katsuhisa Maruyama
 */
public abstract class JavaElement {
    
    /**
     * An AST node for this element.
     */
    protected ASTNode astNode = null;
    
    /**
     * The character index into the original source code indicating where the code fragment for this element begins.
     */
    protected int startPosition;
    
    /**
     * The length in characters of the code fragment for this element.
     */
    protected int codeLength;
    
    /**
     * The upper line number of code fragment for this element.
     */
    protected int upperLineNumber;
    
    /**
     * The bottom line number of code fragment for this element.
     */
    protected int bottomLineNumber;
    
    /**
     * Creates a new, empty object.
     */
    protected JavaElement() {
    }
    
    /**
     * Creates a new object representing a Java program element.
     * @param node an AST node for this element
     */
    protected JavaElement(ASTNode node) {
        astNode = node;
        startPosition = getStartPosition(node);
        codeLength = getCodeLength(node);
        upperLineNumber = getUpperLineNumber(node);
        bottomLineNumber = getBottomLineNumber(node);
    }
    
    /**
     * Sets the code properties with respect to positions and line numbers of this element.
     * @param start the character index indicating where the code fragment for this element begins
     * @param len the length in characters of the code fragment for this element
     * @param upper the upper line number of code fragment for this element
     * @param bottom the bottom line number of code fragment for this element
     */
    public void setCodeProperties(int start, int len, int upper, int bottom) {
        startPosition = start;
        codeLength = len;
        upperLineNumber = upper;
        bottomLineNumber = bottom;
    }
    
    /**
     * Returns the AST node for this element.
     * @return the AST node for this element
     */
    public ASTNode getASTNode() {
        return astNode;
    }
    
    /**
     * Returns the start position of code fragment for this element.
     * @param node an AST node for this element
     * @return the index value, or <code>-1</code> if no source position information is recorded
     */
    private int getStartPosition(ASTNode node) {
        if (node != null) {
            return node.getStartPosition();
        }
        return -1;
    }
    
    /**
     * Returns the character index into the original source code indicating where the code fragment for this element begins.
     * @return the index value, or <code>-1</code> if no source position information is recorded
     */
    public int getStartPosition() {
        return startPosition;
    }
    
    /**
     * Returns the length of code fragment for this element.
     * @param node an AST node for this element
     * @return the length of the characters, or <code>0</code> if no source position information is recorded
     */
    public int getCodeLength(ASTNode node) {
        if (node != null) {
            return node.getLength();
        }
        return -1;
    }
    
    /**
     * Returns the length in characters of the code fragment for this element.
     * @return the length of the characters, or <code>0</code> if no source position information is recorded
     */
    public int getCodeLength() {
        return codeLength;
    }
    
    /**
     * Returns the character index into the original source code indicating where the code fragment for this element ends.
     * @return the index value, or <code>-1</code> if no source position information is recorded
     */
    public int getEndPosition() {
        return startPosition + codeLength - 1;
    }
    
    /**
     * Returns a compilation unit containing this element.
     * @param node an AST node for this element
     * @return the compilation unit containing this element
     */
    public CompilationUnit getCompilationUnit(ASTNode node) {
        if (node != null) {
            return (CompilationUnit)node.getRoot();
        }
        return null;
    }
    
    /**
     * Returns the starting position of code fragment for this element, including comments and whitespace.
     * @return the index value, or <code>-1</code> if no source position information is recorded
     */
    public int getExtendedStartPosition() {
        CompilationUnit cu = getCompilationUnit(astNode);
        if (cu != null) {
            return cu.getExtendedStartPosition(astNode);
        }
        return -1;
    }
    
    
    /**
     * Returns the length of code fragment for this element, including comments and whitespace.
     * @return the length of the characters, or <code>0</code> if no source position information is recorded
     */
    public int getExtendedCodeLength() {
        CompilationUnit cu = getCompilationUnit(astNode);
        if (cu != null) {
            return cu.getExtendedLength(astNode);
        }
        return -1;
    }
    
    /**
     * Returns the character index into the original source code indicating where the code fragment for this element ends.
     * It may include comments and whitespace immediately before or after the normal source range for the element.
     * @return the index value, or <code>-1</code> if no source position information is recorded
     */
    public int getExtendedEndPosition() {
        return getExtendedStartPosition() + getExtendedCodeLength() - 1;
    }
    
    /**
     * Returns the upper line number of code fragment for this element.
     * @param node an AST node for this element
     * @return the upper line number of code fragment
     */
    public int getUpperLineNumber(ASTNode node) {
        CompilationUnit cu = getCompilationUnit(node);
        if (cu != null) {
            return cu.getLineNumber(getStartPosition());
        }
        return -1;
    }
    
    /**
     * Returns the upper line number of code fragment for this element.
     * @return the upper line number of code fragment
     */
    public int getUpperLineNumber() {
        return upperLineNumber;
    }
    
    /**
     * Returns the bottom line number of code fragment for this element.
     * @param node an AST node for this element
     * @return the bottom line number of code fragment
     */
    public int getBottomLineNumber(ASTNode node) {
        CompilationUnit cu = getCompilationUnit(node);
        if (cu != null) {
            return cu.getLineNumber(getEndPosition());
        }
        return -1;
    }
    
    /**
     * Obtains the lines of code fragment for this element.
     * @return the number of lines of code fragment
     */
    public int getBottomLineNumber() {
        return bottomLineNumber;
    }
    
    /**
     * Obtains the lines of code fragment for this element.
     * @return the number of lines of code fragment
     */
    public int getLoc() {
        return bottomLineNumber - upperLineNumber + 1;
    }
    
    /**
     * Returns a class corresponding to a given binding.
     * @param binding the type binding
     * @return the found class, or <code>null</code> if none
     */
    public JavaClass getDeclaringJavaClass(ITypeBinding binding) {
        if (binding != null) {
            JavaClass jc = JavaClass.getJavaClass(binding.getQualifiedName());
            if (jc != null) {
                return jc;
            }
            return ExternalJavaClass.create(binding);
        }
        return null;
    }
    
    /**
     * Returns a method corresponding to a given binding.
     * @param binding the method binding
     * @return the found method, or <code>null</code> if none
     */
    public JavaMethod getDeclaringJavaMethod(IMethodBinding binding) {
        if (binding != null) { 
            JavaClass jc = JavaClass.getJavaClass(binding.getDeclaringClass().getQualifiedName());
            
            if (jc != null) {
                JavaMethod jm = jc.getJavaMethod(getSignature(binding));
                if (jm != null) {
                    return jm;
                }
            }
            return ExternalJavaMethod.create(binding);
        }
        return null;
    }
    
    /**
     * Obtains the signature of a given method.
     * @param bind the binding for the method
     * @return the string of the method signature
     */
    private String getSignature(IMethodBinding binding) {
        return binding.getName() + "(" + getParameterTypes(binding) +" )";
    }
    
    /**
     * Obtains the type list of all the parameters of a given method.
     * @param bind the binding for the method 
     * @return the string including the fully qualified names of the parameter types, or an empty string if there is no parameter
     */
    private String getParameterTypes(IMethodBinding binding) {
        StringBuffer buf = new StringBuffer();
        ITypeBinding[] types = binding.getParameterTypes();
        for (int i = 0; i < types.length; i++) {
            buf.append(" ");
            buf.append(types[i].getQualifiedName());
        }
        return buf.toString();
    }
    
    /**
     * Returns a field variable corresponding to a given binding.
     * @param bind the variable binding
     * @return the found field variable, or <code>null</code> if none
     */
    public JavaField getDeclaringJavaField(IVariableBinding binding) {
        if (binding != null) { 
            ITypeBinding tbinding = binding.getDeclaringClass();
            if (tbinding != null) {
                JavaClass jc = JavaClass.getJavaClass(tbinding.getQualifiedName());
                if (jc != null) {
                    JavaField jf = jc.getJavaField(binding.getName());
                    if (jf != null) {
                        return jf;
                    }
                }
            }
            return ExternalJavaField.create(binding);
        }
        return null;
    }
    
    /**
     * Returns a class that encloses this element.
     * @param node the AST corresponding to this element
     * @return the class encloses this element, <code>null</code> if none
     */
    public JavaClass getDeclaringJavaClass(ASTNode node) {
        TypeDeclaration tnode = (TypeDeclaration)getAncestor(node, ASTNode.TYPE_DECLARATION);
        if (tnode != null) {
            return getDeclaringJavaClass(tnode.resolveBinding());
        }
        
        EnumDeclaration enode = (EnumDeclaration)getAncestor(node, ASTNode.ENUM_DECLARATION);
        if (enode != null) {
            return JavaClass.getJavaClass(enode.resolveBinding().getQualifiedName());
        }
        
        return null;
    }
    
    /**
     * Returns a method that encloses this element.
     * @param node the AST corresponding to this element
     * @return the method that encloses this element, <code>null</code> if none
     */
    public JavaMethod getDeclaringJavaMethod(ASTNode node) {
        MethodDeclaration mnode = (MethodDeclaration)getAncestor(node, ASTNode.METHOD_DECLARATION);
        if (mnode != null) {
            return getDeclaringJavaMethod(mnode.resolveBinding());
        }
        
        Initializer inode = (Initializer)getAncestor(node, ASTNode.INITIALIZER);
        if (inode != null) {
            JavaClass jc = getDeclaringJavaClass(node);
            if (jc != null) {
                return jc.getJavaMethod(JavaMethod.InitializerName);
            }
        }
        
        return null;
    }
    
    /**
     * Obtains the AST Node with a given node type, which declares or encloses a given AST Node.
     * @param node the enclosed AST Node
     * @param sort the node type (@see org.eclipse.jdt.core.dom.ASTNode)
     * @return the found AST node, or <code>null</code> if none
     */
    public ASTNode getAncestor(ASTNode node, int sort) {
        if (node.getNodeType() == sort) {
            return node;
        }
        
        ASTNode parent = node.getParent();
        if (parent != null) {
            return getAncestor(parent, sort);
        }
        
        return null;
    }
    
    
    /**
     * An integer number that indicates the binding level.
     */
    protected static int bindingLevel = 0;
    
    /**
     * Sets the binding level.
     * @param level an integer number that indicates the binding level.
     */
    protected static void setBindingLevel(int level) {
        bindingLevel = level;
    }
    
    /**
     * Returns the binding level.
     * @return An integer number that indicates the binding level.
     */
    protected static int getBindingLevel() {
        return bindingLevel;
    }
    
    /**
     * Obtains source code corresponding to this Java element.
     * @return the contents of the source code
     */
    public String getSource() {
        StringBuffer buf = new StringBuffer(getCompilationUnitSource(astNode));
        return buf.substring(getStartPosition(), getEndPosition() + 1);
    }
    
    /**
     * Obtains source code corresponding to this Java element.
     * It may include comments and whitespace immediately before or after the normal source range for the element.
     * @return the contents of the source code
     */
    public String getExtendedSource() {
        StringBuffer buf = new StringBuffer(getCompilationUnitSource(astNode));
        return buf.substring(getExtendedStartPosition(), getExtendedEndPosition() + 1);
    }
    
    /**
     * Obtains source code corresponding to the compilation unit containing this Java element.
     * @param node the AST corresponding to this element
     * @return the contents of the source code
     */
    private String getCompilationUnitSource(ASTNode node) {
        CompilationUnit cu = getCompilationUnit(node);
        if (cu != null) {
            ICompilationUnit icu = (ICompilationUnit)cu.getJavaElement();
            if (icu != null) {
                try {
                    return icu.getSource();
                } catch (JavaModelException e) { /* empty */ }
            }
        }
        return "";
    }
}
