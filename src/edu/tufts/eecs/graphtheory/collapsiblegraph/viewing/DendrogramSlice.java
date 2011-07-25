package edu.tufts.eecs.graphtheory.collapsiblegraph.viewing;

import edu.tufts.eecs.graphtheory.collapsiblegraph.clustering.DendrogramEdge;
import edu.tufts.eecs.graphtheory.collapsiblegraph.clustering.DendrogramNode;

/**
 * A class representing a slice through the Dendrogram at some specific distance
 * @author Jeremy
 */
public class DendrogramSlice {
    
    private final DendrogramNode[] visibleDendrogramNodes;
    private final DendrogramEdge[] visibleDendrogramEdges;
    
    /**
     * 
     * @param visibleDendrogramNodes The array of DendrogramNodes visible at the given distance
     * @param visibleDendrogramEdges The array of DendrogramEdges visible at the given distance
     */
    public DendrogramSlice(final DendrogramNode[] visibleDendrogramNodes, final DendrogramEdge[] visibleDendrogramEdges) {
        this.visibleDendrogramNodes = visibleDendrogramNodes;
        this.visibleDendrogramEdges = visibleDendrogramEdges;
    }
    
    /**
     * 
     * @return The array of DendrogramNodes visible at the given distance
     */
    public DendrogramNode[] getVisibleDendrogramNodes() {
        return visibleDendrogramNodes;
    }
    
    /**
     * 
     * @return The array of DendrogramEdges visible at the given distance
     */
    public DendrogramEdge[] getVisibleDendrogramEdges() {
        return visibleDendrogramEdges;
    }
}
