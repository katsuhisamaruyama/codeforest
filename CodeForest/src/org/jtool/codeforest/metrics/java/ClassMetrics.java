/*
 *  Copyright 2014, Katsuhisa Maruyama (maru@jtool.org)
 */

package org.jtool.codeforest.metrics.java;

import org.jtool.codeforest.metrics.MetricSort;
import org.jtool.codeforest.metrics.UnsupportedMetricsException;
import org.jtool.eclipse.model.java.JavaClass;
import org.jtool.eclipse.model.java.JavaField;
import org.jtool.eclipse.model.java.JavaFile;
import org.jtool.eclipse.model.java.JavaMethod;
import org.jtool.eclipse.model.java.JavaPackage;
import org.jtool.eclipse.model.java.JavaProject;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * An object storing metric information on a class, an interface, or an enum.
 * @author Katsuhisa Maruyama
 */
public class ClassMetrics extends CommonMetrics {
    
    /**
     * The identifier that represents class metrics
     */
    public static final String Id = "ClassMetrics";
    
    /**
     * An object representing a class, an interface, or an enum.
     */
    private JavaClass jclass;
    
    /**
     * A metrics object for a package containing this class.
     */
    private PackageMetrics packageMetrics;
    
    /**
     * The collection of all method metrics for this class.
     */
    protected List<MethodMetrics> methodMetrics = new ArrayList<MethodMetrics>();
    
    /**
     * The collection of all field metrics for this class.
     */
    protected List<FieldMetrics> fieldMetrics = new ArrayList<FieldMetrics>();
    
    /**
     * The collection of names of afferent classes for this class.
     */
    protected List<String> afferentClassNames = new ArrayList<String>();
    
    /**
     * The collection of names of afferent classes for this class.
     */
    protected List<String> efferentClassNames = new ArrayList<String>();
    
    /**
     * Creates a new object representing a class, an interface, or an enum.
     * @param name the name of the class
     * @param fqn the fully-qualified name of the class
     * @param modifiers The modifiers of the class
     * @param isInterface <code>true</code> if this object represents an interface, otherwise <code>false</code>
     * @param isEnum <code>true</code> if this object represents an enum, otherwise <code>false</code>
     * @param path the path of the file containing the class
     * @param pm a metrics object for a package containing the class
     */
    public ClassMetrics(String name, String fqn, int modifiers, boolean isInterface, boolean isEnum, String path, PackageMetrics pm) {
        super();
        
        JavaProject jproject = pm.getJavaPackage().getJavaProject();
        JavaFile jfile = new JavaFile(path, jproject);
        jproject.addJavaFile(jfile);
        JavaPackage jpackage = pm.getJavaPackage();
        
        jclass = JavaClass.create(name, fqn, modifiers, isInterface, isEnum, jfile, jpackage);
        this.packageMetrics = pm;
    }
    
    /**
     * Creates a new object representing a class, an interface, or an enum.
     * @param node an AST node for this class, interface, or enum
     * @param pm a metrics object for a package containing this class, interface, or enum
     */
    public ClassMetrics(JavaClass jclass, PackageMetrics pm) {
        super();
        
        this.jclass = jclass;
        packageMetrics = pm;
        
        for (JavaMethod jm : jclass.getJavaMethods()) {
            if (jm instanceof CFJavaMethod) {
                MethodMetrics mm = new MethodMetrics((CFJavaMethod)jm, this);
                methodMetrics.add(mm);
            }
        }
        for (JavaField jf : jclass.getJavaFields()) {
            if (jf instanceof CFJavaField) {
                FieldMetrics fm = new FieldMetrics((CFJavaField)jf, this);
                fieldMetrics.add(fm);
            }
        }
        
        for (JavaClass jc : jclass.getAfferentJavaClasses()) {
            afferentClassNames.add(jc.getQualifiedName());
        }
        for (JavaClass jc : jclass.getEfferentJavaClasses()) {
            efferentClassNames.add(jc.getQualifiedName());
        }
        
        collectMetricInfo();
    }
    
    /**
     * Sets the code properties with respect to positions and line numbers of this class.
     * @param start the character index indicating where the code fragment for this class begins
     * @param len the length in characters of the code fragment for this class
     * @param upper the upper line number of code fragment for this class
     * @param bottom the bottom line number of code fragment for this class
     */
    public void setCodeProperties(int start, int len, int upper, int bottom) {
        jclass.setCodeProperties(start, len, upper, bottom);
    }
    
    /**
     * Sets the name of super class of this class.
     * @param name the name of the super class
     */
    public void setSuperClassName(String name) {
        jclass.setSuperClassName(name);
    }
    
