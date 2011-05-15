package edu.tufts.eecs.graphtheory.collapsiblegraph.clustering.strategy;

import edu.tufts.eecs.graphtheory.collapsiblegraph.clustering.ClusterDendrogramNode;
import edu.tufts.eecs.graphtheory.collapsiblegraph.clustering.DendrogramNode;
import edu.tufts.eecs.graphtheory.collapsiblegraph.clustering.LeafDendrogramNode;
import edu.tufts.eecs.graphtheory.collapsiblegraph.node.Node;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 *
 * @author Jeremy Freeman jeremy@jgfreeman.com
 */
public class SingleLinkClusteringStrategy implements ClusteringStrategy {

    /**
     * @param graphNodes The set of logical nodes that should be arranged into a dendrogram.
     * @return DendrogramNode that is the root of the Dendrogram created by clustering
     */
    public final DendrogramNode cluster(final Set<Node> graphNodes) {

        //Map to hold the current set of Node clusters in a top-level DendrogramNode.
        //This is an optimization as each DendrogramNode has a function
        //to retrieve the set of child nodes.
        Map<DendrogramNode, Set<Node>> topClusterMap = new HashMap<DendrogramNode, Set<Node>>();
        //Map of distances to the pair of DendrogramNodes they separate
        DistanceMapper distanceMap = new DistanceMapper();

        Set<DendrogramNode> initialDNodeSet = new HashSet<DendrogramNode>();
        //This loop turns the set of Nodes from the input into a set of singleton DendrogramNodes
        for (Node collapsibleGraphNode : graphNodes) {
            //Create the DendrogramLeafNode to hold this Node
            DendrogramNode newDNodeSet = new LeafDendrogramNode(collapsibleGraphNode);

            //As these new Dendrograms are top-level, they must have nodes for topLevelClusters
            Set<Node> singletonClusterSet = new HashSet<Node>();
            singletonClusterSet.add(collapsibleGraphNode);
            topClusterMap.put(newDNodeSet, singletonClusterSet);

            //A loop to calculate the distance between this DendrogramNode and each singleton DendrogramNode that we've already added
            //In so doing this will make sure we have each of the pairings necessary
            for (DendrogramNode dNode : initialDNodeSet) {
                Set<DendrogramNode> clusterPair = new HashSet<DendrogramNode>();
                clusterPair.add(dNode);
                clusterPair.add(newDNodeSet);
                distanceMap.put(findDistance(newDNodeSet, dNode, topClusterMap), newDNodeSet, dNode);
            }
            //We do this last to avoid self-comparisons.
            initialDNodeSet.add(newDNodeSet);
        }

        //A loop that runs until the we have only a single pairing left.
        while (distanceMap.size() > 2) {
            System.out.println("Distance Map size down to" + distanceMap.size());
            distanceMap.clusterClosestPair(topClusterMap);
        }
        
        //Create and return the root of the dendrogram
        return distanceMap.clusterClosestPair(topClusterMap);
        }

    private Double findDistance(DendrogramNode firstDendrogramNode, DendrogramNode secondDendrogramNode,
            Map<DendrogramNode, Set<Node>> currentClusters) {

        double minimumDistance = Double.MAX_VALUE;

        for (Node currentFirstDendrogramNode : currentClusters.get(firstDendrogramNode)) {
            for (Node currentSecondDendrogramNode : currentClusters.get(secondDendrogramNode)) {
                double thisDistance = currentFirstDendrogramNode.getDistance(currentSecondDendrogramNode);
                if (thisDistance < minimumDistance) {
                    minimumDistance = thisDistance;
                }
            }
        }
        return new Double(minimumDistance);
    }

    private class DistanceMapper {

        private TreeMap<Double, Set<Set<DendrogramNode>>> distanceToSetMap;
        private long numOfElements;
        private Set<DendrogramNode> lowestMember;
        private Double lowestDistance;

        public DistanceMapper() {
            numOfElements = 0;
            distanceToSetMap = new TreeMap<Double, Set<Set<DendrogramNode>>>();
            lowestMember = null;
            lowestDistance = null;
        }

        public void put(Double distance, DendrogramNode firstNode, DendrogramNode secondNode) {
            Set<DendrogramNode> newSet = new HashSet<DendrogramNode>();
            newSet.add(firstNode);
            newSet.add(secondNode);

            if (!distanceToSetMap.containsKey(distance)) {
                distanceToSetMap.put(distance, new HashSet<Set<DendrogramNode>>());
            }
            distanceToSetMap.get(distance).add(newSet);
            numOfElements++;
        }

