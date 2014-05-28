/*
 *  Copyright 2014, Katsuhisa Maruyama (maru@jtool.org)
 */

package org.jtool.codeforest.ui.layout;

/**
 * Interface for a model managing mappable nodes.
 * @author Katsuhisa Maruyama
 * @author Daiki Todoroki
 */
public interface IMapModel {
    
    /**
     * Obtains mappable nodes managed by this model.
     * @return the array of the mappable nodes
     */
    public IMappableNode[] getNodes();
}
