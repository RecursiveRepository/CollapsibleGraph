package edu.tufts.eecs.graphtheory.collapsiblegraph.windowing.screens;

import controlP5.ControlP5;
import processing.core.PApplet;

/**
 *
 * @author Jeremy
 */
public class GraphRendererScreen {

    private static GraphRendererScreen instance;
    PApplet papplet;
    ControlP5 guiController;

    private GraphRendererScreen(PApplet papplet, ControlP5 guiController) {
        this.papplet = papplet;
        this.guiController = guiController;
    }
    
    public static GraphRendererScreen getGraphRendererScreen(PApplet papplet, ControlP5 guiController) {
        instance = new GraphRendererScreen(papplet, guiController);
        return instance;
    }
    
    public static GraphRendererScreen getGraphRendererScreen() {
        return instance;
    }
}
