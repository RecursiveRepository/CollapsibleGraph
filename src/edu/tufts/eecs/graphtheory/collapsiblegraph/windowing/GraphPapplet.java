package edu.tufts.eecs.graphtheory.collapsiblegraph.windowing;

import controlP5.ControlEvent;
import controlP5.ControlP5;
import edu.tufts.eecs.graphtheory.collapsiblegraph.windowing.screens.GraphRendererScreen;
import edu.tufts.eecs.graphtheory.collapsiblegraph.windowing.screens.IntroScreen;
import edu.tufts.eecs.graphtheory.collapsiblegraph.windowing.screens.MainScreen;
import processing.core.PApplet;

/**
 * The main class for running the application. Extends the processing PApplet class which gives it all of its graphical magic
 * @author Jeremy
 */
public class GraphPapplet extends PApplet {

    private ApplicationState currentState; //Which state the application is in
    private MainScreen mainScreen; //A link to the Main Screen
    private IntroScreen introScreen; //A link to the Reference screen
    private GraphRendererScreen graphRendererScreen; //A link to the Graph Renderer screen
    private ControlP5 guiController;
    
    /**
     * Initialize the application
     */
    @Override
    public void setup() {
        size(1024, 600);
        guiController = new ControlP5(this);
        guiController.setAutoDraw(false);

        Fonts.initialize(this);
        mainScreen = MainScreen.getMainScreen(this, guiController);
        introScreen = IntroScreen.getIntroScreen(this, guiController);
        graphRendererScreen = GraphRendererScreen.getGraphRendererScreen(this, guiController);

        currentState = ApplicationState.INTRO_SCREEN;

        background(0);
        smooth();
    }

    /**
     * The loop function that's called many times a second. It's been changed to a switch statement so that the screen corresponding
     * to the current application state may draw its own routine
     */
    @Override 
    public void draw() {
        switch (currentState) {
            case INTRO_SCREEN: {
                currentState = introScreen.draw();
                break;
            }

            case MAIN_SCREEN: {
                currentState = mainScreen.draw();
                break;
            }

            case GRAPH_RENDERER_SCREEN: {
                currentState = graphRendererScreen.draw();
                break;
            } 
                
        }
    }

    /**
     * This function responds to any kind of event-input, such as a button press or the slider moving.
     * @param theEvent 
     */
    public void controlEvent(ControlEvent theEvent) {
        String eventName = theEvent.controller().name();
        if (eventName.equals("loadGraph")) {
            mainScreen.loadGraph();
        }
        
        if (eventName.equals("buildGraph")) {
            currentState = ApplicationState.GRAPH_BUILDER_SCREEN;
        }

        if (eventName.equals("submit")) {
            graphRendererScreen.setDendrogram(mainScreen.processGraph());
            graphRendererScreen.redraw();
            graphRendererScreen.draw();
            currentState = ApplicationState.GRAPH_RENDERER_SCREEN;
        }

        if (eventName.equals("zoomButton")) {
            graphRendererScreen.setZoomLevel((double) theEvent.controller().value());
            graphRendererScreen.redraw();
        }
        
        if (eventName.equals("zoomInButton")) {
            System.out.println("Zoom pressed.");
            graphRendererScreen.zoomIn();
        }
        if (eventName.equals("zoomOutButton")) {
            System.out.println("Zoom pressed.");
            graphRendererScreen.zoomOut();
        }
    }

    public static void main(String[] argv) {
      PApplet.main(new String[]{"edu.tufts.eecs.graphtheory.collapsiblegraph.windowing.GraphPapplet"});
    }
}
