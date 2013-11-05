/*
 *  Copyright 2013, Katsuhisa Maruyama (maru@jtool.org)
 */

package org.jtool.codeforest.ui.layout;


/**
 * Interface for a map layout.
 * @author Daiki Todoroki
 * @author Katsuhisa Maruyama
 */
public interface IMapLayout {
    
    public void layout(IMapModel model, Rect bounds);
    
    public String getName();
    
    public String getDescription();
}
