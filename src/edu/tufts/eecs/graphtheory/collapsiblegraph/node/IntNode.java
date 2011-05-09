/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.tufts.eecs.graphtheory.collapsiblegraph.node;

/**
 *
 * @author Jeremy
 */
public class IntNode extends SkeletonNode{
    
    private int value;
    public IntNode(int value) {
        super(value+"");
        this.value=value;
    }
    
    public int getValue() {
        return value;
    }
    
    public double getDistance(Node otherIntNode) {
     if(!(otherIntNode instanceof IntNode)) {
         throw new RuntimeException("It appears you're trying to compare IntNodes with non-IntNodes.");
     }
     return Math.abs(value - ((IntNode)otherIntNode).getValue());   
    }
    
}
