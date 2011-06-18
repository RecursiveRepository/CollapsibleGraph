/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.tufts.eecs.graphtheory.collapsiblegraph.viewing;

import edu.tufts.eecs.graphtheory.collapsiblegraph.clustering.ClusterDendrogramNode;
import edu.tufts.eecs.graphtheory.collapsiblegraph.clustering.DendrogramEdge;
import edu.tufts.eecs.graphtheory.collapsiblegraph.clustering.DendrogramNode;
import edu.tufts.eecs.graphtheory.collapsiblegraph.clustering.LeafDendrogramNode;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Jeremy
 */
public class DendrogramSlicerImpl implements DendrogramSlicer {

    public DendrogramSlice partitionByDistance(double distance, DendrogramNode rootNode, DendrogramEdge[] edges) {
        List<DendrogramNode> dendrogramNodes = new ArrayList<DendrogramNode>();
        List<DendrogramEdge> dendrogramEdges = new ArrayList<DendrogramEdge>();

        List<DendrogramNode> nodesToCheck = new ArrayList<DendrogramNode>();
        nodesToCheck.add(rootNode);

        while (!nodesToCheck.isEmpty()) {
            DendrogramNode thisNode = nodesToCheck.remove(0);
            if (thisNode instanceof LeafDendrogramNode) {
                dendrogramNodes.add(thisNode);
            } else if (thisNode instanceof ClusterDendrogramNode) {
                if (((ClusterDendrogramNode) thisNode).getDistance() > distance) {
                    nodesToCheck.addAll(((ClusterDendrogramNode) thisNode).getChildNodes());
                } else {
                    dendrogramNodes.add(thisNode);
                }
            }
        }
        DendrogramNode[] nodesArray = new DendrogramNode[1];
        DendrogramEdge[] edgesArray = new DendrogramEdge[1];
        
    return new DendrogramSlice(dendrogramNodes.toArray(nodesArray), dendrogramEdges.toArray(edgesArray));
    }
}
