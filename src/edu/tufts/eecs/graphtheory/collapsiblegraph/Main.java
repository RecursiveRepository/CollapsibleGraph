package edu.tufts.eecs.graphtheory.collapsiblegraph;

import edu.tufts.eecs.graphtheory.collapsiblegraph.clustering.Dendrogram;
import edu.tufts.eecs.graphtheory.collapsiblegraph.clustering.DendrogramNode;
import edu.tufts.eecs.graphtheory.collapsiblegraph.clustering.strategy.ClusteringStrategy;
import edu.tufts.eecs.graphtheory.collapsiblegraph.clustering.strategy.SingleLinkClusteringStrategy;
import edu.tufts.eecs.graphtheory.collapsiblegraph.graphedge.GraphEdge;
import edu.tufts.eecs.graphtheory.collapsiblegraph.graphedge.SkeletonGraphEdge;
import edu.tufts.eecs.graphtheory.collapsiblegraph.graphnode.IntGraphNode;
import edu.tufts.eecs.graphtheory.collapsiblegraph.graphnode.GraphNode;
import edu.tufts.eecs.graphtheory.collapsiblegraph.viewing.DendrogramSlicer;
import edu.tufts.eecs.graphtheory.collapsiblegraph.viewing.DendrogramSlicerImpl;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 *
 * @author Jeremy
 */
public class Main {

  /*  public static void main(String[] argv) {
        CollapsibleGraph cg = null;
        try {
            cg = FileBasedGraphBuilder.buildGraphFromFileName("C:\\Users\\Jeremy\\Downloads\\12dicts-5.0\\3esl.txt");
        } catch (Exception e) {
            e.printStackTrace(System.err);
            System.exit(1);
        }
        System.out.println("Successfully initialized graph with " + cg.getNodes().size() + " nodes.");
        double maxDistance = 0;
        long comparisonsDone = 0;
        for (GraphNode node : cg.getNodes()) {
            for (GraphNode otherNode : cg.getNodes()) {
                if (!node.equals(otherNode)) {
                    double distance = node.getDistance(otherNode);
                    comparisonsDone++;
                    if(distance > maxDistance) {
                        maxDistance = distance;
                    }
                    if(comparisonsDone%100000==0) {
                        System.out.println("Done " + comparisonsDone);
                    }
                }
            }

        }
        System.out.println(maxDistance);
    }
     * 
     */
    
    private static final Random generator = new Random();
    private static final int NUMBER_OF_NODES = 50000;
    
    
    public static void main(String[] argv) {
        
        List<GraphNode> inputNodesList = new ArrayList<GraphNode>();
        Set<GraphNode> inputNodes = new HashSet<GraphNode>();
        for (int i = 0; i < NUMBER_OF_NODES; i++) {
            GraphNode newNode = new IntGraphNode(generator.nextInt(Integer.MAX_VALUE));
            inputNodes.add(newNode);
            inputNodesList.add(newNode);
        }
        
        
        Set<GraphEdge> inputEdges = new HashSet<GraphEdge>();
        for(int i = 0; i < NUMBER_OF_NODES; i++) {
                inputEdges.add(new SkeletonGraphEdge(inputNodesList.get(generator.nextInt(NUMBER_OF_NODES-1)),
                        inputNodesList.get(generator.nextInt(NUMBER_OF_NODES-1))));
        }
        
        System.out.println("Edges: " + inputEdges.size());
       
        ClusteringStrategy singleLinkStrategy = new SingleLinkClusteringStrategy();
        Dendrogram dendrogram = singleLinkStrategy.cluster(inputNodes, inputEdges);
        DendrogramNode root = dendrogram.getRootNode();
       
        DendrogramSlicer ds = new DendrogramSlicerImpl();
       
        
        for(int i = 1; i < 10000000; i= i * 10) {
        
        long startTime = System.currentTimeMillis();
        ds.partitionByDistance(i, dendrogram);
        long endTime = System.currentTimeMillis();
        System.out.println("Cut to " + i + " partition in " + (endTime - startTime) + "milliseconds");
        }
                
        System.out.println("All done.");
        }
}
