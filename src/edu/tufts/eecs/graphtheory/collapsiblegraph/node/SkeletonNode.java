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
public class SkeletonNode implements Node {
private String name;
private Set<Edge> incomingEdgeSet;
private Set<Edge> outgoingEdgeSet;

    public SkeletonNode(Set<Edge> incomingEdgeSet, Set<Edge> outgoingEdgeSet) {
        this.incomingEdgeSet=incomingEdgeSet; 
        this.outgoingEdgeSet=outgoingEdgeSet;
    }
    public Set<Edge> getOutgoingEdgeSet() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Set<Edge> getIncomingEdgeSet() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getName() {
        return name;
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
        final SkeletonNode other = (SkeletonNode) obj;
        if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
            return false;
        }
        return true;
    }

}
