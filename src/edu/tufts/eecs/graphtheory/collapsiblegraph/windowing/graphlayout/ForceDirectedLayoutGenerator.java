package edu.tufts.eecs.graphtheory.collapsiblegraph.windowing.graphlayout;

import edu.tufts.eecs.graphtheory.collapsiblegraph.clustering.DendrogramEdge;
import edu.tufts.eecs.graphtheory.collapsiblegraph.clustering.DendrogramNode;
import edu.tufts.eecs.graphtheory.collapsiblegraph.viewing.DendrogramSlice;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 *
 * @author Jeremy
 */
public class ForceDirectedLayoutGenerator {
    
    private static int MIN_X=0;
    private static int MAX_X=1024;
    private static int MIN_Y=0;
    private static int MAX_Y=500;
    private static int DIAMETER=20;
    private static final float COULOMB_CONSTANT = 8987.00f;
    private static final float DAMPENING = .3f;
    
    private static final Random generator = new Random();
    private Map<DendrogramNode, ViewableDendrogramNode> nodeToViewable = new HashMap<DendrogramNode, ViewableDendrogramNode>();
    
    public GraphLayout generateLayout(DendrogramSlice ds) {
        nodeToViewable.clear();
        DendrogramNode[] nodeSet = ds.getVisibleDendrogramNodes();
        DendrogramEdge[] edgeSet = ds.getVisibleDendrogramEdges();
        
        List<BouncyNode> bouncyNodes = new ArrayList<BouncyNode>(); 
        
        for(DendrogramNode thisNode : nodeSet) {
            int radius = DIAMETER/2;
            int xCoord = generator.nextInt(MAX_X-radius) + radius;
            int yCoord = generator.nextInt(MAX_Y-radius) + radius;
            ViewableDendrogramNode newVDNode = new ViewableDendrogramNode(thisNode, DIAMETER, xCoord, yCoord );
            nodeToViewable.put(thisNode, newVDNode);
            BouncyNode newBouncyNode = new BouncyNode(newVDNode);
            bouncyNodes.add(newBouncyNode);
        }
        
        List<ViewableDendrogramNode> vDNodes = new ArrayList<ViewableDendrogramNode>();
        List<ViewableDendrogramEdge> vDEdges = new ArrayList<ViewableDendrogramEdge>();
        
        
        float kineticEnergy=1000000000f;
        float previousKineticEnergy=2*kineticEnergy;
        int iterations = 0;
        while(Math.abs(previousKineticEnergy-kineticEnergy)/kineticEnergy > .05f && iterations < 100) {
            previousKineticEnergy = kineticEnergy;
            kineticEnergy = 0f;
            for(BouncyNode bouncyNode : bouncyNodes) {
                bouncyNode.setXForce(0F);
                bouncyNode.setYForce(0F);
                
                for(BouncyNode otherBouncyNode : bouncyNodes) {
                    if(bouncyNode != otherBouncyNode) {
                        calculateColoumbRepulsion(bouncyNode, otherBouncyNode);
                    }
                }
                bouncyNode.setXVelocity(bouncyNode.getXVelocity()*DAMPENING + bouncyNode.getXForce());
                bouncyNode.setYVelocity(bouncyNode.getYVelocity()*DAMPENING + bouncyNode.getYForce());
            }
            
            
            for(BouncyNode bouncyNode : bouncyNodes) {
                int oldXCoordinate = bouncyNode.getVDNode().getXCoordinate();
                int oldYCoordinate = bouncyNode.getVDNode().getYCoordinate();
                float xVelocity = bouncyNode.getXVelocity();
                float yVelocity = bouncyNode.getYVelocity();
                
                int newYCoordinate=(int)(oldXCoordinate + xVelocity);;
                int newXCoordinate= (int)(oldYCoordinate + yVelocity);
                
                int radius = bouncyNode.getVDNode().getDiameter()/2;
                
                
                if(newXCoordinate == Integer.MAX_VALUE || newXCoordinate+radius > MAX_X ) {
                    newXCoordinate = MAX_X - radius;
                } else if(newXCoordinate == Integer.MIN_VALUE || newXCoordinate - radius < MIN_X) {
                    newXCoordinate = MIN_X + radius;
                }
                              
                if(newYCoordinate==Integer.MAX_VALUE || newYCoordinate + radius > MAX_Y) {
                    newYCoordinate = MAX_Y - radius;
                } else if(newYCoordinate==Integer.MIN_VALUE || newYCoordinate - radius < MIN_Y) {
                    newYCoordinate = MIN_Y + radius;
                }
                
                bouncyNode.getVDNode().setXCoordinate(newXCoordinate);
                bouncyNode.getVDNode().setYCoordinate(newYCoordinate);
                kineticEnergy += Math.abs(xVelocity) + Math.abs(yVelocity);
                iterations++;
            }
            
        }
        
        
        
        for(BouncyNode bouncyNode : bouncyNodes) {
            vDNodes.add(bouncyNode.getVDNode());
        }
        
        for(DendrogramEdge dEdge : edgeSet) {
            vDEdges.add(new ViewableDendrogramEdge(dEdge, nodeToViewable.get(dEdge.getSourceDendrogramNode()),
                                                    nodeToViewable.get(dEdge.getTargetDendrogramNode())));
        }
        
        
        GraphLayout newLayout = new GraphLayout(vDNodes, vDEdges);
        return newLayout;
    }
    
