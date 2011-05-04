package edu.tufts.eecs.graphtheory.collapsiblegraph.clustering;

import edu.tufts.eecs.graphtheory.collapsiblegraph.node.Node;
import java.util.Set;

/**
 *
 * @author Jeremy
 */
public interface DendrogramNode {

public Set<DendrogramNode> partitionByDistance(double partitionDistance);
public Set<Node> getNodes();


}
