/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.tufts.eecs.graphtheory.collapsiblegraph.viewing;

import edu.tufts.eecs.graphtheory.collapsiblegraph.clustering.ClusterDendrogramNode;
import edu.tufts.eecs.graphtheory.collapsiblegraph.clustering.Dendrogram;
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

    public DendrogramSlice partitionByDistance(double distance, Dendrogram dendrogram) {
        DendrogramNode rootNode = dendrogram.getRootNode();
        DendrogramEdge rootEdge = dendrogram.getRootEdge();

        List<DendrogramNode> dendrogramNodes = new ArrayList<DendrogramNode>();
        Set<DendrogramEdge> dendrogramEdges = new HashSet<DendrogramEdge>();
        Map<DendrogramNode, DendrogramNode> dNodeToParentDNode = new HashMap<DendrogramNode, DendrogramNode>();

        List<DendrogramNode> nodesToCheck = new ArrayList<DendrogramNode>();
        nodesToCheck.add(rootNode);

        while (!nodesToCheck.isEmpty()) {
            DendrogramNode nodeToCheck = nodesToCheck.remove(nodesToCheck.size() - 1);
            if (nodeToCheck instanceof LeafDendrogramNode) {
                dendrogramNodes.add(nodeToCheck);
            } else {
                ClusterDendrogramNode thisCluster = (ClusterDendrogramNode) nodeToCheck;
                if (thisCluster.getDistance() > distance) {
                    nodesToCheck.addAll(thisCluster.getChildNodes());
                } else {
                    dendrogramNodes.add(nodeToCheck);
                }
            }

        }

        List<DendrogramEdge> edgesToCheck = new ArrayList<DendrogramEdge>();
        edgesToCheck.add(rootEdge);

        while (!edgesToCheck.isEmpty()) {
            DendrogramEdge edgeToCheck = edgesToCheck.remove(edgesToCheck.size() - 1);
            if (edgeToCheck.getDistance() > distance) {
                edgesToCheck.addAll(edgeToCheck.getChildEdges());
            } else {
                dendrogramEdges.add(edgeToCheck);
            }
        }


        DendrogramNode[] nodesArray = new DendrogramNode[1];
        DendrogramEdge[] edgesArray = new DendrogramEdge[1];


        return new DendrogramSlice(dendrogramNodes.toArray(nodesArray), dendrogramEdges.toArray(edgesArray));
    }


}
