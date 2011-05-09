/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.tufts.eecs.graphtheory.collapsiblegraph.clustering.strategy;

import edu.tufts.eecs.graphtheory.collapsiblegraph.clustering.ClusterDendrogramNode;
import edu.tufts.eecs.graphtheory.collapsiblegraph.clustering.DendrogramNode;
import edu.tufts.eecs.graphtheory.collapsiblegraph.clustering.LeafDendrogramNode;
import edu.tufts.eecs.graphtheory.collapsiblegraph.node.Node;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 *
 * @author Jeremy
 */
public class SingleLinkClusteringStrategy implements ClusteringStrategy {

    public DendrogramNode cluster(Set<Node> collapsibleGraphNodes) {
        
        //Node to hold the current set of clusters in a top-level DendrogramNode. This is an optimization as each DendrogramNode has a function
        //to retrieve the set of child nodes.
        Map<DendrogramNode, Set<Node>> topLevelClusters = new HashMap<DendrogramNode, Set<Node>>();
        //Set of all DendrogramNodes
        Set<DendrogramNode> dendrogramNodeSet = new HashSet<DendrogramNode>();
        //Map of distances to the pair of DendrogramNodes they separate
        TreeMap<Double, Set<DendrogramNode>> distanceMap = new TreeMap<Double, Set<DendrogramNode>>();
        
        //This loop turns the set of Nodes from the input into a set of singleton DendrogramNodes
        for (Node collapsibleGraphNode : collapsibleGraphNodes) {
            //Create the DendrogramLeafNode to hold this Node
            DendrogramNode newDendrogramNode = new LeafDendrogramNode(collapsibleGraphNode);
            dendrogramNodeSet.add(newDendrogramNode);

            //As these new Dendrograms are top-level, they must have nodes for topLevelClusters
            Set<Node> singleSet = new HashSet<Node>();
            singleSet.add(collapsibleGraphNode);
            topLevelClusters.put(newDendrogramNode, singleSet);
            
            //A loop to calculate the distance between this DendrogramNode and each singleton DendrogramNode that we've already added
            //In so doing this will make sure we have each of the pairings necessary
            for(DendrogramNode dendrogramNode : dendrogramNodeSet) {
                Set<DendrogramNode> clusterPair = new HashSet<DendrogramNode>();
                clusterPair.add(dendrogramNode);
                clusterPair.add(newDendrogramNode);
                distanceMap.put(findDistance(newDendrogramNode, dendrogramNode, topLevelClusters), clusterPair);
            }
        }
        
        //A loop that runs until the we have only a single pairing left.
        while(distanceMap.keySet().size()>1) {
           //If we're going to iterate over the keys in a map, we can't be altering the map concurrently.
           //Instead, let's record the alterations and do them safely at the end of the iteration over the map's keys.
           Set<Double> mapEntriesToRemove = new HashSet<Double>();
           Map<Double, Set<DendrogramNode>> mapEntriesToAdd = new HashMap<Double, Set<DendrogramNode>>();
           
           //Retrieve the lowest-distance pair 
           Map.Entry<Double, Set<DendrogramNode>> lowestDistancePair = distanceMap.firstEntry();
           //Store the lowest distance Double and the pair of DendrogramNodes that they represent
           Double formerClosestPairDistance = lowestDistancePair.getKey();
           Set<DendrogramNode> formerClosestPairSet = lowestDistancePair.getValue();
           //Create a new DendrogramNodeset to join the pair

           DendrogramNode newClusterNode = new ClusterDendrogramNode(formerClosestPairSet, formerClosestPairDistance);
           
           //Update the topLevelClusters map to lose the old top level clusters and add the new one
           Set<Node> newTopLevelCluster = new HashSet<Node>();
           for(DendrogramNode newlyClusteredNode : formerClosestPairSet) {
               newTopLevelCluster.addAll(topLevelClusters.remove(newlyClusteredNode));
           }
           topLevelClusters.put(newClusterNode, newTopLevelCluster);    
           
           //List this old pairing for removal from the distance map
           distanceMap.remove(formerClosestPairDistance); 
           
           //A loop to recalculate each distance pair that involved either of the joined pair
           for(Double currentDistance : distanceMap.keySet()) {
               Set<DendrogramNode> clusterPair = distanceMap.get(currentDistance);
               DendrogramNode pairingNode = null;
               for(DendrogramNode eachDendrogramNode : clusterPair) {
                   if(formerClosestPairSet.contains(eachDendrogramNode)) {
                           formerClosestPairSet.remove(eachDendrogramNode);
                           if(formerClosestPairSet.size()!=1) {
                               throw new RuntimeException("There's no good reason for a pair to have had anything but 2 nodes!");
                           }
                   //Sketchy way to retrieve the other node from this set
                   DendrogramNode onlyNode = (DendrogramNode)(formerClosestPairSet.toArray())[0];
                   Double newClusterDistance = findDistance(onlyNode, eachDendrogramNode, topLevelClusters);
                   
                   //Replace the former node with the newly formed Cluster node
                   formerClosestPairSet.add(newClusterNode);
                   mapEntriesToAdd.put(newClusterDistance, formerClosestPairSet);
                   }
               }
           }
           //Actually do the work of adding and removing mappings from system
           for(Double distanceToBeRemoved : mapEntriesToRemove) {
               distanceMap.remove(distanceToBeRemoved);
           }
           distanceMap.putAll(mapEntriesToAdd);
        }
        
        Map.Entry<Double, Set<DendrogramNode>> lastEntry = distanceMap.lastEntry();
        DendrogramNode rootOfDendrogram = new ClusterDendrogramNode(lastEntry.getValue(), lastEntry.getKey());
        return rootOfDendrogram;
    }
    
    private Double findDistance(DendrogramNode firstDendrogramNode,DendrogramNode secondDendrogramNode,
         Map<DendrogramNode, Set<Node>> currentClusters) {
        
        double minimumDistance = Double.MAX_VALUE;
        for(Node currentFirstDendrogramNode: currentClusters.get(firstDendrogramNode)) {
            for(Node currentSecondDendrogramNode : currentClusters.get(secondDendrogramNode) ) {
                double thisDistance = currentFirstDendrogramNode.getDistance(currentSecondDendrogramNode);
                if( thisDistance < minimumDistance) {
                    minimumDistance = thisDistance;
                }
            }
        }
        return new Double(minimumDistance);
    }
}
