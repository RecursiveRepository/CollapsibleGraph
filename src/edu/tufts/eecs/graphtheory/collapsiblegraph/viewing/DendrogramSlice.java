/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.tufts.eecs.graphtheory.collapsiblegraph.viewing;

import edu.tufts.eecs.graphtheory.collapsiblegraph.clustering.DendrogramEdge;
import edu.tufts.eecs.graphtheory.collapsiblegraph.clustering.DendrogramNode;

/**
 *
 * @author Jeremy
 */
public class DendrogramSlice {
    
    private final DendrogramNode[] visibleDendrogramNodes;
    private final DendrogramEdge[] visibleDendrogramEdges;
    
    public DendrogramSlice(final DendrogramNode[] visibleDendrogramNodes, final DendrogramEdge[] visibleDendrogramEdges) {
        this.visibleDendrogramNodes = visibleDendrogramNodes;
        this.visibleDendrogramEdges = visibleDendrogramEdges;
    }
    
    public DendrogramNode[] getVisibleDendrogramNodes() {
        return visibleDendrogramNodes;
    }
    
    public DendrogramEdge[] getVisibleDendrogramEdges() {
        return visibleDendrogramEdges;
    }
}
