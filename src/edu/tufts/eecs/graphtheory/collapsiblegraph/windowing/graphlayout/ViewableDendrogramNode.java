package edu.tufts.eecs.graphtheory.collapsiblegraph.windowing.graphlayout;

import edu.tufts.eecs.graphtheory.collapsiblegraph.clustering.DendrogramNode;

/**
 *
 * @author Jeremy
 */
 public class ViewableDendrogramNode {

        private DendrogramNode dendrogramNode;
        private int diameter;
        private int xCoordinate;
        private int yCoordinate;
        private float xForce;
        private float yForce;
        private float xVelocity;
        private float yVelocity;

        public ViewableDendrogramNode(DendrogramNode dendrogramNode, int diameter, int xCoordinate, int yCoordinate) {
            this.dendrogramNode = dendrogramNode;
            this.diameter = diameter;
            this.xCoordinate = xCoordinate;
            this.yCoordinate = yCoordinate;
        }

        public DendrogramNode getDendrogramNode() {
            return dendrogramNode;
        }

        public int getDiameter() {
            return diameter;
        }

        public int getXCoordinate() {
            return xCoordinate;
        }

        public int getYCoordinate() {
            return yCoordinate;
        }
        
        public void setXCoordinate(int xCoordinate) {
            this.xCoordinate = xCoordinate;
        }

        public void setYCoordinate(int yCoordinate) {
            this.yCoordinate = yCoordinate;
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
