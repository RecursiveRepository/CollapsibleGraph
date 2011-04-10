/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.tufts.eecs.graphtheory.collapsiblegraph.edge;

import edu.tufts.eecs.graphtheory.collapsiblegraph.node.Node;

/**
 *
 * @author Jeremy
 */
public interface Edge {

public void setSource(Node sourceNode);
public void setTarget(Node targetNode);

public Node getSource();
public Node getTarget();

    @Override
public int hashCode();
}
