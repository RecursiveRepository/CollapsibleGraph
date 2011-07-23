/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.tufts.eecs.graphtheory.collapsiblegraph.clustering;

import java.util.List;

/**
 *
 * @author Jeremy
 */
public interface DendrogramEdge {
    
    public DendrogramNode getSourceDendrogramNode();
    public DendrogramNode getTargetDendrogramNode();
    public void setDistance(double distance);
    public double getDistance();
    public List<DendrogramEdge> getChildEdges();
    public DendrogramEdge getParent();
    public void setParent(DendrogramEdge de);
    
    @Override
    public int hashCode();
    @Override
    public boolean equals(Object o);
    
    
}
