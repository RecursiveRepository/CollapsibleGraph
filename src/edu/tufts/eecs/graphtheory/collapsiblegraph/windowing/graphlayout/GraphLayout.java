package edu.tufts.eecs.graphtheory.collapsiblegraph.windowing.graphlayout;

import java.util.List;

/**
 *
 * @author Jeremy
 */
public class GraphLayout {

    private List<ViewableDendrogramNode> graphNodes;
    private List<ViewableDendrogramEdge> graphEdges;

    public GraphLayout(List<ViewableDendrogramNode> graphNodes, List<ViewableDendrogramEdge> graphEdges) {
        this.graphNodes = graphNodes;
        this.graphEdges = graphEdges;
    }
    
    public List<ViewableDendrogramNode> getGraphNodes() {
        return graphNodes;
    }
    
    public List<ViewableDendrogramEdge> getGraphEdges() {
        return graphEdges;
    }
}
