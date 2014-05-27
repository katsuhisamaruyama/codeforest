/*
 *  Copyright 2014, Katsuhisa Maruyama (maru@jtool.org)
 */

package org.jtool.codeforest.ui.shape;

import javax.media.j3d.Appearance;
import javax.media.j3d.Geometry;
import javax.media.j3d.GeometryArray;
import javax.media.j3d.LineArray;
import javax.media.j3d.LineAttributes;
import javax.media.j3d.PolygonAttributes;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Texture2D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Point3d;
import javax.vecmath.TexCoord2f;
import java.awt.Image;

/**
 * Represents a river on a forest view.
 * @author Katsuhisa Maruyama
 * @author Daiki Todoroki
 */
public class River extends AbstractShape {
    
    /**
     * The texture of this river.
     */
    private static Texture2D riverTexture;
    
    /**
     * The appearance of this river.
     */
    private Appearance appearance;
    
    /**
     * The width of this river.
     */
    private double width;
    
    /**
     * The depth of this river.
     */
    private double depth;
    
    /**
     * Creates a river.
     * @param width the width of the river
     * @param depth the depth of the river
     */
    public River(double width, double depth) {
        this.width = width;
        this.depth = depth;
        
        if (riverTexture == null) {
            Image image = AbstractShape.getAWTImage("river");
            riverTexture = AbstractShape.createTexture(image);
        }
    }
    
    /**
     * Creates the scene graph for this river.
     */
    public void createSceneGraph() {
        setAppearance();
        
        TransformGroup trans = new TransformGroup();
        setAxis(trans);
        addChild(trans);
    }
    
    /**
     * Sets axis for drawing a river.
     * @param trans the transformation group for this river
     */
    private void setAxis(TransformGroup trans) {
        Point3d[] vertex = new Point3d[8];
        double height = 0.01;
        
        vertex[0] = new Point3d(-width / 2, height, depth / 2);
        vertex[1] = new Point3d(-width / 2, height, -depth / 2);
        vertex[2] = new Point3d(-width / 2, height, -depth / 2);
        vertex[3] = new Point3d(width / 2, height, -depth / 2);
        vertex[4] = new Point3d(width / 2, height, -depth / 2);
        vertex[5] = new Point3d(width / 2, height, depth / 2);
        vertex[6] = new Point3d(width / 2, height, depth / 2);
        vertex[7] = new Point3d(-width / 2, height, depth / 2);
        
        TexCoord2f[] txcoords = new TexCoord2f[4];
        txcoords[0] = new TexCoord2f((float)-width / 2, (float)depth / 2);
        txcoords[1] = new TexCoord2f((float)-width / 2, (float)-depth / 2);
        txcoords[2] = new TexCoord2f((float)width / 2, (float)-depth / 2);
        txcoords[3] = new TexCoord2f((float)width / 2, (float)depth / 2);
        
        LineArray geometry = new LineArray(vertex.length, GeometryArray.COORDINATES | GeometryArray.TEXTURE_COORDINATE_2);
        geometry.setCapability(Geometry.ALLOW_INTERSECT);
        geometry.setCoordinates(0, vertex);
        geometry.setTextureCoordinates(0, 0, txcoords);
        
        Shape3D shape = new Shape3D(geometry, appearance);
        shape.setCapability(Shape3D.ALLOW_GEOMETRY_READ);
        trans.addChild(shape);
    }
    
    /**
     * Sets the appearance of a river.
     */
    protected void setAppearance() {
        appearance = new Appearance();
        appearance.setTexture(riverTexture);
        
        PolygonAttributes pa = new PolygonAttributes(PolygonAttributes.POLYGON_FILL, PolygonAttributes.CULL_NONE, 0.0f, false);
        appearance.setPolygonAttributes(pa);
        
        LineAttributes lattr = new LineAttributes(8.0f, LineAttributes.PATTERN_SOLID, true);
        appearance.setLineAttributes(lattr);
    }
}
