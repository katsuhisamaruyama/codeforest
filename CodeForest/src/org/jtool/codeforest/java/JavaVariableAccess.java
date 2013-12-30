/*
 *  Copyright 2013, Katsuhisa Maruyama (maru@jtool.org)
 */

package org.jtool.codeforest.java;

import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.Name;
import org.jtool.codeforest.java.internal.ExternalJavaField;

/**
 * An object representing an expression for a variable access.
 * @author Katsuhisa Maruyama
 */
public class JavaVariableAccess extends JavaExpression {
    
    /**
     * The binding information about this variable access.
     */
    private IVariableBinding binding;
    
    /**
     * The name of this variable.
     */
    protected String name;
    
    /**
     * The type of this variable.
     */
    protected String type;
    
    /**
     * A flag indicating if this object represents a field.
     */
    private boolean isField = false;
    
    /**
     * A flag indicating if this object represents an enum constant.
     */
    private boolean isEnumConstant = false;
    
    /**
     * The method containing this variable access.
     */
    protected JavaMethod declaringMethod = null;
    
    /**
     * The name of class declaring the accessed field.
     */
    private String classNameOfAccessedField;
    
    /**
     * Creates a new, empty object.
     */
    protected JavaVariableAccess() {
        super();
    }
    
    /**
     * Creates a new object representing a variable.
     * @param node an AST node for this variable
     * @param jm the method containing this invocation
     */
    public JavaVariableAccess(Name node, JavaMethod jm) {
        super(node);
        
        declaringMethod = jm;
        binding = (IVariableBinding)node.resolveBinding();
        
        name = binding.getName();
        type = binding.getType().getQualifiedName();
        isField = binding.isField();
        isEnumConstant = binding.isEnumConstant();
        
        
        if (isField || isEnumConstant) {
            ITypeBinding tbinding = binding.getDeclaringClass();
            if (tbinding != null) {
                classNameOfAccessedField = tbinding.getQualifiedName();
            } else {
                JavaField jf = ExternalJavaField.create(binding);
                classNameOfAccessedField = jf.getDeclaringJavaClass().getQualifiedName();
            }
        }
    }
    
    /**
     * Tests if a given name represents a field.
     * @return <code>true</code> if the name represents a field, otherwise <code>false</code>
     */
    public boolean isField() {
        return isField || isEnumConstant;
    }
    
    /**
     * Tests if a given name represents a local variable.
     * @return <code>true</code> if the name represents a local variable, otherwise <code>false</code>
     */
    public boolean isLocal() {
        return !isField && !isEnumConstant;
    }
    
    /**
     * Returns the name of the accessed variable.
     * @return the name
     */
    public String getName() {
        return name;
    }
    
    /**
     * Returns the fully-qualified name of the accessed variable.
     * @return the name
     */
    public String getQualifiedName() {
        if (classNameOfAccessedField != null) {
            return classNameOfAccessedField + "#" + getName();
        }
        return getName();
    }
    
    /**
     * Returns the type of this variable.
     * @return the type
     */
    public String getType() {
        return type;
    }
    
    /**
     * Tests if a given variable access equals to this.
     * @param jacc the variable access
     * @return <code>true</code> if the given variable access equals to this, otherwise <code>false</code>
     */
    public boolean equals(JavaVariableAccess jacc) {
        if (jacc == null) {
            return false;
        }
        if (this == jacc) {
            return true;
        }
        
        return getQualifiedName().compareTo(jacc.getQualifiedName()) == 0;
    }
    
    /**
     * Returns a hash code value for this variable access.
     * @return the hash code value for the variable access
     */
    public int hashCode() {
        return getQualifiedName().hashCode();
    }
    
    /**
     * Tests if this variable access corresponds to a given field.
     * @param jf the field which is compared to
     * @return <code>true</code> if this access corresponds to the field, otherwise <code>false</code>
     */
    public boolean equals(JavaField jf) {
        if (classNameOfAccessedField == null) {
            return false;
        }
        return classNameOfAccessedField.compareTo(jf.getDeclaringJavaClass().getQualifiedName()) == 0 &&
               getName().compareTo(jf.getName()) == 0;
    }
    
    /* ================================================================================
     * The following functionalities can be used after completion of whole analysis 
     * ================================================================================ */
    
    /**
     * A field corresponding to this variable access, or <code>null</code> if this is not a field variable access.
     */
    private JavaField jfield = null;
    
    /**
     * A local variable corresponding to this variable access, or <code>null</code> if this is not a local variable access.
     */
    private JavaLocal jlocal = null;
    
    /**
     * Tests if the binding for this variable access was found.
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
     * Returns a field variable corresponding to this variable access.
     * @return the found field variable, or <code>null</code> if none
     */
    public JavaField getJavaField() {
        bindingCheck();
        
        if (jfield != null) {
            return jfield;
        }
        
        if (binding != null) {
            jfield = getDeclaringJavaField(binding.getVariableDeclaration());
            return jfield;
        }
        return null;
    }
    
    /**
     * Returns the Java class enclosing the accessed field.
     * @return the enclosing class for the accessed field
     */
    public JavaClass getJavaClassOf() {
        bindingCheck();
        
        if (isField()) {
            JavaField jf = getJavaField();
            if (jf != null) {
                return jfield.getDeclaringJavaClass();
            }
        }
        return null;
    }
    
    /**
     * Returns a local variable corresponding to this variable access.
     * @return the found local variable, or <code>null</code> if none
     */
    public JavaLocal getJavaLocal() {
        bindingCheck();
        
        if (jlocal != null) {
            return jlocal;
        }
        
        if (binding != null) {
            jlocal = getDeclaringJavaLocal(binding.getVariableDeclaration());
            return jlocal;
        }
        return null;
    }
    
    /**
     * Returns a local variable corresponding to a given AST node and its binding.
     * @param bind the variable binding
     * @return the found local variable, or <code>null</code> if none
     */
    private JavaLocal getDeclaringJavaLocal(IVariableBinding binding) {
        bindingCheck();
        
        if (declaringMethod != null) {
            JavaLocal jl = declaringMethod.getJavaLocal(binding.getName(), binding.getVariableId());
            if (jl != null) {
                return jl;
            }
        }
        return null;
    }
    
    /**
     * Returns the Java method enclosing the accessed field.
     * @return the enclosing method for the accessed field
     */
    public JavaMethod getJavaMethodOf() {
        bindingCheck();
        
        if (isLocal()) {
            JavaLocal jl = getJavaLocal();
            if (jl != null) {
                return jl.getDeclaringJavaMethod();
            }
        }
        return null;
    }
    
    /**
     * Collects information about this field access.
     * @return the string for printing
     */
    public String toString() {
        StringBuffer buf = new StringBuffer();
        
        if (jfield != null) {
            buf.append(jfield.getDeclaringJavaClass().getQualifiedName());
            buf.append("#");
        }
        
        buf.append(getName());
        buf.append("@");
        buf.append(getType());
        
        if (jlocal != null) {
            buf.append("[");
            buf.append(jlocal.getId());
            buf.append("]");
        }
        
        return buf.toString();
    }
}
