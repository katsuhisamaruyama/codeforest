/*
 *  Copyright 2013, Katsuhisa Maruyama (maru@jtool.org)
 */

package org.jtool.codeforest.java;

import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.SuperMethodInvocation;
import org.eclipse.jdt.core.dom.ConstructorInvocation;
import org.eclipse.jdt.core.dom.SuperConstructorInvocation;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * An object representing a method invocation.
 * @author Katsuhisa Maruyama
 */
public class JavaMethodInvocation extends JavaExpression {
	
    /**
     * The binding information about this method invocation.
     */
    private IMethodBinding binding;
    
    /**
     * The name of the called method.
     */
    private String name;
    
    /**
     * The signature of this method.
     */
    protected String signature;
    
    /**
     * The type of this method.
     */
    protected String type;
    
    /**
     * The collection of arguments for this method invocation.
     */
    private List<JavaExpression> arguments = new ArrayList<JavaExpression>();
    
    /**
     * The collection of argument types for this method invocation.
     */
    private List<String> argumentTypes = new ArrayList<String>();
    
    /**
     * The method containing this method invocation.
     */
    protected JavaMethod declaringMethod = null;
    
    /**
     * The name of class declaring the invoked method.
     */
    private String classNameOfInvokedMethod;
    
    /**
     * Creates a new, empty object.
     */
    protected JavaMethodInvocation() {
        super();
    }
    
    /**
     * Creates a new object representing a method invocation.
     * @param node an AST node for this invocation
     * @param binding the method binding for this invocation
     * @param jm the method containing this invocation
     */
    @SuppressWarnings("unchecked")
    public JavaMethodInvocation(MethodInvocation node, IMethodBinding binding, JavaMethod jm) {
        super(node);
        this.binding = binding;
        
        declaringMethod = jm;
        
        name = binding.getName();
        signature = getSignature(binding);
        type = binding.getReturnType().getQualifiedName();
        
        setArguments(node.arguments());
        setArgumentTypes(binding);
        
        classNameOfInvokedMethod = binding.getDeclaringClass().getQualifiedName();
    }
    
    /**
     * Creates a new object representing a method invocation.
     * @param node an AST node for this invocation
     * @param binding the method binding for this invocation
     * @param jm the method containing this invocation
     */
    @SuppressWarnings("unchecked")
    public JavaMethodInvocation(SuperMethodInvocation node, IMethodBinding binding, JavaMethod jm) {
        super(node);
        this.binding = binding;
        
        name = binding.getName();
        signature = getSignature(binding);
        type = binding.getReturnType().getQualifiedName();
        
        setArguments(node.arguments());
        setArgumentTypes(binding);
        
        declaringMethod = jm;
        classNameOfInvokedMethod = binding.getDeclaringClass().getQualifiedName();
    }
    
    /**
     * Creates a new object representing a constructor invocation.
     * @param node an AST node for this invocation
     * @param binding the method binding for this invocation
     * @param jm the method containing this invocation
     */
    @SuppressWarnings("unchecked")
    public JavaMethodInvocation(ConstructorInvocation node, IMethodBinding binding, JavaMethod jm) {
        super(node);
        this.binding = binding;
        
        name = binding.getName();
        signature = getSignature(binding);
        type = binding.getName();;
        
        setArguments(node.arguments());
        setArgumentTypes(binding);
        
        declaringMethod = jm;
        classNameOfInvokedMethod = binding.getDeclaringClass().getQualifiedName();
    }
    
    /**
     * Creates a new object representing a constructor invocation.
     * @param node an AST node for this invocation
     * @param binding the method binding for this invocation
     * @param jm the method containing this invocation
     */
    @SuppressWarnings("unchecked")
    public JavaMethodInvocation(SuperConstructorInvocation node, IMethodBinding binding, JavaMethod jm) {
        super(node);
        this.binding = binding;
        
        name = binding.getName();
        signature = getSignature(binding);
        type = binding.getName();;
        
        setArguments(node.arguments());
        setArgumentTypes(binding);
        
        declaringMethod = jm;
        classNameOfInvokedMethod = binding.getDeclaringClass().getQualifiedName();
    }
    
