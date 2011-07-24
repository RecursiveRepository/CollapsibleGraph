/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.tufts.eecs.graphtheory.collapsiblegraph.graphnode;

import edu.tufts.eecs.graphtheory.collapsiblegraph.graphedge.GraphEdge;
import java.util.Set;

/**
 *
 * @author Jeremy
 */
public interface GraphNode {
    public Set<GraphEdge> getOutgoingEdgeSet();
    public Set<GraphEdge> getIncomingEdgeSet();
    public String getName();

    @Override
    public int hashCode();

    public double getDistance(GraphNode otherNode);
}
