/*
 *  Copyright 2013, Katsuhisa Maruyama (maru@jtool.org)
 */

package org.jtool.codeforest.java;

import org.jtool.codeforest.java.internal.ExternalJavaClass;
import org.jtool.codeforest.java.internal.TypeCollector;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.AnonymousClassDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.HashMap;

/**
 * An object representing a class, an interface, or an enum.
 * @author Katsuhisa Maruyama
 */
public class JavaClass extends JavaElement {
    
    /**
     * A flag that indicates if this class requires reconstruction.
     */
    protected boolean isDirty;
    
    /**
     * The cache for all objects of classes.
     */
    protected static HashMap<String, JavaClass> cache = new HashMap<String, JavaClass>();
    
    /**
     * The type binding for this class.
     */
    private ITypeBinding binding;
    
    /**
     * The name of this class.
     */
    protected String name;
    
    /**
     * The fully-qualified name of this class.
     */
    protected String fqn;
    
    /**
     * The modifiers of this class.
     */
    private int modifiers;
    
    /**
     * A flag indicating if this object represents an interface.
     */
    private boolean isInterface;
    
    /**
     * A flag indicating if this object represents an enum.
     */
    private boolean isEnum;
    
    /**
     * The name of super class for this class.
     */
    protected String superClassName;
    
    /**
     * The names of super interfaces for this class.
     */
    protected Set<String> superInterfaceNames = new HashSet<String>();
    
    /**
     * A file which this class is written in.
     */
    protected JavaFile jfile;
    
    /**
     * A package containing this class.
     */
    protected JavaPackage jpackage;
    
    /**
     * The collection of all fields within this class.
     */
    protected Set<JavaField> fields = new HashSet<JavaField>();
    
    /**
     * The collection of all methods within this class.
     */
    protected Set<JavaMethod> methods = new HashSet<JavaMethod>();
    
    /**
     * The collection of all classes within this class.
     */
    protected Set<JavaClass> innerClasses = new HashSet<JavaClass>();
    
    /**
     * Creates a new, empty object.
     */
    protected JavaClass() {
        super();
    }
    
    /**
     * Creates a new object representing a class.
     * @param node the AST node for this class
     * @param binding the type binding for this class
     * @param jp the package containing this class
     */
    private JavaClass(ASTNode node, ITypeBinding binding, JavaPackage jp) {
        super(node);
        
        this.binding = binding;
        jpackage = jp;
        
        if (binding != null) {
            name = binding.getName();
            fqn = binding.getQualifiedName();
            modifiers = binding.getModifiers();
            isInterface = binding.isInterface();
            isEnum = binding.isEnum();
            if (binding.getSuperclass() != null) {
                superClassName = binding.getSuperclass().getQualifiedName();
            }
            for (ITypeBinding type : binding.getInterfaces()) {
                superInterfaceNames.add(type.getQualifiedName());
            }
        } else {
            name = ".UNKNOWN";
            fqn = ".UNKNOWN";
            modifiers = 0;
            isInterface = false;
            isEnum = false;
        }
    }
    
    /**
     * Creates a new object representing a class.
     * @param name the name of this class.
     * @param fqn the fully-qualified name of this class
     * @param modifiers The modifiers of this class
     * @param isInterface <code>true</code> if this object represents an interface, otherwise <code>false</code>
     * @param isEnum <code>true</code> if this object represents an enum, otherwise <code>false</code>
     * @param superClassName the name of super class for this class
     * @param superInterfacesNames the names of super interfaces for this class
     * @param jfile the file which this class is written in
     * @param jpackage the package containing this class
     */
    private JavaClass(String name, String fqn, int modifiers, boolean isInterface, boolean isEnum,
                      JavaFile jfile, JavaPackage jpackage) {
        super();
        
        this.name = name;
        this.fqn = fqn;
        this.modifiers = modifiers;
        this.isInterface = isInterface;
        this.isEnum = isEnum;
        this.jpackage = jpackage;
        this.jfile = jfile;
    }
    
