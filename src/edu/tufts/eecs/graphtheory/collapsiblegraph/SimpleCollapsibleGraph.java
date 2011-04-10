/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.tufts.eecs.graphtheory.collapsiblegraph;

import edu.tufts.eecs.graphtheory.collapsiblegraph.edge.Edge;
import edu.tufts.eecs.graphtheory.collapsiblegraph.node.Node;
import java.util.Set;

/**
 *
 * @author Jeremy
 */
public class SimpleCollapsibleGraph implements CollapsibleGraph {

private Set<Node> nodeSet;
private Set<Edge> edgeSet;

    public Set<Node> getNodes() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Set<Edge> getEdges() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public SimpleCollapsibleGraph(Set<Node> nodes, Set<Edge> edges) {
        this.nodeSet = nodes;
        this.edgeSet = edges;
    }
}
