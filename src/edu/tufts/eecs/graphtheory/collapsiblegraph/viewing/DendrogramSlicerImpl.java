/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.tufts.eecs.graphtheory.collapsiblegraph.viewing;

import edu.tufts.eecs.graphtheory.collapsiblegraph.clustering.DendrogramEdge;
import edu.tufts.eecs.graphtheory.collapsiblegraph.clustering.DendrogramNode;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Jeremy
 */
public class DendrogramSlicerImpl implements DendrogramSlicer {
    
    public DendrogramSlice partitionByDistance(double distance, DendrogramNode rootNode, DendrogramEdge[] edges) {
        List<DendrogramNode> dendrogramNodes = new ArrayList<DendrogramNode>();
        List<DendrogramEdge> dendrogramEdges = new ArrayList<DendrogramEdge>();
        
        
        return new DendrogramSlice((DendrogramNode[])dendrogramNodes.toArray(), (DendrogramEdge[])dendrogramEdges.toArray());
    }
}
