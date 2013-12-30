/*
 *  Copyright 2013, Katsuhisa Maruyama (maru@jtool.org)
 */

package org.jtool.codeforest.java;

import org.jtool.codeforest.java.internal.FieldInitializerCollector;
import org.jtool.codeforest.java.internal.MethodInvocationCollector;
import org.eclipse.jdt.core.dom.EnumConstantDeclaration;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.VariableDeclaration;
import java.util.Set;
import java.util.HashSet;

/**
 * An object representing a field or an enum constant.
 * @author Katsuhisa Maruyama
 */
public class JavaField extends JavaExpression {
	
    /**
     * The name of this field.
     */
    protected String name;
    
    /**
     * The type of this field.
     */
    protected String type;
    
    /**
     * The modifiers of this field.
     */
    private int modifiers;
    
    /**
     * A flag indicating if this field is an enum constant.
     */
    private boolean isEnumConstant = false;
    
    /**
     * The class declaring this field.
     */
    protected JavaClass declaringClass = null;
    
    /**
     * Creates a new, empty object.
     */
    protected JavaField() {
        super();
    }
    
    /**
     * Creates a new object representing a field.
     * @param node an AST node for this field
     * @param jc the class declaring this field
     */
    public JavaField(VariableDeclaration node, JavaClass jc) {
        super(node);
        
        declaringClass = jc;
        IVariableBinding binding = node.resolveBinding().getVariableDeclaration();
        
        if (binding != null) {
            name = binding.getName();
            type = binding.getType().getQualifiedName();
            modifiers = binding.getModifiers();
            isEnumConstant = false;
        } else {
            name = ".UNKNOWN";
            type = ".UNKNOWN";
            modifiers = 0;
            isEnumConstant = false;
        }
        
        jc.addJavaField(this);
    }
    
    /**
     * Creates a new object representing an enum constant.
     * @param node an AST node for this enum constant
     * @param decl an AST node for this field declaration
     */
    public JavaField(EnumConstantDeclaration node, JavaClass jc) {
        super(node);
        
        declaringClass = jc;
        IVariableBinding binding = node.resolveVariable();
        
        if (binding != null) {
            name = binding.getName();
            type = binding.getType().getQualifiedName();
            modifiers = binding.getModifiers();
            isEnumConstant = true;
        } else {
            name = ".UNKNOWN";
            type = ".UNKNOWN";
            modifiers = 0;
            isEnumConstant = true;
        }
        
        jc.addJavaField(this);
    }
    
    /**
     * Creates a new object representing a field.
     * @param name the name of this field
     * @param type the type of this field
     * @param modifiers the modifiers of this field
     * @param isEnumConstant <code>true> if this field is an enum constant, otherwise <code>false</code>
     * @param jc the class declaring this field
     */
    public JavaField(String name, String type, int modifiers, boolean isEnumConstant, JavaClass jc) {
        super();
        
        this.name = name;
        this.type = type;
        this.modifiers = modifiers;
        this.isEnumConstant = isEnumConstant;
        declaringClass = jc;
        
        jc.addJavaField(this);
    }
    
    /**
     * Converts a local variable object into a variable access object.
     * @return the created variable access object.
     */
    public JavaVariableAccess convertJavavariableAccess() {
        Name name = null;
        if (astNode instanceof VariableDeclaration) {
            name = ((VariableDeclaration)astNode).getName();
        } else if (astNode instanceof EnumConstantDeclaration) {
            name = ((EnumConstantDeclaration)astNode).getName();
            
        }
        
        if (name != null && name.resolveBinding() != null) {
            return new JavaVariableAccess(name, null);
        }
        return null;
    }
    
    /**
     * Tests if this object represents a normal field.
     * @return <code>true</code> if this object represents a field, otherwise <code>false</code>
     */
    public boolean isField() {
        return !isEnumConstant;
    }
    
