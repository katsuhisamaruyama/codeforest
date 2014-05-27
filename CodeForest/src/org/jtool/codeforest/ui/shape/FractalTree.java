/*
 *  Copyright 2014, Katsuhisa Maruyama (maru@jtool.org)
 */

package org.jtool.codeforest.ui.shape;

import org.jtool.codeforest.ui.view.SettingData;
import org.jtool.codeforest.metrics.IMetric;
import org.jtool.codeforest.metrics.MetricSort;
import org.jtool.codeforest.metrics.java.MethodMetrics;
import org.jtool.codeforest.metrics.java.ClassMetrics;
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
import javax.media.j3d.LineArray;
import javax.media.j3d.GeometryArray;
import javax.vecmath.Color3f;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector4f;
import javax.vecmath.Point3d;
import com.sun.j3d.utils.geometry.Cylinder;

/**
 * Represents a tree on a tree view.
 * @author Katsuhisa Maruyama
 * @author Daiki Todoroki
 */
public class FractalTree extends MetricsTree {
    
    /**
     * The geometry of the trunk of this tree.
     */
    private Geometry trunkGeometry;
    
    /**
     * The texture of the trunk of this tree.
     */
    private Texture2D trunkTexture;
    
    /**
     * The appearance of the trunk of this tree.
     */
    private Appearance trunkAppearance;
    
    /**
     * The geometry of the branch of this tree.
     */
    Geometry branchGeometry;
    
    /**
     * The geometry of the cone part of the branch of this tree.
     */
    Geometry branchConeGeometry;
    
    /**
     * The texture of the branch of this tree.
     */
    Texture2D branchTexture;
    
    /**
     * The appearance of the branch of this tree.
     */
    Appearance branchAppearance;
    
    /**
     * The transformation for this tree.
     */
    Transform3D transform;
    
    /**
     * The transformation for trunks of this tree.
     */
    Transform3D[] childTransform;
    
    /**
     * The transformation group for trunks of this tree.
     */
    private TransformGroup trunk;
    
    /**
     * The transformation group for branches of this tree.
     */
    private TransformGroup branch;
    
    /**
     * The angle of y-axis for the trunk of this tree.
     */
    private final double ANGLE_Y = Math.PI / 5;
    
    /**
     * The angle of z-axis for the trunk of this tree.
     */
    private final double ANGLE_Z = Math.PI / 4;
    
    /**
     * The scale of length of the brunch of this tree.
     */
    private final double BRANCH_LENGTH_SCALE = 0.775;
    
    /**
     * The number of the branches of this tree.
     */
    private final double branchNumber;
    
    /**
     * The number of the branches which have been currently created.
     */
    private double curBranchNumber;
    
    /**
     * The limit of the level of the branches of this tree.
     */
    private final int branchLevelLimit;
    
    /**
     * The index of a branch currently created.
     */
    private int curBranchIndex;
    
    /**
     * The index of a branch previously created. 
     */
    private int prevBranchIndex;
    
    /**
     * The height of the trunk of this tree.
     */
    private double trunkHeight;
    
    /**
     * The radius of the trunk of this tree.
     */
    private double trunkRadius;
    
    /**
     * The height of the branch of this tree.
     */
    private double branchHeight;
    
    /**
     * The color of the trunk of this tree.
     */
    private final Color3f trunkColor = new Color3f(0.0f, 0.0f, 0.0f);
    
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
     * The setting data that forms this tree.
     */
    private SettingData settingData;
    
    /**
     * Creates a tree in a tree view.
     * @param mclass a class represented by the tree
     * @param data the setting data that forms the tree
     */
    public FractalTree(ClassMetrics mclass, SettingData data) {
        super(mclass);
        
        trunkHeight = trunk_DEFAULT_HEIGHT;
        trunkRadius = trunk_DEFAULT_RADIUS;
        
        branchNumber = mclass.getMetricValue(MetricSort.NUMBER_OF_METHODS);
        branchLevelLimit = getBranchLevel(branchNumber);
        
        String name = classMetrics.getQualifiedName();
        float c1 = (float)Math.abs((name + "1").hashCode()) / (float)Integer.MAX_VALUE;
        float c2 = (float)Math.abs((name + "2").hashCode()) / (float)Integer.MAX_VALUE;
        trunk_DEFAULT_COLOR = new Color3f(0.3f + c1 / 3, c2 / 2, 0.0f);
        trunkColor.set(trunk_DEFAULT_COLOR);
        
        settingData = data;
        setMetricValues(data);
    }
    
    /**
     * Returns the setting data that forms this tree.
     * @return the setting data for this tree
     */
    SettingData getSettingData() {
        return settingData;
    }
    
    /**
     * Set the height of the trunk of this tree.
     * @param height the height of the tree trunk
     */
    public void setTrunkHeight(double height) {
        if (height >= 0) {
            trunkHeight = height;
        } else {
            trunkHeight = trunk_DEFAULT_HEIGHT;
        }
        
        branchHeight = trunkHeight * BRANCH_LENGTH_SCALE * 2;
    }
    
