package edu.tufts.eecs.graphtheory.collapsiblegraph.graphedge;

import edu.tufts.eecs.graphtheory.collapsiblegraph.graphnode.GraphNode;

/**
 * An interface to represent the actual edges in the data-related graph 
 * @author Jeremy
 */
public interface GraphEdge {

    /**
     * Sets the Source Node of this edge
     * @param sourceNode is the source Node of this edge
     */
    public void setSource(GraphNode sourceNode);

    /**
     * Sets the Target Node of this edge
     * @param targetNode is the target Node of this edge
     */
    public void setTarget(GraphNode targetNode);
    
    /**
     * @return the Source Node of this edge
     */
    public GraphNode getSource();
    /** 
     * 
     * @return the Target Node of this Edge
     */
    public GraphNode getTarget();

    @Override
    public int hashCode();

    @Override
    public boolean equals(Object o);
}
