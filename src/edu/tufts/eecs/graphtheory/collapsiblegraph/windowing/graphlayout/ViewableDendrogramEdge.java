package edu.tufts.eecs.graphtheory.collapsiblegraph.windowing.graphlayout;

import edu.tufts.eecs.graphtheory.collapsiblegraph.clustering.DendrogramEdge;

/**
 *
 * @author Jeremy
 */
public class ViewableDendrogramEdge {
    
    private DendrogramEdge dEdge;
    private ViewableDendrogramNode sourceNode;
    private ViewableDendrogramNode targetNode;
    
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
}
