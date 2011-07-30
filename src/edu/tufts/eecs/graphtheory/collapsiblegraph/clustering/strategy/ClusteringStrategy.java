package edu.tufts.eecs.graphtheory.collapsiblegraph.clustering.strategy;

import edu.tufts.eecs.graphtheory.collapsiblegraph.clustering.Dendrograms;
import edu.tufts.eecs.graphtheory.collapsiblegraph.graphedge.GraphEdge;
import edu.tufts.eecs.graphtheory.collapsiblegraph.graphnode.GraphNode;
import java.util.Set;

/**
 * An interface to represent any given strategy for Clustering the GraphNodes into a Dendrogram
 * @author Jeremy
 */
public interface ClusteringStrategy {

/**
     * 
     * @param theNodes The set of GraphNodes to be clustered together
     * @param theEdges The set of GraphEdges to be clustered together
     * @return A Dendrogram holding the Clustering built buy this ClusteringStrategy
     */
public Dendrograms cluster(Set<GraphNode> graphNodes, Set<GraphEdge> graphEdges);

}
