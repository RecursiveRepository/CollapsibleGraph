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
public class ClusterDendrogramNode implements DendrogramNode{

    private double distance;
    private Set<DendrogramNode> childNodes;

        public ClusterDendrogramNode(Set<DendrogramNode> childNodes, double distance) {
        this.distance = distance;
        this.childNodes = childNodes;
    }

        public Set<DendrogramNode> partitionByDistance(double partitionDistance) {
        Set<DendrogramNode> clusterSet = new HashSet<DendrogramNode>();

        if (partitionDistance >= distance) {
            clusterSet.add(this);
        } else {
            for (DendrogramNode childNode : childNodes) {
                clusterSet.addAll(childNode.partitionByDistance(partitionDistance));
            }
        }
        return clusterSet;
    }

    public Set<Node> getNodes() {
        Set<Node> nodeSet = new HashSet<Node>();
        for(DendrogramNode childNode : childNodes) {
            nodeSet.addAll(childNode.getNodes());
        }
        return nodeSet;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ClusterDendrogramNode other = (ClusterDendrogramNode) obj;
        if (Double.doubleToLongBits(this.distance) != Double.doubleToLongBits(other.distance)) {
            return false;
        }
        if (this.childNodes != other.childNodes && (this.childNodes == null || !this.childNodes.equals(other.childNodes))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        long sum = 0;
        for (DendrogramNode dn : childNodes) {
         sum += dn.hashCode();
        }
        Long sumObject = new Long(sum);
        return sumObject.hashCode();
    }
}
