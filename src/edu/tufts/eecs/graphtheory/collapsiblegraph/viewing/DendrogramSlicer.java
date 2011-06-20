/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.tufts.eecs.graphtheory.collapsiblegraph.viewing;

import edu.tufts.eecs.graphtheory.collapsiblegraph.clustering.Dendrogram;

/**
 *
 * @author Jeremy
 */
public interface DendrogramSlicer {
    
        public DendrogramSlice partitionByDistance(double distance, Dendrogram dendrogram);

}