        public long size() {
            return numOfElements;
        }

        public Set<DendrogramNode> getClosestPair() {
            if (lowestMember == null) {
                calculateClosestPair();
            }
            return lowestMember;
        }
        
        private void calculateClosestPair() {
                Map.Entry<Double, Set<Set<DendrogramNode>>> lowestEntry = distanceToSetMap.firstEntry();
                lowestMember = (Set<DendrogramNode>) (lowestEntry.getValue().toArray())[0];
                lowestDistance = lowestEntry.getKey();
        }

        public Double getClosestPairDistance() {
            return distanceToSetMap.firstKey();
        }

        public DendrogramNode clusterClosestPair(Map<DendrogramNode, Set<Node>> topLevelClusterMap) {
            if (lowestMember == null) {
                calculateClosestPair();
            }
            
            Set<Set<DendrogramNode>> setToAlter = distanceToSetMap.get(distanceToSetMap.firstKey());
            setToAlter.remove(lowestMember);
            numOfElements--;
            
            //Create new ClusterDendrogramNode to pair the closest pair
            DendrogramNode newClusterDNode = new ClusterDendrogramNode(lowestMember, lowestDistance);

            //Update the topLevelClusters map to lose the old top level clusters and add the new one
            Set<Node> newTopLevelCluster = new HashSet<Node>();
            for (DendrogramNode newlyClusteredNode : lowestMember) {
                newTopLevelCluster.addAll(topLevelClusterMap.remove(newlyClusteredNode));
            }
            topLevelClusterMap.put(newClusterDNode, newTopLevelCluster);


            Map<Double, Set<Set<DendrogramNode>>> mappingsToAdd = new HashMap<Double, Set<Set<DendrogramNode>>>();

            //A loop to recalculate each distance pair that involved either of the joined pair
            Set<Double> distancesToRemove = new HashSet<Double>();
            
            for (Double distance : distanceToSetMap.keySet()) {
                Set<Set<DendrogramNode>> pairsToRemove = new HashSet<Set<DendrogramNode>>();
                
                Iterator<Set<DendrogramNode>> setIterator = distanceToSetMap.get(distance).iterator();
                while (setIterator.hasNext()) {

                    Set<DendrogramNode> preClusterPair = setIterator.next();
                    for (DendrogramNode newlyClusteredDNode : lowestMember) {
                        if (preClusterPair.contains(newlyClusteredDNode)) {
                            
                            setIterator.remove();
                            numOfElements--;
                            preClusterPair.remove(newlyClusteredDNode);
                            
                            if (preClusterPair.size() != 1) {
                                throw new RuntimeException("There's no good reason for a pair to have had anything but 2 nodes!");
                            }
                            //Sketchy way to retrieve the other node from this set
                            DendrogramNode onlyDNode = (DendrogramNode) (preClusterPair.toArray())[0];
                            
                            Double postClusterDistance = findDistance(onlyDNode, newClusterDNode, topLevelClusterMap);

                            //Replace the former node with the newly formed Cluster node
                            preClusterPair.add(newClusterDNode);
                            if(!mappingsToAdd.containsKey(postClusterDistance)) {
                                mappingsToAdd.put(postClusterDistance, new HashSet<Set<DendrogramNode>>());
                            }
                            mappingsToAdd.get(postClusterDistance).add(preClusterPair);
                        }
                    }
                }
                //If we've removed all of the entries in this distance's set, we can maybe delete that distance.
                if(distanceToSetMap.get(distance).isEmpty()) {
                    distancesToRemove.add(distance);
                }
            }
            
            for(Double distanceToAdd : mappingsToAdd.keySet())  {
                int numberBefore = distanceToSetMap.get(distanceToAdd).size();
                distanceToSetMap.get(distanceToAdd).addAll(mappingsToAdd.get(distanceToAdd));
                int numberAfter = distanceToSetMap.get(distanceToAdd).size();
                numOfElements = numOfElements + (numberAfter - numberBefore);
            }
            
            //Actually do the work of adding and removing mappings from system
            for (Double distanceToBeRemoved : distancesToRemove) {
                if(distanceToSetMap.get(distanceToBeRemoved).isEmpty()) {
                distanceToSetMap.remove(distanceToBeRemoved);
                }
            }
            

            
            lowestDistance=null;
            lowestMember=null;
            return newClusterDNode;
        }
    }
}
