package edu.tufts.eecs.graphtheory.collapsiblegraph.graphnode;

import java.io.Serializable;

/**
 * A skeleton implementation of the GraphNode class. This has no datatype except a String representing its own name. 
 * @author Jeremy
 */
public class SkeletonGraphNode implements GraphNode, Serializable {
    private String name;
   
    /**
     * 
     * @param name the name of this node
     */
    public SkeletonGraphNode(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    /**
     * A goofball distance function that returns the difference in the length of the names.
     * @param otherNode the other node to compare to
     * @return the distance between the nodes as a double.
     */
    public double getDistance(GraphNode otherNode) {
        return (double) Math.abs(name.length() - otherNode.getName().length());
    }

    
    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SkeletonGraphNode other = (SkeletonGraphNode) obj;
        if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return name;
    }

    
}
