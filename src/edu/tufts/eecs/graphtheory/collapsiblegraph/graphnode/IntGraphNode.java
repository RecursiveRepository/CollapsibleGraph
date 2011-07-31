package edu.tufts.eecs.graphtheory.collapsiblegraph.graphnode;

import java.io.Serializable;

/**
 * A GraphNode containing only a single Integer as its data. The distance function is the absolute value of
 * the difference between the Integers contained by the 2 nodes
 * @author Jeremy
 */
public class IntGraphNode extends SkeletonGraphNode implements Serializable{
    
    private final String nodeDescriptor = "Integer Node";
    
    private int value;
    
    public String getNodeDescriptor() {
        return nodeDescriptor;
    }
    
    /** 
     * 
     * @param value the integer that this node should contain
     */
    public IntGraphNode(int value) {
        super(Integer.toString(value));
        this.value=value;
    }
    
    public int getValue() {
        return value;
    }
    
    public void setValue(int value) {
        this.value = value;
    }
    
    /**
     * 
     * @param otherIntNode the other IntGraphNode this IntGraphNode should be compared to
     * @return the distance between the two IntGraphNodes
     */
    @Override
    public double getDistance(GraphNode otherIntNode) {
     if(!(otherIntNode instanceof IntGraphNode)) {
         throw new RuntimeException("It appears you're trying to compare IntNodes with non-IntNodes.");
     }
     return Math.abs(value - ((IntGraphNode)otherIntNode).getValue());   
    }
    
    public GraphNodeFactory getGraphNodeFactory() {
        return new IntGraphNodeFactory();
    }

    public class IntGraphNodeFactory implements GraphNodeFactory {

        public GraphNode buildGraphNodeFromString(String s) throws GraphNodeFactoryException {
            int value;
            try {
                value = Integer.parseInt(s);
            } catch (Exception e) {
                throw new GraphNodeFactoryException("Problem parsing first integer: " + s);
            }

            return new IntGraphNode(value);
        }
    }
}
