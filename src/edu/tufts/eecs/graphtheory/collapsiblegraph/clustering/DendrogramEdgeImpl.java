package edu.tufts.eecs.graphtheory.collapsiblegraph.clustering;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Jeremy
 */
public class DendrogramEdgeImpl implements DendrogramEdge, Serializable {

    DendrogramNode sourceNode;
    DendrogramNode targetNode;
    List<DendrogramEdge> childEdges;
    DendrogramEdge parent;
    double distance=0f;
    
    

    public DendrogramNode getSourceDendrogramNode() {
        return sourceNode;
    }

    public DendrogramNode getTargetDendrogramNode() {
        return targetNode;
    }

    public List<DendrogramEdge> getChildEdges() {
        return childEdges;
    }
    
    public void setChildEdges(List<DendrogramEdge> childEdges) {
        this.childEdges = childEdges;
    }
    
    public void setDistance (double distance) {
        this.distance = distance;
    }
    
    public DendrogramEdgeImpl(DendrogramNode sourceNode, DendrogramNode targetNode) {
        this.sourceNode = sourceNode;
        this.targetNode = targetNode;
        childEdges = new ArrayList<DendrogramEdge>();
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
    
    @Override
    public void setParent(DendrogramEdge de) {
        parent = de;
    }
    @Override
    public DendrogramEdge getParent() {
        return parent;
    }
}
