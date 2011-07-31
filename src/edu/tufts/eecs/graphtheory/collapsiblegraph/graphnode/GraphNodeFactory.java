package edu.tufts.eecs.graphtheory.collapsiblegraph.graphnode;

/**
 *
 * @author Jeremy
 */
public interface GraphNodeFactory {
    public GraphNode buildGraphNodeFromString(String input) throws GraphNodeFactoryException;
}
