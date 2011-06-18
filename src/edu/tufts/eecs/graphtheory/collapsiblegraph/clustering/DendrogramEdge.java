/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.tufts.eecs.graphtheory.collapsiblegraph.clustering;

/**
 *
 * @author Jeremy
 */
public interface DendrogramEdge {
    
    public DendrogramNode getSourceDendrogramNode();
    public DendrogramNode getTargetDendrogramNode();
    
}
