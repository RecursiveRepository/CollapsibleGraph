package edu.tufts.eecs.graphtheory.collapsiblegraph.windowing.screens;

import controlP5.Button;
import controlP5.ControlP5;
import controlP5.Slider;
import edu.tufts.eecs.graphtheory.collapsiblegraph.clustering.ClusterDendrogramNode;
import edu.tufts.eecs.graphtheory.collapsiblegraph.clustering.Dendrograms;
import edu.tufts.eecs.graphtheory.collapsiblegraph.graphnode.GraphNode;
import edu.tufts.eecs.graphtheory.collapsiblegraph.viewing.DendrogramSlice;
import edu.tufts.eecs.graphtheory.collapsiblegraph.viewing.DendrogramSlicer;
import edu.tufts.eecs.graphtheory.collapsiblegraph.windowing.ApplicationState;
import edu.tufts.eecs.graphtheory.collapsiblegraph.windowing.Fonts;
import edu.tufts.eecs.graphtheory.collapsiblegraph.windowing.graphlayout.ForceDirectedLayoutGenerator;
import edu.tufts.eecs.graphtheory.collapsiblegraph.windowing.graphlayout.ViewableDendrogramEdge;
import edu.tufts.eecs.graphtheory.collapsiblegraph.windowing.graphlayout.ViewableDendrogramNode;
import java.util.ArrayList;
import java.util.List;
import processing.core.PApplet;

/**
 *
 * @author Jeremy
 */
public class GraphRendererScreen {

    private static GraphRendererScreen instance;
    private PApplet papplet;
    private ControlP5 guiController;
    private Slider slider;
    private Button zoomButton;
    private Button zoomOutButton;
    private Button zoomInButton;
    private Button mainMenuButton;
    private Dendrograms dendrogram;
    private DendrogramSlicer dendrogramSlicer;
    double partitionDistance = 0.0F;
    private List<ViewableDendrogramEdge> viewableDEdges;
    private List<ViewableDendrogramNode> viewableDNodes;
    private ViewableDendrogramNode selectedNode;
    private List<GraphNode> selectedDataNodes = new ArrayList<GraphNode>();
    private ForceDirectedLayoutGenerator layoutGenerator = new ForceDirectedLayoutGenerator();

    private GraphRendererScreen(PApplet papplet, ControlP5 guiController) {
        this.papplet = papplet;
        this.guiController = guiController;
        viewableDNodes = new ArrayList<ViewableDendrogramNode>();
        dendrogramSlicer = new DendrogramSlicer();
    }

    private void setup() {
        slider = guiController.addSlider("zoomSlider", 0f, (float) ((ClusterDendrogramNode) dendrogram.getRootNode()).getDistance(), 30, 525, 200, 30);
        slider.alignValueLabel(ControlP5.TOP);
        slider.showTickMarks(true);
        slider.setCaptionLabel("");
        slider.hide();

        zoomButton = guiController.addButton("zoomButton", 1, 250, 525, 80, 30);
        zoomButton.setLabel("Zoom!");
        zoomButton.hide();


        zoomOutButton = guiController.addButton("zoomOutButton", 2, 350, 525, 80, 30);
        zoomOutButton.setLabel("Zoom Out");
        zoomOutButton.hide();

        zoomInButton = guiController.addButton("zoomInButton", 2, 450, 525, 80, 30);
        zoomInButton.setLabel("Zoom In");
        zoomInButton.hide();

        mainMenuButton = guiController.addButton("mainMenuButton", 2, 30, 565, 120, 30);
        mainMenuButton.setLabel("Main Menu");
        mainMenuButton.hide();

        guiController.draw();
    }

    public void reset() {
        slider.hide();
        zoomButton.hide();
        zoomOutButton.hide();
        zoomInButton.hide();
        mainMenuButton.hide();
    }

    public static GraphRendererScreen getGraphRendererScreen(PApplet papplet, ControlP5 guiController) {
        instance = new GraphRendererScreen(papplet, guiController);
        return instance;
    }

    public static GraphRendererScreen getGraphRendererScreen() {
        return instance;
    }

    public void setDendrogram(Dendrograms dendrogram) {
        this.dendrogram = dendrogram;
        setup();
    }

    public ApplicationState draw() {
        papplet.background(0);
        slider.show();
        zoomButton.show();
        zoomOutButton.show();
        zoomInButton.show();
        mainMenuButton.show();
        guiController.draw();
        viewableDNodes = layoutGenerator.getVDNodes();
        viewableDEdges = layoutGenerator.getGraphEdges();

        for (ViewableDendrogramNode viewableNode : viewableDNodes) {

            if (papplet.mousePressed) {
                if (Math.pow((papplet.mouseX - viewableNode.getXCoordinate()), 2) + Math.pow((papplet.mouseY - viewableNode.getYCoordinate()), 2) < Math.pow(25, 2)) {
                    selectedNode = viewableNode;
                    selectedDataNodes = viewableNode.getDendrogramNode().getGraphNodes();
                }
            }

            if (viewableNode == selectedNode) {
                papplet.fill(0, 0, 255);
            } else {
                papplet.fill(255, 255, 255);
            }
            papplet.ellipseMode(papplet.CENTER);
            papplet.ellipse(viewableNode.getXCoordinate(), viewableNode.getYCoordinate(), viewableNode.getDiameter(), viewableNode.getDiameter());
        }

        papplet.stroke(0, 0, 255);
        for (ViewableDendrogramEdge viewableEdge : viewableDEdges) {
            papplet.line(viewableEdge.getSourceNode().getXCoordinate(), viewableEdge.getSourceNode().getYCoordinate(),
                    viewableEdge.getTargetNode().getXCoordinate(), viewableEdge.getTargetNode().getYCoordinate());

        }

        if (!selectedDataNodes.isEmpty()) {
            StringBuilder nodeString = new StringBuilder();
            for (GraphNode selectedDataNode : selectedDataNodes) {
                nodeString.append(selectedDataNode.toString());
                nodeString.append(",");
            }

            papplet.fill(0, 0, 255);

            papplet.textFont(Fonts.getMediumFont());
            papplet.textAlign(papplet.LEFT);
            papplet.text(nodeString.toString(), 550, 520);
        }
        
        papplet.stroke(0, 0, 255);
        papplet.line(0, 500, 1024, 500);
        layoutGenerator.iterate();
        return ApplicationState.GRAPH_RENDERER_SCREEN;
    }

    public void setZoomLevel(double zoomLevel) {
        partitionDistance = zoomLevel;
    }

    public void zoomIn() {
        System.out.println("Zoomin called");
        double newDistance = layoutGenerator.zoomIn();
        selectedDataNodes.clear();
        slider.setValue((float) newDistance);
    }

    public void zoomOut() {
        System.out.println("Zoomout called");
        double newDistance = layoutGenerator.zoomOut();
        selectedDataNodes.clear();
        slider.setValue((float) newDistance);
    }

    public void redraw() {
        DendrogramSlice currentSlice = dendrogramSlicer.partitionByDistance(partitionDistance, dendrogram);
        layoutGenerator.setupLayout(currentSlice);

        draw();
    }
}
