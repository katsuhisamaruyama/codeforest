/*
 *  Copyright 2014, Katsuhisa Maruyama (maru@jtool.org)
 */

package org.jtool.codeforest.ui.shape;

import org.jtool.codeforest.ui.view.forest.ForestData;
import org.jtool.codeforest.metrics.IMetric;
import java.awt.Image;
import javax.media.j3d.Appearance;
import javax.media.j3d.Geometry;
import javax.media.j3d.Material;
import javax.media.j3d.PolygonAttributes;
import javax.media.j3d.TexCoordGeneration;
import javax.media.j3d.Texture2D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransparencyAttributes;
import javax.vecmath.Color3f;
import javax.vecmath.Vector4f;
import com.sun.j3d.utils.geometry.Sphere;

/**
 * Represents leaves of a tree on a tree view.
 * @author Daiki Todoroki
 * @author Katsuhisa Maruyama
 */
public class Leaves {
    
    Geometry leafGeometry;
    
    Texture2D leafTexture;
    
    Appearance leafAppearance;
    
    double leafSize;
    
    double leafNumber;
    
    final Color3f leafColor = new Color3f(0.0f, 0.0f, 0.0f);
    
    private final double leaf_DEFAULT_SIZE = 1.5d;
    
    private final double leaf_DEFAULT_NUMBER = 10;
    
    private final Color3f leaf_DEFAULT_COLOR;
    
    public Leaves(FractalTree tree) {
        leafNumber = leaf_DEFAULT_NUMBER;
        leafSize = leaf_DEFAULT_SIZE;
        
        String name = tree.classMetrics.getQualifiedName();
        float c3 = (float)Math.abs((name + "3").hashCode()) / (float)Integer.MAX_VALUE;
        leaf_DEFAULT_COLOR = new Color3f(0.0f, c3, 0.0f);
        leafColor.set(leaf_DEFAULT_COLOR);
        
        leafNumber = leaf_DEFAULT_NUMBER;
        leafSize = leaf_DEFAULT_SIZE;
        
        // setMetricValues(tree.forestData, branch.);
        
        setLeafAppearance();
        
    }
    
    void draw(FractalTree tree, Branch branch, Transform3D branchTransform) {
        for (int i = 0; i < leafNumber; i++) {
            float moveY = ((float)i / (float)leafNumber);
            
            Transform3D child1Transform = new Transform3D();
            child1Transform.mul(branchTransform);
            
            Transform3D child2Transform =  new Transform3D();
            child2Transform.mul(branchTransform);
            
            FractalShape child1 = new Leaf(tree, this, moveY);
            child1Transform.mul(tree.childTransform[0]);
            
            FractalShape child2 = new Leaf(tree, this, moveY);
            child2Transform.mul(tree.childTransform[1]);
            
            child1.setTransform(child1Transform);
            branch.addChild(child1);
            
            child2.setTransform(child2Transform);
            branch.addChild(child2);
        }
    }
    
    public void setLeafNumber(double number) {
        if (number >= 0) {
            leafNumber = number;
        } else {
            leafNumber = leaf_DEFAULT_NUMBER;
        }
    }
    
    public void setLeafNumberRate(double percentage) {
        if (percentage >= 0) {
            leafNumber = leaf_DEFAULT_NUMBER * 2 * percentage;
        } else {
            leafNumber = leaf_DEFAULT_NUMBER;
        }
    }
    
    public void setLeafColor(float r, float g, float b) {
        if (r >= 0 && g >= 0 && b >= 0) {
            leafColor.set(r, g, b);
        } else {
            leafColor.set(leaf_DEFAULT_COLOR);
        }
    }
    
    public void setLeafColorRate(double percentage) {
        if (percentage >= 0) {
            leafColor.set(0.0f, (float)percentage, 0.0f);
        } else {
            leafColor.set(leaf_DEFAULT_COLOR);
        }
    }
    
    public void setMetricValues(ForestData forestData) {
        IMetric metric = forestData.getTrunkHeight();
        metric = forestData.getLeafNumber();
        if (metric.isClassMetric()) {
            // System.out.println("LEAF NUMBER = " + metric.getName() + " " + getMetricValuePerMax(metric, classMetrics));
            //setLeafNumberRate(getMetricValuePerMax(metric, classMetrics));
        }
        
        metric = forestData.getLeafColor();
        if (metric.isClassMetric()) {
            // System.out.println("LEAF COLOR = " + metric.getName() + " : " + getMetricValuePerMax(metric, classMetrics));
            //setLeafColorRate(getMetricValuePerMax(metric, classMetrics));
        }
    }
    
    private void setLeafAppearance() {
        Sphere leaf = new Sphere((float)leafSize);
        Color3f leafColor = new Color3f(0.0f, (float)Math.random(), 0.0f);
        
        leafGeometry = leaf.getShape(Sphere.BODY).getGeometry();
        Image image = AbstractShape.getAWTImage("leaf");
        leafTexture = AbstractShape.createTexture(image);
        
        Material material = new Material();
        material.setSpecularColor(leafColor);
        material.setShininess(1.0f);
        material.setCapability(Material.ALLOW_COMPONENT_READ);
        material.setCapability(Material.ALLOW_COMPONENT_WRITE);
        
        leafAppearance = new Appearance();
        leafAppearance.setTexture(leafTexture);
        TexCoordGeneration texgen = new TexCoordGeneration(TexCoordGeneration.EYE_LINEAR, 
                TexCoordGeneration.TEXTURE_COORDINATE_2,
                new Vector4f(1.0f, 0.0f, 0.0f, 0.0f),
                new Vector4f(0.0f, 1.0f, 0.0f, 0.0f),
                new Vector4f(0.0f, 0.0f, 1.0f, 0.0f));
        leafAppearance.setTexCoordGeneration(texgen);
        leafAppearance.setMaterial(material);
        
        TransparencyAttributes attr = new TransparencyAttributes(TransparencyAttributes.SCREEN_DOOR, 0.1f);
        attr.setTransparency(0.01f);
        leafAppearance.setTransparencyAttributes(attr);
        
        PolygonAttributes poly = new PolygonAttributes(PolygonAttributes.POLYGON_FILL, PolygonAttributes.CULL_NONE, 1.0f);
        leafAppearance.setPolygonAttributes(poly);
    }
    
}
