package edu.tufts.eecs.graphtheory.collapsiblegraph;

import edu.tufts.eecs.graphtheory.collapsiblegraph.clustering.Dendrograms;
import edu.tufts.eecs.graphtheory.collapsiblegraph.clustering.strategy.ClusteringStrategy;
import edu.tufts.eecs.graphtheory.collapsiblegraph.clustering.strategy.ClusteringStrategies;
import edu.tufts.eecs.graphtheory.collapsiblegraph.clustering.strategy.SingleLinkClusteringStrategy;
import edu.tufts.eecs.graphtheory.collapsiblegraph.graphnode.GraphNode;
import edu.tufts.eecs.graphtheory.collapsiblegraph.graphnode.GraphNodeFactory;
import edu.tufts.eecs.graphtheory.collapsiblegraph.graphnode.GraphNodes;
import edu.tufts.eecs.graphtheory.collapsiblegraph.persistance.DendrogramSaver;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.PosixParser;
import org.apache.commons.cli.HelpFormatter;

/**
 *
 * @author Jeremy
 */
public class GraphBuilder {

    private static void printHelpAndExit(Options options) {
        HelpFormatter hf = new HelpFormatter();
        hf.printHelp("java GraphBuilder", options);
        System.exit(0);
    }

    public static void main(String args[]) {
        Options options = new Options();

        Option inputfileOption = new Option("f", "input-file", true, "the file from which to read the graph");
        options.addOption(inputfileOption);

        Option clusteringStrategyOption = new Option("c", "clustering-strategy", true, "the clustering strategy to use (default: SingleLink)");
        options.addOption(clusteringStrategyOption);

        Option nodeTypeOption = new Option("n", "node-type", true, "the node type that should be built.");
        options.addOption(nodeTypeOption);

        Option outputFileOption = new Option("o", "output-file", true, "the file you'd like your Dendrogram saved to.");
        options.addOption(outputFileOption);

        Option helpOption = new Option("h", "help", false, "help!");
        options.addOption(helpOption);
        
        Option listStrategiesOption = new Option("lc", "list-clustering-strategies", false, "list the available clustering strategies");
        options.addOption(listStrategiesOption);
        
        Option listGraphNodesOption = new Option("lg", "list-graph-nodes", false, "list the available graph node types");
        options.addOption(listStrategiesOption);

        CommandLineParser parser = new PosixParser();
        CommandLine commandLine = null;
        
        try {
            commandLine = parser.parse(options, args);
        } catch (Exception e) {
            System.out.println("Sorry, your arguments were not understood.");
            printHelpAndExit(options);
        }
        
        if (commandLine.hasOption("h") ) {
            printHelpAndExit(options);
        }
        
        if(commandLine.hasOption("lc")) {
            printClusteringStrategies();
            System.exit(0);
        }
        String nodeType = commandLine.getOptionValue("n");
        String clusteringStrategyType = commandLine.getOptionValue("c");
        String inputFile = commandLine.getOptionValue("f");
        String outputFile = commandLine.getOptionValue("o");
        
        
        if(nodeType==null || outputFile == null || inputFile == null) {
            System.err.println("Sorry, you must provide a node type, an input file, and an outputfile to run this.");
            printHelpAndExit(options);
        }
        
        ClusteringStrategy clusteringStrategy = null;
        if(clusteringStrategyType ==null) {
            clusteringStrategy = new SingleLinkClusteringStrategy();
        } else {
            clusteringStrategy = ClusteringStrategies.getInstance(clusteringStrategyType);
        }
        
        if(clusteringStrategy == null) {
            System.err.println("Problem loading clusteringStrategy.");
            printClusteringStrategies();
            System.exit(1);
        }
        
        GraphNode graphNode = GraphNodes.getInstance(nodeType);
        
        if(graphNode == null ) {
            System.err.println("Problem loading GraphNode");
            printNodeTypes();
            System.exit(1);
        }
        
        GraphNodeFactory graphNodeFactory = graphNode.getGraphNodeFactory();
        Graph theirGraph = null;
        try{ 
        theirGraph = FileBasedGraphBuilder.buildGraphFromFileName(inputFile, graphNodeFactory);
        } catch (FileNotFoundException fnfe) {
            System.err.println("File " + inputFile + " was not found. Sorry.");
            System.exit(1);
        } catch (IOException io ) {
            System.err.println("Problem occurred while reading input file. Sorry.");
            System.exit(1);
        }
        
        Dendrograms theirDendrograms = clusteringStrategy.cluster(theirGraph.getNodes(), theirGraph.getEdges());
        
        try {
        DendrogramSaver.saveDendrogram(theirDendrograms, outputFile);
        } catch (Exception e) {
            System.err.println("Error trying to save your dendrogram to file:" + outputFile);
            e.printStackTrace(System.err);
        }
        
        System.out.println("Dendrogram Successfully saved to " + outputFile);
        System.exit(0);
    }
    
    public static void printClusteringStrategies() {
        System.out.println("The available clustering strategies:");
        for(String clusteringStrategy : ClusteringStrategies.getClusteringStrategies()) {
            System.out.println(clusteringStrategy);
        }
    }
    
        public static void printNodeTypes() {
        System.out.println("The available graphNodeType:");
        for(String graphNode : GraphNodes.getGraphNodes()) {
            System.out.println(graphNode);
        }
    }
}
