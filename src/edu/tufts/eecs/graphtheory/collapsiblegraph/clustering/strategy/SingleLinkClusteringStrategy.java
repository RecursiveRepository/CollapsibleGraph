/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.tufts.eecs.graphtheory.collapsiblegraph.clustering.strategy;

import edu.tufts.eecs.graphtheory.collapsiblegraph.node.Node;
import java.util.List;
import java.util.Set;

/**
 * A clustering strategy using Single-Link clustering, which means that the distance
 * between two clusters is defined as the shortest distance between any node in one cluster
 * to any node in the other cluster.
 * @author jeremy
 */
public class SingleLinkClusteringStrategy extends AbstractClusteringStrategy {
    
       @Override    
        protected double findDistance(int firstDendrogramNodeIndex, int secondDendrogramNodeIndex,
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
}
