/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.tufts.eecs.graphtheory.collapsiblegraph.clustering.distance;

import edu.tufts.eecs.graphtheory.collapsiblegraph.clustering.DendrogramNode;

/**
 *
 * @author Jeremy
 */
public interface ClusterDistanceFunction {

    public double findDistance(DendrogramNode firstCluster, DendrogramNode secondCluster);

}

