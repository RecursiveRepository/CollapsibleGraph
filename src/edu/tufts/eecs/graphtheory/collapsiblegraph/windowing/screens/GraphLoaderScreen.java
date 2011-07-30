/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.tufts.eecs.graphtheory.collapsiblegraph.windowing.screens;

import controlP5.ControlP5;
import processing.core.PApplet;

/**
 *
 * @author jeremy
 */
public class GraphLoaderScreen {

    private PApplet papplet;
    private ControlP5 guiController;
    private static GraphLoaderScreen instance;

    private GraphLoaderScreen(PApplet papplet, ControlP5 guiController) {
        this.papplet = papplet;
        this.guiController = guiController;
    }

    private void setup() {
        guiController.draw();
    }

    public static GraphLoaderScreen getGraphLoaderScreen(PApplet papplet, ControlP5 guiController) {
        instance = new GraphLoaderScreen(papplet, guiController);
        return instance;
    }
}
