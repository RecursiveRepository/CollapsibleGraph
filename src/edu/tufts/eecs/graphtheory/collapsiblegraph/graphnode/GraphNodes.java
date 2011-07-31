package edu.tufts.eecs.graphtheory.collapsiblegraph.graphnode;

import edu.tufts.eecs.graphtheory.collapsiblegraph.clustering.strategy.AverageLinkClusteringStrategy;
import edu.tufts.eecs.graphtheory.collapsiblegraph.clustering.strategy.ClusteringStrategy;
import edu.tufts.eecs.graphtheory.collapsiblegraph.clustering.strategy.CompleteLinkClusteringStrategy;
import edu.tufts.eecs.graphtheory.collapsiblegraph.clustering.strategy.SingleLinkClusteringStrategy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Jeremy
 */
public class GraphNodes {
      private static final Map<String, Class> graphNodeMap = new HashMap<String, Class>();
    
    static {
        graphNodeMap.put("IntegerNode", IntGraphNode.class);
        graphNodeMap.put("IntegerPairNode", IntPairGraphNode.class);
        }
    
    public static List<String> getGraphNodes() {
        List<String> graphNodeList = new ArrayList<String>();
        for(String graphNode : graphNodeMap.keySet()) {
            graphNodeList.add(graphNode);
        }
        return graphNodeList;
    }
    
    public static GraphNode getInstance(String graphNodeName) {
        Class graphNodeClass = graphNodeMap.get(graphNodeName);
        if(graphNodeClass==null) {
            System.err.println("Unable to find strategy: " + graphNodeName);
            return null;
        }
        try {
        return (GraphNode)graphNodeMap.get(graphNodeName).newInstance();
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
        return null;
    }
}
