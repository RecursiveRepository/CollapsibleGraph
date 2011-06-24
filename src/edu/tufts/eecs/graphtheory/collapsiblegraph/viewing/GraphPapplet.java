package edu.tufts.eecs.graphtheory.collapsiblegraph.viewing;

import processing.core.PApplet;
import processing.core.PFont;

/**
 *
 * @author Jeremy
 */
public class GraphPapplet extends PApplet{
 
    private int i = 0;
    @Override 
    public void setup() {
        size(800, 600);
        background(0);
        loop();
    }
    
    @Override
    public void draw() {
        stroke(255, 0, 128);
        fill(255, 126, 0);
        ellipse(100,100,100,100);
        PFont font = createFont("Consolas", 12F);
        textFont(font, 12F);
        text("Hello, Motherfuckers!", ((450+i)%800), 450);
        i++;
        
    }
    
    public static void main(String[] argv) {
        PApplet.main(new String[] { "edu.tufts.eecs.graphtheory.collapsiblegraph.viewing.GraphPapplet" });
        String[] fontNames = PFont.list();
        for(String fontName : fontNames) {
            System.out.println(fontName);
        }
    }
}
