package edu.tufts.eecs.graphtheory.collapsiblegraph.graphnode;

/**
 * An interface to represent the data-bearing GraphNodes in the actual semantic graph.
 * This can be easily implemented for whatever type of GraphNode makes sense for your purpose.
 * @author Jeremy
 */
public interface GraphNode {
    /**
     * 
     * @return a String representing the name of this node (for display, primarily)
     */
    public String getName();
    /**
     * 
     * @param otherNode some other GraphNode of the same implementation type as this one 
     * @return the Distance between this node and the otherNode.
     */
    public double getDistance(GraphNode otherNode);
    
    @Override
    public int hashCode();
    @Override 
    public boolean equals(Object o);

    
}
