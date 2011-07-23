/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.tufts.eecs.graphtheory.collapsiblegraph.clustering;

import edu.tufts.eecs.graphtheory.collapsiblegraph.node.Node;
import java.io.InvalidObjectException;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;


/**
 *
 * @author Jeremy
 */
public class LeafDendrogramNode implements DendrogramNode, Serializable {

    private Node dataNode;
    private DendrogramNode parentNode;
  
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
    @Override
    public int hashCode() {
        return dataNode.hashCode();
    }

    public Set<DendrogramNode> partitionByDistance(double partitionDistance) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public DendrogramNode getParent() {
        return parentNode;
    }
    
    public void setParent(DendrogramNode parentNode) {
        this.parentNode = parentNode;
    }
    
    public void readObjectNoData() throws InvalidObjectException {
        throw new InvalidObjectException("Stream data required");
    }
}
