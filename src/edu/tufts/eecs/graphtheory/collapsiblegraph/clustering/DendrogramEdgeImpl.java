package edu.tufts.eecs.graphtheory.collapsiblegraph.clustering;

/**
 *
 * @author Jeremy
 */
public class DendrogramEdgeImpl implements DendrogramEdge {

    DendrogramNode sourceNode;
    DendrogramNode targetNode;

    public DendrogramNode getSourceDendrogramNode() {
        return sourceNode;
    }

    public DendrogramNode getTargetDendrogramNode() {
        return targetNode;
    }

    public DendrogramEdgeImpl(DendrogramNode sourceNode, DendrogramNode targetNode) {
        this.sourceNode = sourceNode;
        this.targetNode = targetNode;
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
        return true;
    }

    @Override
    public int hashCode() {
        return sourceNode.hashCode() + targetNode.hashCode();
    }
}
