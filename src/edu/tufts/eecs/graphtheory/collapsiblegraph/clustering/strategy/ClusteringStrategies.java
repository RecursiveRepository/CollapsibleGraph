package edu.tufts.eecs.graphtheory.collapsiblegraph.clustering.strategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Jeremy
 */
public class ClusteringStrategies {
    private static final Map<String, Class> clusteringStrategyMap = new HashMap<String, Class>();
    
    static {
        clusteringStrategyMap.put("AverageLink", AverageLinkClusteringStrategy.class);
        clusteringStrategyMap.put("SingleLink", SingleLinkClusteringStrategy.class);
        clusteringStrategyMap.put("CompleteLink", CompleteLinkClusteringStrategy.class);
    }
    
    public static List<String> getClusteringStrategies() {
        List<String> strategyList = new ArrayList<String>();
        for(String strategy : clusteringStrategyMap.keySet()) {
            strategyList.add(strategy);
        }
        return strategyList;
    }
    
    public static ClusteringStrategy getInstance(String strategyName) {
        Class strategyClass = clusteringStrategyMap.get(strategyName);
        if(strategyClass==null) {
            System.err.println("Unable to find strategy: " + strategyName);
            return null;
        }
        try {
        return (ClusteringStrategy)clusteringStrategyMap.get(strategyName).newInstance();
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
        return null;
    }
}
