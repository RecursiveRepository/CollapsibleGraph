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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 *
 * @author Jeremy Freeman jeremy@jgfreeman.com
 */
public class NaiveSingleLinkClusteringStrategy implements ClusteringStrategy {

    /**
     * @param graphNodes The set of logical nodes that should be arranged into a dendrogram.
     * @return DendrogramNode that is the root of the Dendrogram created by clustering
     */
    private final static int NUMBER_OF_THREADS = 4;

    public final DendrogramNode cluster(final Set<Node> graphNodes) {

        ExecutorService distanceExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS) ;

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
            List<Future<MinimumPair>> executorResults = new ArrayList<Future<MinimumPair>>();
            for (int i = 0; i < dendrogramNodes.size()-1; i++) {
                executorResults.add(distanceExecutor.submit(new MinimumDistanceTask(i, dendrogramNodes, topClusterMap)));
            }
            double minimumDistance = Double.MAX_VALUE;
            MinimumPair smallestPair = null;
            for(int i = 0; i < executorResults.size(); i++) {
                Future<MinimumPair> pair = executorResults.get(i);
                try {
                if(pair.get().getDistance() < minimumDistance) {
                    minimumDistance = pair.get().getDistance();
                    smallestPair = pair.get();
                }
                } catch (Exception e) {
                    System.out.println("FUCK!");
                    System.exit(1);
                }
            }
            int index1 = smallestPair.getIndex1();
            int index2 = smallestPair.getIndex2();
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

    protected class MinimumDistanceTask implements Callable<MinimumPair> {

        int indexToCheck;
        List<DendrogramNode> dendrogramNodes;
        List<Set<Node>> topClusterList;

        public MinimumDistanceTask(int indexToCheck, List<DendrogramNode> dendrogramNodes, List<Set<Node>> topClusterList) {
            this.indexToCheck = indexToCheck;
            this.dendrogramNodes = dendrogramNodes;
            this.topClusterList = topClusterList;
        }

        public MinimumPair call() throws Exception {
            double minimumDistance = Double.MAX_VALUE;
            int index1 = -1;
            int index2 = -1;
            for (int i = indexToCheck+1; i < dendrogramNodes.size(); i++) {
                double thisDistance = findDistance(indexToCheck, i, dendrogramNodes, topClusterList);
                if (thisDistance < minimumDistance) {
                    minimumDistance = thisDistance;
                    index1 = indexToCheck;
                    index2 = i;
                }
            }
            return new MinimumPair(minimumDistance, index1, index2);
        }
    }
}
