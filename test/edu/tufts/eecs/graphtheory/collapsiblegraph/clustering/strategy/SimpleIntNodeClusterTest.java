    /*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.tufts.eecs.graphtheory.collapsiblegraph.clustering.strategy;

import edu.tufts.eecs.graphtheory.collapsiblegraph.clustering.Dendrogram;
import edu.tufts.eecs.graphtheory.collapsiblegraph.edge.Edge;
import edu.tufts.eecs.graphtheory.collapsiblegraph.edge.SkeletonEdge;
import edu.tufts.eecs.graphtheory.collapsiblegraph.node.IntNode;
import edu.tufts.eecs.graphtheory.collapsiblegraph.node.Node;
import edu.tufts.eecs.graphtheory.collapsiblegraph.persistance.DendrogramLoader;
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
 * @author Jeremy
 */
public class SimpleIntNodeClusterTest {

    private int[] inputIntegers = {2, 4, 8, 16, 32};
    
    @Test
    public void testCluster() {
        Set<Node> inputNodes = new HashSet<Node>();
        List<Node> inputNodeList = new ArrayList<Node>();
        
        for (int i = 0; i < inputIntegers.length; i++) {
            Node newNode = new IntNode(inputIntegers[i]);
            inputNodes.add(newNode);
            inputNodeList.add(newNode);
        }
        
        ClusteringStrategy singleLinkStrategy = new SingleLinkClusteringStrategy();
        Set<Edge> dendrogramEdges = new HashSet<Edge>();
        //2->4, 2->16
        dendrogramEdges.add(new SkeletonEdge(inputNodeList.get(0), inputNodeList.get(3)));
        dendrogramEdges.add(new SkeletonEdge(inputNodeList.get(0), inputNodeList.get(1)));
        Dendrogram dendrogram = singleLinkStrategy.cluster(inputNodes, dendrogramEdges);
        DendrogramSlicer ds = new DendrogramSlicerImpl();
        DendrogramSlice results = ds.partitionByDistance(5.0, dendrogram);
        DendrogramSaver.saveDendrogram(dendrogram, "c:\\\\users\\jeremy\\saver.txt");
        Dendrogram otherDendrogram = DendrogramLoader.loadDendrogram("c:\\\\users\\jeremy\\saver.txt");
        
        System.out.println("All done.");
    }
}
