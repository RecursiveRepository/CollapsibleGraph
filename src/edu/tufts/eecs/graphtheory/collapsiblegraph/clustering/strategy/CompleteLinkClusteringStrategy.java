package edu.tufts.eecs.graphtheory.collapsiblegraph.clustering.strategy;

import edu.tufts.eecs.graphtheory.collapsiblegraph.graphnode.GraphNode;
import java.util.List;

/**
 * A ClusteringStrategy using the Complete Link method of clustering,
 * wherein the distance between two clusters is defined as the maximum distance
 * between any node in one cluster and any node in the other.
 * @author jeremy
 */
public class CompleteLinkClusteringStrategy extends AbstractClusteringStrategy {
    
       @Override    
        protected double findDistance(int firstDendrogramNodeIndex, int secondDendrogramNodeIndex,
            List<GraphNode[]> currentClusters) {

        double maximumDistance = Double.MIN_VALUE;
        for (GraphNode currentFirstDendrogramNode : currentClusters.get(firstDendrogramNodeIndex)) {
            for (GraphNode currentSecondDendrogramNode : currentClusters.get(secondDendrogramNodeIndex)) {
                double thisDistance = currentFirstDendrogramNode.getDistance(currentSecondDendrogramNode);
                if (thisDistance > maximumDistance) {
                    maximumDistance = thisDistance;
                }
            }
        }
        return maximumDistance;
    }
}
