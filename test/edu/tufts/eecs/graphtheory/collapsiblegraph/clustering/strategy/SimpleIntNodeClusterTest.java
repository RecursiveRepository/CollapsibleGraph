/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.tufts.eecs.graphtheory.collapsiblegraph.clustering.strategy;

import edu.tufts.eecs.graphtheory.collapsiblegraph.clustering.DendrogramNode;
import edu.tufts.eecs.graphtheory.collapsiblegraph.node.IntNode;
import edu.tufts.eecs.graphtheory.collapsiblegraph.node.Node;
import java.util.HashSet;
import java.util.Set;
import org.junit.Test;

/**
 *
 * @author Jeremy
 */
public class SimpleIntNodeClusterTest {

    private int[] inputIntegers = {2, 4, 8, 16, 32};
    
    @Test
    public void testCluster() {
        Set<Node> inputNodes = new HashSet<Node>();
        for (int i = 0; i < inputIntegers.length; i++) {
            inputNodes.add(new IntNode(inputIntegers[i]));
        }
        ClusteringStrategy singleLinkStrategy = new NaiveSingleLinkClusteringStrategy();
        DendrogramNode root = singleLinkStrategy.cluster(inputNodes);
        Set<DendrogramNode> clusters = root.partitionByDistance(5.0);
        System.out.println("All done.");
    }
}
