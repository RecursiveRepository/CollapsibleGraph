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
 * An abstract class representing strategies for clustering Nodes into a DendrogramNode. 
 * To define a clustering strategy, you must override findDistance.
 * @author Jeremy Freeman jeremy@jgfreeman.com
 * @version 1.0
 */
public abstract class AbstractClusteringStrategy implements ClusteringStrategy {

    private final static int NUMBER_OF_THREADS = 8;
    private final static int INDEXES_PER_THREAD = 3;

    /** cluster is the function that turns as set of Nodes from some meaningful graph into a Dendrogram
     * containing those nodes. This is done as pre-processing, so that the clusters at any threshold for a distance function
     * may be viewed quickly.
     * @param graphNodes The set of logical nodes that should be arranged into a dendrogram.
     * @return DendrogramNode that is the root of the Dendrogram created by clustering
     */
    public final DendrogramNode cluster(final Set<Node> graphNodes) {

        ExecutorService distanceExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
        long startTime = System.currentTimeMillis();



        //A Set of Integers corresponding to the indices that still hold valid DendrogramNodes in 
        //the array of dendrogramNodes. This shrinks by 1 each time 2 DendrogramNodes are clustered together
        Set<Integer> dendrogramNodeIndices = new HashSet<Integer>(graphNodes.size());

        //The actual array of DendrogramNodes
        DendrogramNode[] dendrogramNodes = new DendrogramNode[graphNodes.size()];

        //List holding the "topmost" clusters as an optimization. *Co-indexed with dendrogramNodes.*
        //By maintaining a list of Sets of Nodes corresponding to the array of dendrogramNodes, we are able to 
        //instantly retrieve the set of nodes that is contained in the tree that a given dendrogramNode is the root of, inclusive. 
        List<Set<Node>> topClusterList = new ArrayList<Set<Node>>(graphNodes.size());
        //Array holding the index of each dendrogramNode's nearest Neighbor. *Co-indexed with dendrogramNodes*
        int[] nearestNeighborIndices = new int[graphNodes.size()];
        //Array holding the distance to each dendrogramNode's nearest Neighbor. *Co-indexed with dendrogramNodes*
        double[] nearestNeighborDistances = new double[graphNodes.size()];



        //This loop turns the set of Nodes from the input into a set of singleton DendrogramNodes
        int index = 0;
        for (Node collapsibleGraphNode : graphNodes) {
            //Create the DendrogramLeafNode to hold this Node
            DendrogramNode newDNode = new LeafDendrogramNode(collapsibleGraphNode);

            //As these new Dendrograms are top-level, they must have nodes for topLevelClusters
            Set<Node> singletonClusterSet = new HashSet<Node>();
            singletonClusterSet.add(collapsibleGraphNode);
            topClusterList.add(singletonClusterSet);

            dendrogramNodes[index] = newDNode;
            dendrogramNodeIndices.add(index);
            index++;
        }


        List<Future<NearestNeighbor>> nearestNeighbors = new ArrayList<Future<NearestNeighbor>>(dendrogramNodes.length);
        //Calculate the distance between each dendrogramNode and each other.
        for (int i = 0; i < dendrogramNodes.length; i++) {
            nearestNeighbors.add(distanceExecutor.submit(new MinimumDistanceTask(i, i, dendrogramNodes, topClusterList, dendrogramNodeIndices)));
        }

        for (int i = 0; i < dendrogramNodes.length; i++) {
            NearestNeighbor thisNearestNeighbor = null;
            try {
                thisNearestNeighbor = nearestNeighbors.get(i).get();
            } catch (Exception e) {
                System.err.println("Interruption!");
                e.printStackTrace(System.err);
            }
            nearestNeighborDistances[i] = thisNearestNeighbor.getDistance();
            nearestNeighborIndices[i] = thisNearestNeighbor.getIndex();
            long nowTime = System.currentTimeMillis();
            long elapsedTime = nowTime - startTime;

            if (i % 200 == 0) {
                System.out.println(i + " done. Time taken: " + (elapsedTime / 1000));
            }
        }

        //A loop that clusters the closest pair of DendrogramNodes repeatedly until there's only 1 left.
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

            //Collect the indices of the closest pair
            int index1 = minDistanceIndex;
            int index2 = nearestNeighborIndices[minDistanceIndex];

            //Make a new DendrogramNode to cluster this closest pair together
            Set<DendrogramNode> newPair = new HashSet<DendrogramNode>();
            newPair.add(dendrogramNodes[index1]);
            newPair.add(dendrogramNodes[index2]);
            DendrogramNode newCluster = new ClusterDendrogramNode(newPair, nearestNeighborDistances[index1]);

            //Combine the two sets of Nodes to update the topClusterList
            Set<Node> innerNodes = topClusterList.get(index1);
            innerNodes.addAll(topClusterList.get(index2));
            topClusterList.set(index1, innerNodes);
            dendrogramNodes[index1] = newCluster;

            //Remove the no-longer-used dendrogram index from the list of indices
            dendrogramNodeIndices.remove(index2);
            newlyAssignedIndex = index1;

            //A loop to go through all of the dendrogramNodes to see whose nearestNeighbor was altered 
            //by the clustering and updating their distance accordingly. 
            List<Integer> indicesToUpdate = new ArrayList<Integer>();
            List<Future<NearestNeighbor>> newNearestNeighbors = new ArrayList<Future<NearestNeighbor>>();
            for (Integer dendrogramNodeIndex : dendrogramNodeIndices) {
                int thisIndex = dendrogramNodeIndex.intValue();
                if (nearestNeighborIndices[dendrogramNodeIndex.intValue()] == index1 || nearestNeighborIndices[dendrogramNodeIndex.intValue()] == index2) {
                    indicesToUpdate.add(dendrogramNodeIndex);
                    newNearestNeighbors.add(distanceExecutor.submit(new MinimumDistanceTask(thisIndex, thisIndex, dendrogramNodes, topClusterList, dendrogramNodeIndices)));
                }
            }
            
            for (int i = 0; i < newNearestNeighbors.size(); i++) {
                NearestNeighbor replacementNeighbor = null;
                try {
                replacementNeighbor = newNearestNeighbors.get(i).get();
                } catch (Exception e){
                    System.err.println("Problem with threading!");
                    e.printStackTrace(System.err);
                }
                nearestNeighborDistances[indicesToUpdate.get(i).intValue()] = replacementNeighbor.getDistance();
                nearestNeighborIndices[indicesToUpdate.get(i).intValue()] = replacementNeighbor.getIndex();
            }

            long nowTime = System.currentTimeMillis();
            long elapsedTime = nowTime - startTime;

            System.out.println(
                    "Removed 1 DNode, " + dendrogramNodeIndices.size() + " remaining in " + (elapsedTime / 1000));
        }
        distanceExecutor.shutdown();
        return dendrogramNodes[newlyAssignedIndex];
    }

    /*
     * This function should be overridden with your own distance measuring strategy. 
     * You are passed the 2 indices into currentClusters that hold the sets of nodes that represent the clusters.
     * For example, in a Single Link strategy, you would want to find the shortest distance between
     * any node in the first set to any node in the second set.
     * @param firstDendrogramNodeIndex the index into currentClusters that holds the first cluster to be compared
     * @param secondDendrogramNodeIndex the index into currentClusters that holds the second cluster to be compared
     * @param currentClusters the List of all clusters, each index holds a Set<Node> that holds all the Nodes in a given cluster
     * @return a double representing the distance between the two clusters
     */
    protected abstract double findDistance(int firstDendrogramNodeIndex, int secondDendrogramNodeIndex,
            List<Set<Node>> currentClusters);

    /*
     * An inner class to hold the results of a closest-neighbor search
     */
    public static class NearestNeighbor {

        private final double distance;
        private final int index;

        private NearestNeighbor(double distance, int index) {
            this.distance = distance;
            this.index = index;
        }

        /*
         * A function to return the distance of the closest neighbor.
         * @returns the distance to this nearest neighbor
         */
        private double getDistance() {
            return distance;
        }

        /*
         * A function to return the index of the closest neighbor.
         * @returns the index of this nearest neighbor.
         */
        private int getIndex() {
            return index;
        }
    }

    protected class MinimumDistanceTask implements Callable<NearestNeighbor> {

        int minIndexToCheck;
        int maxIndexToCheck;
        DendrogramNode[] dendrogramNodes;
        List<Set<Node>> topClusterList;
        Set<Integer> indicesToCheck;

        public MinimumDistanceTask(int minIndexToCheck, int maxIndexToCheck, DendrogramNode[] dendrogramNodes, List<Set<Node>> topClusterList,
                Set<Integer> indicesToCheck) {
            if(minIndexToCheck < 0 ) {
                System.out.println("WTF MATE");
            }
            this.minIndexToCheck = minIndexToCheck;
            this.maxIndexToCheck = maxIndexToCheck;
            this.dendrogramNodes = dendrogramNodes;
            this.topClusterList = topClusterList;
            this.indicesToCheck = indicesToCheck;
        }

        public NearestNeighbor call() throws Exception {
            double minimumDistance = Double.MAX_VALUE;
            int index = -1;

            for (int indexToCheck = minIndexToCheck; indexToCheck <= maxIndexToCheck; indexToCheck++) {
                for (Integer thisIndex : indicesToCheck) {
                    if(thisIndex<0) {
                        System.err.println("Alarm! Alarm!");
                    }
                    if(indexToCheck<0) {
                        System.err.println("Alert! Alert");
                    }
                    if (thisIndex.intValue() == indexToCheck) {
                        continue;
                    }
                    double thisDistance = findDistance(indexToCheck, thisIndex.intValue(), topClusterList);
                    if (thisDistance < minimumDistance) {
                        minimumDistance = thisDistance;
                        index = thisIndex.intValue();
                    }
                }
            }

            return new NearestNeighbor(minimumDistance, index);
        }
    }
}
