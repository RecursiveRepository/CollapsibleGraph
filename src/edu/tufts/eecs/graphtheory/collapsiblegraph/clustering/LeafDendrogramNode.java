package edu.tufts.eecs.graphtheory.collapsiblegraph.clustering;

import edu.tufts.eecs.graphtheory.collapsiblegraph.graphnode.GraphNode;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * The LeafDendrogramNode is a DendrogramNode that serves as a simple wrapper around a data-bearing GraphNode for the purpose of clustering.
 * Each one stores one GraphNode.
 * @author Jeremy
 */
public class LeafDendrogramNode implements DendrogramNode, Serializable {

    private GraphNode graphNode;
    private DendrogramNode parentDNode;

    /**
     * 
     * @param graphNode the data-storing GraphNode that this LeafDendrogramNode wraps for clustering.
     */
    public LeafDendrogramNode(GraphNode graphNode) {
        this.graphNode = graphNode;
    }

    /**
     * 
     * @return the data-storing GraphNode that this LeafDendrogramNode wraps.
     */
    public GraphNode getGraphNode() {
        return graphNode;
    }

    /**
     * 
     * @return the single node that this LeafDendrogramNode wraps, as the single entry in a List
     */
    public List<GraphNode> getGraphNodes() {
        List<GraphNode> singletonNodeList = new ArrayList<GraphNode>();
        singletonNodeList.add(graphNode);
        return singletonNodeList;
    }

    /**
     * 
     * @return the DendrogramNode that serves as this one's parent
     */
    @Override
    public DendrogramNode getParentDNode() {
        return parentDNode;
    }

    /**
     * 
     * @param parentNode the DendrogramNode that serves as this one's parent
     */
    public void setParentDNode(DendrogramNode parentNode) {
        this.parentDNode = parentNode;
    }

    @Override
    public int hashCode() {
        return graphNode.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof LeafDendrogramNode)) {
            return false;
        }
        if (graphNode == ((LeafDendrogramNode) o).getGraphNode()) {
            return true;
        }
        return false;

    }
}
