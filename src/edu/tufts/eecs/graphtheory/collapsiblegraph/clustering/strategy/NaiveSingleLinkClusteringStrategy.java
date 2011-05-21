package edu.tufts.eecs.graphtheory.collapsiblegraph.clustering.strategy;

import edu.tufts.eecs.graphtheory.collapsiblegraph.clustering.ClusterDendrogramNode;
import edu.tufts.eecs.graphtheory.collapsiblegraph.clustering.DendrogramNode;
import edu.tufts.eecs.graphtheory.collapsiblegraph.clustering.LeafDendrogramNode;
import edu.tufts.eecs.graphtheory.collapsiblegraph.node.Node;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 *
 * @author Jeremy Freeman jeremy@jgfreeman.com
 */
public class NaiveSingleLinkClusteringStrategy implements ClusteringStrategy {

    /**
     * @param graphNodes The set of logical nodes that should be arranged into a dendrogram.
     * @return DendrogramNode that is the root of the Dendrogram created by clustering
     */
    
    private final static int NUMBER_OF_THREADS = 3;
    public final DendrogramNode cluster(final Set<Node> graphNodes) {

        Executor distanceExector = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
        
        //Map to hold the current set of Node clusters in a top-level DendrogramNode.
        //This is an optimization as each DendrogramNode has a function
        //to retrieve the set of child nodes.
        List<Set<Node>> topClusterMap = new ArrayList<Set<Node>>(graphNodes.size());

        //The number of different pairwise distances in the system.


        //An ArrayList to hold the actual dendrogram nodes as they're being clustered
        ArrayList<DendrogramNode> dendrogramNodes = new ArrayList<DendrogramNode>(graphNodes.size());


        //This loop turns the set of Nodes from the input into a set of singleton DendrogramNodes
        for (Node collapsibleGraphNode : graphNodes) {
            //Create the DendrogramLeafNode to hold this Node
            DendrogramNode newDNode = new LeafDendrogramNode(collapsibleGraphNode);

            //As these new Dendrograms are top-level, they must have nodes for topLevelClusters
            Set<Node> singletonClusterSet = new HashSet<Node>();
            singletonClusterSet.add(collapsibleGraphNode);
            topClusterMap.add(singletonClusterSet);

            dendrogramNodes.add(newDNode);
        }

        while (dendrogramNodes.size() > 1) {
            double minimumDistance = Double.MAX_VALUE;
            int index1 = -1;
            int index2 = -1;
            for (int i = 0; i < dendrogramNodes.size(); i++) {
                for (int j = i + 1; j < dendrogramNodes.size(); j++) {
                    double thisDistance = findDistance(i, j, dendrogramNodes, topClusterMap);
                    if (thisDistance < minimumDistance) {
                        minimumDistance = thisDistance;
                        index1 = i;
                        index2 = j;
                    }
                }
            }
            Set<DendrogramNode> newPair = new HashSet<DendrogramNode>();

            Set<Node> innerNodes = topClusterMap.get(index1);
            innerNodes.addAll(topClusterMap.get(index2));

            //Safe to do because index2 > index1 
            topClusterMap.remove(index2);
            newPair.add(dendrogramNodes.get(index1));

            newPair.add(dendrogramNodes.get(index2));
            DendrogramNode newCluster = new ClusterDendrogramNode(newPair, minimumDistance);
            topClusterMap.set(index1, innerNodes);
            dendrogramNodes.set(index1, newCluster);


            dendrogramNodes.remove(index2);
            System.out.println("Removed 1 DNode, " + dendrogramNodes.size() + " remaining.");
        }

        return dendrogramNodes.get(0);
    }

    private double findDistance(int firstDendrogramNodeIndex, int secondDendrogramNodeIndex,
            List<DendrogramNode> dendrogramNodes, List<Set<Node>> currentClusters) {
        
        double minimumDistance = Double.MAX_VALUE;
        for (Node currentFirstDendrogramNode : currentClusters.get(firstDendrogramNodeIndex)) {
            for (Node currentSecondDendrogramNode : currentClusters.get(secondDendrogramNodeIndex)) {
                double thisDistance = currentFirstDendrogramNode.getDistance(currentSecondDendrogramNode);
                if (thisDistance < minimumDistance) {
                    minimumDistance = thisDistance;
                }
            }
        }
        return minimumDistance;
    }

    public static class MinimumPair {

        private final double distance;
        private final int index1;
        private final int index2;

        private MinimumPair(double distance, int index1, int index2) {
            this.distance = distance;
            this.index1 = index1;
            this.index2 = index2;
        }

        private double getDistance() {
            return distance;
        }

        private int getIndex1() {
            return index1;
        }

        private int getIndex2() {
            return index2;
        }
    }
    
    private class minimumDistanceTask implements Callable<MinimumPair> {

        int indexToCheck;
        public minimumDistanceTask(int indexToCheck) {
            this.indexToCheck = indexToCheck;
        }
                
        public MinimumPair call() throws Exception {
                for (int j = i + 1; j < dendrogramNodes.size(); j++) {
                    double thisDistance = findDistance(i, j, dendrogramNodes, topClusterMap);
                    if (thisDistance < minimumDistance) {
                        minimumDistance = thisDistance;
                        index1 = i;
                        index2 = j;
                    }
                }        }
        
    }
}
