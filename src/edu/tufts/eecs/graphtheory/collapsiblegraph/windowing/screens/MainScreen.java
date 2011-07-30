package edu.tufts.eecs.graphtheory.collapsiblegraph.windowing.screens;

import controlP5.Button;
import controlP5.ControlP5;
import controlP5.ControlWindow;
import edu.tufts.eecs.graphtheory.collapsiblegraph.windowing.ApplicationState;
import controlP5.Textfield;
import edu.tufts.eecs.graphtheory.collapsiblegraph.clustering.Dendrograms;
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
    private Button buildGraphButton;
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
       
        buildGraphButton = guiController.addButton("buildGraph", 1, 512, 500, 140, 30);
        buildGraphButton.setCaptionLabel("Build New Graph");
        buildGraphButton.hide(); 
        
        submitButton = guiController.addButton("submit", 2, 700, 300, 60, 20);
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
                buildGraphButton.show();
                guiController.draw();


                papplet.fill(255, 255, 255);
                papplet.text("Select a graph to work with:", 512, 200);
                break;
            }
                
            case SELECT_PREPROCESSED_GRAPH: {
                papplet.background(0);
                loadGraphButton.hide();
                buildGraphButton.hide();
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

    public Dendrograms processGraph() {
        Dendrograms theirDendrogram;
        try {
        FileInputStream theirFileStream = new FileInputStream(graphLocation.getText());
        ObjectInputStream theirDendrogramStream = new ObjectInputStream(theirFileStream);
        theirDendrogram = (Dendrograms)theirDendrogramStream.readObject();
        theirFileStream.close();
        theirDendrogramStream.close();
        
        graphLocation.hide();
        submitButton.hide();
        guiController.draw();
        
        } catch ( Exception e) {
            e.printStackTrace(System.err);
            return null;
        }
        
        mainScreenState = MainScreenState.INITIAL;
        return theirDendrogram;
    }
    private enum MainScreenState {

        INITIAL,
        SELECT_PREPROCESSED_GRAPH,
    }
}
