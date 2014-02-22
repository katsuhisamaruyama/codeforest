/*
 *  Copyright 2014, Katsuhisa Maruyama (maru@jtool.org)
 */

package org.jtool.codeforest.ui.shape;

import org.jtool.codeforest.ui.view.forest.CodeForestUniverse;

import javax.media.j3d.ImageComponent;
import javax.media.j3d.ImageComponent2D;
import javax.media.j3d.Texture2D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Vector3d;

import com.sun.j3d.utils.image.TextureLoader;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.image.PixelGrabber;

/**
 * Represents an abstract object displaying on a forest view and a tree view.
 * @author Katsuhisa Maruyama
 */
public abstract class AbstractShape extends TransformGroup {
    
    protected Transform3D transform3d = new Transform3D();
    
    protected Vector3d location = new Vector3d();
    
    abstract public void createSceneGraph();
    
    abstract protected void setAppearance();
    
    protected AbstractShape() {
        setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
    }
    
    public void setLocation() {
        TransformGroup transform = new TransformGroup();
        transform.setTransform(transform3d);
        addChild(transform);
    }
    
    public void setLocation(double x, double y, double z) {
        location.set(x, y, z);
        transform3d.setTranslation(location);
        setTransform(transform3d);
        
    }
    
    public void setLocation(double left, double top, double right, double bottom, double height) {
        setLocation((left + right) / 2, height, (top + bottom) / 2);
    }
    
    protected void setLocation(Vector3d location) {
        this.location = location;
    }
    
    protected void setScale(double value) {
        transform3d.setScale(value);
    }
    
    public Vector3d getLocation() {
        return location;
    }
    
    static protected Image getAWTImage(String shape) {
        return CodeForestUniverse.getAWTImage(shape);
    }
    
    static protected Texture2D createTexture(Image image) {
        if (image != null) {
            BufferedImage bimage = new BufferedImage(128, 128, BufferedImage.TYPE_INT_ARGB);
            int[] imagefield = ((DataBufferInt)bimage.getRaster().getDataBuffer()).getData();
            PixelGrabber pg = new PixelGrabber(image, 0, 0, 128, 128, imagefield, 0, 128);
            try {
                pg.grabPixels();
                
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            
            ImageComponent2D icompo2d = new ImageComponent2D(ImageComponent.FORMAT_RGBA, bimage);
            Texture2D texture = (Texture2D) new TextureLoader((BufferedImage)image).getTexture();
            texture.setImage(0, icompo2d);
            return texture;
        }
        return null;
    }
}
