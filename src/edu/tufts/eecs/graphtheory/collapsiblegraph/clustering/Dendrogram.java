package edu.tufts.eecs.graphtheory.collapsiblegraph.clustering;

import java.io.Serializable;

/*.eecs
 *
 * @author Jeremy
 */
public class Dendrogram implements Serializable {
    
    private DendrogramNode rootNode;
    private DendrogramEdge rootEdge;
    
   
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
    
}
