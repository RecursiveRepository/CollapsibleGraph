package edu.tufts.eecs.graphtheory.collapsiblegraph.graphedge;

import edu.tufts.eecs.graphtheory.collapsiblegraph.graphnode.GraphNode;

/**
 * This class is a skeleton, barebones implementation of the GraphEdge interface.
 * This could easily be subclassed to include other features such as edge weight.
 * @author Jeremy
 */
public class SkeletonGraphEdge implements GraphEdge {

    private GraphNode sourceNode;
    private GraphNode targetNode;
    private int hashCode;

    /**
     * 
     * @param sourceNode the source GraphNode of this edge
     * @param targetNode the target GraphNode of this edge
     */
    public SkeletonGraphEdge(GraphNode sourceNode, GraphNode targetNode) {
        this.sourceNode = sourceNode;
        this.targetNode = targetNode;
        generateHashCode();
    }

    /**
     * 
     * @param sourceNode the source GraphNode of this Edge
     */
    public void setSource(GraphNode sourceNode) {
        this.sourceNode=sourceNode;
        generateHashCode();
    }

    /**
     * 
     * @param targetNode the target GraphNode of this Edge
     */
    public void setTarget(GraphNode targetNode) {
        this.targetNode=targetNode;
        generateHashCode();
    }

    public GraphNode getSource() {
        return sourceNode;
    }

    public GraphNode getTarget() {
        return targetNode;
    }
     
    //Hashcode is going to be called a lot. Better we calculate once and return the saved value
    private void generateHashCode() {
        hashCode = (sourceNode.getName()+targetNode.getName()).hashCode();
    }

    @Override
    public int hashCode() {
        return hashCode;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SkeletonGraphEdge other = (SkeletonGraphEdge) obj;
        if (this.sourceNode != other.sourceNode && (this.sourceNode == null || !this.sourceNode.equals(other.sourceNode))) {
            return false;
        }
        if (this.targetNode != other.targetNode && (this.targetNode == null || !this.targetNode.equals(other.targetNode))) {
            return false;
        }
        if (this.hashCode != other.hashCode) {
            return false;
        }
        return true;
    }

}
