package edu.tufts.eecs.graphtheory.collapsiblegraph.viewing;

import edu.tufts.eecs.graphtheory.collapsiblegraph.clustering.ClusterDendrogramNode;
import edu.tufts.eecs.graphtheory.collapsiblegraph.clustering.Dendrograms;
import edu.tufts.eecs.graphtheory.collapsiblegraph.clustering.DendrogramEdge;
import edu.tufts.eecs.graphtheory.collapsiblegraph.clustering.DendrogramNode;
import edu.tufts.eecs.graphtheory.collapsiblegraph.clustering.LeafDendrogramNode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A class to slice a Dendrogram at a given distance. 
 * @author Jeremy
 */
public class DendrogramSlicer {

    /**
     * This function cuts the Dendrogram at a specified distance and returns the Slice. It does this by recursively traversing both
     * the GraphNode and GraphEdge dendrograms. 
     * @param distance the distance at which to cut the Dendrogram
     * @param dendrogram the Dendrogram to be sliced
     * @return the DendrogramSlice that represents how the Dendrogram looks at exactly that distance
     */
    public DendrogramSlice partitionByDistance(double distance, Dendrograms dendrogram) {
        DendrogramNode rootDNode = dendrogram.getRootNode();
        DendrogramEdge rootDEdge = dendrogram.getRootEdge();

        //A list to hold the DendrogramNodes that will go in this slice
        List<DendrogramNode> dendrogramNodes = new ArrayList<DendrogramNode>();
        //A Set to hold the DendrogramEdges that will go in this slice
        Set<DendrogramEdge> dendrogramEdges = new HashSet<DendrogramEdge>();
        
        //A stack of DendrogramNodes that must be checked 
        List<DendrogramNode> dNodesToCheck = new ArrayList<DendrogramNode>();
        //We start with just the root Node
        dNodesToCheck.add(rootDNode);

        //While there are still DendrogramNodes to check...
        while (!dNodesToCheck.isEmpty()) {
            DendrogramNode nodeToCheck = dNodesToCheck.remove(dNodesToCheck.size() - 1);
            //If this node is a leaf node, then it has data, isn't a cluster, and should be included in the DendrogramSlice
            if (nodeToCheck instanceof LeafDendrogramNode) {
                dendrogramNodes.add(nodeToCheck);
            } else {
                ClusterDendrogramNode thisCluster = (ClusterDendrogramNode) nodeToCheck;
                //If this cluster was built at a distance greater than the one we're slicing to, then we should check its children
                if (thisCluster.getDistance() > distance) {
                    dNodesToCheck.addAll(thisCluster.getChildDNodes());
                } else {
                    //else, add it to the set of dendrogramNodes for the slice
                    dendrogramNodes.add(nodeToCheck);
                }
            }

        }
        //A stack of DendrogramEdges to be checked
        List<DendrogramEdge> edgesToCheck = new ArrayList<DendrogramEdge>();
        edgesToCheck.add(rootDEdge);

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