    /**
     * Creates a new object representing instance creation with a constructor invocation.
     * @param node an AST node for this invocation
     * @param binding the method binding for this invocation
     * @param jm the method containing this invocation
     */
    @SuppressWarnings("unchecked")
    public JavaMethodInvocation(ClassInstanceCreation node, IMethodBinding binding, JavaMethod jm) {
        super(node);
        this.binding = binding;
        
        name = binding.getName();
        signature = getSignature(binding);
        type = binding.getName();
        
        setArguments(node.arguments());
        setArgumentTypes(binding);
        
        declaringMethod = jm;
        classNameOfInvokedMethod = binding.getDeclaringClass().getQualifiedName();
    }
    
    /**
     * Sets arguments of this method invocation.
     * @param args the list of arguments
     */
    private void setArguments(List<Expression> args) {
        for (Expression arg : args) {
            JavaExpression expr = new JavaExpression(arg);
            arguments.add(expr);
        }
    }
    
    /**
     * Sets argument types of this method invocation.
     * @param binding the binding for the method
     */
    private void setArgumentTypes(IMethodBinding binding) {
        ITypeBinding[] types = binding.getParameterTypes();
        for (int i = 0; i < types.length; i++) {
            argumentTypes.add(types[i].getQualifiedName());
        }
    }
    
    /**
     * Obtains the type list of all the arguments of this method invocation.
     * @param binding the binding for the method
     * @return the string including the fully qualified names of the argument types, or an empty string if there is no parameter
     */
    private String getArgumentTypes(IMethodBinding binding) {
        StringBuffer buf = new StringBuffer();
        ITypeBinding[] types = binding.getParameterTypes();
        for (int i = 0; i < types.length; i++) {
            buf.append(" ");
            buf.append(types[i].getQualifiedName());
        }
        return buf.toString();
    }
    
    /**
     * Obtains the signature of this method or constructor call.
     * @return the string of the method signature.
     */
    private String getSignature(IMethodBinding binding) {
        return getName() + "(" + getArgumentTypes(binding) + " )";
    }
    
    /**
     * Returns the name of the invoked method.
     * @return the name
     */
    public String getName() {
        return name;
    }
    
    /**
     * Returns the fully-qualified name of the invoked method.
     * @return the name
     */
    public String getQualifiedName() {
        if (classNameOfInvokedMethod != null) {
            return  classNameOfInvokedMethod + "#" + getSignature();
        }
        return getSignature();
    }
    
    /**
     * Returns the type of the invoked method.
     * @return the type
     */
    public String getType() {
        return type;
    }
    
    /**
     * Obtains the signature of this method.
     * @return the string of the method signature
     */
    public String getSignature() {
        return signature;
    }
    
    /**
     * Tests if the invoked method has no return value. 
     * @return <code>true</code> if there is no return value of the invoked method, otherwise <code>false</code>
     */
    public boolean isVoid() {
        return getType().compareTo("void") == 0;
    }
    
    /**
     * Returns all the arguments of this method invocation.
     * @return the collection of the arguments
     */
    public List<JavaExpression> getArguments() {
        return arguments;
    }
    
    /**
     * Returns the number of arguments of this method invocation.
     * @return the number of arguments
     */
    public int getArgumentSize() {
        return arguments.size();
    }
    
    /**
     * Returns the argument of this method invocation at specified position.
     * @param pos the ordinal number of the argument to be retrieved
     * @return the found argument, <code>null</code> if no argument was found
     */
    public JavaExpression getArgument(int pos) {
        if (pos < arguments.size()) {
            return arguments.get(pos);
        } else {
            return null;
        }
    }
    
    /**
     * Returns the argument type of this method invocation at specified position.
     * @param pos the ordinal number of the argument to be retrieved
     * @return the string of the found argument type, <code>null</code> if no argument was found
     */
    public String getArgumentType(int pos) {
        return argumentTypes.get(pos);
    }
    
