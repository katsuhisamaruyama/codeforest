/*
 *  Copyright 2014, Katsuhisa Maruyama (maru@jtool.org)
 */

package org.jtool.codeforest.ui.shape;

import org.jtool.codeforest.ui.view.forest.ForestData;
import org.jtool.codeforest.metrics.IMetric;
import org.jtool.codeforest.metrics.MetricSort;
import org.jtool.codeforest.metrics.java.ClassMetrics;
import org.jtool.codeforest.metrics.java.ProjectMetrics;
import java.awt.Image;
import javax.media.j3d.Appearance;
import javax.media.j3d.Geometry;
import javax.media.j3d.Material;
import javax.media.j3d.PolygonAttributes;
import javax.media.j3d.Shape3D;
import javax.media.j3d.TexCoordGeneration;
import javax.media.j3d.Texture2D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Color3f;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector4f;
import com.sun.j3d.utils.geometry.Cylinder;

/**
 * Represents a tree on a tree view.
 * @author Daiki Todoroki
 * @author Katsuhisa Maruyama
 */
public class FractalTree extends AbstractTree {
    
    private Geometry trunkGeometry;
    
    private Texture2D trunkTexture;
    
    private Appearance trunkAppearance;
    
    Geometry branchGeometry;
    
    Geometry branchConeGeometry;
    
    Texture2D branchTexture;
    
    Appearance branchAppearance;
    

    
    Transform3D transform;
    
    Transform3D[] childTransform;
    
    private TransformGroup trunk;
    
    private TransformGroup branch;
    
    private final double ANGLE_Y = Math.PI / 5;
    
    private final double ANGLE_Z = Math.PI / 4;
    
    private final double BRANCH_LENGTH_SCALE = 0.775;
    
    final double branchNumber;
    
    final int branchLevelLimit;
    
    int currentBranchIndex;
    
    final ClassMetrics classMetrics;
    
    private double trunkHeight;
    
    private double trunkRadius;
    
    private final Color3f trunkColor = new Color3f(0.0f, 0.0f, 0.0f);
    
    private final double trunk_DEFAULT_HEIGHT = 1.0;
    
    private final double trunk_DEFAULT_RADIUS = 1.0;
    
    private final Color3f trunk_DEFAULT_COLOR;
    
    public FractalTree(ProjectMetrics mproject, ClassMetrics mclass, ForestData fdata) {
        super(mproject, mclass, fdata);
        
        classMetrics = mclass;
        trunkHeight = trunk_DEFAULT_HEIGHT;
        trunkRadius = trunk_DEFAULT_RADIUS;
        
        branchNumber = mclass.getMetricValue(MetricSort.NUMBER_OF_METHODS);
        branchLevelLimit = getBranchLevel(branchNumber);
        
        String name = classMetrics.getQualifiedName();
        float c1 = (float)Math.abs((name + "1").hashCode()) / (float)Integer.MAX_VALUE;
        float c2 = (float)Math.abs((name + "2").hashCode()) / (float)Integer.MAX_VALUE;
        trunk_DEFAULT_COLOR = new Color3f(0.3f + c1 / 3, c2 / 2, 0.0f);
        trunkColor.set(trunk_DEFAULT_COLOR);
        
        setMetricValues(forestData);
    }
    
    public void setTrunkHeight(double height) {
        if (height >= 0) {
            trunkHeight = height;
        } else {
            trunkHeight = trunk_DEFAULT_HEIGHT;
        }
    }
    
    public void setTrunkRadius(double radius) {
        if (radius >= 0) {
            trunkRadius = radius;
        } else {
            trunkRadius = trunk_DEFAULT_RADIUS;
        }
    }
    
    public double getBranchLength() {
        return BRANCH_LENGTH_SCALE * 2;
    }
    
    private int getBranchLevel(double bnum) {
        if (bnum == 0){
            return 0;
        }
        
        int level = 1;
        while (bnum != 1) {
            bnum = Math.ceil(bnum / 2);
            level++;
        }
        return level;
    }
    
    public void setTrunkColor(float r, float g, float b) {
        if (r >= 0 && g >= 0 && b >= 0) {
            trunkColor.set(r, g, b);
        } else {
            trunkColor.set(trunk_DEFAULT_COLOR);
        }
    }
    
    public void setTrunkColorRate(double percentage) {
        if (percentage >= 0) {
            trunkColor.set(0.3f + (float)percentage, (float)percentage, 0.0f);
        } else {
            trunkColor.set(trunk_DEFAULT_COLOR);
        }
    }
    
    public void setMetricValues(ForestData forestData) {
        IMetric metric = forestData.getTrunkHeight();
        if (metric.isClassMetric()) {
            // System.out.println("TRUNK HEIGHT = " + metric.getName() + " : " + getMetricValuePerAverage(metric, classMetrics));
            setTrunkHeight(adjust(getMetricValuePerAverage(metric, classMetrics) / 2));
        }
        
        metric = forestData.getTrunkRadius();
        if (metric.isClassMetric()) {
            // System.out.println("TRUNK RADIUS = " + metric.getName() + " : " + getMetricValue(metric, classMetrics));
            setTrunkRadius(adjust(getMetricValue(metric, classMetrics) / 2));
        }
        
        metric = forestData.getTrunkColor();
        if (metric.isClassMetric()) {
            // System.out.println("TRUNK COLOR = " + metric.getName() + " : " + getMetricValuePerMax(metric, classMetrics));
            setTrunkColorRate(getMetricValuePerMax(metric, classMetrics));
        }
    }
    
