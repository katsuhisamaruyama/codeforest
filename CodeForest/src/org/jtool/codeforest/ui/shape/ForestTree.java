/*
 *  Copyright 2014, Katsuhisa Maruyama (maru@jtool.org)
 */

package org.jtool.codeforest.ui.shape;

import org.jtool.codeforest.ui.view.SettingData;
import org.jtool.codeforest.metrics.IMetric;
import org.jtool.codeforest.metrics.java.ClassMetrics;
import javax.media.j3d.Appearance;
import javax.media.j3d.Geometry;
import javax.media.j3d.Material;
import javax.media.j3d.PolygonAttributes;
import javax.media.j3d.Shape3D;
import javax.media.j3d.TexCoordGeneration;
import javax.media.j3d.Texture2D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.TransparencyAttributes;
import javax.vecmath.Color3f;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector4f;
import com.sun.j3d.utils.geometry.Cone;
import com.sun.j3d.utils.geometry.Cylinder;

/**
 * Represents a tree on a forest view.
 * @author Katsuhisa Maruyama
 * @author Daiki Todoroki
 */
public class ForestTree extends MetricsTree {
    
    /**
     * The geometry of the trunk of this tree.
     */
    private static Geometry trunkGeometry;
    
    /**
     * The texture of the trunk of this tree.
     */
    private static Texture2D trunkTexture;
    
    /**
     * The geometry of the foliage of this tree.
     */
    private static Geometry foliageGeometry;
    
    /**
     * The geometry of the foliage of this tree.
     */
    private static Texture2D foliageTexture;
    
    /**
     * The appearance of the trunk of this tree.
     */
    private Appearance trunkAppearance;
    
    /**
     * The appearance of the foliage of this tree.
     */
    private Appearance foliageAppearance;
    
    /**
     * The height of the trunk of this tree.
     */
    private double trunkHeight; // between 0.01 and 10.0
    
    /**
     * The radius of the trunk of this tree.
     */
    private double trunkRadius; // between 1 and 100
    
    /**
     * The color of the trunk of this tree.
     */
    private final Color3f trunkColor = new Color3f(0.0f, 0.0f, 0.0f);
    
    /**
     * The height rate of the foliage of this tree.
     */
    private double foliageHeightRate;  // foliageHeight = trunkHeight * foliageHeightRate
    
    /**
     * The radius rate of the foliage of this tree.
     */
    private double foliageRadiusRate;  // foliageRadius = trunkRadius * (1 + foliageRadiusRate)
    
    /**
     * The color of the foliage of this tree.
     */
    private final Color3f foliageColor = new Color3f(0.0f, 0.0f, 0.0f);
    
    /**
     * The default height of the trunk of this tree.
     */
    private final double trunk_DEFAULT_HEIGHT = 1.0;
    
    /**
     * The default radius of the trunk of this tree.
     */
    private final double trunk_DEFAULT_RADIUS = 1.0;
    
    /**
     * The default color of the trunk of this tree.
     */
    private final Color3f trunk_DEFAULT_COLOR;
    
    /**
     * The default color of the foliage of this tree.
     */
    private final Color3f foliage_DEFAULT_COLOR;
    
    /**
     * The default height of the foliage of this tree.
     */
    private final double foliage_HEIGHT_DEFAULT_RATE = 0.5;
    
    /**
     * The default radius of the foliage of this tree.
     */
    private final double foliage_RADIUS_DEFAULT_RATE = 0.5;
    
    /**
     * The color of a selected tree.
     */
    private static final Color3f SETECTED_COLOR = new Color3f(0.5f, 0.0f, 0.5f);
    
