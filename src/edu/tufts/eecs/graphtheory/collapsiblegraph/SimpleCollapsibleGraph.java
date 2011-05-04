/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.tufts.eecs.graphtheory.collapsiblegraph;

import edu.tufts.eecs.graphtheory.collapsiblegraph.edge.Edge;
import edu.tufts.eecs.graphtheory.collapsiblegraph.node.Node;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Jeremy
 */
public class SimpleCollapsibleGraph implements CollapsibleGraph {

private Set<Node> nodeSet;
private Set<Edge> edgeSet;
private Map<String, Node> nodeNameMap;

    public Set<Node> getNodes() {
        return nodeSet;
    }

    public Set<Edge> getEdges() {
        return edgeSet;
    }

    public SimpleCollapsibleGraph(Set<Node> nodes, Set<Edge> edges) {
        this.nodeSet = nodes;
        this.edgeSet = edges;
    }

    public SimpleCollapsibleGraph(Set<Node> nodeSet, Set<Edge> edgeSet, Map<String, Node> nodeNameMap) {
        this.nodeSet = nodeSet;
        this.edgeSet = edgeSet;
        this.nodeNameMap = nodeNameMap;
    }

    public Map<String, Node> getNodeNameMap() {
        return nodeNameMap;
    }
}
