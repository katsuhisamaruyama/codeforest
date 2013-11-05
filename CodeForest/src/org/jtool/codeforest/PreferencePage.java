/*
 *  Copyright 2013, Katsuhisa Maruyama (maru@jtool.org)
 */

package org.jtool.codeforest;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.RadioGroupFieldEditor;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.IWorkbench;

/**
 * Provides a preference page
 * @author Katsuhisa Maruyama
 */
public class PreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {
    
    public static final String P_PATH = "pathPreference";
    public static final String P_BOOLEAN = "booleanPreference";
    public static final String P_CHOICE = "choicePreference";
    public static final String P_STRING = "stringPreference";
    
    /**
     * Creates an object for this preference page.
     */
    public PreferencePage() {
        super(GRID);
        
        setPreferenceStore(Activator.getDefault().getPreferenceStore());
        setDescription("Setting for CodeForest");
    }
    
    /**
     * Creates the field editors for this preference page.
     */
    public void createFieldEditors() {
        addField(new DirectoryFieldEditor(P_PATH,  "&Directory preference:", getFieldEditorParent()));
        addField(new BooleanFieldEditor(P_BOOLEAN, "&An example of a boolean preference", getFieldEditorParent()));
        addField(new RadioGroupFieldEditor(P_CHOICE, "An example of a multiple-choice preference", 1,
                new String[][] { { "&Choice 1", "choice1" }, { "C&hoice 2", "choice2" } }, getFieldEditorParent()));
        addField(new StringFieldEditor(P_STRING, "A &text preference:", getFieldEditorParent()));
    }
    
    /**
     * Initializes this preference page for the given workbench.
     * @param workbench the workbench
     */
    public void init(IWorkbench workbench) {
    }
}