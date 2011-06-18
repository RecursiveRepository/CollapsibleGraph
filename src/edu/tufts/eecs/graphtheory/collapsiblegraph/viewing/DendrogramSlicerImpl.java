/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.tufts.eecs.graphtheory.collapsiblegraph.viewing;

import edu.tufts.eecs.graphtheory.collapsiblegraph.clustering.ClusterDendrogramNode;
import edu.tufts.eecs.graphtheory.collapsiblegraph.clustering.DendrogramEdge;
import edu.tufts.eecs.graphtheory.collapsiblegraph.clustering.DendrogramEdgeImpl;
import edu.tufts.eecs.graphtheory.collapsiblegraph.clustering.DendrogramNode;
import edu.tufts.eecs.graphtheory.collapsiblegraph.clustering.LeafDendrogramNode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Jeremy
 */
public class DendrogramSlicerImpl implements DendrogramSlicer {

    public DendrogramSlice partitionByDistance(double distance, DendrogramNode rootNode, DendrogramEdge[] edges) {
        List<DendrogramNode> dendrogramNodes = new ArrayList<DendrogramNode>();
        Set<DendrogramEdge> dendrogramEdges = new HashSet<DendrogramEdge>();
        Map<DendrogramNode, DendrogramNode> dNodeToParentDNode = new HashMap<DendrogramNode, DendrogramNode>();

        List<DendrogramNodeAndParent> nodesToCheck = new ArrayList<DendrogramNodeAndParent>();
        nodesToCheck.add(new DendrogramNodeAndParent(rootNode, null));

        while (!nodesToCheck.isEmpty()) {
            DendrogramNodeAndParent thisNodeAndParent = nodesToCheck.remove(0);
            DendrogramNode thisNode = thisNodeAndParent.getNode();
            DendrogramNode parentNode = thisNodeAndParent.getParent();

            if (thisNode instanceof LeafDendrogramNode) {
                dendrogramNodes.add(thisNode);
                dNodeToParentDNode.put(thisNode, parentNode == null ? thisNode : parentNode);
            } else if (thisNode instanceof ClusterDendrogramNode) {
                if (((ClusterDendrogramNode) thisNode).getDistance() > distance) {
                    for (DendrogramNode childNode : ((ClusterDendrogramNode) thisNode).getChildNodes()) {
                        nodesToCheck.add(new DendrogramNodeAndParent(childNode, null));
                    }
                } else {
                    if (parentNode == null) {
                        parentNode = thisNode;
                        dendrogramNodes.add(thisNode);
                    }
                    
                    dNodeToParentDNode.put(thisNode, parentNode);
                    
                    for (DendrogramNode childNode : ((ClusterDendrogramNode) thisNode).getChildNodes()) {
                        nodesToCheck.add(new DendrogramNodeAndParent(childNode, parentNode));
                    }
                }
            }
        }
        
        for(DendrogramEdge edge : dendrogramEdges) {
            dendrogramEdges.add(new DendrogramEdgeImpl(dNodeToParentDNode.get(edge.getSourceDendrogramNode()),
                                                       dNodeToParentDNode.get(edge.getTargetDendrogramNode())));
        }

        DendrogramNode[] nodesArray = new DendrogramNode[1];
        DendrogramEdge[] edgesArray = new DendrogramEdge[1];


        return new DendrogramSlice(dendrogramNodes.toArray(nodesArray), dendrogramEdges.toArray(edgesArray));
    }

    private static class DendrogramNodeAndParent {

        DendrogramNode node;
        DendrogramNode parent;

        public DendrogramNodeAndParent(DendrogramNode node, DendrogramNode parent) {
            this.node = node;
            this.parent = parent;
        }

        public DendrogramNode getNode() {
            return node;
        }

        public DendrogramNode getParent() {
            return parent;
        }
    }
}
