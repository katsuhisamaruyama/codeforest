/*
 *  Copyright 2013, Katsuhisa Maruyama (maru@jtool.org)
 */

package org.jtool.codeforest.metrics;

/**
 * Defines the sort of metrics and their abbreviations.
 * @author Katsuhisa Maruyama
 */
public interface MetricSort {
    public static final String LINES_OF_CODE = "LOC";
    public static final String NUMBER_OF_STATEMENTS = "NOST";
    
    public static final String NUMBER_OF_FILES = "NOFL";
    public static final String NUMBER_OF_PACKAGES = "NOPG";
    public static final String NUMBER_OF_CLASSES = "NOCL";
    public static final String NUMBER_OF_METHODS = "NOMD";
    public static final String NUMBER_OF_FIELDS = "NOFD";
    public static final String NUMBER_OF_METHODS_AND_FIELDS = "NOMDFD";
    
    public static final String COUPLING_BETWEEN_OBJECTS = "CBO";
    public static final String DEPTH_OF_INHERITANCE_TREE = "DIT";
    public static final String NUMBER_OF_CHILDREN = "NOC";
    public static final String RESPONSE_FOR_CLASS = "RFC";
    public static final String WEIGHTED_METHODS_PER_CLASS = "WMC";
    public static final String LACK_OF_COHESION_OF_METHODS = "LCOM";
    public static final String NUMBER_OF_PUBLIC_METHODS = "NOPM";
    
    public static final String NUMBER_OF_PARAMETERS = "NOPT";
    public static final String CYCLOMATIC_COMPLEXITY = "CC";
    public static final String MAX_NUMBER_OF_NESTING = "MNON";
    
    public static final String NUMBER_OF_AFFERENT_PACKAGES = "NOAPG";
    public static final String NUMBER_OF_EFFERENT_PACKAGES = "NOEPG";
    public static final String NUMBER_OF_AFFERENT_CLASSES = "NOACL";
    public static final String NUMBER_OF_EFFERENT_CLASSES = "NOECL";
    public static final String NUMBER_OF_AFFERENT_METHODS = "NOAMD";
    public static final String NUMBER_OF_EFFERENT_METHODS = "NOEMD";
    public static final String NUMBER_OF_AFFERENT_FIELDS = "NOAFD";
    public static final String NUMBER_OF_EFFERENT_FIELDS = "NOEFD";
    
    public static final String TOTAL_LINE_OF_CODE = "TOTAL_LOC";
    public static final String TOTAL_NUMBER_OF_STATEMENTS = "TOTAL_NOST";
    
    public static final String TOTAL_NUMBER_OF_FILES = "TOTAL_NOFL";
    public static final String TOTAL_NUMBER_OF_PACKAGES = "TOTAL_NOPG";
    public static final String TOTAL_NUMBER_OF_CLASSES = "TOTAL_NOCL";
    public static final String TOTAL_NUMBER_OF_METHODS = "TOTAL_NOMD";
    public static final String TOTAL_NUMBER_OF_FIELDS = "TOTAL_NOFD";
    public static final String TOTAL_NUMBER_OF_METHODS_AND_FIELDS = "TOTAL_NOMDFD";
    
    public static final String TOTAL_COUPLING_BETWEEN_OBJECTS = "TOTAL_CBO";
    public static final String TOTAL_DEPTH_OF_INHERITANCE_TREE = "TOTAL_DIT";
    public static final String TOTAL_NUMBER_OF_CHILDREN = "TOTAL_NOC";
    public static final String TOTAL_RESPONSE_FOR_CLASS = "TOTAL_RFC";
    public static final String TOTAL_WEIGHTED_METHODS_PER_CLASS = "TOTAL_WMC";
    public static final String TOTAL_LACK_OF_COHESION_OF_METHODS = "TOTAL_LCOM";
    public static final String TOTAL_NUMBER_OF_PUBLIC_METHODS = "TOTAL_NOPM";
    