    /**
     * Sets the radius of the trunk of this tree.
     * @param radius the radius value of the tree trunk
     */
    public void setTrunkRadius(double radius) {
        if (radius >= 0) {
            trunkRadius = radius;
        } else {
            trunkRadius = trunk_DEFAULT_RADIUS;
        }
    }
    
    /**
     * Returns the limited number of branches of this tree.
     * @return the limited number of the branches
     */
    int getBranchLevelLimit() {
        return branchLevelLimit;
    }
    
    /**
     * Increments the number of branches of this tree.
     */
    void incrementBranch() {
        prevBranchIndex = curBranchIndex;
        
        double total = Math.pow(2.0, branchLevelLimit - 1);
        curBranchNumber = curBranchNumber + branchNumber / total;
        curBranchIndex = (int)Math.floor(curBranchNumber);
    }
    
    /**
     * Checks if the number of branches is less than the predefined maximum number.
     * @return <code>true</code> the the number of branches is less than the maximum number, otherwise <code>false</code>
     */
    boolean lessThanBranchMax() {
        return curBranchIndex < branchNumber;
    }
    
    /**
     * Checks if a branch will be created or not.
     * @return <code>true</code> the branch will be created, otherwise <code>false</code>
     */
    boolean branchToBeCreated() {
        return curBranchIndex > prevBranchIndex;
    }
    
    /**
     * Obtains the metrics for a method represented by this tree.
     * @return the method metrics
     */
    MethodMetrics getMethodMetrics() {
        if (curBranchIndex < branchNumber) {
            return classMetrics.getMethodMetrics().get(curBranchIndex);
        }
        return null;
    }
    
    /**
     * Calculates the branch level needed for representing this tree and returns the branch level.
     * @param bnum the number of the branch of this tree
     * @return the the branch level
     */
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
    
    /**
     * Sets the color of the trunk of this tree.
     * @param r the red value of the trunk color
     * @param g the green value of the trunk color
     * @param b the blue value of the trunk color
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
     * @param percentage the percentage of the trunk color
     */
    public void setTrunkColorRate(double percentage) {
        if (percentage >= 0) {
            trunkColor.set(0.3f + (float)percentage, (float)percentage, 0.0f);
        } else {
            trunkColor.set(trunk_DEFAULT_COLOR);
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
            // setTrunkRadius(adjust(getMetricValue(metric, classMetrics) / 2));
            setTrunkRadius(adjust(getMetricValuePerAverage(metric) / 3));
        }
        
        metric = data.getTrunkColor();
        if (metric.isClassMetric()) {
            // System.out.println("TRUNK COLOR = " + metric.getName() + " : " + getMetricValuePerMax(metric, classMetrics));
            setTrunkColorRate(getMetricValuePerMax(metric));
        }
    }
    
    /**
     * Creates the scene graph for this tree.
     */
    public void createSceneGraph() {
        setAppearance();
        
        trunk = new TransformGroup();
        Shape3D trunkShape = new Shape3D();
        trunkShape.setGeometry(trunkGeometry);
        trunkShape.setAppearance(trunkAppearance);
        trunk.addChild(trunkShape);
        
        Transform3D form = new Transform3D();
        // form.setScale(new Vector3d(trunkRadius, trunkHeight, trunkRadius));
        form.setTranslation(new Vector3d(0.0d, -trunkHeight / 2, 0.0d));
        trunk.setTransform(form);
        
        prevBranchIndex = -1;
        curBranchIndex = 0;
        curBranchNumber = 0.000001;
        branch = new Branch(this, 1);
        
        form = new Transform3D();
        form.setTranslation(new Vector3d(0.0d, branchHeight / 2, 0.0d));
        branch.setTransform(form);
        
        TransformGroup group = new TransformGroup();
        group.addChild(trunk);
        group.addChild(branch);
        
        group.addChild(createAxes());
        
        addChild(group);
    }
    
    /**
     * Creates the axes for this tree.
     * @return the created axes
     */
    private Shape3D createAxes() {
        Point3d[] vertex = new Point3d[4];
        vertex[0] = new Point3d(-1.0d, 0.0d, 0.0d);
        vertex[1] = new Point3d(1.0d, 0.0d, 0.0d);
        vertex[2] = new Point3d(0.0d, -1.5d, 0.0d);
        vertex[3] = new Point3d(0.0d, 1.5d, 0.0d);
        
        LineArray line = new LineArray(vertex.length, GeometryArray.COORDINATES | GeometryArray.COLOR_3);
        line.setCoordinates(0, vertex);
        
        Color3f black = new Color3f(0.0f, 0.0f, 0.0f);
        line.setColor(0, black);
        line.setColor(1, black);
        line.setColor(2, black);
        line.setColor(3, black);
        
        return new Shape3D(line);
    }
    
    /**
     * Sets the appearance of this tree.
     */
    protected void setAppearance() {
        setTrunkAppearance();
        setBranchAppearance();
    }
    
    /**
     * Sets the appearance of a trunk of this tree.
     */
    protected void setTrunkAppearance() {
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
    
    /**
     * Sets the appearance of a branch of this tree.
     */
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
        
        slide.setTranslation(new Vector3d(0.0, branchHeight / 2, 0.0));
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
        
        Cylinder branch = new Cylinder((float)trunkRadius, (float)branchHeight);
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
}
