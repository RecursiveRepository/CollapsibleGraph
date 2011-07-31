package edu.tufts.eecs.graphtheory.collapsiblegraph;

import edu.tufts.eecs.graphtheory.collapsiblegraph.graphedge.GraphEdge;
import edu.tufts.eecs.graphtheory.collapsiblegraph.graphnode.GraphNode;
import java.util.Set;

/**
 *
 * @author Jeremy
 */
public class Graph  {

private Set<GraphNode> nodeSet;
private Set<GraphEdge> edgeSet;


    public Set<GraphNode> getNodes() {
        return nodeSet;
    }

    public Set<GraphEdge> getEdges() {
        return edgeSet;
    }

    public Graph(Set<GraphNode> nodes, Set<GraphEdge> edges) {
        this.nodeSet = nodes;
        this.edgeSet = edges;
    }
}