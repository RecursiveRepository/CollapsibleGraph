package edu.tufts.eecs.graphtheory.collapsiblegraph.clustering;

/**
 *
 * @author Jeremy
 */
public class Dendrogram {
    private DendrogramNode rootNode;
    private DendrogramEdge rootEdge;
    
    private DendrogramEdge[] dendrogramEdges;
    
    public Dendrogram (DendrogramNode rootNode, DendrogramEdge rootEdge) {
        this.rootNode = rootNode;
        this.rootEdge = rootEdge;
    }
    
    public DendrogramNode getRootNode() {
        return rootNode;
    }
    
    public DendrogramEdge getRootEdge() {
        return rootEdge;
    }
    
    public DendrogramEdge[] getDendrogramEdges() {
        return dendrogramEdges;
    }
    
    public void setDendrogramEdges(DendrogramEdge[] dendrogramEdges) {
        this.dendrogramEdges = dendrogramEdges;
    }
    
}
