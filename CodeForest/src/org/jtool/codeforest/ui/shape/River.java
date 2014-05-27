/*
 *  Copyright 2014, Katsuhisa Maruyama (maru@jtool.org)
 */

package org.jtool.codeforest.ui.shape;

import java.awt.Image;

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

/**
 * Represents a river on a forest view.
 * @author Katsuhisa Maruyama
 */
public class River extends AbstractShape {
    
    private static Texture2D riverTexture;
    
    private Appearance appearance;
    
    private double width;
    
    private double depth;
    
    public River(double width, double depth) {
        this.width = width;
        this.depth = depth;
        
        if (riverTexture == null) {
            Image image = AbstractShape.getAWTImage("river");
            riverTexture = AbstractShape.createTexture(image);
        }
    }
    
    public void createSceneGraph() {
        setAppearance();
        
        TransformGroup trans = new TransformGroup();
        createAxis(trans);
        addChild(trans);
    }
    
    private void createAxis(TransformGroup trans) {
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
    
    protected void setAppearance() {
        appearance = new Appearance();
        appearance.setTexture(riverTexture);
        
        PolygonAttributes pa = new PolygonAttributes(PolygonAttributes.POLYGON_FILL, PolygonAttributes.CULL_NONE, 0.0f, false);
        appearance.setPolygonAttributes(pa);
        
        LineAttributes lattr = new LineAttributes(8.0f, LineAttributes.PATTERN_SOLID, true);
        appearance.setLineAttributes(lattr);
    }
}
