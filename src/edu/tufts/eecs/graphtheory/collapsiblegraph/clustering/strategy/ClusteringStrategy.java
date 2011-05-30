/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.tufts.eecs.graphtheory.collapsiblegraph.clustering.strategy;

import edu.tufts.eecs.graphtheory.collapsiblegraph.clustering.DendrogramNode;
import edu.tufts.eecs.graphtheory.collapsiblegraph.node.Node;
import java.util.Set;

/**
 *
 * @author Jeremy
 */
public interface ClusteringStrategy {

public DendrogramNode cluster(Set<Node> theNodes);

}
