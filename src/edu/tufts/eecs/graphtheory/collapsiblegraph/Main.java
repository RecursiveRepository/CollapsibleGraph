/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.tufts.eecs.graphtheory.collapsiblegraph;

import edu.tufts.eecs.graphtheory.collapsiblegraph.node.Node;

/**
 *
 * @author Jeremy
 */
public class Main {

    public static void main(String[] argv) {
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
}