    private static void calculateColoumbRepulsion(BouncyNode nodeToChange, BouncyNode nodeToObserve) {
        float xDenominator = (float)Math.pow(nodeToChange.getVDNode().getXCoordinate() - nodeToObserve.getVDNode().getXCoordinate(), 2);
        float xForce = COULOMB_CONSTANT  / (float)Math.pow(nodeToChange.getVDNode().getXCoordinate() - nodeToObserve.getVDNode().getXCoordinate(), 2);
        float yDenominator = (float)Math.pow(nodeToChange.getVDNode().getYCoordinate() - nodeToObserve.getVDNode().getYCoordinate(), 2);
        float yForce = COULOMB_CONSTANT  / (float)Math.pow(nodeToChange.getVDNode().getYCoordinate() - nodeToObserve.getVDNode().getYCoordinate(), 2);
        if(nodeToChange.getVDNode().getXCoordinate() < nodeToObserve.getVDNode().getXCoordinate()) {
            xForce *= -1;
        }
        if(nodeToChange.getVDNode().getYCoordinate() < nodeToObserve.getVDNode().getYCoordinate() ) {
            yForce *= -1;
        }
        nodeToChange.setXForce(nodeToChange.getXForce() + xForce);
        nodeToChange.setYForce(nodeToChange.getYForce() + yForce);
    }
    
    private static void calculateHookeAttraction(ViewableDendrogramEdge vde) {
        
    }
    
    private static class BouncyNode {
        private ViewableDendrogramNode vDNode;
        private float xForce;
        private float yForce;
        private float xVelocity;
        private float yVelocity;
        
        public BouncyNode(ViewableDendrogramNode vDNode) {
            this.vDNode = vDNode;
            xForce=0.0f;
            yForce=0.0f;
            xVelocity=0.0f;
            yVelocity=0.0f;
        }
        
        public ViewableDendrogramNode getVDNode() {
            return vDNode;
        }
        
        public void setXForce(float xForce) {
            this.xForce = xForce;
        }
        
        public float getXForce() {
            return xForce;
        }
        
        public void setYForce(float yForce) {
            this.yForce = yForce;
        }
        
        public float getYForce() {
            return yForce;
        }
        
        public void setXVelocity(float xVelocity) {
            this.xVelocity = xVelocity;
        }
        
        public float getXVelocity() {
            return xVelocity;
        }
        
        public void setYVelocity(float yVelocity) {
            this.yVelocity = yVelocity;
        }
        
        public float getYVelocity() {
            return yVelocity;
        }
    }
    
    private static float calculateDistance(ViewableDendrogramNode node1, ViewableDendrogramNode node2) {
        return (float)Math.sqrt(Math.pow(node1.getXCoordinate()-node2.getXCoordinate(),2) + Math.pow(node1.getYCoordinate()-node2.getYCoordinate(),2));
    }
} 
