package edu.tufts.eecs.graphtheory.collapsiblegraph.clustering.strategy;

import edu.tufts.eecs.graphtheory.collapsiblegraph.clustering.Dendrogram;
import edu.tufts.eecs.graphtheory.collapsiblegraph.graphedge.GraphEdge;
import edu.tufts.eecs.graphtheory.collapsiblegraph.graphedge.SkeletonGraphEdge;
import edu.tufts.eecs.graphtheory.collapsiblegraph.graphnode.IntGraphNode;
import edu.tufts.eecs.graphtheory.collapsiblegraph.graphnode.GraphNode;
import edu.tufts.eecs.graphtheory.collapsiblegraph.persistance.DendrogramLoader;
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
 * @author Jeremy
 */
public class SimpleIntNodeClusterTest {

    private int[] inputIntegers = {2, 4, 8, 16, 32};
    private int[] inputIntegers2 = {2, 4, 8, 16, 32, 64, 128};
    private int[] inputIntegers3 = {2, 4, 8, 16};
    @Test
    public void testCluster() {
        Set<GraphNode> inputNodes = new HashSet<GraphNode>();
        List<GraphNode> inputNodeList = new ArrayList<GraphNode>();
        
        for (int i = 0; i < inputIntegers.length; i++) {
            GraphNode newNode = new IntGraphNode(inputIntegers[i]);
            inputNodes.add(newNode);
            inputNodeList.add(newNode);
        }
        
        ClusteringStrategy singleLinkStrategy = new SingleLinkClusteringStrategy();
        Set<GraphEdge> dendrogramEdges = new HashSet<GraphEdge>();
        //2->4, 2->16
        dendrogramEdges.add(new SkeletonGraphEdge(inputNodeList.get(0), inputNodeList.get(3)));
        dendrogramEdges.add(new SkeletonGraphEdge(inputNodeList.get(0), inputNodeList.get(1)));
        dendrogramEdges.add(new SkeletonGraphEdge(inputNodeList.get(0), inputNodeList.get(2)));
        Dendrogram dendrogram = singleLinkStrategy.cluster(inputNodes, dendrogramEdges);
        DendrogramSlicer ds = new DendrogramSlicer();
        DendrogramSlice results = ds.partitionByDistance(5.0, dendrogram);
        DendrogramSaver.saveDendrogram(dendrogram, "//home//jeremy//saver.txt");
        Dendrogram otherDendrogram = DendrogramLoader.loadDendrogram("//home//jeremy//saver.txt");
        
        System.out.println("All done.");
    }
    
        @Test
    public void testSecondCluster() {
        Set<GraphNode> inputNodes = new HashSet<GraphNode>();
        List<GraphNode> inputNodeList = new ArrayList<GraphNode>();
        
        for (int i = 0; i < inputIntegers.length; i++) {
            GraphNode newNode = new IntGraphNode(inputIntegers[i]);
            inputNodes.add(newNode);
            inputNodeList.add(newNode);
        }
        
        ClusteringStrategy singleLinkStrategy = new SingleLinkClusteringStrategy();
        Set<GraphEdge> dendrogramEdges = new HashSet<GraphEdge>();
        //2->4, 2->16
        dendrogramEdges.add(new SkeletonGraphEdge(inputNodeList.get(0), inputNodeList.get(3)));
                dendrogramEdges.add(new SkeletonGraphEdge(inputNodeList.get(0), inputNodeList.get(2)));
        dendrogramEdges.add(new SkeletonGraphEdge(inputNodeList.get(0), inputNodeList.get(1)));
                dendrogramEdges.add(new SkeletonGraphEdge(inputNodeList.get(0), inputNodeList.get(4)));
        Dendrogram dendrogram = singleLinkStrategy.cluster(inputNodes, dendrogramEdges);
        DendrogramSlicer ds = new DendrogramSlicer();
        DendrogramSlice results = ds.partitionByDistance(5.0, dendrogram);
        DendrogramSaver.saveDendrogram(dendrogram, "//home//jeremy//saver2.txt");
        Dendrogram otherDendrogram = DendrogramLoader.loadDendrogram("//home//jeremy//saver2.txt");
        
        System.out.println("All done.");
    }

    @Test
    public void testThirdCluster() {
        Set<GraphNode> inputNodes = new HashSet<GraphNode>();
        List<GraphNode> inputNodeList = new ArrayList<GraphNode>();

        for (int i = 0; i < inputIntegers2.length; i++) {
            GraphNode newNode = new IntGraphNode(inputIntegers2[i]);
            inputNodes.add(newNode);
            inputNodeList.add(newNode);
        }

        ClusteringStrategy singleLinkStrategy = new SingleLinkClusteringStrategy();
        Set<GraphEdge> dendrogramEdges = new HashSet<GraphEdge>();
        //2->4, 2->16
        dendrogramEdges.add(new SkeletonGraphEdge(inputNodeList.get(0), inputNodeList.get(3)));
                dendrogramEdges.add(new SkeletonGraphEdge(inputNodeList.get(0), inputNodeList.get(2)));
        dendrogramEdges.add(new SkeletonGraphEdge(inputNodeList.get(0), inputNodeList.get(1)));
                dendrogramEdges.add(new SkeletonGraphEdge(inputNodeList.get(0), inputNodeList.get(4)));
                dendrogramEdges.add(new SkeletonGraphEdge(inputNodeList.get(4), inputNodeList.get(5)));
        Dendrogram dendrogram = singleLinkStrategy.cluster(inputNodes, dendrogramEdges);
        DendrogramSlicer ds = new DendrogramSlicer();
        DendrogramSlice results = ds.partitionByDistance(5.0, dendrogram);
        DendrogramSaver.saveDendrogram(dendrogram, "//home//jeremy//saver3.txt");
        

        System.out.println("All done.");
    }
    
     @Test
    public void makeStupidCluster() {
        Set<GraphNode> inputNodes = new HashSet<GraphNode>();
        List<GraphNode> inputNodeList = new ArrayList<GraphNode>();

        for (int i = 0; i < inputIntegers3.length; i++) {
            GraphNode newNode = new IntGraphNode(inputIntegers3[i]);
            inputNodes.add(newNode);
            inputNodeList.add(newNode);
        }
 
        ClusteringStrategy singleLinkStrategy = new SingleLinkClusteringStrategy();
        Set<GraphEdge> dendrogramEdges = new HashSet<GraphEdge>();
        //2->4, 2->16
        dendrogramEdges.add(new SkeletonGraphEdge(inputNodeList.get(0), inputNodeList.get(1)));
        dendrogramEdges.add(new SkeletonGraphEdge(inputNodeList.get(0), inputNodeList.get(2)));
        dendrogramEdges.add(new SkeletonGraphEdge(inputNodeList.get(0), inputNodeList.get(3)));
                
        Dendrogram dendrogram = singleLinkStrategy.cluster(inputNodes, dendrogramEdges);
        DendrogramSlicer ds = new DendrogramSlicer();
        DendrogramSlice results = ds.partitionByDistance(5.0, dendrogram);
        DendrogramSaver.saveDendrogram(dendrogram, "//home//jeremy//saver5.txt");
        

        System.out.println("All done.");
    }
}
