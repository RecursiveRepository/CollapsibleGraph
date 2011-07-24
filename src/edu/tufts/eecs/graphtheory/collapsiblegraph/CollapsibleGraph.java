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
public interface CollapsibleGraph {

    public Set<GraphNode> getNodes();
    public Set<GraphEdge> getEdges();
    public Map<String, GraphNode> getNodeNameMap();
}