    /**
     * Creates a tree in a forest view.
     * @param mclass a class represented by the tree
     * @param data the setting data that forms the tree
     */
    public ForestTree(ClassMetrics mclass, SettingData data) {
        super(mclass);
        
        trunkHeight = trunk_DEFAULT_HEIGHT;
        trunkRadius = trunk_DEFAULT_RADIUS;
        foliageHeightRate = foliage_HEIGHT_DEFAULT_RATE;
        foliageRadiusRate =  foliage_RADIUS_DEFAULT_RATE;
        
        String name = classMetrics.getQualifiedName();
        float c1 = (float)Math.abs((name + "1").hashCode()) / (float)Integer.MAX_VALUE;
        float c2 = (float)Math.abs((name + "2").hashCode()) / (float)Integer.MAX_VALUE;
        float c3 = (float)Math.abs((name + "3").hashCode()) / (float)Integer.MAX_VALUE;
        
        trunk_DEFAULT_COLOR = new Color3f(0.3f + c1 / 3, c2 / 2, 0.0f);
        foliage_DEFAULT_COLOR = new Color3f(0.0f, c3, 0.0f);
        trunkColor.set(trunk_DEFAULT_COLOR);
        foliageColor.set(foliage_DEFAULT_COLOR);
        
        setCapability();
        
        if (trunkGeometry == null) {
            Cylinder cylinder = new Cylinder(0.5f, 1.0f);
            trunkGeometry = cylinder.getShape(Cylinder.BODY).getGeometry();
            trunkTexture = createTexture(AbstractShape.getAWTImage("wood"));
        }
        
        if (foliageGeometry == null) {
            Cone cone = new Cone(0.5f, 1.0f);
            foliageGeometry = cone.getShape(Cone.BODY).getGeometry();
            foliageTexture = createTexture(AbstractShape.getAWTImage("leaf"));
        }
        
        setMetricValues(data);
    }
    
    /**
     * Sets the capability of this tree.
     */
    private void setCapability() {
        setCapability(ENABLE_PICK_REPORTING);
        setCapability(ALLOW_TRANSFORM_READ);
        setCapability(ALLOW_TRANSFORM_WRITE);
        setCapability(ALLOW_PICKABLE_READ);
    }
    
    /**
     * Sets the height of the trunk of this tree.
     * @param height the height of the trunk
     */
    public void setTrunkHeight(double height) {
        if (height >= 0) {
            trunkHeight = height;
        } else {
            trunkHeight = trunk_DEFAULT_HEIGHT;
        }
    }
    
    /**
     * Sets the radius of the trunk of this tree
     * @param radius the radius of the trunk
     */
    public void setTrunkRadius(double radius) {
        if (radius >= 0) {
            trunkRadius = radius;
        } else {
            trunkRadius = trunk_DEFAULT_RADIUS;
        }
    }
    
    /**
     * Sets the color of the trunk of this tree.
     * @param r the red value of the color of the trunk
     * @param g the green value of the color of the trunk
     * @param b the blue value of the color of the trunk
     */
    public void setTrunkColor(float r, float g, float b) {
        if (r >= 0 && g >= 0 && b >= 0) {
            trunkColor.set(r, g, b);
        } else {
            trunkColor.set(trunk_DEFAULT_COLOR);
        }
    }
    
    /**
     * Sets the color rate of the trunk of this tree.
     * @param percentage the percentage of the color of the trunk
     */
    public void setTrunkColorRate(double percentage) {
        if (percentage >= 0) {
            trunkColor.set(0.3f + (float)percentage, (float)percentage, 0.0f);
        } else {
            trunkColor.set(trunk_DEFAULT_COLOR);
        }
    }
    
    /**
     * Sets the height of the foliage of this tree.
     * @param height the height of the foliage
     */
    public void setFoliageHeight(double height) {
        if (height >= 0) {
            foliageHeightRate = height / trunkHeight;
        } else {
            foliageHeightRate = foliage_HEIGHT_DEFAULT_RATE;
        }
    }
    
    /**
     * Sets the height rate of the trunk of this tree
     * @param radius the height rate of the trunk
     */
    public void setFoliageHeightRate(double percentage) {
        if (percentage >= 0) {
            foliageHeightRate = percentage;
        } else {
            foliageHeightRate = foliage_HEIGHT_DEFAULT_RATE;
        }
    }
    
    /**
     * Sets the radius of the foliage of this tree
     * @param radius the radius of the foliage
     */
    public void setFoliageRadius(double radius) {
        if (radius >= 0) {
            foliageRadiusRate = (radius / trunkRadius) - 1;
        } else {
            foliageRadiusRate = foliage_RADIUS_DEFAULT_RATE;
        }
    }
    