    /**
     * Tests if a given method invocation equals to this one.
     * @param ji the method invocation
     * @return <code>true</code> if the given method invocation equals to this one, otherwise <code>false</code>
     */
    public boolean equals(JavaMethodInvocation ji) {
        if (ji == null) {
            return false;
        }
        
        return this == ji || getQualifiedName().compareTo(ji.getQualifiedName()) == 0; 
    }
    
    /**
     * Returns a hash code value for this method invocation.
     * @return the hash code value for the method invocation
     */
    public int hashCode() {
        return getQualifiedName().hashCode();
    }
    
    /**
     * Tests if this method invocation corresponds to a given method.
     * @param jm the method which is compared to
     * @return <code>true</code> if this invocation corresponds to the method, otherwise <code>false</code>
     */
    public boolean equals(JavaMethod jm) {
        if (classNameOfInvokedMethod == null) {
            return false;
        }
        return classNameOfInvokedMethod.compareTo(jm.getDeclaringJavaClass().getQualifiedName()) == 0 &&
               getSignature().compareTo(jm.getSignature()) == 0;
    }
    
    /* ================================================================================
     * The following functionalities can be used after completion of whole analysis 
     * ================================================================================ */
    
    /**
     * The method corresponding to this method invocation.
     */
    private JavaMethod jmethod;
    
    /**
     * Tests if the binding for this method invocation was found.
     * @return <code>true</code> if the binding was found
     */
    public boolean isBindingOk() {
        return binding != null;
    }
    
    /**
     * Displays error log if the binding is not completed.
     */
    private void bindingCheck() {
        if (bindingLevel < 1) {
            System.err.println("This API can be invoked after the completion of whole analysis");
        }
    }
    
    /**
     * Returns the Java class enclosing the invoked method.
     * @return the enclosing class for the invoked method
     */
    public JavaClass getJavaClassOf() {
        bindingCheck();
        JavaMethod jm = getJavaMethod();
        if (jm != null) {
            return jm.getDeclaringJavaClass();
        }
        return null;
    }
    
    /**
     * Returns a method corresponding to this method invocation.
     * @return the found method, or <code>null</code> if none
     */
    public JavaMethod getJavaMethod() {
        bindingCheck();
        
        if (jmethod != null) {
            return jmethod;
        }
        
        if (binding != null) {
            jmethod = getDeclaringJavaMethod(binding.getMethodDeclaration());
            return jmethod;
        }
        return null;
    }
    
    /**
     * Tests if this method call directly invokes the method itself.
     * @return <code>true</code> if this method call directly invokes the method itself, otherwise <code>false</code>
     */
    public boolean callSelfDirectly() {
        bindingCheck();
        
        if (declaringMethod == null) {
            return false;
        }
        return getJavaMethod().equals(declaringMethod);
    }
    
    /**
     * Tests if this method call recursively invokes the method itself.
     * @return <code>true</code> if this method call recursively invokes the method itself, otherwise <code>false</code>
     */
    public boolean callSelfRecursively() {
        bindingCheck();
        
        if (declaringMethod == null) {
            return false;
        }
        HashSet<JavaMethod> methods = new HashSet<JavaMethod>();
        collectCalledMethods(getJavaMethod(), methods);
        return methods.contains(declaringMethod);
    }
    
    /**
     * Collects all called methods that this method invokes traverses.
     * @param jm the called method to be checked and collected
     * @param methods the collection of the invoked methods
     */
    private void collectCalledMethods(JavaMethod jm, HashSet<JavaMethod> methods) {
        methods.add(jm);
        for (JavaMethod cm : jm.getCalledJavaMethods()) {
            if (!methods.contains(cm)) {
                collectCalledMethods(cm, methods);
            }
        }
    }
    
    /**
     * Collects information about this method or constructor call for printing.
     * @return the string for printing
     */
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("METHOD INVOCATION: " + getQualifiedName());
        return buf.toString();
    }
}
