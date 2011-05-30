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
public interface CollapsibleGraph {

    public Set<Node> getNodes();
    public Set<Edge> getEdges();
    public Map<String, Node> getNodeNameMap();
}
