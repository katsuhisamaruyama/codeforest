/*
 *  Copyright 2014, Katsuhisa Maruyama (maru@jtool.org)
 */

package org.jtool.codeforest.ui;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.VirtualUniverse;
import com.sun.j3d.utils.geometry.ColorCube;
import com.sun.j3d.utils.universe.SimpleUniverse;
import java.util.Map;

/**
 * Checks the version of Java3D.
 * @author Katsuhisa Maruyama
 */
public class Java3DVersionCheck {
    
    Java3DVersionCheck() {
    }
    
    @SuppressWarnings("rawtypes")
    void versionCheck() {
        Map j3dPropertyMap = VirtualUniverse.getProperties();
        for (Object key : j3dPropertyMap.keySet()) {
            System.out.println(key + "=" + j3dPropertyMap.get(key));
        }
    }
    
    void drawCheck() {
        Frame frame = new Frame();
        frame.setSize(500, 500);
        frame.setLayout(new BorderLayout());
        
        GraphicsConfiguration gc = SimpleUniverse.getPreferredConfiguration();
        
        Canvas3D canvas3D = new Canvas3D(gc);
        frame.add(canvas3D);
        
        SimpleUniverse simpleU = new SimpleUniverse(canvas3D);
        
        simpleU.getViewingPlatform().setNominalViewingTransform();
        
        BranchGroup objBG = new BranchGroup();
        objBG.addChild(new ColorCube(0.2));
        
        simpleU.addBranchGraph(objBG);
        
        frame.pack();
        frame.setVisible(true);
        
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }
    
    public static void main(String[] args) {
        Java3DVersionCheck check = new Java3DVersionCheck();
        check.versionCheck();
        // check.drawCheck();
    }
}