    /**
     * Creates a new object representing a class.
     * @param node an AST node for this class
     * @param jp the package containing this class
     * @return the created object
     */
    public static JavaClass create(TypeDeclaration node, JavaPackage jp) {
        return create(node, node.resolveBinding(), jp);
    }
    
    /**
     * Creates a new object representing an anonymous class.
     * @param node an AST node for this class
     * @param jp the package containing this class
     * @return the created object
     */
    public static JavaClass create(AnonymousClassDeclaration node, JavaPackage jp) {
        return create(node, node.resolveBinding(), jp);
    }
    
    /**
     * Creates a new object representing an enum.
     * @param node an AST node for this enum
     * @param jp the package containing this enum
     * @return the created object
     */
    public static JavaClass create(EnumDeclaration node, JavaPackage jp) {
        return create(node, node.resolveBinding(), jp);
    }
    
    /**
     * Creates a new object representing a class.
     * @param node an AST node for this class
     * @param bind the type binding for this class
     * @param jp the package containing this class
     * @return the created object, or <code>null</code> if the creation fails
     */
    private static JavaClass create(ASTNode node, ITypeBinding bind, JavaPackage jp) {
        String fqn = bind.getQualifiedName();
        JavaClass jclass = cache.get(fqn);
        if (jclass != null) {
            return jclass;
        }
        
        jclass = new JavaClass(node, bind, jp);
        jp.addJavaClass(jclass);
        cache.put(fqn, jclass);
        return jclass;
    }
    
    /**
     * Creates a new object representing a class.
     * @param name the name of this class.
     * @param fqn the fully-qualified name of this class
     * @param modifiers The modifiers of this class
     * @param isInterface <code>true</code> if this object represents an interface, otherwise <code>false</code>
     * @param isEnum <code>true</code> if this object represents an enum, otherwise <code>false</code>
     * @param superClassName the name of super class for this class
     * @param superInterfacesNames the names of super interfaces for this class
     * @param jfile the file which this class is written in
     * @param jp the package containing this class
     */
    public static JavaClass create(String name, String fqn, int modifiers, boolean isInterface, boolean isEnum,
                      JavaFile jfile, JavaPackage jp) {
        JavaClass jclass = cache.get(fqn);
        if (jclass != null) {
            return jclass;
        }
        
        jclass = new JavaClass(name, fqn, modifiers, isInterface, isEnum, jfile, jp);
        jp.addJavaClass(jclass);
        cache.put(fqn, jclass);
        return jclass;
    }
    
    /**
     * Tests if this class exists in the project.
     * @return always <code>true</code>
     */
    public boolean isInProject() {
        return true;
    }
    
    /**
     * Tests if this class should be reconstructed.
     * @param bool <code>true</code> if this class should be reconstructed
     */
    public boolean isDirty() {
        return isDirty;
    }
    
    /**
     * Sets the name of super class of this class.
     * @param name the name of the super class
     */
    public void setSuperClassName(String name) {
        superClassName = name;
    }
    
    /**
     * Returns the name of super class of this class.
     * @return the name of the super class
     */
    public String getSuperClassName() {
        return superClassName;
    }
    
    /**
     * Adds the name of super interface of this class.
     * @param name the name of the super interface
     */
    public void addSuperInterfaceName(String name) {
        superInterfaceNames.add(name);
    }
    
    /**
     * Returns the names of super interfaces of this class.
     * @return the collection of the names of the super interfaces
     */
    public Set<String> getSuperInterfaceNames() {
        return superInterfaceNames;
    }
    
    /**
     * Returns the type binding for this class or interface.
     * @return the type binding
     */
    public ITypeBinding getBinding() {
        return binding;
    }
    
    /**
     * Returns an object corresponding to a specified class name.
     * @param fqn the fully qualified name of a class to be retrieved
     * @return the found object, or <code>null</code> if none
     */
    public static JavaClass getJavaClass(String fqn) {
        if (fqn != null && fqn.length() != 0) {
            return cache.get(fqn);
        }
        return null;
    }
    
    /**
     * Returns all the classes stored in the cache.
     * @return the collection of the stored classes
     */
    public static Set<JavaClass> getAllClassesInCache() {
        Set<JavaClass> jclasses = new HashSet<JavaClass>();
        for (JavaClass jc : cache.values()) {
            jclasses.add(jc);
        }
        return jclasses;
    }
    