    /**
     * Returns the name of super class of this class.
     * @return the name of the super class
     */
    public String getSuperClassName() {
        return jclass.getSuperClassName();
    }
    
    /**
     * Adds the name of super interface of this class.
     * @param name the name of the super interface
     */
    public void addSuperInterfaceName(String name) {
        jclass.addSuperInterfaceName(name);
    }
    
    /**
     * Returns the names of super interfaces of this class.
     * @return the collection of the names of the super interfaces
     */
    public Set<String> getSuperInterfaceNames() {
        return jclass.getSuperInterfaceNames();
    }
    
    /**
     * Adds the name of an afferent class for this class.
     * @param name the afferent class name
     */
    public void addAfferentClassName(String name) {
        if (!afferentClassNames.contains(name)) {
            afferentClassNames.add(name);
        }
    }
    
    /**
     * Returns the names of afferent classes for this class.
     * @return the collection of the afferent class names
     */
    public List<String> getAfferentClassNames() {
        return afferentClassNames;
    }
    
    /**
     * Adds the name of an efferent class for this class.
     * @param name the efferent class name
     */
    public void addEfferentClassName(String name) {
        if (!efferentClassNames.contains(name)) {
            efferentClassNames.add(name);
        }
    }
    
    /**
     * Returns the names of efferent classes for this class.
     * @return the collection of the efferent class names
     */
    public List<String> getEfferentClassNames() {
        return efferentClassNames;
    }
    
    /**
     * Returns the metrics object for a package containing this class.
     * @return the package metrics
     */
    public PackageMetrics getPackageMetrics() {
        return packageMetrics;
    }
    
    /**
     * Obtains the collection of method metrics for this class.
     * @return the collection of method metrics
     */
    public List<MethodMetrics> getMethodMetrics() {
        return methodMetrics;
    }
    
    /**
     * Stores a method metrics object.
     * @param pm the method metrics
     */
    public void add(MethodMetrics mm) {
        if (!methodMetrics.contains(mm)) {
            methodMetrics.add(mm);
        }
    }
    
    /**
     * Obtains the collection of field metrics for this class.
     * @return the collection of field metrics
     */
    public List<FieldMetrics> getFieldMetrics() {
        return fieldMetrics;
    }
    
    /**
     * Stores a field metrics object.
     * @param pm the field metrics
     */
    public void add(FieldMetrics fm) {
        if (!fieldMetrics.contains(fm)) {
            fieldMetrics.add(fm);
        }
    }
    
    /**
     * Returns the object representing this class.
     * @return the class object
     */
    public JavaClass getJavaClass() {
        return jclass;
    }
    
    /**
     * Returns the name of this class.
     * @return the name
     */
    public String getName() {
        return jclass.getName();
    }
    
    /**
     * Returns the qualified name of this class.
     * @return the qualified name
     */
    public String getQualifiedName() {
        return jclass.getQualifiedName();
    }
    
    /**
     * Returns the name of a package containing this class.
     * @return the name of the package
     */
    public String getPackageName() {
        return packageMetrics.getName();
    }
    
    /**
     * Returns the value representing modifiers of this class.
     * @return the modifiers value
     */
    public int getModifiers() {
        return jclass.getModifiers();
    }
    
    /**
     * Tests if this object represents an interface.
     * @return <code>true</code> if the object represents an interface, otherwise <code>false</code>
     */
    public boolean isInterface() {
        return jclass.isInterface();
    }
    
    /**
     * Tests if this object represents an enum.
     * @return <code>true</code> if the object represents an enum, otherwise <code>false</code>
     */
    public boolean isEnum() {
        return jclass.isEnum();
    }
    
    /**
     * Returns the path of a file containing this class.
     * @return the path of the file
     */
    public String getPath() {
        return jclass.getJavaFile().getPath();
    }
    
    /**
     * Collects information about this class, interface, or enum.
     * @return the string for printing
     */
    public String toString() {
        return getQualifiedName();
    }
    
