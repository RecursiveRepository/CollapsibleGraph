package edu.tufts.eecs.graphtheory.collapsiblegraph.windowing.screens;

import controlP5.Button;
import controlP5.ControlP5;
import controlP5.ControlWindow;
import edu.tufts.eecs.graphtheory.collapsiblegraph.windowing.ApplicationState;
import controlP5.Textfield;
import edu.tufts.eecs.graphtheory.collapsiblegraph.clustering.Dendrogram;
import java.io.FileInputStream;
import java.io.ObjectInputStream;


import processing.core.PApplet;

/**
 *
 * @author Jeremy
 */
public class MainScreen {

    private PApplet papplet;
    private static MainScreen instance;
    private MainScreenState mainScreenState;
    private ControlP5 guiController;
    private ControlWindow controlWindow;
    private Button loadGraphButton;
    private Button submitButton;
    private Textfield graphLocation;

    private MainScreen(PApplet papplet, ControlP5 guiController) {
        this.papplet = papplet;
        this.guiController = guiController;
        mainScreenState = MainScreenState.INITIAL;

        this.setup();
    }

    private void setup() {
        loadGraphButton = guiController.addButton("loadGraph", 1, 256, 500, 140, 30);
        loadGraphButton.setCaptionLabel("Load Pre-Processed Graph");
        loadGraphButton.hide();
       
        submitButton = guiController.addButton("submit", 2, 580, 300, 60, 30);
        submitButton.setCaptionLabel("Submit");
        submitButton.hide(); 
        
        graphLocation = guiController.addTextfield("graphLocation", 256, 300, 400,20);
        graphLocation.hide();
    }

    public static MainScreen getMainScreen(PApplet papplet, ControlP5 guiController) {
        instance = new MainScreen(papplet, guiController);
        return instance;
    }

    public static MainScreen getMainScreen() {
        return instance;
    }

    public ApplicationState draw() {
        switch (mainScreenState) {
            case INITIAL: {
                papplet.background(0);
                loadGraphButton.show();
                guiController.draw();


                papplet.fill(255, 255, 255);
                papplet.text("Select a graph to work with:", 512, 200);
                break;
            }
                
            case SELECT_PREPROCESSED_GRAPH: {
                papplet.background(0);
                loadGraphButton.hide();
                graphLocation.show();
                graphLocation.setFocus(true);
                submitButton.show();
                
                guiController.draw();
                
                papplet.fill(255,255,255);
                papplet.text("Where is that graph???", 512, 200);
                
                break;
            }

        }
        return ApplicationState.MAIN_SCREEN;
    }

    public void loadGraph() {
        mainScreenState = MainScreenState.SELECT_PREPROCESSED_GRAPH;
    }

    public Dendrogram processGraph() {
        Dendrogram theirDendrogram;
        try {
        FileInputStream theirFileStream = new FileInputStream(graphLocation.getText());
        ObjectInputStream theirDendrogramStream = new ObjectInputStream(theirFileStream);
        theirDendrogram = (Dendrogram)theirDendrogramStream.readObject();
        theirFileStream.close();
        theirDendrogramStream.close();
        } catch ( Exception e) {
            e.printStackTrace(System.err);
            return null;
        }
        
        
        mainScreenState = MainScreenState.PROCESS_GRAPH;
        return theirDendrogram;
    }
    private enum MainScreenState {

        INITIAL,
        SELECT_PREPROCESSED_GRAPH,
        PROCESS_GRAPH;
    }
}
