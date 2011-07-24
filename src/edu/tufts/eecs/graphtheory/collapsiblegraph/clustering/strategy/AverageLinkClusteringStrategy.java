/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.tufts.eecs.graphtheory.collapsiblegraph.clustering.strategy;

import edu.tufts.eecs.graphtheory.collapsiblegraph.graphnode.GraphNode;
import java.util.List;

/**
 * A ClusteringStrategy using the Complete Link method of clustering,
 * wherein the distance between two clusters is defined as the maximum distance
 * between any node in one cluster and any node in the other.
 * @author jeremy
 */
public class AverageLinkClusteringStrategy extends AbstractClusteringStrategy {

    @Override
    protected double findDistance(int firstDendrogramNodeIndex, int secondDendrogramNodeIndex,
            List<GraphNode[]> currentClusters) {

        double sum = 0.0;
        int comparisons = 0;
        for (GraphNode currentFirstDendrogramNode : currentClusters.get(firstDendrogramNodeIndex)) {
            for (GraphNode currentSecondDendrogramNode : currentClusters.get(secondDendrogramNodeIndex)) {
                double thisDistance = currentFirstDendrogramNode.getDistance(currentSecondDendrogramNode);
                sum += thisDistance;
                comparisons++;
            }
        }
        return sum / comparisons;
    }
}