    /**
     * Clears information about all classes stored in the cache. 
     */
    public static void clearAll() {
        cache.clear();
    }
    
    /**
     * Returns the name of this class.
     * @return the name
     */
    public String getName() {
        return name;
    }
    
    /**
     * Returns the fully-qualified name of this class.
     * @return the fully-qualified name
     */
    public String getQualifiedName() {
        return fqn;
    }
    
    /**
     * Returns the value representing modifiers of this class.
     * @return the modifiers value
     */
    public int getModifiers() {
        return modifiers;
    }
    
    /**
     * Sets the file which this class is written in.
     * @param jfile the file
     */
    public void setJavaFile(JavaFile jfile) {
        this.jfile = jfile;
    }
    
    /**
     * Returns the file which this class is written in.
     * @return the file
     */
    public JavaFile getJavaFile() {
        return jfile;
    }
    
    /**
     * Returns the package containing this class.
     * @return the package
     */
    public JavaPackage getJavaPackage() {
        return jpackage;
    }
    
    /**
     * Tests if this object represents a normal class.
     * @return <code>true</code> if the object represents a class, otherwise <code>false</code>
     */
    public boolean isClass() {
        return !isInterface && !isEnum;
    }
    
    /**
     * Tests if this object represents an interface.
     * @return <code>true</code> if the object represents an interface, otherwise <code>false</code>
     */
    public boolean isInterface() {
        return isInterface;
    }
    
    /**
     * Tests if this object represents an enum.
     * @return <code>true</code> if the object represents an enum, otherwise <code>false</code>
     */
    public boolean isEnum() {
        return isEnum;
    }
    
    /**
     * Tests if the access setting of this class is public.
     * @return <code>true</code> if this is a public class, otherwise <code>false</code>
     */
    public boolean isPublic() {
        return Modifier.isPublic(modifiers);
    }
    
    /**
     * Tests if the access setting of this class is protected.
     * @return <code>true</code> if this is a protected class, otherwise <code>false</code>
     */
    public boolean isProtected() {
        return Modifier.isProtected(modifiers);
    }
    
    /**
     * Tests if the access setting of this class is private.
     * @return <code>true</code> if this is a private class, otherwise <code>false</code>
     */
    public boolean isPrivate() {
        return Modifier.isPrivate(modifiers);
    }
    
    /**
     * Tests if the access setting of this class has default visibility.
     * @return <code>true</code> if this is a class with default visibility, otherwise <code>false</code>
     */
    public boolean isDefault() {
        return !isPublic() && !isProtected() && !isPrivate();
    }
    
    /**
     * Tests if the access setting of this class is final.
     * @return <code>true</code> if this is a final class, otherwise <code>false</code>
     */
    public boolean isFinal() {
        return Modifier.isFinal(modifiers);
    }
    
    /**
     * Tests if the access setting of this class is abstract.
     * @return <code>true</code> if this is an abstract class, otherwise <code>false</code>
     */
    public boolean isAbstract() {
        return Modifier.isAbstract(modifiers);
    }
    
    /**
     * Tests if the access setting of this class is static.
     * @return <code>true</code> if this is a static class, otherwise <code>false</code>
     */
    public boolean isStatic() {
        return Modifier.isStatic(modifiers);
    }
    
    /**
     * Tests if the access setting of this class is strictfp.
     * @return <code>true</code> if this is a strictfp class, otherwise <code>false</code>
     */
    public boolean isStrictfp() {
        return Modifier.isStrictfp(modifiers);
    }
    
    /**
     * Adds a field to the members of this class.
     * @param jf the field to be added
     */
    public void addJavaField(JavaField jf) {
        if (!fields.contains(jf)) {
            fields.add(jf);
        }
    }
    
    /**
     * Returns all the fields within this class.
     * @return the collection of the fields declared in this class
     */
    public Set<JavaField> getJavaFields() {
        return fields;
    }
    
