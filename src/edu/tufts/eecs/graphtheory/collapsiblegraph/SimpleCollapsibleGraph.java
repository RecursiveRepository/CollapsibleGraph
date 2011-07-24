/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.tufts.eecs.graphtheory.collapsiblegraph;

import edu.tufts.eecs.graphtheory.collapsiblegraph.graphedge.GraphEdge;
import edu.tufts.eecs.graphtheory.collapsiblegraph.graphnode.GraphNode;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Jeremy
 */
public class SimpleCollapsibleGraph implements CollapsibleGraph {

private Set<GraphNode> nodeSet;
private Set<GraphEdge> edgeSet;
private Map<String, GraphNode> nodeNameMap;

    public Set<GraphNode> getNodes() {
        return nodeSet;
    }

    public Set<GraphEdge> getEdges() {
        return edgeSet;
    }

    public SimpleCollapsibleGraph(Set<GraphNode> nodes, Set<GraphEdge> edges) {
        this.nodeSet = nodes;
        this.edgeSet = edges;
    }

    public SimpleCollapsibleGraph(Set<GraphNode> nodeSet, Set<GraphEdge> edgeSet, Map<String, GraphNode> nodeNameMap) {
        this.nodeSet = nodeSet;
        this.edgeSet = edgeSet;
        this.nodeNameMap = nodeNameMap;
    }

    public Map<String, GraphNode> getNodeNameMap() {
        return nodeNameMap;
    }
}
