package edu.tufts.eecs.graphtheory.collapsiblegraph.node;

import edu.tufts.eecs.graphtheory.collapsiblegraph.graphnode.GraphNode;
import edu.tufts.eecs.graphtheory.collapsiblegraph.graphnode.IntPairGraphNode;
import org.junit.Test;



/**
 *
 * @author jeremy
 */
public class IntPairNodeTest {

    @Test
    public void testDistance() {
        GraphNode intPairNode = new IntPairGraphNode(-2, -3);
        GraphNode secondIntPairNode = new IntPairGraphNode(-4, 4);
        System.out.println(intPairNode.getDistance(secondIntPairNode));
    }
}
