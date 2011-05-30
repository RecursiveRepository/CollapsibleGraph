/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.tufts.eecs.graphtheory.collapsiblegraph;

import edu.tufts.eecs.graphtheory.collapsiblegraph.edge.Edge;
import edu.tufts.eecs.graphtheory.collapsiblegraph.edge.SkeletonEdge;
import edu.tufts.eecs.graphtheory.collapsiblegraph.node.Node;
import edu.tufts.eecs.graphtheory.collapsiblegraph.node.SkeletonNode;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Jeremy
 */
public class FileBasedGraphBuilder {

    public static CollapsibleGraph buildGraphFromFileName(String fileName) throws Exception {
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        
        Set<Edge> edgeSet = new HashSet<Edge>();
        Set<Node> nodeSet = new HashSet<Node>();

        Map<String, Node> nodeNameMap = new HashMap<String, Node>();

        String line = null;
        int linesRead = 0;
        while((line = br.readLine())!=null) {
            String[] pieces = line.split(",");
            if(pieces.length==1) {
                if(nodeNameMap.containsKey(pieces[0])) {
                    System.err.println("Duplicate node name " + pieces[0] + " at line " + linesRead + "." );
                    throw new Exception();
                }
                Node newNode = new SkeletonNode(pieces[0]);
                nodeNameMap.put(pieces[0], newNode);
                nodeSet.add(newNode);
            } else if(pieces.length==2) {
                 if(!nodeNameMap.containsKey(pieces[0])) {
                     System.err.println("Edge source " + pieces[0] + " does not exist at line " + linesRead + "!");
                     throw new Exception();
                 }
                 if(!nodeNameMap.containsKey(pieces[1])) {
                     System.err.println("Edge target " + pieces[1] + " does not exist at line " + linesRead + "!");
                     throw new Exception();
                 }

                 edgeSet.add(new SkeletonEdge(nodeNameMap.get(pieces[0]), nodeNameMap.get(pieces[1])));

            }
            linesRead++;
        }
        return new SimpleCollapsibleGraph(nodeSet, edgeSet, nodeNameMap);
    }
}
