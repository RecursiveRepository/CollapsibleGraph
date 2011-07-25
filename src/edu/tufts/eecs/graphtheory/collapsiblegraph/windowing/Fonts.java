package edu.tufts.eecs.graphtheory.collapsiblegraph.windowing;

import processing.core.PApplet;
import processing.core.PFont;

/**
 * A class to hold all the fonts so we don't need to needlessly create them
 * @author Jeremy
 */
public class Fonts {
    
    private static PFont consolas32;
    private static PFont consolas24;
    private static PFont consolas16;
    
    public static void initialize(PApplet papplet) {
        consolas32 = papplet.createFont("Consolas", 32, false);    
        consolas24 = papplet.createFont("Consolas", 24, false);
        consolas16 = papplet.createFont("Consolas", 16, false);
    }
    
    public static PFont getLargeFont() {
        return consolas32;
    }
    
    public static PFont getMediumFont() {
        return consolas24;
    }
    
    public static PFont getSmallFont() {
        return consolas16;
    }
}
