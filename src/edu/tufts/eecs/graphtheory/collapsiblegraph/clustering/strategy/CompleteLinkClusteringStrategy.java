/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.tufts.eecs.graphtheory.collapsiblegraph.clustering.strategy;

import edu.tufts.eecs.graphtheory.collapsiblegraph.node.Node;
import java.util.List;
import java.util.Set;

/**
 * A ClusteringStrategy using the Complete Link method of clustering,
 * wherein the distance between two clusters is defined as the maximum distance
 * between any node in one cluster and any node in the other.
 * @author jeremy
 */
public class CompleteLinkClusteringStrategy extends AbstractClusteringStrategy {
    
       @Override    
        protected double findDistance(int firstDendrogramNodeIndex, int secondDendrogramNodeIndex,
            List<Node[]> currentClusters) {

        double maximumDistance = Double.MIN_VALUE;
        for (Node currentFirstDendrogramNode : currentClusters.get(firstDendrogramNodeIndex)) {
            for (Node currentSecondDendrogramNode : currentClusters.get(secondDendrogramNodeIndex)) {
                double thisDistance = currentFirstDendrogramNode.getDistance(currentSecondDendrogramNode);
                if (thisDistance > maximumDistance) {
                    maximumDistance = thisDistance;
                }
            }
        }
        return maximumDistance;
    }
}