    /**
     * Sets the radius rate of the foliage of this tree
     * @param radius the radius rate of the foliage
     */
    public void setFoliageRadiusRate(double percentage) {
        if (percentage >= 0) {
            foliageRadiusRate = percentage;
        } else {
            foliageRadiusRate = foliage_RADIUS_DEFAULT_RATE;
        }
    }
    
    /**
     * Sets the color of the foliage of this tree.
     * @param r the red value of the color of the foliage
     * @param g the green value of the color of the foliage
     * @param b the blue value of the color of the foliage
     */
    public void setFoliageColor(float r, float g, float b) {
        if (r >= 0 && g >= 0 && b >= 0) {
            foliageColor.set(r, g, b);
        } else {
            foliageColor.set(foliage_DEFAULT_COLOR);
        }
    }
    
    /**
     * Sets the color rate of the foliage of this tree.
     * @param percentage the percentage of the color of the foliage
     */
    public void setFoliageColorRate(double percentage) {
        if (percentage >= 0) {
            foliageColor.set(0.0f, (float)percentage, 0.0f);
        } else {
            foliageColor.set(foliage_DEFAULT_COLOR);
        }
    }
    
    /**
     * Sets the metrics value for this tree.
     * @param data the setting data that forms this tree
     */
    public void setMetricValues(SettingData data) {
        IMetric metric;
        
        metric = data.getTrunkHeight();
        if (metric.isClassMetric()) {
            // System.out.println("TRUNK HEIGHT = " + metric.getName() + " : " + getMetricValuePerAverage(metric, classMetrics));
            setTrunkHeight(adjust(getMetricValuePerAverage(metric) / 2));
        }
        
        metric = data.getTrunkRadius();
        if (metric.isClassMetric()) {
            // System.out.println("TRUNK RADIUS = " + metric.getName() + " : " + getMetricValue(metric, classMetrics));
            setTrunkRadius(adjust(getMetricValuePerAverage(metric) / 3));
        }
        
        metric = data.getTrunkColor();
        if (metric.isClassMetric()) {
            // System.out.println("TRUNK COLOR = " + metric.getName() + " : " + getMetricValuePerMax(metric, classMetrics));
            setTrunkColorRate(getMetricValuePerMax(metric));
        }
        
        metric = data.getFoliageHeight();
        if (metric.isClassMetric()) {
            // System.out.println("FOLIAGE HEIGHT = " + metric.getName() + " : " + getMetricValuePerMax(metric, classMetrics));
            setFoliageHeightRate(getMetricValuePerMax(metric));
        }
        
        metric = data.getFoliageRadius();
        if (metric.isClassMetric()) {
            // System.out.println("FOLIAGE RADIUS = " + metric.getName() + " " + getMetricValuePerMax(metric, classMetrics));
            setFoliageRadiusRate(getMetricValuePerMax(metric));
        }
        
        metric = data.getFoliageColor();
        if (metric.isClassMetric()) {
            // System.out.println("FOLIAGE COLOR = " + metric.getName() + " : " + getMetricValuePerMax(metric, classMetrics));
            setFoliageColorRate(getMetricValuePerMax(metric));
        }
    }
    
    /**
     * Creates the scene graph for this tree.
     */
    public void createSceneGraph() {
        setAppearance();
        
        TransformGroup group = new TransformGroup();
        Transform3D rotate = new Transform3D();
        group.setTransform(rotate);
        addChild(group);
        
        group.addChild(createTrunk());
        group.addChild(createFoliage());
    }
    
    /**
     * Creates a trunk of this tree.
     * @return the transformation for the trunk
     */
    private TransformGroup createTrunk() {
        TransformGroup group = new TransformGroup();
        
        Shape3D trunkShape = new Shape3D();
        trunkShape.setGeometry(trunkGeometry);
        trunkShape.setAppearance(trunkAppearance);
        group.addChild(trunkShape);
        
        Transform3D transform = new Transform3D();
        transform.setScale(new Vector3d(trunkRadius, trunkHeight, trunkRadius));
        transform.setTranslation(new Vector3d(0.0d, trunkHeight / 2, 0.0d));
        group.setTransform(transform);
        
        return group;
    }
    