    /**
     * Collects information on this class.
     */
    private void collectMetricInfo() {
        try {
            metrics.put(MetricSort.LINES_OF_CODE, new Double(jclass.getLoc()));
            metrics.put(MetricSort.NUMBER_OF_METHODS, new Double(jclass.getJavaMethods().size()));
            metrics.put(MetricSort.NUMBER_OF_FIELDS, new Double(jclass.getJavaFields().size()));
            metrics.put(MetricSort.NUMBER_OF_METHODS_AND_FIELDS, new Double(jclass.getJavaMethods().size() + jclass.getJavaFields().size()));
            metrics.put(MetricSort.NUMBER_OF_AFFERENT_CLASSES, new Double(jclass.getAfferentJavaClassesInProject().size()));
            metrics.put(MetricSort.NUMBER_OF_EFFERENT_CLASSES, new Double(jclass.getEfferentJavaClassesInProject().size()));
            
            metrics.put(MetricSort.NUMBER_OF_STATEMENTS, sum(MetricSort.NUMBER_OF_STATEMENTS));
            metrics.put(MetricSort.NUMBER_OF_CHILDREN, new Double(jclass.getChildren().size()));
            metrics.put(MetricSort.DEPTH_OF_INHERITANCE_TREE, new Double(jclass.getAllSuperClasses().size()));
            
            double nopm = 0;
            for (JavaMethod jm : jclass.getJavaMethods()) {
                if (jm.isPublic()) {
                    nopm++;
                }
            }
            metrics.put(MetricSort.NUMBER_OF_PUBLIC_METHODS, new Double(nopm));
            
            List<JavaMethod> calledMethods = new ArrayList<JavaMethod>();
            for (JavaMethod jm : jclass.getJavaMethods()) {
                for (JavaMethod m : jm.getCalledJavaMethodsInProject()) {
                    calledMethods.add(m);
                }
            }
            double rfc = jclass.getJavaMethods().size() + calledMethods.size();
            metrics.put(MetricSort.RESPONSE_FOR_CLASS, new Double(rfc));
            
            List<JavaClass> classes = new ArrayList<JavaClass>();
            collectCoupledClasses(jclass, classes);
            metrics.put(MetricSort.COUPLING_BETWEEN_OBJECTS, new Double(classes.size()));
            
            double lcom = getLCOM();
            metrics.put(MetricSort.LACK_OF_COHESION_OF_METHODS, new Double(lcom));
            
            double wmc  = 0;
            for (MethodMetrics mm : methodMetrics) {
                wmc = wmc + mm.getMetricValueWithException(MetricSort.CYCLOMATIC_COMPLEXITY);
            }
            metrics.put(MetricSort.WEIGHTED_METHODS_PER_CLASS, new Double(wmc));
            
        } catch (UnsupportedMetricsException e) {
            System.out.println(e.getMessage() + " in the class: " + getName());
        }
    }
    
    /**
     * Obtains the difference value between methods accessing disjoint sets of fields and methods sharing the same one.
     * @return the difference value (the value of LCOM) 
     */
    private int getLCOM() {
        int accessedMethods = 0;
        int cohesiveMethods = 0;
        
        ArrayList<JavaMethod> jmethods = new ArrayList<JavaMethod>(jclass.getJavaMethods());
        for (int i = 0; i < jmethods.size(); i++) {
            for (int j = i + 1; j < jmethods.size(); j++) {
                JavaMethod jm1 = jmethods.get(i);
                JavaMethod jm2 = jmethods.get(j);
                
                for (JavaField jf1 : jm1.getAccessedJavaFieldsInProject()) {
                    for (JavaField jf2 : jm2.getAccessedJavaFieldsInProject()) {
                        if (jf1.equals(jf2)) {
                            cohesiveMethods++;
                        } else {
                            accessedMethods++;
                        }
                    }
                }
            }
        }
        
        if (accessedMethods > cohesiveMethods) {
            return accessedMethods - cohesiveMethods;
        }
        return 0;
    }
    
    /**
     * Obtains all classes coupled to a given class.
     * @param jc the originating class
     * @param classes the collection of the coupled classes
     */
    private void collectCoupledClasses(JavaClass jc, List<JavaClass> classes) {
        for (JavaClass c : jc.getAfferentJavaClassesInProject()) {
            if (!classes.contains(c)) {
                classes.add(c);
                collectCoupledClasses(c, classes);
            }
        }
    }
    
    /**
     * Obtains the sum of values with respect to a metric with a given name.
     * @param sort the metric name
     * @return the sum of the values
     * @throws UnsupportedMetricsException if the metric value is not obtained
     */
    private Double sum(String sort) throws UnsupportedMetricsException {
        double value = 0;
        for (MethodMetrics mm : methodMetrics) {
            value = value + mm.getMetricValueWithException(sort);
        }
        for (FieldMetrics fm : fieldMetrics) {
            value = value + fm.getMetricValueWithException(sort);
        }
        return new Double(value);
    }
    
