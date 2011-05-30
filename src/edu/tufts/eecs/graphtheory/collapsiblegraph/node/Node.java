/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.tufts.eecs.graphtheory.collapsiblegraph.node;

import edu.tufts.eecs.graphtheory.collapsiblegraph.edge.Edge;
import java.util.Set;

/**
 *
 * @author Jeremy
 */
public interface Node {
    public Set<Edge> getOutgoingEdgeSet();
    public Set<Edge> getIncomingEdgeSet();
    public String getName();

    @Override
    public int hashCode();

    public double getDistance(Node otherNode);
}
