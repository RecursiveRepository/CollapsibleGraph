package edu.tufts.eecs.graphtheory.collapsiblegraph.node;

import org.junit.Test;



/**
 *
 * @author jeremy
 */
public class IntPairNodeTest {

    @Test
    public void testDistance() {
        Node intPairNode = new IntPairNode(-2, -3);
        Node secondIntPairNode = new IntPairNode(-4, 4);
        System.out.println(intPairNode.getDistance(secondIntPairNode));
    }
}
