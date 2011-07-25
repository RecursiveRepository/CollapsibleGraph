package edu.tufts.eecs.graphtheory.collapsiblegraph.windowing.graphlayout;

import edu.tufts.eecs.graphtheory.collapsiblegraph.clustering.DendrogramEdge;

/**
 * A class that wraps a DendrogramEdge that holds its visibility data
 * @author Jeremy
 */
public class ViewableDendrogramEdge {
    
    private DendrogramEdge dEdge;
    private ViewableDendrogramNode sourceNode;
    private ViewableDendrogramNode targetNode;
    
    /**
     * 
     * @param dEdge The DendrogramEdge that this ViewableDendrogramEdge wraps
     * @param sourceNode The ViewableDendrogramNode that this edge leads out from
     * @param targetNode The ViewableDendrogramNode that this edge goes into
     */
    public ViewableDendrogramEdge(DendrogramEdge dEdge, ViewableDendrogramNode sourceNode, ViewableDendrogramNode targetNode) {
        this.dEdge = dEdge;
        this.sourceNode = sourceNode; 
        this.targetNode = targetNode;
    }
    
    public ViewableDendrogramNode getSourceNode() {
        return sourceNode;
    }

    public ViewableDendrogramNode getTargetNode() {
        return targetNode;
    }
    
    public DendrogramEdge getDendrogramEdge() {
        return dEdge;
    }
    
    @Override 
    public boolean equals(Object o ) {
        if(!(o instanceof ViewableDendrogramEdge)) {
            return false;
        }
        if(dEdge.equals(((ViewableDendrogramEdge)o).getDendrogramEdge())) {
            return true;
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return dEdge.hashCode();
    }
}
