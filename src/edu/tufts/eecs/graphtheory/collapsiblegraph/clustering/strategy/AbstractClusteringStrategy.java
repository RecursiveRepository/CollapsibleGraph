package edu.tufts.eecs.graphtheory.collapsiblegraph.clustering.strategy;

import edu.tufts.eecs.graphtheory.collapsiblegraph.clustering.ClusterDendrogramNode;
import edu.tufts.eecs.graphtheory.collapsiblegraph.clustering.Dendrograms;
import edu.tufts.eecs.graphtheory.collapsiblegraph.clustering.DendrogramEdge;
import edu.tufts.eecs.graphtheory.collapsiblegraph.clustering.DendrogramNode;
import edu.tufts.eecs.graphtheory.collapsiblegraph.clustering.LeafDendrogramNode;
import edu.tufts.eecs.graphtheory.collapsiblegraph.graphedge.GraphEdge;
import edu.tufts.eecs.graphtheory.collapsiblegraph.graphnode.GraphNode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 *
 * An abstract class representing strategies for clustering Nodes into a DendrogramNode. 
 * To define a clustering strategy, you must override findDistance with some strategy for finding the
 * distance between two DendrogramNodes (Single link or Multi link, for example).
 * @author Jeremy Freeman jeremy@jgfreeman.com
 * @version 1.0
 */
public abstract class AbstractClusteringStrategy implements ClusteringStrategy {

    private final static int NUMBER_OF_THREADS = 3; //The number of threads that should be used to calculate distances. 
    private final static int INDEXES_PER_THREAD = 3; //How many indices should be calculated per thread per time