    /**
     * Tests if this object represents an enum constant.
     * @return <code>true</code> if this object represents an enum constant, otherwise <code>false</code>
     */
    public boolean isEnumConstant() {
        return isEnumConstant;
    }
    
    /**
     * Tests if this field exists in the project.
     * @return always <code>true</code>
     */
    public boolean isInProject() {
        return true;
    }
    
    /**
     * Returns the class that declares this field.
     * @return the class that declares this field
     */
    public JavaClass getDeclaringJavaClass() {
        return declaringClass;
    }
    
    /**
     * Returns the name of this field.
     * @return the name
     */
    public String getName() {
        return name;
    }
    
    /**
     * Returns the fully-qualified name of this field.
     * @return the name
     */
    public String getQualifiedName() {
        JavaClass jc = getDeclaringJavaClass();
        if (jc != null) {
            return jc.getQualifiedName() + "#" + getName();
        }
        return getName();
    }
    
    /**
     * Returns the type of this field.
     * @return the type
     */
    public String getType() {
        return type;
    }
    
    /**
     * Returns the value representing modifiers of this field.
     * @return the modifiers value
     */
    public int getModifiers() {
        return modifiers;
    }
    
    /**
     * Tests if the access setting of this field is public.
     * @return <code>true</code> if this is a public field, otherwise <code>false</code>
     */
    public boolean isPublic() {
        return Modifier.isPublic(modifiers);
    }
    
    /**
     * Tests if the access setting of this field is protected.
     * @return <code>true</code> if this is a protected field, otherwise <code>false</code>
     */
    public boolean isProtected() {
        return Modifier.isProtected(modifiers);
    }
    
    /**
     * Tests if the access setting of this field is private.
     * @return <code>true</code> if this is a private field, otherwise <code>false</code>
     */
    public boolean isPrivate() {
        return Modifier.isPrivate(modifiers);
    }
    
    /**
     * Tests if the access setting of this field has default visibility.
     * @return <code>true</code> if this is a field with default visibility, otherwise <code>false</code>
     */
    public boolean isDefault() {
        return !isPublic() && !isProtected() && !isPrivate();
    }
    
    /**
     * Tests if the access setting of this field is final.
     * @return <code>true</code> if this is a final field, otherwise <code>false</code>
     */
    public boolean isFinal() {
        return Modifier.isFinal(modifiers);
    }
    
    /**
     * Tests if the access setting of this field is static.
     * @return <code>true</code> if this is a static field, otherwise <code>false</code>
     */
    public boolean isStatic() {
        return Modifier.isStatic(modifiers);
    }
    
    /**
     * Tests if the access setting of this field is volatile.
     * @return <code>true</code> if this is a volatile field, otherwise <code>false</code>
     */
    public boolean isVolatile() {
        return Modifier.isVolatile(modifiers);
    }
    
    /**
     * Tests if the access setting of this field is transient.
     * @return <code>true</code> if this is a transient field, otherwise <code>false</code>
     */
    public boolean isTransient() {
        return Modifier.isTransient(modifiers);
    }
    
    /**
     * Tests if a given field equals to this.
     * @param jf the Java field
     * @return <code>true</code> if the given field equals to this, otherwise <code>false</code>
     */
    public boolean equals(JavaField jf) {
        if (jf == null) {
            return false;
        }
        
        return this == jf || (getDeclaringJavaClass().equals(jf.getDeclaringJavaClass()) &&
                              getName().compareTo(jf.getName()) == 0); 
    }
    
    /**
     * Returns a hash code value for this field.
     * @return the hash code value for the field
     */
    public int hashCode() {
        return getName().hashCode();
    }
    
    /* ================================================================================
     * The following functionalities can be used after completion of whole analysis 
     * ================================================================================ */
    
    /**
     * The collection of fields that this field declaration accesses.
     */
    private Set<JavaField> accessedFields = new HashSet<JavaField>();
    
