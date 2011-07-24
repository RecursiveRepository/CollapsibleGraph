/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.tufts.eecs.graphtheory.collapsiblegraph.graphedge;

import edu.tufts.eecs.graphtheory.collapsiblegraph.graphnode.GraphNode;

/**
 *
 * @author Jeremy
 */
public class SkeletonGraphEdge implements GraphEdge {

    private GraphNode sourceNode;
    private GraphNode targetNode;
    private int hashCode;

    public SkeletonGraphEdge(GraphNode sourceNode, GraphNode targetNode) {
        this.sourceNode = sourceNode;
        this.targetNode = targetNode;
        generateHashCode();
    }

    public void setSource(GraphNode sourceNode) {
        this.sourceNode=sourceNode;
        generateHashCode();
    }

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
