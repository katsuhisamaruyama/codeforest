/*
 *  Copyright 2014, Katsuhisa Maruyama (maru@jtool.org)
 */

package org.jtool.codeforest.ui.view;

import org.jtool.codeforest.Activator;
import org.jtool.codeforest.ui.CodeForestFrame;
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
 * A 3D visual environment for forest and tree views.
 * @author Daiki Todoroki
 * @author Katsuhisa Maruyama
 */
public class CodeForestUniverse extends SimpleUniverse {
    
    protected CodeForestFrame frame;
    
    protected CodeForestUniverse(Canvas3D canvas, CodeForestFrame frame) {
        super(canvas);
        this.frame = frame;
    }
    
    protected void setCapability(BranchGroup bg) {
        bg.setCapability(BranchGroup.ALLOW_DETACH);
        bg.setCapability(BranchGroup.ALLOW_PICKABLE_READ);
        bg.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
        bg.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);
        bg.setCapability(BranchGroup.ALLOW_CHILDREN_READ);
        
        addBranchGraph(bg);
    }
    
    protected void setConfigurations() {
        BoundingSphere bounds = new BoundingSphere(new Point3d(0,0,0), 100.0);
        
        ViewingPlatform viewingPlatform = getViewingPlatform();
        PlatformGeometry pg = new PlatformGeometry();
        createSomeLighting(pg);
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
    
    private void createSomeLighting(PlatformGeometry pg) {
        pg.addChild(createAmbientLight());
        
        final Color3f lightColor = new Color3f(1.0f, 1.0f, 0.9f);
        final Vector3f lightDirection = new Vector3f(1.0f, 1.0f, 1.0f);
        final Color3f lightColor2 = new Color3f(1.0f, 1.0f, 1.0f);
        final Vector3f lightDirection2 = new Vector3f(-1.0f, -1.0f, -1.0f);
        
        pg.addChild(createDirectionLight(lightColor, lightDirection));
        pg.addChild(createDirectionLight(lightColor2, lightDirection2));
    }
    
    private Node createDirectionLight(Color3f lightColor, Vector3f lightDirection) {
        final Bounds bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0);
        final DirectionalLight light = new DirectionalLight(lightColor, lightDirection);
        
        light.setInfluencingBounds(bounds);
        return light;
    }
    
    private Node createAmbientLight() {
        final Bounds bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0);
        final Color3f ambientColor = new Color3f(1.0f, 1.0f, 1.0f);
        final AmbientLight ambientLightNode = new AmbientLight(true, ambientColor);
        
        ambientLightNode.setInfluencingBounds(bounds);
        return ambientLightNode;
    }
    
    protected void picker(BranchGroup branch){
        BoundingSphere bounds = new BoundingSphere(new Point3d(0,0,0), 100.0);
        MousePicker picker = new MousePicker(getCanvas(), branch, bounds, frame);
        
        branch.addChild(picker);
    }
    
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
