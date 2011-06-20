package edu.tufts.eecs.graphtheory.collapsiblegraph;

import edu.tufts.eecs.graphtheory.collapsiblegraph.clustering.Dendrogram;
import edu.tufts.eecs.graphtheory.collapsiblegraph.clustering.DendrogramNode;
import edu.tufts.eecs.graphtheory.collapsiblegraph.clustering.strategy.ClusteringStrategy;
import edu.tufts.eecs.graphtheory.collapsiblegraph.clustering.strategy.SingleLinkClusteringStrategy;
import edu.tufts.eecs.graphtheory.collapsiblegraph.edge.Edge;
import edu.tufts.eecs.graphtheory.collapsiblegraph.edge.SkeletonEdge;
import edu.tufts.eecs.graphtheory.collapsiblegraph.node.IntNode;
import edu.tufts.eecs.graphtheory.collapsiblegraph.node.Node;
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
        for (Node node : cg.getNodes()) {
            for (Node otherNode : cg.getNodes()) {
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
        
        List<Node> inputNodesList = new ArrayList<Node>();
        Set<Node> inputNodes = new HashSet<Node>();
        for (int i = 0; i < NUMBER_OF_NODES; i++) {
            Node newNode = new IntNode(generator.nextInt(Integer.MAX_VALUE));
            inputNodes.add(newNode);
            inputNodesList.add(newNode);
        }
        
        
        Set<Edge> inputEdges = new HashSet<Edge>();
        for(int i = 0; i < NUMBER_OF_NODES; i++) {
                inputEdges.add(new SkeletonEdge(inputNodesList.get(generator.nextInt(NUMBER_OF_NODES-1)),
                        inputNodesList.get(generator.nextInt(NUMBER_OF_NODES-1))));
        }
        
        System.out.println("Edges: " + inputEdges.size());
       
        ClusteringStrategy singleLinkStrategy = new SingleLinkClusteringStrategy();
        Dendrogram dendrogram = singleLinkStrategy.cluster(inputNodes, inputEdges);
        DendrogramNode root = dendrogram.getRootNode();
        
        long startTime = System.currentTimeMillis();
        Set<DendrogramNode> clusters = root.partitionByDistance(1.0);
        long endTime = System.currentTimeMillis(); 
        DendrogramSlicer ds = new DendrogramSlicerImpl();
        System.out.println("Cut to 1 partition in " + (endTime - startTime) + "milliseconds");
        
        for(int i = 1; i < 10000000; i= i * 10) {
        
        startTime = System.currentTimeMillis();
        ds.partitionByDistance(i, dendrogram);
        endTime = System.currentTimeMillis();
        System.out.println("Cut to " + i + " partition in " + (endTime - startTime) + "milliseconds");
        }
                
        System.out.println("All done.");
        }
}
