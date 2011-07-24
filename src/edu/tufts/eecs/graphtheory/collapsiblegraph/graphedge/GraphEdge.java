/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.tufts.eecs.graphtheory.collapsiblegraph.graphedge;

import edu.tufts.eecs.graphtheory.collapsiblegraph.graphnode.GraphNode;

/**
 *
 * @author Jeremy
 */
public interface GraphEdge {

public void setSource(GraphNode sourceNode);
public void setTarget(GraphNode targetNode);

public GraphNode getSource();
public GraphNode getTarget();

    @Override
public int hashCode();
}
