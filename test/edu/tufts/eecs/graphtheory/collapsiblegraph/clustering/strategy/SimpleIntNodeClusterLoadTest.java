package edu.tufts.eecs.graphtheory.collapsiblegraph.clustering.strategy;

import edu.tufts.eecs.graphtheory.collapsiblegraph.clustering.DendrogramNode;
import edu.tufts.eecs.graphtheory.collapsiblegraph.graphnode.IntGraphNode;
import edu.tufts.eecs.graphtheory.collapsiblegraph.graphnode.GraphNode;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import org.junit.Test;

/**
 *
 * @author Jeremy
 */
public class SimpleIntNodeClusterLoadTest {


    
    private static final Random generator = new Random();
    private static final int NUMBER_OF_NODES = 1000;
    
    @Test
    public void testCluster() {
        Set<GraphNode> inputNodes = new HashSet<GraphNode>();
        for (int i = 0; i < NUMBER_OF_NODES; i++) {
            inputNodes.add(new IntGraphNode(generator.nextInt(Integer.MAX_VALUE)));
        }
   //     ClusteringStrategy singleLinkStrategy = new SingleLinkClusteringStrategy();
     //   DendrogramNode root = singleLinkStrategy.cluster(inputNodes);
     //   Set<DendrogramNode> clusters = root.partitionByDistance(5.0);
        System.out.println("All done.");
    }
}
