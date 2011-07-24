package edu.tufts.eecs.graphtheory.collapsiblegraph.clustering;

import java.io.Serializable;

/**
 * A simple class that represents a Dendrogram over a collapsible graph. It actually contains two separate Dendrograms -- one for the GraphNodes
 * and one for the GraphEdges. It provides access to both by giving a link to the root of those Dendrograms.
 * @author Jeremy
 */
public class Dendrogram implements Serializable {
    
    private DendrogramNode rootNode;
    private DendrogramEdge rootEdge;
    
    /**
     * 
     * @param rootNode The DendrogramNode that sits at the root of the GraphNode dendrogram
     * @param rootEdge The DendrogramEdge that sits at the root of the GraphEdge dendrogram
     */
    public Dendrogram (DendrogramNode rootNode, DendrogramEdge rootEdge) {
        this.rootNode = rootNode;
        this.rootEdge = rootEdge;
    }
    /**
     * 
     * @return The DendrogramNode that sits at the root of this Dendrogram's GraphNode dendrogram
     */
    public DendrogramNode getRootNode() {
        return rootNode;
    }
    
    /**
     * 
     * @return The DendrogramEdge that sits at the root of this Dendrogram's GraphEdge dendrogram
     */
    public DendrogramEdge getRootEdge() {
        return rootEdge;
    }
    
}