    /**
     * Returns the field having a specified name.
     * @param name the name of the field to be retrieved
     * @return the found field, or <code>null</code> if none
     */
    public JavaField getJavaField(String name) {
        for (JavaField jf : fields) {
            if (jf.getName().compareTo(name) == 0) {
                return jf;
            }
        }
        return null;
    }
    
    /**
     * Adds a method to the members of this class.
     * @param jm the method to be added
     */
    public void addJavaMethod(JavaMethod jm) {
        if (!methods.contains(jm)) {
            methods.add(jm);
        }
    }
    
    /**
     * Returns all the methods within this class.
     * @return the collection of the methods declared in this class
     */
    public Set<JavaMethod> getJavaMethods() {
        return methods;
    }
    
    /**
     * Returns a method having a specified signature.
     * @param sig the signature of the method to be retrieved
     * @return the found method, or <code>null</code> if none
     */
    public JavaMethod getJavaMethod(String sig) {
        for (JavaMethod jm : methods) {
            if (jm.getSignature().compareTo(sig) == 0) {
                return jm;
            }
        }
        return null;
    }
    
    /**
     * Adds a class to the members of this class.
     * @param jm the class to be added
     */
    public void addJavaInnerClass(JavaClass jc) {
        if (!innerClasses.contains(jc)) {
            innerClasses.add(jc);
        }
    }
    
    /**
     * Returns all the classes within this class.
     * @return the collection of the classes declared in this class
     */
    public Set<JavaClass> getJavaInnerClasses() {
        return innerClasses;
    }
    
    /**
     * Tests if a given class equals to this.
     * @param jc the class
     * @return <code>true</code> if the given class equals to this, otherwise <code>false</code>
     */
    public boolean equals(JavaClass jc) {
        if (jc == null) {
            return false;
        }
        
        return this == jc || getQualifiedName().compareTo(jc.getQualifiedName()) == 0; 
    }
    
    /**
     * Returns a hash code value for this class.
     * @return the hash code value for the class
     */
    public int hashCode() {
        return getQualifiedName().hashCode();
    }
    
    /* ================================================================================
     * The following functionalities can be used after completion of whole analysis 
     * ================================================================================ */
    
    /**
     * A class declaring this class.
     */
    protected JavaClass declaringClass = null;
    
    /**
     * A method declaring this class.
     */
    protected JavaMethod declaringMethod = null;
    
    /**
     * The super class of this class.
     */
    private JavaClass superClass;
    
    /**
     * The list of super interfaces of this class.
     */
    private Set<JavaClass> superInterfaces = new HashSet<JavaClass>();
    
    /**
     * The collection of all classes that depend on this class.
     */
    protected Set<JavaClass> afferentClasses = new HashSet<JavaClass>();
    
    /**
     * The collection of all classes that this class depends on.
     */
    protected Set<JavaClass> efferentClasses = new HashSet<JavaClass>();
    
    /**
     * A flag that indicates all bindings for types, methods, and variables were found.
     */
    protected boolean bindingOk = true;
    
    /**
     * Collects additional information on this class.
     */
    public void collectLevel2Info() {
        if (binding != null) {
            declaringClass = getDeclaringJavaClass(binding.getDeclaringClass());
            declaringMethod = getDeclaringJavaMethod(binding.getDeclaringMethod());
            findSuperClass(binding);
            findSuperInterfaces(binding);
        }
        
        if (astNode != null) {
            TypeCollector tvisitor = new TypeCollector();
            astNode.accept(tvisitor);
            efferentClasses = tvisitor.getTypeUses();
            for (JavaClass jc : efferentClasses) {
                jc.addAfferentClass(this);
            }
            
            bindingOk = tvisitor.isBindingOk();
        }
    }
    
    /**
     * Tests if the binding for this class was found.
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
     * Returns the Java class that declares this class.
     * @return the class that declares this class, or <code>null</code> if none
     */
    public JavaClass getDeclaringJavaClass() {
        bindingCheck();
        return declaringClass;
    }
    
    /**
     * Returns the Java method that declares this class.
     * @return the method that declares this class, or <code>null</code> if none
     */
    public JavaMethod getDeclaringJavaMethod() {
        bindingCheck();
        return declaringMethod;
    }
    
