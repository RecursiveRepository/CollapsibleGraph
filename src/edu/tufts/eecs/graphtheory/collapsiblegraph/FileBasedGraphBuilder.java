/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.tufts.eecs.graphtheory.collapsiblegraph;

import edu.tufts.eecs.graphtheory.collapsiblegraph.edge.Edge;
import edu.tufts.eecs.graphtheory.collapsiblegraph.node.Node;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Jeremy
 */
public class FileBasedGraphBuilder {

    public static CollapsibleGraph buildGraphFromFileName(String fileName) throws FileNotFoundException {
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        Set<Node> theNodes = new HashSet<Node>();
        Set<Edge> theEdges = new HashSet<Edge>();



      
    }
}
