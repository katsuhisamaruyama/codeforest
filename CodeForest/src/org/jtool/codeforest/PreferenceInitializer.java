/*
 *  Copyright 2013, Katsuhisa Maruyama (maru@jtool.org)
 */

package org.jtool.codeforest;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

/**
 * Initializes the preference values.
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {

    /**
     * Stores initial preference values.
     */
    public void initializeDefaultPreferences() {
        IPreferenceStore store = Activator.getDefault().getPreferenceStore();
        store.setDefault(PreferencePage.P_BOOLEAN, true);
        store.setDefault(PreferencePage.P_CHOICE, "choice2");
        store.setDefault(PreferencePage.P_STRING, "Default value");
    }
}
