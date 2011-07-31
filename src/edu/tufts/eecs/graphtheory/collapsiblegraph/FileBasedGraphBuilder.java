package edu.tufts.eecs.graphtheory.collapsiblegraph;

import edu.tufts.eecs.graphtheory.collapsiblegraph.graphedge.GraphEdge;
import edu.tufts.eecs.graphtheory.collapsiblegraph.graphedge.SkeletonGraphEdge;
import edu.tufts.eecs.graphtheory.collapsiblegraph.graphnode.GraphNode;
import edu.tufts.eecs.graphtheory.collapsiblegraph.graphnode.GraphNodeFactory;
import edu.tufts.eecs.graphtheory.collapsiblegraph.graphnode.GraphNodeFactoryException;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Jeremy
 */
public class FileBasedGraphBuilder {

    public static Graph buildGraphFromFileName(String fileName, GraphNodeFactory nodeFactory) throws FileNotFoundException, IOException {
        BufferedReader br = new BufferedReader(new FileReader(fileName));

        Set<GraphEdge> edgeSet = new HashSet<GraphEdge>();
        Set<GraphNode> nodeSet = new HashSet<GraphNode>();

        Map<String, GraphNode> nodeNameMap = new HashMap<String, GraphNode>();

        String line = null;
        int linesRead = 0;
        boolean inEdges = false;
        while ((line = br.readLine()) != null) {
            if (line.equals("EDGES")) {
                inEdges = true;
                linesRead++;
                continue;
            }
            
            if (!inEdges) {
                GraphNode newNode = null;
                try {
                newNode = nodeFactory.buildGraphNodeFromString(line);
                } catch (GraphNodeFactoryException gnfe) {
                    System.err.println("Error processing node on line " + linesRead + ":" + gnfe.getMessage());
                    throw new IOException();
                }
                nodeNameMap.put(newNode.getName(), newNode);
                nodeSet.add(newNode);
            } else {
                String[] pieces = line.split("->");

                if (!nodeNameMap.containsKey(pieces[0])) {
                    System.err.println("Edge source node" + pieces[0] + " does not exist at line " + linesRead + "!");
                    br.close();
                    throw new IOException();
                }
                if (!nodeNameMap.containsKey(pieces[1])) {
                    System.err.println("Edge target node" + pieces[1] + " does not exist at line " + linesRead + "!");
                    br.close();
                    throw new IOException();
                }
                edgeSet.add(new SkeletonGraphEdge(nodeNameMap.get(pieces[0]), nodeNameMap.get(pieces[1])));
            }
            linesRead++;
        }
        br.close();
        return new Graph(nodeSet, edgeSet);
    }
}