    /** cluster is the function that turns a set of GraphNodes from some meaningful graph into a Dendrogram
     * containing those nodes. This is done as pre-processing, so that the clusters at any threshold for a distance function
     * may be viewed quickly.
     * @param graphNodes The set of logical nodes that should be arranged into a dendrogram.
     * @return DendrogramNode that is the root of the Dendrogram created by clustering
     */
    public final Dendrograms cluster(final Set<GraphNode> graphNodes, final Set<GraphEdge> graphEdges) {
        
        //An ExecutorService to oversee the multi-threaded calculation of distances
        ExecutorService distanceExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
        
        //When the clustering started
        long startTime = System.currentTimeMillis();

        //A Set of Integers corresponding to the indices that still hold valid DendrogramNodes in 
        //the array of dendrogramNodes. This shrinks by 1 each time 2 DendrogramNodes are clustered together
        Set<Integer> dendrogramNodeIndices = new HashSet<Integer>(graphNodes.size());

        //The actual array of DendrogramNodes
        DendrogramNode[] dendrogramNodes = new DendrogramNode[graphNodes.size()];
        //The actual array of DendrogramEdges
        DendrogramEdge[] dendrogramEdges = new DendrogramEdge[graphEdges.size()];

        //Two lists, *coindexed with dendrogramNodes* array, that hold the edge indices that are incoming and outgoing 
        //to and from each node. That is, the 4th element in the outgoingEdgeIndices List is all of the edges that are
        //outgoing from the GraphNode at index 4 in dendrogramNodes
        List<List<Integer>> outgoingEdgeIndices = new ArrayList<List<Integer>>(graphNodes.size());
        List<List<Integer>> incomingEdgeIndices = new ArrayList<List<Integer>>(graphNodes.size());

        for (int i = 0; i < graphNodes.size(); i++) {
            outgoingEdgeIndices.add(null);
            incomingEdgeIndices.add(null);
        }
        
        //List holding the "topmost" clusters as an optimization. *Co-indexed with dendrogramNodes.*
        //By maintaining a List of Arrays of Nodes corresponding to the array of dendrogramNodes, we are able to 
        //instantly retrieve the set of nodes that is contained in the tree that a given dendrogramNode is the root of, inclusive. 
        List<GraphNode[]> topClusterList = new ArrayList<GraphNode[]>(graphNodes.size());
        
        //Array holding the index of each dendrogramNode's nearest Neighbor. *Co-indexed with dendrogramNodes*
        int[] nearestNeighborIndices = new int[graphNodes.size()];
        //Array holding the distance to each dendrogramNode's nearest Neighbor. *Co-indexed with dendrogramNodes*
        double[] nearestNeighborDistances = new double[graphNodes.size()];

        //A map that goes from GraphNodes to DendrogramNodes, important for building DendrogramEdges 
        Map<GraphNode, Integer> gNodeToDNodeMap = new HashMap<GraphNode, Integer>();

        //This loop turns the set of GraphNodes from the input into a set of singleton DendrogramNodes
        int index = 0;
        for (GraphNode collapsibleGraphNode : graphNodes) {
            //Create the DendrogramLeafNode to hold this GraphNode
            DendrogramNode newDNode = new LeafDendrogramNode(collapsibleGraphNode);

            gNodeToDNodeMap.put(collapsibleGraphNode, index);
            //As these new Dendrograms are top-level, they must have nodes for topLevelClusters
            GraphNode[] singletonClusterSet = new GraphNode[1];
            singletonClusterSet[0] = collapsibleGraphNode;
            topClusterList.add(singletonClusterSet);

            dendrogramNodes[index] = newDNode;
            dendrogramNodeIndices.add(index);
            index++;
        }
        
        //This loop turns the set of GraphNodes from the input into a set of  DendrogramEdges
        //It also adds the new DendrogramEdge indices to the incomingEdgeIndices and outgoingEdgeIndices lists 
        DendrogramEdge[] dEdges = new DendrogramEdge[graphEdges.size()];
        int edgeIndex = 0;
        for (GraphEdge graphEdge : graphEdges) {
            DendrogramNode sourceDNode = dendrogramNodes[gNodeToDNodeMap.get(graphEdge.getSource())];
            DendrogramNode targetDNode = dendrogramNodes[gNodeToDNodeMap.get(graphEdge.getTarget())];
            DendrogramEdge newDEdge = new DendrogramEdge(sourceDNode, targetDNode);
            dEdges[edgeIndex] = newDEdge;
            dendrogramEdges[edgeIndex] = newDEdge;

            if (incomingEdgeIndices.get(gNodeToDNodeMap.get(graphEdge.getTarget())) == null) {
                List<Integer> sourceList = new ArrayList<Integer>();
                sourceList.add(edgeIndex);
                incomingEdgeIndices.set(gNodeToDNodeMap.get(graphEdge.getTarget()), sourceList);
            } else {
                incomingEdgeIndices.get(gNodeToDNodeMap.get(graphEdge.getTarget())).add(edgeIndex);
            }

            if (outgoingEdgeIndices.get(gNodeToDNodeMap.get(graphEdge.getSource())) == null) {
                List<Integer> targetList = new ArrayList<Integer>();
                targetList.add(edgeIndex);
                outgoingEdgeIndices.set(gNodeToDNodeMap.get(graphEdge.getSource()), targetList);
            } else {
                outgoingEdgeIndices.get(gNodeToDNodeMap.get(graphEdge.getSource())).add(edgeIndex);
            }
            edgeIndex++;
        }

        //Set an array to hold the Future results of the distance calculation threads
        List<Future<NearestNeighbor>> nearestNeighbors = new ArrayList<Future<NearestNeighbor>>(dendrogramNodes.length);
        
        //Calculate the distance between each dendrogramNode and each other. by shipping it off to the executor
        for (int i = 0; i < dendrogramNodes.length; i++) {
            nearestNeighbors.add(distanceExecutor.submit(new MinimumDistanceTask(i, i, dendrogramNodes, topClusterList, dendrogramNodeIndices)));
        }

        //Retrieve the results of the multi-threaded distance calculation by retrieving the nearest neighbors and their distances
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
        int mostRecentDNodeIndex = -1; //A Hackish way to retrieve the root graph node when we get there

        while (dendrogramNodeIndices.size() > 1) {
            //Initialize the minimum distance to a very high value
            double minDistance = Double.MAX_VALUE;
            int minDistanceIndex = -1; //Initialize the index of that distance to a nonsense value
            
            //Check to see which DendrogramNode's nearest neighbor is in fact the closest overall.
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
            Set<DendrogramNode> newDendrogramNodePair = new HashSet<DendrogramNode>();
            newDendrogramNodePair.add(dendrogramNodes[index1]);
            newDendrogramNodePair.add(dendrogramNodes[index2]);
            DendrogramNode newCluster = new ClusterDendrogramNode(newDendrogramNodePair, minDistance, null);
            dendrogramNodes[index1].setParentDNode(newCluster);
            dendrogramNodes[index2].setParentDNode(newCluster);
            
            //A map that maps from the former child DendrogramNodes to the new parent DendrogramNode
            Map<DendrogramNode, DendrogramNode> oldNodeToNewNode = new HashMap<DendrogramNode, DendrogramNode>();
            oldNodeToNewNode.put(dendrogramNodes[index1], newCluster);
            oldNodeToNewNode.put(dendrogramNodes[index2], newCluster);

            dendrogramNodes[index1] = newCluster;  //Store the new DendrogramNode in the array where the lower-indexed of the two children used to be
            
            //Maps that maps from DendrogramNodes to the indices of edges that already use that DendrogramNode in that capacity
            Map<DendrogramNode, Integer> sourceDNodesToDEdgeIndices = new HashMap<DendrogramNode, Integer>();
            Map<DendrogramNode, Integer> targetDNodesToEdgeIndices = new HashMap<DendrogramNode, Integer>();
            //A map to hold updates to the Edge array (we must collect changes and do them all at the end, otherwise
            //We'll end up modifying the very structure we're trying to read
            Map<Integer, DendrogramEdge> changes = new TreeMap<Integer, DendrogramEdge>();

            //Update all of the edges that tough either of the newly clusterd nodes. 
            if (incomingEdgeIndices.get(index1) != null) {
                updateEdges(incomingEdgeIndices.get(index1), dendrogramEdges, oldNodeToNewNode, minDistance, sourceDNodesToDEdgeIndices, targetDNodesToEdgeIndices, true, newDendrogramNodePair, changes);
            }
            if (incomingEdgeIndices.get(index2) != null) {
                updateEdges(incomingEdgeIndices.get(index2), dendrogramEdges, oldNodeToNewNode, minDistance, sourceDNodesToDEdgeIndices, targetDNodesToEdgeIndices, true, newDendrogramNodePair, changes);
            }
            if (outgoingEdgeIndices.get(index1) != null) {
                updateEdges(outgoingEdgeIndices.get(index1), dendrogramEdges, oldNodeToNewNode, minDistance, sourceDNodesToDEdgeIndices, targetDNodesToEdgeIndices, false, newDendrogramNodePair, changes);
            }
            if (outgoingEdgeIndices.get(index2) != null) {
                updateEdges(outgoingEdgeIndices.get(index2), dendrogramEdges, oldNodeToNewNode, minDistance, sourceDNodesToDEdgeIndices, targetDNodesToEdgeIndices, false, newDendrogramNodePair, changes);
            }
            //Apply the changes gathered from the 4 calls above 
            for (Entry<Integer, DendrogramEdge> change : changes.entrySet()) {
                dendrogramEdges[change.getKey()] = change.getValue();
            }
            
            //Update the edge Index Lists to reflect the fact that we've clustered the nodes at index1 and index2 into a new node at index1
            if (incomingEdgeIndices.get(index1) == null ) {
                incomingEdgeIndices.set(index1, incomingEdgeIndices.get(index2));
            } else {
                if(incomingEdgeIndices.get(index2) != null)
                incomingEdgeIndices.get(index1).addAll(incomingEdgeIndices.get(index2));
            }
            if (outgoingEdgeIndices.get(index1) == null) {
                outgoingEdgeIndices.set(index1, outgoingEdgeIndices.get(index2));
            } else {
                if(outgoingEdgeIndices.get(index2) != null)
                outgoingEdgeIndices.get(index1).addAll(outgoingEdgeIndices.get(index2));
            }

            //Combine the two sets of Nodes to update the topClusterList
            GraphNode[] newArray = new GraphNode[topClusterList.get(index1).length + topClusterList.get(index2).length];
            System.arraycopy(topClusterList.get(index1), 0, newArray, 0, topClusterList.get(index1).length);
            System.arraycopy(topClusterList.get(index2), 0, newArray, topClusterList.get(index1).length, topClusterList.get(index2).length);
            topClusterList.set(index1, newArray);

            //Remove the no-longer-used dendrogram index from the list of indices
            dendrogramNodeIndices.remove(index2);
            mostRecentDNodeIndex = index1;

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
            //Update the nearestNeighbors
            for (int i = 0; i < newNearestNeighbors.size(); i++) {
                NearestNeighbor replacementNeighbor = null;
                try {
                    replacementNeighbor = newNearestNeighbors.get(i).get();
                } catch (Exception e) {
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
        Dendrograms finalDendrogram = new Dendrograms(dendrogramNodes[mostRecentDNodeIndex], dendrogramEdges[0]);
        return finalDendrogram;
    }

    /**
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
            List<GraphNode[]> currentClusters);

    /**
     * An inner class to hold the results of a closest-neighbor search
     */
    protected static class NearestNeighbor {

        private final double distance;
        private final int index;

        protected NearestNeighbor(double distance, int index) {
            this.distance = distance;
            this.index = index;
        }

        /**
         * A function to return the distance of the closest neighbor.
         * @returns the distance to this nearest neighbor
         */
        protected double getDistance() {
            return distance;
        }

        /**
         * A function to return the index of the closest neighbor.
         * @returns the index of this nearest neighbor.
         */
        protected int getIndex() {
            return index;
        }
    }

    /**
     * An inner class to be fed up to the Executor. It finds the nearest neighbor and distance to it, using the overridden findDistance function.
     * Each task takes a range of indices to check.
     */
    protected class MinimumDistanceTask implements Callable<NearestNeighbor> {

        
        int minIndexToCheck;
        int maxIndexToCheck;
        DendrogramNode[] dendrogramNodes;
        List<GraphNode[]> topClusterList;
        Set<Integer> indicesToCheck;
/**
         * This function takes a range of indices to check, an array of DendrogramNodes that it's checking, a List of GraphNode arrays
         * as an optimization so it can see the entire set of GraphNodes in each DendrogramNode's subtree, and 
         * @param minIndexToCheck
         * @param maxIndexToCheck
         * @param dendrogramNodes
         * @param topClusterList
         * @param indicesToCheck 
         */
        public MinimumDistanceTask(int minIndexToCheck, int maxIndexToCheck, DendrogramNode[] dendrogramNodes, List<GraphNode[]> topClusterList,
                Set<Integer> indicesToCheck) {
            this.minIndexToCheck = minIndexToCheck;
            this.maxIndexToCheck = maxIndexToCheck;
            this.topClusterList = topClusterList;
            this.indicesToCheck = indicesToCheck;
        }

        public NearestNeighbor call() throws Exception {
            double minimumDistance = Double.MAX_VALUE;
            int index = -1;

            //Iterate over the indices to update. 
            for (int indexToCheck = minIndexToCheck; indexToCheck <= maxIndexToCheck; indexToCheck++) {
                for (Integer thisIndex : indicesToCheck) {
                    //Don't bother calculating anything's distance to itself
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

    /*
     * updateEdges is called to update either incoming or outgoing edges that had been attached to one of two newly clustered DedrogramNodes.
     * isSource is to say whether we're updating the edges where a given node was the SOURCE, (false if we're looking at the edges where given node was the target
     * 
     */
    private void updateEdges(List<Integer> edgeIndices, DendrogramEdge[] dendrogramEdges, Map<DendrogramNode, DendrogramNode> oldNodeToNewNode, double minDistance,
            Map<DendrogramNode, Integer> sourceDNodesToEdges, Map<DendrogramNode, Integer> targetDNodesToEdges, boolean isSource, Set<DendrogramNode> newPair, Map<Integer, DendrogramEdge> changes) {
        
        //Iterate over the edge indices provided in the input list
        for (Integer edgeIndex : edgeIndices) {
            //If the the DendrogramNode we're looking at got clustered, and we've already seen the other node touching this edge (in the same source/target role),
            if ((isSource && targetDNodesToEdges.containsKey(dendrogramEdges[edgeIndex].getTargetDendrogramNode()) && newPair.contains(dendrogramEdges[edgeIndex].getSourceDendrogramNode()))
                    || (!isSource && sourceDNodesToEdges.containsKey(dendrogramEdges[edgeIndex].getSourceDendrogramNode()) && newPair.contains(dendrogramEdges[edgeIndex].getTargetDendrogramNode()))) {
                //Then there is already an edge going from this new cluster to that other node in the correct direction, and we should just add this edge to that
                //edge's children                
                Integer previouslyCreatedEdgeIndex = null;

                if (isSource) {
                    previouslyCreatedEdgeIndex = targetDNodesToEdges.get(dendrogramEdges[edgeIndex].getTargetDendrogramNode());
                } else {
                    previouslyCreatedEdgeIndex = sourceDNodesToEdges.get(dendrogramEdges[edgeIndex].getSourceDendrogramNode());
                }

                changes.get(previouslyCreatedEdgeIndex).getChildEdges().add(dendrogramEdges[edgeIndex]);
                dendrogramEdges[edgeIndex].setParentDEdge(changes.get(previouslyCreatedEdgeIndex));

                changes.put(edgeIndex, changes.get(previouslyCreatedEdgeIndex));
            } else {
                //There wasn't already a pre-existing edge corresponding to this one, so we'll have to make a new one
                //Source node tries to see if the source node of our edge was one of the two re-clustered 
                //If so, add the target to the list of nodes there's already an edge to
                //if not, then the source remains as the source of this edge
                DendrogramNode sourceNode = oldNodeToNewNode.get(dendrogramEdges[edgeIndex].getSourceDendrogramNode());
                if (sourceNode == null) {
                    sourceNode = dendrogramEdges[edgeIndex].getSourceDendrogramNode();
                } else {
                    targetDNodesToEdges.put(dendrogramEdges[edgeIndex].getTargetDendrogramNode(), edgeIndex);
                }
                //Vice versa of above
                DendrogramNode targetNode = oldNodeToNewNode.get(dendrogramEdges[edgeIndex].getTargetDendrogramNode());
                if (targetNode == null) {
                    targetNode = dendrogramEdges[edgeIndex].getTargetDendrogramNode();
                } else {
                    sourceDNodesToEdges.put(dendrogramEdges[edgeIndex].getSourceDendrogramNode(), edgeIndex);
                }

                //Create the new edge
                DendrogramEdge newEdge = new DendrogramEdge(sourceNode, targetNode);

                newEdge.setDistance(minDistance);
                newEdge.getChildEdges().add(dendrogramEdges[edgeIndex]);
                dendrogramEdges[edgeIndex].setParentDEdge(newEdge); 
                //Add it to the list of changes to be made
                changes.put(edgeIndex, newEdge);

            }
        }
    }
}