    public static final String TOTAL_NUMBER_OF_PARAMETERS = "TOTAL_NOPT";
    public static final String TOTAL_CYCLOMATIC_COMPLEXITY = "TOTAL_CC";
    public static final String TOTAL_MAX_NUMBER_OF_NESTING = "TOTAL_MNON";
    
    public static final String TOTAL_NUMBER_OF_AFFERENT_PACKAGES = "TOTAL_NOAPG";
    public static final String TOTAL_NUMBER_OF_EFFERENT_PACKAGES = "TOTAL_NOEPG";
    public static final String TOTAL_NUMBER_OF_AFFERENT_CLASSES = "TOTAL_NOACL";
    public static final String TOTAL_NUMBER_OF_EFFERENT_CLASSES = "TOTAL_NOECL";
    public static final String TOTAL_NUMBER_OF_AFFERENT_METHODS = "TOTAL_NOAMD";
    public static final String TOTAL_NUMBER_OF_EFFERENT_METHODS = "TOTAL_NOEMD";
    public static final String TOTAL_NUMBER_OF_AFFERENT_FIELDS = "TOTAL_NOAFD";
    public static final String TOTAL_NUMBER_OF_EFFERENT_FIELDS = "TOTAL_NOEFD";
    
    public static final String MAX_LINE_OF_CODE = "MAX_LOC";
    public static final String MAX_NUMBER_OF_STATEMENTS = "MAX_NOST";
    
    public static final String MAX_NUMBER_OF_FILES = "MAX_NOFL";
    public static final String MAX_NUMBER_OF_PACKAGES = "MAX_NOPG";
    public static final String MAX_NUMBER_OF_CLASSES = "MAX_NOCL";
    public static final String MAX_NUMBER_OF_METHODS = "MAX_NOMD";
    public static final String MAX_NUMBER_OF_FIELDS = "MAX_NOFD";
    public static final String MAX_NUMBER_OF_METHODS_AND_FIELDS = "MAX_NOMDFD";
    
    public static final String MAX_COUPLING_BETWEEN_OBJECTS = "MAX_CBO";
    public static final String MAX_DEPTH_OF_INHERITANCE_TREE = "MAX_DIT";
    public static final String MAX_NUMBER_OF_CHILDREN = "MAX_NOC";
    public static final String MAX_RESPONSE_FOR_CLASS = "MAX_RFC";
    public static final String MAX_WEIGHTED_METHODS_PER_CLASS = "MAX_WMC";
    public static final String MAX_LACK_OF_COHESION_OF_METHODS = "MAX_LCOM";
    public static final String MAX_NUMBER_OF_PUBLIC_METHODS = "MAX_NOPM";
    
    public static final String MAX_NUMBER_OF_PARAMETERS = "MAX_NOPT";
    public static final String MAX_CYCLOMATIC_COMPLEXITY = "MAX_CC";
    public static final String MAX_MAX_NUMBER_OF_NESTING = "MAX_MNON";
    
    public static final String MAX_NUMBER_OF_AFFERENT_PACKAGES = "MAX_NOAPG";
    public static final String MAX_NUMBER_OF_EFFERENT_PACKAGES = "MAX_NOEPG";
    public static final String MAX_NUMBER_OF_AFFERENT_CLASSES = "MAX_NOACL";
    public static final String MAX_NUMBER_OF_EFFERENT_CLASSES = "MAX_NOECL";
    public static final String MAX_NUMBER_OF_AFFERENT_METHODS = "MAX_NOAMD";
    public static final String MAX_NUMBER_OF_EFFERENT_METHODS = "MAX_NOEMD";
    public static final String MAX_NUMBER_OF_AFFERENT_FIELDS = "MAX_NOAFD";
    public static final String MAX_NUMBER_OF_EFFERENT_FIELDS = "MAX_NOEFD";
    
    public static final IMetric[] ALL_SELECTABLE = {
        new LOC(), new NOST(), new NOMD(), new NOFD(), new NOMDFD(), new NOPM(),
        new CBO(), new DIT(), new NOC(), new RFC(), new WMC(), new LCOM(),
        new NOPT(), new CC(), new MNON()
    };
    
    public static final IMetric DEFAULT_METRIC = new Default();
}