    /**
     * Collects information on this class, which is related to total metric values.
     */
    protected void collectMetricInfoForTotal() {
        double totalLOC = 0;
        double totalNOST = 0;
        double totalNOPT = 0;
        double totalCC = 0;
        double totalMNON = 0;
        double totalNOAMD = 0;
        double totalNOEMD = 0;
        
        for (MethodMetrics mm : getMethodMetrics()) {
            try {
                totalLOC = totalLOC + mm.getMetricValueWithException(MetricSort.LINES_OF_CODE);
                totalNOST = totalNOST + mm.getMetricValueWithException(MetricSort.NUMBER_OF_STATEMENTS);
                totalNOPT = totalNOPT + mm.getMetricValueWithException(MetricSort.NUMBER_OF_PARAMETERS);
                totalCC = totalCC + mm.getMetricValueWithException(MetricSort.CYCLOMATIC_COMPLEXITY);
                totalMNON = totalMNON + mm.getMetricValueWithException(MetricSort.MAX_NUMBER_OF_NESTING);
                totalNOAMD = totalNOAMD + mm.getMetricValueWithException(MetricSort.NUMBER_OF_AFFERENT_METHODS);
                totalNOEMD = totalNOEMD + mm.getMetricValueWithException(MetricSort.NUMBER_OF_EFFERENT_METHODS);
            } catch (UnsupportedMetricsException e) {
                System.out.println(e.getMessage() + " in the class: " + getName());
            }
        }
        
        putMetricValue(MetricSort.TOTAL_LINE_OF_CODE, totalLOC);
        putMetricValue(MetricSort.TOTAL_NUMBER_OF_STATEMENTS, totalNOST);
        putMetricValue(MetricSort.TOTAL_NUMBER_OF_PARAMETERS, totalNOPT);
        putMetricValue(MetricSort.TOTAL_CYCLOMATIC_COMPLEXITY, totalCC);
        putMetricValue(MetricSort.TOTAL_MAX_NUMBER_OF_NESTING, totalMNON);
        putMetricValue(MetricSort.TOTAL_NUMBER_OF_AFFERENT_METHODS, totalNOAMD);
        putMetricValue(MetricSort.TOTAL_NUMBER_OF_EFFERENT_METHODS, totalNOEMD);
    }
    
    /**
     * Collects information on this class, which is related to maximum metric values.
     */
    protected void collectMetricInfoForMax() {
        double maxLOC = 0;
        double maxNOST = 0;
        double maxNOPT = 0;
        double maxCC = 0;
        double maxMNON = 0;
        double maxNOAMD = 0;
        double maxNOEMD = 0;
        
        for (MethodMetrics mm : getMethodMetrics()) {
            try {
                maxLOC = Math.max(maxLOC, mm.getMetricValueWithException(MetricSort.LINES_OF_CODE));
                maxNOST = Math.max(maxNOST, mm.getMetricValueWithException(MetricSort.NUMBER_OF_STATEMENTS));
                maxNOPT = Math.max(maxNOPT, mm.getMetricValueWithException(MetricSort.NUMBER_OF_PARAMETERS));
                maxCC = Math.max(maxCC, mm.getMetricValueWithException(MetricSort.CYCLOMATIC_COMPLEXITY));
                maxMNON = Math.max(maxMNON, mm.getMetricValueWithException(MetricSort.MAX_NUMBER_OF_NESTING));
                maxNOAMD = Math.max(maxNOAMD, mm.getMetricValueWithException(MetricSort.NUMBER_OF_AFFERENT_METHODS));
                maxNOEMD = Math.max(maxNOEMD, mm.getMetricValueWithException(MetricSort.NUMBER_OF_EFFERENT_METHODS));
            } catch (UnsupportedMetricsException e) {
                System.out.println(e.getMessage() + " in the class: " + getName());
            }
        }
        
        putMetricValue(MetricSort.MAX_LINE_OF_CODE, maxLOC);
        putMetricValue(MetricSort.MAX_NUMBER_OF_STATEMENTS, maxNOST);
        putMetricValue(MetricSort.MAX_NUMBER_OF_PARAMETERS, maxNOPT);
        putMetricValue(MetricSort.MAX_CYCLOMATIC_COMPLEXITY, maxCC);
        putMetricValue(MetricSort.MAX_MAX_NUMBER_OF_NESTING, maxMNON);
        putMetricValue(MetricSort.MAX_NUMBER_OF_AFFERENT_METHODS, maxNOAMD);
        putMetricValue(MetricSort.MAX_NUMBER_OF_EFFERENT_METHODS, maxNOEMD);
    }
    
    /**
     * Sorts the method metrics in dictionary order of their names.
     */
    public void sortMethods() {
        Collections.sort(methodMetrics, new Comparator<MethodMetrics>() {
            
            public int compare(MethodMetrics m1, MethodMetrics m2) {
                return m1.getSignature().compareTo(m2.getSignature());
            }
        });
    }
    
    /**
     * Sorts the field metrics in dictionary order of their names.
     */
    public void sortFields() {
        Collections.sort(fieldMetrics, new Comparator<FieldMetrics>() {
            
            public int compare(FieldMetrics m1, FieldMetrics m2) {
                return m1.getName().compareTo(m2.getName());
            }
        });
    }
}
