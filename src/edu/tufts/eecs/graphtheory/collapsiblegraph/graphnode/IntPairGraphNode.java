package edu.tufts.eecs.graphtheory.collapsiblegraph.graphnode;

import java.io.Serializable;

/**
 * A GraphNode implementation that uses coordinate pairs for values. The distance between the two IntPairGraphNode
 * objects is defined as the pythagorean distance between the coordinate pairs 
 * @author Jeremy
 */
public class IntPairGraphNode extends SkeletonGraphNode implements Serializable{
    
    private final String nodeDescriptor = "Integer Node";
    private int xValue;
    private int yValue;
    
    /** 
     * 
     * @param xValue the first value in the coordinate pair
     * @param yValue the second value in the coordinate pair
     */
    public IntPairGraphNode(int xValue, int yValue) {
        super(String.format("(%d,%d)", xValue, yValue));
        this.xValue=xValue;
        this.yValue=yValue;
    }
    
    public int getXValue() {
        return xValue;
    }

    public int getYValue() {
        return yValue;
    }

    public String getNodeDescriptor() {
        return nodeDescriptor;
    }
    
    public void setXValue(int xValue) {
        this.xValue = xValue;
    }

    public void setYValue(int yValue) {
        this.yValue = yValue;
    }
/**
     * A function that calculates the pythagorean distance between this node and another
     * @param otherIntPairNode the other IntPairGraphNode to which this one should be compared
     * @return the distance between the IntPairGraphNodes
     */
    @Override
    public double getDistance(GraphNode otherIntPairNode) {
     if(!(otherIntPairNode instanceof IntPairGraphNode)) {
         throw new RuntimeException("It appears you're trying to compare IntPairNodes with non-IntPairNodes.");
     }

     IntPairGraphNode otherINode = (IntPairGraphNode) otherIntPairNode;
     return Math.sqrt(Math.pow((this.getXValue() - otherINode.getXValue()),2) + Math.pow((this.getYValue() - otherINode.getYValue()),2));
    }
    
}
