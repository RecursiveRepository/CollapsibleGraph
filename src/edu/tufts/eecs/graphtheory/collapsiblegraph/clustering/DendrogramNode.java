package edu.tufts.eecs.graphtheory.collapsiblegraph.clustering;

import edu.tufts.eecs.graphtheory.collapsiblegraph.graphnode.GraphNode;
import java.util.List;

/**
 * DendrogramNode is an interface created for clustering. In  clustering, a Binary Dendrogram Tree will be created, made up of these DendrogramNodes. 
 * The DendrogramNode class exists so that the clustering can occur agnostic to whether the nodes being clustered are the LeafDendrogramNodes (the data-storing leaves)
 * and ClusterDendrogramNodes (the glue-nodes that hold the tree together).
 * 
 * @author Jeremy 
 */
public interface DendrogramNode{
/**
     * Returns the List of data-bearing GraphNodes contained by this DendrogramNode or any of its children. 
     * @return The set of GraphNodes contained by all DendrogramNodes in the subtree rooted at this node.
     */
public List<GraphNode> getGraphNodes();
/**
 * @return the DendrogramNode of which this one is a child. If the DendrogramNode has no parent, this returns null.
 */
public DendrogramNode getParentDNode();
/**
 * @param parentDNode the DendrogramNode that is this DendrogramNode's parent.
 */
public void setParentDNode(DendrogramNode parentDNode);

}
