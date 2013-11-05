/*
 *  Copyright 2013, Katsuhisa Maruyama (maru@jtool.org)
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
    }
    
    /**
     * Saves this plug-in's preference when the plug-in is stopped.
     * @param context the bundle context for this plug-in
     * @throws Exception if this method fails to shutdown this plug-in
     * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
     */
    public void stop(BundleContext context) throws Exception {
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
            
            desc = ImageDescriptor.createFromURL(new URL(icons, "projects.gif"));
            reg.put("project", desc);
            desc = ImageDescriptor.createFromURL(new URL(icons, "package_obj.gif"));
            reg.put("package", desc);
            desc = ImageDescriptor.createFromURL(new URL(icons, "classes.gif"));
            reg.put("class", desc);
            desc = ImageDescriptor.createFromURL(new URL(icons, "enum_obj.gif"));
            reg.put("enum", desc);
            desc = ImageDescriptor.createFromURL(new URL(icons, "int_obj.gif"));
            reg.put("interface", desc);
            desc = ImageDescriptor.createFromURL(new URL(icons, "methpub_obj.gif"));
            reg.put("method_pub", desc);
            desc = ImageDescriptor.createFromURL(new URL(icons, "methpro_obj.gif"));
            reg.put("method_pro", desc);
            desc = ImageDescriptor.createFromURL(new URL(icons, "methdef_obj.gif"));
            reg.put("method_def", desc);
            desc = ImageDescriptor.createFromURL(new URL(icons, "methpri_obj.gif"));
            reg.put("method_pri", desc);
            desc = ImageDescriptor.createFromURL(new URL(icons, "constr_ovr.gif"));
            reg.put("constructor", desc);
            desc = ImageDescriptor.createFromURL(new URL(icons, "field_public_obj.gif"));
            reg.put("field_pub", desc);
            desc = ImageDescriptor.createFromURL(new URL(icons, "field_protected_obj.gif"));
            reg.put("field_pro", desc);
            desc = ImageDescriptor.createFromURL(new URL(icons, "field_default_obj.gif"));
            reg.put("field_def", desc);
            desc = ImageDescriptor.createFromURL(new URL(icons, "field_private_obj.gif"));
            reg.put("field_pri", desc);
            desc = ImageDescriptor.createFromURL(new URL(icons, "enum_tsk.gif"));
            reg.put("enum_const", desc);
            desc = ImageDescriptor.createFromURL(new URL(icons, "abstract_co.gif"));
            reg.put("abstract", desc);
            desc = ImageDescriptor.createFromURL(new URL(icons, "final_co.gif"));
            reg.put("final", desc);
            desc = ImageDescriptor.createFromURL(new URL(icons, "static_co.gif"));
            reg.put("static", desc);
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
        ImageDescriptor desc = getDefault().getImageRegistry().getDescriptor(name);
        if (desc != null) {
            return desc.createImage();
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
}