    public void createSceneGraph() {
        setAppearance();
        
        setBranchAppearance();
        
        trunk = new TransformGroup();
        Shape3D trunkShape = new Shape3D();
        trunkShape.setGeometry(trunkGeometry);
        trunkShape.setAppearance(trunkAppearance);
        trunk.addChild(trunkShape);
        
        Transform3D form = new Transform3D();
        // form.setScale(new Vector3d(trunkRadius, trunkHeight, trunkRadius));
        form.setTranslation(new Vector3d(0.0d, -trunkHeight / 2, 0.0d));
        trunk.setTransform(form);
        
        currentBranchIndex = 0;
        branch = new Branch(this, 1);
        form = new Transform3D();
        form.setTranslation(new Vector3d(0.0d, getBranchLength() / 2, 0.0d));
        branch.setTransform(form);
        
        TransformGroup group = new TransformGroup();
        group.addChild(trunk);
        group.addChild(branch);
        addChild(group);
    }
    
    protected void setAppearance() {
        transform = new Transform3D();
        
        Cylinder cylinder = new Cylinder((float)trunkRadius, (float)trunkHeight);
        trunkGeometry = cylinder.getShape(Cylinder.BODY).getGeometry();
        Image image = AbstractShape.getAWTImage("wood");
        trunkTexture = AbstractShape.createTexture(image);
        
        trunkAppearance = new Appearance();
        trunkAppearance.setTexture(trunkTexture);
        TexCoordGeneration texgen = new TexCoordGeneration(TexCoordGeneration.EYE_LINEAR, 
                TexCoordGeneration.TEXTURE_COORDINATE_2,
                new Vector4f(1.0f, 0.0f, 0.0f, 0.0f),
                new Vector4f(0.0f, 1.0f, 0.0f, 0.0f));
        trunkAppearance.setTexCoordGeneration(texgen);
        
        Material material = new Material(
                new Color3f(0.5f, 0.3f, 0.2f),
                new Color3f(0.5f, 0.3f, 0.2f),
                new Color3f(0.5f, 0.3f, 0.2f),
                new Color3f(0.5f, 0.3f, 0.2f),
                1.0f);
        material.setShininess(1.0f);
        material.setCapability(Material.ALLOW_COMPONENT_READ);
        material.setCapability(Material.ALLOW_COMPONENT_WRITE);
        trunkAppearance.setMaterial(material);
        
        PolygonAttributes poly = new PolygonAttributes(PolygonAttributes.POLYGON_FILL, PolygonAttributes.CULL_NONE, 1.0f);
        trunkAppearance.setPolygonAttributes(poly);
    }
    
    protected void setBranchAppearance() {
        Transform3D factor = new Transform3D();
        factor.setScale(BRANCH_LENGTH_SCALE);
        
        Transform3D ry = new Transform3D();
        ry.rotY(ANGLE_Y);
        Transform3D rz1 = new Transform3D();
        rz1.rotZ(ANGLE_Z);
        Transform3D rz2 = new Transform3D();
        rz2.rotZ(-ANGLE_Z);
        Transform3D slide = new Transform3D();
        
        slide.setTranslation(new Vector3d(0.0, getBranchLength() / 2, 0.0));
        childTransform = new Transform3D[2];
        childTransform[0] = new Transform3D();
        childTransform[1] = new Transform3D();
        
        childTransform[0].mul(slide);
        childTransform[0].mul(rz1);
        childTransform[0].mul(ry);
        childTransform[0].mul(factor);
        childTransform[0].mul(slide);
        
        childTransform[1].mul(slide);
        childTransform[1].mul(rz2);
        childTransform[1].mul(ry);
        childTransform[1].mul(factor);
        childTransform[1].mul(slide);
        
        Cylinder branch = new Cylinder((float)trunkRadius, (float)getBranchLength());
        branchGeometry = branch.getShape(Cylinder.BODY).getGeometry();
        Image image = AbstractShape.getAWTImage("wood");
        branchTexture = AbstractShape.createTexture(image);
        
        branchAppearance = new Appearance();
        branchAppearance.setTexture(branchTexture);
        
        Material material = new Material(
                new Color3f(0.5f, 0.3f, 0.2f),
                new Color3f(0.5f, 0.3f, 0.2f),
                new Color3f(0.5f, 0.3f, 0.2f),
                new Color3f(0.5f, 0.3f, 0.2f),
                1.0f);
        TexCoordGeneration texgen = new TexCoordGeneration(TexCoordGeneration.EYE_LINEAR, 
                TexCoordGeneration.TEXTURE_COORDINATE_2,
                new Vector4f(1.0f, 0.0f, 0.0f, 0.0f),
                new Vector4f(0.0f, 1.0f, 0.0f, 0.0f));
        branchAppearance.setTexCoordGeneration(texgen);
        branchAppearance.setMaterial(material);
        
        PolygonAttributes poly = new PolygonAttributes(PolygonAttributes.POLYGON_FILL, PolygonAttributes.CULL_NONE, 1.0f);
        branchAppearance.setPolygonAttributes(poly);
    }
    
    /*
    protected void setLeafAppearance() {
        Sphere leaf = new Sphere((float)leafSize);
        Color3f leafColor = new Color3f(0.0f, (float)Math.random(), 0.0f);
        
        leafGeometry = leaf.getShape(Sphere.BODY).getGeometry();
        Image image = getAWTImage("leaf");
        leafTexture = createTexture(image);
        
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
    */
}
