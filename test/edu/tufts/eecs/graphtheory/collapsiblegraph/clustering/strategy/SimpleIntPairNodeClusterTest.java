/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.tufts.eecs.graphtheory.collapsiblegraph.clustering.strategy;

import edu.tufts.eecs.graphtheory.collapsiblegraph.clustering.Dendrogram;
import edu.tufts.eecs.graphtheory.collapsiblegraph.edge.Edge;
import edu.tufts.eecs.graphtheory.collapsiblegraph.edge.SkeletonEdge;
import edu.tufts.eecs.graphtheory.collapsiblegraph.node.IntPairNode;
import edu.tufts.eecs.graphtheory.collapsiblegraph.node.Node;
import edu.tufts.eecs.graphtheory.collapsiblegraph.persistance.DendrogramSaver;
import edu.tufts.eecs.graphtheory.collapsiblegraph.viewing.DendrogramSlice;
import edu.tufts.eecs.graphtheory.collapsiblegraph.viewing.DendrogramSlicer;
import edu.tufts.eecs.graphtheory.collapsiblegraph.viewing.DendrogramSlicerImpl;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.Test;

/**
 *
 * @author jeremy
 */
public class SimpleIntPairNodeClusterTest {

    int inputList[][] = {{1,1}, {2,1}, {4,4}};

     @Test
    public void testThirdCluster() {

        Set<Node> inputNodes = new HashSet<Node>();
        List<Node> inputNodeList = new ArrayList<Node>();

        for (int i = 0; i < inputList.length; i++) {
            Node newNode = new IntPairNode(inputList[i][0], inputList[i][1]);
            inputNodes.add(newNode);
            inputNodeList.add(newNode);
        }

        ClusteringStrategy singleLinkStrategy = new SingleLinkClusteringStrategy();
        Set<Edge> dendrogramEdges = new HashSet<Edge>();
        //2->4, 2->16
        //dendrogramEdges.add(new SkeletonEdge(inputNodeList.get(0), inputNodeList.get(3)));
                dendrogramEdges.add(new SkeletonEdge(inputNodeList.get(0), inputNodeList.get(2)));
        dendrogramEdges.add(new SkeletonEdge(inputNodeList.get(0), inputNodeList.get(1)));
        //        dendrogramEdges.add(new SkeletonEdge(inputNodeList.get(0), inputNodeList.get(4)));
          //      dendrogramEdges.add(new SkeletonEdge(inputNodeList.get(4), inputNodeList.get(5)));
        Dendrogram dendrogram = singleLinkStrategy.cluster(inputNodes, dendrogramEdges);
        DendrogramSlicer ds = new DendrogramSlicerImpl();
        DendrogramSlice results = ds.partitionByDistance(5.0, dendrogram);
        DendrogramSaver.saveDendrogram(dendrogram, "//home//jeremy//saver4.txt");


        System.out.println("All done.");
    }
}
