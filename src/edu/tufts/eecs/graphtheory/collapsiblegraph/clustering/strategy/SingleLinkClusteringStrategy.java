package edu.tufts.eecs.graphtheory.collapsiblegraph.clustering.strategy;

import edu.tufts.eecs.graphtheory.collapsiblegraph.graphnode.GraphNode;
import java.util.List;

/**
 * A clustering strategy using Single-Link clustering, which means that the distance
 * between two clusters is defined as the shortest distance between any node in one cluster
 * to any node in the other cluster.
 * @author jeremy
 */
public class SingleLinkClusteringStrategy extends AbstractClusteringStrategy {

    @Override
    protected double findDistance(int firstDendrogramNodeIndex, int secondDendrogramNodeIndex,
            List<GraphNode[]> currentClusters) {
        double minimumDistance = Double.MAX_VALUE;
        GraphNode[] firstDendrogramNodes = currentClusters.get(firstDendrogramNodeIndex);
        GraphNode[] secondDendrogramNodes = currentClusters.get(secondDendrogramNodeIndex);
        for (int i = 0; i < firstDendrogramNodes.length; i++) {

            for (int j = 0; j < secondDendrogramNodes.length; j++) {
                double thisDistance = firstDendrogramNodes[i].getDistance(secondDendrogramNodes[j]);
                if (thisDistance < minimumDistance) {
                    minimumDistance = thisDistance;
                }
            }
        }
        return minimumDistance;
    }
}
