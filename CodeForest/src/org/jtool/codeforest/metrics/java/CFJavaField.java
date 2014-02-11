/*
 *  Copyright 2014, Katsuhisa Maruyama (maru@jtool.org)
 */

package org.jtool.codeforest.metrics.java;

import org.jtool.eclipse.model.java.JavaClass;
import org.jtool.eclipse.model.java.JavaField;
import org.eclipse.jdt.core.dom.EnumConstantDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclaration;

/**
 * An object representing a field or an enum constant.
 * @author Katsuhisa Maruyama
 */
public class CFJavaField extends JavaField {
    
    /**
     * Creates a new, empty object.
     */
    protected CFJavaField() {
        super();
    }
    
    /**
     * Creates a new object representing a field.
     * @param node an AST node for this field
     * @param jc the class declaring this field
     */
    public CFJavaField(VariableDeclaration node, JavaClass jc) {
        super(node, jc);
    }
    
    /**
     * Creates a new object representing an enum constant.
     * @param node an AST node for this enum constant
     * @param decl an AST node for this field declaration
     */
    public CFJavaField(EnumConstantDeclaration node, JavaClass jc) {
        super(node, jc);
    }
    
    /**
     * Creates a new object representing a field.
     * @param name the name of this field
     * @param type the type of this field
     * @param modifiers the modifiers of this field
     * @param isEnumConstant <code>true> if this field is an enum constant, otherwise <code>false</code>
     * @param jc the class declaring this field
     */
    public CFJavaField(String name, String type, int modifiers, boolean isEnumConstant, JavaClass jc) {
        super(name, type, modifiers, isEnumConstant, jc);
    }
    
    /**
     * Tests if a given field equals to this.
     * @param jf the Java field
     * @return <code>true</code> if the given field equals to this, otherwise <code>false</code>
     */
    public boolean equals(JavaField jf) {
        return super.equals(jf);
    }
    
    /**
     * Returns a hash code value for this field.
     * @return the hash code value for the field
     */
    public int hashCode() {
        return super.hashCode();
    }
}
