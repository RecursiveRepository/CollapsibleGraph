package edu.tufts.eecs.graphtheory.collapsiblegraph.clustering;

import edu.tufts.eecs.graphtheory.collapsiblegraph.edge.Edge;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Jeremy
 */
public class DendrogramEdgeImpl implements DendrogramEdge {

    DendrogramNode sourceNode;
    DendrogramNode targetNode;
    Set<DendrogramEdge> childEdges;
    double distance;
    
    

    public DendrogramNode getSourceDendrogramNode() {
        return sourceNode;
    }

    public DendrogramNode getTargetDendrogramNode() {
        return targetNode;
    }

    public Set<DendrogramEdge> getChildEdges() {
        return childEdges;
    }
    
    public void setChildEdges(Set<DendrogramEdge> childEdges) {
        this.childEdges = childEdges;
    }
    
    public void setDistance (double distance) {
        this.distance = distance;
    }
    
    public DendrogramEdgeImpl(DendrogramNode sourceNode, DendrogramNode targetNode) {
        this.sourceNode = sourceNode;
        this.targetNode = targetNode;
        childEdges = new HashSet<DendrogramEdge>();
    }

    public double getDistance() {
        return distance; 
    }
    
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof DendrogramEdgeImpl)) {
            return false;
        }
        DendrogramEdgeImpl otherDEdge = (DendrogramEdgeImpl)obj;
        if(!otherDEdge.getSourceDendrogramNode().equals(sourceNode)) {
            return false;
        }
        if(!otherDEdge.getTargetDendrogramNode().equals(targetNode)) {
            return false;
        }
        
        if(otherDEdge.getDistance() != distance) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return sourceNode.hashCode() + targetNode.hashCode();
    }
}
