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
    private final static int NUMBER_OF_THREADS = 3;
    private final static int INDEXES_PER_THREAD = 3;

    public final DendrogramNode cluster(final Set<Node> graphNodes) {

      //  ExecutorService distanceExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
        long startTime = System.currentTimeMillis();

        //Map to hold the current set of Node clusters in a top-level DendrogramNode.
        //This is an optimization as each DendrogramNode has a function
        //to retrieve the set of child nodes.
        List<Set<Node>> topClusterMap = new ArrayList<Set<Node>>(graphNodes.size());

        //The number of different pairwise distances in the system.


        //An ArrayList to hold the actual dendrogram nodes as they're being clustered
        Set<Integer> dendrogramNodeIndices = new HashSet<Integer>(graphNodes.size());

        DendrogramNode[] dendrogramNodes = new DendrogramNode[graphNodes.size()];
        int[] nearestNeighborIndices = new int[graphNodes.size()];
        double[] nearestNeighborDistances = new double[graphNodes.size()];



        //This loop turns the set of Nodes from the input into a set of singleton DendrogramNodes
        int index = 0;
        for (Node collapsibleGraphNode : graphNodes) {
            //Create the DendrogramLeafNode to hold this Node
            DendrogramNode newDNode = new LeafDendrogramNode(collapsibleGraphNode);

            //As these new Dendrograms are top-level, they must have nodes for topLevelClusters
            Set<Node> singletonClusterSet = new HashSet<Node>();
            singletonClusterSet.add(collapsibleGraphNode);
            topClusterMap.add(singletonClusterSet);

            dendrogramNodes[index] = newDNode;
            dendrogramNodeIndices.add(index);
            index++;
        }


        for (int i = 0; i < dendrogramNodes.length; i++) {
            double minDistance = Double.MAX_VALUE;
            int minDistanceIndex = -1;
            for (int j = 0; j < dendrogramNodes.length; j++) {
                if (i == j) {
                    continue;
                }
                double thisDistance = findDistance(i, j, topClusterMap);
                if (thisDistance < minDistance) {
                    minDistance = thisDistance;
                    minDistanceIndex = j;
                }
            }
            nearestNeighborDistances[i] = minDistance;
            nearestNeighborIndices[i] = minDistanceIndex;
            if(i%50 == 0 ) System.out.println(i + " done.");
        }

        int newlyAssignedIndex = -1;
        while (dendrogramNodeIndices.size() > 1) {
            double minDistance = Double.MAX_VALUE;
            int minDistanceIndex = -1;
            for (int dendrogramNodeIndex : dendrogramNodeIndices) {
                if (nearestNeighborDistances[dendrogramNodeIndex] < minDistance) {
                    minDistance = nearestNeighborDistances[dendrogramNodeIndex];
                    minDistanceIndex = dendrogramNodeIndex;
                }
            }


            int index1 = minDistanceIndex;
            int index2 = nearestNeighborIndices[minDistanceIndex];

            Set<DendrogramNode> newPair = new HashSet<DendrogramNode>();

            Set<Node> innerNodes = topClusterMap.get(index1);
            innerNodes.addAll(topClusterMap.get(index2));

            //Safe to do because index2 > index1 

            newPair.add(dendrogramNodes[index1]);

            newPair.add(dendrogramNodes[index2]);
            DendrogramNode newCluster = new ClusterDendrogramNode(newPair, nearestNeighborDistances[index1]);
            topClusterMap.set(index1, innerNodes);
            dendrogramNodes[index1] = newCluster;
            
            dendrogramNodeIndices.remove(index2);
            newlyAssignedIndex = index1;

           
            for (Integer dendrogramNodeIndex : dendrogramNodeIndices) {
                if (nearestNeighborIndices[dendrogramNodeIndex.intValue()] == index1 || nearestNeighborIndices[dendrogramNodeIndex.intValue()] == index2) {
                    double newMinDistance = Double.MAX_VALUE;
                    int newMinDistanceIndex = -1;
                    for (Integer neighborDendrogramNodeIndex : dendrogramNodeIndices) {
                        if(neighborDendrogramNodeIndex.equals(dendrogramNodeIndex)) continue;
                        double thisDistance = findDistance(dendrogramNodeIndex, neighborDendrogramNodeIndex, topClusterMap);
                        if (thisDistance < newMinDistance) {
                            newMinDistance = thisDistance;
                            newMinDistanceIndex = neighborDendrogramNodeIndex;
                        }
                    }
                    nearestNeighborDistances[dendrogramNodeIndex] = newMinDistance;
                    nearestNeighborIndices[dendrogramNodeIndex] = newMinDistanceIndex;
                }


            }
            long nowTime = System.currentTimeMillis();
            long elapsedTime = nowTime - startTime;
            System.out.println("Removed 1 DNode, " + dendrogramNodeIndices.size() + " remaining in " + (elapsedTime / 1000));
        }

        return dendrogramNodes[newlyAssignedIndex];
    }

    private double findDistance(int firstDendrogramNodeIndex, int secondDendrogramNodeIndex,
            List<Set<Node>> currentClusters) {

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

        int minIndexToCheck;
        int maxIndexToCheck;
        List<DendrogramNode> dendrogramNodes;
        List<Set<Node>> topClusterList;

        public MinimumDistanceTask(int minIndexToCheck, int maxIndexToCheck, List<DendrogramNode> dendrogramNodes, List<Set<Node>> topClusterList) {
            this.minIndexToCheck = minIndexToCheck;
            this.maxIndexToCheck = maxIndexToCheck;
            this.dendrogramNodes = dendrogramNodes;
            this.topClusterList = topClusterList;
        }

        public MinimumPair call() throws Exception {
            double minimumDistance = Double.MAX_VALUE;
            int index1 = -1;
            int index2 = -1;

            for (int indexToCheck = minIndexToCheck; indexToCheck <= maxIndexToCheck; indexToCheck++) {
                for (int i = indexToCheck + 1; i < dendrogramNodes.size(); i++) {
                    double thisDistance = findDistance(indexToCheck, i, topClusterList);
                    if (thisDistance < minimumDistance) {
                        minimumDistance = thisDistance;
                        index1 = indexToCheck;
                        index2 = i;
                    }
                }
            }

            return new MinimumPair(minimumDistance, index1, index2);
        }
    }
}
