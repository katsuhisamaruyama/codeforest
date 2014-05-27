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
 * @author Daiki Todoroki
 */
public abstract class AbstractShape extends TransformGroup {
    
    /**
     * The transformation for a visual object.
     */
    protected Transform3D transform3d = new Transform3D();
    
    /**
     * The location of a visual object.
     */
    protected Vector3d location = new Vector3d();
    
    /**
     * Creates the scene graph for a visual object.
     */
    abstract public void createSceneGraph();
    
    /**
     * Sets the appearance of a visual object.
     */
    abstract protected void setAppearance();
    
    /**
     * Creates an abstract object displaying on a forest view and a tree view.
     */
    protected AbstractShape() {
        setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
    }
    
    /**
     * Sets the scale of a visual object.
     * @param value the value of the scale
     */
    protected void setScale(double value) {
        transform3d.setScale(value);
    }
    
    /**
     * Sets the location of a visual object.
     */
    public void setLocation() {
        TransformGroup transform = new TransformGroup();
        transform.setTransform(transform3d);
        addChild(transform);
    }
    
    /**
     * Sets the location of a visual object.
     * @param x the x-position of the visual object
     * @param y the y-position of the visual object
     * @param z the z-position of the visual object
     */
    public void setLocation(double x, double y, double z) {
        location.set(x, y, z);
        transform3d.setTranslation(location);
        setTransform(transform3d);
        
    }
    
    /**
     * Sets the location of a visual object.
     * @param left the left position of the visual object
     * @param top the top position of the visual object
     * @param right the right position of the visual object
     * @param bottom the bottom position of the visual object
     * @param height the height of the visual object
     */
    public void setLocation(double left, double top, double right, double bottom, double height) {
        setLocation((left + right) / 2, height, (top + bottom) / 2);
    }
    
    /**
     * Sets the location of a visual object.
     * @return the location of the visual object
     */
    protected void setLocation(Vector3d location) {
        this.location = location;
    }
    
    /**
     * Obtains the location of a visual object.
     * @return the location of the visual object
     */
    public Vector3d getLocation() {
        return location;
    }
    
    /**
     * Obtains the AWT image for a visual object.
     * @param shape the visual object
     * @return the AWT image
     */
    static protected Image getAWTImage(String shape) {
        return CodeForestUniverse.getAWTImage(shape);
    }
    
    /**
     * Creates the texture of a visual object.
     * @param image the image for the texture
     * @return the created texture
     */
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
