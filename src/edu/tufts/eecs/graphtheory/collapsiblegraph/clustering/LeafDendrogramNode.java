/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.tufts.eecs.graphtheory.collapsiblegraph.clustering;

import edu.tufts.eecs.graphtheory.collapsiblegraph.node.Node;
import java.util.HashSet;
import java.util.Set;


/**
 *
 * @author Jeremy
 */
public class LeafDendrogramNode implements DendrogramNode {

    private Node dataNode;

    public LeafDendrogramNode(Node dataNode) {
        this.dataNode = dataNode;
    }
    
    public Node getNode() {
        return dataNode;
    }

    public Set<Node> getNodes() {
        Set<Node> nodeSet = new HashSet<Node>();
        nodeSet.add(dataNode);
        return nodeSet;
    }

         /**
         * * 
         * @param partitionDistance
         * @return
         * @deprecated Implementing DendrogramEdges necessitated making the DendrogramSlicer class for this. Use that.
         */
    @Deprecated
    public Set<DendrogramNode> partitionByDistance(double partitionDistance) {
        Set<DendrogramNode> partitionSet = new HashSet<DendrogramNode>();
        partitionSet.add(this);
        return partitionSet;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final LeafDendrogramNode other = (LeafDendrogramNode) obj;
        if (this.dataNode != other.dataNode && (this.dataNode == null || !this.dataNode.equals(other.dataNode))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return dataNode.hashCode();
    }

}
