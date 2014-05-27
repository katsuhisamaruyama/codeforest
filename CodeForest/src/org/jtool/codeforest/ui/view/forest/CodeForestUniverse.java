/*
 *  Copyright 2014, Katsuhisa Maruyama (maru@jtool.org)
 */

package org.jtool.codeforest.ui.view.forest;

import org.jtool.codeforest.Activator;
import org.jtool.codeforest.ui.CodeForestFrame;
import org.jtool.codeforest.ui.view.ImageConverter;
import org.jtool.codeforest.ui.view.MousePicker;
import org.eclipse.swt.graphics.ImageData;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import javax.media.j3d.AmbientLight;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.Bounds;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.GraphicsConfigTemplate3D;
import javax.media.j3d.Node;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3f;
import com.sun.j3d.utils.behaviors.keyboard.KeyNavigatorBehavior;
import com.sun.j3d.utils.behaviors.vp.OrbitBehavior;
import com.sun.j3d.utils.universe.PlatformGeometry;
import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.j3d.utils.universe.ViewingPlatform;

/**
 * A visual environment for a forest or tree view.
 * @author Katsuhisa Maruyama
 * @author Daiki Todoroki
 */
public class CodeForestUniverse extends SimpleUniverse {
    
    /**
     * The main frame.
     */
    protected CodeForestFrame frame;
    
    /**
     * Creates visual environment for a forest or tree view.
     * @param canvas a drawing canvas for a forest or tree
     * @param frame the main frame
     */
    protected CodeForestUniverse(Canvas3D canvas, CodeForestFrame frame) {
        super(canvas);
        this.frame = frame;
    }
    
    /**
     * Sets capabilities for the branch group of the scene graph.
     * @param bg the branch group of the scene graph
     */
    protected void setCapability(BranchGroup bg) {
        bg.setCapability(BranchGroup.ALLOW_DETACH);
        bg.setCapability(BranchGroup.ALLOW_PICKABLE_READ);
        bg.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
        bg.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);
        bg.setCapability(BranchGroup.ALLOW_CHILDREN_READ);
        
        addBranchGraph(bg);
    }
    
    /**
     * Sets configuration of a forest or tree view.
     */
    protected void setConfiguration() {
        BoundingSphere bounds = new BoundingSphere(new Point3d(0,0,0), 100.0);
        
        ViewingPlatform viewingPlatform = getViewingPlatform();
        PlatformGeometry pg = new PlatformGeometry();
        createLighting(pg);
        viewingPlatform.setPlatformGeometry(pg);
        viewingPlatform.setNominalViewingTransform();
        
        TransformGroup viewTrans = this.getViewingPlatform().getViewPlatformTransform();
        KeyNavigatorBehavior keybehavior = new KeyNavigatorBehavior(viewTrans);
        keybehavior.setSchedulingBounds(bounds);
        PlatformGeometry vp = new PlatformGeometry();
        vp.addChild(keybehavior);
        addBranchGraph(vp);
        
        OrbitBehavior orbit = new OrbitBehavior(getCanvas(), OrbitBehavior.REVERSE_ALL);
        orbit.setSchedulingBounds(bounds);
        getViewingPlatform().setViewPlatformBehavior(orbit);
    }
    
    /**
     * Creates the lighting for the platform geometry.
     * @param pg the platform geometry.
     */
    private void createLighting(PlatformGeometry pg) {
        pg.addChild(createAmbientLight());
        
        final Color3f lightColor = new Color3f(1.0f, 1.0f, 0.9f);
        final Vector3f lightDirection = new Vector3f(1.0f, 1.0f, 1.0f);
        final Color3f lightColor2 = new Color3f(1.0f, 1.0f, 1.0f);
        final Vector3f lightDirection2 = new Vector3f(-1.0f, -1.0f, -1.0f);
        
        pg.addChild(createDirectionLight(lightColor, lightDirection));
        pg.addChild(createDirectionLight(lightColor2, lightDirection2));
    }
    
    /**
     * Creates the direction light.
     * @param lightColor the color of the light
     * @param lightDirection the direction of the light
     * @return the direction light
     */
    private Node createDirectionLight(Color3f lightColor, Vector3f lightDirection) {
        Bounds bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0);
        DirectionalLight light = new DirectionalLight(lightColor, lightDirection);
        
        light.setInfluencingBounds(bounds);
        return light;
    }
    
    /**
     * Creates the ambient light.
     * @return the ambient light
     */
    private Node createAmbientLight() {
        Bounds bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0);
        Color3f ambientColor = new Color3f(1.0f, 1.0f, 1.0f);
        AmbientLight ambientLightNode = new AmbientLight(true, ambientColor);
        
        ambientLightNode.setInfluencingBounds(bounds);
        return ambientLightNode;
    }
    
    /**
     * Registers the mouse picker.
     * @param branch the branch group on the scene graph
     */
    protected void picker(BranchGroup branch) {
        BoundingSphere bounds = new BoundingSphere(new Point3d(0,0,0), 100.0);
        MousePicker picker = new MousePicker(getCanvas(), branch, bounds, frame);
        
        branch.addChild(picker);
    }
    
    /**
     * Obtains the graphics configuration of the main frame.
     * @param frame the main frame
     * @return the graphics configuration
     */
    public static GraphicsConfiguration getGraphicsConfiguration(Frame frame) {
        GraphicsConfiguration gc = frame.getGraphicsConfiguration();
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] gs = ge.getScreenDevices();
        GraphicsConfiguration good = null;
        
        GraphicsConfigTemplate3D gct = new GraphicsConfigTemplate3D();
        
        for (GraphicsDevice gd : gs) {
            if (gd == gc.getDevice()) {
                good = gct.getBestConfiguration(gd.getConfigurations());
                if (good != null) {
                    break;
                }
            }
        }
        return good;
    }
    
    /**
     * Obtains an AWT image of a visual object.
     * @param shape the visual object
     * @return the AWT image
     */
    public static Image getAWTImage(String shape) {
        ImageConverter loader = ImageConverter.getInstance();
        if (Activator.getImage(shape) != null) {
            ImageData data = Activator.getImage(shape).getImageData();
            Image image = loader.convertToAWT(data);
            return image;
        }
        return null;
    }
}
