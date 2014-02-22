/*
 *  Copyright 2014, Katsuhisa Maruyama (maru@jtool.org)
 */

package org.jtool.codeforest;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.swt.graphics.Image;
import org.jtool.codeforest.ui.view.control.WorkingSetStore;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.net.MalformedURLException;

/**
 * The activator class controlling the plug-in life cycle.
 * @author Katsuhisa Maruyama
 */
public class Activator extends AbstractUIPlugin {
    
    /**
     * The plug-in identification.
     */
    public static final String PLUGIN_ID = "CodeForest";
    
    /**
     * A shared plug-in object.
     */
    private static Activator plugin;
    
    /**
     * A plug-in bundle.
     */
    private Bundle bundle;
    
    /**
     * A preference name for storing working sets of metrics.
     */
    private static final String WORKING_SETS = "WorkingSetPreference";
    
    /**
     * Creates a plug-in runtime object.
     */
    public Activator() {
        bundle = Platform.getBundle(PLUGIN_ID);
    }
    
    /**
     * Refreshes this plug-in's actions when the plug-in is activated.
     * @param context the bundle context for this plug-in
     * @throws Exception if this plug-in did not start up properly
     * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
     */
    public void start(BundleContext context) throws Exception {
        super.start(context);
        plugin = this;
        doLoad();
    }
    
    /**
     * Saves this plug-in's preference when the plug-in is stopped.
     * @param context the bundle context for this plug-in
     * @throws Exception if this method fails to shutdown this plug-in
     * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
     */
    public void stop(BundleContext context) throws Exception {
        doStore();
        plugin = null;
        super.stop(context);
    }
    
    /**
     * Returns the shared instance.
     * @return the shared instance
     */
    public static Activator getDefault() {
        return plugin;
    }
    
    /**
     * Returns an image descriptor for the image file at the given plug-in relative path.
     * @param path the path
     * @return the image descriptor
     */
    public static ImageDescriptor getImageDescriptor(String path) {
        return imageDescriptorFromPlugin(PLUGIN_ID, path);
    }
    
    /**
     * Returns the root of the workspace.
     * @return the workspace root
     */
    public static IWorkspaceRoot getWorkspaceRoot() {
        return ResourcesPlugin.getWorkspace().getRoot();
    }
    
    /**
     * Obtains the path containing the specified resources.
     * @return the resource path, or <code>null</code> if there is an I/O error
     */
    public String getResourcePath() {
        try {
            Enumeration<URL> bundleUrls = bundle.getResources("xml");
            URL fileUrl = FileLocator.toFileURL(bundleUrls.nextElement());
            return fileUrl.toString();
        } catch (IOException e) { /* empty */ }
        return null;
    }
    
    private String[][] iconImages = {
            { "projects.gif", "project" },
            { "jcu_obj.gif", "javafile" },
            { "package_obj.gif", "package" },
            { "classes.gif", "class" },
            { "enum_obj.gif", "enum" },
            { "int_obj.gif", "interface" },
            { "methpub_obj.gif", "method_pub" },
            { "methpro_obj.gif", "method_pro" },
            { "methdef_obj.gif", "method_def" },
            { "methpri_obj.gif", "method_pri" },
            { "constr_ovr.gif", "constructor" },
            { "field_public_obj.gif", "field_pub" },
            { "field_protected_obj.gif", "field_pro" },
            { "field_default_obj.gif", "field_def" },
            { "field_private_obj.gif", "field_pri" },
            { "enum_tsk.gif", "enum_const" },
            { "abstract_co.gif", "abstract" },
            { "final_co.gif", "final" },
            { "static_co.gif", "static" },
            { "view_menu.gif", "menu" },
            { "workset.gif", "workset" },
            { "workingsets.gif", "workingset" },
            { "container_obj.gif", "memo" },
            { "tasks_tsk.gif", "task" },
            { "editor.gif", "editor" },
            { "det_pane_hide.gif", "setting" },
            { "insp_sbook.gif", "inspect" },
            { "find_obj", "find" },
            { "properties.gif", "properties" },
            { "undo_edit.gif", "undo" },
            { "redo_edit.gif", "redo" },
            { "tree1.png", "tree1" },
            { "tree2.png", "tree2" },
            { "tree3.png", "tree3" },
    };
    
    /**
     * Initializes an image registry with images which are frequently used by the plug-in.
     * @param reg the registry to initialize
     */
    protected void initializeImageRegistry(ImageRegistry reg) {
        super.initializeImageRegistry(reg);
        
        URL images = getBundle().getResource("images");
        URL icons = getBundle().getResource("icons");
        
        ImageDescriptor desc;
        try {
            desc = ImageDescriptor.createFromURL(new URL(images, "sky1.png"));
            reg.put("sky", desc);
            desc = ImageDescriptor.createFromURL(new URL(images, "grass.png"));
            reg.put("grass", desc);
            desc = ImageDescriptor.createFromURL(new URL(images, "river.png"));
            reg.put("river", desc);
            desc = ImageDescriptor.createFromURL(new URL(images, "wood3.png"));
            reg.put("wood", desc);
            desc = ImageDescriptor.createFromURL(new URL(images, "leaf1.png"));
            reg.put("leaf", desc);
            
            for (int i = 0; i < iconImages.length; i++) {
                desc = ImageDescriptor.createFromURL(new URL(icons, iconImages[i][0]));
                reg.put(iconImages[i][1], desc);
            }
            
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Obtains the image corresponding to a given name.
     * @param shape the image name stored in the image registry
     * @return the image object, or <code>null</code> if none
     */
    public static Image getImage(String name) {
        if (getDefault() != null) {
            ImageDescriptor desc = getDefault().getImageRegistry().getDescriptor(name);
            if (desc != null) {
                return desc.createImage();
            }
        }
        return null;
    }
    
    /**
     * Obtains the workbench window.
     * @return the workbench window
     */
    public static IWorkbenchWindow getWorkbenchWindow() {
        return PlatformUI.getWorkbench().getActiveWorkbenchWindow();
    }
    
    /**
     * Loads working sets of metrics from this plugin's preferences.
     */
    public void doLoad() {
        // Activator.getDefault().getPreferenceStore().setValue(WORKING_SETS, "");
        String store = Activator.getDefault().getPreferenceStore().getString(WORKING_SETS);
        WorkingSetStore.setPreference(store);
    }
    
    /**
     * Stores working sets of metrics from this plugin's preferences.
     */
    public void doStore() {
        String store = WorkingSetStore.getPreference();
        Activator.getDefault().getPreferenceStore().setValue(WORKING_SETS, store);
    }
}
