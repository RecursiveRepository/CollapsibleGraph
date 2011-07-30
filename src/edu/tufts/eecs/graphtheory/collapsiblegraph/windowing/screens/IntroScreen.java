package edu.tufts.eecs.graphtheory.collapsiblegraph.windowing.screens;

import controlP5.ControlP5;
import edu.tufts.eecs.graphtheory.collapsiblegraph.CollapsibleGraphConstants;
import edu.tufts.eecs.graphtheory.collapsiblegraph.windowing.ApplicationState;
import edu.tufts.eecs.graphtheory.collapsiblegraph.windowing.Fonts;
import processing.core.PApplet;

/**
 *
 * @author Jeremy
 */
public class IntroScreen {
    
    private static IntroScreen instance;
    private PApplet papplet;
    
    private IntroScreen(PApplet papplet) {
        this.papplet = papplet;
    }
    
    public static IntroScreen getIntroScreen(PApplet papplet, ControlP5 guiController) {
        instance = new IntroScreen(papplet);
        return instance;
    }
    
    public static IntroScreen getIntroScreen() {
        return instance;
    }
    
    
    public ApplicationState draw() {
        if (papplet.mousePressed) {
            return ApplicationState.MAIN_SCREEN;
        }

        papplet.fill(255, 255, 255);
        papplet.textFont(Fonts.getLargeFont());
        papplet.textAlign(papplet.CENTER);
        papplet.text("Collapsible Graph System", 512, 150);
        papplet.text(CollapsibleGraphConstants.VERSION_NUMBER, 512, 180);

        
        papplet.textFont(Fonts.getSmallFont());
        papplet.text("Click anywhere to continue", 512, 550);
        return ApplicationState.INTRO_SCREEN;
    }
}