    /**
     * Creates a foliage of this tree.
     * @return the transformation for the foliage
     */
    private TransformGroup createFoliage() {
        TransformGroup group = new TransformGroup();
        
        Shape3D foliageShape = new Shape3D();
        foliageShape.setGeometry(foliageGeometry);
        foliageShape.setAppearance(foliageAppearance);
        group.addChild(foliageShape);
        
        double foliageHeight = trunkHeight * foliageHeightRate;
        double foliageRadius = trunkRadius * (1 + foliageRadiusRate);
        
        Transform3D transform = new Transform3D();
        transform.setScale(new Vector3d(foliageRadius, foliageHeight, foliageRadius));
        transform.setTranslation(new Vector3d(0.0d, foliageHeight / 2 + trunkHeight, 0.0d));
        group.setTransform(transform);
        
        return group;
    }
    
    /**
     * Sets the appearance of this tree.
     */
    protected void setAppearance() {
        setTrunkAppearance();
        setFoliageAppearance();
        
        TransparencyAttributes attr = new TransparencyAttributes(TransparencyAttributes.SCREEN_DOOR, 0.1f);
        attr.setTransparency(0.1f);
        trunkAppearance.setTransparencyAttributes(attr);
        foliageAppearance.setTransparencyAttributes(attr);
    }
    
    /**
     * Sets the appearance of a trunk of this tree.
     */
    private void setTrunkAppearance() {
        trunkAppearance = new Appearance();
        trunkAppearance.setTexture(trunkTexture);
        TexCoordGeneration texgen = new TexCoordGeneration(TexCoordGeneration.EYE_LINEAR, 
                TexCoordGeneration.TEXTURE_COORDINATE_2,
                new Vector4f(1.0f, 0.0f, 0.0f, 0.0f),
                new Vector4f(0.0f, 1.0f, 0.0f, 0.0f));
        trunkAppearance.setTexCoordGeneration(texgen);
        
        Material material = new Material();
        material.setSpecularColor(trunkColor);
        material.setShininess(1.0f);
        material.setCapability(Material.ALLOW_COMPONENT_READ);
        material.setCapability(Material.ALLOW_COMPONENT_WRITE);
        trunkAppearance.setMaterial(material);
        
        PolygonAttributes poly = new PolygonAttributes(PolygonAttributes.POLYGON_FILL, PolygonAttributes.CULL_BACK, 1.0f);
        trunkAppearance.setPolygonAttributes(poly);
    }
    
    /**
     * Sets the appearance of a foliage of this tree.
     */
    protected void setFoliageAppearance() {
        foliageAppearance = new Appearance();
        foliageAppearance.setTexture(foliageTexture);
        TexCoordGeneration texgen = new TexCoordGeneration(TexCoordGeneration.EYE_LINEAR, 
                TexCoordGeneration.TEXTURE_COORDINATE_2,
                new Vector4f(1.0f, 0.0f, 0.0f, 0.0f),
                new Vector4f(0.0f, 1.0f, 0.0f, 0.0f),
                new Vector4f(0.0f, 0.0f, 1.0f, 0.0f));
        foliageAppearance.setTexCoordGeneration(texgen);
        
        Material material = new Material();
        material.setSpecularColor(foliageColor);
        material.setShininess(1.0f);
        material.setCapability(Material.ALLOW_COMPONENT_READ);
        material.setCapability(Material.ALLOW_COMPONENT_WRITE);
        foliageAppearance.setMaterial(material);
        
        PolygonAttributes poly = new PolygonAttributes(PolygonAttributes.POLYGON_FILL, PolygonAttributes.CULL_NONE, 1.0f);
        foliageAppearance.setPolygonAttributes(poly);
    }
    
    /**
     * Selects a tree.
     * @param isSelected <code>true</code> this tree is selected , otherwise <code>false<c/ode>
     */
    public void changedSelected(boolean isSelected) {
        if (isSelected) {
            Material material = foliageAppearance.getMaterial();
            material.setSpecularColor(SETECTED_COLOR);
        } else {
            Material material = foliageAppearance.getMaterial();
            material.setSpecularColor(foliageColor);
        }
    }
}
