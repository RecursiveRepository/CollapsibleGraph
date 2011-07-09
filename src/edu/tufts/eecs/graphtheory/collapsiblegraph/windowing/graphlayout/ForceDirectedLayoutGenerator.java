package edu.tufts.eecs.graphtheory.collapsiblegraph.windowing.graphlayout;

import edu.tufts.eecs.graphtheory.collapsiblegraph.clustering.DendrogramEdge;
import edu.tufts.eecs.graphtheory.collapsiblegraph.clustering.DendrogramNode;
import edu.tufts.eecs.graphtheory.collapsiblegraph.viewing.DendrogramSlice;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

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
    
    private static final Random generator = new Random();
    
    public GraphLayout generateLayout(DendrogramSlice ds) {
         DendrogramNode[] nodeSet = ds.getVisibleDendrogramNodes();
        DendrogramEdge[] edgeSet = ds.getVisibleDendrogramEdges();
        
        List<BouncyNode> bouncyNodes = new ArrayList<BouncyNode>(); 
        
        for(DendrogramNode thisNode : nodeSet) {
            int radius = DIAMETER/2;
            int xCoord = generator.nextInt(MAX_X-radius) + radius;
            int yCoord = generator.nextInt(MAX_Y-radius) + radius;
            ViewableDendrogramNode newVDNode = new ViewableDendrogramNode(thisNode, DIAMETER, xCoord, yCoord );
            BouncyNode newBouncyNode = new BouncyNode(newVDNode);
            bouncyNodes.add(newBouncyNode);
        }
        
        List<ViewableDendrogramNode> vDNodes = new ArrayList<ViewableDendrogramNode>();
        List<ViewableDendrogramEdge> vDEdges = new ArrayList<ViewableDendrogramEdge>();
        
        for(BouncyNode bouncyNode : bouncyNodes) {
            vDNodes.add(bouncyNode.getVDNode());
        }
        
        GraphLayout newLayout = new GraphLayout(vDNodes, vDEdges);
        return newLayout;
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
} 
