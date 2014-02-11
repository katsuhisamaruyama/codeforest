/*
 *  Copyright 2014, Katsuhisa Maruyama (maru@jtool.org)
 */

package org.jtool.codeforest.ui.view.forest;

/**
 * Represents a pair of nodes for constructing a forest.
 * @author Katsuhisa Maruyama
 */
public class ForestNodePair {
    
    private int edgeType;
    
    private ForestNode node1;
    
    private ForestNode node2;
    
    public ForestNodePair(int type, ForestNode n1, ForestNode n2) {
        edgeType = type;
        node1 = n1;
        node2 = n2;
    }
    
    public int getNodeEdgeType() {
        return edgeType;
    }
    
    public ForestNode getOneNode() {
        return node1;
    }
    
    public ForestNode getAnotherNode() {
        return node2;
    }
}
