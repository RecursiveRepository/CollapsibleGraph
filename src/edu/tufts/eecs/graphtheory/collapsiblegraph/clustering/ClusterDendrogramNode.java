package edu.tufts.eecs.graphtheory.collapsiblegraph.clustering;

import edu.tufts.eecs.graphtheory.collapsiblegraph.graphnode.GraphNode;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author Jeremy
 * The ClusterDendrogramNode is a kind of DendrogramNode that acts as glue for the clustering algorithm.
 * It holds links to its child DendrogramNodes in a List, and stores the distance at which those DendrogramNodes join together. 
 */
public class ClusterDendrogramNode implements DendrogramNode, Serializable {

    private double distance; //The distance at which these childnodes come together int his cluster
    private List<DendrogramNode> childDNodes; //A list of the childnodes
    private DendrogramNode parentDNode; //The parent DendrogramNode of this node.

    /**
     * The constructor for ClusterDendrogramNode. It is required to have the parentNode in the constructor for serialization/deserialization to work.
     * This may be set to null, but should be set to its correct value before serializing.
     * @param childNodes The pair of nodes that are being clustered together.
     * @param distance The distance at which this clustering is occurring
     * @param parentNode The parentNode, which may be null
     */
    public ClusterDendrogramNode(Collection<DendrogramNode> childNodes, double distance, DendrogramNode parentNode) {
        this.distance = distance;
        this.childDNodes = new ArrayList<DendrogramNode>(childNodes);
        this.parentDNode = parentNode;
    }

    /**Returns the List of data-bearing GraphNodes contained by this DendrogramNode or any of its children. As this can end up traversing the entire tree,
     * this operation is in O(n) so you may want to avoid it.
     * @return the List of GraphNodes in this ClusterDendrogramNode's subtree
     */
    public List<GraphNode> getGraphNodes() {
        List<GraphNode> gNodeSet = new ArrayList<GraphNode>();
        for (DendrogramNode childDNode : childDNodes) {
            gNodeSet.addAll(childDNode.getGraphNodes());
        }
        return gNodeSet;
    }

    /**
     * 
     * @return the distance at which this ClusterDendrogramNode was created
     */
    public double getDistance() {
        return distance;
    }

    /**
     * @return the pair of DendrogramNodes that this ClusterDendrogramNode clusters together
     */
    public List<DendrogramNode> getChildDNodes() {
        return childDNodes;
    }

    public DendrogramNode getParentDNode() {
        return parentDNode;
    }

    public void setParentDNode(DendrogramNode parentNode) {
        this.parentDNode = parentNode;
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
        if (this.childDNodes != other.childDNodes && (this.childDNodes == null || !this.childDNodes.equals(other.childDNodes))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        long sum = 0;
        for (DendrogramNode dn : childDNodes) {
            sum += dn.hashCode();
        }
        Long sumObject = new Long(sum);
        return sumObject.hashCode();
    }
}
