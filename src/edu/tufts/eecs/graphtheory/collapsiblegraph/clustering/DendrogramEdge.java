/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.tufts.eecs.graphtheory.collapsiblegraph.clustering;

import java.util.Set;

/**
 *
 * @author Jeremy
 */
public interface DendrogramEdge {
    
    public DendrogramNode getSourceDendrogramNode();
    public DendrogramNode getTargetDendrogramNode();
    public void setDistance(double distance);
    public double getDistance();
    public Set<DendrogramEdge> getChildEdges();

    
}