    /**
     * Finds a super class this class directly extends.
     * @param binding the type binding for this class
     */
    private void findSuperClass(ITypeBinding binding) {
        if (isClass()) {
            String name = binding.getSuperclass().getQualifiedName();
            JavaClass jc = cache.get(name);
            if (jc != null) {
                superClass = jc;
            }
        }
        superClass = ExternalJavaClass.create(name);
        
    }
    
    /**
     * Returns the super class that this class directly extends.
     * @return the super class
     */
    public JavaClass getSuperClass() {
        bindingCheck();
        return superClass;
    }
    
    /**
     * Finds super interfaces that this class directly implements.
     * @param binding the type binding for this class
     */
    private void findSuperInterfaces(ITypeBinding binding) {
        for (ITypeBinding type : binding.getInterfaces()) {
            String name = type.getQualifiedName();
            JavaClass jc = cache.get(name);
            if (jc != null) {
                superInterfaces.add(jc);
            } else {
                superInterfaces.add(ExternalJavaClass.create(name));
            }
        }
    }
    
    /**
     * Returns the super interfaces that this class directly implements.
     * @return the collection of the super interfaces, or an empty collection if none
     */
    public Set<JavaClass> getSuperInterfaces() {
        bindingCheck();
        return superInterfaces;
    }
    
    /**
     * Returns all the subclasses and sub-interfaces that extends or implements this class.
     * @return the collection of the subclasses and sub-interfaces
     */
    public List<JavaClass> getChildren() {
        bindingCheck();
        
        List<JavaClass> classes = new ArrayList<JavaClass>(); 
        for (JavaClass jc : getAllClassesInCache()) {
            if (jc.isChildOf(this)) {
                classes.add(jc);
            }
        }
        return classes;
    }
    
