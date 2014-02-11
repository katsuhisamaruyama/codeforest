/*
 *  Copyright 2014, Katsuhisa Maruyama (maru@jtool.org)
 */

package org.jtool.codeforest.metrics.java;

import org.jtool.eclipse.model.java.JavaClass;
import org.jtool.eclipse.model.java.JavaMethod;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Initializer;

/**
 * An object representing a method, a constructor, or an initializer.
 * @author Katsuhisa Maruyama
 */
public class CFJavaMethod extends JavaMethod {
    
    /**
     * The number of parameters of this method.
     */
    private int numberOfParameters;
    
    /**
     * The number of statements enclosed this method.
     */
    private int numberOfStatements;
    
    /**
     * The maximum number of nesting with respect to statements.
     */
    private int maxNumberOfNesting;
    
    /**
     * The cyclomatic number for measuring complexity of this method.
     */
    private int cyclomaticNumber;
    
    /**
     * Creates a new, empty object.
     */
    protected CFJavaMethod() {
        super();
    }
    
    /**
     * Creates a new object representing a method.
     * @param node an AST node for this method
     * @param jc the class declaring this method
     */
    public CFJavaMethod(MethodDeclaration node, JavaClass jc) {
        super(node);
        
        declaringClass = jc;
        numberOfParameters = node.parameters().size();
        
        IMethodBinding binding = node.resolveBinding();
        if (binding != null) {
            
            collectAccessedFields(node);
            collectCalledMethods(node);
            collectUsedTypes(node);
            
            collectStatementInfo(node);
            
            name = binding.getName();
            signature = getSignature(binding);
            type = binding.getReturnType().getQualifiedName();
            modifiers = binding.getModifiers();
            isConstructor = binding.isConstructor();
            isInitializer = false;
            for (ITypeBinding tbinding : binding.getExceptionTypes()) {
                exceptionNames.add(tbinding.getQualifiedName());
            }
            
        } else {
            name = ".UNKNOWN";
            signature = ".UNKNOWN";
            bindingOk = false;
        }
        
        jc.addJavaMethod(this);
    }
    
    /**
     * Creates a new object representing an initializer.
     * @param node the AST node for this initializer
     */
    public CFJavaMethod(Initializer node, JavaClass jc) {
        super(node);
        
        declaringClass = jc;
        numberOfParameters = 0;
        
        collectAccessedFields(node);
        collectCalledMethods(node);
        collectUsedTypes(node);
        
        collectStatementInfo(node);
        
        name = InitializerName;
        signature = name;
        type = "void";
        modifiers = 0;
        isConstructor = false;
        isInitializer = true;
        
        jc.addJavaMethod(this);
    }
    
    /**
     * Creates a new object representing an class.
     * @param name the name of this method
     * @param sig the signature of this method
     * @param type the type of this method.
     * @param modifiers the modifiers of this method.
     * @param isConstructor <code>true</code> if this method is a constructor, otherwise <code>false</code>
     * @param isInitializer <code>true</code> if this method is a initializer, otherwise <code>false</code>
     * @param jc the class declaring this method
     */
    public CFJavaMethod(String name, String sig, String type, int modifiers, boolean isConstructor, boolean isInitializer, JavaClass jc) {
        super(name, sig, type, modifiers, isConstructor, isInitializer, jc);
    }
    
    /**
     * Collects local variables declared in this method.
     * @param node the AST node for this method
     */
    private void collectStatementInfo(ASTNode node) {
        CFStatementInfoCollector svisitor = new CFStatementInfoCollector();
        node.accept(svisitor);
        
        numberOfStatements = svisitor.getNumberOfStatements();
        maxNumberOfNesting = svisitor.getMaximumNuberOfNesting();
        cyclomaticNumber = svisitor.getCyclomaticNumber();
    }
    
    /**
     * Returns the number of parameters of this method.
     * @return the number of parameters
     */
    public int getNumberOfParameters() {
        return numberOfParameters;
    }
    
    /**
     * Returns the number of statements enclosed this method.
     * @return the number of statements
     */
    public int getNumberOfStatements() {
        return numberOfStatements;
    }
    
    /**
     * Returns the maximum number of nesting with respect to statements enclosed this method.
     * @return the maximum number of nesting with respect to statements
     */
    public int getMaximumNuberOfNesting() {
        return maxNumberOfNesting;
    }
    
    /**
     * Returns the cyclomatic number for measuring complexity of this method.
     * @return the cyclomatic number
     */
    public int getCyclomaticNumber() {
        return cyclomaticNumber;
    }
    
    /**
     * Tests if a given method equals to this.
     * @param jm the Java method
     * @return <code>true</code> if the given method equals to this, otherwise <code>false</code>
     */
    public boolean equals(CFJavaMethod jm) {
        return super.equals(jm);
    }
    
    /**
     * Returns a hash code value for this method.
     * @return the hash code value for the method
     */
    public int hashCode() {
        return super.hashCode();
    }
}