    /**
     * The collection of all field declarations that access this field.
     */
    private Set<JavaField> accessingFields = new HashSet<JavaField>();
    
    /**
     * The collections of all methods that this field declaration calls.
     */
    private Set<JavaMethod> calledMethods = new HashSet<JavaMethod>();
    
    /**
     * The collection of all methods that accesses this field.
     */
    private Set<JavaMethod> accessingMethods = new HashSet<JavaMethod>();
    
    /**
     * A flag that indicates all bindings for methods and variables were found.
     */
    private boolean bindingOk = false;
    
    /**
     * Collects additional information on this method.
     */
    public void collectLevel2Info() {
        if (astNode != null) {
            FieldInitializerCollector fvisitor = new FieldInitializerCollector();
            astNode.accept(fvisitor);
            accessedFields = fvisitor.getAccessedFields();
            for (JavaField jf : accessedFields) {
                jf.addAccessingJavaField(this);
            }
            
            MethodInvocationCollector mvisitor = new MethodInvocationCollector(null);
            astNode.accept(mvisitor);
            calledMethods = mvisitor.getMethodInvocations();
            for (JavaMethod jm : calledMethods) {
                jm.addAccessingJavaField(this);
            }
            
            bindingOk = fvisitor.isBindingOk() && mvisitor.isBindingOk();
        }
    }
    
    /**
     * Tests if the binding for this field was found.
     * @return <code>true</code> if the binding was found
     */
    public boolean isBindingOk() {
        return bindingOk;
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
     * Returns all the fields accessed by this field.
     * @return the collection of the accessed fields
     */
    public Set<JavaField> getAccessedJavaFields() {
        bindingCheck();
        return accessedFields;
    }
    
    /**
     * Returns all the fields accessed by this field with the project.
     * @return the collection of the accessed fields
     */
    public Set<JavaField> getAccessedJavaFieldsInProject() {
        bindingCheck();
        
        Set<JavaField> fields = new HashSet<JavaField>();
        for (JavaField jf : getAccessedJavaFields()) {
            if (jf.isInProject()) {
                fields.add(jf);
            }
        }
        return fields;
    }
    
    /**
     * Returns all the fields access this field.
     * @return the collection of the accessing fields
     */
    public Set<JavaField> getAccessingJavaFields() {
        bindingCheck();
        return accessingFields;
    }
    
    /**
     * Adds a method that accesses this field.
     * @param jm the method
     */
    void addCallingJavaMethod(JavaMethod jm) {
        accessingMethods.add(jm);
    }
    
    /**
     * Returns all the methods access this field.
     * @return the collection of the accessing methods
     */
    public Set<JavaMethod> getAccessingJavaMethods() {
        bindingCheck();
        return accessingMethods;
    }
    
    /**
     * Adds a field that accesses this field.
     * @param jf the field
     */
    void addAccessingJavaField(JavaField jf) {
        accessingFields.add(jf);
    }
    
    /**
     * Returns all the methods by this field.
     * @return the collection of the called methods
     */
    public Set<JavaMethod> getCalledJavaMethods() {
        bindingCheck();
        return calledMethods;
    }
    
    /**
     * Returns all the methods by this field within the project.
     * @return the collection of the called methods within the project
     */
    public Set<JavaMethod> getCalledJavaMethodsInProject() {
        bindingCheck();
        
        Set<JavaMethod> methods = new HashSet<JavaMethod>();
        for (JavaMethod jm : getCalledJavaMethods()) {
            if (jm.isInProject()) {
                methods.add(jm);
            }
        }
        return methods;
    }
    
    /**
     * Obtains the source code of the file containing this field.
     */
    public String getSource() {
        return declaringClass.getSource();
    }
    
    /**
     * Collects information about this field.
     * @return the string for printing
     */
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("FIELD: ");
        buf.append(getName());
        buf.append("@");
        buf.append(getType());
        buf.append("\n");
        
        return buf.toString();
    }
}
