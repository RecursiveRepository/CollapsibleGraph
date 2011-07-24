package edu.tufts.eecs.graphtheory.collapsiblegraph.clustering;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * The DendrogramEdge is arranged into a Dendrogram to represent the set of edges that are visible at a given distance. When nodes are 
 * clustered together, the edges they touch become the children of newly formed DendrogramEdges as formed at that distance. By creating a Dendrogram
 * out of the DendrogramEdges, you can traverse from the root of that Dendrogram to any given distance in linear time.
 * @author Jeremy
 */
public class DendrogramEdge implements Serializable {

    DendrogramNode sourceDNode;
    DendrogramNode targetDNode;
    DendrogramEdge parentDEdge;
    List<DendrogramEdge> childDEdges;
    double distance = 0f;

    /**
     * 
     * @param sourceDNode The DendrogramNode this DendrogramEdge originates from
     * @param targetDNode The DendrogramNode this DendrogramEdge points to
     */
    public DendrogramEdge(DendrogramNode sourceDNode, DendrogramNode targetDNode) {
        this.sourceDNode = sourceDNode;
        this.targetDNode = targetDNode;
        childDEdges = new ArrayList<DendrogramEdge>();
    }

    /**
     * 
     * @return The DendrogramNode this DendrogramEdge originates from
     */
    public DendrogramNode getSourceDendrogramNode() {
        return sourceDNode;
    }

    /**
     * 
     * @return The DendrogramNode this DendrogramEdge points to
     */
    public DendrogramNode getTargetDendrogramNode() {
        return targetDNode;
    }

    /**
     * 
     * @return The DendrogramEdges that this DendrogramEdge clusters together
     */
    public List<DendrogramEdge> getChildEdges() {
        return childDEdges;
    }

    /**
     * 
     * @param childDEdges The DendrogramEdges that this DendrogramEdge clusters together
     */
    public void setChildEdges(List<DendrogramEdge> childDEdges) {
        this.childDEdges = childDEdges;
    }

    /**
     * 
     * @param distance The distance at which this DendrogramEdge forms
     */
    public void setDistance(double distance) {
        this.distance = distance;
    }

    /**
     * 
     * @return The distance at which this DendrogramEdge forms
     */
    public double getDistance() {
        return distance;
    }

    /**
     * 
     * @param parentDEdge The DendrogramEdge that ends up holding this DendrogramEdge, thus becoming its parent in that 
     */
    public void setParentDEdge(DendrogramEdge parentDEdge) {
        parentDEdge = parentDEdge;
    }

    /**
     * 
     * @return The DendrogramEdge that serves as this DendrogramEdge's parent in the edge Dendrogram
     */
    public DendrogramEdge getParentDEdge() {
        return parentDEdge;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof DendrogramEdge)) {
            return false;
        }
        DendrogramEdge otherDEdge = (DendrogramEdge) obj;
        if (!otherDEdge.getSourceDendrogramNode().equals(sourceDNode)) {
            return false;
        }
        if (!otherDEdge.getTargetDendrogramNode().equals(targetDNode)) {
            return false;
        }

        if (otherDEdge.getDistance() != distance) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return sourceDNode.hashCode() + targetDNode.hashCode();
    }
}
