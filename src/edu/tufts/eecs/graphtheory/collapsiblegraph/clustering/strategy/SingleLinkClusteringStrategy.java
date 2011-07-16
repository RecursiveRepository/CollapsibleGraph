package edu.tufts.eecs.graphtheory.collapsiblegraph.clustering.strategy;

import edu.tufts.eecs.graphtheory.collapsiblegraph.node.Node;
import java.util.List;

/**
 * A clustering strategy using Single-Link clustering, which means that the distance
 * between two clusters is defined as the shortest distance between any node in one cluster
 * to any node in the other cluster.
 * @author jeremy
 */
public class SingleLinkClusteringStrategy extends AbstractClusteringStrategy {
    
    private final static Node[] nodeArray = new Node[0];
       @Override    
        protected double findDistance(int firstDendrogramNodeIndex, int secondDendrogramNodeIndex,
            List<Node[]> currentClusters) {

        
        double minimumDistance = Double.MAX_VALUE;
        Node[] firstDendrogramNodes = currentClusters.get(firstDendrogramNodeIndex);
        Node[] secondDendrogramNodes = currentClusters.get(secondDendrogramNodeIndex);
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
