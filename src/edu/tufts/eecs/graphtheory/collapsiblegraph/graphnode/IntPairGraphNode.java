/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.tufts.eecs.graphtheory.collapsiblegraph.graphnode;

import java.io.Serializable;

/**
 *
 * @author Jeremy
 */
public class IntPairGraphNode extends SkeletonGraphNode implements Serializable{
    
        
    private final String nodeDescriptor = "Integer Node";
    
    
    private int xValue;
    private int yValue;
    
    public IntPairGraphNode(int xValue, int yValue) {
        super("(" + xValue + "," + yValue + ")");
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

    public double getDistance(GraphNode otherIntPairNode) {
     if(!(otherIntPairNode instanceof IntPairGraphNode)) {
         throw new RuntimeException("It appears you're trying to compare IntPairNodes with non-IntPairNodes.");
     }

     IntPairGraphNode otherINode = (IntPairGraphNode) otherIntPairNode;
     return Math.sqrt(Math.pow((this.getXValue() - otherINode.getXValue()),2) + Math.pow((this.getYValue() - otherINode.getYValue()),2));
    }
    
}