    /**
     * Test if this class is a direct child of a specified class.
     * @param jc the superclass or super interface
     * @return <code>true</code> if this class is a child of the specified class, otherwise <code>false</code>
     */
    public boolean isChildOf(JavaClass jc) {
        bindingCheck();
        
        if (superClass.getQualifiedName().compareTo(jc.getQualifiedName()) == 0) {
            return true;
        }
        for (JavaClass c : getSuperInterfaces()) {
            if (c.getQualifiedName().compareTo(jc.getQualifiedName()) == 0) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Returns all the super classes of this class.
     * @return the collection of the super classes in bottom-up order.
     */
    public List<JavaClass> getAllSuperClasses() {
        bindingCheck();
        
        List<JavaClass> classes = new ArrayList<JavaClass>();
        JavaClass parent = getSuperClass();
        while (parent != null) {
            classes.add(parent);
            parent = parent.getSuperClass();
        }
        return classes;
    }
    
    /**
     * Returns all the super interfaces of this class.
     * @return the collection of the super interfaces in no particular order.
     */
    public List<JavaClass> getAllSuperInterfaces() {
        bindingCheck();
        
        List<JavaClass> classes = new ArrayList<JavaClass>();
        getAllSuperInterfaces(this, classes);
        return classes;
    }
    
    /**
     * Collects all super interfaces of a given class.
     * @param jc the base class
     * @param classes the collection of the super interfaces
     */
    private void getAllSuperInterfaces(JavaClass jc, List<JavaClass> classes) {
        bindingCheck();
        
        for (JavaClass parent : jc.getSuperInterfaces()) {
            classes.add(parent);
            getAllSuperInterfaces(parent, classes);
        }
    }
    
    /**
     * Collects all children of a given class.
     * @param jc the base class
     * @param classes the collection of the children
     */
    private void getAllChildren(JavaClass jc, List<JavaClass> classes) {
        bindingCheck();
        
        for (JavaClass child : jc.getChildren()) {
            classes.add(child);
            getAllChildren(child, classes);
        }
    }
    
    /**
     * Returns all the ancestors of this class.
     * @return the collection of the ancestors in no particular order.
     */
    public List<JavaClass> getAncestors() {
        bindingCheck();
        
        List<JavaClass> classes = new ArrayList<JavaClass>();
        classes.addAll(getAllSuperClasses());
        classes.addAll(getAllSuperInterfaces());
        return classes;
    }
    
    /**
     * Returns all the descendants of this class.
     * @return the collection of the descendants in no particular order.
     */
    public List<JavaClass> getDescendants() {
        bindingCheck();
        
        List<JavaClass> classes = new ArrayList<JavaClass>();
        getAllChildren(this, classes);
        return classes;
    }
    
    /**
     * Returns all the classes that depend on this class.
     * @return the collection of the afferent classes 
     */
    public Set<JavaClass> getAfferentJavaClasses() {
        bindingCheck();
        return afferentClasses;
    }
    
    /**
     * Returns all the classes that depend on this class.
     * @return the collection of the classes in the project
     */
    public Set<JavaClass> getAfferentJavaClassesInProject() {
        bindingCheck();
        
        Set<JavaClass> classes = new HashSet<JavaClass>();
        for (JavaClass jc : getAfferentJavaClasses()) {
            if (jc.isInProject()) {
                classes.add(jc);
            }
        }
        return classes;
    }
    
    /**
     * Adds a class that depends on this class.
     * @param jc the afferent class
     */
    private void addAfferentClass(JavaClass jc) {
        if (!afferentClasses.contains(jc)) {
            afferentClasses.add(jc);
        }
    }
    
    /**
     * Returns all the classes that this class depends on.
     * @return the collection of the efferent classes 
     */
    public Set<JavaClass> getEfferentJavaClasses() {
        bindingCheck();
        return efferentClasses;
    }
    
    /**
     * Returns all the classes that his class depends on.
     * @return the collection of the efferent classes in the project
     */
    public Set<JavaClass> getEfferentJavaClassesInProject() {
        bindingCheck();
        
        Set<JavaClass> classes = new HashSet<JavaClass>();
        for (JavaClass jc : getEfferentJavaClasses()) {
            if (jc.isInProject()) {
                classes.add(jc);
            }
        }
        return classes;
    }
    
    /**
     * Extracts the method overrides a given method from the methods within this class.
     * @param jm the overridden method 
     * @return the found method, or <code>null</code> if none
     */
    public Set<JavaMethod> getOverridingJavaMethod() {
        Set<JavaMethod> oms = new HashSet<JavaMethod>();
        for (JavaMethod jm : methods) {
            if (jm.getOverriddenJavaMethods().size() != 0) {
                oms.add(jm);
            }
        }
        return oms;
    }
    
    /**
     * Obtains the source code of the file containing this class.
     */
    public String getSource() {
        return jfile.getCurrentCode();
    }
    
    /**
     * Collects information about this class or interface.
     * @return the string for printing
     */
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("CLASS: ");
        buf.append(getName());
        buf.append("\n");
        if (isClass()) {
            buf.append(" EXTENDS: ");
            buf.append(superClassName);
            buf.append("\n");
        }
        buf.append(" IMPLEMENTS:");
        for (String name :  superInterfaceNames) {
            buf.append(" " + name);
        }
        buf.append("\n");
        
        buf.append(getFieldInfo());
        buf.append(getMethodInfo());
        
        return buf.toString();
    }
    
    /**
     * Collects information about all fields defined within this class.
     * @return the string for printing
     */
    public String getFieldInfo() {
        StringBuffer buf = new StringBuffer();
        for (JavaField jf : getJavaFields()) {
            buf.append(jf.toString());
        }
        
        return buf.toString();
    }
    
    /**
     * Collects information about all methods defined within this class.
     * @return the string for printing
     */
    public String getMethodInfo() {
        StringBuffer buf = new StringBuffer();
        for (JavaMethod jm : getJavaMethods()) {
            buf.append(jm.toString());
        }
        
        return buf.toString();
    }
    
    /**
     * Collects information about all classes defined within this class.
     * @return the string for printing
     */
    public String getClassInfo() {
        StringBuffer buf = new StringBuffer();
        for (JavaClass jc : getJavaInnerClasses()) {
            buf.append(jc.toString());
        }
        
        return buf.toString();
    }
}
