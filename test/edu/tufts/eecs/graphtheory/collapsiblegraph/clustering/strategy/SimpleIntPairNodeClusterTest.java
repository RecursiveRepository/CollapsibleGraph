package edu.tufts.eecs.graphtheory.collapsiblegraph.clustering.strategy;

import edu.tufts.eecs.graphtheory.collapsiblegraph.clustering.Dendrograms;
import edu.tufts.eecs.graphtheory.collapsiblegraph.graphedge.GraphEdge;
import edu.tufts.eecs.graphtheory.collapsiblegraph.graphedge.SkeletonGraphEdge;
import edu.tufts.eecs.graphtheory.collapsiblegraph.graphnode.IntPairGraphNode;
import edu.tufts.eecs.graphtheory.collapsiblegraph.graphnode.GraphNode;
import edu.tufts.eecs.graphtheory.collapsiblegraph.persistance.DendrogramSaver;
import edu.tufts.eecs.graphtheory.collapsiblegraph.viewing.DendrogramSlice;
import edu.tufts.eecs.graphtheory.collapsiblegraph.viewing.DendrogramSlicer;
import edu.tufts.eecs.graphtheory.collapsiblegraph.viewing.DendrogramSlicer;
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

    int inputList[][] = {{1,1}, {2,1}, {4,4}, {10,1}, {3,3}};

     @Test
    public void testThirdCluster() {

        Set<GraphNode> inputNodes = new HashSet<GraphNode>();
        List<GraphNode> inputNodeList = new ArrayList<GraphNode>();

        for (int i = 0; i < inputList.length; i++) {
            GraphNode newNode = new IntPairGraphNode(inputList[i][0], inputList[i][1]);
            inputNodes.add(newNode);
            inputNodeList.add(newNode);
        }

        ClusteringStrategy singleLinkStrategy = new SingleLinkClusteringStrategy();
        Set<GraphEdge> dendrogramEdges = new HashSet<GraphEdge>();
        //2->4, 2->16
        dendrogramEdges.add(new SkeletonGraphEdge(inputNodeList.get(0), inputNodeList.get(3)));
        dendrogramEdges.add(new SkeletonGraphEdge(inputNodeList.get(0), inputNodeList.get(2)));
        dendrogramEdges.add(new SkeletonGraphEdge(inputNodeList.get(0), inputNodeList.get(1)));
        //        dendrogramEdges.add(new SkeletonGraphEdge(inputNodeList.get(0), inputNodeList.get(4)));
          //      dendrogramEdges.add(new SkeletonGraphEdge(inputNodeList.get(4), inputNodeList.get(5)));
        Dendrograms dendrogram = singleLinkStrategy.cluster(inputNodes, dendrogramEdges);
        DendrogramSlicer ds = new DendrogramSlicer();
        DendrogramSlice results = ds.partitionByDistance(5.0, dendrogram);
        DendrogramSaver.saveDendrogram(dendrogram, "//home//jeremy//saver4.txt");


        System.out.println("All done.");
    }
}
